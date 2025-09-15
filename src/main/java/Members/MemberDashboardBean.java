package Members;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import loginController.Helper;

@ManagedBean(name = "memberDashboardBean")
@SessionScoped
public class MemberDashboardBean {
	
	// Member must be logged in
	@ManagedProperty(value = "#{memberLoginBean}")
	private MemberLoginBean memberLoginBean;
	public void setMemberLoginBean(MemberLoginBean memberLoginBean) {
	    this.memberLoginBean = memberLoginBean;
	}
    
	public boolean isMemberLoggedIn() {
	    return memberLoginBean != null && memberLoginBean.isLoggedIn();
	}
	
	public void checkLogin() throws IOException {
	    if (!isMemberLoggedIn()) {
	        Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Member must be logged in to use this dashboard.", null);
	    }
	}
	
    public String logout() {
    	return (memberLoginBean.logout());
    }
    
    public String getWelcomeMember() {
    	if(isMemberLoggedIn()) {
    	    return ("Welcome, " + memberLoginBean.getUsername() + "!");
    	}
    	return "-- Please login --"; 
    }
    
    // REDIRECT methods
    public String viewWorkspaces() {
    	if(isMemberLoggedIn()) {
    	    return ("memberDashboard");
    	}
    	return null; 
    }
    
    public String viewMemberBookings() {
    	if(isMemberLoggedIn()) {
    	    return ("memberBookings");
    	}
    	return null; 
    }
    public String createBookings() {
    	if(isMemberLoggedIn()) { return ("createbooking");}
    	return null; 
    }
}
