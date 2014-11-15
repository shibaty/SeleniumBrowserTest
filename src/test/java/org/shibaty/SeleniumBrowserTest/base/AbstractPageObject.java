
package org.shibaty.SeleniumBrowserTest.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.shibaty.SeleniumBrowserTest.base.utils.Settings;

/**
 * PageObjectのベースクラス.
 */
public abstract class AbstractPageObject {

  /**
   * WebDriver.
   */
  protected final WebDriver driver;

  /**
   * URI. 派生クラスのコンストラクタで定義すること
   */
  protected String uri;

  /**
   * コンストラクタ.<br>
   *
   * @param driver WebDriver
   * @param uri 対象ページのURI
   */
  public AbstractPageObject(WebDriver driver, String uri) {
    this.driver = driver;
    this.uri = uri;
  }

  /**
   * タイトルを取得.<br>
   *
   * @return タイトル
   */
  public String getTitle() {
    return driver.getTitle();
  }

  /**
   * ページを開く.<br>
   *
   * @param <T> PageObjectの派生クラス
   * @return PageObject
   */
  public <T extends AbstractPageObject> T open() {
    driver.get(uri);
    return initElements();
  }

  /**
   * Elementを初期化.<br>
   *
   * @param <T> PageObjectの派生クラス
   * @return PageObject
   */
  @SuppressWarnings("unchecked")
  public <T extends AbstractPageObject> T initElements() {
    return (T) PageFactory.initElements(driver, this.getClass());
  }

  /**
   * 「戻る」を行う.<br>
   * 派生クラスにて返却するPageObjectを定義すること
   *
   * @param <T> PageObjectの派生クラス
   * @return PageObject
   */
  public <T extends AbstractPageObject> T back() {
    driver.navigate().back();
    return null;
  }

  /**
   * 「進む」を行う.<br>
   * 派生クラスにて返却するPageObjectを定義すること
   *
   * @param <T> PageObjectの派生クラス
   * @return PageObject
   */
  public <T extends AbstractPageObject> T forward() {
    driver.navigate().forward();
    return null;
  }

  /**
   * 「更新」を行う.<br>
   *
   * @param <T> PageObjectの派生クラス
   * @return PageObject
   */
  public <T extends AbstractPageObject> T refresh() {
    driver.navigate().refresh();
    return initElements();
  }

  /**
   * 表示待ちを行う.<br>
   *
   * @return WebDriverWait
   */
  public WebDriverWait waitPreview() {
    return new WebDriverWait(driver, Settings.getInstance().getTestWindowPreviewWait());
  }
}
