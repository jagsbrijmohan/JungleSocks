package Socks;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelToHashMap {

	public static void loadExcel(String workSheetName) throws Exception {
		System.out.println("Loading Excel data...");
		File file = new File(Constants.EXCELFILELOCATION);
		Constants.fis = new FileInputStream(file);
		Constants.workbook = new XSSFWorkbook(Constants.fis);
		Constants.sheet = Constants.workbook.getSheet(workSheetName);// "StateTaxRate"
		Constants.fis.close();
	}

	public static Map<String, Double> getDataMap(String workSheetName) throws Exception {

		if (Constants.sheet == null) {
			loadExcel(workSheetName);
		}
		Map<String, Double> myMap = new HashMap<String, Double>();
		for (int i = 1; i < Constants.sheet.getLastRowNum() + 1; i++) {
			Constants.row = Constants.sheet.getRow(i);
			String keyCell = Constants.row.getCell(0).getStringCellValue();

			// int colNum = Constants.row.getLastCellNum();
			for (int j = 1; j < 2; j++) {
				Double value = Constants.row.getCell(j).getNumericCellValue();
				myMap.put(keyCell, value);
			}
		}
		return myMap;
	}

	public static Double getTaxRate(String key) throws Exception {
		Map<String, Double> myVal = getDataMap("StateTaxRate");// .get("STATERATEDATA");
		Double taxRate = myVal.get(key);
		return taxRate;
	}

}
