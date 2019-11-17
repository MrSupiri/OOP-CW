package Models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public abstract class Vehicle implements Comparable<Vehicle> {
    protected String plateNumber;
    protected BigDecimal costPerDay;
    protected VehicleModel vehicleModel;
    protected double mileage;
    protected double engineCapacity;
    protected int seats;
    protected String transmission;
    protected static int vehicleCount;

    public Vehicle(String plateNumber, BigDecimal costPerDay, VehicleModel vehicleModel, double mileage, double engineCapacity, int seats, String transmission) {
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

    public VehicleModel getVehicleModel() {
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

    public boolean isAvailable(){
        // TODO: Check Database to find out availability
        return false;
    }

    public boolean rentVehicle(Date pickUpDate, Date dropOffDate){
        return isAvailable();
        // TODO: Update the availability
    }

    public BigDecimal getRentValue(Date pickUpDate, Date dropOffDate){
        // TODO: Find the days between pickUpDate and dropOffDate and return days * costPerDay
        return null;
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
