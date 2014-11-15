
package org.shibaty.SeleniumBrowserTest.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.Dimension;

/**
 * 設定保持クラス.<br>
 */
public class Settings {

  /**
   * 自インスタンス(Singleton).
   */
  private static final Settings instance = new Settings();

  /**
   * properties ファイル名.
   */
  private static final String PROPERTIES_FILENAME = "settings.properties";

  private static final String KEY_HUB_URI = "hub.uri";
  private static final String KEY_APPIUM_VERSION = "appium.version";
  private static final String KEY_APPIUM_ANDROID_VERSION = "appium.android.version";
  private static final String KEY_TEST_TARGET = "test.target";
  private static final String KEY_TEST_WINDOW_SIZE_WIDTH = "test.window.size.width";
  private static final String KEY_TEST_WINDOW_SIZE_HEIGHT = "test.window.size.height";
  private static final String KEY_TEST_WINDOW_SCROLL_WAIT = "test.window.scroll.wait";
  private static final String KEY_TEST_WINDOW_PREVIEW_WAIT = "test.window.preview.wait";

  /**
   * Properties.
   */
  private Properties prop = new Properties();

  /**
   * コンストラクタ.<br>
   */
  private Settings() {
    try (InputStream is = Settings.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
      prop.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * インスタンス取得.<br>
   *
   * @return 自インスタンス
   */
  public static Settings getInstance() {
    return instance;
  }

  public String getHubUri() {
    return prop.getProperty(KEY_HUB_URI);
  }

  public String getAppiumVersion() {
    return prop.getProperty(KEY_APPIUM_VERSION);
  }

  public String getAppiumAndroidVersion() {
    return prop.getProperty(KEY_APPIUM_ANDROID_VERSION);
  }

  public String getTestTarget() {
    return prop.getProperty(KEY_TEST_TARGET);
  }

  public Dimension getTestWindowSize() {
    int width = Integer.parseInt(prop.getProperty(KEY_TEST_WINDOW_SIZE_WIDTH));
    int height = Integer.parseInt(prop.getProperty(KEY_TEST_WINDOW_SIZE_HEIGHT));

    return new Dimension(width, height);
  }

  public int getTestWindowScrollWait() {
    return Integer.parseInt(prop.getProperty(KEY_TEST_WINDOW_SCROLL_WAIT));
  }

  public int getTestWindowPreviewWait() {
    return Integer.parseInt(prop.getProperty(KEY_TEST_WINDOW_PREVIEW_WAIT));
  }
}
