package Controllers;

import API.ResponseView;
import Models.Vehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class WestminsterRentalVehicleManager implements RentalVehicleManager {
    final static int MAX_VEHICLES = 50;
    private static final String API_ENDPOINT = "http://localhost:4567/api/admin/vehicle/";
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static Gson gson = new Gson();

    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static String sessionToken;

    /**
     * Initialize the a session with the server using empID and password
     *
     * @param empID    - Employee ID
     * @param password - Employee Password
     */
    public WestminsterRentalVehicleManager(String empID, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empID", empID);
        jsonObject.addProperty("password", password);
        //noinspection deprecation
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://localhost:4567/api/auth/")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                sessionToken = (String) gson.fromJson(Objects.requireNonNull(response.body()).string(), ResponseView.class).getSuccess();
            } else {
                throw new InvalidParameterException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fetchVehicles();
    }

    /**
     * Send Vehicle Object to the API to save it to the database
     *
     * @param vehicle - vehicle that need to be added to the database
     */
    @Override
    public void addVehicle(Vehicle vehicle) {
        //noinspection deprecation
        RequestBody body = RequestBody.create(JSON, gson.toJson(vehicle));

        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .put(body)
                .addHeader("Authorization", sessionToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                System.out.printf("Vehicle was successfully added to database, %.0f Slots Left",
                        MAX_VEHICLES - (double) gson.fromJson(
                                Objects.requireNonNull(response.body()).string(), ResponseView.class
                        ).getSuccess()
                );
            } else {
                System.out.printf("Error: %s\n",
                        gson.fromJson(Objects.requireNonNull(response.body()).string(), ResponseView.class).getError()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send Vehicle Object to the API to modify the database
     *
     * @param vehicle - vehicle that need to be updated in the database
     */
    @Override
    public void updateVehicle(Vehicle vehicle) {
        //noinspection deprecation
        RequestBody body = RequestBody.create(JSON, gson.toJson(vehicle));

        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .patch(body)
                .addHeader("Authorization", sessionToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                System.out.printf("Vehicle was successfully updated on the database, %.0f Slots Left",
                        MAX_VEHICLES - (double) gson.fromJson(
                                Objects.requireNonNull(response.body()).string(), ResponseView.class
                        ).getSuccess()
                );
            } else {
                System.out.printf("Error: %s\n",
                        gson.fromJson(Objects.requireNonNull(response.body()).string(), ResponseView.class).getError()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a vehicle from the Database using the API
     *
     * @param plateNumber - Plate Number number of the vehicle that need to be removed from the database
     */
    @Override
    public void deleteVehicle(String plateNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("plateNumber", plateNumber);
        //noinspection deprecation
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .delete(body)
                .addHeader("Authorization", sessionToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                System.out.printf("Vehicle was successfully Delete from database, %.0f Slots Left",
                        MAX_VEHICLES - (double) gson.fromJson(
                                Objects.requireNonNull(response.body()).string(), ResponseView.class
                        ).getSuccess()
                );
            } else {
                System.out.printf("Error: %s\n",
                        gson.fromJson(Objects.requireNonNull(response.body()).string(), ResponseView.class).getError()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the List of Vehicles in the Database
     */
    @Override
    public void printVehicle() {
        if (!fetchVehicles()) {
            System.out.println("Reading Vehicles Failed");
            return;
        }
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

    /**
     * Save the Vehicle List the this disk
     */
    @Override
    public void save() {
        if (!fetchVehicles()) {
            System.out.println("Writing to Disk Failed");
            return;
        }
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

    /**
     * Fetch all the vehicles in the database
     *
     * @return - List of Vehicles
     */
    private boolean fetchVehicles() {
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .addHeader("Authorization", sessionToken)
                .build();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Vehicle.class, new JsonToVehicleMapper());
        Gson gson = builder.create();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                Type listType = new TypeToken<ArrayList<Vehicle>>() {
                }.getType();
                vehicles = gson.fromJson(Objects.requireNonNull(response.body()).string(), listType);
                return true;
            } else {
                System.out.printf("Error: %s\n",
                        gson.fromJson(Objects.requireNonNull(response.body()).string(), ResponseView.class).getError()
                );
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check and get the Vehicle Object from the Plate Number
     *
     * @param plateNumber - Plate Number of the vehicle search for
     * @return - Vehicle object or none if it's not found
     */
    public static Vehicle getVehicleByPlateNumber(String plateNumber) {
        for (Vehicle v : vehicles) {
            if (v.getPlateNumber().equalsIgnoreCase(plateNumber)) {
                return v;
            }
        }
        return null;
    }
}