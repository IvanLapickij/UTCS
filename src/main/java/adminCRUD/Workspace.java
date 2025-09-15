package adminCRUD;

public class Workspace {

    private static int countID=0;

    private String name, description, roomType, imagePath;
    private int roomID, maxCapacity;
    private double price;
    private boolean canEdit;
    private String imageFile; // e.g. "open_desk_area.jpg"
    
    public Workspace() {
    	
    }

    public Workspace(String name, String description, String roomType, int maxCapacity,
                     double price, String imageFile) {
        this.roomID = ++countID;
        this.name = name;
        this.description = description;
        this.roomType = roomType;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.imageFile = imageFile;
    }

    public Workspace(String name, String description, String roomType, int maxCapacity, double price) {
        this(name, description, roomType, maxCapacity, price, null);
    }

    // Getters/Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getRoomID() { return roomID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }

    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "Workspace:[" + roomID + ", " + name + ", " + roomType + ", " +
                maxCapacity + ", €" + price + "]";
    }
}
