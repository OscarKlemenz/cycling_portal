package cycling;


/** Child class for Segment, represents a climb in a stage
 *
 */
public class Climb extends Segment {

	// private attributes
	private double averageGradient;
	private double length;

	// Points for climb type
	private static final int[] C4 = {1};
	private static final int[] C3 = {2, 1};
	private static final int[] C2 = {5, 3, 2, 1};
	private static final int[] C1 = {10 ,8, 6, 4, 2, 1};
	private static final int[] HC = {20, 15, 12, 10, 8, 6, 4, 2};

	/** Climb constructor
	 *
	 * @param location Location of finish of climb
	 * @param type Type of climb
	 * @param averageGradient Average gradient of climb
	 * @param length Length of climb
	 */
	public Climb(double location, SegmentType type, double averageGradient
	, double length) {
		super(location, type);

		assert(length >= 0)
				: "Length is a negative number, validity of length has not been checked";

		this.averageGradient = averageGradient;
		this.length = length;
	}

	// getters
	public double getAverageGradient() { return averageGradient; }
	public double getLength() { return length; }

	// setters
	public void setAverageGradient( double averageGradient) {
		this.averageGradient = averageGradient;
	}
	public void setLength( double length ) {
		this.length = length;
	}

	/** Chooses which points to assign to a rider given the climb type
	 */
	public void assignClimbPoints() {

		// Switch case determines which points are applicable for climb
		int[] chosenResults = switch (super.getType()) {
			case C4 -> C4;
			case C3 -> C3;
			case C2 -> C2;
			case C1 -> C1;
			case HC -> HC;
			default -> new int[0];
		};

		super.assignPoints(chosenResults);
	}


	/** toString
	 *
	 * @return Concatenated climb information
	 */
	public String toString() {
		return "Climb[segmentID=" + super.getSegmentID() + ", location= "
				+ super.getLocation() + ", avgGradient= " + averageGradient
				+ "length=" + length + "]";
	}
}


