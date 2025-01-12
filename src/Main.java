import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the first three digits of the account number: ");
            String input = scanner.nextLine().trim();

            if (!isValidInput(input)) {
                System.out.println("Invalid format. Please enter exactly 3 digits. Only digits are allowed!");
            } else {
                processBankSearch(input);
            }

            System.out.print("Do you want to search for another bank? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("y")) {
                System.out.println("Exiting the program. Cya!");
                break;
            }
        }
    }

    private static boolean isValidInput(String input) {
        return input.length() == 3 && input.matches("\\d{3}");
    }

    private static void processBankSearch(String input) {
        try {
            URL url = new URL("https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                searchBank(reader, input);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while loading the file: " + e.getMessage());
        }
    }

    private static void searchBank(BufferedReader reader, String input) throws Exception {
        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(input)) {
                String[] parts = line.split("\\t+", -1);

                if (parts.length > 4) {
                    String bankCode = parts[0].trim();
                    String bankName = parts[1].trim();

                    System.out.println("Bank code: " + bankCode);
                    System.out.println("Bank name: " + bankName);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("No bank found for the entered code: " + input);
        }
    }
}
