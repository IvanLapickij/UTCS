package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ManagerDataTest {
	
	ManagerData data;
	
	@BeforeEach 
    void setUp() {
		data = new ManagerData();
    }
	
	@Test
	 public void testfindDefaultManager() {
		// Default member
		Manager UTCS_Manager = data.findManager("manager", "manager123");
		
		assertNotNull(UTCS_Manager);
		assertEquals("manager", UTCS_Manager.getUsername());
	}

	@Test
	 public void testfindotherManager() {
		//Testing other members
		Manager UTCS_Manager = data.findManager("testManager", "test123");
		
		assertNotNull(UTCS_Manager);	
	}

	@Test
	 public void testFailFindManagerWrongPassword() {
		// wrong password 
		Manager UTCS_Manager = data.findManager("manager", "wrongPassword");
		
		assertNull(UTCS_Manager);
	}

	@Test
	 public void testFailFindManagerWrongUsername() {
		// User name is not exist
		Manager UTCS_Manager = data.findManager("wrongUsername", "manager123");
		
		assertNull(UTCS_Manager);
	}
	
}
