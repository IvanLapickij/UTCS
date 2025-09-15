package adminCRUD;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkspaceTest {

    private Workspace workspace;

    // Run before each test to reset static ID and create a fresh workspace
    @BeforeEach
    void setUp() throws Exception {
        // Reset the static room ID counter for consistent test output
        Field field = Workspace.class.getDeclaredField("countID");
        field.setAccessible(true);
        field.set(null, 0);

        // Create a workspace with typical values
        workspace = new Workspace("Innovation Hub", "Creative open space", "Open Desk Area", 12, 25.0, "open-desk.jpg");

        System.out.println("Workspace created: " + workspace);
    }

    // Test 1
    // Checks that all fields are set correctly by the constructor
    @Test
    void testConstructorSetsFieldsCorrectly() {
        assertEquals("Innovation Hub", workspace.getName());
        assertEquals("Creative open space", workspace.getDescription());
        assertEquals("Open Desk Area", workspace.getRoomType());
        assertEquals(12, workspace.getMaxCapacity());
        assertEquals(25.0, workspace.getPrice());
        assertEquals("open-desk.jpg", workspace.getImageFile());
    }

    // Test 2
    // Make sure canEdit starts off false and can be toggled
    @Test
    void testCanEditSetterAndGetter() {
        assertFalse(workspace.isCanEdit(), "Expected canEdit to be false by default");

        workspace.setCanEdit(true);
        assertTrue(workspace.isCanEdit(), "Expected canEdit to be true after setting it");
    }

    // Test 3
    // Verifies that imagePath can be updated and retrieved
    @Test
    void testImagePathSetterAndGetter() {
        workspace.setImagePath("/images/innovation.jpg");
        assertEquals("/images/innovation.jpg", workspace.getImagePath());
    }

    // Test 4
    // toString() should return a clean, readable summary
    @Test
    void testToStringFormat() {
        String output = workspace.toString();
        assertTrue(output.contains("Workspace:["));
        assertTrue(output.contains("Innovation Hub"));
        assertTrue(output.contains("Open Desk Area"));
        assertTrue(output.contains("€25.0"));
    }

    // Test 5
    // Using the constructor that doesn't include an image file
    @Test
    void testConstructorWithoutImageFile() {
        Workspace simple = new Workspace("Brainstorm Bay", "Team breakout area", "Meeting Pod", 6, 18.0);
        System.out.println("Workspace created: " + simple);
        assertNull(simple.getImageFile(), "Expected imageFile to be null when not provided");
        assertEquals("Meeting Pod", simple.getRoomType());
        assertEquals("Brainstorm Bay", simple.getName());
    }

    // Test 6
    // Create a workspace for events and validate the details
    @Test
    void testEventAreaWorkspace() {
        Workspace event = new Workspace("Grand Hall", "Spacious for events", "Event Area", 30, 100.0, "event.jpg");
        System.out.println("Workspace created: " + event);
        assertEquals("Event Area", event.getRoomType());
        assertEquals(30, event.getMaxCapacity());
        assertEquals("event.jpg", event.getImageFile());
    }

    // Test 7
    // Create a conference room and ensure price and type are valid
    @Test
    void testConferenceRoomWorkspace() {
        Workspace conf = new Workspace("Strategy Green Room", "Good for board meetings", "Conference Room", 15, 60.0, "conf.jpg");
        System.out.println("Workspace created: " + conf);
        assertEquals("Conference Room", conf.getRoomType());
        assertTrue(conf.getPrice() >= 50);
    }

    // Test 8
    // Try creating a room with the minimum allowed capacity
    @Test
    void testMinimumCapacity() {
        Workspace tiny = new Workspace("MossPod", "For individual meeting", "Meeting Pod", 1, 5.0, "tiny.jpg");
        System.out.println("Workspace created: " + tiny);
        assertEquals(1, tiny.getMaxCapacity());
        assertTrue(tiny.getPrice() > 0);
    }
}

