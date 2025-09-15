package adminCRUD;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class WorkspaceData {

    private ArrayList<Workspace> workspaces = new ArrayList<>();

    // 👇 Added field for confirm dialog
    private String selectedWorkspaceName;

    public WorkspaceData() {
        workspaces.add(new Workspace("Open Desk", "Flexible shared seating, ideal for freelancers.",
                "Open Desk Area", 1, 15.00, "open_desk_area.jpg"));
        workspaces.add(new Workspace("Private Office", "Enclosed office space with privacy and focus.",
                "Private Office", 4, 45.00, "private_office.jpg"));
        workspaces.add(new Workspace("Meeting Pod", "Acoustically treated pods for focused meetings.",
                "Meeting Pod", 6, 30.00, "meeting_pods.jpg"));
        workspaces.add(new Workspace("Conference Room", "Spacious setup for large presentations and collaboration.",
                "Conference Room", 12, 75.00, "conference_room.jpg"));
        workspaces.add(new Workspace("Event Area", "Open space for networking, launches, and events.",
                "Event Area", 50, 150.00, "event_area.jpg"));
    }

    public ArrayList<Workspace> getWorkspaces() { return workspaces; }

    // ✅ Getter/Setter for confirm dialog
    public String getSelectedWorkspaceName() {
        return selectedWorkspaceName;
    }

    public void setSelectedWorkspaceName(String selectedWorkspaceName) {
        this.selectedWorkspaceName = selectedWorkspaceName;
    }

    public String editWorkspace(Workspace w) {
        w.setCanEdit(true);
        return null;
    }

    public String cancelEdit(Workspace w) {
        w.setCanEdit(false);
        return null;
    }

    public String updateWorkspace(Workspace w) {
        if (w == null) return null;
        if (w.getMaxCapacity() < 1 || w.getPrice() < 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid values", null));
            return null;
        }
        w.setImageFile(workspaceGetCorrespondingImage(w));
        w.setCanEdit(false);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated",
                        "Workspace \"" + w.getName() + "\" was updated."));
        return null;
    }

    public String deleteWorkspace(Workspace w) {
        if (w == null) return null;
        boolean removed = workspaces.removeIf(ws -> ws.getRoomID() == w.getRoomID());
        if (removed) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted",
                            "Workspace \"" + w.getName() + "\" was deleted."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Not found",
                            "Workspace could not be found."));
        }
        return null;
    }

    public void addWorkspace(Workspace w) {
        String imagePath = workspaceGetCorrespondingImage(w);
        w.setImageFile(imagePath);
        workspaces.add(w);
    }

    public String workspaceGetCorrespondingImage(Workspace w) {
        String t = w.getRoomType();
        switch (t) {
            case "Open Desk Area": return "open_desk_area.jpg";
            case "Private Office": return "private_office.jpg";
            case "Meeting Pod": return "meeting_pods.jpg";
            case "Conference Room": return "conference_room.jpg";
            case "Event Area": return "event_area.jpg";
            default: return null;
        }
    }

    public int numOfWorkspaces() { return workspaces.size(); }
    
    // Find a workspace by roomID
    public Workspace findById(int id) {
        return workspaces.stream().filter(w -> w.getRoomID() == id).findFirst().orElse(null);
    }

    // For Testing
	public void setWorkspaces(ArrayList<Workspace> workspaces) {
		this.workspaces = workspaces;
	}
}
