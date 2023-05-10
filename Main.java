import java.sql.*;

public class SQLConnection {
    public static void main(String[] args) {
        String url = ""; // just needs correct file 
        String username = "CS3700"; 
        String password = "CS3700";

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection(url, username, password);

            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute a query
            String query = "SELECT * FROM mytable"; // Replace mytable with your table name
            ResultSet rs = stmt.executeQuery(query);

            // Process the result set
            while (rs.next()) {
                // Replace column1, column2, etc. with your column names
                int column1 = rs.getInt("column1");
                String column2 = rs.getString("column2");
                // Print the values
                System.out.println(column1 + " " + column2);
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }
}
