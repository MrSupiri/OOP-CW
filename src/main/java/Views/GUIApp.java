package Views;

import Models.Vehicle;

import java.util.Date;
import java.util.List;

public class GUIApp {
    private static String sessionToken;
    private static String customerID;

    private static void register(String name, String telephone){
        // Register the User and get the sessionToken and customerID from the database;
    }

    private static List<Vehicle> listVehicles(){
        // fetch all vehicles from the database
        return null;
    }

    private static void rentVehicle(Vehicle vehicle, Date pickUpDate, Date dropOffDate){
        // rent the vehicle and create new sales entry
    }

    private static void logout(){
        sessionToken = null;
        customerID = null;
    }

}
