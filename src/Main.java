/*
Intro CS 17-18 ~ Ms. Abot
Unit 5: Intro to Java
Project 05: OOP BankAccount
*/
/**
 * @Author: Ali Ghasemian & Matthew Egyed
 * @Since 3/14/18
 */

import java.util.*;

class Main {

    public static void main(String[] args) {
        //Account class tests
        //Account testing1 = new Account(0);
        //Account testing2 = new Account(5);

        //testing1.get_acct_bal();
        //testing2.withdraw(1);
        //testing2.account_summary();
        //testing2.deposit(2);
        //testing2.get_acct_num();

        //Object creation
        ManageBank test1 = new ManageBank();
        ManageBank test2 = new ManageBank();
        ManageBank test3 = new ManageBank();
        test1.welcome();
        test2.welcome();
        test3.welcome();
    }

}

class ManageBank {
    /*
    Welcomes the user, opens the menu, handles creating new accounts, and deals with the process of
    interacting with each account

     Initialize class variables here:
        - input variable in order to accept user responses
        - ArrayList to track all bank accounts created
        - Keeps track of passwords - E.C.
        - Keeps track of security Q and A - E.C
    Instance variable initialized for the user's name.
     */
    private String name;

    static ArrayList users = new ArrayList();

    static ArrayList<String> userPasswords = new ArrayList<String>(); //ArrayList stores passwords in plaintext
    static ArrayList<String> securityQ = new ArrayList(); // ArrayList stores the string for the security questions
    static ArrayList<String> securityA = new ArrayList(); // ArrayList stores the string for the security answers
    static ArrayList<Integer> passwordAttempts = new ArrayList(); //stores amount of password attempts

    public void welcome() {
        //Welcomes an account holder to the bank - Takes input of name and greets them
        System.out.println("Hello, what is your name?");
        Scanner userInput = new Scanner(System.in);
        name = userInput.nextLine();
        System.out.println("Hi " + name + ". Welcome to the bank!");
        menu_select();
    }

    public void menu_select() {
        /*
        First menu of choices for user - gives options of creating accounts, interacting with existing accounts,
        and exiting
         */
        System.out.println("Choose from the following: (Enter the number)");
        System.out.println("1. Transaction");
        System.out.println("2. Open New Account");
        System.out.println("3. Leave Bank");
        int option = intInput();
        if (option == 1) {
            transact_acct();
        } else if (option == 2) {
            open_acct();
        } else if (option == 3) {
            System.out.println("Thank you for using our bank. Goodbye!");
            return;
        } else {
            System.out.println("Sorry, the option you entered is invalid. Please try again.");
            menu_select();
        }
    }

    public void open_acct() {
        /*
        One option for user - Creates an instance of account and enters it into the arrayList
        Adds one security question for the account.
         */
        System.out.print("Enter a password for your account: ");
        Scanner temp_pass = new Scanner(System.in);
        String pass = temp_pass.nextLine();
        userPasswords.add(pass);

        System.out.println("Enter a security question for your account:");
        Scanner temp_Q = new Scanner(System.in);
        String question = temp_Q.nextLine();
        securityQ.add(question);

        System.out.println("Enter the answer to the question:");
        Scanner temp_A = new Scanner(System.in);
        String answer = temp_A.nextLine();
        securityA.add(answer);

        passwordAttempts.add(0);

        System.out.println("How much would you like to deposit as your initial balance?");
        double bal = doubleInput();
        Account acc = new Account(bal);
        users.add(acc);
        System.out.println("Your account number is " + acc.accountNumber);

        System.out.println("Would you like to make a 1. transaction or 2. exit to main menu?");
        int choice = intInput();
        if (choice == 1) {
            transact_acct();
        } else if (choice == 2) {
            welcome();
        } else {
            System.out.println("Sorry, the option you entered is invalid. Exiting to main menu.");
            menu_select();
        }
    }

