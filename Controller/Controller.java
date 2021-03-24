package Controller;


import java.sql.Connection;
import java.sql.DriverManager;

public class Controller {

    private static Connection con;  //Connection to the database

    /**
     * Establishes a connection to the MYSQL database server
     * @return will return a Connection object
     * @throws Exception
     */
    public static Connection getConnection(){
        try {
            String driver = "_INSERT_ACTUAL_";                                      //driver for mySQL
            String url = "_INSERT_ACTUAL_";                                         // IP ADDRESS OF mySQL database
            String username = "_INSERT_ACTUAL_";                                    //username
            String password = "_INSERT_ACTUAL_";                                    //password
            Class.forName(driver);                                                  //initialize the driver
            con = DriverManager.getConnection(url,username, password);   //establish connection to DB
            System.out.println("Connected Successful");                     
            return con;                                                             //return connection if succesful
        }
        catch (Exception e){
            System.out.println("Connection Failed");   
            return null;                                                             //conection failed
        }
    }

    /**
     * hashPassword will turn a String into a hashValue to store into database
     * @param password
     * @return a hash version of the password
     */
    public static long hashPassword(String password){
        long hashValue = password.hashCode();
        return hashValue;
    }






}
