package Reservation_Page;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adminCRUD.Workspace;

public class BookingTest {

    private Booking booking;
    private Workspace workspace;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        workspace = new Workspace(
            "Test Workspace",
            "Description",
            "Open Desk",
            10,
            50.0
        );
        workspace.setRoomID(101); // optional

        startDate = new Date(System.currentTimeMillis());
        endDate = new Date(System.currentTimeMillis() + 3600000); // +1 hour

        booking = new Booking("testUser", workspace, startDate, endDate, 75.0);
    }

    @Test
    void testConstructorAndGetters() {
        assertNotNull(booking);
        assertEquals("testUser", booking.getMemberUsername());
        assertEquals(workspace, booking.getWorkspace());
        assertEquals(startDate, booking.getStartDateTime());
        assertEquals(endDate, booking.getEndDateTime());
        assertEquals(75.0, booking.getTotalPrice());
        assertEquals("CONFIRMED", booking.getStatus());
//        assertNull(booking.getPaypalTransactionId());
    }

    @Test
    void testSetters() {
        booking.setMember("newUser");
        assertEquals("newUser", booking.getMemberUsername());

        Workspace newWorkspace = new Workspace(
            "New Workspace",
            "New Description",
            "Private Room",
            5,
            60.0
        );
        newWorkspace.setRoomID(102);
        booking.setWorkspace(newWorkspace);
        assertEquals(newWorkspace, booking.getWorkspace());

        Date newStart = new Date(System.currentTimeMillis() + 10000);
        booking.setStartDateTime(newStart);
        assertEquals(newStart, booking.getStartDateTime());

        Date newEnd = new Date(System.currentTimeMillis() + 7200000);
        booking.setEndDateTime(newEnd);
        assertEquals(newEnd, booking.getEndDateTime());

        booking.setTotalPrice(120.0);
        assertEquals(120.0, booking.getTotalPrice());

        booking.setStatus("CANCELLED");
        assertEquals("CANCELLED", booking.getStatus());

//        booking.setPaypalTransactionId("TX12345");
//        assertEquals("TX12345", booking.getPaypalTransactionId());
    }

    @Test
    void testToString() {
        String str = booking.toString();
        assertTrue(str.contains("testUser"));
        assertTrue(str.contains("Test Workspace"));
        assertTrue(str.contains("€75.0"));
        assertTrue(str.contains("CONFIRMED"));
        assertTrue(str.contains(String.valueOf(booking.getBookingId())));
    }
}
