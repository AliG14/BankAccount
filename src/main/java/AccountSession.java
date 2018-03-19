/*
 * Copyright (C) 2018 BankAccount - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, March 2018
 */

import sql.SQLHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * @author Connor Hollasch
 * @since Mar 19, 4:51 AM
 */
public class AccountSession extends SQLHandler
{
    private Bank bank;
    private Connection db;

    private UUID sessionUUID;

    private String userId;
    private String passwordHash;

    public AccountSession (final Bank bank, final String userId, final String passwordHash)
    {
        this.bank = bank;
        this.db = bank.getDb();

        this.sessionUUID = UUID.randomUUID();

        this.userId = userId;
        this.passwordHash = passwordHash;
    }

    public void insertTransaction (final String description, final double cost)
    {
        doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "INSERT INTO TRANSACTIONS(USER_ID, DESCRIPTION, AMOUNT, OUTGOING)"    + "\n" +
                    "VALUES(?, ?, ?, ?);"
            );

            ps.setString(1, this.userId);
            ps.setString(2, description);
            ps.setDouble(3, cost);
            ps.setInt(4, cost >= 0 ? 1 : 0);

            ps.execute();
            return null;
        });

        updateBalance(cost * -1);
    }

    public BankCode transferToOtherAccount (final String otherUser, final double amount)
    {
        if (!bank.accountExists(otherUser)) {
            return BankCode.ACCOUNT_DOES_NOT_EXIST;
        }

        if (amount <= 0) {
            return BankCode.AMOUNT_MUST_BE_POSITIVE;
        }

        insertTransaction("Transfer to " + otherUser, amount);
        this.bank.loginNoauth(otherUser).insertTransaction("Transfer from " + this.userId, amount * -1);

        return BankCode.OK;
    }

    private void updateBalance (final double adding)
    {
        doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE USER_ID LIKE ?"
            );

            ps.setDouble(1, adding);
            ps.setString(2, this.userId);

            ps.execute();
            return null;
        });
    }

    public double getAccountBalance ()
    {
        return doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT BALANCE FROM ACCOUNTS WHERE USER_ID LIKE ?"
            );

            ps.setString(1, this.userId);
            final ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("An error occurred while trying to get the account balance for " + this.userId);
                return 0.0;
            }

            return rs.getDouble(1);
        });
    }

    public void printAccountDetails ()
    {
        doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT * FROM ACCOUNTS WHERE USER_ID LIKE ?"
            );

            ps.setString(1, this.userId);
            final ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("An error occurred while retrieving account data.");
                return null;
            }

            int colIdx = 1;
            final String userId = rs.getString(colIdx++);
            final String username = rs.getString(colIdx++);
            final String email = rs.getString(colIdx++);

            // Ignore password
            ++colIdx;

            final double balance = rs.getDouble(colIdx++);
            final String firstName = rs.getString(colIdx++);
            final String lastName = rs.getString(colIdx++);
            final String phoneNumber = rs.getString(colIdx);

            System.out.println("Account details:");
            System.out.println("  User ID: " + userId);
            System.out.println("  Account Balance: " + balance);
            System.out.println("  Username: " + username);
            System.out.println("  Email: " + email);
            System.out.println("  First Name: " + firstName);
            System.out.println("  Last Name: " + lastName);
            System.out.println("  Phone Number: " + phoneNumber);

            return null;
        });
    }

    public void printAccountTransactions ()
    {
        doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT TRANSACTION_ID, DESCRIPTION, AMOUNT, OUTGOING FROM TRANSACTIONS WHERE USER_ID LIKE ?"
            );

            ps.setString(1, this.userId);
            final ResultSet rs = ps.executeQuery();

            System.out.println("Transactions:");

            while (rs.next()) {
                final String id = rs.getString(1);
                final String desc = rs.getString(2);
                final double amount = rs.getDouble(3);
                final boolean outgoing = rs.getInt(4) == 1;

                System.out.println("  " + id + ") " + desc + " (" + (outgoing ? "-" : "+") + amount + ")");
            }

            return null;
        });
    }

    public UUID getSessionUUID ()
    {
        return this.sessionUUID;
    }
}
