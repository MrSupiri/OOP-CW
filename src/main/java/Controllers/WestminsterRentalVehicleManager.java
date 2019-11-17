package Controllers;

import Models.Vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WestminsterRentalVehicleManager implements RentalVehicleManager {
    public final static int MAX_VEHICLES = 50;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    private String empID;
    private String name;

    public WestminsterRentalVehicleManager(String empID, String name) {
        this.empID = empID;
        this.name = name;
    }

    @Override
    public void addVehicle() {

    }

    @Override
    public void deleteVehicle(String plateNumber) {

    }

    @Override
    public void printVehicle() {
        Collections.sort(vehicles);
        System.out.println("+-----+------------------+----------+");
        System.out.println("|  #  |   Plate Number   |   Type   |");
        System.out.println("+-----+------------------+----------+");
        String format = "|  %02d |   %-12s   |   %-6s |%n";
        int i = 1;
        for (Vehicle vehicle : vehicles) {
            System.out.printf(format, i, vehicle.getPlateNumber(), vehicle.getClass().getName().replace("Models.", ""));
            i++;
        }
        System.out.println("+-----+------------------+----------+");
    }

    @Override
    public void save() {
        StringBuilder content = new StringBuilder();
        for (Vehicle vehicle : vehicles) {
            content.append(vehicle.toString()).append("\n");
        }
        try {
            File file = new File("data.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content.toString());
            bw.close(); // Be sure to close BufferedWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
