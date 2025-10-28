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

            if(CapabilitiesResolver.isLocal()) {
                AppiumServerManager.startServer();
                String url = Env.get("APPIUM_URL", AppiumServerManager.getServiceUrl());
                if (url == null) throw new IllegalStateException("Appium server URL not available.");
                driver = new AndroidDriver(new URL(url), options);
            } else {
                throw new IllegalStateException("Mode not supported.");
            }

            DRIVER.set(driver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium endpoint URL", e);
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
