package Reservation_Page;

import adminCRUD.Workspace;
import adminCRUD.WorkspaceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BookingBeanTest {

	private BookingBean bookingBean;
	private BookingData bookingData;
	private WorkspaceData workspaceData;
	private Workspace testWorkspace;
	private int testWorkspaceId;
	private Calendar cal;

	@BeforeEach
	public void setUp() {
		bookingBean = new BookingBean();
		workspaceData = new WorkspaceData();
		bookingData = new BookingData();
		bookingData.setWorkspaceData(workspaceData);
		testWorkspaceId = 0;
		testWorkspace = workspaceData.getWorkspaces().get(testWorkspaceId); 	// TestWorkspace set to first workspace
		Booking.resetBookingId();								// Reset the ID counter for each test
		bookingData.init();
		cal = Calendar.getInstance();
	}

	@Test
	public void testGetToday() {
		Date today = bookingBean.getToday();

		cal.setTime(today);

		assertEquals(Calendar.getInstance().get(Calendar.YEAR), cal.get(Calendar.YEAR));
		assertEquals(Calendar.getInstance().get(Calendar.MONTH), cal.get(Calendar.MONTH));
		assertEquals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
	}

	@Test
	public void testRoundToNextFullHour() {
		cal.set(Calendar.MINUTE, 45);
		cal.set(Calendar.SECOND, 30);
		cal.set(Calendar.MILLISECOND, 0);

		Date input = cal.getTime();
		Date rounded = bookingBean.roundToNextFullHour(input);

		cal.add(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		assertEquals(cal.getTime(), rounded);
	}


	@Test
	public void testFullWorkDay() {
		cal.set(2025, Calendar.AUGUST, 18, 9, 0); // Monday 9 AM
		Date start = cal.getTime();
		cal.set(2025, Calendar.AUGUST, 18, 17, 0); // Monday 5 PM
		Date end = cal.getTime();

		long hours = bookingBean.billableHours(start, end);
		assertEquals(8, hours);
	}

	@Test
	public void testPartialDay() {
		cal.set(2025, Calendar.AUGUST, 18, 13, 0); // Monday 1 PM
		Date start = cal.getTime();
		cal.set(2025, Calendar.AUGUST, 18, 17, 0); // Monday 5 PM
		Date end = cal.getTime();

		long hours = bookingBean.billableHours(start, end);
		assertEquals(4, hours);
	}

	@Test
	public void testWeekendExcluded() {
		cal.set(2025, Calendar.AUGUST, 16, 9, 0); // Saturday 9 AM
		Date start = cal.getTime();
		cal.set(2025, Calendar.AUGUST, 17, 17, 0); // Sunday 5 PM
		Date end = cal.getTime();

		long hours = bookingBean.billableHours(start, end);
		// Saturday should count, Sunday should not
		assertEquals(8, hours);
	}

	@Test
	public void testNonWorkingHoursExcluded() {
		cal.set(2025, Calendar.AUGUST, 18, 6, 0); // Monday 6 AM
		Date start = cal.getTime();
		cal.set(2025, Calendar.AUGUST, 18, 10, 0); // Monday 10 AM
		Date end = cal.getTime();

		long hours = bookingBean.billableHours(start, end);
		// Only 9-10 AM counts
		assertEquals(1, hours);
	}

	@Test
	public void testCrossMultipleDays() {

		cal.set(2025, Calendar.AUGUST, 18, 15, 0); // Monday 3 PM
		Date start = cal.getTime();
		cal.set(2025, Calendar.AUGUST, 19, 11, 0); // Tuesday 11 AM
		Date end = cal.getTime();

		long hours = bookingBean.billableHours(start, end);
		// Monday: 3–5 PM → 2 hours, Tuesday: 9–11 AM → 2 hours
		assertEquals(4, hours);
	}

	// recalcSubTotal Calculations
	@Test
	void recalcSubTotal_whenWorkspaceIsNull() {
		bookingBean.setEndDateTime(null);
		bookingBean.setStartDateTime(null);
		bookingBean.recalcSubTotal(null);

		assertEquals(0, bookingBean.getSubTotal());
		assertEquals(0, bookingBean.getTotalAmount());
	}

	@Test
	void recalcSubTotal_whenRoomIdIsNull() {
		bookingBean.setSelectedRoomId(null);
		bookingBean.recalcSubTotal(testWorkspace);

		assertEquals(0, bookingBean.getSubTotal());
		assertEquals(0, bookingBean.getTotalAmount());
	}

	@Test
	void recalcSubTotal_whenStartDateIsNull() {
		LocalDateTime endTime = LocalDateTime.of(2026, 1, 1, 9, 0);
		Date end = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

		bookingBean.setStartDateTime(null);
		bookingBean.setEndDateTime(end);
		bookingBean.recalcSubTotal(testWorkspace);

		assertEquals(0, bookingBean.getSubTotal());
		assertEquals(0, bookingBean.getTotalAmount());
	}

	@Test
	void recalcSubTotal_whenEndDateIsNull() {
		LocalDateTime startTime = LocalDateTime.of(2026, 1, 1, 9, 0);
		Date start = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());

		bookingBean.setStartDateTime(start);
		bookingBean.setEndDateTime(null);
		bookingBean.recalcSubTotal(testWorkspace);

		assertEquals(0, bookingBean.getSubTotal());
		assertEquals(0, bookingBean.getTotalAmount());
	}

	@Test
	void recalcSubTotal_whenEndDateNotAfterStartDate() {
		bookingBean.setSelectedRoomId(testWorkspaceId);

		LocalDateTime endBefore = LocalDateTime.of(2026, 1, 1, 9, 0);
		LocalDateTime startAfter = endBefore.plusHours(2);
		Date start = Date.from(endBefore.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(startAfter.atZone(ZoneId.systemDefault()).toInstant());

		bookingBean.setStartDateTime(end);
		bookingBean.setEndDateTime(start); 		// End is before start

		bookingBean.recalcSubTotal(testWorkspace);

		assertEquals(0, bookingBean.getSubTotal(), "Subtotal should be 0");
		assertEquals(0, bookingBean.getTotalAmount(), "Total amount should be 0");
	}

	@Test
	void recalcSubTotal_forTwoHourWeekdayBooking() {
		bookingBean.setSelectedRoomId(testWorkspaceId);
		// A Thursday from 10:00 to 12:00 (2 billable hours)
		LocalDateTime startTime = LocalDateTime.of(2025, 8, 8, 10, 0);
		LocalDateTime endTime = startTime.plusHours(2);
		Date start = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

		bookingBean.setStartDateTime(start);
		bookingBean.setEndDateTime(end);
		bookingBean.recalcSubTotal(testWorkspace);

		// Assert
		double expectedSubTotal = 30.0; // 15.0 * 2
		double expectedTax = expectedSubTotal * .23; // 6.9
		double expectedTotal = expectedSubTotal + expectedTax; // 36.9

		assertEquals(expectedSubTotal, bookingBean.getSubTotal());
		assertEquals(expectedTax, bookingBean.getTax());
		assertEquals(expectedTotal, bookingBean.getTotalAmount());
	}

	@Test
	void recalcSubTotal_forBookingSpanningNonBillableHours() {
		bookingBean.setSelectedRoomId(testWorkspaceId);

		LocalDateTime startTime = LocalDateTime.of(2025, 8, 8, 18, 0);
		LocalDateTime endTime = startTime.plusHours(2);
		Date start = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

		bookingBean.setStartDateTime(start);
		bookingBean.setEndDateTime(end);
		bookingBean.recalcSubTotal(testWorkspace);

		// Assert
		assertEquals(0, bookingBean.getSubTotal());
		assertEquals(0, bookingBean.getTotalAmount());
	}

	// 3. Simple getters/setters and tax logic tests
	@Test
	public void testSelectedRoomIdGetterSetter() {
		Integer roomId = 5;
		bookingBean.setSelectedRoomId(roomId);
		assertEquals(roomId, bookingBean.getSelectedRoomId());
	}

	@Test
	public void testStartDateTimeGetterSetter() {
		Date now = new Date();
		bookingBean.setStartDateTime(now);
		assertEquals(now, bookingBean.getStartDateTime());
	}

	@Test
	public void testEndDateTimeGetterSetter() {
		Date future = new Date(System.currentTimeMillis() + 3600000); // +1 hour
		bookingBean.setEndDateTime(future);
		assertEquals(future, bookingBean.getEndDateTime());
	}

	@Test
	public void testSubTotalGetterSetter() {
		double subTotal = 100.0;
		bookingBean.setSubTotal(subTotal);
		assertEquals(subTotal, bookingBean.getSubTotal());
	}

	@Test
	public void testTaxGetterAfterSettingSubTotal() {
		bookingBean.setSubTotal(100.0);  // 23% tax should be 23.0
		assertEquals(23.0, bookingBean.getTax(), 0.001);
	}

	@Test
	public void testTaxSetterDoesNotAffectGetter() {
		bookingBean.setSubTotal(200.0);
		bookingBean.setTax(0);  // should be ignored, getTax() always recalculates
		assertEquals(46.0, bookingBean.getTax(), 0.001);  // 23% of 200 = 46.0
	}

	@Test
	public void testTotalAmountGetterSetter() {
		double amount = 123.45;
		bookingBean.setTotalAmount(amount);
		assertEquals(amount, bookingBean.getTotalAmount());
	}

	@Test
	public void testBookingIdGetter() {
		assertEquals(-1, bookingBean.getBookingId());
	}

	@Test
	public void testResetFields() {
		bookingBean.setSelectedRoomId(123);
		bookingBean.setStartDateTime(new Date());
		bookingBean.setEndDateTime(new Date());
		bookingBean.setSubTotal(100.0);
		bookingBean.setTotalAmount(200.0);
		bookingBean.setTax(30.0);

		bookingBean.resetFields();

		assertNull(bookingBean.getSelectedRoomId());
		assertNull(bookingBean.getStartDateTime());
		assertNull(bookingBean.getEndDateTime());
		assertEquals(0.0, bookingBean.getSubTotal());
		assertEquals(0.0, bookingBean.getTotalAmount());
		assertEquals(0.0, bookingBean.getTax());
	}
}
