package API;

import static spark.Spark.*;

import Controllers.RentalVehicleManager;
import Controllers.WestminsterRentalVehicleManager;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;
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
                post("/", "application/json", (request, response) -> getVehicles(request, response), gson::toJson);
                put("/", "application/json", (request, response) -> addVehicles(request, response), gson::toJson);
                patch("/", "application/json", (request, response) -> getVehicles(request, response), gson::toJson);
                delete("/", "application/json", (request, response) -> getVehicles(request, response), gson::toJson);
            });
        });
    }

    private static String getVehicles(Request request, Response response) {
        return "It Fucking Worked";
    }

    private static ResponseView addVehicles(Request request, Response response) {
        try {
            Map data = gson.fromJson(request.body(), Map.class);
            Document doc = createVehicleDocument(data);
            if (((String) data.get("type")).equalsIgnoreCase("car")) {
                doc.append("doors", data.get("doors"))
                        .append("airConditioned", data.get("airConditioned"))
                        .append("trunkCapacity", data.get("trunkCapacity"));
            } else if (((String) data.get("type")).equalsIgnoreCase("bike")) {
                doc.append("wheelSize", data.get("wheelSize"))
                        .append("sideCar", data.get("sideCar"))
                        .append("numOfHelmets", data.get("numOfHelmets"));
            }else{
                response.status(400);
                return new ResponseView(null, "Invalid Vehicle Type");
            }
            vehicleCollection.insertOne(doc);
            response.status(201);
            return new ResponseView("Book Created", null);
        } catch (NullPointerException e){
            response.status(400);
            return new ResponseView(null, "Request is missing some key data");
        }
        catch (Exception e){
            e.printStackTrace();
            response.status(500);
            return new ResponseView(null, "Something went Wrong");
        }
    }

    private static Document createVehicleDocument(Map data) {
        Map vehicleModel = (Map) data.get("vehicleModel");
        return new Document("plateNumber", data.get("plateNumber"))
                .append("costPerDay", data.get("costPerDay"))
                .append("vehicleModel", new Document("type", vehicleModel.get("type")).append("make", vehicleModel.get("make")).append("model", vehicleModel.get("model")))
                .append("mileage", data.get("mileage"))
                .append("engineCapacity", data.get("engineCapacity"))
                .append("seats", data.get("seats"))
                .append("transmission", data.get("transmission"));
    }

}
