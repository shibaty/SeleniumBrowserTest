
package org.shibaty.SeleniumBrowserTest.example.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.shibaty.SeleniumBrowserTest.base.PageObjectBase;

/**
 * Google トップページ.
 */
public class POGoogle extends PageObjectBase {

  /**
   * SearchBox.
   */
  @FindBy(name = "q")
  @CacheLookup
  private WebElement weSearch;

  /**
   * コンストラクタ.<br>
   *
   * @param driver
   */
  public POGoogle(WebDriver driver) {
    super(driver, "http://www.google.co.jp");
  }

  /**
   * 検索.<br>
   *
   * @param query 検索文字列
   * @return 検索結果ページ(PageObject)
   */
  public POGoogleResult search(String query) {
    weSearch.sendKeys(query + "\n");

    waitPreview().until(
        ExpectedConditions.titleContains(query));

    return new POGoogleResult(driver).initElements();
  }
}
