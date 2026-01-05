package hotel;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestSQLite {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}
