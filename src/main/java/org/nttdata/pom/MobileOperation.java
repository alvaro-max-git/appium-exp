package org.nttdata.pom;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public abstract class MobileOperation {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;
    protected final Duration timeout;

    protected MobileOperation(AndroidDriver driver, Duration timeout) {
        this.driver = driver;
        this.timeout = timeout;
        this.wait   = new WebDriverWait(driver, timeout);
    }

        // ---------- Wait / sync ----------
    protected WebElement waitVisible(WebElement el) {
        return wait.until(ExpectedConditions.visibilityOf(el));
    }

    protected WebElement waitClickable(WebElement el) {
        return wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    protected boolean waitGone(WebElement el) {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(el));
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected <T> T waitUntil(String reason, Supplier<T> supplier) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        RuntimeException last = null;
        while (System.currentTimeMillis() < end) {
            try {
                T v = supplier.get();
                if (v != null)
                    return v;
            } catch (RuntimeException e) {
                last = e;
            }
            throw new TimeoutException("Timeout waiting for: " + reason, last);
        }
        throw new TimeoutException("Timeout waiting for: " + reason, last);
    }

    // ---------- Safe actions ----------
    protected void clickWhenVisible(WebElement el) {
        waitVisible(el).click();
    }

    protected void typeWhenVisible(WebElement el, CharSequence text, boolean clear) {
        WebElement target = waitVisible(el);
        if (clear) target.clear();
        target.sendKeys(text);
    }

    protected String textOf(WebElement el) {
        return waitVisible(el).getText();
    }

    protected boolean isVisible(WebElement el) {
        try { return el.isDisplayed(); }
        catch (NoSuchElementException | StaleElementReferenceException ignored) { return false; }
    }

// ---------- Gestures básicos ----------
    protected void swipeUp(int pixels, int durationMs) { swipe(0.5, 0.8, 0.5, Math.max(0.1, 0.8 - pixels/2000.0), durationMs); }
    protected void swipeDown(int pixels, int durationMs) { swipe(0.5, 0.2, 0.5, Math.min(0.9, 0.2 + pixels/2000.0), durationMs); }
    protected void swipeLeft(int durationMs) { swipe(0.8, 0.5, 0.2, 0.5, durationMs); }
    protected void swipeRight(int durationMs) { swipe(0.2, 0.5, 0.8, 0.5, durationMs); }

    private void swipe(double startX, double startY, double endX, double endY, int durationMs) {
        Dimension size = driver.manage().window().getSize();
        int x1 = (int) (size.width * startX);
        int y1 = (int) (size.height * startY);
        int x2 = (int) (size.width * endX);
        int y2 = (int) (size.height * endY);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence seq = new Sequence(finger, 1);
        seq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x1, y1));
        seq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), x2, y2));
        seq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(seq));
    }

       // ---------- Micro aserciones utilitarias ----------
    protected void checkThat(String message, Supplier<Boolean> condition) {
        if (!Boolean.TRUE.equals(condition.get())) {
            throw new AssertionError(message);
        }
    }

    protected void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }

    public AndroidDriver driver() { return driver; }

}



