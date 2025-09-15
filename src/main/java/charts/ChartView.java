package charts;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import Reservation_Page.Booking;
import Reservation_Page.BookingData;
import loginController.Helper;

@ManagedBean(name = "chartView")
@ViewScoped
public class ChartView {

    private PieChartModel pieModel;
    private BarChartModel barChart;

    @PostConstruct
    public void init() {
        buildPie();
        createBarChart();
    }

    private void buildPie() {
        pieModel = new PieChartModel();
        BookingData bookingData = Helper.getBean("bookingData", BookingData.class);
        ArrayList<Booking> bookings = bookingData.getBookings();

        if (bookings == null || bookings.isEmpty()) {
            return;
        }

        ChartData data = calculateRevenueByType(bookings);
        for (int i = 0; i < data.labels.size(); i++) {
            pieModel.set(data.labels.get(i), data.values.get(i));
        }

        pieModel.setTitle("Revenue by Workspace");
        pieModel.setLegendPosition("w");
        pieModel.setShowDataLabels(true);
    }

    public void createBarChart() {
        barChart = new BarChartModel();
        BookingData bookingData = Helper.getBean("bookingData", BookingData.class);
        ArrayList<Booking> bookings = bookingData.getBookings();

        if (bookings == null || bookings.isEmpty()) {
            return;
        }

        ChartData data = calculateHoursByType(bookings);
        for (int i = 0; i < data.labels.size(); i++) {
            ChartSeries series = new ChartSeries();
            series.setLabel(data.labels.get(i));
            series.set(" ", data.values.get(i));
            barChart.addSeries(series);
        }

        barChart.setTitle("Total Hours Booked per Workspace");
        barChart.setLegendPosition("ne");
        barChart.setAnimate(true);
        barChart.setSeriesColors("138496,c87f0a,c9a40c,117864,6b8e23");
    }

    public PieChartModel getPieModel() { return pieModel; }
    public BarChartModel getBarModel() {
        if (barChart == null) {
            createBarChart();
        }
        return barChart;
    }

    // === Refactored static methods and data structure ===

    public static class ChartData {
        public List<String> labels = new ArrayList<>();
        public List<Double> values = new ArrayList<>();
    }

    public static ChartData calculateRevenueByType(List<Booking> bookings) {
        ChartData data = new ChartData();

        for (Booking b : bookings) {
            if (b == null || b.getWorkspace() == null || b.getWorkspace().getRoomType() == null) continue;

            String type = b.getWorkspace().getRoomType();
            double value = b.getTotalPrice();

            int index = data.labels.indexOf(type);
            if (index == -1) {
                data.labels.add(type);
                data.values.add(value);
            } else {
                data.values.set(index, data.values.get(index) + value);
            }
        }

        return data;
    }

    public static ChartData calculateHoursByType(List<Booking> bookings) {
        ChartData data = new ChartData();

        for (Booking b : bookings) {
            if (b == null || b.getWorkspace() == null || b.getWorkspace().getRoomType() == null) continue;
            if (b.getStartDateTime() == null || b.getEndDateTime() == null) continue;

            String type = b.getWorkspace().getRoomType();
            double hours = (b.getEndDateTime().getTime() - b.getStartDateTime().getTime()) / (1000.0 * 60.0 * 60.0);
            if (hours < 0) hours = 0;

            int idx = data.labels.indexOf(type);
            if (idx == -1) {
                data.labels.add(type);
                data.values.add(hours);
            } else {
                data.values.set(idx, data.values.get(idx) + hours);
            }
        }

        return data;
    }
}
