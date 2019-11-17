package Controllers;

import Models.Vehicle;

public interface RentalVehicleManager {
    int MAX_VEHICLES = 50;

    void addVehicle(Vehicle vehicle);

    void deleteVehicle(String plateNumber);

    void printVehicle();

    void save();
}
