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

    /**
     * Initialize all the collections
     *
     * @param database - MongoDB Object
     */
    public DatabaseController(MongoDatabase database) {
        this.vehicleCollection = database.getCollection("vehicle");
        this.reservationCollection = database.getCollection("reservation");
        this.adminsCollection = database.getCollection("admin");
    }

    /**
     * Get the List of Vehicles in the Database
     *
     * @return - List of Vehicles in the Database
     */
    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> items.add(deserializeVehicle(document)));
        return items;
    }

    /**
     * Map a MongoDB document object to the Vehicle Object
     *
     * @param document - MongoDB document
     * @return - Vehicle Object
     */
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

    /**
     * Add an Vehicle to the Database
     *
     * @param vehicle - Vehicle Object
     * @throws OutOfMemoryError - Error thrown if packing space is full
     */
    public void addVehicle(Document vehicle) throws OutOfMemoryError {
        if (this.numOfVehiclesInTheDatabase() >= WestminsterRentalVehicleManager.MAX_VEHICLES) {
            throw new OutOfMemoryError();
        }
        vehicleCollection.insertOne(vehicle);
    }

    /**
     * Delete an Vehicle to the Database
     *
     * @param plateNumber plateNumber of the Vehicle
     */
    public void deleteVehicle(String plateNumber) {
        if (isVehicle(plateNumber)) {
            vehicleCollection.findOneAndDelete(eq("plateNumber", plateNumber));
            return;
        }
        throw new InvalidParameterException("PlateNumber is not in the database");
    }

    /**
     * Check if vehicle exists  in the database
     *
     * @param plateNumber - plateNumber of the Vehicle
     * @return - vehicle exists or not
     */
    public boolean isVehicle(String plateNumber) {
        return vehicleCollection.count(eq("plateNumber", plateNumber)) > 0;
    }

    /**
     * Get the Number Of Vehicles In The Database
     *
     * @return - Number Of Vehicles In The Database
     */
    public int numOfVehiclesInTheDatabase() {
        return (int) vehicleCollection.count();
    }

    /**
     * Create a MongoDB document from HashMap
     *
     * @param data - Data about the Vehicle
     * @return - Vehicle MongoDB document
     * @throws IllegalArgumentException - Thrown when there is Invalid Vehicle Type
     * @throws NullPointerException     - Thrown when some key data are missing
     * @throws ClassCastException       - Thrown when invalid date is present
     */
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

    /**
     * Make a Reservation for Vehicle
     *
     * @param data Reservation data
     */
    public void bookVehicle(Map data) {
        Document document = new Document("plateNumber", data.get("plateNumber"))
                .append("firstName", data.get("firstName"))
                .append("lastName", data.get("lastName"))
                .append("phoneNumber", data.get("phoneNumber"))
                .append("pickupDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("pickupDate"), timeFormatter))))
                .append("dropOffDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("dropOffDate"), timeFormatter))));
        reservationCollection.insertOne(document);
    }

    /**
     * Check Vehicle is Available in the given date range
     *
     * @param plateNumber - plateNumber of the Vehicle
     * @param pickupDate  - pickupDate of the vehicle
     * @param dropOffDate - dropOffDate of the Vehicle
     * @return - Vehicle availability
     */
    public boolean isVehicleAvailable(String plateNumber, String pickupDate, String dropOffDate) {
        Date pickUp = Date.from(Instant.from(OffsetDateTime.parse(pickupDate, timeFormatter)));
        Date dropOff = Date.from(Instant.from(OffsetDateTime.parse(dropOffDate, timeFormatter)));
        return (int) reservationCollection.count(and(
                eq("plateNumber", plateNumber),
                and(
                        lt("pickupDate", dropOff),
                        gte("dropOffDate", pickUp)
                )
        )) == 0;
    }

    /**
     * Get the List of Vehicles in the Database which are Available in the given date range
     *
     * @param pickupDate  - pickupDate of the vehicle
     * @param dropOffDate - dropOffDate of the Vehicle
     * @return - List of Available Vehicles in the Database
     */
    public ArrayList<Vehicle> getAvailableVehicles(String pickupDate, String dropOffDate) {
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> {
            Vehicle vehicle = deserializeVehicle(document);
            assert vehicle != null;
            if (isVehicleAvailable(vehicle.getPlateNumber(), pickupDate, dropOffDate)) {
                items.add(vehicle);
            }
        });
        return items;
    }

    /**
     * Check Username and Password exits in the database
     *
     * @param empID    - username
     * @param password - password
     * @return - User availability
     */
    public boolean checkCredentials(String empID, String password) {
        return (int) adminsCollection.count(and(
                eq("empID", empID),
                eq("password", password)
        )) == 1;
    }

    /**
     * Check if Session ID match with the database
     * @param sessionID - session ID of the User
     * @return - Valid or not
     */
    public boolean checkSession(String sessionID) {
        return (int) adminsCollection.count(
                eq("sessionID", sessionID)
        ) == 1;
    }

    /**
     * Create New session using user name and the password
     * @param empID    - username
     * @param password - password
     * @return - New Session Token
     */
    public String createNewSession(String empID, String password) {
        if (!checkCredentials(empID, password))
            throw new InvalidParameterException();
        String uuid = UUID.randomUUID().toString();
        adminsCollection.updateOne(and(eq("empID", empID), eq("password", password))
                , new Document("$set", new Document("sessionID", uuid))
        );
        return uuid;
    }
}
