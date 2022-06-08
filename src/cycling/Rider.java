package cycling;

import java.io.Serializable;

/** Rider class represents each cyclist competing in races,
 *  riders are stored within the team they are in.
 *
 */
public class Rider implements Serializable {
	// rider attributes
	private int teamID;
	private int riderID;
	private String riderName;
	private int yearOfBirth;

	// static to hold last assigned ID number
	private static int currentRiderID = 1;


	/** Rider constructor
	 *
	 * @param teamID  ID of team rider is on
	 * @param name Name of rider
	 * @param yearOfBirth Year rider was born
	 */
	public Rider(int teamID, String name, int yearOfBirth){

		// Assertion used to check name has had a validity check
		assert(name != null)
				: "Rider name is null, checks have not been performed";
		// Assertion used to check YOB has had a validity check
		assert(yearOfBirth >= 1900)
				: "Year of birth is less than 1900";

		this.teamID = teamID;
		this.riderName = name;
		this.yearOfBirth = yearOfBirth;
		// rider ID assigned and incremented
		this.riderID = currentRiderID++;
	}

	// getters
	public int getTeamID(){ return teamID; }

	public int getRiderID(){ return riderID; }

	public String getRiderName(){ return riderName; }

	public int getYearOfBirth(){ return yearOfBirth; }

	public static int getCurrentRiderID() { return currentRiderID; }

	// setters
	public void setTeamID(int teamID) { this.teamID = teamID; }

	public void setRiderID(int riderID) { this.riderID = riderID; }

	public void setRiderName(String riderName) { this.riderName = riderName; }

	public void setYearOfBirth(int yearOfBirth) { this.yearOfBirth = yearOfBirth; }

	public static void setCurrentRiderID(int ID) { currentRiderID = ID; }

	/** toString
	 *
	 * @return Concatenates rider information
	 */
	public String toString() {
		return "Rider[teamID=" + teamID + ",riderIO= " + riderID +
				", riderName= " + riderName + ", yearOfBirth= " + yearOfBirth + "]";
	}


}
