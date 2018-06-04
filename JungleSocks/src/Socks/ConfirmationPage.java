package Socks;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class ConfirmationPage {

	@Test
	public void VerifyIfCorrectTotals() {

		Assert.assertEquals(Constants.SubTotal,
				Double.parseDouble(Constants.driver.findElement(By.id("subtotal")).getText().replace("$", "")), 0.00);
		Assert.assertEquals(Constants.TotalTax,
				Double.parseDouble(Constants.driver.findElement(By.id("taxes")).getText().replace("$", "")), 0.00);
		Assert.assertEquals(Constants.Total,
				Double.parseDouble(Constants.driver.findElement(By.id("total")).getText().replace("$", "")), 0.00);
		System.out.println("Calculations were correct.");
	}

	@AfterTest
	public void terminateDriver() {

		Constants.driver.close();

	}
}
