package hotel;

import javafx.beans.property.*;

public class Guest {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty phone;

    public Guest(int id, String name, String phone) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }

    // Property methods
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty phoneProperty() { return phone; }

    // THIS IS THE KEY FIX
    @Override
    public String toString() {
        return name.get() + " (ID: " + id.get() + ") - " + phone.get();
    }
}