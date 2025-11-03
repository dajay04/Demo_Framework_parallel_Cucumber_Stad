package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import tests.DashboardPage;
import tests.LoginPage;
import utils.PropertiesUtil;

public class LoginSteps
{
    public LoginPage loginPage = new LoginPage(Hooks.getDriver());
    public DashboardPage dashboardPage  = new DashboardPage(Hooks.getDriver());

    @Given("User login into LoginPage")
    public void userLoginIntoLoginPage()
    {
        Hooks.getDriver().get(PropertiesUtil.getProperty("base.url")); // https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
    }
    @When("User enters valid username {string} and password {string}")
    public void userEntersValidUsernameAndPassword(String username, String password)
    {
        loginPage.enterUserName(username);
        loginPage.enterPassword(password);
    }

    @And("user clicks on Login Button")
    public void userClicksOnLoginButton()
    {
        loginPage.clickingLogin();
    }

    @Then("User redirected to Dashboard Page")
    public void userRedirectedToDashboardPage() throws Exception
    {
        dashboardPage.waitTillDashboardTextAppear();
        String currentUrl = Hooks.getDriver().getCurrentUrl();
        Thread.sleep(7000);
        Assertions.assertEquals
                ("https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index"
                        ,currentUrl
                        ,"Dashboard URL didn't Matchedd!!!!!"
                );
    }
    @Then("User verifies Error Message should be displayed as {string}")
    public void userVeirifiesErrorMessageShouldBeDisplayedAs(String expectedMessage)
    {
        String actualText = loginPage.getErrorText();
        Assertions.assertEquals(expectedMessage,actualText ," Error Text Not Matches!!!!");
    }
}
