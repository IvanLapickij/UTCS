package utcs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import admin.Admin;
import admin.AdminData;

class AdminDataTest {
    private AdminData adminData;
   
    @BeforeEach
    void setUp() {
        adminData = new AdminData();
    }
   
    @Test
    void testDefaultAdmin() {
        String username = "admin";
        String password = "admin123";
       
        Admin result = adminData.findAdmin(username, password);
        assertNotNull(result, "Default admin should be found");
        assertEquals(username, result.getUsername(), "Returned username should match");
    }

    @Test
    void testUnknownAdmin() {
        String unknownUsername = "nobody";
        String unknownPassword = "password";
       
        Admin result = adminData.findAdmin(unknownUsername, unknownPassword);
        assertNull(result, "Admin does not exist");
    }
   
    @Test
    void testIncorrectUsername() {
        String username = "wrong";
        String password = "admin";
       
        Admin result = adminData.findAdmin(username, password);
        assertNull(result, "Username or Password is incorrect");
    }
   
    @Test
    void testIncorrectPassword() {
        String username = "admin";
        String password = "password";
       
        Admin result = adminData.findAdmin(username, password);
        assertNull(result, "Username or Password is incorrect");
    }
   
}