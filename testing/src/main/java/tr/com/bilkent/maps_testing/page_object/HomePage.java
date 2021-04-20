package tr.com.bilkent.maps_testing.page_object;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The home page object model (POM). It is the main page that is displayed while
 * accessing the website. Depending on the selected tab, content may differ.
 */
public class HomePage {
	private static final By SUBMIT_BUTTON = By.name("submitBtn");
	private static final By NAVIGATE_TO_CITY = By.id("pills-city-tab");
	private static final By NAVIGATE_TO_EARTH = By.id("pills-world-tab");
	private static final By RESULT = By.id("result");
	private static final By LATITUDE = By.name("latInput");
	private static final By LONGTITUDE = By.name("lngInput");

	protected WebDriver driver;

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	public void submit(String latitude, String longtitude) {
		enterInput(latitude, LATITUDE);
		enterInput(longtitude, LONGTITUDE);
		submit();
	}

	public void submit() {
		WebElement button = getVisibleElement(SUBMIT_BUTTON);
		button.click();

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.or(ExpectedConditions.alertIsPresent(),
				ExpectedConditions.presenceOfElementLocated(RESULT)));
	}

	public void enterInput(String input, By by) {
		WebElement inputField = getVisibleElement(by);
		clearField(inputField);
		inputField.sendKeys(input);
	}

	public void navigateToCity() {
		navigate(NAVIGATE_TO_CITY);
	}

	public void navigateToEarth() {
		navigate(NAVIGATE_TO_EARTH);
	}

	private void navigate(By by) {
		WebElement button = driver.findElement(by);
		button.click();
	}

	public String getResult() {
		WebElement label = driver.findElement(RESULT);
		if (label.getText().equals("There are no cities nearby.")) {
			return "fail";
		}
		return label.getText();
	}

	private static void clearField(WebElement field) {
		while (field.getAttribute("value").length() != 0) {
			field.sendKeys(Keys.BACK_SPACE);
			field.sendKeys(Keys.DELETE);
		}
	}

	private WebElement getVisibleElement(By by) {
		List<WebElement> elements = driver.findElements(by);
		return elements.stream().filter(WebElement::isDisplayed).findAny().get();
	}
}
