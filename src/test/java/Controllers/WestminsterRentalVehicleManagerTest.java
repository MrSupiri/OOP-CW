package Controllers;

import Models.Car;
import Models.VehicleModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class WestminsterRentalVehicleManagerTest {
    private static WestminsterRentalVehicleManager manger;

    @BeforeClass
    public static void setUp() {
        manger = new WestminsterRentalVehicleManager("isala", "needToHashThis");
    }

    @AfterClass
    public static void tearDown() {
        manger.deleteVehicle("Test-1111");
    }

    @Test
    public void addVehicle() {
        Car car = new Car(
                "Test-1111",
                new BigDecimal(10.29),
                new VehicleModel("Van", "BMW", "i8"),
                50.0,
                5000.0,
                5,
                "Auto",
                2,
                true,
                3
        );
        manger.addVehicle(car);
    }

    @Test
    public void deleteVehicle() {
        manger.deleteVehicle("Test-1111");
    }

    @Test
    public void printVehicle() {
        manger.printVehicle();
    }

    @Test
    public void save() {
        manger.save();
        File f = new File("data.txt");
        assertTrue(f.isFile());
    }
}