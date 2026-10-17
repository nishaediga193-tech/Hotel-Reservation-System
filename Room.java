public class Room {
    public enum Category { STANDARD, DELUXE, SUITE }
    private int roomNumber;
    private Category category;
    private double pricePerNight;
    private boolean available;
    private String description;

    public Room(int roomNumber, Category category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.available = true;
        switch (category) {
            case STANDARD -> { this.pricePerNight = 1500.00; this.description = "Cozy room with single bed, AC, WiFi"; }
            case DELUXE   -> { this.pricePerNight = 3000.00; this.description = "Spacious room with king bed, TV, AC, WiFi"; }
            case SUITE    -> { this.pricePerNight = 6000.00; this.description = "Luxury suite with lounge, jacuzzi, mini-bar"; }
        }
    }

    public int getRoomNumber()       { return roomNumber; }
    public Category getCategory()    { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable()     { return available; }
    public String getDescription()   { return description; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("Room %3d | %-8s | ₹%7.2f/night | %s | %s",
            roomNumber, category, pricePerNight,
            available ? "AVAILABLE" : "BOOKED   ", description);
    }
}
