package Controllers;

public interface RentalVehicleManager {
    final int MAX_VEHICLES = 50;

    public void addVehicle();
    public void editVehicle(String plateNumber);
    public void deleteVehicle(String plateNumber);
    public void printVehicle();
    public void save();
}
