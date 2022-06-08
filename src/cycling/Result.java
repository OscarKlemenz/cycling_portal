package cycling;

import java.io.Serializable;
import java.time.LocalTime;

/** Holds the result of a rider in a race/stage/segment
 *
 */
public class Result implements Serializable {

	// private attributes
	private int riderID;
	private LocalTime finishTime;
	private LocalTime adjustedTime;
	// Number of points corresponding to which place, rider came in stage/segment
	// May be adjusted when new riders added, so only assigned when required
	private int points = 0;

	/** Result Constructor
	 *
	 * @param riderID ID of the rider whose results it is
	 * @param finishTime Time the rider completed the race/stage/segment
	 */
	public Result(int riderID, LocalTime finishTime) {

		assert( riderID > 0)
				: "Incorrect rider ID has been inputted";

		this.riderID = riderID;
		this.finishTime = finishTime;
	}

	// getters
	public int getRiderID() { return riderID; }
	public LocalTime getFinishTime() { return finishTime; }
	public LocalTime getAdjustedTime() { return adjustedTime; }
	public int getPoints() { return points; }

	// setters
	public void setRiderID( int riderID ) { this.riderID = riderID; }
	public void setFinishTime( LocalTime finishTime ) { this.finishTime = finishTime; }
	public void setAdjustedTime( LocalTime adjustedTime ) { this.adjustedTime = adjustedTime; }
	public void setPoints( int points ) { this.points = points; }

	/** toString
	 *
	 * @return Concatenated information about RaceResult
	 */
	public String toString() {
		return "Result[riderID=" + riderID + ",finishTime=" + finishTime.toString() + ",adjustedTime="
				+ adjustedTime.toString() + ",points=" + points + "]";
	}
}


