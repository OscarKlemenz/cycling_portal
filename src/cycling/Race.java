package cycling;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/** Race class represents each race in the system, contains the stages in each race and methods
 *  to deal with race results.
 */
public class Race extends OrderResults {

	// private stage attributes
	private int raceID;
	private String raceName;
	private String raceDescription;
	// static counter for last used ID int
	private static int currentRaceID = 1;
	// Arraylist contains all stage segments
	private ArrayList<Stage> stages = new ArrayList<Stage>();

	/** Constructor for race class
	 *
	 * @param raceName Name of new race
	 * @param raceDescription Description of new race
	 */
	public Race(String raceName, String raceDescription) {

		this.raceName = raceName;
		this.raceDescription = raceDescription;

		// assigns race ID and increments
		this.raceID = currentRaceID++;
	}

	// getters
	public int getRaceID() { return raceID; }

	public String getName() { return raceName; }

	public String getDesc() { return raceDescription; }

	public ArrayList<Stage> getStages() { return stages; }

	public static int getCurrentRaceID() { return currentRaceID; }

	// setters
	public static void setCurrentRaceID(int ID) { currentRaceID = ID; }

	public void setRaceName(String raceName) { this.raceName = raceName; }

	public void setRaceDescription(String desc) { this.raceDescription = raceDescription; }


	/** Adds a new stage to the race, orders them based upon their start time
	 *
	 * @param newStage Stage to be added to the list of stages
	 */
	public void addStage(Stage newStage) {

		// Stages need to be added based upon their start time
		// If no stages in race, adds to start of list
		if(stages.size() == 0) {
			stages.add(newStage);
		} else {

			boolean added = false;
			// Loops through and finds the first stage the new stage is before
			for(int i = 0 ; i < stages.size() ; i++) {
				if(newStage.getStartTime().isBefore(stages.get(i).getStartTime())) {
					stages.add(i, newStage);
					added = true;
					break;
				}
			}
			if(!added) {
				stages.add(newStage);
			}
		}
		assert(stages.contains(newStage))
				: "Stage has not been correctly added to race.";
	}


	/** Removes a stage from the list of stages, using the stages ID
	 *
	 * @param stageToRemove ID of the stage to remove
	 */
	public void removeStage(int stageToRemove) {
		// loops through all segments
		for (int i = 0; i < stages.size(); i++) {
			// checks through all segments to remove to current seg
			if (stageToRemove == stages.get(i).getStageID()) {
				// removes stage
				stages.remove(i);
				break;
			}
		}
	}

	/** Loops through all stages and places each ones ID into an array to
	 * 	return
	 *
	 * @return stageIDs	Formatted array containing all stage IDs
	 */
	public int[] getStageIDs() {
		int[] stageIDs = new int[stages.size()];

		/* loops through all stage IDs and places them in
		array of ints */
		for (int i=0; i<stages.size(); i++){
			int ID = stages.get(i).getStageID();
			stageIDs[i] = ID;
		}
		return stageIDs;
	}

	/** Outputs information concerning a race. Formatted in a string
	 *
	 * @return details Race information, contains raceID, name, description,
	 * 				   the number of stages in the race and the total length
	 * 				   of all stages
	 */
	public String allRaceDetails() {
		double totalLength = 0;
		for (Stage currentStage : stages) {
			totalLength += currentStage.getLength();
		}

		String details = "race ID= " + raceID + ", name = " + raceName + ", description= " + raceDescription +
		", Number of stages= " + stages.size() + ", total length= " + totalLength;

		return details;
	}

	/** toString
	 *
	 * @return		Concatenates all the race information
	 */
	public String toString() {
		return "Race[raceID=" + raceID + ",raceName= " + raceName +
				", description= " + raceDescription + "]";
	}

