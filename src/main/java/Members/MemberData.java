package Members;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import adminCRUD.Workspace;

import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class MemberData {

    private ArrayList<Member> members = new ArrayList<Member>();

    public MemberData() {
        // Default member
    	Member member = new Member("member", "mem", "ber", "member@member.ie", "member123");
        members.add(member);
        
        //Testing other members
        Member testmember = new Member("testMember", "Firstname", "Lastname", "test@gmail.com", "test123");
        members.add(testmember);
    }
    
	public ArrayList<Member> getMembers(){
		return members;
	}

    public Member findMember(String username, String password) {
    	System.out.println("Searching for: " + username + " in ArrayList (MemberData)");
    	for(Member a: members) {
    		if(a.getUsername().equals(username)) {
    			if(a.getPassword().equals(password)) {
    				System.out.println("--- FOUND ---");
    				return a;
    			}
    		}
    	}
    	System.out.println("--- NOT FOUND ---");
        return null;
    }
    
    public Member doesUsernameExist(String username) {
    	System.out.println("Searching for: " + username + " in ArrayList (MemberData)");
    	for(Member m: members) {
    		if(username != null && m.getUsername().equals(username)) {
    			return m;
    		}
    	}
    	System.out.println("--- Username: " + username + " doesn't exist ---");
        return null;
    }
    
    public Member doesEmailExist(String email) {
    	System.out.println("Searching for: " + email + " EMAIL in ArrayList (MemberData)");
    	for(Member m: members) {
    		if(m.getEmailAddress().equals(email)) {
    			return m;
    		}
    	}
    	System.out.println("--- Email: " + email + " already in use ---");
        return null;
    }
    
    public Member getMemberUsername(String username) {
    	System.out.println("Searching for: " + username + " USERNAME in ArrayList (MemberData)");
    	for(Member m: members) {
    		if(m.getUsername().equals(username)) {
    			return m;
    		}
    	}
    	System.out.println("--- User: " + username + " not found ---");
        return null;
    }
    
    public void addMember(Member m) {
    	members.add(m);
    	System.out.println("Add new member: " + members);
    }
    
    public int numOfMembers() {
    	return members.size();
    }
    
    
}