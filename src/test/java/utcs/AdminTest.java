package utcs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import admin.Admin;
import admin.AdminData;

class AdminTest {
    private Admin admin;
   
    @BeforeEach
    void setUp() {
    	admin = new Admin(null, null);
    }
   
    @Test
    void testFindAdminSuccess() {
        AdminData adminData = new AdminData();

        Admin result = adminData.findAdmin("admin", "admin123");
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("admin123", result.getPassword());
    }

    @Test
    void testFindAdminFailure() {
        AdminData adminData = new AdminData();

        Admin result = adminData.findAdmin("fakeUser", "fakePass");
        assertNull(result);
    }
   

}