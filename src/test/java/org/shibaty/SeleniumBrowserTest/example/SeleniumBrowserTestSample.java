
package org.shibaty.SeleniumBrowserTest.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.shibaty.SeleniumBrowserTest.base.SeleniumBrowserTestBase;
import org.shibaty.SeleniumBrowserTest.base.TargetBrowser;
import org.shibaty.SeleniumBrowserTest.example.pageobject.POGoogle;
import org.shibaty.SeleniumBrowserTest.example.pageobject.POGoogleResult;

/**
 * サンプルテストケース.<br>
 *
 * @author Yasutaka
 */
public class SeleniumBrowserTestSample extends SeleniumBrowserTestBase {

    /**
     * コンストラクタ.<br>
     */
    public SeleniumBrowserTestSample() {
        super(TargetBrowser.CHROME);
    }

    /**
     * テストケース.<br>
     * Ex.)Googleで任意の文字列を検索して表示
     */
    @Test
    public void testOne() {
        WebDriver driver = getDriver();
        POGoogle poGoogle = new POGoogle(driver).open();

        POGoogleResult poGoogleResult = poGoogle.search("webdriver");

        saveScreenshot(createScreenshotFilename());
        assertThat(poGoogleResult.getTitle(), containsString("webdriver"));
    }

    /**
     * テストケース.<br>
     * Ex.)Googleで任意の文字列を検索したあと戻る
     */
    @Test
    public void testTwo() {
        WebDriver driver = getDriver();
        POGoogle poGoogle = new POGoogle(driver).open();

        POGoogleResult poGoogleResult = poGoogle.search("webdriver");

        poGoogle = poGoogleResult.back();

        saveScreenshot(createScreenshotFilename());
        assertThat(poGoogle.getTitle(), containsString("新しいタブ"));
    }

}
