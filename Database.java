//Database Class
//For Software Engineering
//With Professor Strother
//U of A Spring 2026
//By Gus harr


/*
            DATABASE CLASS


for interacting with the PostGreSQL Database
*/


import java.sql.*;



public class Database {

    //  VARS
    private static final String URL = "jdbc:postgresql:photon";
    private static Connection connection;

    //  CONSTRUCTOR
    public Database() {
        //establish connection to the database
        try {
            this.connection = DriverManager.getConnection(URL, "student", "student");
            System.out.println("Connection successful!");
        }
        catch(SQLException e) {
            System.out.println("Could not connect to the database!");
            System.out.println(e.getMessage());
        }
    }
}