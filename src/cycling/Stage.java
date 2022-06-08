
package cycling;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/** Stage class represents each stage in a race
 *
 */
public class Stage extends OrderResults {

	// private stage attributes
	private int stageID;
	private String stageName;
	private String stageDescription;
	private double length;
	private LocalDateTime startTime;
	private StageType type;
	private StageState state;
	// static counter for last used ID int
	private static int currentStageID = 1;
	// Contains all stage segments
	private ArrayList<Segment> segments = new ArrayList<Segment>();

	// Array of points for stage type
	private static final int[] FLAT = {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] HILL =  {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
	private static final int[] OTHER = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };


	/** Stage constructor
	 *
	 * @param stageName Name of stage
	 * @param stageDescription Description of stage
	 * @param length Length of stage
	 * @param startTime	Start time of stage
	 * @param type Type of stage
	 */
	public Stage(String stageName, String stageDescription, double length, LocalDateTime startTime, StageType type) {
		this.stageName = stageName;
		this.stageDescription = stageDescription;
		this.length = length;
		this.startTime = startTime;
		this.type = type;

		// assigns stage ID and increments
		this.stageID = currentStageID++;

		// Sets state to preparing
		this.state = StageState.PREPARING;
	}

	// getters
	public int getStageID() { return stageID; }

	public String getName() { return stageName; }

	public String getDesc() { return stageDescription; }

	public double getLength() { return length; }

	public LocalDateTime getStartTime() { return startTime; }

	public StageType getType() { return type; }

	public StageState getState() { return state; }

	public ArrayList<Segment> getSegments() { return segments; }

	public static int getCurrentStageID() { return currentStageID; }

	// Setters
	public void setState(StageState state) { this.state = state;}

	public static void setCurrentStageID(int ID) { currentStageID = ID; }

	public void setLength(double length) { this.length = length; }

	public void setStageName(String stageName) { this.stageName = stageName; }

	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

	public void setStageDescription(String stageDescription) { this.stageDescription = stageDescription; }

	public void setType(StageType type) { this.type = type; }

	/** Adding segment to array list of segments, added based upon location in stage
	 *
	 * @param newSeg Segment to add to array list of segments in the stage
	 */
	public void addSegment(Segment newSeg) {
		// If no other segments exist, then added to front of array list
		if(segments.size() == 0) {
			segments.add(newSeg);
		} else {
			boolean added = false;
			// Loops through and finds the first segment the new seg is before
			for(int i = 0 ; i < segments.size() ; i++) {
				// Compares the location pf the new segment to current segments
				if(segments.get(i).getLocation() > newSeg.getLocation()) {

					assert((segments.get(i).getLocation() - newSeg.getLocation()) > 0)
							: "Segment has been placed in the wrong location";

					segments.add(i, newSeg);
					added = true;
					break;
				}
			}
			// If segment is at end of race it is placed at the back of the array list
			if(!added) {
				segments.add(newSeg);
			}
		}
	}

	/** Remove segment based on segment ID
	 *
	 * @param segToRemove Segment ID of the segment to remove
	 */
	public void removeSegment(int segToRemove) {
		// removes segment
		segments.removeIf(segmentToCheck -> segToRemove == segmentToCheck.getSegmentID());
	}

	/** Returns all the IDs of the segments inside a stage
	 *
	 * @return segmentIDs All the IDs of segments within the stage
	 */
	public int[] getSegmentIDs() {

		int[] segmentIDs = new int[segments.size()];

		/* loops through all segment IDs and places them in
		array of ints */
		for (int i=0; i<segments.size(); i++){
			int ID = segments.get(i).getSegmentID();
			segmentIDs[i] = ID;
		}
		return segmentIDs;
	}

