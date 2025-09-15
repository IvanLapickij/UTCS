package Members;

public class Member {
	private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    
	public Member(String username, String firstName, String lastName, String emailAddress, String password) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
	}

    public String getUsername() { return username; }
	public void setUsername(String username) {this.username = username;}

	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}

	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}
	
	public String getEmailAddress() {return emailAddress;}
	public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
	
	public void setPassword(String password) {this.password = password;}
	public String getPassword() { return password; }
	
	public String toString() {
		return "Member:[" + username + ", " + firstName + ", " + lastName + ", " + emailAddress + "]";
	}
}