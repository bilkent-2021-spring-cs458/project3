package tr.com.bilkent.maps_testing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import lombok.Getter;
import tr.com.bilkent.maps_testing.page_object.HomePage;

/**
 * First test set. Includes test cases for testing various combinations of
 * coordinates.
 */
public class TestSet1 implements TestSet {
	private final JSONArray data;
	private final WebDriver driver;

	@Getter
	private TestSetResult result;

	/**
	 * Creates this test set by fetching its data from the JSON resource file.
	 * 
	 * @param driver The driver to use for this test
	 */
	public TestSet1(WebDriver driver) {
		JSONTokener tokener = new JSONTokener(getClass().getResourceAsStream("TestCaseOneData.json"));
		data = new JSONArray(tokener);
		this.driver = driver;
		result = new TestSetResult(0, data.length());
	}

	public TestSetResult run() {
		data.forEach(obj -> {
			boolean caseResult = run((JSONObject) obj);
			if (caseResult) {
				result.incrementPassedCases();
			}
		});
		return result;
	}

	/**
	 * Runs a test case specified by the JSON object.
	 * 
	 * @param data JSON data object
	 * @return true if the test succeeds, false otherwise
	 */
	private boolean run(JSONObject data) {
		String expected = "expected_result";
		Util.resetWebDriver(driver);
		
		driver.get(Util.BASE_URL);
		HomePage home = new HomePage(driver);
		try {
			home.submit(data.getString("latitude"), data.getString("longitude"));
			driver.switchTo().alert().accept();
			return "fail".equals(data.getString(expected));

		} catch (NoAlertPresentException e) {
			return (data.getString(expected).equals(home.getResult()));
		} catch (Exception e) {
			return "fail".equals(data.getString(expected));
		}
	}
}
