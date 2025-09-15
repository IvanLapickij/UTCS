package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ManagerLoginBeanTest {
	
	ManagerData data;
	ManagerLoginBean loginBean;
	
	@BeforeEach 
    void setUp() {
		data = new ManagerData();
		loginBean = new  ManagerLoginBean();
    }
	
    @Test
    public void testSuccessfulLogin() {
        loginBean.setManagerData(data);
        
        String username = "manager";
        String password = "manager123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertEquals("managerDashboard", result);
        assertTrue(loginBean.isLoggedIn());
    }

    @Test
    public void testFailedLogin() {
        loginBean.setManagerData(data);

        String username = "wrong";
        String password = "wrong";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertNull(result);
        assertFalse(loginBean.isLoggedIn());
    }

    @Test
    public void testLogout() {
        loginBean.setManagerData(data);

        String username = "manager";
        String password = "manager123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        loginBean.login(username, password);
        
        String result = loginBean.logout();

        assertEquals("login", result);
        assertFalse(loginBean.isLoggedIn());
        assertNull(loginBean.getUsername());
        assertNull(loginBean.getPassword());
    }
}
