package saucelabsdemoapp.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.nttdata.drivers.AndroidDriverManager;
import io.appium.java_client.android.AndroidDriver;
import saucelabsdemoapp.screens.AboutScreen;
import saucelabsdemoapp.screens.ProductsScreen;

public class LocalSauceLabsDemoAppTest extends AndroidTestBase {

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

    @Test
    void openAboutFromHamburgerMenu() {
        var driver = AndroidDriverManager.get();
        var timeout = Duration.ofSeconds(8);

        // 1) Estamos en Products
        ProductsScreen products = new ProductsScreen(driver, timeout).waitUntilLoaded();

        // 2) Abrir menú y seleccionar "About"
        products.menu().tapAbout();

        // 3) Verificar AboutScreen
        AboutScreen about = new AboutScreen(driver, timeout).waitUntilLoaded();
        // aserción
        assertTrue(about.isLoaded(), "About screen should be loaded");
    }
}