package com.ATM;

import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        //init Scanner
        Scanner sc=new Scanner(System.in);

        //init Bank
        Bank theBank=new Bank("Meli Bank Of Iran");

        //add a user ,Which also creates a savings account

        User user=theBank.addUser("John","Doe","1234");

        //add a checking account for our user
        Account account=new Account("Checking",user,theBank);
        user.addAccount(account);
        theBank.addAccount(account);

        User curUser;
        while (true){
            // stay in the login prompt until successful login
            curUser=ATM.mainMenuPrompt(theBank,sc);

            //stay in main menu until user quites
            ATM.printUserMenu(curUser,sc);
        }



    }

    public static void printUserMenu(User theUser, Scanner sc) {
        //print a summary of the user's accounts
        theUser.printAccountsSummary();

        //init
        int choice;

        //user menu
        do{
            System.out.printf("Welcome %s , what would you like to do?\n",theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdraw");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.println("Enter choice: ");
            choice=sc.nextInt();

            if(choice<1 || choice>5){
                System.out.println("Invalid choice. Please choose 1-5");
            }
        }while (choice<1 || choice>5);

        //process the choice
        switch (choice){
            case 1:
                ATM.showTransHistory(theUser,sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser,sc);
                break;
            case 3:
                ATM.depositFunds(theUser,sc);
                break;
            case 4:
                ATM.transferFunds(theUser,sc);
            break;
        }

        //redisplay this menu unless the user wants to quit

        if(choice!=5){
            ATM.printUserMenu(theUser,sc);
        }

    }


    /**
     * Process a fund deposit to an account
     * @param theUser     the logged-in User object
     * @param sc          the Scanner object used for user input
     */
    private static void depositFunds(User theUser, Scanner sc) {


        //inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;


        //get the accounts to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit in : ", theUser.numAccounts());
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct>=theUser.numAccounts()){
                System.out.println("Invalid account.Please try again.");
            }
        }while (toAcct<0 || toAcct>=theUser.numAccounts());

        acctBal=theUser.getAcctBalance(toAcct);

        //get the amount to transfer
        do {
            System.out.printf("Enter the amount to deposit (max $%.02f): $",acctBal);
            amount=sc.nextDouble();
            if(amount<0){
                System.out.println("Amount must be greater than 0");
            }
        }while (amount<0);


        sc.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo=sc.nextLine();

        //do the withdraw
        theUser.addAcctTransaction(toAcct,amount,memo);


    }

    /**
     * process a fund withdraw from an account
     * @param theUser    the logged-in User object
     * @param sc         the Scanner object user for user input
     */
    private static void withdrawFunds(User theUser, Scanner sc) {

        //inits
        int formAcct;
        int toAcct;
        double amount;
        double acctBal;
        String memo;


        //get the accounts to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ",theUser.numAccounts());
            formAcct=sc.nextInt()-1;
            if(formAcct<0 || formAcct>=theUser.numAccounts()){
                System.out.println("Invalid account.Please try again.");
            }
        }while (formAcct<0 || formAcct>=theUser.numAccounts());

        acctBal=theUser.getAcctBalance(formAcct);

        //get the amount to transfer
        do {
            System.out.printf("Enter the amount to withDraw (max %0.2f): $",acctBal);
            amount=sc.nextDouble();
            if(amount<0){
                System.out.println("Amount must be greater than 0");
            }else if(amount>acctBal){
                System.out.println("Amount must not be greater than\n"+
                        "balance of $%.02f.\n");
            }
        }while (amount<0 || amount>acctBal);


        sc.nextLine();

        System.out.println("Enter a memo: ");
        memo=sc.nextLine();

        //do the withdraw
        theUser.addAcctTransaction(formAcct,-1 *amount,memo);



    }


    /**
     * process transferring funds from one account to another
     * @param theUser    the logged-in User object
     * @param sc         the Scanner object used for user input
     */
    private static void transferFunds(User theUser, Scanner sc) {
        //inits
        int formAcct;
        int toAcct;
        double amount;
        double acctBal;


        //get the accounts to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from : ",theUser.numAccounts());
            formAcct=sc.nextInt()-1;
            if(formAcct<0 || formAcct>=theUser.numAccounts()){
                System.out.println("Invalid account.Please try again.");
            }
        }while (formAcct<0 || formAcct>=theUser.numAccounts());

        acctBal=theUser.getAcctBalance(formAcct);

        //get the account to transfer to

        do {
            System.out.printf("Enter the number (1-$d) of the account\n" +
                    "to transfer to : ");
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct>=theUser.numAccounts()){
                System.out.println("Invalid account.Please try again.");
            }
        }   while (toAcct<0 || toAcct>=theUser.numAccounts());


        //get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max %0.2f): $",acctBal);
            amount=sc.nextDouble();
            if(amount<0){
                System.out.println("Amount must be greater than 0");
            }else if(amount>acctBal){
                System.out.println("Amount must not be greater than\n"+
                        "balance of $%.02f.\n");
            }
        }while (amount<0 || amount>acctBal);

        //finally,do the transfer
        theUser.addAcctTransaction(formAcct,-1*amount,String.format("Transfer to account %s",theUser.getAccUUID(toAcct)));
        theUser.addAcctTransaction(toAcct,amount,String.format("Transfer to account %s",theUser.getAccUUID(formAcct)));

        }

    /**
     * Show the transaction history for an account
     * @param theUser  the logged-in User object
     * @param sc       the Scanner object used for user input
     */
    public static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;
        //get account whose transaction to history look at
        do {
            System.out.printf("Enter the number (1- %d) of the account\n"
                    + " whose transactions you want to see: ",theUser.numAccounts());

            theAcct=sc.nextInt()-1;
            if(theAcct<0 || theAcct>=theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        }while (theAcct<0 || theAcct>=theUser.numAccounts());

        //print the transaction history
        theUser.printAcctTransHistory(theAcct);

    }

    /**
     * Print the ATM's login menu
     * @param      theBank the Bank object whose accounts to use
     * @param sc   the Scanner object to use for user input
     * @return
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        String userId;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nWelcome to %s\n\n",theBank.getName());
            System.out.print("Enter user ID: ");
            userId=sc.nextLine();
            System.out.print("Enter pin: ");
            pin=sc.nextLine();

            //try to get the user object corresponding to the ID and pin combo
            authUser=theBank.userLogin(userId,pin);

            if (authUser==null){
                System.out.println("Incorrect user ID/pin combination." +"Please try again.");
            }
        }while (authUser==null); //continue looping until successful login

        return authUser;
    }
}
