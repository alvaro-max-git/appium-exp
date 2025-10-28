package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.nttdata.drivers.AndroidDriverManager;
import io.appium.java_client.android.AndroidDriver;

public class LocalSauceLabsAppTest extends AndroidTestBase {

    @Test
    void openApp() {
        // 1. El método setUp() de AndroidTestBase ya ha llamado a AndroidDriverManager.create()
        //    y ha lanzado la app.
        AndroidDriver driver = AndroidDriverManager.get();

        // 2. Verificamos que la app lanzada es la correcta.
        String expectedPackage = "com.saucelabs.mydemoapp.android";
        assertEquals(expectedPackage, driver.getCurrentPackage(), "La aplicación no se ha abierto correctamente.");

        // 3. El método tearDown() se encargará de cerrar el driver y el servidor.
    }
}
