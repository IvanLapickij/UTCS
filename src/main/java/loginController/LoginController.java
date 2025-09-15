package loginController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import Members.MemberLoginBean;
import admin.AdminLoginBean;
import management.ManagerLoginBean;

@ManagedBean
@RequestScoped
public class LoginController {
    final static String LOGIN_ERROR = "Username or Password invalid";
 
    private String username;
    private String password;

    /**
     * Attempts to log the user in by passing their credentials to each
     * login bean. The first successful login determines the user's role
     * and redirects to the correct dashboard.
     *
     * @return The navigation rule for the successful login, or null on failure.
     */
    public String logUserIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = null;

        // Try to log in as an Admin
        AdminLoginBean admin = Helper.getBean("adminLoginBean", AdminLoginBean.class);
        redirect = admin.login(username, password);
        if (redirect != null) {
            System.out.println("Redirecting to " + redirect + ".xhtml ");
            return redirect;
        }

        // Try to log in as a Member
        MemberLoginBean member = Helper.getBean("memberLoginBean", MemberLoginBean.class);
        redirect = member.login(username, password);
        if (redirect != null) {
            System.out.println("Redirecting to " + redirect + ".xhtml ");
            return redirect;
        }

        // Try to log in as a Manager
        ManagerLoginBean manager = Helper.getBean("managerLoginBean", ManagerLoginBean.class);
        redirect = manager.login(username, password);
        if (redirect != null) {
            System.out.println("Redirecting to " + redirect + ".xhtml ");
            return redirect;
        }

        // If none of the login attempts were successful, display an error message
        System.out.println("Display Login Error message --> " + LOGIN_ERROR);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, LOGIN_ERROR, null));
        return null;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // Handle redirect to registration page instead of static link
    public String redirectToRegister() {
        System.out.println("Redirecting to Registration Page");
        return "memberRegistration";
    }
}
