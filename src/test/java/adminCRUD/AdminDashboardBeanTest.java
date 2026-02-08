package adminCRUD;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Members.MemberData;
import admin.AdminData;
import admin.AdminLoginBean;

public class AdminDashboardBeanTest {

    private AdminDashboardBean dashboardBean;
    private AdminLoginBean loginBean;
    private MemberData memberData;
    private WorkspaceData workspaceData;

    @BeforeEach
    public void setUp() {
        // Setup admin login
        loginBean = new AdminLoginBean();
        loginBean.setAdminData(new AdminData());
        String username = "admin";
        String password = "admin123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        loginBean.login(username, password);

        // Setup Members Data
        memberData = new MemberData();
        
        // Setup Workspace Data
        workspaceData = new WorkspaceData();
       

        dashboardBean = new AdminDashboardBean();
        dashboardBean.setAdminLoginBean(loginBean);
        dashboardBean.setMemberData(memberData);
        dashboardBean.setWorkspaceData(workspaceData);
        System.out.println(); System.out.println();
    }
    
    @Test
    public void testNumOfMembers() {
        System.out.println("Running: testNumOfMembers");
        assertEquals(2, dashboardBean.getNumOfMembers());
    }
    
    @Test
    public void testNumOfEmptyMembers() {
        System.out.println("Running: testNumOfEmptyMembers");
        memberData.getMembers().clear();
        assertEquals(10, dashboardBean.getNumOfMembers());
    }

    @Test
    public void testNumOfWorkspaces() {
        System.out.println("Running: testNumOfWorkspaces");
        assertEquals(5, dashboardBean.getNumOfWorkspaces());
    }
    
    @Test
    public void testNumOfEmptyWorkspaces() {
        System.out.println("Running: testNumOfEmptyWorkspaces");
        workspaceData.getWorkspaces().clear();
        assertEquals(0, dashboardBean.getNumOfWorkspaces());
    }

    @Test
    public void testIsAdminLoggedIn_TRUE() {
        System.out.println("Running: testIsAdminLoggedIn_TRUE");
        assertTrue(dashboardBean.isAdminLoggedIn());
    }

    @Test
    public void testIsAdminLoggedIn_FALSE() {
        System.out.println("Running: testIsAdminLoggedIn_FALSE");
        loginBean.logout();
        assertFalse(dashboardBean.isAdminLoggedIn());
        dashboardBean.setAdminLoginBean(null);
        assertFalse(dashboardBean.isAdminLoggedIn());
    }

    @Test
    public void testAdminLogout() {
        System.out.println("Running: testAdminLogout");
        String result = dashboardBean.logout();
        assertEquals("login", result);
        assertFalse(loginBean.isLoggedIn());
    }

    @Test
    public void testCheckLogin_WhenLoggedIn() throws IOException {
        System.out.println("Running: testCheckLogin_WhenLoggedIn");
        String result = dashboardBean.checkLogin();
        assertNull(result);
    }

    @Test
    public void testViewWorkspaces_WhenLoggedIn() {
        System.out.println("Running: testViewWorkspaces_WhenLoggedIn");
        String result = dashboardBean.viewWorkspaces();
        assertEquals("workspaces", result);
    }

    @Test
    public void testCreateWorkspace_WhenLoggedIn() {
        System.out.println("Running: testCreateWorkspace_WhenLoggedIn");
        String result = dashboardBean.createWorkspace();
        assertEquals("createWorkspace", result);
    }

    @Test
    public void testViewReports_WhenLoggedIn() {
        System.out.println("Running: testViewReports_WhenLoggedIn");
        String result = dashboardBean.viewReports();
        assertEquals("adminFinancialReports", result);
    }

    @Test
    public void testViewUsers_WhenLoggedIn() {
        System.out.println("Running: testViewUsers_WhenLoggedIn");
        String result = dashboardBean.viewUsers();
        assertEquals("users", result);
    }

    @Test
    public void testAdminDashboard_WhenLoggedIn() {
        System.out.println("Running: testAdminDashboard_WhenLoggedIn");
        String result = dashboardBean.adminDashboard();
        assertEquals("adminDashboard", result);
    }

    @Test
    public void testCheckLogin_WhenNotLoggedIn() throws IOException {
        System.out.println("Running: testCheckLogin_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.checkLogin();
        assertEquals("login", result);
    }

    @Test
    public void testViewWorkspaces_WhenNotLoggedIn() {
        System.out.println("Running: testViewWorkspaces_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.viewWorkspaces();
        assertNull(result);
    }

    @Test
    public void testCreateWorkspace_WhenNotLoggedIn() {
        System.out.println("Running: testCreateWorkspace_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.createWorkspace();
        assertNull(result);
    }

    @Test
    public void testAdminDashboard_WhenNotLoggedIn() {
        System.out.println("Running: testAdminDashboard_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.adminDashboard();
        assertNull(result);
    }
    
    @Test
    public void testViewUsers_WhenNotLoggedIn() {
        System.out.println("Running: testAdminDashboard_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.viewUsers();
        assertNull(result);
    }
    
    @Test
    public void testViewReports_WhenNotLoggedIn() {
        System.out.println("Running: testAdminDashboard_WhenNotLoggedIn");
        loginBean.logout();
        String result = dashboardBean.viewReports();
        assertNull(result);
    }
}
