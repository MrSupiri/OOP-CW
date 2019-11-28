package Views;

import Controllers.RentalVehicleManager;
import Controllers.WestminsterRentalVehicleManager;
import Models.Bike;
import Models.Car;
import Models.Vehicle;
import Models.VehicleModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("SameParameterValue")
public class ConsoleApp {
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        // TODO: Initiate the Manager CLI
        RentalVehicleManager manager = new WestminsterRentalVehicleManager("123", "Test");
        displayMenu();
        int option = promptForInt(">>> ", "Invalid Option");
        while (option != -1){
            switch (option){
                case 1:
                    manager.addVehicle(promptForVehicleInfo());
                    break;
                case 2:
                    System.out.print("Enter the Plate Number: ");
                    String plateNumber = sc.nextLine();
                    manager.deleteVehicle(plateNumber);
                    break;
                case 3:
                    manager.printVehicle();
                    break;
                case 4:
                    manager.save();
                    break;
                case 5:
                    System.out.println("Opening React App on port 3000");
                    try {
                        Runtime.getRuntime().exec("xdg-open localhost:3000");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
            displayMenu();
            option = promptForInt(">>> ", "Invalid Option");
        }
        System.out.println("\nExisting Gracefully");
    }


    private static void launchGUI(){
        // TODO: Start the Electron Binary
    }

    private static void displayMenu(){
        System.out.println();
        System.out.println(" +---------------------+ ");
        System.out.println(" | 1 | Added Vehicle   |");
        System.out.println(" | 2 | Delete Vehicle  |");
        System.out.println(" | 3 | Print Vehicle   |");
        System.out.println(" | 4 | Save Vehicle    |");
        System.out.println(" | 5 | Launch  GUI     |");
        System.out.println(" |-1 | Exit            |");
        System.out.println(" +---------------------+ ");
    }

    private static Vehicle promptForVehicleInfo(){
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
            return new Bike(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, wheelSize, sideCar, numOfHelmets);
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
            return new Car(plateNumber, costPerDay, vehicleModel, mileage, engineCapacity, seats, transmission, doors, airConditioned, trunkCapacity);
        }
    }

    /**
     * prompt user till the he enters a Integer Value
     * @param msg - message that keep promoting to user
     * @param err - error given when user enters a invalid number
     * @return - number user input
     */
    private static int promptForInt(String msg, String err) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            sc.nextLine();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    private static int promptForPositiveInt(String msg, String error) {
        int value = promptForInt(msg, error);
        while(value < 0){
            System.out.printf("\n\t%s\n\n", error);
            value = promptForInt(msg, error);
        }
        return value;
    }

    private static BigDecimal promptForBigDecimal(String msg, String err) {
        System.out.print(msg);
        while (!sc.hasNextBigDecimal()) {
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            sc.nextLine();
        }
        BigDecimal value = sc.nextBigDecimal();
        sc.nextLine();
        return value;
    }

    private static double promptForDouble(String msg, String err) {
        System.out.print(msg);
        while (!sc.hasNextDouble()) {
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            sc.nextLine();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }

    private static double promptForPositiveDouble(String msg, String err) {
        System.out.print(msg);
        while (!sc.hasNextDouble()) {
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            sc.nextLine();
        }
        double value = sc.nextDouble();
        while(value < 0){
            System.out.printf("\n\t\t%s\n\n", err);
            value = promptForDouble(msg, err);
        }
        sc.nextLine();
        return value;
    }

    private static String promptForENUM(String msg, String err, List<String> types) {
        System.out.print(msg);
        String type = sc.nextLine().toLowerCase();
        while (true) {
            for (String t: types) {
                if(t.equalsIgnoreCase(type))
                    return t;
            }
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            type = sc.nextLine();
        }
    }

    private static boolean promptForBoolean(String msg, String err) {
        System.out.print(msg);
        String value = sc.nextLine().toLowerCase();
        while (!value.equals("yes") && !value.equals("no") ){
            System.out.printf("\n\t\t%s\n\n", err);
            System.out.print(msg);
            value = sc.nextLine().toLowerCase();
        }
        return value.equals("yes");
    }
}
