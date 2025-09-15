package admin;

import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class AdminLoginBean {

    private String username;
    private String password;
    private boolean loggedIn;

    public String login(String username, String password) {
        AdminData adminData = getAdminData();

        // Check if the admin exists.
        if (adminData.findAdmin(username, password) != null) {
        	//Print to console
        	System.out.println("---[ " + username + " ] LOGGED IN SUCCESSFULLY ---");
            // Only set the session-scoped bean's properties on successful login
            this.username = username;
            this.password = password;
            this.loggedIn = true;
            return "adminDashboard";
        }
        
        // Return null if the login fails
        return null;
    }
    
 // Add setter for testing
    private AdminData adminData;
    public void setAdminData(AdminData adminData) {
        this.adminData = adminData;
    }
    private AdminData getAdminData() {
        if (adminData != null) {
            return adminData;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        return app.evaluateExpressionGet(context, "#{adminData}", AdminData.class);
    }


    public String logout() {
    	//Print to console
    	System.out.println("---[ " + username + " ] LOGOUT ---");
        loggedIn = false;
        username = null;
        password = null;
        return "login";
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password;}

    public boolean isLoggedIn() {return loggedIn;}
}