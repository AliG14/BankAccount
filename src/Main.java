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

        //ManageBank test1 = new ManageBank();
        // ManageBank test2 = new ManageBank();
        //ManageBank test3 = new ManageBank();
        //test1.welcome();
        //test1.menu_select();
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

    ArrayList<String> accounts = new ArrayList<>();


    public void welcome() {
        /* Welcome an account holder to your bank - Replace with more descriptive
        comment about purpose of method */
        System.out.println("Hello, what is your name?");
        Scanner userInput = new Scanner(System.in);
        name = userInput.next();
        System.out.println("Hi " + name + ". Welcome to the bank!");
    }

    public void menu_select() {
        /* First menu of choices for user - Replace with more descriptive
        comment about purpose of method */
        System.out.println("Would you like to make make a transaction?");
        Scanner userInput = new Scanner(System.in);

    }

    public void open_acct() {
        /* One option for user - Replace with more descriptive
        comment about purpose of method */
    }

    public void transact_acct() {
        /* Another, more involved option for user - Replace with more
        descriptive comment aobut purpose of method */
        
        /* Ask user for account number. Keep looping through options
        until user decides to exit. 
        
        Keep temporary variables to track if the user inputs an integer, 
        double, or String. 
        
        Keep another variable to track the user's option choice. 
        
        You will need to use casting here to access the user's account directly
        from the ArrayList.*/

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
    private int accountNumber;
    private ArrayList<String> transactions = new ArrayList<>();

    static int numAccounts;

    public Account(double bal) {
        /* Constructor; creates account object. Initializes the 
        object's balance & assigns account number. */
        balance = bal;
        numAccounts += 1;
        accountNumber = numAccounts;
    }

    public void get_acct_num() {
        /* Accessor; get the account's number. */
        System.out.println("The account number is " + accountNumber);
    }

    public void get_acct_bal() {
        /* Accessor; get the account's balance. */
        System.out.println("The account balance is " + balance);
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
        System.out.println("Transactions Log");
        System.out.println("________________");
        for (String logs : transactions){
            System.out.println(logs);
        }

    }
}