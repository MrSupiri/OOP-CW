package Controllers;

import Models.Bike;
import Models.Car;
import Models.Vehicle;
import Models.VehicleModel;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

public class DatabaseController {
    private MongoCollection<Document> vehicleCollection;
    private MongoCollection<Document> reservationCollection;
    private MongoCollection<Document> adminsCollection;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public DatabaseController(MongoDatabase database) {
        this.vehicleCollection = database.getCollection("vehicle");
        this.reservationCollection = database.getCollection("reservation");
        this.adminsCollection = database.getCollection("admin");
    }

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> items.add(deserializeVehicle(document)));
        return items;
    }

    private Vehicle deserializeVehicle(Document document) {
        Document vehicleModel = (Document) document.get("vehicleModel");
        if (document.getString("type").equalsIgnoreCase("car")) {
            return new Car(
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
            );
        } else if (document.getString("type").equalsIgnoreCase("bike")) {
            return new Bike(
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
            );
        } else {
            System.err.println("Unrecognized Vehicle Type");
            System.err.println(document.toJson());
            return null;
        }
    }

    public void addVehicle(Document vehicle) throws OutOfMemoryError {
        if (this.numOfFreeParkingSlots() >= WestminsterRentalVehicleManager.MAX_VEHICLES) {
            throw new OutOfMemoryError();
        }
        vehicleCollection.insertOne(vehicle);
    }

    public void deleteVehicle(String plateNumber) {
        if (isVehicle(plateNumber)) {
            vehicleCollection.findOneAndDelete(eq("plateNumber", plateNumber));
            return;
        }
        throw new InvalidParameterException("PlateNumber is not in the database");
    }

    public boolean isVehicle(String plateNumber) {
        return vehicleCollection.count(eq("plateNumber", plateNumber)) > 0;
    }

    public int numOfFreeParkingSlots() {
        return (int) vehicleCollection.count();
    }

    public static Document createVehicleDocument(Map data) throws IllegalArgumentException, NullPointerException, ClassCastException {
        Map vehicleModel = (Map) data.get("vehicleModel");
        Document doc = new Document("plateNumber", data.get("plateNumber"))
                //.append("costPerDay", new BigDecimal((double) data.get("costPerDay")))  //  This throws a java.lang.NumberFormatException
                .append("costPerDay", new BigDecimal(String.valueOf(data.get("costPerDay"))))
                .append("vehicleModel", new Document("type", vehicleModel.get("type"))
                        .append("make", vehicleModel.get("make"))
                        .append("model", vehicleModel.get("model"))
                )
                .append("mileage", data.get("mileage"))
                .append("engineCapacity", data.get("engineCapacity"))
                .append("seats", (int) Math.round((double) data.get("seats"))) // TODO: Find why I did this weird cast and comment it
                .append("transmission", data.get("transmission"));

        if (data.containsKey("doors")) {
            doc.append("doors", (int) Math.round((double) data.get("doors")))
                    .append("airConditioned", data.get("airConditioned"))
                    .append("trunkCapacity", (int) Math.round((double) data.get("trunkCapacity")))
                    .append("type", "car");
        } else if (data.containsKey("wheelSize")) {
            doc.append("wheelSize", data.get("wheelSize"))
                    .append("sideCar", data.get("sideCar"))
                    .append("numOfHelmets", (int) Math.round((double) data.get("numOfHelmets")))
                    .append("type", "bike");
        } else {
            throw new IllegalArgumentException(String.format("%s is not a valid Vehicle Type", data.get("type")));
        }

        return doc;
    }

    public void bookVehicle(Map data){
        Document document = new Document("plateNumber",  data.get("plateNumber"))
                .append("firstName",  data.get("firstName"))
                .append("lastName",  data.get("lastName"))
                .append("phoneNumber",  data.get("phoneNumber"))
                .append("pickupDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("pickupDate"), timeFormatter))))
                .append("dropOffDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("dropOffDate"), timeFormatter))));
        reservationCollection.insertOne(document);
    }

    public boolean isVehicleAvailable(String plateNumber, String pickupDate, String dropOffDate){
        Date pickUp = Date.from(Instant.from(OffsetDateTime.parse(pickupDate, timeFormatter)));
        Date dropOff = Date.from(Instant.from(OffsetDateTime.parse(dropOffDate, timeFormatter)));
        return (int) reservationCollection.count(and(
                eq("plateNumber", plateNumber),
                and(
                        lt("pickupDate", dropOff),
                        gte("dropOffDate", pickUp)
                )
        ))  ==  0;
    }

    public ArrayList<Vehicle> getAvailableVehicles(String pickupDate, String dropOffDate){
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> {
            Vehicle vehicle = deserializeVehicle(document);
            assert vehicle != null;
            if(isVehicleAvailable(vehicle.getPlateNumber(), pickupDate, dropOffDate)) {
                items.add(vehicle);
            }
        });
        return items;
    }

    public boolean checkCredentials(String empID, String password){
        return (int) adminsCollection.count(and(
                eq("empID", empID),
                eq("password", password)
        ))  ==  1;
    }

    public boolean checkSession(String sessionID){
        return (int) adminsCollection.count(
                eq("sessionID", sessionID)
        )  ==  1;
    }

    public String createNewSession(String empID, String password){
        if(!checkCredentials(empID, password))
            throw new InvalidParameterException();
        String uuid = UUID.randomUUID().toString();
        adminsCollection.updateOne(and(eq("empID", empID), eq("password", password))
                , new Document("$set", new Document("sessionID", uuid))
        );
        return uuid;
    }
}
