package saucelabsdemoapp.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import saucelabsdemoapp.components.HeaderComponent;
import saucelabsdemoapp.components.MenuComponent;

import org.nttdata.pom.MobileScreen;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public abstract class BaseContentScreen extends MobileScreen {

    @AndroidFindBy(id="com.saucelabs.mydemoapp.android:id/header")
    private WebElement headerContainer;

    @AndroidFindBy(id="com.saucelabs.mydemoapp.android:id/menuRV")
    private WebElement menuContainer;

    protected BaseContentScreen(AndroidDriver driver, Duration timeout) {
        super(driver, timeout);
    }

    @Override
    protected WebElement uniqueElement() {
        return headerContainer;
    }

    public HeaderComponent headerComponent () {
        return new HeaderComponent(driver, timeout, headerContainer);
    }

    public boolean isMenuOpen() {
        return isVisible(menuContainer);
    }


    public MenuComponent menu() {
        if (!isMenuOpen()) {
            headerComponent().tapHamburger();
            waitVisible(menuContainer);
        }
        return new MenuComponent(driver, timeout, menuContainer);
    }

    public void closeMenu() {
        if (isMenuOpen()) {
            
            waitGone(menuContainer);
        }
    }

}

