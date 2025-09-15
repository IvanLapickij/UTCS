package management;

import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ManagerLoginBean {

    private String username;
    private String password;
    private boolean loggedIn;

    public String login(String username, String password) {
        ManagerData managerData = getManagerData();

        // Check manager exists in ManagerData class
       if (managerData.findManager(username, password) != null) {
        	//Print to console
        	System.out.println("---[ " + username + " ] LOGGED IN SUCCESSFULLY ---");
            loggedIn = true;
            this.username = username;
            this.password = password;
            this.loggedIn = true;
            return "managerDashboard";
        }
        return null;
    }
    
 // Add setter for testing
    private ManagerData managerData;
    public void setManagerData(ManagerData managerData) {
        this.managerData = managerData;
    }
    private ManagerData getManagerData() {
        if (managerData != null) {
            return managerData;
        }       
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        return app.evaluateExpressionGet(context, "#{managerData}", ManagerData.class);
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
