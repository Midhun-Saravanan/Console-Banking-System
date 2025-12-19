import java.util.*;
import java.io.*;

class Transaction implements Serializable {
    String type;
    double amount;
    double balanceAfter;
    Date date;

    Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return String.format("%s | %-10s | Amount: ₹%.2f | Balance: ₹%.2f",
                date, type, amount, balanceAfter);
    }
}

class User implements Serializable {
    String name, phone, email, pass, accno;
    double balance;
    ArrayList<Transaction> history;

    User(String name, String phone, String email, String pass, String accno) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.accno = accno;
        this.balance = 0.0;
        this.history = new ArrayList<>();
    }

    void addTransaction(String type, double amount) {
        history.add(new Transaction(type, amount, balance));
    }
}

class Admin {
    String username, pass;

    Admin() {
        this.username = "mithxx";
        this.pass = "navin";
    }
}

public class App {
    public static ArrayList<User> users = new ArrayList<>();
    public static Scanner sc = new Scanner(System.in);
    public static User currentUser = null;

    /* ============ USER SIGNUP ============ */
    public static void userSignup() {
        sc.nextLine();
        System.out.print("Enter Your Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Your Phone Number: ");
        String phone = sc.nextLine();
        System.out.print("Enter Your Email ID: ");
        String email = sc.nextLine();
        System.out.print("Enter Your Account Number: ");
        String accno = sc.nextLine();
        System.out.print("Create a Password: ");
        String pass = sc.nextLine();

        for (User u : users) {
            if (u.accno.equals(accno)) {
                System.out.println("Account number already exists. Try another one!");
                return;
            }
        }

        users.add(new User(name, phone, email, pass, accno));
        System.out.println("Signup Successful! You can now Sign in.");
    }

    public static int userSignin() {
        sc.nextLine();
        System.out.print("Enter Your Account Number: ");
        String accno = sc.nextLine();
        System.out.print("Enter Your Password: ");
        String pass = sc.nextLine();

        for (User u : users) {
            if (u.accno.equals(accno) && u.pass.equals(pass)) {
                currentUser = u;
                System.out.println("Login Successful!");
                return 1;
            }
        }
        System.out.println("Invalid credentials!");
        return 0;
    }

    public static boolean adminLogin() {
        sc.nextLine();
        System.out.print("Enter Admin Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Admin Password: ");
        String pass = sc.nextLine();
        Admin ad = new Admin();

        if (ad.username.equals(username) && ad.pass.equals(pass)) {
            System.out.println("Admin Logged in Successfully!");
            return true;
        } else {
            System.out.println("Invalid Admin Credentials!");
            return false;
        }
    }

    public static void userDashboard() {
        if (currentUser == null) return;

        System.out.println("\nWelcome, " + currentUser.name + "!");
        while (true) {
            System.out.println("\n----- USER DASHBOARD -----");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Fund Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Enter Your Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: ₹" + currentUser.balance);
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double dep = sc.nextDouble();
                    if (dep <= 0) System.out.println("Invalid amount!");
                    else {
                        currentUser.balance += dep;
                        currentUser.addTransaction("Deposit", dep);
                        System.out.println("₹" + dep + " deposited successfully!");
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double wd = sc.nextDouble();
                    if (wd > currentUser.balance) System.out.println("Insufficient Balance!");
                    else {
                        currentUser.balance -= wd;
                        currentUser.addTransaction("Withdraw", wd);
                        System.out.println("₹" + wd + " withdrawn successfully!");
                    }
                    break;

                case 4:
                    sc.nextLine();
                    System.out.print("Enter recipient account number: ");
                    String toAcc = sc.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double amt = sc.nextDouble();

                    User receiver = null;
                    for (User u : users) {
                        if (u.accno.equals(toAcc)) {
                            receiver = u;
                            break;
                        }
                    }

                    if (receiver == null) {
                        System.out.println("Account not found!");
                    } else if (amt > currentUser.balance) {
                        System.out.println("Insufficient funds!");
                    } else {
                        currentUser.balance -= amt;
                        receiver.balance += amt;
                        currentUser.addTransaction("Transfer to " + toAcc, amt);
                        receiver.addTransaction("Received from " + currentUser.accno, amt);
                        System.out.println("₹" + amt + " transferred successfully to " + receiver.name);
                    }
                    break;

                case 5:
                    System.out.println("\n--- TRANSACTION HISTORY ---");
                    if (currentUser.history.isEmpty())
                        System.out.println("No transactions yet.");
                    else
                        currentUser.history.forEach(System.out::println);
                    break;

                case 6:
                    System.out.println("Logged out successfully!");
                    currentUser = null;
                    return;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void adminDashboard() {
        System.out.println("\n----- ADMIN DASHBOARD -----");
        while (true) {
            System.out.println("1. Add Customer");
            System.out.println("2. Remove Customer");
            System.out.println("3. View All Customers");
            System.out.println("4. View Customer Transactions");
            System.out.println("5. Logout");
            System.out.print("Enter Your Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Account Number: ");
                    String accno = sc.nextLine();
                    System.out.print("Create Password: ");
                    String pass = sc.nextLine();

                    for (User u : users) {
                        if (u.accno.equals(accno)) {
                            System.out.println("Account number already exists!");
                            return;
                        }
                    }
                    users.add(new User(name, phone, email, pass, accno));
                    System.out.println("Customer added successfully!");
                    break;

                case 2:
                    System.out.print("Enter Account Number to remove: ");
                    String removeAcc = sc.next();
                    users.removeIf(u -> u.accno.equals(removeAcc));
                    System.out.println("User removed (if existed)");
                    break;

                case 3:
                    System.out.println("\n--- ALL CUSTOMERS ---");
                    for (User u : users) {
                        System.out.printf("%s | %s | %s | AccNo: %s | Balance: ₹%.2f\n",
                                u.name, u.phone, u.email, u.accno, u.balance);
                    }
                    break;

                case 4:
                    System.out.print("Enter Account Number to view transactions: ");
                    String accView = sc.next();
                    for (User u : users) {
                        if (u.accno.equals(accView)) {
                            System.out.println("--- Transaction History for " + u.name + " ---");
                            if (u.history.isEmpty()) System.out.println("No transactions yet.");
                            else u.history.forEach(System.out::println);
                        }
                    }
                    break;

                case 5:
                    System.out.println("Admin logged out!");
                    return;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Console-based Banking System");

        while (true) {
            System.out.println("\n1. Signup");
            System.out.println("2. Signin");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Enter Your Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    userSignup();
                    break;
                case 2:
                    if (userSignin() == 1) userDashboard();
                    break;
                case 3:
                    if (adminLogin()) adminDashboard();
                    break;
                case 4:
                    System.out.println("Thank you for using the Banking System!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
