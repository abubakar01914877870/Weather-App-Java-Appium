package weatherAppTest;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.cucumber.java.*;
import io.cucumber.java.en.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {
    int MAX_TIMEOUT = 30;
    static AppiumDriverLocalService service;
    AndroidDriver driver;

    @BeforeAll
    public static void beforeAll(){
        System.out.println("Before All");
        service = AppiumDriverLocalService.buildDefaultService();
        service.start();
    }

    @Before
    public void doSomethingBefore() {
        UiAutomator2Options options = new UiAutomator2Options()
                //.setUdid("VCQ47TS8RCPFOZLN")
                .setUdid("emulator-5554")
                .setAutoGrantPermissions(true)
                .setApp("src/test/resources/apk/app-debug.apk");
        driver = new AndroidDriver(
                // The default URL in Appium 1 is http://127.0.0.1:4723/wd/hub
                service.getUrl(), options
        );

        // Grant location permission manually
       // driver.findElement(AppiumBy.xpath("//android.view.View[@text='Allow']")).click();
       // driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")).click();

    }


    @After
    public void close_driver(){
        driver.quit();
    }
    @AfterAll
    public static void afterAll(){
        System.out.println("After All");
        service.stop();
    }


    @Given("I am a general user of the weather forcast")
    public void i_am_a_general_user_of_the_weather_forcast() {
        WebElement today = locateElementAfterElementIsVisible(driver, "//android.view.View[@text='Today']");
        assertEquals("Today", today.getText());
    }

    @When("I request the current weather")
    public void i_request_the_current_weather() {
        WebElement today_temp = locateElementAfterElementIsVisible(driver, "//android.view.ViewGroup/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View[1]");
        Assertions.assertTrue((today_temp.getText()).contains("°C"), "Today temperature doesn't contain °C");
    }
    @Then("The weather app displays the current weather forcast including temperature and condition")
    public void the_weather_app_displays_the_current_weather_forcast_including_temperature_and_condition() {
        WebElement today_condition = locateElementAfterElementIsVisible(driver, "//android.view.ViewGroup/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View[2]");
        Assertions.assertFalse((today_condition.getText()).isEmpty(), "Today condition should not be empty");

        WebElement min_temp = locateElementAfterElementIsVisible(driver, "//android.view.ViewGroup/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View[3]");
        Assertions.assertTrue((min_temp.getText()).contains("Min Today:"), "Min Today: doesn't exist");

        WebElement max_temp = locateElementAfterElementIsVisible(driver, "//android.view.ViewGroup/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View[4]");
        Assertions.assertTrue((max_temp.getText()).contains("Max Today:"), "Max Today: doesn't exist");
    }
    @When("I request to view hourly weather forcasts for the day")
    public void i_request_to_view_hourly_weather_forcasts_for_the_day() {
        List<WebElement> scroll_view =  driver.findElements(AppiumBy.xpath("//android.widget.HorizontalScrollView"));
        assertEquals(2, scroll_view.size());

    }

    @Then("The weather app provides me with weather forcasts for each hour including temperature and conditions")
    public void the_weather_app_provides_me_with_weather_forcasts_for_each_hour_including_temperature_and_conditions() throws InterruptedException {
        List<WebElement> scroll_view;
        String txt = "";
        for(int i = 0; i < 24; i++){
            scroll_view =  driver.findElements(AppiumBy.xpath("//android.widget.HorizontalScrollView"));
            List<WebElement> hour_scroll_view = scroll_view.get(0).findElements(AppiumBy.xpath("//android.view.View"));
            txt = txt.concat(getTextFromElementList(hour_scroll_view)+" \n");
            swipeGestureElement(scroll_view.get(0));
        }

        String [] hours_24 = {
                "00", "01", "02", "03", "04",
                "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19",
                "20", "21", "22", "23"
        };
        for(String h: hours_24){
            String input = h.concat(":00.+?%.+?°C");
            Assertions.assertTrue(checkString(input, txt));
        }
    }

    @When("I Select to view weekly weather forcast")
    public void i_select_to_view_weekly_weather_forcast() {
        List<WebElement> scroll_view =  driver.findElements(AppiumBy.xpath("//android.widget.HorizontalScrollView"));
        assertEquals(2, scroll_view.size());
    }
    @Then("The weather app displays the weather forcast for the entire week")
    public void the_weather_app_displays_the_weather_forcast_for_the_entire_week() {
        List<WebElement> scroll_view;
        String txt = "";
        for(int i = 0; i < 7; i++){
            scroll_view =  driver.findElements(AppiumBy.xpath("//android.widget.HorizontalScrollView"));
            List<WebElement> hour_scroll_view = scroll_view.get(1).findElements(AppiumBy.xpath("//android.view.View"));
            txt = txt.concat(getTextFromElementList(hour_scroll_view)+" \n");
            swipeGestureElement(scroll_view.get(1));
        }
        String [] week_days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for(String day: week_days){
            String input = day.concat(".+?%.+?°C");
            Assertions.assertTrue(checkString(input, txt));
        }
    }

    public WebElement locateElementAfterElementIsVisible(WebDriver driver, String locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(MAX_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(locator)));
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath(locator)));
        return driver.findElement(AppiumBy.xpath(locator));
    }

    public void swipeGestureElement(WebElement element){
        // Scroll left to right
        ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "direction", "left",
                "percent", 0.4,
                "speed", 320
        ));
    }

    public String getTextFromElementList(List<WebElement> element_list){
        String element_txt = "";
        for(WebElement el : element_list){
            element_txt = element_txt.concat(el.getText()+" ");
        }
        return element_txt;
    }

    public boolean checkString(String input, String target){
        // Create a Pattern object from the pattern string
        Pattern pattern = Pattern.compile(input);

        // Create a Matcher object and apply the pattern to the target string
        Matcher matcher = pattern.matcher(target);

        return matcher.find();
    }


}
