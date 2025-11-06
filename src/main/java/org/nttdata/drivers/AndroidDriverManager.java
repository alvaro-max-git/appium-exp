package org.nttdata.drivers;

import java.net.MalformedURLException;

import org.nttdata.config.CapabilitiesResolver;
import org.nttdata.config.Env;
import org.nttdata.server.AppiumServerManager;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;

public class AndroidDriverManager {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private AndroidDriverManager () {}

    public static void create () {

        UiAutomator2Options options = CapabilitiesResolver.isSauceLabs()
            ? CapabilitiesResolver.androidSauceOptions()
            : CapabilitiesResolver.androidLocalOptions();

        
        try {
            AndroidDriver driver;
            URL url;

            if (CapabilitiesResolver.isLocal()) {
                AppiumServerManager.startServer();
                String localUrl = Env.get("APPIUM_URL", AppiumServerManager.getServiceUrl());
                if (localUrl == null) throw new IllegalStateException("Appium server URL not available.");
                url = new URL(localUrl);
            } else if (CapabilitiesResolver.isSauceLabs()) {
                String sauceUrl = Env.getRequired("SAUCELABS_TEST_URL");
                url = new URL(sauceUrl);
            } else {
                throw new IllegalStateException("RUN_TARGET not supported. Use 'Local' or 'Saucelabs'.");
            }
            
            driver = new AndroidDriver(url, options);
            DRIVER.set(driver);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid endpoint URL", e);
        }

    }

    public static void quit () {
        if (DRIVER.get() != null) {
            DRIVER.get().quit();
            DRIVER.remove();
        }
        // También detenemos el servidor si se está ejecutando localmente
        if (CapabilitiesResolver.isLocal()) {
            AppiumServerManager.stopServer();
        }
    }

    public static AndroidDriver get () {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("Driver not initialized. Call AndroidDriverManager.create() first.");
        return driver;
    }

    //TODO: meter timeouts. POr ejemplo:
    //     private static void setupTimeouts() {
    //   get().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    // }

}
