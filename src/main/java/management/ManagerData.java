package management;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class ManagerData {
    private ArrayList<Manager> managers = new ArrayList<Manager>();

    public ManagerData() {
        // Default manager
    	Manager manager = new Manager("manager", "manager123");
        managers.add(manager);
        
        //Testing other manager
        Manager testManager = new Manager("testManager", "test123");
        managers.add(testManager);
    }

    public Manager findManager(String username, String password) {
    	System.out.println("Searching for: " + username + " in ArrayList (ManagerData)");
    	for(Manager m: managers) {
    		if(m.getUsername().equals(username)) {
    			if(m.getPassword().equals(password)) {
    				System.out.println("--- FOUND ---");
    				return m;
    			}
    		}
    	}
    	System.out.println("--- NOT FOUND ---");
        return null;
    }
}
