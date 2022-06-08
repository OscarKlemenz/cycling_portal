package cycling;

/**
 * This enum is used to represent the state of a stage
 *
 */
public enum StageState {

	/**
	 * State when stage is being created
	 */
	PREPARING,

	/**
	 * When stage preparation has been completed, and awaiting results
	 */
	WAITING_FOR_RESULTS,

}
