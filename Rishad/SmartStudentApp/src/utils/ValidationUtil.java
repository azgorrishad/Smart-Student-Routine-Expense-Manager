package utils;

public class ValidationUtil {

    public static boolean isEmptyOrNull(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isValidAmount(String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}
