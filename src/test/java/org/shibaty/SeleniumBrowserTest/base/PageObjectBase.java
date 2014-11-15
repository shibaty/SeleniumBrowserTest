
package org.shibaty.SeleniumBrowserTest.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * PageObjectのベースクラス.
 *
 */
public abstract class PageObjectBase {

  /**
   * wait for Preview(Second).
   */
  protected static final int WAIT_PREVIEW_SECOND = 5;

  /**
   * WebDriver.
   */
  protected final WebDriver _driver;

  /**
   * URI. 派生クラスのコンストラクタで定義すること
   */
  protected String uri;

  /**
   * コンストラクタ.<br>
   *
   * @param driver WebDriver
   */
  public PageObjectBase(WebDriver driver) {
    _driver = driver;
  }

  /**
   * タイトルを取得.<br>
   *
   * @return タイトル
   */
  public String getTitle() {
    return _driver.getTitle();
  }

  /**
   * ページを開く.<br>
   *
   * @return PageObject
   */
  public <T extends PageObjectBase> T open() {
    _driver.get(uri);
    return initElements();
  }

  /**
   * Elementを初期化.<br>
   *
   * @return PageObject
   */
  @SuppressWarnings("unchecked")
  public <T extends PageObjectBase> T initElements() {
    return (T) PageFactory.initElements(_driver, this.getClass());
  }

  /**
   * 「戻る」を行う.<br>
   * 派生クラスにて返却するPageObjectを定義すること
   *
   * @return PageObject
   */
  public <T extends PageObjectBase> T back() {
    _driver.navigate().back();
    return null;
  }

  /**
   * 「進む」を行う.<br>
   * 派生クラスにて返却するPageObjectを定義すること
   *
   * @return PageObject
   */
  public <T extends PageObjectBase> T forward() {
    _driver.navigate().forward();
    return null;
  }

  /**
   * 「更新」を行う.<br>
   *
   * @return PageObject
   */
  public <T extends PageObjectBase> T refresh() {
    _driver.navigate().refresh();
    return initElements();
  }

}
