package com.ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class User {
    /**
     * The first name of the user.
     */
    private String firstName;
    /**
     * The last name of the user.
     */
    private String lastName;
    /**
     * The  ID number of the user.
     */
    private String uuid;

    /**
     * The MD5 hash of the user's pin number.
     */
    private byte[] pinHash;

    /**
     * The list of accounts for this user.
     */
    private ArrayList<Account> accounts;


    /**
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param pin       the user's account pin number
     * @param theBank   the bank object that user is a customer
     */
    public User(String firstName,String lastName,String pin,Bank theBank){
        //set user's name
        this.firstName=firstName;
        this.lastName=lastName;

        //store the pin's MD5, rather than the original value, for security reasons
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            this.pinHash=md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        //get a new, unique universal ID for the user
        this.uuid=theBank.getNewUserUUID();

        //create empty list of accounts
        this.accounts=new ArrayList<>();

        //print log message
        System.out.printf("New user %s, %s with Id %s created\n ",firstName,lastName,this.uuid);
    }


    //getters


    public String getFirstName() {
        return firstName;
    }

    /**
     *  Add an account for the user
     * @param account     the account to add
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }



    public String getUUID() {
        return uuid;
    }


    /**
     * Check whether a given pin matches the true User pin
     * @param pin  the pin to check
     * @return     whether the pin is valid or not
     */
    public boolean validatePin(String pin) {
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()),this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        return false;
    }

    /**
     * Print summaries for the accounts of this user
     */
    public void printAccountsSummary() {

        System.out.printf("\n\n%s's accounts summary\n",this.firstName);
        for(int a=0;a<this.accounts.size();a++){
            System.out.printf("  %d) %s\n",a+1,this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Get the number of accounts of the user
     * @return   the number of accounts
     */
    public int  numAccounts() {
        return this.accounts.size();
    }

    /**
     * Print transaction history for a particular accounts.
     * @param acctIdx   the index of the accounts to use
     */
    public void printAcctTransHistory(int acctIdx) {
        this.accounts.get(acctIdx).printTransHistory();

    }

    /**
     * Get the balance of a particular account
     * @param acctIdx    the index of the account to use
     * @return           the balance of the account
     */
    public double getAcctBalance(int acctIdx) {

      return this.accounts.get(acctIdx).getBalance();

    }

    /**
     * Get the UUID of a particular account
     * @param acctIdx     the index of the account to use
     * @return            the UUID of the account
     */
    public String getAccUUID(int acctIdx) {

        return this.uuid;
    }

    public void addAcctTransaction(int accIdx, double amount, String memo) {
        this.accounts.get(accIdx).addTransaction(amount,memo);

    }
}
