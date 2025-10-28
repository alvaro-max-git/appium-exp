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
        //not implemented yet
        return null;
    }

}
