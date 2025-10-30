package org.nttdata.pom;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public abstract class MobileComponent extends MobileOperation {

    //contenedor del componente:
    protected final WebElement container;

    protected MobileComponent (AndroidDriver driver, Duration timeout, WebElement container) {
        super(driver, timeout);
        this.container = container;
        PageFactory.initElements(new AppiumFieldDecorator(driver, timeout), this);
    }

    public WebElement root () {
        return container;
    }

    public boolean isPresent() {
        return isVisible(container);
    }

}
