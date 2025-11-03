package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;

import java.time.Duration;

public class LoginPage extends BaseTest
{
    public LoginPage(WebDriver driver)
    {
        super(driver);
    }

    //1. Identify webelements
    private By userNameField = By.xpath("//input[@name='username']");
    private By passwordField = By.xpath("//input[@name='password' and @type='password']");
    private By loginButton = By.xpath("//button[contains(.,'Login')]");
    private By errorText = By.xpath("//p[contains(.,'Invalid credentials')]");
    //4. Actions

    public void enterUserName(String username)
    {
        try
        {
            enterValue(userNameField,username);
            Hooks.logPass("Username Entered Successfully");
        }
        catch(Exception e)
        {
            Hooks.logFail("Username Entered Failed!!! Exception occured!!!");
            e.printStackTrace();
        }

    }

    public void enterPassword(String password)
    {
        try
        {
            enterValue(passwordField,password);
            Hooks.logPass("Password Entered Successfully");
        }
        catch(Exception e)
        {
            Hooks.logFail("Password Entered Failed!!! Exception occured!!!");
            e.printStackTrace();
        }

    }

    public void clickingLogin()
    {
        try
        {
            click(loginButton);
            Hooks.logPass("Login Button Clicked Successfully");
        }
        catch(Exception e)
        {
            Hooks.logFail("Login Button Clicked Failed!!! Exception occured!!!");
            e.printStackTrace();
        }
    }

    public String getErrorText()
    {
        try {
            String text = getText(errorText);
            Hooks.logPass("Data Returned Successfully!");
            return text;
        } catch (Exception e)
        {
            Hooks.logFail("Data Returned Failed! Exception occurred: " + e.getMessage());
            return null; // or return "" depending on your use case
        }
    }


}
