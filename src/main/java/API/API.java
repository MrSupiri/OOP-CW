package API;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.*;

import Controllers.RentalVehicleManager;
import Controllers.WestminsterRentalVehicleManager;
import Models.Bike;
import Models.Car;
import Models.Vehicle;
import Models.VehicleModel;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Decimal128;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class API {
    private static MongoCollection<Document> vehicleCollection;
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        String username = "isala";
        String password = "1234";
        String JWT = "Yi0jadyJTTp%@KZ1PEB8d3oK9d!yYv6Y^$tTv&ZnCm2kcaagAZ";

        String MONGODB_URI = String.format("mongodb://%s:%s@%s:27017/%s",
                System.getenv("MONGODB_USER"), System.getenv("MONGODB_PASSWORD"),
                System.getenv("MONGODB_HOST"), System.getenv("MONGODB_DATABASE"));

        MongoClientURI uri = new MongoClientURI(MONGODB_URI);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(System.getenv("MONGODB_DATABASE"));
        vehicleCollection = database.getCollection("vehicle");


        RentalVehicleManager manger = new WestminsterRentalVehicleManager("EMP1234", "Isala");

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
        ArrayList<Vehicle> items = new ArrayList<>();
        try {
            vehicleCollection.find().forEach((Block<Document>) document -> {
                Document vehicleModel = (Document) document.get("vehicleModel");
                if (document.getString("type").equalsIgnoreCase("car")) {
                    items.add(new Car(
                            document.getString("plateNumber"),
                            ((Decimal128) document.get("costPerDay")).bigDecimalValue(),
                            new VehicleModel(
                                    vehicleModel.getString("type"),
                                    vehicleModel.getString("make"),
                                    vehicleModel.getString("model")
                            ),
                            document.getDouble("mileage"),
                            document.getDouble("engineCapacity"),
                            document.getInteger("seats"),
                            document.getString("transmission"),
                            document.getInteger("doors"),
                            document.getBoolean("airConditioned"),
                            document.getInteger("trunkCapacity")
                    ));
                } else if (document.getString("type").equalsIgnoreCase("bike")) {
                    items.add(new Bike(
                            document.getString("plateNumber"),
                            ((Decimal128) document.get("costPerDay")).bigDecimalValue(),
                            new VehicleModel(
                                    vehicleModel.getString("type"),
                                    vehicleModel.getString("make"),
                                    vehicleModel.getString("model")
                            ),
                            document.getDouble("mileage"),
                            document.getDouble("engineCapacity"),
                            document.getInteger("seats"),
                            document.getString("transmission"),
                            document.getDouble("wheelSize"),
                            document.getBoolean("sideCar"),
                            document.getInteger("numOfHelmets")
                    ));
                } else{
                    System.err.println("Unrecognized Vehicle Type");
                    System.err.println(document.toJson());
                }
            });
            return items;
        }catch (Exception e){
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static ResponseView addVehicles(Request request, Response response) {
        response.type("application/json");
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            Document doc = createVehicleDocument(data);
            if (((String) data.get("type")).equalsIgnoreCase("car")) {
                doc.append("doors", (int) Math.round((double) data.get("doors")))
                        .append("airConditioned", data.get("airConditioned"))
                        .append("trunkCapacity", (int) Math.round((double) data.get("trunkCapacity")))
                        .append("type", "car");
            } else if (((String) data.get("type")).equalsIgnoreCase("bike")) {
                doc.append("wheelSize", data.get("wheelSize"))
                        .append("sideCar", data.get("sideCar"))
                        .append("numOfHelmets", (int) Math.round((double) data.get("numOfHelmets")))
                        .append("type", "bike");
            }else{
                response.status(400);
                return new ResponseView(null, "Invalid Vehicle Type");
            }
            vehicleCollection.insertOne(doc);
            response.status(201);
            return new ResponseView("Vehicle Created", null);
        } catch (NullPointerException e){
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        } catch (ClassCastException e){
            response.status(400);
            e.printStackTrace();
            return new ResponseView(null, "Data Type Mismatch");
        }
        catch (Exception e){
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static ResponseView deleteVehicle(Request request, Response response){
        response.type("application/json");
        try{
            Map data = gson.fromJson(request.body(), Map.class);
            if(vehicleCollection.count(eq("plateNumber", data.get("plateNumber"))) == 0){
                response.status(404);
                return new ResponseView(null, "Vehicle not found");
            }
            vehicleCollection.findOneAndDelete(eq("plateNumber", data.get("plateNumber")));
            return new ResponseView("Vehicle was deleted", null);
        } catch (NullPointerException e){
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        } catch (ClassCastException e){
            response.status(400);
            e.printStackTrace();
            return new ResponseView(null, "Data Type Mismatch");
        }
        catch (Exception e){
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static Object updateVehicle(Request request, Response response){
        response.type("application/json");
        deleteVehicle(request, response);
        return addVehicles(request, response);
    }

    private static Document createVehicleDocument(Map data) {
        Map vehicleModel = (Map) data.get("vehicleModel");
        return new Document("plateNumber", data.get("plateNumber"))
//                .append("costPerDay", new BigDecimal((double) data.get("costPerDay"))) This throws a java.lang.NumberFormatException
                .append("costPerDay", new BigDecimal(String.valueOf(data.get("costPerDay"))))
                .append("vehicleModel", new Document("type", vehicleModel.get("type")).append("make", vehicleModel.get("make")).append("model", vehicleModel.get("model")))
                .append("mileage", data.get("mileage"))
                .append("engineCapacity", data.get("engineCapacity"))
                .append("seats", (int) Math.round((double) data.get("seats")))
                .append("transmission", data.get("transmission"));
    }

}
