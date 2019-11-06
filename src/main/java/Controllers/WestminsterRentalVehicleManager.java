package Controllers;

import Models.Bike;
import Models.Car;
import Models.Vehicle;
import Models.VehicleModel;
import Views.ConsoleApp;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class WestminsterRentalVehicleManager implements RentalVehicleManager {
    private final int MAX_VEHICLES = 50;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private String empID;
    private String name;

    public WestminsterRentalVehicleManager(String empID, String name) {
        this.empID = empID;
        this.name = name;
    }

    @Override
    public void addVehicle() {
        if(vehicles.size() >= MAX_VEHICLES){
            System.out.println("\n Error: There is no space to add more vehicles");
            return;
        }

        ArrayList<String> vTypes = new ArrayList<>();
        vTypes.add("Bike");
        vTypes.add("Car");

        String vehicleType = ConsoleApp.promptForENUM(
                "\tEnter Vehicle Type: ",
                "Invalid Vehicle Type",
                vTypes
        );

        System.out.print("\tEnter the Plate Number of the Vehicle: ");
        String plateNumber = ConsoleApp.sc.nextLine();
        BigDecimal costPerDay = ConsoleApp.promptForBigDecimal(
                "\tEnter the Price of the Vehicle for day: ",
                "Invalid Price !"
        );
        System.out.print("\tEnter the Type of the Vehicle: ");
        String type = ConsoleApp.sc.nextLine();
        System.out.print("\tEnter the Maker of the Vehicle: ");
        String make = ConsoleApp.sc.nextLine();
        System.out.print("\tEnter the Model of the Vehicle: ");
        String model = ConsoleApp.sc.nextLine();
        VehicleModel vehicleModel = new VehicleModel(type, make, model);
        double mileage = ConsoleApp.promptForDouble(
                "\tEnter the Mileage of the Vehicle: ",
                "Invalid mileage !"
        );
        double engineCapacity = ConsoleApp.promptForPositiveDouble(
                "\tEnter the Engine Capacity of the Vehicle: ",
                "Invalid Engine Capacity !"
        );

        int seats = ConsoleApp.promptForPositiveInt(
                "\tEnter the Number of Seats in the Vehicle: ",
                "Invalid Number of Seats !"
        );

        System.out.print("\tEnter the Transmission type of the Vehicle: ");
        String transmission = ConsoleApp.sc.nextLine();

        if(vehicleType.equals("Bike")){
            double wheelSize = ConsoleApp.promptForDouble(
                    "\tEnter the WheelSize of the Bike: ",
                    "Invalid wheelSize !"
            );

            boolean sideCar = ConsoleApp.promptForBoolean(
                    "\tDoes this bike has a side car ? : ",
                    "Invalid Answer ! (Yes/No)"
            );
            int numOfHelmets = ConsoleApp.promptForPositiveInt(
                    "\tEnter the Number of Helmets given with the Bike: ",
                    "Invalid Number of Helmets !"
            );
            vehicles.add(new Bike(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, wheelSize, sideCar, numOfHelmets));
        }
        else{
            int doors = ConsoleApp.promptForPositiveInt(
                    "\tEnter the Number of doors in the Car: ",
                    "Invalid Number of doors !"
            );

            boolean airConditioned = ConsoleApp.promptForBoolean(
                    "\tDoes this car has a Air Condition ? : ",
                    "Invalid Answer ! (Yes/No)"
            );

            int trunkCapacity = ConsoleApp.promptForPositiveInt(
                    "\tEnter the Number of luggage fits in trunk of the Car: ",
                    "Invalid Number of luggages !"
            );
            vehicles.add(new Car(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, doors, airConditioned, trunkCapacity));
        }

        save();
    }

    @Override
    public void editVehicle(String plateNumber) {
//        Vehicle vehicle = getVehicleByPlateNumber(plateNumber);
//        if (vehicle == null){
//            System.out.printf("Vehicle with the plate number - %s was not found in the database", plateNumber);
//            return;
//        }
//        System.out.printf("What is the attribute you want to edit from %s ?", plateNumber);
//        if(vehicle instanceof Bike) {
//            System.out.println("[ plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, wheelSize, sideCar, numOfHelmets ]");
//
//        }else{
//            System.out.println("[ plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, doors, airConditioned, trunkCapacity ]");
//        }

    }

    @Override
    public void deleteVehicle(String plateNumber) {
        Vehicle vehicle = getVehicleByPlateNumber(plateNumber);
        if (vehicle == null){
            System.out.printf("Vehicle with the plate number - %s was not found in the database\n", plateNumber);
            return;
        }
        vehicles.remove(vehicle);
        System.out.printf("%s with the number plate '%s' was delete from the records (%s Slots Left)\n",
                vehicle.getClass().getName().replace("Models.", ""),
                plateNumber,
                MAX_VEHICLES - vehicles.size());
        save();
    }

    @Override
    public void printVehicle() {
        Collections.sort(vehicles);
        System.out.println("+-----+------------------+----------+");
        System.out.println("|  #  |   Plate Number   |   Type   |");
        System.out.println("+-----+------------------+----------+");
        String format = "|  %02d |   %-12s   |   %-6s |%n";
        int i = 1;
        for (Vehicle vehicle: vehicles) {
            System.out.printf(format, i, vehicle.getPlateNumber(), vehicle.getClass().getName().replace("Models.", ""));
            i++;
        }
        System.out.println("+-----+------------------+----------+");
    }

    @Override
    public void save() {
        StringBuilder content = new StringBuilder();
        for (Vehicle vehicle: vehicles) {
            content.append(vehicle.toString()).append("\n");
        }
        try {
            File file = new File("data.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content.toString());
            bw.close(); // Be sure to close BufferedWriter
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private Vehicle getVehicleByPlateNumber(String plateNumber){
        for (Vehicle v: vehicles) {
            if (v.getPlateNumber().equalsIgnoreCase(plateNumber)){
                return v;
            }
        }
        return null;
    }
}
