package API;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import spark.Request;
import spark.Response;

import java.security.InvalidParameterException;
import java.util.Map;

import static spark.Spark.*;

public class API {
    private static Gson gson = new Gson();
    private static DatabaseController databaseController;

    public static void main(String[] args) {
        String JWT = "Yi0jadyJTTp%@KZ1PEB8d3oK9d!yYv6Y^$tTv&ZnCm2kcaagAZ";

        String MONGODB_URI = String.format("mongodb://%s:%s@%s:27017/%s",
                System.getenv("MONGODB_USER"), System.getenv("MONGODB_PASSWORD"),
                System.getenv("MONGODB_HOST"), System.getenv("MONGODB_DATABASE"));

        MongoClientURI uri = new MongoClientURI(MONGODB_URI);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(System.getenv("MONGODB_DATABASE"));
        databaseController = new DatabaseController(database);

        path("/api", () -> {

            before("/vehicle/*", (request, response) -> {
                if (!request.headers().contains("Authorization") || !request.headers("Authorization").equals(JWT)) {
                    halt(401, "Invalid Token!");
                }
            });
            path("/vehicle", () -> {
                get("/", "application/json", (request, response) -> getVehicles(request, response), gson::toJson);
                post("/", "application/json", (request, response) -> addVehicles(request, response), gson::toJson);
                put("/", "application/json", (request, response) -> addVehicles(request, response), gson::toJson);
                patch("/", "application/json", (request, response) -> updateVehicle(request, response), gson::toJson);
                delete("/", "application/json", (request, response) -> deleteVehicle(request, response), gson::toJson);
            });
        });
    }

    private static Object getVehicles(Request request, Response response) {
        response.type("application/json");
        try {
            return databaseController.getVehicles();
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static ResponseView addVehicles(Request request, Response response) {
        response.type("application/json");
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            databaseController.addVehicle(DatabaseController.createVehicleDocument(data));
            return new ResponseView(databaseController.numOfFreeParkingSlots(), null);
        } catch (OutOfMemoryError e) {
            response.status(400);
            return new ResponseView(null, "Packing lot is full");
        } catch (IllegalArgumentException e) {
            response.status(400);
            return new ResponseView(null, "Invalid vehicle type");
        } catch (NullPointerException e) {
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        } catch (ClassCastException e) {
            response.status(400);
            e.printStackTrace();
            return new ResponseView(null, "Data Type Mismatch");
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static ResponseView deleteVehicle(Request request, Response response) {
        response.type("application/json");
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            databaseController.deleteVehicle((String) data.get("plateNumber"));
            return new ResponseView("Vehicle was deleted", null);
        } catch (InvalidParameterException e) {
            response.status(404);
            return new ResponseView(null, "Vehicle not found");
        } catch (NullPointerException e) {
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        } catch (ClassCastException e) {
            response.status(400);
            e.printStackTrace();
            return new ResponseView(null, "Data Type Mismatch");
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static Object updateVehicle(Request request, Response response) {
        response.type("application/json");
        deleteVehicle(request, response);
        return addVehicles(request, response);
    }


}
