package Controllers;

import org.bson.Document;

public interface RentalVehicleManager {
    int MAX_VEHICLES = 50;

    void addVehicle();

    void deleteVehicle(String plateNumber);

    void printVehicle();

    void save();
}
