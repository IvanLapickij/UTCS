package Reservation_Page;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import adminCRUD.Workspace;
import adminCRUD.WorkspaceData;
@ManagedBean
@ApplicationScoped
public class BookingData {

    private ArrayList<Booking> bookings = new ArrayList<>();

    public ArrayList<Booking> getBookings() {
        return bookings;
    }
    
    @ManagedProperty(value = "#{workspaceData}")
    private WorkspaceData workspaceData;
    public void setWorkspaceData(WorkspaceData workspaceData) {
        this.workspaceData = workspaceData;
    }
    private ArrayList<Workspace> workspaces;
    
    @PostConstruct
    public void init () {
    	workspaces = workspaceData.getWorkspaces();
        createDummyBookings();
    }

    private void createDummyBookings() {
        for (int i = 0; i < workspaces.size(); i++) {
            Workspace workspace = workspaces.get(i);
            LocalDateTime manualStartDateTime = LocalDateTime.of(2025, 8, 21, (9), 0);
            LocalDateTime endDateTime = manualStartDateTime.plusHours(3 + i);
            Date startDate = Date.from(manualStartDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
            
            double totalPrice = workspace.getPrice() * 3;
            totalPrice += (totalPrice * .23);  // Add tax

            Booking booking = new Booking("member", workspace, startDate, endDate, totalPrice);
            bookings.add(booking);
            System.out.println(booking.toString());
        }
    }

    public Booking getById(Integer bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) return b;
        }
        return null;
    }

    public List<Booking> getBookingsForUsername(String username) {
        return bookings.stream()
                .filter(b -> b.getMemberUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsForWorkspace(Workspace w) {
        return bookings.stream()
                .filter(b -> b.getWorkspace().getRoomID() == w.getRoomID())
                .collect(Collectors.toList());
    }

    public boolean addBooking(Booking booking) {
        // prevent overlapping bookings for the same workspace
        if (!isWorkspaceAvailable(booking.getWorkspace(),
                                  booking.getStartDateTime(),
                                  booking.getEndDateTime())) {
            return false;
        }
        bookings.add(booking);
        System.out.println("Added booking: " + booking);
        return true;
    }

    public void removeBooking(int bookingId) {
        bookings.removeIf(b -> b.getBookingId() == bookingId);
    }

    public boolean isWorkspaceAvailable(Workspace workspace,
                                        Date start,
                                        Date end) {
        if (start == null || end == null || !end.after(start)) return false;

        for (Booking b : bookings) {
            // Check for the same workspace ID
            if (b.getWorkspace().getRoomID() == workspace.getRoomID()) {
                Date existingStart = b.getStartDateTime();
                Date existingEnd = b.getEndDateTime();
                if (start.before(existingEnd) && end.after(existingStart)) {
                    return false; // Overlap detected, workspace is not available
                }
            }
        }
        return true; // No overlap, workspace is available
    }

	 public static LocalDateTime toLdt(Date d) {
	        if (d == null) return null;
	        return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
	}
	 
    public int numOfBookings() {
        return bookings.size();
    }
}
