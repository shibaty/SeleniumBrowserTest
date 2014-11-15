
package org.shibaty.SeleniumBrowserTest.example.pageobject;

import org.openqa.selenium.WebDriver;
import org.shibaty.SeleniumBrowserTest.base.AbstractPageObject;

/**
 * Google検索結果ページ.
 */
public class POGoogleResult extends AbstractPageObject {

  /**
   * コンストラクタ.<br>
   *
   * @param driver WebDriver
   */
  public POGoogleResult(WebDriver driver) {
    super(driver, null);
  }

  /**
   * {@inheritDoc}
   *
   * @see org.shibaty.SeleniumBrowserTest.base.AbstractPageObject#back()
   */
  @Override
  public <T extends AbstractPageObject> T back() {
    super.back();
    return new POGoogle(driver).initElements();
  }

}
