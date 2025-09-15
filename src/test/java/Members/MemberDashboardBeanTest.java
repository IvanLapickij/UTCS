package Members;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MemberDashboardBeanTest {

    private MemberDashboardBean dashboardBean;
    private MemberLoginBean loginBean;

    @Before
    public void setUp() {
        //Setup real login bean and put in real MemberData 
        MemberData data = new MemberData(); // includes "member"/"member123"
        
        loginBean = new MemberLoginBean();
        loginBean.setMemberData(data); 

        dashboardBean = new MemberDashboardBean();
        dashboardBean.setMemberLoginBean(loginBean);
    }

    @Test
    public void testLoginAndDashboard() {
        loginBean.setUsername("member");
        loginBean.setPassword("member123");

        String loginResult = loginBean.login("member", "member123");
        assertEquals("memberDashboard", loginResult);
        assertTrue(loginBean.isLoggedIn());

        assertTrue(dashboardBean.isMemberLoggedIn());
        assertEquals("Welcome, member!", dashboardBean.getWelcomeMember());
        assertEquals("memberDashboard", dashboardBean.viewWorkspaces());
        assertEquals("memberBookings", dashboardBean.viewMemberBookings());
        assertEquals("createbooking", dashboardBean.createBookings());

        // Logout
        String logoutResult = dashboardBean.logout();
        assertEquals("login", logoutResult);
        assertFalse(loginBean.isLoggedIn());
        assertFalse(dashboardBean.isMemberLoggedIn());
        assertEquals("-- Please login --", dashboardBean.getWelcomeMember());
        assertNull(dashboardBean.viewWorkspaces());
        assertNull(dashboardBean.viewMemberBookings());
    }

    @Test
    public void testDashboardWhenNotLoggedIn() {
        loginBean.setUsername("member");
        loginBean.setPassword("wrongpassword");

        String loginResult = loginBean.login("member", "wrongpassword"); // should fail
        assertNull(loginResult);
        assertFalse(loginBean.isLoggedIn());
        assertFalse(dashboardBean.isMemberLoggedIn());
        assertEquals("-- Please login --", dashboardBean.getWelcomeMember());
        assertNull(dashboardBean.viewWorkspaces());
        assertNull(dashboardBean.viewMemberBookings());
        assertNull(dashboardBean.createBookings());
    }
}

