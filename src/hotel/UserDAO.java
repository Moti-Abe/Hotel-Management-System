package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public UserDAO() {
        createTable();
        insertDefaultAdmin();
    }

    // ---------------------------------------------------
    // Create Users Table
    // ---------------------------------------------------
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL" +
                ");";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Insert a default admin (only once)
    // ---------------------------------------------------
    private void insertDefaultAdmin() {
        String check = "SELECT * FROM users WHERE username = 'admin'";
        String insert = "INSERT INTO users(username, password, role) VALUES('admin', 'admin123', 'admin')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(check);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
                    insertStmt.executeUpdate();
                    System.out.println("Default admin created: username=admin, password=admin123");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Validate Login
    // ---------------------------------------------------
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // true if found

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------
    // Get User Role (Admin / Staff)
    // ---------------------------------------------------
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // not found
    }

    // ---------------------------------------------------
    // Add New User
    // ---------------------------------------------------
    public boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("ERROR adding user: " + e.getMessage());
            return false;
        }
    }
}
