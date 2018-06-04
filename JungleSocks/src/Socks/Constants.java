package Socks;

import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

public class Constants {
	//For Chrome Browser Set BrowserToTest = "Chrome"
	//For IE Browser Set BrowserToTest = "IE"
	//For Firefox Browser Set BrowserToTest = "Firefox"
	//For Edge Browser Set BrowserToTest = "Edge"
	
	public static String BrowserToTest = "Chrome";
	public static WebDriver driver = null;
	public static String WebDrive;
	public static String DriverPath;	
	public static String Url = "https://jungle-socks.herokuapp.com/";
	public static String Filename = "StatesWithTaxRates.xlsx";
	public static String Path = ".\\Data\\" + Filename;
	public static FileInputStream fis;
	public static final String EXCELFILELOCATION = Path;
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFRow row;

	public static double SubTotal = 0.00;
	public static double TotalTax = 0.00;
	public static double Total = 0.00;
}
