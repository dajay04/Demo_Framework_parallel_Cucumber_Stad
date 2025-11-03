package stepdefinitions;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.DashboardPage;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardSteps
{
    DashboardPage dashboardPage = new DashboardPage(Hooks.getDriver());
    @Then("User verifies elements on Dashboard menu items:")
    public void user_verifies_elements_on_dashboard_menu_items(List<String> expectedMenuItems)
    {
        //1. We have to capture all elements on Menu items ( ul.oxd-main-menu > li span.oxd-text ) all elements
        List<WebElement> actualMenuElements = dashboardPage.allMenuItems();

        //2. Convert that List Element to List String
        List<String> actualTextMenuItems = actualMenuElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList() );

        //3. We are travessing Expected List and check if any String matches with actualMenuItems
        for (String expectedItem:expectedMenuItems)
        {
            Assertions.assertTrue(actualTextMenuItems.contains(expectedItem),"Dashboard items missing!!!!!");
        }
    }
}
