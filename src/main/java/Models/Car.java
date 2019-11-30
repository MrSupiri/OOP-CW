package Models;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class Car extends Vehicle {
    private int doors;
    private boolean airConditioned;
    private int trunkCapacity;

    public Car(String plateNumber, BigDecimal costPerDay, VehicleModel vehicleModel, double mileage, double engineCapacity, int seats, String transmission, int doors, boolean airConditioned, int trunkCapacity) {
        super(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission);
        this.doors = doors;
        this.airConditioned = airConditioned;
        this.trunkCapacity = trunkCapacity;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public boolean isAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public int getTrunkCapacity() {
        return trunkCapacity;
    }

    public void setTrunkCapacity(int trunkCapacity) {
        this.trunkCapacity = trunkCapacity;
    }

    @Override
    public String toString() {
        return "Car{" +
                "plateNumber='" + plateNumber + '\'' +
                ", costPerDay=" + costPerDay +
                ", vehicleModel=" + vehicleModel +
                ", mileage=" + mileage +
                ", engineCapacity=" + engineCapacity +
                ", seats=" + seats +
                ", transmission='" + transmission + '\'' +
                ", doors=" + doors +
                ", airConditioned=" + airConditioned +
                ", trunkCapacity=" + trunkCapacity +
                '}';
    }
}
