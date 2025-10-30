package saucelabsdemoapp.screens;

import java.time.Duration;

import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class AboutScreen extends BaseContentScreen {

    @AndroidFindBy(id="com.saucelabs.mydemoapp.android:id/aboutTV")
    private WebElement aboutTitle;

    public AboutScreen (AndroidDriver driver, Duration timeout) {
        super(driver, timeout);
    }

    @Override
    protected WebElement uniqueElement() {
        return aboutTitle;
    }

}
