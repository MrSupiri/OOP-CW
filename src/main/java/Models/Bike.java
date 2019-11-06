package Models;

import java.math.BigDecimal;

public class Bike extends Vehicle {
    private double wheelSize;
    private boolean sideCar;
    private int numOfHelmets;

    public Bike(String plateNumber, BigDecimal costPerDay, VehicleModel vehicleModel, double mileage, double engineCapacity, int seats, String transmission, double wheelSize, boolean sideCar, int numOfHelmets) {
        super(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission);
        this.wheelSize = wheelSize;
        this.sideCar = sideCar;
        this.numOfHelmets = numOfHelmets;
    }

    public double getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(double wheelSize) {
        this.wheelSize = wheelSize;
    }

    public boolean isSideCar() {
        return sideCar;
    }

    public void setSideCar(boolean sideCar) {
        this.sideCar = sideCar;
    }

    public int getNumOfHelmets() {
        return numOfHelmets;
    }

    public void setNumOfHelmets(int numOfHelmets) {
        this.numOfHelmets = numOfHelmets;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "plateNumber='" + plateNumber + '\'' +
                ", costPerDay=" + costPerDay +
                ", vehicleModel=" + vehicleModel +
                ", mileage=" + mileage +
                ", engineCapacity=" + engineCapacity +
                ", seats=" + seats +
                ", transmission='" + transmission + '\'' +
                ", wheelSize=" + wheelSize +
                ", sideCar=" + sideCar +
                ", numOfHelmets=" + numOfHelmets +
                '}';
    }
}
