package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage extends BaseTest
{
    public DashboardPage(WebDriver driver)
    {
        super(driver);
    }
    //3. Identify webelements
    private By dashBoardElementText = By.xpath("//h6[contains(.,'Dashboard')]");
    public By menuItems = By.cssSelector("ul.oxd-main-menu > li span.oxd-text");
    //4. Actions

    public void waitTillDashboardTextAppear()
    {
       getElement(dashBoardElementText);
    }
    public List<WebElement> allMenuItems()
    {
        getElement(menuItems);
        return getElements(menuItems);
    }
}
