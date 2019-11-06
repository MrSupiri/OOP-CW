package Views;
import Controllers.WestminsterRentalVehicleManager;
import Models.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static String sessionToken;
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // TODO: Initiate the Manager CLI

        displayMenu();
        WestminsterRentalVehicleManager manager = new WestminsterRentalVehicleManager("123", "Test");
        int option = promptForInt(">>> ", "Invalid Option");
        while (option != -1){
            switch (option){
                case 1:
                    manager.addVehicle();
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
                default:
                    System.out.println("Invalid Option");
                    break;
            }
            displayMenu();
            option = promptForInt(">>> ", "Invalid Option");
        }
        System.out.println("\nExisting Gracefully");
    }

    private static boolean login(String username, String password){
        // TODO: Match the username and password with database, update the sessionToken and return true if authorized
        return false;
    }

    private static void launchGUI(){
        // TODO: Start the Electron Binary
    }

    private static void logOut(){
        sessionToken = null;
    }

    private static void displayMenu(){
        System.out.println();
        System.out.println(" +---------------------+ ");
        System.out.println(" | 1 | Added Vehicle   |");
        System.out.println(" | 2 | Delete Vehicle  |");
        System.out.println(" | 3 | Print Vehicle   |");
        System.out.println(" | 4 | Save Vehicle    |");
        System.out.println(" |-1 | Exit            |");
        System.out.println(" +---------------------+ ");
    }

    /**
     * prompt user till the he enters a Integer Value
     * @param msg - message that keep promoting to user
     * @param err - error given when user enters a invalid number
     * @return - number user input
     */
    public static int promptForInt(String msg, String err) {
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

    public static int promptForPositiveInt(String msg, String error) {
        int value = promptForInt(msg, error);
        while(value < 0){
            System.out.printf("\n\t%s\n\n", error);
            value = promptForInt(msg, error);
        }
        return value;
    }

    public static BigDecimal promptForBigDecimal(String msg, String err) {
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

    public static double promptForDouble(String msg, String err) {
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

    public static double promptForPositiveDouble(String msg, String err) {
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

    public static String promptForENUM(String msg, String err, List<String> types) {
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

    public static boolean promptForBoolean(String msg, String err) {
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
