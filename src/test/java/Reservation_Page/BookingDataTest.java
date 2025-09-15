package Reservation_Page;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adminCRUD.Workspace;
import adminCRUD.WorkspaceData;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

class BookingDataTest {

	private BookingData bookingData;
	private WorkspaceData workspaceData;
	private Workspace testWorkspace;

	private Date invalidStart;
	private Date invalidEnd;

	private Date validStart;
	private Date validEnd;

	@BeforeEach
	public void setUp() {
		// Invalid Dates
		LocalDateTime manualStartDateTime = LocalDateTime.of(2025, 8, 21, 9, 0);
		LocalDateTime endDateTime = manualStartDateTime.plusHours(3);
		invalidStart = Date.from(manualStartDateTime.atZone(ZoneId.systemDefault()).toInstant());
		invalidEnd = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		//Valid Dates
		LocalDateTime start = LocalDateTime.of(2026, 1, 1, 9, 0);
		LocalDateTime end = start.plusHours(2);
		validStart = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
		validEnd = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

		// Setting up WorkspaceData
		workspaceData = new WorkspaceData();
		bookingData = new BookingData();
		bookingData.setWorkspaceData(workspaceData);
		testWorkspace = workspaceData.getWorkspaces().get(0); // TestWorkspace set to first workspace
		Booking.resetBookingId();								  // Reset the ID counter for each test
		bookingData.init();
	}

	@Test
	void testInitCreatesDummyBookings() {
		assertEquals(5, bookingData.getBookings().size()); 
	}

	@Test
	void testNumOfBookings() {
		assertEquals(5, bookingData.numOfBookings());
	}

	@Test
	void testGetBookings() {
		ArrayList<Booking> bookings = bookingData.getBookings();
		assertNotNull(bookings);
		assertEquals(5, bookings.size());
	}

	@Test
	void testIsWorkspaceAvailable_Success() {
		assertTrue(bookingData.isWorkspaceAvailable(testWorkspace, validStart, validEnd));
	}

	@Test
	void testIsWorkspaceAvailable_Fail_NullDates() {
		assertFalse(bookingData.isWorkspaceAvailable(testWorkspace, null, null));
	}

	@Test
	void testIsWorkspaceAvailable_Fail_EndDateBefore() {
		assertFalse(bookingData.isWorkspaceAvailable(testWorkspace, invalidEnd, invalidStart));
	}

	@Test
	void testIsWorkspaceAvailable_Fail_Overlap() {
		assertFalse(bookingData.isWorkspaceAvailable(testWorkspace, invalidStart, invalidEnd));
	}

	@Test
	void testAddBooking() {
		Booking newBooking = new Booking("member1", testWorkspace, validStart, validEnd, 230.0);
		boolean added = bookingData.addBooking(newBooking);

		assertTrue(added);
		assertEquals(6, bookingData.numOfBookings());
	}
    
	@Test 
	void testAddBooking_Fail_Overlap() {
		Booking overlappingBooking = new Booking("member2", testWorkspace, invalidStart, invalidEnd, 230.0);
		boolean added = bookingData.addBooking(overlappingBooking);
		
		assertFalse(added); 
		assertEquals(5, bookingData.numOfBookings()); 
	}

	@Test
	void testGetBookingById() {
		Booking foundBooking = bookingData.getById(2);
		assertNotNull(foundBooking);
		assertEquals(2, foundBooking.getBookingId());
	}

	@Test
	void testGetBookingById_Fail() {
		assertNull(bookingData.getById(999));
	}
	
	@Test
    void testGetBookingsForUsername() {
        List<Booking> memberBookings = bookingData.getBookingsForUsername("member");
        assertNotNull(memberBookings);
        assertEquals(5, memberBookings.size());

        List<Booking> noBookings = bookingData.getBookingsForUsername("non_existent_user");
        assertNotNull(noBookings);
        assertTrue(noBookings.isEmpty());
    }

    @Test
    void testGetBookingsForWorkspace() {
        List<Booking> workspaceBookings = bookingData.getBookingsForWorkspace(testWorkspace);
        assertNotNull(workspaceBookings);
        assertEquals(1, workspaceBookings.size());
        assertEquals(testWorkspace.getRoomID(), workspaceBookings.get(0).getWorkspace().getRoomID());

        Workspace unbookedWorkspace = new Workspace("Unbooked Room", "Test", "Test", 1, 100.0);
        List<Booking> noBookings = bookingData.getBookingsForWorkspace(unbookedWorkspace);
        assertNotNull(noBookings);
        assertTrue(noBookings.isEmpty());
    }
    
	@Test 
	void testRemoveBooking() {
		assertEquals(5, bookingData.numOfBookings());
		
		bookingData.removeBooking(3); 
		
		assertEquals(4, bookingData.numOfBookings());
		assertNull(bookingData.getById(3)); 
	}
	
	@Test
	void testRemoveBooking_NonExistent() {
		assertEquals(5, bookingData.numOfBookings());
		
		bookingData.removeBooking(999); 
		
		assertEquals(5, bookingData.numOfBookings()); 
	}
    
	@Test 
	void testToLdt_NullInput() {
		assertNull(BookingData.toLdt(null));
	}
}