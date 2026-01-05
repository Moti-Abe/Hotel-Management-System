package hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add a new room
    public static boolean addRoom(int roomNumber, String type, boolean available) {
        String sql = "INSERT INTO rooms(room_number, type, available) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            stmt.setString(2, type);
            stmt.setBoolean(3, available);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage()); // Log for debug
            return false;
        }
    }

    // Delete a room
    public static boolean deleteRoom(int roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update room availability
    public static boolean updateRoomAvailability(int roomNumber, boolean available) {
        String sql = "UPDATE rooms SET available=? WHERE room_number=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get room by number (new for efficiency in BookingDAO)
    public static Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getBoolean("available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all rooms
    public static ObservableList<Room> getAllRooms() {
        ObservableList<Room> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getBoolean("available")
                );
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get available rooms only
    public static List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE available=1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getBoolean("available")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
}