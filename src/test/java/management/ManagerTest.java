package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManagerTest {
    private Manager manager;
   
    @BeforeEach
    void setUp() {
    	manager = new Manager(null, null);
    }
   
    @Test
    void testFindAdminSuccess() {
    	ManagerData managerData = new ManagerData();

    	Manager result = managerData.findManager("manager", "manager123");
        assertNotNull(result);
        assertEquals("manager", result.getUsername());
        assertEquals("manager123", result.getPassword());
    }

    @Test
    void testFindAdminFailure() {
    	ManagerData managerData = new ManagerData();

    	Manager result = managerData.findManager("fakeUser", "fakePass");
        assertNull(result);
    }
   

}