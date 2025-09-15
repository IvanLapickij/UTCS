package utcs;

import org.junit.jupiter.api.Test;

import admin.AdminData;
import admin.AdminLoginBean;

import static org.junit.jupiter.api.Assertions.*;

public class AdminLoginBeanTest {

    @Test
    public void testSuccessfulLogin() {
        AdminLoginBean loginBean = new AdminLoginBean();
        loginBean.setAdminData(new AdminData());  // Inject real AdminData

        String username = "admin";
        String password = "admin123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertEquals("adminDashboard", result);
        assertTrue(loginBean.isLoggedIn());
    }

    @Test
    public void testFailedLogin() {
        AdminLoginBean loginBean = new AdminLoginBean();
        loginBean.setAdminData(new AdminData());

        String username = "notReal";
        String password = "wrongPass";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertNull(result);
        assertFalse(loginBean.isLoggedIn());
    }

    @Test
    public void testLogout() {
        AdminLoginBean loginBean = new AdminLoginBean();
        loginBean.setAdminData(new AdminData());

        String username = "admin";
        String password = "admin123";
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
