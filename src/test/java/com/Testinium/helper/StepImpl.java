package com.Testinium.helper;


import com.Testinium.helper.RandomString;
import com.Testinium.helper.StoreHelper;
import com.Testinium.model.SelectorInfo;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.KeyEventMetaModifier;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.time.Duration.ofMillis;

public class StepImpl extends HookImpl {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static int DEFAULT_MAX_ITERATION_COUNT = 70;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    public List<MobileElement> findElements(By by) throws Exception {
        List<MobileElement> webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List<MobileElement>>() {
                @Nullable
                @Override
                public List<MobileElement> apply(@Nullable WebDriver driver) {
                    List<MobileElement> elements = driver.findElements(by);
                    return elements.size() > 0 ? elements : null;
                }
            });
            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }
        } catch (Exception e) {
            throw e;
        }
        return webElementList;
    }

    public List<MobileElement> findElementsWithoutAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
        }
        return mobileElements;
    }

    public List<MobileElement> findElementsWithAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Elements not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElements;
    }


    public MobileElement findElement(By by) throws Exception {
        MobileElement mobileElement;
        try {
            mobileElement = findElements(by).get(0);
        } catch (Exception e) {
            throw e;
        }
        return mobileElement;
    }

    public MobileElement findElementWithoutAssert(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            //   e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementWithAssertion(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Element not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElement;
    }

    public void sendKeysTextByAndroidKey(String text) {

        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        char[] chars = text.toCharArray();
        String stringValue = "";
        for (char value : chars) {
            stringValue = String.valueOf(value);
            if (Character.isDigit(value)) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf("DIGIT_" + String.valueOf(value))));
            } else if (Character.isLetter(value)) {
                if (Character.isLowerCase(value)) {
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue.toUpperCase())));
                } else {
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue))
                            .withMetaModifier(KeyEventMetaModifier.SHIFT_ON));
                }
            } else if (stringValue.equals("@")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.AT));
            } else if (stringValue.equals(".")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.PERIOD));
            } else if (stringValue.equals(" ")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.SPACE));
            } else {
                Assert.fail("Metod " + stringValue + " desteklemiyor.");
            }
        }
        logger.info(text + " texti AndroidKey yollanarak yazıldı.");
    }

    public MobileElement findElementByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Element not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElement;
    }


    public List<MobileElement> findElemenstByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElements;
    }

    public List<MobileElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Elements not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElements;
    }


    @Step({"Değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element text equals <text> and click"})
    public void clickByText(String text) {
        findElementWithAssertion(By.xpath(".//*[contains(@text,'" + text + "')]")).click();
    }

    @Step({"Pincode Değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element pincode area equals <text> and click"})
    public void clickByTextonPincodeArea(String text) {
        findElementWithAssertion(By.xpath("//XCUIElementTypeButton[@name='" + text + "']")).click();
    }

    @Step({"Pizza <text> e eşit olan elementli bul ve tıkla",
            "Find element pizza  equals <text> and click"})
    public void clickByTextonPizza(String text) {
        findElementWithAssertion(By.xpath("//XCUIElementTypeStaticText[@name='" + text + "']")).click();
    }

    @Step({"İçeriği <value> e eşit olan elementli bul ve tıkla",
            "Find element value equals <value> and click"})
    public void clickByValue(String value) {
        findElementWithAssertion(MobileBy.xpath(".//*[contains(@value,'" + value + "')]")).click();
    }

    @Step({"Değeri <text> e eşit olan <index>. elementi bul ve tıkla"})
    public void clickByText(String text, int index) {
        findElementWithAssertion(By.xpath("(.//*[contains(@text,'" + text + "')])[" + index + "]")).click();
    }

    @Step({"İçeriği <value> e eşit olan <index>. elementi bul ve tıkla"})
    public void clickByValue(String value, int index) {
        findElementWithAssertion(MobileBy.xpath("(.//*[contains(@value,'" + value + "')])[" + index + "]")).click();
    }

    @Step("Uygulamanın açıldığını kontrol et")
    public void checkApp() throws InterruptedException {
        logger.info("Uygulamanin acildigini kontrol et");
        if (appiumDriver instanceof AndroidDriver) {
            existClickByKey("reddetButonu");
            clickBybackButton();
            waitBySecond(10);
        } else {
            existClickByKey("reddetButonu");
            waitBySecond(10);
        }

    }

    @Step("Kullanıcı girişi yapılmışsa çıkış yap")
    public void logout() throws InterruptedException {
        waitBySecond(3);
        if (!appiumDriver.getPageSource().contains("Giriş Yap")) {
            logger.info("Giris yap yok");
            clickByKey("profilSekmesiBtn");
            waitBySecond(3);
            swipe(2);
            clickByKeyWithSwipe("profilAyarGit");
            waitBySecond(3);
            swipe(2);
            clickByKeyWithSwipe("cikisButonu");
            clickByKey("popupCikisYapButonu");
            waitBySecond(5);
            getPageSourceFindWord("Giriş Yap");
        }
    }


    @Step({"<key> li elementi bul ve tıkla", "Click element by <key>"})
    public void clickByKey(String key) {
        doesElementExistByKey(key, 5);
        doesElementClickableByKey(key, 5);
        //       logger.info("appiumDriver.getPageSource().contains(key)");
        findElementByKey(key).click();
//        MobileElement me = findElementByKey(key);
//        tapElementWithCoordinate(me.getLocation().x, me.getLocation().y);
        logger.info(key + " elementine tıklandı");


    }

    @Step({"<key> li element sayfada bulunuyor mu kontrol et", "Check if <key> element exist"})
    public void existElement(String key) {
        Assert.assertTrue(key + " elementi sayfada bulunamadı!", findElementByKey(key).isEnabled());
        logger.info("Found element: " + key);
    }

    @Step("giris yap butonuna tikla")
    public void clickLoginButton() throws InterruptedException {

        newWebDriverWait(30, 1000);

        findElementWithAssertion(By.id("com.ttech.android.onlineislem:id/buttonPasswordContinue")).click();

        waitBySecond(5);
        existClickByKey("dinamikKartlarıKapatmaIkonu");
    }


    @Step({"<key> li elementi bul ve varsa tıkla", "Click element by <key> if exist"})
    public void existClickByKey(String key) {

        MobileElement element;

        element = findElementByKeyWithoutAssert(key);

        if (element != null) {
            System.out.println("  varsa tıklaya girdi");
            Point elementPoint = ((MobileElement) element).getCenter();
            TouchAction action = new TouchAction(appiumDriver);
            action.tap(PointOption.point(elementPoint.x, elementPoint.y)).perform();
        }
    }

    @Step({"<key> li elementi bul ve varsa dokun", "Click element by <key> if exist"})
    public void existTapByKey(String key) {

        WebElement element = null;
        try {
            element = findElementByKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            element.click();
        }
    }

    @Step({"sayfadaki <X> <Y>  alana dokun"})
    public void coordinateTap(int X, int Y) {

        Dimension dimension = appiumDriver.manage().window().getSize();
        int width = dimension.width;
        int height = dimension.height;

        TouchAction action = new TouchAction(appiumDriver);
        action.tap(PointOption.point((width * X) / 100, (height * Y) / 100))
                .release().perform();

    }


    @Step({"<key> li elementi bul, temizle ve <text> değerini yaz",
            "Find element by <key> clear and send keys <text>"})
    public void sendKeysByKey(String key, String text) {
        MobileElement webElement = findElementByKey(key);
        webElement.click();
        // webElement.sendKeys(Keys.DELETE);
        webElement.clear();
        webElement.setValue(text);
        webElement.sendKeys();
    }

    @Step({"<Key> li elementi bul ve temizle",
            "Find element by <key> and clear text"})
    public void ClearText(String key) {
        MobileElement webElement = findElementByKey(key);
        webElement.clear();
        logger.info(key + " elementinin değeri temizlendi.");
        if (appiumDriver instanceof IOSDriver) {
            findElementByKey(key).clear();
            findElementByKey(key).clear();
            findElementByKey(key).clear();
//            int lentgh = findElementByKey(key).getText().length();
//            for (int i=0 ; i> lentgh ; i++){
//                element.sendKeys(Keys.BACK_SPACE);
//            }
            logger.info("IOS değeri temizlendi.");
        }

    }


    @Step({"<key> li elementi bul ve <text> değerini yaz",
            "Find element by <key> and send keys <text>"})
    public void sendKeysByKeyNotClear(String key, String text) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).sendKeys(text);

    }

    @Step("<text> değerini klavye ile yaz")
    public void sendKeysWithAndroidKey(String text) {
        sendKeysTextByAndroidKey(text);

    }

    @Step("<key> li elementi bul ve <text> değerini tek tek yaz")
    public void sendKeysValueOfClear(String key, String text) {
        MobileElement me = findElementByKey(key);
        me.clear();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            me.sendKeys(String.valueOf(c));
        }
        System.out.println("'" + text + "' written to '" + key + "' element.");

    }

    @Step({"<key> li elementi bul ve değerini <saveKey> olarak sakla",
            "Find element by <key> and save text <saveKey>"})
    public void saveTextByKey(String key, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey, findElementByKey(key).getText());
        logger.info(saveKey + " variable is stored with: " + findElementByKey(key).getText());
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKey(String key, String saveKey) {
        Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), findElementByKey(key).getText());
        logger.info("Element by " + key + " is equal to " + StoreHelper.INSTANCE.getValue(saveKey));
        logger.info("Element by " + key + " contains: " + findElementByKey(key).getText() + " and Equals to " + saveKey + ": " + StoreHelper.INSTANCE.getValue(saveKey));
    }


    @Step({"<key> li ve değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element by <key> text equals <text> and click"})
    public void clickByIdWithContains(String key, String text) {
        List<MobileElement> elements = findElemenstByKey(key);
        for (MobileElement element : elements) {
            logger.info("Text !!!" + element.getText());
            if (element.getText().toLowerCase().contains(text.toLowerCase())) {
                element.click();
                break;
            }
        }
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementli bulana kadar swipe et ve tıkla",
            "Find element by <key> text equals <text> swipe and click"})
    public void clickByKeyWithSwipe(String key, String text) throws InterruptedException {
        boolean find = false;
        int maxRetryCount = 10;
        while (!find && maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            for (MobileElement element : elements) {
                if (element.getText().contains(text)) {
                    element.click();
                    find = true;
                    break;
                }
            }
            if (!find) {
                maxRetryCount--;
                if (appiumDriver instanceof AndroidDriver) {
                    swipeUpAccordingToPhoneSize();
                    waitBySecond(1);
                } else {
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);
                }
            }
        }
    }

    @Step({"<key> li elementi bulana kadar swipe et ve tıkla",
            "Find element by <key>  swipe and click"})
    public void clickByKeyWithSwipe(String key) throws InterruptedException {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);

                } else {
                    elements.get(0).click();
                    logger.info(key + " elementine tıklandı");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();
                waitBySecond(1);
            }
        }
    }


    private int getScreenWidth() {
        return appiumDriver.manage().window().getSize().width;
    }

    private int getScreenHeight() {
        return appiumDriver.manage().window().getSize().height;
    }

    private int getScreenWithRateToPercent(int percent) {
        return getScreenWidth() * percent / 100;
    }

    private int getScreenHeightRateToPercent(int percent) {
        return getScreenHeight() * percent / 100;
    }


    public void swipeDownAccordingToPhoneSize(int startXLocation, int startYLocation, int endXLocation, int endYLocation) {
        startXLocation = getScreenWithRateToPercent(startXLocation);
        startYLocation = getScreenHeightRateToPercent(startYLocation);
        endXLocation = getScreenWithRateToPercent(endXLocation);
        endYLocation = getScreenHeightRateToPercent(endYLocation);

        new TouchAction(appiumDriver)
                .press(PointOption.point(startXLocation, startYLocation))
                .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                .moveTo(PointOption.point(endXLocation, endYLocation))
                .release()
                .perform();
    }

    @Step({"<key> id'li elementi bulana kadar <times> swipe yap ",
            "Find element by <key>  <times> swipe "})
    public void swipeDownUntilSeeTheElement(String element, int limit) throws InterruptedException {
        for (int i = 0; i < limit; i++) {
            List<MobileElement> meList = findElementsWithoutAssert(By.id(element));
            meList = meList != null ? meList : new ArrayList<MobileElement>();

            if (meList.size() > 0 &&
                    meList.get(0).getLocation().x <= getScreenWidth() &&
                    meList.get(0).getLocation().y <= getScreenHeight()) {
                break;
            } else {
                swipeDownAccordingToPhoneSize(50, 80, 50, 30);
                waitBySecond(1);

                break;
            }
        }
    }


    @Step({"<key> li elementi bulana kadar swipe et",
            "Swipe down until find element by <key>"})
    public void findByKeyWithSwipe(String key) {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;

                    swipeDownAccordingToPhoneSize();

                } else {
                    logger.info(key + " element bulundu");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();

            }

        }
    }


    @Step(" <yön> yönüne swipe et")
    public void swipe(String yon) {

        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        if (yon.equals("SAĞ")) {

            int swipeStartWidth = (width * 80) / 100;
            int swipeEndWidth = (width * 30) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else if (yon.equals("SOL")) {

            int swipeStartWidth = (width * 30) / 100;
            int swipeEndWidth = (width * 80) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);

            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();

        }
    }

    @Step({"<key> li elementi bul yoksa <message> mesajını hata olarak göster",
            "Find element by <key> if not exist show error message <message>"})
    public void isElementExist(String key, String message) {
        Assert.assertTrue(message, findElementByKey(key) != null);
    }

    @Step({"<key> li elementin değeri <text> e içerdiğini kontrol et",
            "Find element by <key> and text contains <text>"})
    public void containsTextByKey(String key, String text) {
        By by = selector.getElementInfoToBy(key);
        Assert.assertTrue(appiumFluentWait.until(new ExpectedCondition<Boolean>() {
            private String currentValue = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentValue = driver.findElement(by).getAttribute("value");
                    return currentValue.contains(text);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("text contains be \"%s\". Current text: \"%s\"", text, currentValue);
            }
        }));
    }

    @Step({"<key> li elementin değeri <text> e eşitliğini kontrol et",
            "Find element by <key> and text equals <text>"})
    public void equalsTextByKey(String key, String text) {
        Assert.assertTrue(key + " elementinin text değeri " + text + " e eşit değil!",
                appiumFluentWait.until(ExpectedConditions.textToBe(selector.getElementInfoToBy(key), text)));
        logger.info(key + " elementinin text değerinin " + text + " e eşit olduğu kontrol edildi");
    }

    @Step({"<seconds> saniye bekle", "Wait <second> seconds"})
    public void waitBySecond(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
            logger.info(seconds + " saniye beklendi.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Testinium")
    public void TestiniumPage() {
        waitBySecond(3);
    }

    public void swipeUpAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println(width + "  " + height);

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 40) / 100;
            int swipeEndHeight = (height * 60) / 100;
            System.out.println("Start width: " + swipeStartWidth + " - Start height: " + swipeStartHeight + " - End height: " + swipeEndHeight);
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((AndroidDriver) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 70) / 100;
            int swipeEndHeight = (height * 30) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        }
    }


    public void swipeDownAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println("Android1 : " + width + " - " + height);
            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 60) / 100;
            int swipeEndHeight = (height * 20) / 100;
            System.out.println("Start width: " + swipeStartWidth + " - Start height: " + swipeStartHeight + " - End height: " + swipeEndHeight);
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 40) / 100;
            int swipeEndHeight = (height * 20) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    public boolean isElementPresent(By by) {
        return findElementWithoutAssert(by) != null;
    }


    @Step({"<times> kere aşağıya kaydır", "Swipe times <times>"})
    public void swipe(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeDownAccordingToPhoneSize();
            waitBySecond(2);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }
    }

    @Step({"Sayfanın  ortasından itibaren  <times> kere aşağıya kaydır", "Middle  on page  and Swipe times <times>"})
    public void MiddlePageswipe(int times) {
        for (int i = 0; i < times; i++) {
            if (appiumDriver instanceof AndroidDriver) {
                Dimension d = appiumDriver.manage().window().getSize();
                int width = d.width;

                int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
                int swipeStartHeight = (400 * 90) / 100;
                int swipeEndHeight = (400 * 50) / 100;
                //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
                new TouchAction(appiumDriver)
                        .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                        .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                        .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                        .release()
                        .perform();
            } else {
                Dimension d = appiumDriver.manage().window().getSize();
                // int height = d.height;
                int width = d.width;

                int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
                int swipeStartHeight = (400 * 90) / 100;
                int swipeEndHeight = (400 * 40) / 100;
                // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
                new TouchAction(appiumDriver)
                        .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                        .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                        .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                        .release()
                        .perform();
            }
        }
        waitBySecond(3);

        System.out.println("-----------------------------------------------------------------");
        System.out.println("SWİPE EDİLDİ");
        System.out.println("-----------------------------------------------------------------");

    }


    @Step({"<times> kere yukarı doğru kaydır", "Swipe up times <times>"})
    public void swipeUP(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeUpAccordingToPhoneSize();
            waitBySecond(1);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }
    }
    @Step({"Random swipe yap"})
    public void swipeRandom() throws InterruptedException {
        Random rnd = new Random();

        for (int i = 0; i < rnd.nextInt(4); i++) {
            swipeUpAccordingToPhoneSize();
            waitBySecond(1);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }

    }


    @Before
    public void setUp() throws Exception {

    }

    @Step({"Klavyeyi kapat", "Hide keyboard"})
    public void hideAndroidKeyboard() {
        try {

            if (localAndroid == false) {
                findElementWithoutAssert(By.xpath("//XCUIElementTypeButton[@name='Toolbar Done Button']")).click();
            } else {
                appiumDriver.hideKeyboard();
            }

        } catch (Exception ex) {
            logger.error("Klavye kapatılamadı ", ex.getMessage());
        }
    }

    @Step({"<key> değerini sayfa üzerinde olup olmadığını kontrol et."})
    public void getPageSourceFindWord(String key) {
        logger.info("Page sources   !!!!!   " + appiumDriver.getPageSource());
        Assert.assertTrue(key + " sayfa üzerinde bulunamadı.",
                appiumDriver.getPageSource().contains(key));

        logger.info(key + " sayfa üzerinde bulundu");
    }


    @Step({"<length> uzunlugunda random bir kelime üret ve <saveKey> olarak sakla"})
    public void createRandomNumber(int length, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey, new RandomString(length).nextString());
    }

    @Step("Geri butonuna bas")
    public void clickBybackButton() {
        if (!localAndroid) {
            backPage();
        } else {
            ((AndroidDriver) appiumDriver).pressKey(new KeyEvent().withKey(AndroidKey.BACK));
        }

    }

    @Step("Tamam butonuna tıkla")
    public void clickOKButton() {

        newWebDriverWait(30, 1000);

        MobileElement me = (MobileElement) appiumFluentWait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.ttech.android.onlineislem:id/btn_confirm")));
        me.click();
    }


    @Step("Uygulamada login kontrolünü yap")
    public void LoginControl() throws InterruptedException {
        waitBySecond(3);
        if (isElementPresent(By.xpath("//*[@text='Moneye ile konuşuyorsun']"))) {
            clickBybackButton();
            waitBySecond(2);
            appiumDriver.findElement(By.id("co.moneye.android:id/btnProfile")).click();
            waitBySecond(1);
            MiddlePageswipe(17);
            appiumDriver.findElement(By.id("co.moneye.android:id/btnSignOut")).click();
            waitBySecond(1);
            logger.info("Başarılı bir şekilde logout olundu");
            waitBySecond(3);
        } else {
            waitBySecond(3);
            logger.info("Sisteme login olma işlemi başladı");
            waitBySecond(1);
        }
    }

    @Step("Anasayfa widget kontrolü")
    public void maninPagewidgetControl() throws InterruptedException {
        waitBySecond(3);
        if (isElementPresent(By.id("com.ttech.android.onlineislem:id/image_selocan_right"))) {

            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_myaccount")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_shop")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_support")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/imageViewProfilIcon")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/linearLayoutNotificationsn")).click();

        } else {
            logger.info("Widget görülmedi !!!!!");
        }
    }

    @Step("Pup-up Kontrolü yap")
    public void PopUpControl() throws InterruptedException {
        waitBySecond(2);
        if (isElementPresent(By.id("com.bordatech.lighthouse.uat:id/layout_v3_borda_dialog_message_text"))) {
            appiumDriver.findElement(By.id("com.bordatech.lighthouse.uat:id/layout_v3_borda_dialog_positive_button")).click();
            waitBySecond(2);
            logger.info("evet butonuna tıklando");
        } else {
            logger.info("Pop-up görünür olmadı");
        }
    }
    @Step({"<key> li elementin değeri <key> e eşitliğini kontrol et bakalım Aykut",
            "Find element by <key> and text equals <text> bakalim"})
    public void equalsTexts(String key, String key2) {
        String elementText = findElementByKey(key).getText().trim();
        String elementText2 = findElementByKey(key2).getText().trim();
        Assert.assertEquals("Değerler eşleşmiyor.", elementText, elementText2);
        logger.info(" elementinin text değerinin eşit olduğu kontrol edildi");
    }


    @Step("Yeni şifre <text> ve yeni şifre tekrar <textrepeat>  alanlarına tex değerlerini yaz")
    public void writeNewPassword(String text, String textrepeat) {

        newWebDriverWait(30, 1000);
        objectTextandClick(By.id("com.ttech.android.onlineislem:id/editTextPassword"), text);
        newWebDriverWait(20, 1000);
        clickBybackButton();
        newWebDriverWait(30, 1000);
        objectTextandClick(By.id("com.ttech.android.onlineislem:id/editTextPasswordRepeat"), textrepeat);
        clickBybackButton();

    }


    @Step("<StartX>,<StartY> oranlarından <EndX>,<EndY> oranlarına <times> kere swipe et")
    public void pointToPointSwipe(int startX, int startY, int endX, int endY, int count) {
        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        startX = (width * startX) / 100;
        startY = (height * startY) / 100;
        endX = (width * endX) / 100;
        endY = (height * endY) / 100;


        for (int i = 0; i < count; i++) {
            //appiumDriver.swipe(startX, startY, endX, endY, 1000);
            TouchAction action = new TouchAction(appiumDriver);
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(endX, endY))
                    .release().perform();
        }
    }

    @Step({"uygulamayı yeniden başlat", "Restart the application"})
    public void restart() throws InterruptedException {


        appiumDriver.closeApp();
        appiumDriver.launchApp();
        logger.info("uygulama yeniden başlatıldı");
        waitBySecond(5);
        //existClickByKey("ızınVer");

    }


    @Step("Galeriden fotograf seç")
    public void selectPhotoFromGallery() throws InterruptedException {

        String deviceName = getCapability("deviceName");

        System.out.println("----- CİHAZDANDAN GELEN DEVİCE NAME: (" + deviceName + ")-------");
        if (deviceName.contains("CB512EHLX6")) {
            logger.info("SONY telefonu galerisine girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("0f810c2a")) {
            logger.info("Samsung J5 telefonu galerisine girdi");
            waitBySecond(5);
            clickByKey("samsungGallery");
            waitBySecond(5);
            clickByKey("samsungJ5Photo");
        } else if (deviceName.contains("9889db324d434f4637")) {
            logger.info("Samsung S8 galerisine girdi");
            // existClickByKey("samsungChoose");
            //existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("5200fbe7f4688403")) {
            logger.info("Samsung A5 telefonu galerisine girdi");
            // existClickByKey("samsungChoose");
            // existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            existClickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("ad051602404b854bca")) {
            logger.info("Samsung S7 Edge telefonu galerisine girdi");
            //existClickByKey("samsungChoose");
            //existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");

        } else if (deviceName.contains("ce03171382675e0f01")) {
            logger.info("Galaxy s8 (7.0) telefonu galerisine girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            existClickByKey("samsungChoose");
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else {
            logger.info("iphone galeriye girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("ıphoneGaleri");
            existClickByKey("ızınVer");
            clickByKey("ıphoneGaleriPhotoChoose");
        }
    }

    @Step("pop-up izin ver")
    public void closePopupAndCookie() throws InterruptedException {
        waitBySecond(2);

        contextText("NATIVE_APP");

        new WebDriverWait(appiumDriver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='İZİN VER']")));

        if (isElementPresent(By.xpath("//*[@text='İZİN VER']"))
        ) {
            logger.info("Notification pop-up kapatıldı !!!!!!");
            waitBySecond(2);
            contextText("NATIVE_APP");
            findElementWithoutAssert(By.xpath("//*[@text='İZİN VER']")).click();


        } else {

            logger.info("Pop-up görülmedi");

        }


    }


    @Step("5S için <key> elementini bulana kadar yukarı kaydır")
    public void swipeFor5S(String element) {

        logger.info(" // BROWSERNAME: " + getCapability("deviceName") + " //");


        if (getCapability("deviceName").contains("5S")) {
            logger.info("5S için if koşulları gerçekleştiriliyor...");
            int maxRetryCount = 10;
            while (maxRetryCount > 0) {
                List<MobileElement> elements = findElemenstByKey(element);
                if (elements.size() > 0) {
                    if (!elements.get(0).isDisplayed()) {
                        maxRetryCount--;
                        swipeUpAccordingToPhoneSize();
                    } else {
                        elements.get(0).click();
                        logger.info(element + " elementine tıklandı");
                        break;
                    }
                } else {
                    maxRetryCount--;
                    swipeUpAccordingToPhoneSize();
                }
            }
        }

    }

    @Step("deneme checkbox")
    public void denemeCheckbox() {
        if (appiumDriver instanceof AndroidDriver) {

            contextText("WEBVIEW");
            findElementsWithoutAssert(By.cssSelector("label>i")).get(0).click();
            contextText("NATIVE_APP");

        } else {
            findElementWithoutAssert(By.xpath("//XCUIElementTypeStaticText[@name='okudum kabul ediyorum.']")).click();

        }

    }


    @Step("Debug step")
    public void debugStep() {

        logger.info("debug");
    }

    private void newWebDriverWait(int sec, int mil) {
        new WebDriverWait(appiumDriver, sec, mil);
    }


    private void backPage() {
        appiumDriver.navigate().back();
    }


    private void objectTextandClick(By by, String text) {
        MobileElement me = (MobileElement) appiumFluentWait.until(ExpectedConditions.presenceOfElementLocated(by));
        me.click();
        me.setValue(text);

    }

    private void contextText(String text) {

        appiumDriver.context(appiumDriver.getContextHandles().stream().filter(s -> s.contains(text)).findFirst().get());

    }

    private String getCapability(String text) {
        return appiumDriver.getCapabilities().getCapability(text).toString();

    }

    @Step("<key> li elementin yüklenmesini <time> saniye bekle")
    public boolean doesElementExistByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }

    }

    public boolean doesElementClickableByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.elementToBeClickable(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }

    }


    @Step("<key>,<startPointX>,<finishPointX> kaydır baakalım")
    public void sliderSwipe(String key, int startPointX, int finishPointX) {

        int coordinateX = appiumDriver.manage().window().getSize().width;
        int pointY = findElementByKey(key).getCenter().y;
        int firstPointX = (coordinateX * startPointX) / 100;
        int lastPointX = (coordinateX * finishPointX) / 100;

        TouchAction action = new TouchAction(appiumDriver);
        action
                .press(PointOption.point(firstPointX, pointY))
                .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                .moveTo(PointOption.point(lastPointX, pointY))
                .release().perform();

    }

    @Step("Verilen <x> <y> koordinatına tıkla")
    public void tapElementWithCoordinate(int x, int y) {
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.tap(PointOption.point(x, y)).perform();
    }

    @Step("<key> li elementin  merkezine tıkla ")
    public void tapElementWithKey(String key) {

        Point point = findElementByKey(key).getCenter();
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.tap(PointOption.point(point.x, point.y)).perform();
    }

    @Step("<key> li element varsa  <x> <y> koordinatına tıkla ")
    public void tapElementWithKeyCoordinate(String key, int x, int y) {

        logger.info("element varsa verilen koordinata tıkla ya girdi");
        MobileElement mobileElement;

        mobileElement = findElementByKeyWithoutAssert(key);

        if (mobileElement != null) {

            Point point = mobileElement.getLocation();
            logger.info(point.x + "  " + point.y);
            Dimension dimension = mobileElement.getSize();
            logger.info(dimension.width + "  " + dimension.height);
            TouchAction a2 = new TouchAction(appiumDriver);
            a2.tap(PointOption.point(point.x + (dimension.width * x) / 100, point.y + (dimension.height * y) / 100)).perform();
        }
    }

    @Step("<key> li elementin  merkezine  press ile çift tıkla ")
    public void pressElementWithKey(String key) {

        Point point = findElementByKey(key).getCenter();
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.press(PointOption.point(point.x, point.y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                .press(PointOption.point(point.x, point.y)).release().perform();

    }


    @Step("<key> li elementin  merkezine double tıkla ")
    public void pressElementWithKey2(String key) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(findElementByKey(key));
        actions.doubleClick();
        actions.perform();
        appiumDriver.getKeyboard();

    }

    @Step("<key> li elementi rasgele sec")
    public void chooseRandomProduct(String key) {

        List<MobileElement> productList = new ArrayList<>();
        List<MobileElement> elements = findElemenstByKey(key);
        int elementsSize = elements.size();
        int height = appiumDriver.manage().window().getSize().height;
        for (int i = 0; i < elementsSize; i++) {
            MobileElement element = elements.get(i);
            int y = element.getCenter().getY();
            if (y > 0 && y < (height - 100)) {
                productList.add(element);
            }
        }
        Random random = new Random();
        int randomNumber = random.nextInt(productList.size());
        productList.get(randomNumber).click();
    }


    @Step("<key> li elemente kadar <text> textine sahip değilse ve <timeout> saniyede bulamazsa swipe yappp")
    public void swipeAndFindwithKey(String key, String text, int timeout) {


        MobileElement sktYil1 = null;
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        WebDriverWait wait = new WebDriverWait(appiumDriver, timeout);
        int count = 0;
        while (true) {
            count++;
            try {
                sktYil1 = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
                if (text.equals("") || sktYil1.getText().trim().equals(text)) {
                    break;
                }
            } catch (Exception e) {
                logger.info("Bulamadı");

            }
            if (count == 8) {

                Assert.fail("Element bulunamadı");
            }

            Dimension dimension = appiumDriver.manage().window().getSize();
            int startX1 = dimension.width / 2;
            int startY1 = (dimension.height * 75) / 100;
            int secondX1 = dimension.width / 2;
            int secondY1 = (dimension.height * 30) / 100;

            TouchAction action2 = new TouchAction(appiumDriver);

            action2
                    .press(PointOption.point(startX1, startY1))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(secondX1, secondY1))
                    .release()
                    .perform();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Step("<key>li elementi bulana kadar <limit> kere swipe yap ve elementi bul")
    public void swipeKeyy(String key, int limit) throws InterruptedException {


        boolean isAppear = false;

        int windowHeight = this.getScreenHeight();
        for (int i = 0; i < limit; ++i) {
            try {

                Point elementLocation = findElementByKeyWithoutAssert(key).getLocation();
                logger.info(elementLocation.x + "  " + elementLocation.y);
                Dimension elementDimension = findElementByKeyWithoutAssert(key).getSize();
                logger.info(elementDimension.width + "  " + elementDimension.height);
                // logger.info(appiumDriver.getPageSource());
                if (elementDimension.height > 20) {
                    isAppear = true;
                    logger.info("aranan elementi buldu");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Element ekranda görülmedi. Tekrar swipe ediliyor");
            }
            System.out.println("Element ekranda görülmedi. Tekrar swipe ediliyor");

            swipeUpAccordingToPhoneSize();
            waitBySecond(1);
        }

    }

    @Step("Urun stokta var mi kontrol et")
    public void stokKontrol() throws Exception {

        logger.info("Stok kontrolüne girdi");
        MobileElement element;
        element = findElementByKeyWithoutAssert("haberdarEt");
        waitBySecond(5);
        if (element != null) {
            clickBybackButton();
            waitBySecond(2);
            swipeUpAccordingToPhoneSize();
            waitBySecond(5);
            clickByKey("hemenAlButonu");

        }
        logger.info("ürün stokta bulunuyor");

    }

    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomeMail(String key) {
        Long timestamp = getTimestamp();

        MobileElement webElement = findElementByKey(key);
        webElement.clear();
        webElement.setValue("testotomasyon" + timestamp + "@sahabt.com");
    }


    @Step("Dropdown dan key <key> alanını seç")
    public void selectDropdawnoNKey(String key) {
        appiumDriver.findElement(By.className("XCUIElementTypePickerWheel")).sendKeys(key);
    }


    @Step({"<key> li elementi bul basılı tut"})
    public void basiliTut(String key) {
        MobileElement mobileElement = findElementByKey(key);

        TouchAction action = new TouchAction<>(appiumDriver);
        action
                .longPress(LongPressOptions.longPressOptions()
                        .withElement(ElementOption.element(mobileElement)).withDuration(Duration.ofSeconds(4)))
                .release()
                .perform();

    }

    @Step({"<key> li elementi bul <times> kere tıkla"})
    public void bulTikla(String key, int times) {
        MobileElement mobileElement = findElementByKey(key);
        for (int i = 0; i < times; i++) {

            mobileElement.click();
        }
    }

    @Step({"<key> li elementi bul ve <times> kere sil"})
    public void clickAndDelete(String key, int times) {
        MobileElement mobileElement = findElementByKey(key);
        for (int i = 0; i < times; i++) {
            mobileElement.sendKeys(Keys.BACK_SPACE);
        }
    }

    @Step("Click element by Sign_in_btn")
    public void ClicSiginButton() {
        appiumDriver.findElementByAccessibilityId("Sign In").click();

    }

    @Step("Click element by Tick_Symbol_Btn")
    public void ClicTick_Symbol_Btn() {
        appiumDriver.findElementByAccessibilityId("Finish").click();

    }

    @Step("İd li <id> elemetin <index> index değerini al ve <key> değerini yaz")
    public void SetObjectByindex(String id, int index, String key) {
        appiumDriver.findElements(By.id(id)).get(index).setValue(key);

    }

    @Step("İd li <id> elemetin <index> index değerini al ve elemente tıkla")
    public void ClickObjectByindex(String id, int index) {
        appiumDriver.findElements(By.id(id)).get(index).click();

    }

    @Step("<key> li elementin <attributeName> attribute değerinin <value> e eşit olduğunu kontrol et ")
    public void equalsAttributeValue(String key, String attributeName, String value) {
        Assert.assertEquals(value, getAttribute(key, attributeName));
        logger.info(key + " li elementin " + attributeName + " attribute değerinin " + value + " e eşit olduğu kontrol edildi");
    }

    String getAttribute(String key, String attributeName) {
        return findElementByKey(key).getAttribute(attributeName);
    }

    @Step("Verify toast message <text>")
    public void verifyToast(String text) {
        String name = appiumDriver.findElement(By.xpath("//android.widget.Toast[1]")).getAttribute("name");
        System.out.println(name);
        if (name.equals(text))
            logger.info("Tost mesaj doğrulandı.");
        else
            Assert.fail("Toast mesaj doğrulanamadı.");
    }

    @Step("<key> li element sayfada bulunuyor mu ve disabled mı")
    public void isExistAndDisabled(String key) {
        boolean notTrue = !findElementByKey(key).isEnabled();
        Assert.assertTrue("Element disabled değil.", notTrue);

        logger.info(key + " elementin disabled olduğu doğrulandı.");
    }

    @Step("<key> li elemente <text> text değeri yazılabilir mi")
    public boolean isTextBoxEditable(String key, String text) {
        MobileElement element = findElementByKey(key);
        element.setValue(text);
        element.clear();
        return true;
    }

    @Step("Press ESC for android")
    public void pressEscKeyForAndroid() {
        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        ((AndroidDriver<MobileElement>) appiumDriver).pressKey(new KeyEvent().withKey(AndroidKey.ESCAPE));
    }

    @Step("Press ENTER for android")
    public void pressEnterKeyForAndroid() {
        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        ((AndroidDriver<MobileElement>) appiumDriver).pressKey(new KeyEvent().withKey(AndroidKey.ENTER));
    }

    @Step("Press TAB for android")
    public void pressTabKeyForAndroid() {
        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        ((AndroidDriver<MobileElement>) appiumDriver).pressKey(new KeyEvent().withKey(AndroidKey.TAB));
    }

    @Step({"<key> li elementi bul ve tıkla, sonra klavyeyi kapat", "Click element by <key> then close keyboard"})
    public void clickByKeyAndThenCloseKeyboard(String key) {
        doesElementExistByKey(key, 5);
        doesElementClickableByKey(key, 5);
        //       logger.info("appiumDriver.getPageSource().contains(key)");
        findElementByKey(key).click();
//        MobileElement me = findElementByKey(key);
//        tapElementWithCoordinate(me.getLocation().x, me.getLocation().y);
        logger.info(key + " elementine tıklandı");

        closeKeyboard();
    }


    @Step({"Check if element <key> not exists",
            "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        int loopCount = 0;
        while (loopCount < 70) {
            if (appiumDriver.findElements(selectorInfo.getBy()).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
        }
        Assert.fail("Element '" + key + "' still exist.");
    }

    @Step("Close Keyboard")
    public void closeKeyboard() {
        appiumDriver.hideKeyboard();
    }

    public void swipeDownAccordingToPhoneSizeFromSpecificPosition() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (int) (height * 0.35);
            int swipeEndHeight = (int) (height * 0.8);
            System.out.println("Start width: " + swipeStartWidth + " - Start height: " + swipeStartHeight + " - End height: " + swipeEndHeight);
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (int) (height * 0.35);
            int swipeEndHeight = (int) (height * 0.8);
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementi bulana kadar aşağı swipe et ve tıkla",
            "Find element by <key> text equals <text> swipe down and click"})
    public void clickByKeyWithSwipeDown(String key, String text) throws InterruptedException {
        boolean find = false;
        int maxRetryCount = 10;
        while (!find && maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            for (MobileElement element : elements) {
                if (element.getText().contains(text)) {
                    element.click();
                    find = true;
                    break;
                }
            }
            if (!find) {
                maxRetryCount--;
                swipeDownAccordingToPhoneSizeFromSpecificPosition();
                waitBySecond(1);
            }
        }
    }

    @Step("<key> li elementi bulana kadara yukarı swipe et")
    public void swipeUntilToFindElement(String key) {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            MobileElement element = findElementByKeyWithoutAssert(key);
            if (element != null) {
                break;
            }
            maxRetryCount--;
            swipeUpAccordingToPhoneSize();
            waitBySecond(1);
        }
    }

    @Step({"Check if element <key> exists",
            "Wait for element to load with key <key>",
            "Element var mı kontrol et <key>",
            "Elementin yüklenmesini bekle <key>"})
    public MobileElement getElementWithKeyIfExists(String key) throws InterruptedException {
        MobileElement element;
        try {
            element = findElementByKey(key);
            logger.info(key + " key'li element bulundu.");
        } catch (Exception ex) {
            Assert.fail("Element: '" + key + "' doesn't exist.");
            return null;
        }
        return element;
    }


    @Step("Change context to <key>")
    public void setContext(String key) {
        waitBySecond(10);
        appiumDriver.context(key);
        logger.info("Context " + key + " olarak değiştirildi.");
    }


    public int getRandomIntegerValue(int size) {
        Random rand = new Random();
        int randomValue = rand.nextInt(size);
        logger.info("Random integer value: " + randomValue);
        return randomValue;
    }

    public int getRandomIntegerValue(String key) {
        Random rand = new Random();
        int randomValue = rand.nextInt(findElemenstByKey(key).size());
        logger.info("Random integer value: " + randomValue);
        return randomValue;
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void swipeDownFromMiddleOfPage() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println("Android1 : " + width + " - " + height);
            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = height / 2;
            int swipeEndHeight = swipeStartHeight + (swipeStartHeight * 60) / 100;
            System.out.println("Start width: " + swipeStartWidth + " - Start height: " + swipeStartHeight + " - End height: " + swipeEndHeight);
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = height / 2;
            int swipeEndHeight = swipeStartHeight + (height * 40) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }

    }

    @Step({"Sayfanın ortasından itibaren <times> kere aşağıya kaydır", "Swipe times <times> from middle of the page"})
    public void swipeDownFromMiddleOfPageBy(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeDownFromMiddleOfPage();
            waitBySecond(2);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }
    }

    public Integer parseStringValueToFormattedInteger(String key) {

        int loopCount = 0;
        while (getElementText(key).equals("")) { // wait element's load until 10 seconds
            waitByMilliSeconds(1000);
            if (loopCount == 10)
                break;
            loopCount++;
        }
        String value = getElementText(key).trim();
        if (value.contains("₺")) {
            String[] productPrice = value.split("₺");
            value = productPrice[0].trim();
        }
        if (value.contains(",")) {
            value = value.replace(",", "");

        }
        if (value.contains(".")) {
            value = value.replace(".", "");

        }
        logger.info(key + " elementinin text değeri double formatına dönüştürüldü.");
        return Integer.parseInt(value);
    }

    public String getElementText(String key) {
        return findElementByKey(key).getText().trim();
    }

    public String convertIntegerToStringValue(int key) {
        String value = new Integer(key).toString();
        return value;
    }

    @Step({"Check if element <expectedText> contains text <expectedText>",
            "<key> elementi <expectedText> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {
        //Boolean containsText = findElementByKey(key).getText().contains(expectedText);
        //Assert.assertEquals(findElementByKey(key).getText().trim(), expectedText.trim());
        logger.info("Expected: " + expectedText);
        logger.info("Screen : " + findElementByKey(key).getText());
        Assert.assertFalse("Expected text is not contained", findElementByKey(key).getText().contains(expectedText));
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElementByKey(key).getAttribute(attribute);
    }

    @Step({"Check if element <key> equals text <expectedText>",
            "<key> elementi <text> değerini eşit mi kontrol et"})
    public void checkElementEqualsText(String key, String expectedText) {
        String elementText = findElementByKey(key).getText().trim();
        Assert.assertEquals("Değerler eşleşmiyor.", elementText, expectedText);
        logger.info(key + "'li elementin değerinin " + expectedText + " olduğu doğrulandı.");
    }

    @Step({"Check if element <key> is not empty"})
    public void checkElementIsNotEmpty(String key) {
        String text = findElementByKey(key).getText();
        Assert.assertTrue(key + " elementinin dolu olması bekleniyordu fakat boş geldi!", text.length() > 0);
        logger.info(key + "elementi boş değil '" + text + "' textini içeriyor");
    }

}


