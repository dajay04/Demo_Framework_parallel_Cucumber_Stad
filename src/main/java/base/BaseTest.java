package base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTest
{
    protected WebDriver driver;
    protected WebDriverWait wait;
    public static long randomNumber;

    public BaseTest(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        randomNumber = (long) (Math.random()*10000 + 33333300000L); // 1263126371273
    }
    public void jsClick(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
    public void doubleClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        new Actions(driver).doubleClick(el).perform();
    }

    /** Waits for presence and returns the element if found, or null otherwise */
    public WebElement getElement(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            return null;
        }
    }

    /** Waits for element visibility and returns true if displayed, false otherwise */
    public boolean isElementDisplayed(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Clicks the element after waiting for it to be clickable */
    public void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
    }

    /** Enters a value in input after waiting for element to appear */
    public void enterValue(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }

    /** Returns list of elements located by locator */
    public List<WebElement> getElements(By locator)
    {
        return driver.findElements(locator);
    }

    /** Moves to the supplied element */
    public void moveToElement(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    /** Scrolls to the element (uses JS) */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /** Checks if an element's attribute is present/non-null */
    public boolean isAttributePresent(By locator, String attribute) {
        WebElement el = getElement(locator);
        if (el == null) return false;
        return el.getAttribute(attribute) != null;
    }

    /** Selects a value from a dropdown based on visible text */
    public void selectDropdownByText(By locator, String visibleText) {
        WebElement dropdownEl = getElement(locator);
        if (dropdownEl != null) {
            Select select = new Select(dropdownEl);
            select.selectByVisibleText(visibleText);
        }
    }

    /** Checks availability of an option in dropdown (by visible text) */
    public boolean isOptionAvailableInDropdown(By locator, String optionText) {
        WebElement dropdownEl = getElement(locator);
        if (dropdownEl == null) return false;
        Select select = new Select(dropdownEl);
        return select.getOptions().stream()
                .anyMatch(opt -> opt.getText().equalsIgnoreCase(optionText));
    }

    /** Switches to browser alert and accepts/dismisses as per action */
    public void handleAlert(String action) {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            if ("accept".equalsIgnoreCase(action)) {
                alert.accept();
            } else if ("dismiss".equalsIgnoreCase(action) || "cancel".equalsIgnoreCase(action)) {
                alert.dismiss();
            }
        } catch (TimeoutException ignored) {}
    }

    /** Switches to another window by index */
    public void switchToWindow(int index) {
        List<String> handles = driver.getWindowHandles()
                .stream()
                .collect(Collectors.toList());
        if (index < handles.size()) {
            driver.switchTo().window(handles.get(index));
        }
    }


    /** Switches to another window by handle */
    public void switchToWindowHandle(String handle) {
        driver.switchTo().window(handle);
    }

    /** Sets checkbox to checked (true) or unchecked (false) */
    public void setCheckbox(By locator, boolean checked) {
        WebElement el = getElement(locator);
        if (el != null && el.isSelected() != checked) {
            el.click();
        }
    }

    /** Clicks a radio button if not already selected */
    public void selectRadioButton(By locator) {
        WebElement el = getElement(locator);
        if (el != null && !el.isSelected()) {
            el.click();
        }
    }

    /** Uploads a file using sendKeys to an <input type="file"> element */
    public void uploadFile(By locator, String filePath) {
        WebElement fileInput = getElement(locator);
        if (fileInput != null) {
            fileInput.sendKeys(filePath);
        }
    }

    /** Gets the text of the element, or null if not found */
    public String getText(By locator) {
        WebElement el = getElement(locator);
        return el != null ? el.getText() : null;
    }
}
