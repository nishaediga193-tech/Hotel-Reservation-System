import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        initializeRooms();
    }

    private void initializeRooms() {
        // Standard rooms: 101-110
        for (int i = 101; i <= 110; i++) rooms.add(new Room(i, Room.Category.STANDARD));
        // Deluxe rooms: 201-207
        for (int i = 201; i <= 207; i++) rooms.add(new Room(i, Room.Category.DELUXE));
        // Suite rooms: 301-303
        for (int i = 301; i <= 303; i++) rooms.add(new Room(i, Room.Category.SUITE));
    }

    public void displayAllRooms() {
        System.out.println("\n  ═══════════════════════════════════════════════════════════════════");
        System.out.println("                       " + name.toUpperCase() + " — ALL ROOMS");
        System.out.println("  ═══════════════════════════════════════════════════════════════════");
        Room.Category current = null;
        for (Room r : rooms) {
            if (r.getCategory() != current) {
                current = r.getCategory();
                System.out.println("  --- " + current + " ROOMS ---");
            }
            System.out.println("  " + r);
        }
        System.out.println();
    }

    public void displayAvailableRooms(Room.Category filter) {
        System.out.println("\n  Available Rooms" + (filter != null ? " [" + filter + "]" : " [All Categories]") + ":");
        System.out.println("  ─────────────────────────────────────────────────────────────────");
        boolean any = false;
        for (Room r : rooms) {
            if (r.isAvailable() && (filter == null || r.getCategory() == filter)) {
                System.out.println("  " + r);
                any = true;
            }
        }
        if (!any) System.out.println("  [!] No rooms available in this category.");
        System.out.println();
    }

    public Room findAvailableRoom(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber && r.isAvailable()) return r;
        }
        return null;
    }

    public Room findRoom(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }

    public Reservation makeReservation(Guest guest, int roomNumber, LocalDate checkIn, LocalDate checkOut, String payment) {
        Room room = findAvailableRoom(roomNumber);
        if (room == null) return null;
        if (!checkOut.isAfter(checkIn)) return null;
        room.setAvailable(false);
        Reservation res = new Reservation(guest, room, checkIn, checkOut, payment);
        reservations.add(res);
        return res;
    }

    public Reservation findReservation(int bookingId) {
        for (Reservation r : reservations) {
            if (r.getBookingId() == bookingId && r.isActive()) return r;
        }
        return null;
    }

    public boolean cancelReservation(int bookingId) {
        Reservation res = findReservation(bookingId);
        if (res == null) return false;
        res.cancel();
        return true;
    }

    public void displayAllReservations() {
        List<Reservation> active = reservations.stream().filter(Reservation::isActive).toList();
        System.out.println("\n  Active Reservations: " + active.size());
        System.out.println("  ─────────────────────────────────────────────────────────────────");
        if (active.isEmpty()) {
            System.out.println("  [!] No active reservations.\n");
            return;
        }
        for (Reservation res : active) {
            System.out.printf("  Booking #%d | Guest: %-15s | Room: %3d %-8s | %s to %s | ₹%.2f%n",
                res.getBookingId(), res.getGuest().getName(), res.getRoom().getRoomNumber(),
                res.getRoom().getCategory(), res.getCheckIn(), res.getCheckOut(), res.getTotalAmount());
        }
        System.out.println();
    }

    public String getName() { return name; }
}
