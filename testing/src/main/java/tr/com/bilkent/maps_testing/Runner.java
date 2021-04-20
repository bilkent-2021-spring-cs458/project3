package tr.com.bilkent.maps_testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;

/**
 * Runs all the test sets and displays their results.
 */
public class Runner {
	public static void main(String[] args) {
		WebDriver driver;
		if (args.length < 1) {
			System.out.println(
					"Running with Chrome Driver. Path an argument to use another driver. Possible options: chrome, firefox, opera\n\n");
			driver = new ChromeDriver();
		} else {
			switch (args[0]) {
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "opera":
				driver = new OperaDriver();
				break;
			default:
				driver = new ChromeDriver();
			}
		}

		TestSetResult overall = new TestSetResult(0, 0);
		TestSet[] sets = { new TestSet1(driver), new TestSet2(driver), new TestSet3(driver) };

		for (int setNum = 0; setNum < sets.length; setNum++) {
			System.out.println("Test Set " + (setNum + 1) + ": " + sets[setNum].run());
			overall.add(sets[setNum].getResult());
		}
		driver.quit();

		System.out.println();
		System.out.println("Overall Test Results: \t" + overall);
	}
}
