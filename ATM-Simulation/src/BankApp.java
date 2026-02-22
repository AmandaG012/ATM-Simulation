import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApp {private static final String FILE_NAME = "accounts.txt";
    private static Map<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts();

        while (true) {
            System.out.println("\n==== ATM SYSTEM ====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    saveAccounts();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter account number: ");
        String accNum = scanner.next();

        if (accounts.containsKey(accNum)) {
            System.out.println("Account already exists.");
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.next();

        accounts.put(accNum, new Account(accNum, name, 0.0));
        saveAccounts();
        System.out.println("Account created successfully.");
    }

    private static void login() {
        System.out.print("Enter account number: ");
        String accNum = scanner.next();

        if (!accounts.containsKey(accNum)) {
            System.out.println("Account not found.");
            return;
        }

        Account current = accounts.get(accNum);

        while (true) {
            System.out.println("\nWelcome, " + current.getName());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            int choice = getIntInput();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter amount: ");
                        double depositAmount = getDoubleInput();
                        current.deposit(depositAmount);
                        saveAccounts();
                        System.out.println("Deposit successful.");
                        break;

                    case 2:
                        System.out.print("Enter amount: ");
                        double withdrawAmount = getDoubleInput();
                        current.withdraw(withdrawAmount);
                        saveAccounts();
                        System.out.println("Withdrawal successful.");
                        break;

                    case 3:
                        System.out.println("Current balance: $" + current.getBalance());
                        break;

                    case 4:
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String accNum = parts[0];
                String name = parts[1];
                double balance = Double.parseDouble(parts[2]);

                accounts.put(accNum, new Account(accNum, name, balance));
            }
        } catch (IOException e) {
            // need to add files
        }
    }

    private static void saveAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts.values()) {
                writer.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Enter a valid number.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
