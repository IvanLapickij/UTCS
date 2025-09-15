package managementTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import management.ManagerDashboardBean;
import management.ManagerData;
import management.ManagerLoginBean;

class ManagerDashboardBeanTest {

	private ManagerDashboardBean dashboardBean;
	private ManagerLoginBean loginBean;

	@BeforeEach
	void setUp() throws Exception {
		// Manager Data
		ManagerData managerData = new ManagerData();
		
		// Setup manager login
		loginBean = new ManagerLoginBean();
		loginBean.setManagerData(managerData);
        String username = "manager";
        String password = "manager123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String outcome = loginBean.login(username, password);
		assertEquals("managerDashboard", outcome, "Precondition: login should succeed with default ManagerData users.");
 
		// Dashboard bean connected to the ManagerLoginBean
		dashboardBean = new ManagerDashboardBean();
		dashboardBean.setManagerLoginBean(loginBean);
	}

	// Checks to see if the dashboard has the correct login status (manager is logged in or not)
	@Test
	void greetMessageAfterLogin() {
		assertTrue(dashboardBean.isManagerLoggedIn());
	}
	
	// checks if welcome message includes the manager's name
	@Test
	void greetMessageWithName() {
		assertEquals("Welcome, manager!", dashboardBean.getWelcomeManager());
	}
	
	// should be able to get redirected to the financial report page when logged in as a manager
	@Test
	void redirectionToFinancialReportsWhenLoggedIn() {
		assertEquals("financialReports", dashboardBean.financialReports());
	}
	
	// should clear the session and redirect to the login page after logging out
	@Test
	void logoutClearSessionAndRedirect() {
		String outcome = dashboardBean.logout();
		assertEquals("login", outcome);
		
		assertFalse(loginBean.isLoggedIn(), "LoginBean should be in a logged-out state.");
        assertFalse(dashboardBean.isManagerLoggedIn(), "DashboardBean should be in a logged-out state.");
        
        assertNull(dashboardBean.financialReports(), "Financial reports should not be accessible when logged out.");
        assertEquals("-- Please login --", dashboardBean.getWelcomeManager());
	}
	 
	// should navigate to the financial reports page when clicked
	@Test
	void financialReportsNavigationStability() {
	    assertEquals("financialReports", dashboardBean.financialReports());
	    assertEquals("financialReports", dashboardBean.financialReports());
	}


}
