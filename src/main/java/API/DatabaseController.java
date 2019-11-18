package API;

import Controllers.WestminsterRentalVehicleManager;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class DatabaseController {
    private MongoCollection<Document> vehicleCollection;
    private MongoCollection<Document> reservationCollection;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public DatabaseController(MongoDatabase database) {
        this.vehicleCollection = database.getCollection("vehicle");
        this.reservationCollection = database.getCollection("reservation");
    }

    public ArrayList<Vehicle> getVehicles() {
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> {
            items.add(deserializeVehicle(document));
        });
        return items;
    }

    public Vehicle deserializeVehicle(Document document) {
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
                //.append("costPerDay", new BigDecimal((double) data.get("costPerDay"))) This throws a java.lang.NumberFormatException
                .append("costPerDay", new BigDecimal(String.valueOf(data.get("costPerDay"))))
                .append("vehicleModel", new Document("type", vehicleModel.get("type"))
                        .append("make", vehicleModel.get("make"))
                        .append("model", vehicleModel.get("model"))
                )
                .append("mileage", data.get("mileage"))
                .append("engineCapacity", data.get("engineCapacity"))
                .append("seats", (int) Math.round((double) data.get("seats")))
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

    public void bookVehicle(Map data) throws ParseException {
        Document document = new Document("plateNumber",  data.get("plateNumber"))
                .append("firstName",  data.get("firstName"))
                .append("lastName",  data.get("lastName"))
                .append("email",  data.get("email"))
                .append("pickupDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("pickupDate"), timeFormatter))))
                .append("dropOffDate", Date.from(Instant.from(OffsetDateTime.parse((String) data.get("dropOffDate"), timeFormatter))));
        reservationCollection.insertOne(document);
    }

    public boolean isVehicleAvailable(String plateNumber, String pickupDate, String dropOffDate) throws ParseException {
        return reservationCollection.count(and(
                eq("vehicle.plateNumber", plateNumber),
                or(
                        and(lte("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(pickupDate, timeFormatter)))), gt("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(pickupDate, timeFormatter))))),
                        and(lt("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(dropOffDate, timeFormatter)))), gte("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(dropOffDate, timeFormatter))))),
                        and(gt("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(pickupDate, timeFormatter)))), lt("pickupDate", Date.from(Instant.from(OffsetDateTime.parse(dropOffDate, timeFormatter)))))
                )
        )) == 0;
    }

    public ArrayList<Vehicle> getAvailableVehicles(String pickupDate, String dropOffDate) throws ParseException {
        ArrayList<Vehicle> items = new ArrayList<>();
        vehicleCollection.find().forEach((Block<Document>) document -> {
            Vehicle vehicle = deserializeVehicle(document);
            try {
                if(isVehicleAvailable(vehicle.getPlateNumber(), pickupDate, dropOffDate)) {
                    items.add(vehicle);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return items;
    }
}