	/** Goes through each rider and collects their times in each of the race stages,
	 *  these are then ordered based upon total completion time using superclass
	 *
	 */
	public void collectResults() {
		// Resets the list of results, ready to repopulate
		super.clearResults();
		// Check stages exist for the race
		if(stages.size() > 0 ) {

			// Array of riders which have competed in stages is gotten.
			// First stage may not have results so checks every result until one contains results
			int[] ridersInRace = new int[0];
			for(Stage stageWithResults : stages) {
				// Rider results in all stages are adjusted
				stageWithResults.adjustRiderResults();
				// First stage containing results is then accessed to get the riders in the race
				if(stageWithResults.getRankedRiders().length != 0){
					ridersInRace = stageWithResults.getRankedRiders();
				}
			}

			// Goes through each rider and gathers times
			for (int rider : ridersInRace) {

				LocalTime totalAdjustedTime = LocalTime.of(0, 0, 0);

				for (Stage stageToCheck : stages) {

					// Gets the time the rider took for the stage
					LocalTime timeForStage = stageToCheck.getAdjustedRiderTime(rider);

					// Checks that there is a result for the stage
					if (timeForStage != null) {
						// Converts to a duration and adds it
						totalAdjustedTime = totalAdjustedTime.plus(Duration.ofNanos(timeForStage.toNanoOfDay()));
					}

				}
				// Once all results for rider are gotten, result is placed into race results
				super.addResult(rider, totalAdjustedTime);
				super.setAdjustedTimeToResult(rider, totalAdjustedTime);
			}
		}

	}

	/** Goes through each stage and rider and collects their points for the race total
	 *
	 */
	public void collectPoints() {

		// Goes through every stage getting rider points
		for (Stage stageWithPoints : stages) {

			// Gets array of riders and points corresponding
			int[] rankedRiders = stageWithPoints.getRankedRiders();
			int[] rankedPoints = stageWithPoints.getRidersPointsForStage();
			// Adds rider points to their total number of points in the race
			for (int i = 0 ; i < rankedRiders.length ; i++) {
				super.addPointsToResult(rankedRiders[i], rankedPoints[i]);
			}
		}

	}

	/** Goes through each stage and rider and collects their mountain points for the
	 *  race total
	 */
	public void collectMountainPoints() {

		// Goes through every stage getting rider points
		for (Stage stageWithPoints : stages) {

			// Gets array of riders and points corresponding
			int[] rankedRiders = stageWithPoints.getRankedRiders();
			int[] rankedMountainPoints = stageWithPoints.getMountainPointsForStage();
			// Adds rider points to their total number of points in the race
			for (int i = 0 ; i < rankedRiders.length ; i++) {
				super.addPointsToResult(rankedRiders[i], rankedMountainPoints[i]);
			}
		}

	}

	/** Gets list of points for the race, with points being ranked on total finish time across
	 *  all the stages
	 *
	 * @return		Array of points for each rider
	 */
	public int[] getRacePoints() {

		ArrayList<Result> resultsWithPoints = super.getResults();
		// Creates an array to place points in
		int[] points = new int[resultsWithPoints.size()];
		// Array list looped through and each riders points added to new array
		for (int i = 0 ; i < points.length ; i++) {

			points[i] = resultsWithPoints.get(i).getPoints();
		}

		return points;

	}

	/** Collects all riders points across all stages and ranks riders based upon the amount
	 *  of points they have
	 *
	 * @return Ranked array of riders, ranked based upon total points for the race
	 */
	public int[] getRidersRankedByPoints() {

		ArrayList<Result> resultsWithPoints = super.getResults();

		ArrayList<Integer> riderIDs = new ArrayList<>();

		// Extracts result one by one, starting with the largest
		while (resultsWithPoints.size() > 0) {

			// Holds the index of the rider with the most points currently
			int currentLargestIndex = 0;
			// Holds the largest point count for comparison later on
			int currentLargestPoints = -1;

			// Compare results to find the rider with most points currently
			for (int i=0; i<resultsWithPoints.size(); i++) {
				// Finds the rider with the largest points currently in the old arraylist
				if (resultsWithPoints.get(i).getPoints() > currentLargestPoints) {
					currentLargestIndex = i;
					currentLargestPoints = resultsWithPoints.get(i).getPoints();
				}
			}
			/* Results are added to new array list one by one, starting with the largest
			 */
			riderIDs.add(resultsWithPoints.get(currentLargestIndex).getRiderID());
			resultsWithPoints.remove(currentLargestIndex);

		}
		// Converts array list into an array
		int[] arrayRiderIDs = new int[riderIDs.size()];

		for(int i = 0; i<riderIDs.size();i++) {
			arrayRiderIDs[i] = riderIDs.get(i);
		}

		return arrayRiderIDs;
	}

}
