package saucelabsdemoapp.components;

import java.time.Duration;

import org.nttdata.pom.MobileComponent;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class HeaderComponent extends MobileComponent {

    @AndroidFindBy(accessibility = "View menu")
    private WebElement viewMenuButton;

    @AndroidFindBy(accessibility = "Shows current sorting order and displays available sorting options")
    private WebElement sortingButton;

    @AndroidFindBy(accessibility = "Displays number of items in your cart")
    private WebElement cartButton;

    public HeaderComponent (AndroidDriver driver, Duration timeout, WebElement container) {
        super(driver, timeout, container);
    }

    public void tapHamburger () {
        clickWhenVisible(viewMenuButton);
    }
}
