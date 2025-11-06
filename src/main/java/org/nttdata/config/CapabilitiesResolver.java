package org.nttdata.config;

import java.time.Duration;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;

/*
 * Builds Desired Capabilites
 */

public class CapabilitiesResolver {

    private CapabilitiesResolver () {}

    public static boolean isSauceLabs() {
        return Env.getRequired("RUN_TARGET").equalsIgnoreCase("saucelabs");
    }

    public static boolean isLocal() {
        return Env.getRequired("RUN_TARGET").equalsIgnoreCase("local");
    }


    //configures UiAutomator2. Assumes we are testing on Android.
    public static UiAutomator2Options androidLocalOptions() {

        UiAutomator2Options options = new UiAutomator2Options();

        String deviceUdid= Env.getRequired("ANDROID_UDID");
        String appPackage = Env.get("APP_PACKAGE", "com.saucelabs.mydemoapp.android");
        String appActivity = Env.get("APP_ACTIVITY", "com.saucelabs.mydemoapp.android.view.activities.SplashActivity");

        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
            .setAvdLaunchTimeout(Duration.ofSeconds(300))
            .setAvdReadyTimeout(Duration.ofSeconds(120))
            .setUdid(deviceUdid)
            .setAppPackage(appPackage)
            .setAppActivity(appActivity);

        return options;
    }

    public static UiAutomator2Options androidSauceOptions () {
        UiAutomator2Options options = new UiAutomator2Options();
        
        // Appium capabilities for Android on Sauce Labs
        options.setPlatformName("android");
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        
        // Asignación dinámica de dispositivo real en Sauce Labs
        options.setDeviceName(".*"); // Cualquier dispositivo Android real disponible
        options.setPlatformVersion(".*"); // Última versión.
        
        // La app debe estar subida a Sauce Storage. 
        // Aquí asumimos que se llama 'sauce-demo-app.apk'
        options.setApp("storage:filename=mda-2.2.0-25.apk");

        // Sauce Labs specific options
        options.setCapability("sauce:options", new java.util.HashMap<String, Object>() {{
            put("username", Env.getRequired("SAUCE_USERNAME"));
            put("accessKey", Env.getRequired("SAUCE_ACCESS_KEY"));
            put("build", "appium-exp-build-" + System.currentTimeMillis());
            put("name", "LocalSauceLabsDemoAppTest on Sauce");
            put("appiumVersion", "latest");
        }});

        return options;
    }

}
