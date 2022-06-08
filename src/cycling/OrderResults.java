package cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/** Abstract Class used to order the results contained within
 *  the race/stage/segment objects
 *
 */
public abstract class OrderResults implements Serializable {

	// private attributes
	private ArrayList<Result> results = new ArrayList<Result>();

	//getters
	public ArrayList<Result> getResults() { return results; }

	/** Adds a result to list of current results for stage/segment/race
	 *
	 * @param riderID ID of the rider whose result it is
	 * @param finishTime Time the rider finishes
	 */
	public void addResult(int riderID, LocalTime finishTime) {

		boolean inserted = false;

		Result newResult = new Result(riderID, finishTime);
		// Adds the finishing time for the whole stage
		// If no other results exist yet, no sort is needed
		if (results.size() == 0) {
			results.add(newResult);
		}
		else {
			for (Result result : results) {
				if (finishTime.isBefore(result.getFinishTime())) {
					// Finds the index before the element it is quicker than
					int index = results.indexOf(result);
					// Adds the result to list of results
					results.add(index, newResult);
					// Exits loop so time not added multiple times
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				results.add(newResult);
			}
		}

		// Validates that result has been added
		assert(results.contains(newResult))
				: "Inputted result was not added to array list of results";
	}

	/** Getter to return result of a specific rider
	 *
	 * @param riderID ID of the rider whose result it is
	 * @return Riders time
	 */
	public LocalTime getResult(int riderID){

		for (Result resultToCheck : results) {
			if (riderID == resultToCheck.getRiderID()) {
				return resultToCheck.getFinishTime();
			}
		}
		return null;
	}

	/** Getter to return result and rider
	 *
	 * @param riderID ID of the rider whose result it is
	 * @return Rider and result corresponding to inputted ID
	 */
	public Result getRiderAndResult(int riderID) {

		for (Result resultToCheck : results) {
			if (riderID == resultToCheck.getRiderID()) {
				return resultToCheck;
			}
		}
		return null;
	}

	/** Gets riderIDs in ranked order, based on their total elapsed time
	 *
	 * @return rankedRiders	Ranked list of riderIDs based on what time they finished the race
	 */
	public int[] getRankedRiders(){

		int[] rankedRiders = new int[results.size()];
		/* As riders are already ordered by their time,
		 arraylist just needs to be converted to array */
		for (int i = 0 ; i <results.size() ; i++) {
			rankedRiders[i] = results.get(i).getRiderID();
		}

		return rankedRiders;

	}

	/** Gets adjusted times in ranked order, as result array already ordered only needs a for
	 *  loop to work
	 *
	 * @return rankedRiders Ranked list of riderIDs based on what time they finished the race
	 */
	public LocalTime[] getRankedAdjustedTimes(){

		LocalTime[] rankedResults = new LocalTime[results.size()];

		for (int i = 0 ; i <results.size() ; i++) {
			rankedResults[i] = results.get(i).getAdjustedTime();
		}

		return rankedResults;
	}


	/** Getter to return adjusted result of a specific rider
	 *
	 * @param riderID ID of the rider whose result it is
	 * @return Riders adjusted time
	 */
	public LocalTime getAdjustedResult(int riderID){

		for (Result resultToCheck : results) {
			if (riderID == resultToCheck.getRiderID()) {
				return resultToCheck.getAdjustedTime();
			}
		}
		return null;
	}

	/** Checks a result for rider has not already been added
	 *
	 * @param riderID ID of the rider to check
	 * @return True if new result, false if rider result already exists
	 */
	public boolean checkNewResult(int riderID) {
		for(Result resultToCheck : results) {
			if(resultToCheck.getRiderID() == riderID) {
				return false;
			}
		}
		return true;
	}

	/** Removes a result from the array of results based on riderID
	 *
	 * @param riderID ID of the rider whose result will be removed
	 */
	public void removeResult(int riderID) {
		results.removeIf(resultToCheck -> resultToCheck.getRiderID() == riderID);
	}

	/** Sets the adjusted time of a riders result
	 *
	 * @param riderID ID of the rider whose result it is
	 * @param adjustedTime Result that has been adjusted, based on other rider times
	 */
	public void setAdjustedTimeToResult(int riderID, LocalTime adjustedTime) {

		for (Result resultToCheck : results) {
			//Adds time to a result
			if (resultToCheck.getRiderID() == riderID) {

				resultToCheck.setAdjustedTime(adjustedTime);
				break;
			}
		}
	}

	/** Adds additional points to a specific rider, used for summing points in a
	 *  stage/race.
	 *
	 * @param riderID ID of the rider to add more points to
	 * @param points Amount of points to increase the result by
	 */
	public void addPointsToResult(int riderID, int points) {

		for (Result resultToCheck : results) {
			//Add points to a result
			if (resultToCheck.getRiderID() == riderID) {

				int currentPoints = resultToCheck.getPoints();
				int newPoints = currentPoints + points;
				resultToCheck.setPoints(newPoints);
				break;
			}
		}
	}

	/** Clears all the results for the race/stage/segment
	 *
	 */
	public void clearResults() {
		results.clear();
	}


}


