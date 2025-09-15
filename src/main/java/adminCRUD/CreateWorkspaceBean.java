package adminCRUD;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import admin.AdminLoginBean;
import loginController.Helper;

@ManagedBean
@ViewScoped
public class CreateWorkspaceBean {

	private static String[] typeOfRoom = {	"Open Desk Area", "Private Office", 
											"Meeting Pod", "Conference Room", "Event Area"};
	final private int MAX_ROOM_CAPACITY = 99;
	private String name, description, roomType;
	private int maxCapacity;
	private double price;
	
	// Admin must be logged in
	@ManagedProperty(value = "#{adminLoginBean}")
	private AdminLoginBean adminLoginBean;
	public void setAdminLoginBean(AdminLoginBean adminLoginBean) {
	    this.adminLoginBean = adminLoginBean;
	}
	
    public String createWorkspace() {
    	
        // Prevent workspace creation if admin is not logged in
        if (adminLoginBean == null || !adminLoginBean.isLoggedIn()) {
            System.out.println("ERROR: Unauthorized attempt to create workspace. Admin is not logged in.");
            Helper.addMessage(FacesMessage.SEVERITY_ERROR, "Unauthorized attempt to create workspace. Admin is not logged in.", null);
	        return null; 
        }
        
    	WorkspaceData workspaceData = getWorkspaceData();
    	
        if (validRoomType()) {
            System.out.println(" + [ " + roomType + " ] is valid");

            if (validCapacity()) {
                System.out.println(" + [ " + maxCapacity + " ] is valid");

                if (validPrice()) {
                    System.out.println(" + [ " + price + " ] is valid");
                    Workspace w = new Workspace(name, description, roomType, maxCapacity, price);
                    workspaceData.addWorkspace(w);
                    clearWorkspaceForm();
                    return "workspaces";
                }

                System.out.println("ERROR: Not a valid price");
                addMessage("form-container:price", "Price can't be negative or 0.");

            } else {
                System.out.println("ERROR: Not a valid room capacity");
                addMessage("form-container:maxCapacity", "Not a valid room capacity (1 - 99).");
            }

        } else {
            System.out.println("ERROR: Not a valid room type");
            addMessage("form-container:roomType", "Not a valid room type.");
        }
    	
    	return null;
    }
    
    // Method to handle displaying error messages 
    private void addMessage(String clientId, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
        }
    }
   
    
    private WorkspaceData testWorkspaceData;  // For unit test injection
    public void setWorkspaceData(WorkspaceData workspaceData) { this.testWorkspaceData = workspaceData;}
    private WorkspaceData getWorkspaceData() {
        if (testWorkspaceData != null) {
            return testWorkspaceData;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        return app.evaluateExpressionGet(context, "#{workspaceData}", WorkspaceData.class);
    }
    
    public String cancel() {
    	System.out.println("CANCEL workspace creation. Return to Admin Dashboard. ");
    	clearWorkspaceForm();
    	return ("adminDashboard");
    }
    
    public void clearWorkspaceForm() {
    	name = null;
    	description = null;
    	roomType = null;
    	maxCapacity = 0;
    	price = 0;
    }
    
    // Not sure if this is necessary, input could be handle by a drop-down menu on the frontend 
    public boolean validRoomType() {
    	for(String s: typeOfRoom) {
    		if(roomType.equals(s)) { return true; }
    	}
    	return false;
    }
    
    // Could use switch cases here to have different capacities for the different room types.
    public boolean validCapacity() {
    	return maxCapacity >= 1 && maxCapacity <= MAX_ROOM_CAPACITY;
    }
    
    public boolean validPrice() {
    	return price > 0;
    }
    
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public String getRoomType() {return roomType;}
	public void setRoomType(String roomType) {this.roomType = roomType;}
	public int getMaxCapacity() {return maxCapacity;}
	public void setMaxCapacity(int maxCapacity) {this.maxCapacity = maxCapacity;}
	public double getPrice() {return price;}
	public void setPrice(double price) {this.price = price;}
    
}
