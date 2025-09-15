package viewWorkspaces;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adminCRUD.Workspace;
import adminCRUD.WorkspaceData;

class WorkspaceDataTest {
	
	private WorkspaceData workSpaceData;

	//Method is executed before each test case
	//It creates a new instance of WorkspaceData which initializes the list
	//5 objects are available by default already
	@BeforeEach
	void setUp() throws Exception {
		workSpaceData = new WorkspaceData();
	}
	
	//Test 1
	//Confirms that the constructor in WorkspaceData is populated with data
	@Test
	void testInitialWorkSpace() {
		List<Workspace> workspaces = workSpaceData.getWorkspaces();
		assertEquals(5, workspaces.size(), "Initial workspace count should be 5");
	}
	
	//Test 2
	//Tests to see if a new workspace can be created and added successfully
	//Tests to see if the workspace exists
	//Tests to see if the size of the lists increases by 1
	@Test
	void testAddWorkSpace() {
		Workspace newWorkspace = new Workspace("Test Space", "Test Description", "Test Type", 5, 9.00);
		workSpaceData.addWorkspace(newWorkspace);
		
		assertTrue(workSpaceData.getWorkspaces().contains(newWorkspace), "Workspace should be added");
		assertEquals(6, workSpaceData.getWorkspaces().size(), "Workspace count should be increased to 6");
	}
	
	//Test 3
	//Deletes the first workspace in the list
	//the size of the list decreases to 1
	@Test
	void testDeleteWorkSpace() {
		Workspace deleteWorkspace = workSpaceData.getWorkspaces().get(0);
		workSpaceData.deleteWorkspace(deleteWorkspace);
		
		assertFalse(workSpaceData.getWorkspaces().contains(deleteWorkspace), "Workspace should be deleted");
		assertEquals(4, workSpaceData.getWorkspaces().size(), "Workspace count should be decreased to 4");
	}
	
	//Test 4
	//Ensures when the method editWorkspace is called it enables editing
	//"canEdit" is set to true after being called, otherwise it remains false        
	@Test
	void testEditWorkSpace() {
		Workspace editWorkspace = workSpaceData.getWorkspaces().get(0);
		assertFalse(editWorkspace.isCanEdit(), "initally not able to edit");
		
		workSpaceData.editWorkspace(editWorkspace);
		assertTrue(editWorkspace.isCanEdit(), "Workspace should be editable");
	}
	
	//Test 5
	//Makes sures saveAction disables editing mode for all workspaces
	//enables edit mode for one workspace, then calls saveAction
	//then checks that 'canEdit' is false for all workspaces.
	@Test
	void testSaveAction() {
		Workspace w = workSpaceData.getWorkspaces().get(0);
		workSpaceData.editWorkspace(w);
		assertTrue(w.isCanEdit(), "Should be able to edit before saving");
		
		workSpaceData.updateWorkspace(w);
		assertFalse(w.isCanEdit(), "saveAction() should disable editing ");
	}

}
