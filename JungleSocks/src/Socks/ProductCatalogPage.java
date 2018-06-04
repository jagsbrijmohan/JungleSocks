package Socks;

import java.util.List;
import java.util.concurrent.TimeUnit; 
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.text.DecimalFormat;

public class ProductCatalogPage {

	static Select selectedState;
	static Double SubTotal, TotalTax, Total, TaxRate;
	// public static void main(String[] args) throws Exception {
	// TODO Auto-generated method stub

	@BeforeTest
	public void Initialization() {
		switch (Constants.BrowserToTest) {
		case "Chrome": {

			Constants.DriverPath = ".\\Drivers\\ChromeBrowser\\chromedriver.exe";
			Constants.WebDrive = "webdriver.chrome.driver";
			System.setProperty(Constants.WebDrive, Constants.DriverPath);
			Constants.driver = new ChromeDriver();
			break;
		}
		/*case "IE": {

			Constants.DriverPath = ".\\Drivers\\IE11\\IEDriverServer.exe";
			Constants.WebDrive = "webdriver.ie.driver";
			System.setProperty(Constants.WebDrive, Constants.DriverPath);
			Constants.driver = new InternetExplorerDriver();
			break;
		}*/
		case "Edge": {

			Constants.DriverPath = ".\\Drivers\\Edge\\MicrosoftWebDriver.exe";
			Constants.WebDrive = "webdriver.edge.driver";
			System.setProperty("java.net.preferIPv4Stack", "true");
			System.setProperty(Constants.WebDrive, Constants.DriverPath);
			Constants.driver = new EdgeDriver();
			break;
		}
		case "Firefox": {

			Constants.DriverPath = ".\\Drivers\\FirefoxBrowser\\geckodriver.exe";
			Constants.WebDrive = "webdriver.gecko.driver";
			System.setProperty(Constants.WebDrive, Constants.DriverPath);
			Constants.driver = new FirefoxDriver();
			break;
		}
		default: {

			Constants.DriverPath = ".\\Drivers\\ChromeBrowser\\chromedriver.exe";
			Constants.WebDrive = "webdriver.chrome.driver";
			System.setProperty(Constants.WebDrive, Constants.DriverPath);
			Constants.driver = new ChromeDriver();
		}

		}

		Constants.driver.get(Constants.Url);
		Constants.driver.manage().window().maximize();
		Constants.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Test
	public void DataEntry() {
		Constants.driver.findElement(By.xpath("//*[@id=\"line_item_quantity_zebra\"]")).sendKeys("1.0");
		Constants.driver.findElement(By.xpath("//*[@id=\"line_item_quantity_lion\"]")).sendKeys("2");
		Constants.driver.findElement(By.xpath("//*[@id=\"line_item_quantity_elephant\"]")).sendKeys("1");
		Constants.driver.findElement(By.xpath("//*[@id=\"line_item_quantity_giraffe\"]")).sendKeys("8");

		selectedState = new Select(Constants.driver.findElement(By.name("state")));
		selectedState.selectByValue("CA");

		// Constants.driver.findElement(By.xpath("/html/body/form/input")).click();
	}

	@Test(dependsOnMethods = { "DataEntry" })
	public void CheckOutClick() {

		Assert.assertTrue(selectedState.getFirstSelectedOption().isSelected());

		IterateCatalogPageTable();

		Constants.driver.findElement(By.name("commit")).click();

		// ConfirmationPage cp = new ConfirmationPage();
		// cp.VerifyIfCorrectTotals();
	}

	public void IterateCatalogPageTable() {

		String state = selectedState.getFirstSelectedOption().getText();
		try {
			Assert.assertNotNull(TaxRate = ExcelToHashMap.getTaxRate(state));
			Constants.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
		}
		// Iterate the Table and print the Values
		WebElement Webtable = Constants.driver.findElement(By.tagName("Table"));

		List<WebElement> tableRows = Webtable.findElements(By.xpath("/html/body/form/table[1]/tbody/tr"));
		System.out.println(tableRows.size());
		int RowIndex = 1;
		for (WebElement rowElement : tableRows) {
			List<WebElement> Columns = rowElement.findElements(By.xpath("td"));
			int ColumnIndex = 1;
			Double Price = 0.00, InStock = 0.00, Quantity = 0.00;
			System.out.println(Columns.size());
			for (WebElement colElement : Columns) {
				if (ColumnIndex == 2) {// Price

					Price = Double.parseDouble(colElement.getText());
				} else if (ColumnIndex == 3) {// InStock
					InStock = Double.parseDouble(colElement.getText());
				} else if (ColumnIndex == 4) {// Quantity
					if (colElement.getAttribute("innerHTML").contains("input")) {

						WebElement input = colElement.findElement(By.tagName("input"));

						ValidateEnteredQuantity(input.getAttribute("value"));

						if (!input.getAttribute("value").isEmpty()) {
							Quantity = Double.parseDouble(input.getAttribute("value"));
						}
					}
				}
				ColumnIndex = ColumnIndex + 1;
			}
			Assert.assertTrue("Entered Quantity is more than InStock", Quantity <= InStock);
			CalculateTotals(Price, Quantity, InStock, TaxRate);
			RowIndex = RowIndex + 1;
		}
	}

	private void ValidateEnteredQuantity(String Quantity) {
		if (!Quantity.isEmpty() && Quantity != null) {
			// "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$"
			Boolean isDouble = Quantity.matches("^(-?)(0|([1-9][0-9]*))(\\.[0]+)?$");

			Assert.assertTrue("Found Invalid Value", isDouble);
			if (isDouble) {
				Assert.assertTrue(Double.parseDouble(Quantity) >= 0);
			} else
				System.out.println("Invalid Value" + Quantity);
		}
	}

	/**
	 * SubTotal = ((Price of Item1 * Quantity) + (Price of Item2 * Quantity) +
	 * (Price of Item3 * Quantity) + (Price of Item4 * Quantity))
	 * 
	 * TotalTax = (((Price of Item1 * Quantity) * (Rate/100)) + ((Price of Item2 *
	 * Quantity) * (Rate/100)) + ((Price of Item3 * Quantity) * (Rate/100)) +
	 * ((Price of Item4 * Quantity) * (Rate/100))
	 * 
	 * Total = SubTotal + TotalTax
	 */
	public void CalculateTotals(Double Price, Double Quantity, Double InStock, Double TaxRate) {
		DecimalFormat df = new DecimalFormat("0.00");
		Constants.SubTotal += Double.parseDouble(df.format(Price * Quantity));
		Constants.TotalTax += Double.parseDouble(df.format(((Price * Quantity) * TaxRate / 100)));
		Constants.Total += Double.parseDouble(df.format((Price * Quantity) + ((Price * Quantity) * TaxRate / 100)));
		// System.out.println(
		// "SubTotal:" + Constants.SubTotal + "TotalTax:" + Constants.TotalTax +
		// "Total:" + Constants.Total);
	}
}
