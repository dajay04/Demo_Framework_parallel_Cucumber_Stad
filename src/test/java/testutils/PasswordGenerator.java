package testutils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator
{
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";

    public static String generatePassword(int length) {
        if (length < 10) {
            throw new IllegalArgumentException("Password length must be at least 10 characters.");
        }

        SecureRandom random = new SecureRandom();
        List<Character> password = new ArrayList<>();

        // At least one of each
        password.add(UPPER.charAt(random.nextInt(UPPER.length())));
        password.add(LOWER.charAt(random.nextInt(LOWER.length())));
        password.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill rest with random chars from all
        String allChars = UPPER + LOWER + DIGITS + SPECIAL;
        for (int i = 4; i < length; i++) {
            password.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle for random order
        Collections.shuffle(password, random);

        // Build final string
        StringBuilder sb = new StringBuilder();
        for (char c : password) {
            sb.append(c);
        }
        return sb.toString();
    }
}
