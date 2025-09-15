package MemberRegistration;

import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import Members.Member;
import Members.MemberData;
import loginController.Helper;

@ManagedBean
@SessionScoped
public class RegistrationBean {
	private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String confirmPassword;

    public String register() {
        MemberData memberData = getMemberData();

        String usernameError = validUsername(memberData);
        if (usernameError == null) {
            System.out.println("---[ " + username + " ] is unique ---");

            if (memberData.doesEmailExist(emailAddress) == null) {
                System.out.println("---[ " + emailAddress + " ] is unique ---");

                if (validPassword() && passwordsMatch()) {
                    Member m = new Member(username, firstName, lastName, emailAddress, password);
                    memberData.addMember(m);
                    return "show-registration";
                }

                System.out.println("ERROR: Passwords don't match");
                addMessage("form-container:password2", "Passwords do not match.");

            } else {
                System.out.println("ERROR: Email Address already in use");
                emailAddress = null;
                addMessage("form-container:emailAddress", "Email Address already in use.");
            }

        } else {
            System.out.println("ERROR: Username already exists");
            username = null;
            addMessage("form-container:username", usernameError);
        }

        return null;
    }
    
    // Method to handle displaying error messages 
    private void addMessage(String clientId, String message) {
    	Helper.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
    
    private MemberData testMemberData;  // For unit test injection
    public void setMemberData(MemberData memberData) { this.testMemberData = memberData;  }
    // Modify getMemberData():
    private MemberData getMemberData() {
        if (testMemberData != null) {
            return testMemberData;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        return app.evaluateExpressionGet(context, "#{memberData}", MemberData.class);
    }
    
    public String clearRegForm() {
    	username = null;
        firstName = null;
        lastName = null;
        emailAddress = null;
        password = null;
        confirmPassword = null;
        
        return "login";
    }
    
    public boolean validPassword() {
    	// Simple validation for now
    	if(this.password.length() > 5) {
    		return true;
    	}
    	return false;
    }
    
    public String validUsername(MemberData memberData) {
    	if(memberData.doesUsernameExist(username) != null) { return "Username already exists. "; }
    	for(char c: username.toCharArray()) {
    		if(c == ' '){ return "Username cannot contain blank spaces. ";}
    	}
    	
    	return null;
    }
    
    public boolean passwordsMatch() {
    	return password != null && password.equals(confirmPassword);
    }
    
    public String cancel() {
    	System.out.println("CANCEL registration. Return to login. ");
    	return clearRegForm();
    }
    
    // Show partially hidden password
    public String getObscuredPassword() {
        return firstLetter(password) + "..." + lastLetter(password);
    }

    // Helper methods
    private String firstLetter(String s) {
        return s != null && s.length() > 0 ? s.substring(0, 1) : "";
    }

    private String lastLetter(String s) {
        return s != null && s.length() > 0 ? s.substring(s.length() - 1) : "";
    }
    
    // Getters and Setters
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}