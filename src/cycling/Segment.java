package cycling;

import java.util.ArrayList;

/** Parent class of Climb and Sprint, basic framework for each child classes.
 *  Extends order results, a superclass which deals with storing and ordering results
 *
 */
abstract class Segment extends OrderResults {

	// Counter for ID of segment
	private static int currentSegmentID = 1;

	// attributes
	private int segmentID;
	private double location;
	private SegmentType type;

	/** Segment superclass constructor
	 *
	 * @param location Location of segment
	 * @param type Type of segment
	 */
	public Segment(double location, SegmentType type){
		this.segmentID = currentSegmentID++;
		this.location = location;
		this.type = type;
	}

	// getters
	public int getSegmentID() { return segmentID; }
	public double getLocation() { return location; }
	public SegmentType getType(){ return type; }
	public static int getCurrentSegmentID() { return currentSegmentID; }

	// setters
	public void setSegmentID( int segmentID ) { this.segmentID = segmentID; }
	public void setLocation(double location){ this.location = location;}
	public void setType( SegmentType type ) { this.type = type; }
	public static void setCurrentSegmentID(int ID) { currentSegmentID = ID; }

	/** After child class determines the points to assign based on segment type this
	 *  method assigns the points.
	 *
	 * @param chosenResults Array of points to assign, based on segment type
	 */
	public void assignPoints(int[] chosenResults) {

		// Gets the results for the sprint
		ArrayList<Result> climbResults = super.getResults();

		// Assign points to each rider based on their position
		for (int i =0 ; i < chosenResults.length ; i++) {

			// If there are less than 15 riders in a race this stops points being assigned
			// to null riders
			if (i < climbResults.size()){
				climbResults.get(i).setPoints(chosenResults[i]);

				assert(climbResults.get(i).getPoints() == chosenResults[i])
						: "Climb results have not been inputted correctly";

			} else {
				break;
			}
		}
	}

}


