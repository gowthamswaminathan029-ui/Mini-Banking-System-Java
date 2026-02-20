import java.io.*;
import java.util.*;

class Account implements Serializable {
    int accNo;
    String name;
    double balance;

    Account(int accNo, String name, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
    }

    void display() {
        System.out.println("Account No: " + accNo);
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
    }
}

public class MiniBankingSystem {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Account> accounts = new ArrayList<>();
    static final double MIN_BALANCE = 500; // minimum balance rule
    static final String FILE_NAME = "accounts.dat";

    public static void main(String[] args) {
        loadFromFile();

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Search Account");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> searchAccount();
                case 5 -> {
                    saveToFile();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // Create Account
    static void createAccount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        sc.nextLine();

        if (findAccount(accNo) != null) {
            System.out.println("Account already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Initial Balance: ");
        double bal = sc.nextDouble();

        if (bal < MIN_BALANCE) {
            System.out.println("Minimum balance must be " + MIN_BALANCE);
            return;
        }

        accounts.add(new Account(accNo, name, bal));
        System.out.println("Account created successfully!");
    }

    // Deposit
    static void deposit() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        Account acc = findAccount(accNo);

        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter amount: ");
        double amt = sc.nextDouble();
        acc.balance += amt;
        System.out.println("Amount deposited!");
    }

    // Withdraw with minimum balance check
    static void withdraw() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        Account acc = findAccount(accNo);

        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter amount: ");
        double amt = sc.nextDouble();

        if (acc.balance - amt < MIN_BALANCE) {
            System.out.println("Cannot withdraw! Minimum balance must be maintained.");
        } else {
            acc.balance -= amt;
            System.out.println("Withdrawal successful!");
        }
    }

    // Search Account
    static void searchAccount() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        Account acc = findAccount(accNo);

        if (acc != null) {
            acc.display();
        } else {
            System.out.println("Account not found!");
        }
    }

    // Find account
    static Account findAccount(int accNo) {
        for (Account a : accounts) {
            if (a.accNo == accNo)
                return a;
        }
        return null;
    }

    // Save to file
    static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(accounts);
        } catch (Exception e) {
            System.out.println("Error saving file!");
        }
    }

    // Load from file
    static void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (ArrayList<Account>) in.readObject();
        } catch (Exception e) {
            accounts = new ArrayList<>(); // first run
        }
    }
}