	/** Used to separate results into segment and stage results
	 *
	 * @param riderID ID of the rider whose results it is
	 * @param checkpoints Finishing times at the end of each segment/stage
	 */
	public void processResults(int riderID, LocalTime... checkpoints) {

		//Format results taking away the start time
		LocalTime startTime = checkpoints[0];

		for (int i = 1 ; i < checkpoints.length ; i++) {
			// Finds difference between start and each segment
			LocalTime timeBetween =  checkpoints[i].minus(Duration.ofNanos(startTime.toNanoOfDay()));
			checkpoints[i] = timeBetween;
		}

		// Finds the finish time for the stage
		LocalTime stageFinishTime = checkpoints[checkpoints.length - 1];

		super.addResult(riderID, stageFinishTime);

		// Places remaining checkpoints inside of segments
		// -2 as don't want to include stage finish time
		for (int i=0; i < checkpoints.length -2 ; i++) {
			// Checkpoints[i+1] as start time is not included
			segments.get(i).addResult(riderID, checkpoints[i+1]);
		}
		// Riders results are then adjusted
		// If its a time trial results are not adjusted
		if(type != StageType.TT) {
			adjustRiderResults();
		}

	}

	/** Gets all the results for a rider whose completed the stage
	 *
	 * @param riderID ID of the rider the results are for
	 * @return Array of segment finish times, and total completion time
	 */
	public LocalTime[] getStageResults(int riderID) {

		// Checks a result is registered for rider on the system
		if(!super.checkNewResult(riderID)) {

			// Create array as size of segments in race
			LocalTime[] results = new LocalTime[segments.size() + 1];
			// Gets result for each segment
			for (int i=0 ; i< segments.size() ; i++) {
				results[i] = segments.get(i).getResult(riderID);
			}
			// Adds total completion to end of array
			results[segments.size()] = super.getResult(riderID);

			return results;

		} else {
			// If no results exist, an empty array is returned
			return new LocalTime[0];
		}
	}

	/**	Adjusts the times of riders who are less than a second apart from each other
	 *
	 */
	public void adjustRiderResults() {

		// Check results aren't being adjusted for a time trial
		assert(type != StageType.TT)
				: "Stage time is time trial, results should not be adjusted.";
		// Order results to see if there is any adjustments needed
		ArrayList<Result> stageResults = super.getResults();
		// If there are no results for the stage calculations are not done
		if (stageResults.size() >= 1) {
			// Sets the first result to itself as no rider before it, so time can't be adjusted
			stageResults.get(0).setAdjustedTime(stageResults.get(0).getFinishTime());
			// If there's only one rider result for stage no need to compare
			if (stageResults.size() > 1) {
				LocalTime currentLowestTime = null;
				for (int i = 0; i < (stageResults.size() - 1); i++) {
					// Gets time to check and time before it
					LocalTime l1 = stageResults.get(i).getFinishTime();
					LocalTime l2 = stageResults.get(i + 1).getFinishTime();

					if (ChronoUnit.SECONDS.between(l1, l2) < 1) {
						// Set to the same time as rider before it
						if (currentLowestTime == null) {
							currentLowestTime = stageResults.get(i).getFinishTime();
						}
						stageResults.get(i + 1).setAdjustedTime(currentLowestTime);

					} else {
						// Current lowest time set to null as times are more than a second apart
						currentLowestTime = null;
						// Time is just set to its regular time as no adjustment needed
						LocalTime adjustedFinish = stageResults.get(i + 1).getFinishTime();
						stageResults.get(i + 1).setAdjustedTime(adjustedFinish);
					}
				}
			}
		}
	}

	/** Gets the adjusted times for a stage, if a Time trial, regular results are returned
	 *  as no need for adjustments
	 *
	 * @param riderID ID of the rider, whose results are to be found
	 * @return Adjusted results of a specific rider
	 */
	public LocalTime getAdjustedRiderTime(int riderID) {

		// If a time trial results do not need to be adjusted
		if (type == StageType.TT) {
			return super.getResult(riderID);
		}
		else {
			// Results are adjusted, this makes sure whenever new results are added
			// results will be readjusted
			adjustRiderResults();
			return super.getAdjustedResult(riderID);
		}
	}

