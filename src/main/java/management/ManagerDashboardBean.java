package management;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import loginController.Helper;

@ManagedBean(name = "managerDashboardBean")
@SessionScoped
public class ManagerDashboardBean {

	// Management must be logged in
	@ManagedProperty(value = "#{managerLoginBean}")
	private ManagerLoginBean managerLoginBean;
	public void setManagerLoginBean(ManagerLoginBean managerLoginBean) {
		this.managerLoginBean = managerLoginBean;
	}
	
	public boolean isManagerLoggedIn() {
	    return managerLoginBean != null && managerLoginBean.isLoggedIn();
	} 
	
	public void checkLogin() throws IOException {
	    if (!isManagerLoggedIn()) {
	        Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Managers must be logged in to use this dashboard.", null);
	    
	    }
	}
	
    public String logout() {
    	return (managerLoginBean.logout());
    }
    
    public String getWelcomeManager() {
    	if(isManagerLoggedIn()) { return ("Welcome, " + managerLoginBean.getUsername() + "!");}
    	return "-- Please login --"; 
    }
    
    // REDIRECT methods
    
    public String financialReports() {
    	if(isManagerLoggedIn()) { return ("financialReports");}
    	return null; 
    }
}
