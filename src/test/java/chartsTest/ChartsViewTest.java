package chartsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import Reservation_Page.Booking;
import adminCRUD.Workspace;
import charts.ChartView;
import charts.ChartView.ChartData;

class ChartsViewTest {

	// Test total revenue grouped by workspace type
	@Test
	void testCalculateRevenueByType() {
	Workspace ws1 = new Workspace();
	ws1.setRoomType("Event Area");

	Workspace ws2 = new Workspace();
	ws2.setRoomType("Conference Room");

	Booking b1 = new Booking("user1", ws1, new Date(), new Date(), 100.0);
	Booking b2 = new Booking("user2", ws1, new Date(), new Date(), 200.0);
	Booking b3 = new Booking("user3", ws2, new Date(), new Date(), 50.0);

	List<Booking> bookings = Arrays.asList(b1, b2, b3);

	ChartData data = ChartView.calculateRevenueByType(bookings);

	assertEquals(2, data.labels.size());
	assertTrue(data.labels.contains("Event Area"));
	assertTrue(data.labels.contains("Conference Room"));

	int privateIdx = data.labels.indexOf("Event Area");
	int sharedIdx = data.labels.indexOf("Conference Room");

	assertEquals(300.0, data.values.get(privateIdx), 0.01);
	assertEquals(50.0, data.values.get(sharedIdx), 0.01);
	
	}

	// Test total hours booked grouped by workspace type
	@Test
	void testCalculateHoursByType() {
	Workspace ws = new Workspace();
	ws.setRoomType("Meeting Pod");

	Calendar cal = Calendar.getInstance();
	Date start = cal.getTime();
	Date end = new Date(start.getTime() + 2 * 60 * 60 * 1000); // 2 hours later

	Booking b1 = new Booking("user1", ws, start, end, 0.0);
	Booking b2 = new Booking("user2", ws, start, end, 0.0);

	ChartData data = ChartView.calculateHoursByType(List.of(b1, b2));

	assertEquals(1, data.labels.size());
	assertEquals("Meeting Pod", data.labels.get(0));
	assertEquals(4.0, data.values.get(0), 0.01); // 2h + 2h
	
	}

	// Test that invalid bookings (null or missing data) are ignored
	@Test
	void testRevenueByTypeWithInvalidBookings() {
	List<Booking> bookings = new ArrayList<>();
	bookings.add(null); // null booking
	bookings.add(new Booking("user", null, new Date(), new Date(), 100.0)); // null workspace

	ChartData data = ChartView.calculateRevenueByType(bookings);

	assertTrue(data.labels.isEmpty());
	assertTrue(data.values.isEmpty());
	
	}

	// Test that bookings with null dates are ignored in hour calculations
	@Test
	void testHoursByTypeWithNullDates() {
	Workspace ws = new Workspace();
	ws.setRoomType("Meeting Pod");

	Booking booking = new Booking("user", ws, null, null, 0.0);

	ChartData data = ChartView.calculateHoursByType(List.of(booking));

	assertTrue(data.labels.isEmpty());
	assertTrue(data.values.isEmpty());
	
	}

	// Test that negative durations are treated as 0 hours in hour calculations
	@Test
	void testHoursByTypeWithNegativeDuration() {
	Workspace ws = new Workspace();
	ws.setRoomType("Open Desk Area");

	Date end = new Date();
	Date start = new Date(end.getTime() + 10000); // start is after end

	Booking b = new Booking("user", ws, start, end, 0.0);

	ChartData data = ChartView.calculateHoursByType(List.of(b));

	assertEquals(1, data.labels.size());
	assertEquals("Open Desk Area", data.labels.get(0));
	assertEquals(0.0, data.values.get(0));
	
	}
	
}
