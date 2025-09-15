package Reservation_Page;

import adminCRUD.Workspace;
import adminCRUD.WorkspaceData;
import loginController.Helper;
import payment.PaymentServices;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.paypal.base.rest.PayPalRESTException;
import Members.MemberLoginBean;

import java.io.IOException;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@ManagedBean(name = "bookingBean")
@ViewScoped
public class BookingBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double TAX_RATE = 0.23;

    @ManagedProperty(value = "#{workspaceData}")
    private WorkspaceData workspaceData;
    public void setWorkspaceData(WorkspaceData workspaceData) {
        this.workspaceData = workspaceData;
    }

    @ManagedProperty(value = "#{memberLoginBean}")
    private MemberLoginBean memberLoginBean;
    public void setMemberLoginBean(MemberLoginBean memberLoginBean) {
        this.memberLoginBean = memberLoginBean;
    }
    
    @ManagedProperty(value = "#{bookingData}")
    private BookingData bookingData;
    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    private Booking orderDetail;

    private Integer selectedRoomId;
    private Date startDateTime;
    private Date endDateTime;

    private double totalAmount;
    private double subTotal;
    private double tax;

    private boolean validDates;
    private int bookingId = -1;

    public BookingBean() {
        validDates = true;
        if (startDateTime == null) startDateTime = roundToNextFullHour(new Date());
        if (endDateTime == null) endDateTime = roundToNextFullHour(new Date());
    }

    public void resetFields() {
        this.selectedRoomId = null;
        this.startDateTime = null;
        this.endDateTime = null;
        this.totalAmount = 0;
        this.subTotal = 0;
        this.tax = 0;
    }

    // ==================== DATE UTILS ====================
    public Date getToday() {
        LocalDate today = LocalDate.now();
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date roundToNextFullHour(Date date) {
        if (date == null) return null;
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        if (ldt.getMinute() > 0 || ldt.getSecond() > 0 || ldt.getNano() > 0) {
            ldt = ldt.plusHours(1).truncatedTo(java.time.temporal.ChronoUnit.HOURS);
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public long billableHours(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        
        long billableHours = 0;
        while (cal.getTime().before(end)) {
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            boolean workDay = (dow >= Calendar.MONDAY && dow <= Calendar.SATURDAY);
            boolean workHours = (hour >= 9 && hour < 17);

            if (workDay && workHours) billableHours++;
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        
        return billableHours;
    }
    
    // ==================== DATE FILTER VALIDATION ====================
    public void dateFilter(FacesContext context, UIComponent component, Object value) {
        Date selectedDate = (Date) value;
        LocalDateTime ldt = LocalDateTime.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
        validDates = true;

        if (selectedRoomId == null || startDateTime == null || endDateTime == null) {
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Booking Incomplete", "Please complete booking details.");
            validDates = false;
            return;
        }

        // block Sunday
        if (ldt.getDayOfWeek() == DayOfWeek.SUNDAY) {
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Invalid Day", "Bookings not allowed on Sundays");
            validDates = false;
            return;
        }

        // block outside working hours
        int hour = ldt.getHour();
        if (hour < 9 || hour > 17) {
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Invalid Time", "Booking time must be between 09:00 and 17:00");
            validDates = false;
            return;
        }

        // ensure end > start
        if (component.getId().equals("end")) {
            if (startDateTime != null && selectedDate.before(startDateTime)) {
                Helper.addMessage(FacesMessage.SEVERITY_ERROR, "End Before Start", "End date cannot be before start date.");
                validDates = false;
                return;
            }
            if (startDateTime != null && selectedDate.equals(startDateTime)) {
                Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Same Start and End", "Start and End cannot be the same.");
                validDates = false;
                return;
            }
        }
    }

    // ==================== PRICE CALCULATION ====================
    public void recalcSubTotal(Workspace ws) {
    	if (selectedRoomId == null) {
    		subTotal = 0;
    		totalAmount = 0;
    		return;
    	}
    	
        if (ws == null || startDateTime == null || endDateTime == null) {
            subTotal = 0; totalAmount = 0; return;
        }

        startDateTime = roundToNextFullHour(startDateTime);
        endDateTime = roundToNextFullHour(endDateTime);
        if (!endDateTime.after(startDateTime)) {
            subTotal = 0; 
            totalAmount = 0; 
            return;
        }
        
        long billableHours = billableHours(startDateTime, endDateTime);

        subTotal = ws.getPrice() * billableHours;
        if (subTotal <= 0) {
            subTotal = 0; totalAmount = 0;
        } else {
            tax = subTotal * TAX_RATE;
            totalAmount = subTotal + tax;
        }
    }

    // ==================== PAYMENT FLOW ====================
    public String proceedToPayment() throws IOException {

        if (memberLoginBean.getUsername() == null) { return null; } System.out.println("member ------------");
        if(!validDates) {return null; } System.out.println("dates ------------");
        
        if (!bookingData.isWorkspaceAvailable(workspaceData.findById(selectedRoomId), startDateTime, endDateTime)) {
        	Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Workspace Booked Already", "This workspace is already booked during selected times.");
        	return null;
        }
        //recalcSubTotal(workspaceData.findById(selectedRoomId));
        System.out.println("price ------------");
        if (totalAmount == 0) {
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", "Booking price cannot be zero.");
            return null;
        }
        System.out.println("amount ------------");
        try {
            this.orderDetail = new Booking(
                    memberLoginBean.getUsername(),
                    workspaceData.findById(selectedRoomId),
                    startDateTime,
                    endDateTime,
                    totalAmount
            );
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("currentBooking", this.orderDetail);

            PaymentServices ps = new PaymentServices();
            String approvalLink = ps.authorizePayment(orderDetail);
            FacesContext.getCurrentInstance().getExternalContext().redirect(approvalLink);
        } catch (PayPalRESTException ex) {
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Error: " + ex.getMessage());
        }
        return null;
    }
    

    // ==================== CONFIRM PAYMENT ====================
    public int getBookingId() { return bookingId; }
    public void confirmPayment() {
        Booking confirmedBooking = (Booking) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("currentBooking");

        if (confirmedBooking != null) {
            BookingData bookingData = Helper.getBean("bookingData", BookingData.class);
            bookingData.getBookings().add(confirmedBooking);
            this.bookingId = confirmedBooking.getBookingId();

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().remove("currentBooking");

            resetFields();
        }
    }

    public Integer getSelectedRoomId() { return selectedRoomId; }
    public void setSelectedRoomId(Integer selectedRoomId) { this.selectedRoomId = selectedRoomId; }

    public Date getStartDateTime() { return startDateTime; }
    public void setStartDateTime(Date startDateTime) { this.startDateTime = startDateTime; }

    public Date getEndDateTime() { return endDateTime; }
    public void setEndDateTime(Date endDateTime) { this.endDateTime = endDateTime; }

    public double getTotalAmount() { return totalAmount; }
    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public double getTax() { return this.subTotal * TAX_RATE; }
    public void setTax(double tax) { this.tax = tax; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}