    public void transact_acct() {
        /*
        Another, more involved option for user - Gives the options to deposit, withdraw,
        view the transaction log, or exit

        Asks user for account number. Keeps looping through options
		until user decides to exit.
	    Keeps temporary variables to track if the user inputs an integer,
		double, or String.
		Tracks users option choice.
         */
        System.out.println("Please enter your bank number to make a transaction.");
        int accNum = intInput();
        if (accNum > users.size()) {
            System.out.println("There is no account associated with the account number entered.");
            System.out.println("Returning to main menu...");
            System.out.println();
            menu_select();
        } else {
            /*
            User is allotted 3 password attempts to enter password. This will continue as long as the
            attempts have not exceeded 3 tries.
             */
            if (passwordAttempts.get(accNum - 1) < 3) { //
                System.out.print("Enter the password associated with the account number: ");
                Scanner temp_pass = new Scanner(System.in);
                String pass = temp_pass.nextLine();

                if (!pass.equals(userPasswords.get(accNum - 1))) {
                    System.out.println("The password you entered is invalid. Returning to main menu.");
                    passwordAttempts.set(accNum - 1, passwordAttempts.get(accNum - 1) + 1);
                    menu_select();
                } else {
                    System.out.println("Would you like to:");
                    System.out.println("1. Deposit");
                    System.out.println("2. Withdraw");
                    System.out.println("3. View Account Summary");
                    System.out.println("4. Exit to Main Menu");
                    int choice = intInput();
                    if (choice == 1) {
                        System.out.print("Enter deposit amount: ");
                        double amt = doubleInput();
                        Account temp_acc = (Account) users.get(accNum - 1);
                        temp_acc.deposit(amt);
                        menu_select();
                    } else if (choice == 2) {
                        System.out.print("Enter withdrawal amount: ");
                        double amt = doubleInput();
                        Account temp_acc = (Account) users.get(accNum - 1);
                        temp_acc.withdraw(amt);
                        menu_select();
                    } else if (choice == 3) {
                        Account temp_acc = (Account) users.get(accNum - 1);
                        temp_acc.account_summary();
                        menu_select();
                    } else if (choice == 4) {
                        menu_select();
                    } else {
                        System.out.println("That option is invalid. Please try again.");
                        menu_select();
                    }
                }
            } else {
                /*
                If all password attempts are failed, the user will have to answer a security question
                to reset password.
                 */
                System.out.println("You have entered the wrong password three times. Answer the security question to reset your password:");
                System.out.print(securityQ.get(accNum - 1) + ": ");

                Scanner temp_ans = new Scanner(System.in);
                String answer = temp_ans.nextLine();

                if (!answer.equals(securityA.get(accNum - 1))) {
                    System.out.println("Password reset failed. Incorrect answer entered. Please try again another time.");
                    passwordAttempts.set(accNum - 1, 0);
                    menu_select();
                } else {
                    System.out.print("Please enter a new password: ");
                    Scanner new_pass = new Scanner(System.in);
                    String newPassword = new_pass.nextLine();
                    userPasswords.set(accNum - 1, newPassword);
                    System.out.println("Successfully reset password. Returning to the main menu...");
                    passwordAttempts.set(accNum - 1, 0);
                    Account temp_account = (Account) users.get(accNum - 1);
                    temp_account.passResetLog();
                    menu_select();
                }
            }
        }
    }

    public int intInput() {
        // Sanitizes inputs and does not return until given an integer
        boolean withinBounds;
        String input;
        int returnValue = 0;
        do {
            withinBounds = true; // It is necessary to define this as true before checking the while loop conditional
            Scanner userInput = new Scanner(System.in);
            input = userInput.next();

            try {
                returnValue = Integer.parseInt(input);
            } catch (NumberFormatException e) { //Catches any exceptions that could come from the above code
                withinBounds = false;
                System.out.println("Please enter an integer:");
            }
        } while (!withinBounds);
        return returnValue;
    }

    public double doubleInput() {
        // Sanitizes inputs and does not return until a double is taken as input
        boolean withinBounds;
        String input;
        double returnValue = 0;
        do {
            withinBounds = true;
            Scanner userInput = new Scanner(System.in);
            input = userInput.next();

            try { //Catches errors to keep the loop looping
                returnValue = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                withinBounds = false;
                System.out.println("Please enter an number:");
            }
        } while (!withinBounds);
        return returnValue;
    }
}

class Account {
    /*
    This utilizes abstraction by holding all the necessary variables and information in one class
    Instance variables initialized here:
        - balance
		- account number
		- transaction log (ArrayList)
	Class variable initialized for the number of accounts.
	*/

    private double balance;
    public int accountNumber;
    private ArrayList<String> transactions = new ArrayList();

    static int numAccounts;

    public Account(double bal) {
        /*
        Constructor; creates account object. Initializes the
		object's balance & assigns account number.
		*/
        balance = bal;
        transactions.add("The account started with: $" + bal);
        numAccounts += 1;
        accountNumber = numAccounts;
    }

    public int get_acct_num() {
        // Accessor; returns the account's number.
        return accountNumber;
    }

    public double get_acct_bal() {
        // Accessor; returns the account's balance.
        return balance;
    }

    public void deposit(double amt) {
        /*
        Mutator; deposits the specified amount, amt, into
		the account balance. Also added to transaction log (ArrayList<String>)
		*/
        balance += amt;
        transactions.add("$" + amt + " was deposited.");
        System.out.println("$" + amt + " was deposited.");
    }

    public void withdraw(double amt) {
        /*
        Mutator; withdraws the specified amount, amt, from
		the account balance if the amt will not overdraw
		from the account. Also added to transaction log (ArrayList<String>)
		*/
        if (amt > get_acct_bal()) {
            System.out.println("The specified withdrawal amount exceeds your account balance.");
            System.out.println("Returning to main menu...");
            return;
        } else {
            balance -= amt;
            transactions.add("$" + amt + " was withdrawn.");
            System.out.println("$" + amt + " was withdrawn.");
        }
    }

    public void account_summary() {
        /*
        Prints out the account summary, which includes: account
		number, balance, & transactions log.
		*/
        System.out.println("Account number: " + get_acct_num());
        System.out.println();
        System.out.println("Current balance: $" + get_acct_bal());
        System.out.println();
        System.out.println("Transactions Log");
        System.out.println("________________");

        for (int i = 0; i < transactions.size(); i++){
            System.out.println("| " + (i + 1) + ". " + transactions.get(i));
        }
    }

    public void passResetLog() {
        //Logs the moment relative to transaction occurrences in which the account password was reset.
        transactions.add("Password was reset here.");
    }
}
