package hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class BookingDAO {

    // Add booking and set room unavailable
    public boolean addBooking(int guestId, int roomNumber, LocalDate checkin, LocalDate checkout, String status) {
        String sql = "INSERT INTO bookings(guest_id, room_number, checkin, checkout, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            stmt.setInt(2, roomNumber);
            stmt.setString(3, checkin.toString());
            stmt.setString(4, checkout.toString());
            stmt.setString(5, status);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Set room unavailable on successful reserve
                RoomDAO.updateRoomAvailability(roomNumber, false);
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update booking status (Check-in/Check-out)
    public static void updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();

            // If checked out, set room available again
            if (status.equalsIgnoreCase("Checked Out")) {
                int roomNumber = getRoomNumberByBookingId(bookingId);
                RoomDAO.updateRoomAvailability(roomNumber, true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper to get room number from booking
    private static int getRoomNumberByBookingId(int bookingId) {
        String sql = "SELECT room_number FROM bookings WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("room_number");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get all bookings (fix column names, use getRoomByNumber)
    public static ObservableList<Booking> getAllBookings() {
        ObservableList<Booking> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bookings";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int guestId = rs.getInt("guest_id");
                int roomNumber = rs.getInt("room_number");
                Booking booking = new Booking(
                        rs.getInt("id"),
                        GuestDAO.getGuestById(guestId),
                        RoomDAO.getRoomByNumber(roomNumber),
                        LocalDate.parse(rs.getString("checkin")),
                        LocalDate.parse(rs.getString("checkout")),
                        rs.getString("status")
                );
                list.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}