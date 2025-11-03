package testutils;

public class DateUtils
{
    private static final String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    // For tenancy start (numeric values)
    public static String[] splitDateForValue(String dateStr) {
        String[] parts = dateStr.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }
        return new String[]{parts[0], parts[1], parts[2]}; // keep "01", "01", "2026"
    }

    // For tenancy end (month name values)
    public static String[] splitDateForTextMonth(String dateStr) {
        String[] parts = dateStr.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }
        String day = parts[0];              // "01"
        String monthName = MONTH_NAMES[Integer.parseInt(parts[1]) - 1]; // 01 → January
        String year = parts[2];
        return new String[]{day, monthName, year};
    }
}
