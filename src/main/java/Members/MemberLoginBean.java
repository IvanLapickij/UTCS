package Members;

import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class MemberLoginBean {

    private String username;
    private String password;
    private boolean loggedIn;

    public String login(String username, String password) {
        MemberData memberData = getMemberData();

        // Check member exists in MemberData class
        if (memberData.findMember(username, password) != null) {
        	//Print to console
        	System.out.println("---[ " + username + " ] LOGGED IN SUCCESSFULLY ---");
            this.username = username;
            this.password = password;
            this.loggedIn = true;
            return "memberDashboard";
        }
        // failed login
        return null;
    }

    
 // Add setter for testing
    private MemberData memberData;
    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }
    private MemberData getMemberData() {
        if (memberData != null) {
            return memberData;
        }
    
    
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        return app.evaluateExpressionGet(context, "#{memberData}", MemberData.class);
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