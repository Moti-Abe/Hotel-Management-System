package hotel;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseSetup {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            // Create Guests Table
            String guestTable = """
                CREATE TABLE IF NOT EXISTS guests (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT NOT NULL
                );
            """;

            // Create Rooms Table (use room_number as PK)
            String roomTable = """
                CREATE TABLE IF NOT EXISTS rooms (
                    room_number INTEGER PRIMARY KEY,
                    type TEXT NOT NULL,
                    available BOOLEAN NOT NULL DEFAULT 1
                );
            """;

            // Create Bookings Table (use room_number, fix foreign key)
            String bookingTable = """
                CREATE TABLE IF NOT EXISTS bookings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    guest_id INTEGER,
                    room_number INTEGER,
                    checkin TEXT,
                    checkout TEXT,
                    status TEXT,
                    FOREIGN KEY(guest_id) REFERENCES guests(id),
                    FOREIGN KEY(room_number) REFERENCES rooms(room_number)
                );
            """;

            stmt.execute(guestTable);
            stmt.execute(roomTable);
            stmt.execute(bookingTable);

            // Insert sample guests if empty (matches your screenshot)
            String countGuests = "SELECT COUNT(*) FROM guests";
            ResultSet rsGuests = stmt.executeQuery(countGuests);
            if (rsGuests.getInt(1) == 0) {
                String insertGuests = """
                    INSERT INTO guests(name, phone) VALUES
                    ('Moti Abe', '+251992172455'),
                    ('Mideksa Shifera', '+251921128734');
                """;
                stmt.executeUpdate(insertGuests);
            }

            // Insert sample rooms if empty (for testing)
            String countRooms = "SELECT COUNT(*) FROM rooms";
            ResultSet rsRooms = stmt.executeQuery(countRooms);
            if (rsRooms.getInt(1) == 0) {
                String insertRooms = """
                    INSERT INTO rooms(room_number, type, available) VALUES
                    (101, 'Single', 1),
                    (102, 'Double', 1);
                """;
                stmt.executeUpdate(insertRooms);
            }

            System.out.println("Tables created and sample data inserted if needed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}