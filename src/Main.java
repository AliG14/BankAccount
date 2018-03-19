/*  Intro CS 17-18 ~ Ms. Abot
		Unit 5: Intro to Java
		Project 05: OOP BankAccount */

import java.util.*;

class Main {

  public static void main(String[] args) {

    // Object creation #1
        /* Uncomment the following lines of code as needed to test your code
				in the ManageBank class.

				You must write additional tests for Account class. */


    ManageBank test1 = new ManageBank();
    //ManageBank test2 = new ManageBank();
    //ManageBank test3 = new ManageBank();
    test1.welcome();
    //test2.welcome();
    //test2.menu_select();
    //test3.welcome();
    //test3.menu_select();
  }

}

class ManageBank {
		/* Replace with descriptive comment of class purpose. */

  /* Initialize class variables here:
  - userInput variable (see PWD 01 slides) in order to accept user responses
  - ArrayList to track all bank accounts created
  - any additional ones needed for E.C.
  Initialize an instance variable for the user's name. */
  private String name;

  static ArrayList users = new ArrayList();
  static ArrayList<String> userPasswords = new ArrayList<String>();

  public void welcome() {
				/* Welcome an account holder to your bank - Replace with more descriptive
				comment about purpose of method */
    System.out.println("Hello, what is your name?");
    Scanner userInput = new Scanner(System.in);
    name = userInput.next();
    System.out.println("Hi " + name + ". Welcome to the bank!");
    menu_select();
  }

  public void menu_select() {
				/* First menu of choices for user - Replace with more descriptive
				comment about purpose of method */
    System.out.println("Choose from the following (enter the number)");
    System.out.println("1. Transaction, 2. open account, or 3. exit to main menu.");
    int option = intInput();
    if (option == 1) {
      transact_acct();
    } else if (option == 2) {
      open_acct();
    } else if (option == 3) {
      welcome();
    } else {
      System.out.println("Sorry, the option you entered is invalid. Please try again.");
      menu_select();
    }
  }

  public void open_acct() {
				/* One option for user - Replace with more descriptive
				comment about purpose of method */
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
				/* Another, more involved option for user - Replace with more
				descriptive comment about purpose of method */

				/* Ask user for account number. Keep looping through options
				until user decides to exit.
				Keep temporary variables to track if the user inputs an integer,
				double, or String.
				Keep another variable to track the user's option choice.
				You will need to use casting here to access the user's account directly
				from the ArrayList.*/
    System.out.println("Please enter your bank number to make a transaction.");
    int accNum = intInput();
    if (accNum > users.size()) {
      System.out.println("There is no account associated with the account number entered.");
      System.out.println("Returning to main menu...");
      System.out.println();
      menu_select();
    }
    System.out.println("Enter the password associated with the account number.");
    Scanner temp_pass = new Scanner(System.in);
    String pass = temp_pass.nextLine();

    if (!pass.equals(userPasswords.get(accNum - 1))) {
      System.out.println("The password you entered is invalid. Returning to main menu.");
      menu_select();
    }

    System.out.println("Would you like to 1. deposit, 2. withdraw from your account, 3. view your account summary 4. exit to menu (enter the number next to the options.");
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

  public int intInput() {
    boolean withinBounds;
    String input;
    int returnValue = 0;
    do {
      withinBounds = true;
      Scanner userInput = new Scanner(System.in);
      input = userInput.next();

      try {
        returnValue = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        withinBounds = false;
        System.out.println("Please enter an integer:");
      }
    } while (!withinBounds);
    return returnValue;
  }

  public double doubleInput() {
    boolean withinBounds;
    String input;
    double returnValue = 0;
    do {
      withinBounds = true;
      Scanner userInput = new Scanner(System.in);
      input = userInput.next();

      try {
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
		/* Replace with descriptive comment of class purpose. */

		/* Initialize instance variables here:
		- balance (what data type should this be?)
		- account number
		- transaction log - must be ArrayList (see PWD 01 slides)
		Initialize a class variable for the number of accounts. You should
		use this number to help assign a unique account number. */

  private double balance;
  public int accountNumber;
  private ArrayList<String> transactions = new ArrayList();

  static int numAccounts;

  public Account(double bal) {
				/* Constructor; creates account object. Initializes the
				object's balance & assigns account number. */
    balance = bal;
    transactions.add("The account started with: " + bal);
    numAccounts += 1;
    accountNumber = numAccounts;
  }

  public int get_acct_num() {
				/* Accessor; get the account's number. */
    return accountNumber;
  }

  public double get_acct_bal() {
				/* Accessor; get the account's balance. */
    return balance;
  }

  public void deposit(double amt) {
				/* Mutator; deposit the specified amount, amt, into
				the account balanace. Add this deposit transaction to the
				transaction log & print out a description. */
    balance += amt;
    transactions.add(amt + " was deposited.");
    System.out.println(amt + " was deposited.");
  }

  public void withdraw(double amt) {
				/* Mutator; withdraw the specified amount, amt, from
				the account balanace if the amt will not overdraw
				from the account. Add this withdraw transaction to the
				transaction log & print out a description. */
    balance -= amt;
    transactions.add(amt + " was withdrawn.");
    System.out.println(amt + " was withdrawn.");
  }

  public void account_summary() {
				/* Print out the account summary, which includes: account
				number, balance, & transactions log. Make this summary print
				out in an easy-to-read format.

				Each transaction should print out on a separate line.
				Hint: See Lab 5.3 Starter File A, check_schedule(), for hints. */
    System.out.println("Account number: " + get_acct_num());
    System.out.println();
    System.out.println("Current balance: " + get_acct_bal());
    System.out.println();
    System.out.println("Transactions Log");
    System.out.println("________________");
    for (String logs : transactions) {
      System.out.println(logs);
    }
  }
}
