
package org.shibaty.SeleniumBrowserTest.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * テストデータファクトリクラス.
 */
public class DataFactory {

  /**
   * テストデータがあるシート名.
   */
  private static final String SHEET_NAME = "testdata";

  /**
   * テストデータがあるresources配下のパス.
   */
  private static final String TESTDATA_PATH = "testdata";

  /**
   * コンストラクタ.<br>
   * インスタンス生成抑止
   */
  private DataFactory() {
    // nop
  }

  /**
   * テストデータ取得.<br>
   *
   * @param fileName ファイル名
   * @return テストデータリスト
   * @throws InvalidFormatException ファイルフォーマット不正
   * @throws IOException ファイルが存在しない等
   */
  public static List<Map<String, String>> getTestData(String fileName)
      throws InvalidFormatException, IOException {
    return getTestData(open(fileName).getSheet(SHEET_NAME));
  }

  /**
   * Workbookを開く.<br>
   *
   * @param fileName ファイル名.
   * @return Workbook
   * @throws InvalidFormatException ファイルフォーマット不正
   * @throws IOException ファイルが存在しない等
   */
  private static Workbook open(String fileName)
      throws InvalidFormatException, IOException {
    Workbook wb = null;
    StringBuilder sb = new StringBuilder();
    sb.append(TESTDATA_PATH)
      .append("/")
      .append(fileName);
    try (
        InputStream is = DataFactory.class.getClassLoader().getResourceAsStream(
            sb.toString())) {
      wb = WorkbookFactory.create(is);
    }
    return wb;
  }

  /**
   * テストデータを取得.<br>
   *
   * @param sheet Sheet
   * @return テストデータリスト.
   * @throws InvalidFormatException フォーマット不正
   */
  private static List<Map<String, String>> getTestData(Sheet sheet)
      throws InvalidFormatException {

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    // Row Iterator
    Iterator<Row> itRow = sheet.rowIterator();

    if (!itRow.hasNext()) {
      // フォーマットが不正
      throw new InvalidFormatException("Nothing header.");
    }

    // キー情報の取得
    List<String> keyList = new ArrayList<String>();
    for (Cell cell : itRow.next()) {
      keyList.add(cell.getStringCellValue());
    }

    // データの取得
    while (itRow.hasNext()) {
      Map<String, String> map = new HashMap<String, String>();

      Row row = itRow.next();
      Iterator<Cell> itCell = row.cellIterator();
      for (String key : keyList) {
        String value = null;
        if (itCell.hasNext()) {
          value = itCell.next().getStringCellValue();
        }
        map.put(key, value);
      }
      list.add(map);
    }
    return list;
  }
}
