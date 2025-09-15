package adminCRUD;

import java.io.IOException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import Members.Member;
import Members.MemberData;
import admin.AdminLoginBean;
import Reservation_Page.BookingData;
import loginController.Helper;

@ManagedBean(name = "adminDashboardBean")
@SessionScoped
public class AdminDashboardBean {
	
	// Admin must be logged in
	@ManagedProperty(value = "#{adminLoginBean}")
	private AdminLoginBean adminLoginBean;
	public void setAdminLoginBean(AdminLoginBean adminLoginBean) {
	    this.adminLoginBean = adminLoginBean;
	}
	
    // MemberData
    @ManagedProperty(value = "#{memberData}")
    private MemberData memberData;
    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }

    // WorkspaceData
    @ManagedProperty(value = "#{workspaceData}")
    private WorkspaceData workspaceData;
    public void setWorkspaceData(WorkspaceData workspaceData) {
        this.workspaceData = workspaceData;
    }
    
    // BookingData
    @ManagedProperty(value = "#{bookingData}")
    private BookingData bookingData;
    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }
    
	public boolean isAdminLoggedIn() {
	    return adminLoginBean != null && adminLoginBean.isLoggedIn();
	}
	
	public String checkLogin() throws IOException {
	    if (!isAdminLoggedIn()) {
	        Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Admin must be logged in to use this dashboard.", null);
	        return "login";
	    }
	    return null;
	}
	
    public String logout() {
    	return (adminLoginBean.logout());
    }
    
    // REDIRECT methods
    public String viewWorkspaces() {
    	if(isAdminLoggedIn()) { return ("workspaces");}
    	return null; 
    }
    
    public String createWorkspace() {
    	if(isAdminLoggedIn()) { return ("createWorkspace");}
    	return null;
    }
    
    public String adminDashboard() {
    	if(isAdminLoggedIn()) { return ("adminDashboard");}
    	return null; 
    }
    
    public String viewUsers() {
    	if(isAdminLoggedIn()) { return ("users");}
    	return null; 
    }
    
    public String viewReports() {
    	if(isAdminLoggedIn()) { return ("adminFinancialReports");}
    	return null; 
    }
    
    public int getNumOfMembers() {
        return memberData != null ? memberData.numOfMembers() : 0;
    }

    public int getNumOfWorkspaces() {
        return workspaceData != null ? workspaceData.numOfWorkspaces() : 0;
    }
    
    public int getNumOfBookings() {
    	return bookingData != null ? bookingData.getBookings().size() : 0;
    }
    
    public ArrayList<Member> getMembersList() {
        return memberData != null ? memberData.getMembers() : new ArrayList<>();
    }

}
