package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/** Class stores teams and races and next assignable IDs,
 *  which can then be serialized and deserialized
 *
 */
public class Store implements Serializable {

	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<Race> races = new ArrayList<Race>();

	//ID holder - Team, Rider, Race, Stage, Segment
	private int[] ids;

	/** Constructor
	 *
	 * @param teams ArrayList of teams to be stored
	 * @param races ArrayList of races to be stored
	 * @param ids Array of next assignable IDs
	 */
	public Store(ArrayList<Team> teams, ArrayList<Race> races, int[] ids) {

		this.teams = teams;
		this.races = races;

		assert(ids.length == 5)
				: "Not all IDs have been inputted to be serialised";

		this.ids = ids;
	}

	//getters
	public ArrayList<Team> getTeams() { return teams; }
	public ArrayList<Race> getRaces() { return races; }
	public int[] getIds(){ return ids; }

	//setters
	public void setTeams( ArrayList<Team> teams ) { this.teams = teams; }
	public void setRaces( ArrayList<Race> races ) { this.races = races; }
	public void setIds( int[] ids) { this.ids = ids; }

}
