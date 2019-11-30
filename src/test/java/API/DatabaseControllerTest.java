package API;

import Controllers.DatabaseController;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

public class DatabaseControllerTest {
    private static DatabaseController databaseController;
    private static MongoClient mongoClient;
    private static String session;
    private static MongoDatabase database;

    @BeforeClass
    public static void setUp() {
        String MONGODB_URI = String.format("mongodb://%s:%s@%s:27017/%s",
                System.getenv("MONGODB_USER"), System.getenv("MONGODB_PASSWORD"),
                System.getenv("MONGODB_HOST"), System.getenv("MONGODB_DATABASE"));

        MongoClientURI uri = new MongoClientURI(MONGODB_URI);
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase(System.getenv("MONGODB_DATABASE"));
        databaseController = new DatabaseController(database);
    }

    @AfterClass
    public static void tearDown() {
        databaseController.deleteVehicle("Test-12345");
        database.getCollection("reservation").deleteMany(eq("plateNumber", "Test-12345"));
        mongoClient.close();
    }

    @Test
    public void getVehicles() {
        assertNotNull(databaseController.getVehicles());
    }

    @Test
    public void addVehicle() {
        databaseController.addVehicle(sampleVehicleDocument());
    }

    @Test(expected = InvalidParameterException.class)
    public void deleteVehicle() {
        databaseController.deleteVehicle("gshadjogkfdgldfgrgr");
    }

    @Test
    public void isVehicle() {
        assertTrue(databaseController.isVehicle("Test-12345"));
    }

    @Test
    public void numOfFreeParkingSlots() {
        assertTrue(databaseController.numOfVehiclesInTheDatabase() <= 50);
    }

    @Test
    public void bookVehicle() {
        databaseController.bookVehicle(sampleBook());
    }

    @Test
    public void isVehicleAvailable() {
        assertFalse(databaseController.isVehicleAvailable("Test-12345",
                "2020-12-16T18:30:00.000Z",
                "2020-12-17T18:30:00.000Z"));
    }

    @Test
    public void getAvailableVehicles() {
        assertNotNull(databaseController.getAvailableVehicles("2020-12-16T18:30:00.000Z", "2020-12-17T18:30:00.000Z"));
    }

    @Test
    public void checkCredentials() {
        assertTrue(databaseController.checkCredentials("isala", "needToHashThis"));
    }

    @Test
    public void createNewSession() {
        session = databaseController.createNewSession("isala", "needToHashThis");
    }

    @Test
    public void checkSession() {
        assertTrue(databaseController.checkSession(session));
    }

    private Map sampleBook() {
        Map<String, String> data = new HashMap<>();
        data.put("plateNumber", "Test-12345");
        data.put("firstName", "Keanu");
        data.put("lastName", "Reeves");
        data.put("phoneNumber", "+94712345678");
        data.put("pickupDate", "2020-12-16T18:30:00.000Z");
        data.put("dropOffDate", "2020-12-17T18:30:00.000Z");
        return data;
    }

    private Document sampleVehicleDocument() {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "Car");
        data.put("plateNumber", "Test-12345");
        data.put("costPerDay", 25.9);
        data.put("mileage", 33.2);
        data.put("engineCapacity", 3000.0);
        data.put("seats", 15.0);
        data.put("transmission", "manual");
        data.put("doors", 3.0);
        data.put("airConditioned", false);
        data.put("trunkCapacity", 4.0);

        Map<String, String> vehicleModel = new HashMap<>();
        vehicleModel.put("type", "Van");
        vehicleModel.put("make", "Toyota");
        vehicleModel.put("model", "Caravan");

        data.put("vehicleModel", vehicleModel);

        return DatabaseController.createVehicleDocument(data);
    }
}