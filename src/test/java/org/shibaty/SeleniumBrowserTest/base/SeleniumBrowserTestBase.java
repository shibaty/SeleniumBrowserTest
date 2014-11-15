
package org.shibaty.SeleniumBrowserTest.base;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Seleniumによるブラウザテストのベースクラス.<br>
 */
public class SeleniumBrowserTestBase {

  /**
   * Appium Version.
   */
  private static final String APPIUM_VERSION = "1.2.4";

  /**
   * Appium RemoteWebDriver URL.
   */
  private static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";

  /**
   * Target Android Version.
   */
  private static final String TARGET_ANDROID_VERSION = "4.1";

  /**
   * wait for Scroll(MilliSecond).
   */
  private static final long WAIT_SCROLL_MILLISECOND = 300;

  /**
   * Windows Size.
   */
  private static final Dimension WINDOW_SIZE = new Dimension(500, 768);

  /**
   * Web Driver.
   */
  private WebDriver _driver;

  /**
   * Target Browser for this.
   */
  private TargetBrowser _target;

  /**
   * Screenshot Directory.
   */
  private static Path _screenshotDir;

  /**
   * コンストラクタ.<br>
   *
   * @param target 試験対象ブラウザ
   */
  public SeleniumBrowserTestBase(TargetBrowser target) {
    _target = target;
  }

