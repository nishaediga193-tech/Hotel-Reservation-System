import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private static int counter = 1000;
    private int bookingId;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;
    private String paymentMethod;
    private boolean active;

    public Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, String paymentMethod) {
        this.bookingId     = ++counter;
        this.guest         = guest;
        this.room          = room;
        this.checkIn       = checkIn;
        this.checkOut      = checkOut;
        this.paymentMethod = paymentMethod;
        long nights        = ChronoUnit.DAYS.between(checkIn, checkOut);
        this.totalAmount   = nights * room.getPricePerNight();
        this.active        = true;
    }

    public void cancel() {
        this.active = false;
        room.setAvailable(true);
    }

    public long getNights() { return ChronoUnit.DAYS.between(checkIn, checkOut); }

    public int getBookingId()       { return bookingId; }
    public Guest getGuest()         { return guest; }
    public Room getRoom()           { return room; }
    public LocalDate getCheckIn()   { return checkIn; }
    public LocalDate getCheckOut()  { return checkOut; }
    public double getTotalAmount()  { return totalAmount; }
    public String getPaymentMethod(){ return paymentMethod; }
    public boolean isActive()       { return active; }

    public void printDetails() {
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.printf( "  │  Booking ID   : %-40d│%n", bookingId);
        System.out.printf( "  │  Guest        : %-40s│%n", guest.getName());
        System.out.printf( "  │  Phone        : %-40s│%n", guest.getPhone());
        System.out.printf( "  │  Email        : %-40s│%n", guest.getEmail());
        System.out.printf( "  │  Room         : %-40d│%n", room.getRoomNumber());
        System.out.printf( "  │  Category     : %-40s│%n", room.getCategory());
        System.out.printf( "  │  Check-In     : %-40s│%n", checkIn);
        System.out.printf( "  │  Check-Out    : %-40s│%n", checkOut);
        System.out.printf( "  │  Nights       : %-40d│%n", getNights());
        System.out.printf( "  │  Rate/Night   : ₹%-39.2f│%n", room.getPricePerNight());
        System.out.printf( "  │  Total Amount : ₹%-39.2f│%n", totalAmount);
        System.out.printf( "  │  Payment      : %-40s│%n", paymentMethod);
        System.out.printf( "  │  Status       : %-40s│%n", active ? "ACTIVE ✓" : "CANCELLED ✗");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
    }
}
