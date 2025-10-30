package org.nttdata.pom;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public abstract class MobileScreen extends MobileOperation {

    protected MobileScreen (AndroidDriver driver, Duration timeout) {
        super(driver, timeout);
        PageFactory.initElements(new AppiumFieldDecorator(driver, timeout), this);
    }

    //Devuelve un elemento único de la pantalla para "health-check"

    protected abstract WebElement uniqueElement();

    //Verifica si la pantalla está cargada con el health-check de uniqueElement.

    public boolean isLoaded() {
        try {
            return isVisible(uniqueElement());
        } catch (Exception e) {
            return false;
        }
    }

    /** Espera a que la pantalla esté lista; devuelve this para Fluent API. */
    public <T extends MobileScreen> T waitUntilLoaded() {
        waitVisible(uniqueElement());
        return (T) this;
    }

}
