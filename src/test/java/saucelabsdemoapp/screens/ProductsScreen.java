package saucelabsdemoapp.screens;

import java.time.Duration;

import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsScreen extends BaseContentScreen  {

    @AndroidFindBy(id="com.saucelabs.mydemoapp.android:id/productTV")
    private WebElement productsTitle;


    public ProductsScreen (AndroidDriver driver, Duration timeout) {
        super(driver, timeout);
    }

    @Override
    protected WebElement uniqueElement() {
        return productsTitle;
    }



}
