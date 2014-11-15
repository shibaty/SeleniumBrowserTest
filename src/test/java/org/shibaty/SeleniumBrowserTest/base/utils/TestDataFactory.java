/**
 *
 */

package org.shibaty.SeleniumBrowserTest.base.utils;

import java.io.FileInputStream;
import java.io.IOException;
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
 * @author Yasutaka
 */
public class TestDataFactory {

  private static final String SHEET_NAME = "testdata";

  private TestDataFactory() {
    // nop
  }

  public static List<Map<String, String>> getTestData(String path)
      throws InvalidFormatException, IOException {
    return getTestData(open(path).getSheet(SHEET_NAME));
  }

  private static Workbook open(String fileName)
      throws InvalidFormatException, IOException {
    Workbook wb = null;
    String path = TestDataFactory.class.getClassLoader().getResource("xlsx/" + fileName).getPath();

    try (
        FileInputStream fis = new FileInputStream(path)) {
      wb = WorkbookFactory.create(fis);
    }

    return wb;
  }

  private static List<Map<String, String>> getTestData(Sheet sheet)
      throws InvalidFormatException {

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    // Row Iterator
    Iterator<Row> itRow = sheet.rowIterator();

    if (!itRow.hasNext()) {
      // シートが不正
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
