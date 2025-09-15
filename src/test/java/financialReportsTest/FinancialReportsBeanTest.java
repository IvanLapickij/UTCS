package financialReportsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Reservation_Page.Booking;
import Reservation_Page.BookingData;
import adminCRUD.Workspace;
import financialReports.FinancialReportsBean;

class FinancialReportsBeanTest {
	
	BookingData bookingData;
	FinancialReportsBean bean;

	@BeforeEach
	void setUp() {
		bookingData = new BookingData();
		bean = new FinancialReportsBean(bookingData);
	}

	// Test the total revenue for multiple bookings
	@Test
	void testTotalRevenueWithBookings() {
		Workspace workspace = new Workspace();
	    workspace.setRoomID(1);
	    workspace.setName("Room A");

	    Date start1 = new Date(System.currentTimeMillis() + 1000);
	    Date end1 = new Date(start1.getTime() + 1000 * 60 * 60); // +1hr

	    Date start2 = new Date(end1.getTime() + 1000); // start after b1 ends
	    Date end2 = new Date(start2.getTime() + 1000 * 60 * 60); // +1hr

	    Booking b1 = new Booking("user1", workspace, start1, end1, 100.0);
	    Booking b2 = new Booking("user2", workspace, start2, end2, 200.0);

	    assertTrue(bookingData.addBooking(b1));
	    assertTrue(bookingData.addBooking(b2));

	    double result = bean.getTotalRevenue();
	    assertEquals(300.0, result, 0.01);
	}

	// Tests the revenue is correctly calculated for bookings in August of 2025
	@Test
	void testRevenuePerMonth() {
		Workspace workspace = new Workspace();
	    workspace.setRoomID(1);
	    workspace.setName("Room A");

	    Calendar cal = Calendar.getInstance();
	    cal.set(2025, Calendar.AUGUST, 21);
	    Date start = cal.getTime();
	    Date end = new Date(start.getTime() + 1000 * 60 * 60);
	    
	    Booking booking = new Booking("user1", workspace, start, end, 150.0);
	    bookingData.addBooking(booking);
	    
	    FinancialReportsBean bean = new FinancialReportsBean(bookingData);

	    String label = "Aug 2025";
	    
	    double result = bean.getTotalRevenue();
	    assertEquals(150.0, result, 0.01);
	}	
	
	// Tests that the revenue isn't counted for a label doesn't match a booking month
	@Test
	void testRevenuePerMonth_withNoMatchingMonth() {
	    Workspace workspace = new Workspace();
	    workspace.setRoomID(1);
	    workspace.setName("Room A");

	    // Booking in July 2025
	    Calendar cal = Calendar.getInstance();
	    cal.set(2025, Calendar.JULY, 15); // 0-based month
	    Date start = cal.getTime();
	    Date end = new Date(start.getTime() + 1000 * 60 * 60);

	    Booking booking = new Booking("user1", workspace, start, end, 120.0);
	    bookingData.addBooking(booking);

	    String label = "Aug 2025"; // Does not match
	    double result = bean.revenuePerMonth(label);

	    assertEquals(0.0, result, 0.01);
	}

	// Test that a booking with a null start date is skipped and does not affect revenue
	@Test
	void testRevenuePerMonth_withNullStartDate() {
	    Workspace workspace = new Workspace();
	    workspace.setRoomID(2);
	    workspace.setName("Room B");

	    Booking booking = new Booking("user2", workspace, null, null, 200.0);
	    bookingData.addBooking(booking);

	    String label = "Aug 2025"; // Irrelevant, date is null
	    double result = bean.revenuePerMonth(label);

	    assertEquals(0.0, result, 0.01);
	}

	// Test that an empty booking list returns 0 revenue
	@Test
	void testRevenuePerMonth_withEmptyBookingList() {
	    // No bookings added at all
	    String label = "Aug 2025";
	    double result = bean.revenuePerMonth(label);

	    assertEquals(0.0, result, 0.01);
	}

	// Test that the monthsLabel format always returns "MMM yyyy"
	@Test
	void testMonthsLabel_formatIsCorrect() {
	    String label = bean.monthsLabel(0); // current month
	    assertTrue(label.matches("^[A-Z][a-z]{2} \\d{4}$")); // e.g., "Aug 2025"
	}

	// Test that monthsLabel returns the correct label for +1 month offset
	@Test
	void testMonthsLabel_returnsExpectedValue() {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, 1);

	    String expected = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.ENGLISH)
	                            .format(cal.getTime());

	    String result = bean.monthsLabel(1);
	    assertEquals(expected, result);
	}
	
	// Test that monthsLabel returns correct label for negative offset (e.g. -2 months)
	@Test
	void testMonthsLabel_negativeOffset() {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, -2);

	    String expected = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.ENGLISH)
	                            .format(cal.getTime());

	    String result = bean.monthsLabel(-2);
	    assertEquals(expected, result);
	}
	
	// Test that passing null as month label returns 0 revenue
	@Test
	void testRevenuePerMonth_withNullLabel() {
	    double result = bean.revenuePerMonth(null);
	    assertEquals(0.0, result);
	}
 
	// Test that passing an empty string as label returns 0 revenue
	@Test
	void testRevenuePerMonth_withEmptyLabel() {
	    double result = bean.revenuePerMonth("");
	    assertEquals(0.0, result);
	}
	
	// Test that null bookings in the list are skipped without causing errors
	@Test
	void testRevenuePerMonth_withNullBookingInList() {
	    bookingData.getBookings().add(null); // add a null booking

	    String label = bean.monthsLabel(0);
	    double result = bean.revenuePerMonth(label);

	    assertEquals(0.0, result); // null should be skipped
	}


}
