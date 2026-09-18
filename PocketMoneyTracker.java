import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Expense {
    String date;
    String category;
    double amount;
    String description;

    public Expense(String date, String category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "%-12s | %-12s | Rs%-8.2f | %s",
                date, category, amount, description
        );
    }

    public String toFileFormat() {
        return date + "|" + category + "|" + amount + "|" + description;
    }
}

public class PocketMoneyTracker {

    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {

        loadDataFromFile();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=====================================");
        System.out.println("   Welcome to PocketMoney Manager!  ");
        System.out.println("=====================================");

        while (running) {

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Add an expense");
            System.out.println("2. View all expenses");
            System.out.println("3. Show total spent");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                addExpense(scanner);

            } else if (choice.equals("2")) {
                viewExpenses();

            } else if (choice.equals("3")) {
                showTotalSpent();

            } else if (choice.equals("4")) {
                System.out.println("Saving your data... Bye!");
                saveDataToFile();
                running = false;

            } else {
                System.out.println("Oops, that's not a valid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void addExpense(Scanner scanner) {

        try {
            System.out.print("Enter date (e.g., 12-Oct): ");
            String date = scanner.nextLine();

            System.out.print("Category (Food/Travel/Study/Other): ");
            String category = scanner.nextLine();

            System.out.print("Amount spent: Rs");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Error: Amount must be greater than zero.");
                return;
            }

            System.out.print("Short description (e.g., Pizza with friends): ");
            String description = scanner.nextLine();

            Expense expense = new Expense(
                    date,
                    category,
                    amount,
                    description
            );

            expenses.add(expense);

            System.out.println("Awesome, expense added!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for the amount!");
        }
    }
  
    private static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println(
                    "No expenses logged yet. You're doing great saving money!"
            );
            return;
        }

        System.out.println("\n--- Your Expense History ---");

        System.out.println(
                String.format(
                        "%-12s | %-12s | %-8s | %s",
                        "Date",
                        "Category",
                        "Amount",
                        "Description"
                )
        );

        System.out.println(
                "--------------------------------------------------------------"
        );

        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }
  
    private static void showTotalSpent() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.amount;
        }

        System.out.println(
                "\n-> Total spent so far: Rs" +
                String.format("%.2f", total)
        );
    }

    private static void loadDataFromFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                String[] parts = line.split("\\|", -1);

                if (parts.length == 4) {

                    try {
                        String date = parts[0];
                        String category = parts[1];
                        double amount = Double.parseDouble(parts[2]);
                        String description = parts[3];

                        Expense expense = new Expense(
                                date,
                                category,
                                amount,
                                description
                        );

                        expenses.add(expense);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Skipping invalid expense record."
                        );
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not load previous data.");

        } catch (Exception e) {
            System.out.println(
                    "Something went wrong loading the file."
            );
        }
    }
    private static void saveDataToFile() {

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Expense expense : expenses) {
                writer.println(expense.toFileFormat());
            }

        } catch (IOException e) {
            System.out.println(
                    "Uh oh, failed to save data to file!"
            );
        }
    }
}