  /**
   * スクリーンショット保存用ディレクトリ作成.<br>
   */
  @BeforeClass
  public static void createScreenshotDir() {
    _screenshotDir = Paths.get(new SimpleDateFormat("'ss'/yyyyMMddHHmmss").format(new Date()));
    try {
      Files.createDirectories(_screenshotDir);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * システムプロパティ設定.<br>
   */
  @BeforeClass
  public static void setProperties() {
    System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
    System.setProperty("webdriver.ie.driver", "./driver/IEDriverServer.exe");
  }

  /**
   * ドライバの作成.<br>
   */
  @Before
  public void createDriver() {
    if (_target == TargetBrowser.IE) {
      // use IEDriverServer
      // IE11で実施する場合は、インターネットオプション->セキュリティの
      // インターネットと制限付きサイトの保護モードを無効化すること。
      // レジストリの変更は不要。
      DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
      capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
      capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
      capabilities.setCapability(
          InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
          true);

      _driver = new InternetExplorerDriver(capabilities);
    } else if (_target == TargetBrowser.FIREFOX) {
      // use Selenium Default Driver
      _driver = new FirefoxDriver();
    } else if (_target == TargetBrowser.CHROME) {
      // Use ChromeDriver
      _driver = new ChromeDriver();
    } else if (_target == TargetBrowser.ANDROID_CHROME
        || _target == TargetBrowser.ANDROID_BROWSER) {
      // Use Appium
      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setCapability("appium-version", APPIUM_VERSION);
      capabilities.setCapability("deviceName", "Android");
      capabilities.setCapability("platformName", "Android");
      capabilities.setCapability("platformVersion", TARGET_ANDROID_VERSION);
      if (_target == TargetBrowser.ANDROID_CHROME) {
        capabilities.setCapability("browserName", "Chrome");
      } else {
        // 標準ブラウザはうまく動かない
        capabilities.setCapability("browserName", "Browser");
      }

      try {
        _driver = new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }

    if (_target != TargetBrowser.ANDROID_CHROME && _target != TargetBrowser.ANDROID_BROWSER) {
      // excepted Android
      _driver.manage().window().setSize(WINDOW_SIZE);
    }
  }

  /**
   * ドライバの終了.<br>
   */
  @After
  public void quitDriver() {
    _driver.quit();
  }

  /**
   * WebDriverの取得.<br>
   *
   * @return WebDriver
   */
  public WebDriver getDriver() {
    return _driver;
  }

  /**
   * 呼び元の情報とページタイトルからスクリーンショットのファイル名を生成.
   *
   * @return スクリーンショットのファイル名.
   */
  protected String createScreenshotFilename() {
    StackTraceElement el = Thread.currentThread().getStackTrace()[2];
    String className = el.getClassName();
    String methodName = el.getMethodName();
    String pageTitle = _driver.getTitle();

    StringBuilder sb = new StringBuilder();
    return sb.append("ss_")
        .append(className)
        .append("_")
        .append(methodName)
        .append("_")
        .append(pageTitle)
        .append(".png")
        .toString();
  }

  /**
   * スクリーンショットの保存.<br>
   *
   * @param driver WebDriver
   * @param filename ファイル名
   */
  protected void saveScreenshot(String filename) {
    if (_driver instanceof TakesScreenshot) {
      // 実装している場合のみ実施する

      TakesScreenshot screen = (TakesScreenshot) _driver;
      Path capture = _screenshotDir.resolve(filename);
      try {
        byte[] screenshotData;
        if (_target == TargetBrowser.CHROME || _target == TargetBrowser.ANDROID_CHROME) {
          screenshotData = getScreenshotForChrome();
        } else {
          screenshotData = screen.getScreenshotAs(OutputType.BYTES);
        }

        Files.write(capture, screenshotData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("TakesScreenshot is not implemented in this webdriver.");
    }
  }

  /**
   * Chrome用にスクロールしながら画面全体のスクリーンショットを取得.<br>
   *
   * @param driver WebDriber
   * @return スクリーンショット
   */
  private byte[] getScreenshotForChrome() {
    JavascriptExecutor executor = (JavascriptExecutor) _driver;
    TakesScreenshot screen = (TakesScreenshot) _driver;

    // スクロールバーを消す
    executor.executeScript("document.body.style.overflow = 'hidden';");

    // ドキュメント全体のサイズを求める
    long totalWidth = (long) executor.executeScript("return document.body.scrollWidth;");
    long totalHeight = (long) executor.executeScript("return document.body.scrollHeight;");

    // 表示領域のサイズを求める
    long viewportWidth = (long) executor.executeScript("return window.innerWidth;");
    long viewportHeight = (long) executor.executeScript("return window.innerHeight;");

    // キャプチャ領域を計算
    List<Rectangle> rects = new ArrayList<Rectangle>();
    for (long y = 0; y < totalHeight; y += viewportHeight) {
      long newHeight = viewportHeight;
      if (y + viewportHeight > totalHeight) {
        newHeight = totalHeight - y;
      }

      for (long x = 0; x < totalWidth; x += viewportWidth) {
        long newWidth = viewportWidth;
        if (x + viewportWidth > totalWidth) {
          newWidth = totalWidth - x;
        }
        Rectangle curRect = new Rectangle(
            (int) x, (int) y, (int) newWidth, (int) newHeight);
        rects.add(curRect);
      }
    }

    // キャプチャ領域に従って、キャプチャを取得してマージ
    BufferedImage image = new BufferedImage((int) totalWidth, (int) totalHeight,
        BufferedImage.TYPE_INT_RGB);
    Rectangle prevRect = null;
    for (Rectangle rect : rects) {
      if (prevRect != null) {
        int xDiff = rect.x - prevRect.x;
        int yDiff = rect.y - prevRect.y;

        executor.executeScript("window.scrollBy(" + xDiff + "," + yDiff + ");");
        try {
          Thread.sleep(WAIT_SCROLL_MILLISECOND);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }

      byte[] buf = screen.getScreenshotAs(OutputType.BYTES);
      try {
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(buf));
        Graphics g = image.getGraphics();
        int x = 0;
        int y = 0;
        if (prevRect != null) {
          x = rect.x - (int) viewportWidth + rect.width;
          y = rect.y - (int) viewportHeight + rect.height;
        }
        g.drawImage(bufImage, x, y, (int) viewportWidth, (int) viewportHeight, null);
      } catch (IOException e) {
        e.printStackTrace();
      }
      prevRect = rect;
    }

    // byte[]に変換
    ByteArrayOutputStream baos = null;
    try {
      baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return baos.toByteArray();
  }
}
