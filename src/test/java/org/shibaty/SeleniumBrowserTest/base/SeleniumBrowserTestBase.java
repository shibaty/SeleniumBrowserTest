
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.shibaty.SeleniumBrowserTest.base.utils.Settings;

/**
 * Seleniumによるブラウザテストのベースクラス.<br>
 */
public class SeleniumBrowserTestBase {

  /**
   * Web Driver.
   */
  private WebDriver driver;

  /**
   * Target Browser for this.
   */
  private TargetBrowser target;

  /**
   * Screenshot Directory.
   */
  private static Path screenshotDir;

  /**
   * コンストラクタ.<br>
   */
  public SeleniumBrowserTestBase() {
    target = TargetBrowser.fromString(Settings.getInstance().getTestTarget());
  }

  /**
   * スクリーンショット保存用ディレクトリ作成.<br>
   */
  @BeforeClass
  public static void createScreenshotDir() {
    screenshotDir = Paths.get(new SimpleDateFormat("'ss'/yyyyMMddHHmmss").format(new Date()));
    try {
      Files.createDirectories(screenshotDir);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * ドライバの作成.<br>
   */
  @Before
  public void createDriver() {
    Settings settings = Settings.getInstance();

    DesiredCapabilities capabilities = null;
    if (target == TargetBrowser.IE) {
      // IE11で実施する場合は、インターネットオプション->セキュリティの
      // インターネットと制限付きサイトの保護モードを無効化すること。
      // レジストリの変更は不要。
      capabilities = DesiredCapabilities.internetExplorer();
      capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
      capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
      capabilities.setCapability(
          InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
          true);
    } else if (target == TargetBrowser.FIREFOX) {
      capabilities = DesiredCapabilities.firefox();
    } else if (target == TargetBrowser.CHROME) {
      capabilities = DesiredCapabilities.chrome();
    } else if (target == TargetBrowser.ANDROID_CHROME) {
      // Use Appium
      capabilities = new DesiredCapabilities();
      capabilities.setCapability("appium-version", settings.getAppiumVersion());
      capabilities.setCapability("deviceName", "Android");
      capabilities.setCapability("platformName", "Android");
      capabilities.setCapability("platformVersion", settings.getAppiumAndroidVersion());
      capabilities.setCapability("browserName", "Chrome");
    }

    try {
      driver = new RemoteWebDriver(new URL(settings.getHubUri()), capabilities);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    if (target != TargetBrowser.ANDROID_CHROME) {
      // excepted Android
      driver.manage().window().setSize(settings.getTestWindowSize());
    }
  }

  /**
   * ドライバの終了.<br>
   */
  @After
  public void quitDriver() {
    driver.quit();
  }

  /**
   * WebDriverの取得.<br>
   *
   * @return WebDriver
   */
  public WebDriver getDriver() {
    return driver;
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
    String pageTitle = driver.getTitle();

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
   * @param filename ファイル名
   */
  protected void saveScreenshot(String filename) {
    if (driver instanceof TakesScreenshot) {
      // 実装している場合のみ実施する

      TakesScreenshot screen = (TakesScreenshot) driver;
      Path capture = screenshotDir.resolve(filename);
      try {
        byte[] screenshotData;
        if (target == TargetBrowser.CHROME || target == TargetBrowser.ANDROID_CHROME) {
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
   * @return スクリーンショット
   */
  private byte[] getScreenshotForChrome() {
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    TakesScreenshot screen = (TakesScreenshot) driver;

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
          Thread.sleep(Settings.getInstance().getTestWindowScrollWait());
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
