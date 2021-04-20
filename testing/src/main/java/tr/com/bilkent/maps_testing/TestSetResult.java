package tr.com.bilkent.maps_testing;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the result of a set of test cases.
 */
@Data
@AllArgsConstructor
public class TestSetResult {
	private int numPassed;
	private int total;

	/**
	 * Increments the number of passed test cases.
	 */
	public void incrementPassedCases() {
		numPassed++;
	}

	/**
	 * Sums the two test set results as if they were from the same set.
	 * 
	 * @param other The result to add to this result
	 */
	public void add(TestSetResult other) {
		numPassed += other.numPassed;
		total += other.total;
	}

	@Override
	public String toString() {
		return "Passed: " + numPassed + " \tFailed: " + (total - numPassed) + " \tTotal: " + total + " \tSuccess rate: "
				+ String.format("%.2f", 1.0 * numPassed / total);
	}
}