	/** Removes the results in a stage for a specific rider
	 *
	 * @param riderID ID of the rider whose results will be removed
	 */
	public void removeAllRiderResults(int riderID) {

		super.removeResult(riderID);

		for(Segment segmentWithResult : segments) {
			segmentWithResult.removeResult(riderID);
		}
	}

	/** Assigns points to a rider given the stage type
	 */
	public void assignStagePoints() {

		int[] chosenResults = switch (type) {
			case FLAT -> FLAT;
			case MEDIUM_MOUNTAIN -> HILL;
			default -> OTHER;
		};

		// Gets the results for the sprint
		ArrayList<Result> sprintResults = super.getResults();

		// Assigns points to each rider based on their position
		for (int i =0 ; i < chosenResults.length ; i++) {

			// If there are less than 15 riders in a race this stops points being assigned
			// to null riders
			if (i < sprintResults.size()){
				sprintResults.get(i).setPoints(chosenResults[i]);

				assert(sprintResults.get(i).getPoints() == chosenResults[i])
						: "Stage Points are being miss-assigned";
			} else {
				break;
			}
		}
	}

	/** Gets points for each rider in the stage, ranked by general classification times
	 *
	 * @return Array of points, ordered by the time they finished the race
	 */
	public int[] getRidersPointsForStage(){

		// Assign points to riders, this is best done right before result assignment
		// as new results may be added later down the line
		assignStagePoints();

		//Set up array of ints
		int[] rankedRiderPoints = new int[super.getResults().size()];
		// Array of results
		ArrayList<Result> resultsToCheck = super.getResults();
		// Go through each rider based on elapsed time on the stage and get their points
		for (int i = 0 ; i < resultsToCheck.size() ; i++) {

			int currentID = resultsToCheck.get(i).getRiderID();
			int totalPoints = resultsToCheck.get(i).getPoints();
			// Goes through each segment getting the riders points for each sprint
			// And adding them to the total
			for (Segment segmentToCheck : segments) {

				if( segmentToCheck.getType() == SegmentType.SPRINT) {
					// Sprint child class is then set
					Sprint sprintToCheck = (Sprint) segmentToCheck;
					// Assigns sprint points to riders
					sprintToCheck.intermediateSprintPoints();
					Result riderResultInSegment = sprintToCheck.getRiderAndResult(currentID);
					totalPoints += riderResultInSegment.getPoints();
				}
			}

			rankedRiderPoints[i] = totalPoints;


		}
		return rankedRiderPoints;
	}

	/** Gets mountain points for each rider in the stage, ranked by general classification times
	 *
	 * @return Array of mountain points, ordered by the time they finished the race
	 */
	public int[] getMountainPointsForStage(){

		//Set up array of ints
		int[] rankedRiderPoints = new int[super.getResults().size()];
		// Array of results
		ArrayList<Result> resultsToCheck = super.getResults();
		// Go through each rider based on elapsed time on the stage and get their points for climbs
		for (int i = 0 ; i < resultsToCheck.size() ; i++) {

			int currentID = resultsToCheck.get(i).getRiderID();
			int totalPoints = 0;
			// Goes through each segment getting the riders points for each climb
			// And adding them to the total
			for (Segment segmentToCheck : segments) {

				if(segmentToCheck instanceof Climb climbToCheck) {
					// Climb class is set
					// Points are assigned to the climb
					climbToCheck.assignClimbPoints();
					// Result is gotten for rider with correct ID
					Result riderResultInClimb = climbToCheck.getRiderAndResult(currentID);
					// Points are added to the total
					totalPoints += riderResultInClimb.getPoints();
				}
			}

			rankedRiderPoints[i] = totalPoints;


		}
		return rankedRiderPoints;
	}

	/** toString
	 *
	 * @return String with concatenated information
	 */
	public String toString() {
		return "Stage[stageID=" + stageID + ",stageName= " + stageName +
				", description= " + stageDescription + ", length = " + length + ", startTime = " + startTime +
				", type = " + type + "]";
	}
}
