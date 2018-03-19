/*
 * Copyright (C) 2018 BankAccount - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, March 2018
 */

import java.util.Scanner;

/**
 * @author Connor Hollasch
 * @since Mar 19, 4:16 AM
 */
public class Main
{
    private Bank bank;
    private Scanner scanner;

    private AccountSession session = null;

    private Main ()
    {
        System.out.println("--------");

        this.bank = new Bank("bank.db");
        this.scanner = new Scanner(System.in);

        System.out.println("--------");

        inputLoop();
    }

    private void inputLoop ()
    {
        while (true) {
            if (this.session != null) {
                sessionLoop();
                continue;
            }

            System.out.println("Please enter '1' to create an account, or '2' to login, anything else will exit.");

            final String next = this.scanner.nextLine();
            int option;
            try {
                option = Integer.parseInt(next);
            } catch (final NumberFormatException e) {
                option = -1;
            }

            if (option == 1) {
                openAccount();
                continue;
            } else if (option == 2) {
                sessionLoop();
                continue;
            } else {
                System.exit(1);
            }
        }
    }

    private void sessionLoop ()
    {
        if (this.session != null) {
            System.out.println("Please enter...");
            System.out.println("  '1' to print account status");
            System.out.println("  '2' to print a transaction list");
            System.out.println("  '3' to transfer money to another account");
            System.out.println("  '4' to insert a transaction");
            System.out.println("  Anything else will logout");

            final String next = this.scanner.nextLine();
            int option;
            try {
                option = Integer.parseInt(next);
            } catch (final NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1:
                    this.session.printAccountDetails();
                    break;
                case 2:
                    this.session.printAccountTransactions();
                    break;
                case 3:
                    System.out.print("Enter the user id of the account transferring to: ");
                    final String to = this.scanner.nextLine();

                    System.out.print("Enter the amount of money to transfer to that account: ");
                    double amount = this.scanner.nextDouble();
                    this.scanner.nextLine();

                    BankCode code = this.session.transferToOtherAccount(to, amount);
                    switch (code) {
                        case ACCOUNT_DOES_NOT_EXIST:
                            System.out.println("The specified account does not exist!");
                            break;
                        case AMOUNT_MUST_BE_POSITIVE:
                            System.out.println("You must enter a non-negative amount to transfer");
                            break;
                        case OK:
                            System.out.println(amount + " transferred to " + to + " successfully.");
                            break;
                    }

                    break;
                case 4:
                    System.out.print("Enter a transaction description: ");
                    final String desc = this.scanner.nextLine();

                    System.out.print("Enter the cost of the transaction (negative would be a deposit): ");
                    amount = this.scanner.nextDouble();
                    this.scanner.nextLine();

                    this.session.insertTransaction(desc, amount);
                    System.out.println("Transaction inserted successfully.");
                    break;
                default:
                    this.session = null;
                    System.out.println("Logged out...");
                    return;
            }

            return;
        }

        System.out.print("Enter your user id, username, or email: ");
        final String key = this.scanner.nextLine();

        System.out.print("Enter your password: ");
        final String password = this.scanner.nextLine();

        this.session = this.bank.login(key, password);

        if (this.session == null) {
            System.out.println("Invalid user id, username, email, or password");
            return;
        }

        System.out.println("Login successful!");
    }

    private void openAccount ()
    {
        System.out.print("Please enter a username: ");
        final String username = this.scanner.nextLine();

        System.out.print("Please enter an email: ");
        final String email = this.scanner.nextLine();

        System.out.print("Please enter a password: ");
        final String password = this.scanner.nextLine();

        final BankCode code = bank.validate(username, password);

        switch (code) {
            case ACCOUNT_EXISTS:
                System.out.println("An account under this name already exists.");
                break;
            case EMPTY_PASSWORD:
                System.out.println("No password specified");
                break;
            case EMPTY_USERNAME:
                System.out.println("No username specified");
                break;
            case PASSWORD_NOT_LONG_ENOUGH:
                System.out.println("Your password must be at least 8 characters.");
                break;
            case USERNAME_NOT_LONG_ENOUGH:
                System.out.println("Your username must be at least 3 characters.");
                break;
            case OK:
                final String userId = bank.openAccount(username, email, password);
                System.out.println("Account created successfully. Your user ID is " + userId);
        }
    }

    public static void main (final String[] args)
    {
        new Main();
    }
}
