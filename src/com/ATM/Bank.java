package com.ATM;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts;


    /**
     * Create a new Bank object with empty lists of users and accounts
     * @param name  the name of the bank
     */
    public Bank(String name) {
        this.name = name;
        this.users=new ArrayList<>();
        this.accounts=new ArrayList<>();
    }





    //getters
    public String getName(){
        return this.name;
    }

    /**
     * Generate a new universally unique ID for a user.
     * @return the uuid
     */
    public String getNewUserUUID(){
        String uuid;
        Random rng=new Random();
        int len=6;
        boolean nonUnique;

        //continuing looping until we get a unique ID
        do {
            //generate the number
            uuid="";
            for (int c=0;c<len;c++){
                uuid+=((Integer)rng.nextInt(10)).toString();
            }
            //check to make sure it's unique
            nonUnique=false;
            for (User u :this.users) {
                if(uuid.compareTo(u.getUUID())==0){
                    nonUnique=true;
                    break;
                }

            }

        }while (nonUnique);


        return uuid;
    }

    /**
     * Generate a new universally unique ID for an account
     * @return
     */

    public String getNewAccountUUID() {

        String uuid;
        Random rng=new Random();
        int len=10;
        boolean nonUnique;

        //continuing looping until we get a unique ID
        do {
            //generate the number
            uuid="";
            for (int c=0;c<len;c++){
                uuid+=((Integer)rng.nextInt(10)).toString();
            }
            //check to make sure it's unique
            nonUnique=false;
            for (Account a :this.accounts) {
                if(uuid.compareTo(a.getUUID())==0){
                    nonUnique=true;
                    break;
                }

            }

        }while (nonUnique);


        return uuid;
    }

    /**
     * Add an account
     * @param account   the account to add
     */
    public void addAccount(Account account){
        this.accounts.add(account);
    }


    /**
     * Create a new user of the bank
     * @param firstName  firstName the user's first name
     * @param lastName   lastName the user's last name
     * @param pin        the user's pin
     * @return           the new User object
     */
    public User addUser(String firstName, String lastName,String pin){
        // create a new User object and add it our list
        User user=new User(firstName,lastName,pin,this);

        this.users.add(user);

        //create a savings account for the user and add to User And Bank
        //accounts lists
        Account account=new Account("Savings",user,this);

        //add to holder and bank lists
        user.addAccount(account);
        this.addAccount(account);

        return user;
    }


    /**
     * Get the User object associated with a particular userId and pin,if they are valid
     * @param userId    the UUID of the user to log in
     * @param pin       the pin of the user
     * @return          the User object, if the login is successful,or null if it is not
     */
    public User userLogin(String userId,String pin){

        //search through list of users
        for(User user: this.users){
            //check user ID is correct
            if(user.getUUID().compareTo(userId)==0 && user.validatePin(pin)){
                return user;
            }
        }

        //if we haven't found the user or have an incorrect pin
        return null;

    }
}
