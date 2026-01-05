package hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class GuestDAO {

    public static Guest getGuestById(int id) {
        Guest guest = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM guests WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                guest = new Guest(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guest;
    }
    // INSERT guest
    public static void addGuest(String name, String phone) {
        String sql = "INSERT INTO guests(name, phone) VALUES(?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE guest
    public static void updateGuest(int id, String name, String phone) {
        String sql = "UPDATE guests SET name=?, phone=? WHERE id=?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setInt(3, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE guest
    public static void deleteGuest(int id) {
        String sql = "DELETE FROM guests WHERE id=?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FETCH ALL guests
    public static ObservableList<Guest> getAllGuests() {
        ObservableList<Guest> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM guests";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Guest(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
