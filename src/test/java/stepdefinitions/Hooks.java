package stepdefinitions;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import utils.PropertiesUtil;

import java.net.URI;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeAll
    public static void setupExtentReport() {
        // Create Spark Reporter
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");

        // Optional configurations
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("EWF QA Automation Execution");
        spark.config().setTheme(Theme.STANDARD); // or Theme.DARK
        spark.config().setOfflineMode(true); // <--- CRITICAL: makes it self-contained (works on VPN)

        // Attach reporter
        extent = new ExtentReports();
        extent.attachReporter(spark);

        // System info (optional)
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        ExtentTest test = extent.createTest(scenario.getName());
        extentTest.set(test);


        String browser = PropertiesUtil.getProperty("browser");
        String headlessProp = PropertiesUtil.getProperty("headless");
        boolean headless = "true".equalsIgnoreCase(headlessProp);

        WebDriver webDriver = createDriver(browser, headless);
        driver.set(webDriver);
        logInfo("WebDriver initialized for browser: " + browser);
    }

    private WebDriver createDriver(String browser, boolean headless) {
        WebDriver webDriver;
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fOptions = new FirefoxOptions();
                if (headless) fOptions.addArguments("--headless");
                webDriver = new FirefoxDriver(fOptions);
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions cOptions = new ChromeOptions();
                if (headless) {
                    cOptions.addArguments("--headless", "--window-size=1920,1080");
                }
                webDriver = new ChromeDriver(cOptions);
                break;
            case "ie":
                WebDriverManager.iedriver().setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                webDriver = new InternetExplorerDriver(ieOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        webDriver.manage().window().maximize();
        return webDriver;
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver webDriver = driver.get();
        ExtentTest test = extentTest.get();

        if (webDriver != null) {
            if (scenario.isFailed()) {
                try {
                    byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
                    String screenshotPath = saveScreenshotToFile(screenshot, scenario);
                    test.fail("Scenario Failed: " + scenario.getName())
                            .addScreenCaptureFromPath(screenshotPath);
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");
                } catch (Exception e) {
                    test.warning("Failed to capture screenshot: " + e.getMessage());
                }
            } else {
                test.pass("Scenario passed");
            }
            webDriver.quit();
            driver.remove();
            extentTest.remove();
        }
        extent.flush();
    }

    private static String saveScreenshotToFile(byte[] bytes, Scenario scenario) throws Exception {
        String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Path screenshotPath = Paths.get("reports/screenshots", scenarioName + "_" + timeStamp + ".png");
        Files.createDirectories(screenshotPath.getParent());
        Files.write(screenshotPath, bytes);
        return screenshotPath.toString();
    }

    @AfterAll
    public static void tearDownExtentReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void logInfo(String msg) { extentTest.get().info(msg); }
    public static void logPass(String msg) { extentTest.get().pass(msg); }
    public static void logFail(String msg) { extentTest.get().fail(msg); }
    public static void logWarning(String msg) { extentTest.get().warning(msg); }
}
