
package org.shibaty.SeleniumBrowserTest.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.shibaty.SeleniumBrowserTest.base.AbstractSeleniumBrowserTest;
import org.shibaty.SeleniumBrowserTest.base.utils.DataFactory;
import org.shibaty.SeleniumBrowserTest.example.pageobject.POGoogle;
import org.shibaty.SeleniumBrowserTest.example.pageobject.POGoogleResult;

/**
 * サンプルテストケース.<br>
 */
public class GoogleTest extends AbstractSeleniumBrowserTest {

  /**
   * テストケース.<br>
   * Ex.)Googleで任意の文字列を検索して表示
   */
  @Test
  public void testOne() {
    WebDriver driver = getDriver();

    // テストデータの取得
    try {
      List<Map<String, String>> testDataList = DataFactory
          .getTestData("example.xlsx");

      String value = testDataList.get(0).get("test_data1");

      // Googleトップページを開いて検索
      POGoogle poGoogle = new POGoogle(driver).open();
      POGoogleResult poGoogleResult = poGoogle.search(value);

      saveScreenshot(createScreenshotFilename());
      assertThat(poGoogleResult.getTitle(), containsString(value));
    } catch (InvalidFormatException | IOException e) {
      e.printStackTrace();
      fail();
    }
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
