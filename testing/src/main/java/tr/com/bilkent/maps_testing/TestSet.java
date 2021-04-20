package tr.com.bilkent.maps_testing;

/**
 * An interface used for all the test sets.
 */
public interface TestSet {
	/**
	 * Runs this test set, and returns its result.
	 * 
	 * @return the result of running this test set
	 */
	public TestSetResult run();

	/**
	 * Returns the result of running this test set.
	 * 
	 * @return the result of running this test set
	 */
	public TestSetResult getResult();
}
