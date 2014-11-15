
package org.shibaty.SeleniumBrowserTest.example.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
  private WebElement _weSearch;

  /**
   * コンストラクタ.<br>
   *
   * @param driver
   */
  public POGoogle(WebDriver driver) {
    super(driver);
    uri = "http://www.google.co.jp";
  }

  /**
   * 検索.<br>
   *
   * @param query 検索文字列
   * @return 検索結果ページ(PageObject)
   */
  public POGoogleResult search(String query) {
    _weSearch.sendKeys(query + "\n");

    (new WebDriverWait(_driver, WAIT_PREVIEW_SECOND)).until(
        ExpectedConditions.titleContains(query));

    return new POGoogleResult(_driver).initElements();
  }
}
