package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("features")
//@ConfigurationParameter(key = "cucumber.filter.tags", value = "@sanity or @smoke")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-report.html, com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")
@ConfigurationParameter(key = "cucumber.glue", value = "stepdefinitions")
public class RunCucumberTest {

}
