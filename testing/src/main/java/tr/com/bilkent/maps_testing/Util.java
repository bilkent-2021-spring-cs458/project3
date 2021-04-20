package tr.com.bilkent.maps_testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.WebStorage;


/**
 * A collection of utility methods for test cases.
 */
public final class Util {
	/**
	 * The home page URL of the website
	 */
	public static final String BASE_URL = "file:///C:/Javid/CS/CS458/Project3/index.html";

	private Util() {
	}

	/**
	 * Resets the given driver by deleting its cookies, and clearing its
	 * sessionStorage and localStorage.
	 * 
	 * @param driver The driver to reset.
	 */
	public static void resetWebDriver(WebDriver driver) {
		driver.manage().deleteAllCookies();
		try {
			WebStorage webStorage = (WebStorage) driver;
			webStorage.getSessionStorage().clear();
			webStorage.getLocalStorage().clear();
		} catch (Exception e) {
			System.err.println("Look");
			// Probably it is the first test case, so no need
		}
		
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			// No alert visible. Nothing to do.
		}
	}
}
