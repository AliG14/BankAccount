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
    Welcomes the user, opens the menu, handles creating new accounts, and the process of
    interacting with each account

     Initialize class variables here:
        - input variable in order to accept user responses
        - ArrayList to track all bank accounts created
        - Keeping track of passwords - E.C.
    Initialize instance variable for the user's name.
     */
    private String name;

    static ArrayList users = new ArrayList();
    static ArrayList<String> userPasswords = new ArrayList<String>();

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
        System.out.println("1. Transaction, 2. open account, or 3. leave bank");
        int option = intInput();
        if (option == 1) {
            transact_acct();
        } else if (option == 2) {
            open_acct();
        } else if (option == 3) {
            System.out.println("Thank you for using our bank. Goodbye!");
            return;
            // welcome();
        } else {
            System.out.println("Sorry, the option you entered is invalid. Please try again.");
            menu_select();
        }
    }

    public void open_acct() {
        //One option for user - Creates an instance of account and enters it into the arrayList
        System.out.println("Enter a password for your account.");
        Scanner temp_pass = new Scanner(System.in);
        String pass = temp_pass.nextLine();
        userPasswords.add(pass);

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

        Ask user for account number. Keep looping through options
		until user decides to exit.
	    Keep temporary variables to track if the user inputs an integer,
		double, or String.
		Keep another variable to track the user's option choice.
		You will need to use casting here to access the user's account directly
		from the ArrayList.
         */
        System.out.println("Please enter your bank number to make a transaction.");
        int accNum = intInput();
        if (accNum > users.size()) {
            System.out.println("There is no account associated with the account number entered.");
            System.out.println("Returning to main menu...");
            System.out.println();
            menu_select();
        } else {
            System.out.println("Enter the password associated with the account number.");
            Scanner temp_pass = new Scanner(System.in);
            String pass = temp_pass.nextLine();

            if (!pass.equals(userPasswords.get(accNum - 1))) {
                System.out.println("The password you entered is invalid. Returning to main menu.");
                menu_select();
            } else {
                System.out.println("Would you like to 1. deposit, 2. withdraw from your account, 3. view your account summary 4. exit to menu.");
                int choice = intInput();
                if (choice == 1) {
                    System.out.println("How much would you like to deposit?");
                    double amt = doubleInput();
                    Account temp_acc = (Account) users.get(accNum - 1);
                    temp_acc.deposit(amt);
                    menu_select();
                } else if (choice == 2) {
                    System.out.println("How much would you like to withdraw?");
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
	Class variable initialized for the number of accounts. You should
	use this number to help assign a unique account number.
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
        // Accessor; return the account's number.
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
		from the account. lso added to transaction log (ArrayList<String>)
		*/
        if (amt > get_acct_bal()){
            System.out.println("The specified withdrawal amount exceeds your account balance.");
            System.out.println("Returning to main menu...");
            return;
        }
        else{
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
        System.out.println("Account number:" + get_acct_num());
        System.out.println();
        System.out.println("Current balance: $" + get_acct_bal());
        System.out.println();
        System.out.println("Transactions Log");
        System.out.println("________________");
        for (String logs : transactions) {
            System.out.println(logs);
        }
    }
}
