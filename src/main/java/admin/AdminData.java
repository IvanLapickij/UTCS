package admin;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class AdminData {

    private ArrayList<Admin> admins = new ArrayList<Admin>();

    public AdminData() {
        // Default admin
        Admin admin = new Admin("admin", "admin123");
        admins.add(admin);
        
        //Testing other admins
        Admin testAdmin = new Admin("testAdmin", "test123");
        admins.add(testAdmin);
    }

    public Admin findAdmin(String username, String password) {
    	System.out.println("Searching for: " + username + " in ArrayList (AdminData)");
    	for(Admin a: admins) {
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
}