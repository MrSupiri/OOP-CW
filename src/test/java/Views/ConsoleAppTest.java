package Views;

import Controllers.WestminsterRentalVehicleManager;
import Models.Car;
import Models.VehicleModel;
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ConsoleAppTest {


    @Test
    public void promptForVehicleInfo() {
        setUpConsoleInput("ConsoleTestInputs/promptForVehicleInfo.txt");
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
        assertEquals(ConsoleApp.promptForVehicleInfo(), car);
    }

    @Test
    public void promptForInt() {
        setUpConsoleInput("ConsoleTestInputs/promptForInt.txt");
        assertEquals(ConsoleApp.promptForInt("", ""), -10);
    }

    @Test
    public void promptForPositiveInt() {
        setUpConsoleInput("ConsoleTestInputs/promptForPositiveInt.txt");
        assertEquals(ConsoleApp.promptForPositiveInt("", ""), 10);
    }

    @Test
    public void promptForBigDecimal() {
        setUpConsoleInput("ConsoleTestInputs/promptForBigDecimal.txt");
        assertEquals(ConsoleApp.promptForBigDecimal("", ""), new BigDecimal(-10.99).round(new MathContext(4)));
    }

    @Test
    public void promptForDouble() {
        setUpConsoleInput("ConsoleTestInputs/promptForDouble.txt");
        assertEquals(ConsoleApp.promptForDouble("", ""), -10.0, 0);
    }

    @Test
    public void promptForPositiveDouble() {
        setUpConsoleInput("ConsoleTestInputs/promptForPositiveDouble.txt");
        assertEquals(ConsoleApp.promptForPositiveDouble("", ""), 10.0, 0);
    }

    @Test
    public void promptForENUM() {
        setUpConsoleInput("ConsoleTestInputs/promptForENUM.txt");
        ArrayList<String> types = new ArrayList<>();
        types.add("Bike");
        types.add("Car");
        assertEquals(ConsoleApp.promptForENUM("", "", types), "Car");
    }

    @Test
    public void promptForBoolean() {
        setUpConsoleInput("ConsoleTestInputs/promptForBoolean.txt");
        assertTrue(ConsoleApp.promptForBoolean("", ""));
    }

    @Test
    public void promptForCredentials() {
        setUpConsoleInput("ConsoleTestInputs/promptForCredentials.txt");
        assertSame(ConsoleApp.promptForCredentials().getClass(), WestminsterRentalVehicleManager.class);
    }

    private void setUpConsoleInput(String fileName) {
        File file = new File(fileName);
        try {
            ConsoleApp.sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}