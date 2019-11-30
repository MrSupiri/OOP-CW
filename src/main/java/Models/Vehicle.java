package Models;

import java.math.BigDecimal;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class Vehicle implements Comparable<Vehicle> {
    String plateNumber;
    BigDecimal costPerDay;
    VehicleModel vehicleModel;
    double mileage;
    double engineCapacity;
    int seats;
    String transmission;
    private static int vehicleCount;

    Vehicle(String plateNumber, BigDecimal costPerDay, VehicleModel vehicleModel, double mileage, double engineCapacity, int seats, String transmission) {
        this.plateNumber = plateNumber;
        this.costPerDay = costPerDay;
        this.vehicleModel = vehicleModel;
        this.mileage = mileage;
        this.engineCapacity = engineCapacity;
        this.seats = seats;
        this.transmission = transmission;
        vehicleCount += 1;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public BigDecimal getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(BigDecimal costPerDay) {
        this.costPerDay = costPerDay;
    }

    private VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public static int getVehicleCount() {
        return vehicleCount;
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        return this.vehicleModel.getModel().compareTo(vehicle.getVehicleModel().getMake());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return plateNumber.equals(vehicle.plateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "plateNumber='" + plateNumber + '\'' +
                ", costPerDay=" + costPerDay +
                ", vehicleModel=" + vehicleModel +
                ", mileage=" + mileage +
                ", engineCapacity=" + engineCapacity +
                ", seats=" + seats +
                ", transmission='" + transmission + '\'' +
                '}';
    }
}
