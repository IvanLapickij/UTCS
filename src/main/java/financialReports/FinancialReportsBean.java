package financialReports;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import Reservation_Page.Booking;
import Reservation_Page.BookingData;
import loginController.Helper;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@ManagedBean(name = "financialReportsBean")
@RequestScoped
public class FinancialReportsBean {

    private BookingData bookingData;

    // Default constructor (for JSF)
    public FinancialReportsBean() {
        this.bookingData = Helper.getBean("bookingData", BookingData.class);
    }

    // Constructor for testing
    public FinancialReportsBean(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    public double getTotalRevenue() {
        ArrayList<Booking> bookings = bookingData.getBookings();

        double sum = 0.0;
        for (Booking b : bookings) {
            sum += b.getTotalPrice();
        }
        return sum;
    }

    public String monthsLabel(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);
        Date date = calendar.getTime();
        return new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(date);
    }

    public double revenuePerMonth(String label) {
        ArrayList<Booking> month = bookingData.getBookings();
        if (month == null || month.isEmpty()) return 0.0;

        SimpleDateFormat fmt = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        double sum = 0.0;

        for (Booking b : month) {
            if (b == null || b.getStartDateTime() == null) continue;
            String months = fmt.format(b.getStartDateTime());
            if (label.equalsIgnoreCase(months)) {
                sum += b.getTotalPrice();
            }
        }
        return sum;
    }
}
