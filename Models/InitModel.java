/**
 * CEN4010 - Calc-Assist
 * InitModel is a Model Class that deals with the Registraition of a User, Login, and Changing of password Credentials.
 * Project by Team 1 
 */

package Models;

import Controller.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class InitModel {
    //establish private connection to the database 
    private static Connection con = Controller.getConnection();

    //userID will be passed to other Classes to access given user information
    private int userID;

    /**
     * This method will use the information given to the registration screen and try and create a new user
     * @param username must be a unique username 
     * @param password password will be hashed then put into database
     * @param confirmedPassword password will be hashed again and verified against initial hash
     * @return method will return 0 if successful and -1 if something went wrong, and 1 if user already in system
     */
    public int RegisterNewUser(String username, String password, String confirmedPassword, boolean Status){
        try {
            String SQL = "SELECT * FROM users WHERE username = " + username + ";";
            Statement st = con.createStatement();
            ResultSet results = st.executeQuery(SQL);

            if (!results.wasNull()){
                //verify password matched confirmed password
                long hashedPassword = Controller.hashPassword(password);
                long confirmedHash = Controller.hashPassword(confirmedPassword);
                if (hashedPassword != confirmedHash) { 
                    throw new Exception("Password did not match Confirmed Password");
                }

                //set student or teacher status
                char statusAsAString = 'N';
                if (!Status) {statusAsAString = 'S';}
                else {statusAsAString = 'T';}

                //create the SQL query using the above information
                SQL = "INSERT INTO users (Username, Password, Level) VALUES ( "  +
                        username + "," +
                        hashedPassword + "," +
                        statusAsAString + ");";
                st = con.createStatement();
                int m = st.executeUpdate(SQL);

                //if excecuted properly will return 0
                if (m == 0){ return 0;}
                else {
                    throw new Exception("Something went Wrong when Updating SQL");
                }
            //if username already exist will return 1
            } else {
                throw new Exception("Userename Already Exisit");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }


    /**
     * UserLogin takes the username and password input and returns whether user has valid credentials
     * @param username find username in database
     * @param password match password against username in database
     * @return 0 if valid, -1 if invalid, 1 if username not registered
     */
    public int UserLogin( String username, String password){
        try {
            //Make SQL Query for username in Database
            String SQL = "SELECT * FROM users WHERE username = " + username;
            Statement st = con.createStatement();
            ResultSet usernameSet = st.executeQuery(SQL);
            //make sure usernameSet returned is not null
            if (usernameSet.wasNull()){
                throw new Exception("username not in system");
            }
            //
            String passwordHash = usernameSet.getString("Password");
            long testingHashValue = Controller.hashPassword(password);
            if ((String.valueOf(testingHashValue)).compareTo(passwordHash) == 0){
                return 0;
            }
            else {
                throw new Exception("Passwords do not match");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


    /**
     * returns teacher or student status 
     * returns 0 if student and 1 if teacher 
     * will only return -1 if something went wrong
     */
    public int getUserStatus() throws Exception {
        try {
            //create SQL query 
            String SQL = "SELECT Level FROM users WHERE id = " + userID + ";";
            Statement st = con.createStatement();
            ResultSet status = st.executeQuery(SQL);

            //validate status returned was not null
            if (status.wasNull()){
                throw new Exception("no users found");
            }

            //S = Student
            //T = Teacher
            if (status.getString("level") == "S") {
                return 0;
            }
            else {
                return 1;
            }
        } catch (Exception e) {
            System.out.println("error occured when excecuting database query");
        }
        return -1;
    }


    /**
     * Method to set a new password for a user
     * @param status 
     * @throws Exception
     */
    public void setUserPassword(String username, String oldPassword, String newPassword){
        try {
            // hash the old password and hash the new password
            long oldPasswordHash = Controller.hashPassword(oldPassword);
            long passwordHashValue = Controller.hashPassword(newPassword);

            //Create SQL query to get the user information and make sure user exist in database
            String SQL = "SELECT password FROM users WHERE username = " + username +";";
            Statement st = con.createStatement();
            ResultSet set = st.executeQuery(SQL);
            if(set.wasNull()){
                throw new Exception("Username does not exist in our database");
            }

            //if username is valid get the password hash for current password in database 
            // compare against old password if they are different throw exception
            String dbPassword = set.getString("password");
            if (!(String.valueOf(oldPasswordHash)).equals(dbPassword)) {
                throw new Exception("Old Password Does not match");
            }

            // Change password to new password in database once everything was validated
            SQL = "UPDATE users SET password =" + passwordHashValue + " WHERE username = " + username + ";";
            st = con.createStatement();
            int success = st.executeUpdate(SQL);
            if (success == 0){
                System.out.println("Successful password change");
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
