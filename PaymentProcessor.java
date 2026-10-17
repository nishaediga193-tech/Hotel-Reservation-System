public class PaymentProcessor {
    public enum Method { CASH, CARD, UPI }

    public static boolean processPayment(double amount, Method method, String detail) {
        System.out.println();
        System.out.println("  ┌──────────────────────────────────────┐");
        System.out.println("  │        PAYMENT PROCESSING...         │");
        System.out.println("  └──────────────────────────────────────┘");
        System.out.printf("  Amount  : ₹%.2f%n", amount);
        System.out.printf("  Method  : %s%n", method);
        System.out.printf("  Details : %s%n", detail);
        System.out.println("  Processing");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(300);
            }
        } catch (InterruptedException ignored) {}
        System.out.println();
        System.out.println("  ✅ Payment of ₹" + String.format("%.2f", amount) + " successful via " + method + "!");
        System.out.println();
        return true;
    }
}
