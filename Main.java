import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Hotel hotel = new Hotel("CodeAlpha Grand Hotel");

    public static void main(String[] args) {
        printBanner();
        System.out.println("  Welcome to " + hotel.getName() + "!");
        System.out.println("  Total rooms: 20 (10 Standard | 7 Deluxe | 3 Suite)\n");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("  Enter choice: ");
            System.out.println();
            switch (choice) {
                case 1 -> hotel.displayAllRooms();
                case 2 -> searchAvailableRooms();
                case 3 -> makeReservation();
                case 4 -> viewReservation();
                case 5 -> cancelReservation();
                case 6 -> hotel.displayAllReservations();
                case 7 -> { System.out.println("  Thank you for choosing " + hotel.getName() + "! 🏨"); running = false; }
                default -> System.out.println("  [!] Invalid option. Try again.\n");
            }
        }
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║   🏨 HOTEL RESERVATION SYSTEM  v1.0          ║");
        System.out.println("  ║   CodeAlpha Java Internship — Task 4         ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    static void printMenu() {
        System.out.println("  ┌────────────────────────────────────────┐");
        System.out.println("  │              MAIN MENU                 │");
        System.out.println("  ├────────────────────────────────────────┤");
        System.out.println("  │  1. View All Rooms                     │");
        System.out.println("  │  2. Search Available Rooms             │");
        System.out.println("  │  3. Make a Reservation                 │");
        System.out.println("  │  4. View Booking Details               │");
        System.out.println("  │  5. Cancel Reservation                 │");
        System.out.println("  │  6. View All Active Bookings           │");
        System.out.println("  │  7. Exit                               │");
        System.out.println("  └────────────────────────────────────────┘");
    }

    static void searchAvailableRooms() {
        System.out.println("  Filter by category:");
        System.out.println("    1. Standard  (₹1,500/night)");
        System.out.println("    2. Deluxe    (₹3,000/night)");
        System.out.println("    3. Suite     (₹6,000/night)");
        System.out.println("    4. All rooms");
        int choice = readInt("  Choose: ");
        Room.Category cat = switch (choice) {
            case 1 -> Room.Category.STANDARD;
            case 2 -> Room.Category.DELUXE;
            case 3 -> Room.Category.SUITE;
            default -> null;
        };
        hotel.displayAvailableRooms(cat);
    }

    static void makeReservation() {
        System.out.println("  ── Guest Details ──────────────────────────");
        System.out.print("  Full Name  : "); String name  = scanner.nextLine().trim();
        System.out.print("  Phone      : "); String phone = scanner.nextLine().trim();
        System.out.print("  Email      : "); String email = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("  [!] Name and phone are required.\n"); return;
        }
        Guest guest = new Guest(name, phone, email);

        hotel.displayAvailableRooms(null);
        int roomNo = readInt("  Enter Room Number to book: ");

        Room room = hotel.findRoom(roomNo);
        if (room == null) { System.out.println("  [!] Room not found.\n"); return; }
        if (!room.isAvailable()) { System.out.println("  [!] Room " + roomNo + " is not available.\n"); return; }

        LocalDate checkIn  = readDate("  Check-In Date  (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("  Check-Out Date (YYYY-MM-DD): ");

        if (checkIn == null || checkOut == null) { System.out.println("  [!] Invalid dates.\n"); return; }
        if (!checkOut.isAfter(checkIn)) { System.out.println("  [!] Check-out must be after check-in.\n"); return; }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double total = nights * room.getPricePerNight();
        System.out.printf("%n  Summary: Room %d (%s) × %d nights = ₹%.2f%n", roomNo, room.getCategory(), nights, total);

        System.out.println("  Payment Method:");
        System.out.println("    1. Cash  2. Card  3. UPI");
        int pm = readInt("  Choose: ");
        PaymentProcessor.Method method = switch (pm) {
            case 2 -> PaymentProcessor.Method.CARD;
            case 3 -> PaymentProcessor.Method.UPI;
            default -> PaymentProcessor.Method.CASH;
        };

        String detail = "";
        if (method == PaymentProcessor.Method.CARD) {
            System.out.print("  Card last 4 digits: "); detail = "CARD ending in " + scanner.nextLine().trim();
        } else if (method == PaymentProcessor.Method.UPI) {
            System.out.print("  UPI ID: "); detail = scanner.nextLine().trim();
        } else {
            detail = "Cash payment at counter";
        }

        PaymentProcessor.processPayment(total, method, detail);

        Reservation res = hotel.makeReservation(guest, roomNo, checkIn, checkOut, method.name());
        if (res != null) {
            System.out.println("  ✅ Reservation confirmed! Your Booking ID: " + res.getBookingId());
            res.printDetails();
        } else {
            System.out.println("  [!] Reservation failed. Please try again.\n");
        }
    }

    static void viewReservation() {
        int id = readInt("  Enter Booking ID: ");
        Reservation res = hotel.findReservation(id);
        if (res == null) {
            System.out.println("  [!] No active reservation found with ID: " + id + "\n");
        } else {
            res.printDetails();
        }
    }

    static void cancelReservation() {
        int id = readInt("  Enter Booking ID to cancel: ");
        Reservation res = hotel.findReservation(id);
        if (res == null) {
            System.out.println("  [!] No active reservation found with ID: " + id + "\n"); return;
        }
        System.out.printf("  Cancelling booking for %s, Room %d, Check-in: %s%n",
            res.getGuest().getName(), res.getRoom().getRoomNumber(), res.getCheckIn());
        System.out.print("  Confirm cancellation? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            hotel.cancelReservation(id);
            System.out.println("  ✅ Reservation #" + id + " has been cancelled. Room is now available.\n");
        } else {
            System.out.println("  [!] Cancellation aborted.\n");
        }
    }

    static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Invalid date format. Use YYYY-MM-DD (e.g. 2026-10-20).");
            }
        }
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }
}
