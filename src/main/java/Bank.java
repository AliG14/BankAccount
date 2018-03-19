/*
 * Copyright (C) 2018 BankAccount - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, March 2018
 */

import org.apache.commons.lang3.RandomStringUtils;
import sql.SQLHandler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Connor Hollasch
 * @since Mar 19, 4:07 AM
 */
public class Bank extends SQLHandler
{
    private static MessageDigest sha256;

    static {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (final Exception e) {
            System.out.println("Error while getting SHA-256 message digest object.");
            e.printStackTrace();
        }
    }

    private Connection db;
    private File bankDBFile;

    public Bank (final String bankFilePath)
    {
        this.bankDBFile = new File(bankFilePath);

        try {
            Class.forName("org.sqlite.JDBC");
            this.db = DriverManager.getConnection("jdbc:sqlite:" + this.bankDBFile.getAbsolutePath());
        } catch (final Exception e) {
            System.out.println("An exception occurred while trying to connect to the bank database.");
            e.printStackTrace();
            return;
        }

        System.out.println("Established connection to bank database successfully.");

        createBankTables();
        System.out.println("Bank tables created or updated successfully.");
    }

    private void createBankTables ()
    {
        doSQL(() -> this.db.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ACCOUNTS("          + "\n" +
                "  USER_ID TEXT PRIMARY KEY NOT NULL,"          + "\n" +
                "  USERNAME TEXT NOT NULL,"                     + "\n" +
                "  EMAIL TEXT NOT NULL,"                        + "\n" +
                "  PASSWORD TEXT NOT NULL,"                     + "\n" +
                "  BALANCE DOUBLE NOT NULL DEFAULT 0,"          + "\n" +
                "  FIRST_NAME TEXT,"                            + "\n" +
                "  LAST_NAME TEXT,"                             + "\n" +
                "  PHONE_NUMBER TEXT,"                          + "\n" +
                "  CONSTRAINT uk_username UNIQUE (USERNAME),"   + "\n" +
                "  CONSTRAINT uk_email UNIQUE (EMAIL)"          + "\n" +
                ");"
        ).execute());

        doSQL(() -> this.db.prepareStatement(
                "CREATE TABLE IF NOT EXISTS TRANSACTIONS("              + "\n" +
                "  TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,"   + "\n" +
                "  USER_ID TEXT NOT NULL,"                              + "\n" +
                "  DESCRIPTION TEXT NOT NULL,"                          + "\n" +
                "  AMOUNT NUMERIC NOT NULL,"                            + "\n" +
                "  OUTGOING INTEGER NOT NULL DEFAULT 1,"                + "\n" +
                "  FOREIGN KEY(USER_ID) REFERENCES ACCOUNTS(USER_ID)"   + "\n" +
                ");"
        ).execute());
    }

    public String openAccount (final String username, final String email, final String password)
    {
        // Assume verification of account details was preformed.
        // If unique key constraint is violated, an SQLException will be thrown and a null user id is returned.
        return doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "INSERT INTO ACCOUNTS(USER_ID, USERNAME, EMAIL, PASSWORD)"   + "\n" +
                    "VALUES (?, ?, ?, ?);"
            );

            final String userId = username.substring(0, 5)  + "-"
                    + RandomStringUtils.randomAlphabetic(5) + "-"
                    + RandomStringUtils.randomNumeric(4);

            ps.setString(1, userId);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, doPasswordHash(password));

            ps.execute();

            return userId;
        });
    }

    public AccountSession login (final String usernameOrEmail, final String password)
    {
        return loginHelper(usernameOrEmail, password, false);
    }

    protected AccountSession loginNoauth (final String usernameOrEmail)
    {
        return loginHelper(usernameOrEmail, null, true);
    }

    private AccountSession loginHelper (final String key, final String password, final boolean noauth)
    {
        final Boolean accountExists = doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT USERNAME"       + "\n" +
                    "FROM ACCOUNTS"         + "\n" +
                    "WHERE USERNAME LIKE ? OR EMAIL LIKE ? OR USER_ID LIKE ?;"
            );

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        });

        if (accountExists == null || !accountExists) {
            return null;
        }

        return doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT USER_ID, PASSWORD"                  + "\n" +
                    "FROM ACCOUNTS"                             + "\n" +
                    "WHERE USERNAME LIKE ? OR EMAIL LIKE ? OR USER_ID LIKE ?;"
            );

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            final ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            final String userId = rs.getString(1);
            final String passwordHash = rs.getString(2);

            if (!noauth && !passwordHash.equals(doPasswordHash(password))) {
                return null;
            }

            return new AccountSession(Bank.this, userId, passwordHash);
        });
    }

    public BankCode validate (final String username, final String password)
    {
        if (username == null || username.isEmpty()) {
            return BankCode.EMPTY_USERNAME;
        }

        if (password == null || password.isEmpty()) {
            return BankCode.EMPTY_PASSWORD;
        }

        if (username.length() < 3) {
            return BankCode.USERNAME_NOT_LONG_ENOUGH;
        }

        if (password.length() < 8) {
            return BankCode.PASSWORD_NOT_LONG_ENOUGH;
        }

        if (accountExists(username)) {
            return BankCode.ACCOUNT_EXISTS;
        }

        return BankCode.OK;
    }

    public boolean accountExists (final String username)
    {
        return doSQL(() -> {
            final PreparedStatement ps = this.db.prepareStatement(
                    "SELECT USERNAME"       + "\n" +
                    "FROM ACCOUNTS"         + "\n" +
                    "WHERE USERNAME LIKE ?;"
            );

            ps.setString(1, username);
            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        });
    }

    public Connection getDb ()
    {
        return this.db;
    }

    private String doPasswordHash (final String password)
    {
        return new String(Bank.sha256.digest(password.getBytes(StandardCharsets.UTF_8)));
    }


}
