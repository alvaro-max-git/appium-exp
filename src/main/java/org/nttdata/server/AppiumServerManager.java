package org.nttdata.server;

import org.nttdata.config.Env;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.time.Duration;
import java.io.File;

public class AppiumServerManager {

    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();

    private AppiumServerManager() {
    }

    public static void startServer() {

        //if is already running, return
         if (service.get() != null && service.get().isRunning())
            return;

        String ip = Env.get("APPIUM_IP", "127.0.0.1");
        int port = Integer.parseInt((Env.get("APPIUM_PORT", "4723")));

        String appiumPath = Env.getRequired("APPIUM_PATH");
        String nodePath = Env.getRequired("NODE_PATH");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
            .withIPAddress(ip)
            .usingPort(port)
            .withAppiumJS(new File(appiumPath))
            .usingDriverExecutable(new File(nodePath))
            .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
            .withTimeout(Duration.ofSeconds(60))
            .withLogFile(new File("appium.log"));

        AppiumDriverLocalService newService = AppiumDriverLocalService.buildService(builder);
        newService.start();
        service.set(newService);

    }

    public static void stopServer () {
        if (service.get() != null) {
            service.get().stop();
            service.remove();
        }
    }

    public static synchronized AppiumDriverLocalService getService() {
        return service.get();
    } 

    public static synchronized String getServiceUrl() {
        AppiumDriverLocalService currentService = service.get();
        if (currentService == null || !currentService.isRunning()) return null;
        return currentService.getUrl().toString();
    }

}
