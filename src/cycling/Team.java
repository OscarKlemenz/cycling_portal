package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/** Class represents teams on the system with each team containing riders assigned
 *  to the team.
 *
 */
public class Team implements Serializable {
	// team attributes
	private int teamID;
	private String teamName;
	private String description;

	// Holds the latest assigned team ID
	private static int currentTeamID = 1;

	// riders on the team
	private ArrayList<Rider> riders = new ArrayList<Rider>();

	/** Constructor for the team class
	 *
	 * @param teamName Name of the team
	 * @param description Description of the team
	 */
	public Team(String teamName, String description) {

		// Assertion used to check name has had a validity check
		assert(teamName != null)
				: "Team name is null, checks have not been performed";

		this.teamName = teamName;
		this.description = description;
		// assigns team ID and increments
		this.teamID = currentTeamID++;
	}

	// getters
	public int getTeamID(){ return teamID; }

	public String getTeamName(){ return teamName; }

	public String getDescription(){ return description; }

	public ArrayList<Rider> getRiders(){ return riders; }

	public static int getCurrentTeamID() { return currentTeamID; }

	// setters
	public static void setCurrentTeamID(int ID) { currentTeamID = ID; }

	public void setDescription(String description) { this.description = description; }

	public void setTeamName(String teamName) { this.teamName = teamName; }

	/** Adds a rider to the team
	 *
	 * @param rider Rider to add to the team
	 */
	public void addRider(Rider rider) {
		riders.add(rider);
	}

	/** Checks the list of riders in the team and removes rider with matching ID
	 *
	 * @param riderID Unique identifier for each rider on the system
	 */
	public void removeRider(int riderID){
		// removes rider from team
		riders.removeIf(riderToCheck -> riderID == riderToCheck.getRiderID());
	}

	/** Loops through all the riders in a team and returns their IDs
	 *
	 * @return riderIDs List of all rider ID
	 */
	public int[] returnRiderIDs(){

		// declares int array the same size as the number of riders
		int[] riderIDs = new int[riders.size()];

		// loops through all riders
		for(int i=0;i<riders.size();i++){
			// gets ID of current rider
			int ID = riders.get(i).getRiderID();
			// id is placed in the array
			riderIDs[i] = ID;
		}

		return riderIDs;
	}

	/** toString
	 *
	 * @return Concatenates team information
	 */
	public String toString() {
		return "Team[teamID=" + teamID + ",teamName= " + teamName +
				", description= " + description + ", riders= " + riders + "]";
	}
}
