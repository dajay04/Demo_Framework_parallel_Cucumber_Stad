package stepdefinitions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.PropertiesUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Hooks {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static List<String> browsers = new ArrayList<>();
    private static AtomicInteger browserIndex = new AtomicInteger(0);

    /*******************  BeforeAll  *******************/
    @BeforeAll
    public static void setupExtentReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("Selenium Grid Execution");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setOfflineMode(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        String browserList = PropertiesUtil.getProperty("browsers");
        if (browserList != null && !browserList.isEmpty()) {
            for (String b : browserList.split(",")) {
                browsers.add(b.trim().toLowerCase());
            }
        } else {
            browsers = Collections.singletonList("chrome");
        }

        System.out.println("[INFO] Configured browsers for parallel run: " + browsers);
    }

    /*******************  Before each Scenario  *******************/
    @Before
    public void beforeScenario(Scenario scenario) {
        ExtentTest test = extent.createTest(scenario.getName());
        extentTest.set(test);

        String browser = getNextBrowser();
        boolean headless = Boolean.parseBoolean(getPropertySafe("headless", "false"));
        String runMode = getPropertySafe("run.mode", "local");

        logInfo("Launching browser: " + browser + " | Mode: " + runMode);

        WebDriver webDriver = createDriver(browser, headless, runMode);
        driver.set(webDriver);

        getDriver().manage().window().maximize();
    }

    /*******************  After each Scenario  *******************/
    @After
    public void afterScenario(Scenario scenario) {
        WebDriver webDriver = getDriver();
        ExtentTest test = extentTest.get();

        try {
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
                    test.pass("Scenario Passed");
                }
            }
        } finally {
            if (webDriver != null) {
                webDriver.quit();
                driver.remove();
            }
            extentTest.remove();
            extent.flush();
            System.out.println("[INFO] Finished Scenario: " + scenario.getName());
        }
    }

    /*******************  AfterAll  *******************/
    @AfterAll
    public static void tearDownExtentReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("[INFO] Extent Report generated successfully.");
        }
    }

    /*******************  Helper: Driver creation  *******************/
    private WebDriver createDriver(String browser, boolean headless, String runMode) {
        try {
            if ("remote".equalsIgnoreCase(runMode)) {
                return createRemoteDriver(browser, headless);
            } else {
                return createLocalDriver(browser, headless);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver for browser: " + browser, e);
        }
    }

    private WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions cOptions = new ChromeOptions();
                if (headless) cOptions.addArguments("--headless=new");
                return new ChromeDriver(cOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fOptions = new FirefoxOptions();
                if (headless) fOptions.addArguments("--headless");
                return new FirefoxDriver(fOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private WebDriver createRemoteDriver(String browser, boolean headless) throws Exception {
        String gridUrl = PropertiesUtil.getProperty("grid.url");
        if (gridUrl == null || gridUrl.isEmpty()) {
            gridUrl = "http://localhost:4444/wd/hub";
        }

        URL remoteUrl = new URL(gridUrl);

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless=new");
                return new RemoteWebDriver(remoteUrl, chromeOptions);

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                return new RemoteWebDriver(remoteUrl, firefoxOptions);

            default:
                throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }
    }

    /*******************  Helpers  *******************/
    private synchronized String getNextBrowser() {
        int index = browserIndex.getAndIncrement() % browsers.size();
        return browsers.get(index);
    }

    private String getPropertySafe(String key, String defaultValue) {
        String value = PropertiesUtil.getProperty(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    private static String saveScreenshotToFile(byte[] bytes, Scenario scenario) throws IOException {
        String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Path screenshotPath = Paths.get("reports/screenshots", scenarioName + "_" + timeStamp + ".png");
        Files.createDirectories(screenshotPath.getParent());
        Files.write(screenshotPath, bytes);
        return screenshotPath.toString();
    }

    /*******************  Logger methods for reporting  *******************/
    public static void logInfo(String msg) {
        if (extentTest.get() != null) extentTest.get().info(msg);
        System.out.println("[INFO] " + msg);
    }

    public static void logPass(String msg) {
        if (extentTest.get() != null) extentTest.get().pass(msg);
        System.out.println("[PASS] " + msg);
    }

    public static void logFail(String msg) {
        if (extentTest.get() != null) extentTest.get().fail(msg);
        System.out.println("[FAIL] " + msg);
    }

    public static void logWarning(String msg) {
        if (extentTest.get() != null) extentTest.get().warning(msg);
        System.out.println("[WARN] " + msg);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }
}