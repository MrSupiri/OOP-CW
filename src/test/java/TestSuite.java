import API.DatabaseControllerTest;
import Controllers.WestminsterRentalVehicleManagerTest;
import Views.ConsoleAppTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DatabaseControllerTest.class,
        WestminsterRentalVehicleManagerTest.class,
        ConsoleAppTest.class
})

public class TestSuite {
}