public class Guest {
    private String name;
    private String phone;
    private String email;

    public Guest(String name, String phone, String email) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("%-20s | Phone: %-15s | Email: %s", name, phone, email);
    }
}
