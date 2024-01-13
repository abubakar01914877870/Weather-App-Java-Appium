package weatherAppTest;


import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class WeatherAppTest {

    public static void main(String[] args) throws MalformedURLException {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
        service.start();
        try {
            System.out.println("ServerInfo: "+  service.getUrl());
            UiAutomator2Options options = new UiAutomator2Options()
                    .setUdid("emulator-5554")
                    .setApp("src/test/resources/apk/app-debug.apk");
            AndroidDriver driver = new AndroidDriver(
                    // The default URL in Appium 1 is http://127.0.0.1:4723/wd/hub
                    service.getUrl(), options
            );
            try {
               // WebElement el = driver.findElement(AppiumBy.xpath, "//Button");
               // el.click();
                System.out.println("info: "+driver.getPageSource());
            } finally {
                driver.quit();
            }

        } finally {
            service.stop();
        }



    }

//    public static void openWeatherApp(){
//        DesiredCapabilities cap = new DesiredCapabilities();
//        cap.setCapability('deviceName', 'emulator');
//        cap.setCapability('udid', 'emulator-5554');
//        cap.setCapability('platformName', 'Android');
//        cap.setCapability('platformVersion', '14');
//
//        cap.setCapability('appPackage', 'me.example.android.weatherapp');
//        cap.setCapability('appActivity', 'me.example.android.weatherapp.MainActivity');
//    }
}
