package Reservation_Page;

import java.text.SimpleDateFormat;
import java.util.Date;

import adminCRUD.Workspace;

public class Booking {

    private static int nextId = 1;

    private int bookingId;
    private String memberUsername;
    private Workspace workspace;
    private Date startDateTime;
    private Date endDateTime;
    private double totalPrice;

    // Optional: status (future use)
    private String status;                // e.g. "CONFIRMED"

    public Booking(String memberUsername,
                   Workspace workspace,
                   java.util.Date startDateTime2,
                   java.util.Date endDateTime2,
                   double totalPrice) {

        this.bookingId = nextId++;
        this.memberUsername = memberUsername;
        this.workspace = workspace;
        this.startDateTime = startDateTime2;
        this.endDateTime = endDateTime2;
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
    }

    public int getBookingId() { return bookingId; }

    public String getMemberUsername() { return memberUsername; }
    public void setMember(String memberUsername) { this.memberUsername = memberUsername; }

    public Workspace getWorkspace() { return workspace; }
    public void setWorkspace(Workspace workspace) { this.workspace = workspace; }

    public Date getStartDateTime() { return startDateTime; }
    public void setStartDateTime(Date startDateTime) { this.startDateTime = startDateTime; }

    public Date getEndDateTime() { return endDateTime; }
    public void setEndDateTime(Date endDateTime) { this.endDateTime = endDateTime; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking[" + bookingId + ", " +
        		memberUsername + ", ID:" +
                workspace.getRoomID() + ":[" + workspace.getName() + "], " +
                startDateTime + " → " + endDateTime +
                ", €" + totalPrice + ", " + status + "]";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - EEEE dd MMM");
    
    public String formattedStartDate;
    public String getFormattedStartDate() { return this.formattedStartDate = sdf.format(startDateTime);}
    public String formattedEndDate;
    public String getFormattedEndDate() { return this.formattedEndDate = sdf.format(endDateTime);}
    
    // Used during testing
    public static void resetBookingId() { nextId = 0; }
}
