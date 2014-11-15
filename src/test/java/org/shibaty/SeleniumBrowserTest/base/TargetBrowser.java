
package org.shibaty.SeleniumBrowserTest.base;

/**
 * 試験対象ブラウザ定数定義.<br>
 */
public enum TargetBrowser {
  /** Internet Explorer. */
  IE,
  /** Mozilla Firefox. */
  FIREFOX,
  /** Google Chrome. */
  CHROME,
  /** Google Chrome for Android. */
  ANDROID_CHROME,
  ;

  private static final String STRING_IE = "ie";
  private static final String STRING_FIREFOX = "firefox";
  private static final String STRING_CHROME = "chrome";
  private static final String STRING_ANDROID_CHROME = "android_chrome";

  /**
   * 文字列からターゲットを判別.<br>
   *
   * @param targetString ターゲット文字列
   * @return target
   */
  public static TargetBrowser fromString(String targetString) {
    targetString = targetString.toLowerCase();
    TargetBrowser target;
    switch (targetString) {
    case STRING_IE:
      target = IE;
      break;
    case STRING_FIREFOX:
      target = FIREFOX;
      break;
    case STRING_CHROME:
      target = CHROME;
      break;
    case STRING_ANDROID_CHROME:
      target = ANDROID_CHROME;
      break;
    default:
      // default is firefox
      target = FIREFOX;
      break;
    }
    return target;
  }
}
