package adminCRUD;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adminCRUD.CreateWorkspaceBean;
import adminCRUD.WorkspaceData;
import admin.AdminLoginBean;

class CreateWorkspaceBeanTest {
	
	private CreateWorkspaceBean bean;
	private WorkspaceData testData;
	
	// Simulates admin login status using username and password
	private AdminLoginBean simulateLogin(String username, String password) {
		return new AdminLoginBean() {
			@Override
			public boolean isLoggedIn() {
				return "admin".equals(username) && "admin123".equals(password);
			}
		};
	}

	// Runs before each test
	// Sets up a fresh CreateWorkspaceBean with test data and mock login
	@BeforeEach
	void setUp() {
		bean = new CreateWorkspaceBean();
		testData = new WorkspaceData();
		bean.setWorkspaceData(testData);

		// Correct login: admin / admin123
		bean.setAdminLoginBean(simulateLogin("admin", "admin123"));
	}
	
	// Test 1
	// Confirms a workspace with valid details is created successfully
	// Should return "workspaces" and increase list size
	@Test
	void testValidWorkspaceCreation() {
		bean.setName("Focus Pod 1");
		bean.setDescription("Quiet space for deep work");
		bean.setRoomType("Meeting Pod");
		bean.setMaxCapacity(3);
		bean.setPrice(15.0);

		String result = bean.createWorkspace();
		assertEquals("workspaces", result, "Should return 'workspaces' on success");
		assertEquals(6, testData.getWorkspaces().size(), "Workspace count should be 6 after adding");
		assertEquals("Focus Pod 1", testData.getWorkspaces().get(5).getName());
	}
	
	// Test 2
	// Uses an invalid room type
	// Should return null and workspace list should not change
	@Test
	void testInvalidRoomType() {
		bean.setRoomType("Invalid Type");
		bean.setMaxCapacity(5);
		bean.setPrice(10);

		String result = bean.createWorkspace();
		assertNull(result, "Should return null for invalid room type");
		assertEquals(5, testData.getWorkspaces().size(), "No workspace should be added");
	}
	
	// Test 3
	// Uses invalid capacity (e.g. 0)
	// Should block creation and return null
	@Test
	void testInvalidCapacity() {
		bean.setRoomType("Private Office");
		bean.setMaxCapacity(0);
		bean.setPrice(20);

		String result = bean.createWorkspace();
		assertNull(result, "Should return null for invalid capacity");
		assertEquals(5, testData.getWorkspaces().size(), "Workspace list should remain unchanged");
	}
	
	// Test 4
	// Uses invalid price (e.g. 0)
	// Should block creation and return null
	@Test
	void testInvalidPrice() {
		bean.setRoomType("Private Office");
		bean.setMaxCapacity(5);
		bean.setPrice(0);

		String result = bean.createWorkspace();
		assertNull(result, "Should return null for invalid price");
		assertEquals(5, testData.getWorkspaces().size(), "Workspace list should remain unchanged");
	}
	
	// Test 5
	// Simulates failed admin login
	// Should block creation even if inputs are valid
	@Test
	void testAdminNotLoggedIn() {
		CreateWorkspaceBean tempBean = new CreateWorkspaceBean();
		tempBean.setWorkspaceData(testData);

		// Simulate invalid login
		tempBean.setAdminLoginBean(simulateLogin("hacker", "wrongpass"));

		tempBean.setName("Unauthorized");
		tempBean.setDescription("Should not be created");
		tempBean.setRoomType("Private Office");
		tempBean.setMaxCapacity(3);
		tempBean.setPrice(20);

		String result = tempBean.createWorkspace();
		assertNull(result, "Should return null when admin is not logged in");
		assertEquals(5, testData.getWorkspaces().size(), "Workspace list should remain unchanged");
	}
}

