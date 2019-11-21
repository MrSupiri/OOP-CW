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

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        path("/api", () -> {

            before("/admin/*", (request, response) -> {
                if (!request.headers().contains("Authorization") || !request.headers("Authorization").equals(JWT)) {
                    halt(401, new Gson().toJson(new ResponseView(null, "Invalid Token!")));
                }
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Methods", "GET");
            });
            path("/admin", () -> {
                get("/vehicle/", "application/json", (request, response) -> getVehicles(request, response), gson::toJson);
                post("/vehicle/", "application/json", (request, response) -> addVehicle(request, response), gson::toJson);
                put("/vehicle/", "application/json", (request, response) -> addVehicle(request, response), gson::toJson);
                patch("/vehicle/", "application/json", (request, response) -> updateVehicle(request, response), gson::toJson);
                delete("/vehicle/", "application/json", (request, response) -> deleteVehicle(request, response), gson::toJson);
            });
            path("/user", () -> {
                post("/vehicle/", "application/json", (request, response) -> getAvailableVehicles(request, response), gson::toJson);
                post("/book/", "application/json", (request, response) -> bookVehicle(request, response), gson::toJson);
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

    private static ResponseView addVehicle(Request request, Response response) {
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
            if (data == null) {
                throw new InvalidParameterException();
            }
            System.out.println(data);
            databaseController.deleteVehicle((String) data.get("plateNumber"));
            return new ResponseView(databaseController.numOfFreeParkingSlots(), null);
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
        return addVehicle(request, response);
    }

    private static ResponseView bookVehicle(Request request, Response response) {
        response.type("application/json");
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            String plateNumber =  (String) data.get("plateNumber");
            String startDate = (String) data.get("pickupDate");
            String endDate = (String) data.get("dropOffDate");
            if (databaseController.isVehicleAvailable(plateNumber, startDate, endDate)) {
                databaseController.bookVehicle(data);
                return new ResponseView("Reservation was successful", null);
            } else {
                return new ResponseView(null, "Vehicle is already booked in this time frame");
            }
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

    private static Object getAvailableVehicles(Request request, Response response) {
        response.type("application/json");
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            String pickUpDate = (String) data.get("pickUpDate");
            String dropOffDate = (String) data.get("dropOffDate");
            return databaseController.getAvailableVehicles(pickUpDate, dropOffDate);
        } catch (NullPointerException e) {
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }
}
