package saucelabsdemoapp.components;

import java.time.Duration;

import org.nttdata.pom.MobileComponent;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuComponent extends MobileComponent {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Catalog\")")
    WebElement webView;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"QR Code Scanner\")")
    WebElement qrCodeScanner;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Geo Location\")")
    WebElement geoLocation;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Drawing\")")
    WebElement drawing;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"About\")")
    WebElement about;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Reset App State\")")
    WebElement resetAppState;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"FingerPrint\")")
    WebElement fingerPrint;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Virtual USB\")")
    WebElement virtualUsb;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Crash app (debug)\")")
    WebElement crashAppDebug;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Log in\")")
    WebElement logIn;

    public MenuComponent (AndroidDriver driver, Duration timeout, WebElement container) {
        super(driver, timeout, container);
    }

    public void tapWebView () {
        clickWhenVisible(webView);
    }

    public void tapQrCodeScanner() {
        clickWhenVisible(qrCodeScanner);
    }

    public void tapGeoLocation() {
        clickWhenVisible(geoLocation);
    }

    public void tapDrawing() {
        clickWhenVisible(drawing);
    }

    public void tapAbout() {
        clickWhenVisible(about);
    }

    public void tapResetAppState() {
        clickWhenVisible(resetAppState);
    }

    public void tapFingerPrint() {
        clickWhenVisible(fingerPrint);
    }

    public void tapVirtualUsb() {
        clickWhenVisible(virtualUsb);
    }

    public void tapCrashAppDebug() {
        clickWhenVisible(crashAppDebug);
    }

    public void tapLogIn() {
        clickWhenVisible(logIn);
    }

}
