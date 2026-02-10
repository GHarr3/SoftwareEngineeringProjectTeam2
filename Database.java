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
            System.out.println("Connection to the database was successful!");
        }
        catch(SQLException e) {
            System.out.println("Could not connect to the database!");
            System.out.println(e.getMessage());
        }
    }

    //  ADD AND REMOVE
    //addPlayer method
    //returns true when a player is successfully added to the database, false otherwise
    public boolean addPlayer(int id, String name) {    
        try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO players2 (id, codename) VALUES (?, ?)")) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            
            return true;
        }

        catch(SQLException e) {
            System.err.println("SQL exception occurred whille attempting to add a user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //printPlayers method
    public void printPlayers() {
        //fetching data from table
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM players2")) {

            //getting column data
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            
            //printing data
            System.out.println("\n\n");
            System.out.println("Player list:");
            System.out.printf("%-10s", "ID:");
            System.out.printf("%-10s", "Codename:");
            System.out.println();

            
            while(rs.next()) {
                for (int i = 1; i <= colsCount; i++) {
                    System.out.printf("%-10s", rs.getString(i));
                }
                System.out.println();
            }
        }
        catch(SQLException e) {
            System.err.println("SQL exception occurred whille attempting to print users: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
}