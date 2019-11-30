package Controllers;

import Models.Vehicle;

public interface RentalVehicleManager {

    void addVehicle(Vehicle vehicle);

    void deleteVehicle(String plateNumber);

    void printVehicle();

    void save();
}
