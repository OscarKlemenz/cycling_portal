package cycling;

/** Child class for Segment class, class represents intermediate sprints
 *
 */
public class Sprint extends Segment {

	// Points assigned to riders based on the time they finish the intermediate sprint
	private static final int[] SPRINT_POINTS = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };

	/** Sprint Constructor
	 *
	 * @param location Location of finish line for sprint
	 */
	public Sprint(double location) {
		super(location, SegmentType.SPRINT);
	}


	/** Returns points to assign to riders based upon their rank
	 *
	 */
	public void intermediateSprintPoints() {

		// Gets the results for the sprint
		super.assignPoints(SPRINT_POINTS);
	}

	/** toString
	 *
	 * @return toString statement
	 */
	public String toString() {
		return "Sprint[segmentID=" + super.getSegmentID() +
				", location= " + super.getLocation() + "]";
	}

}


