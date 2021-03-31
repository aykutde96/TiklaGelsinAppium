package com.Testinium.helper;

import com.Testinium.selector.Selector;
import com.Testinium.selector.SelectorFactory;
import com.Testinium.selector.SelectorType;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HookImpl {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AppiumDriver<MobileElement> appiumDriver;
    protected static FluentWait<AppiumDriver<MobileElement>> appiumFluentWait;
    protected boolean localAndroid = true;
    protected static Selector selector ;


    @BeforeScenario
    public void beforeScenario() throws MalformedURLException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Test baslıyor!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (StringUtils.isEmpty(System.getenv("key"))) {
            if (localAndroid) {
                logger.info("Local Browser");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
                // desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
                desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.ataexpress.tiklagelsin");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.ataexpress.tiklagelsin.MainActivity");
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities
                        .setCapability(MobileCapabilityType.FULL_RESET, false);

                desiredCapabilities.setCapability("autoDismissAlerts", true);
                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 3000);
                // desiredCapabilities.setCapability("unicodeKeyboard", true);
                // desiredCapabilities.setCapability("resetKeyboard", true);
                URL url = new URL("http://0.0.0.0:4723/wd/hub");
                appiumDriver = new AndroidDriver(url, desiredCapabilities);
            } else {
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.UDID, "00008030-00157936018B802E");
                desiredCapabilities
                        .setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.monitise.mea.CCI.TNEXT");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.DEVICE_NAME, "iPhonebatu");

                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.0.1");
                desiredCapabilities.setCapability("connectHardwareKeyboard", false);


                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);

                //desiredCapabilities.setCapability("sendKeyStrategy","setValue");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                appiumDriver = new IOSDriver(url, desiredCapabilities);


            }
        } else {
            String hubURL = "http://hub.testinium.io/wd/hub";
            DesiredCapabilities capabilities = new DesiredCapabilities();
            System.out.println("key:" + System.getenv("key"));
            System.out.println("platform" + System.getenv("platform"));
            System.out.println("version" + System.getenv("version"));
            if (System.getenv("platform").equals("ANDROID")) {
                capabilities.setCapability("key", System.getenv("key"));
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "co.moneye.android");
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "co.moneye.android.ui.splash.view.SplashActivity");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"uiautomator2");
                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                capabilities.setCapability("unicodeKeyboard", true);
                capabilities.setCapability("resetKeyboard", true);
                appiumDriver = new AndroidDriver(new URL(hubURL), capabilities);
                localAndroid = true;
            } else {
                localAndroid=false;
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!İos Test baslıyor!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                capabilities.setCapability("usePrebuiltWDA", true); //changed
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability("waitForAppScript", "$.delay(1000);");
                capabilities.setCapability("bundleId", "com.monitise.mea.CCI.TNEXT");
                capabilities.setCapability("usePrebuiltWDA",true);
                capabilities.setCapability("useNewWDA", true);
                capabilities.setCapability("autoAcceptAlerts",true);
                appiumDriver = new IOSDriver(new URL(hubURL), capabilities);
            }
        }
        selector = SelectorFactory
                .createElementHelper(localAndroid ? SelectorType.ANDROID : SelectorType.IOS);
        //appiumDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        appiumFluentWait = new FluentWait<AppiumDriver<MobileElement>>(appiumDriver);
        appiumFluentWait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(450))
                .ignoring(NoSuchElementException.class);



    }


    @AfterScenario
    public void afterScenario() {
        if(appiumDriver != null)
            appiumDriver.quit();
    }

}
