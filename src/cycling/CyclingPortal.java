package cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * CyclingPortal is a compiling, functioning implementor
 * of the CyclingPortalInterface interface.
 * 
 * @author Oscar Klemenz & Olivia Kerschen
 * @version 1.0
 *
 */
public class CyclingPortal implements CyclingPortalInterface {

	// Attributes hold teams and races on the portal.
	protected ArrayList<Team> teams = new ArrayList<>();
	protected ArrayList<Race> races = new ArrayList<>();

	@Override
	public int[] getRaceIds() {

		// Gets all the IDs of currently created races
		int[] raceIDs = new int[races.size()];

		for (int i=0; i<races.size(); i++){
			int ID = races.get(i).getRaceID();
			raceIDs[i] = ID;
		}

		return raceIDs;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {

		// Check if the name is legal (not a duplicate)
		String[] raceNames = new String[races.size()];
		for (int i = 0; i < races.size(); i++) {
			raceNames[i] = races.get(i).getName();
		}
		//Checks the name is unique
		legalNameCheck(raceNames, name);
		// Checks if name should throw an invalid name exception
		validNameCheck(name);

		// New race is created
		Race newRace = new Race(name, description);
		races.add(newRace);
		return newRace.getRaceID();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		// Finds race (function also throws exception if race does not exist)
		Race race = findRaceWithID(raceId);
		// Returns details
		return race.allRaceDetails();

	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// Finds race (function also throws exception if race does not exist)
		Race raceToDelete = findRaceWithID(raceId);
		// Removes race from list of races
		races.remove(raceToDelete);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		// Finds race (function also throws exception if race does not exist)
		Race chosenRace = findRaceWithID(raceId);
		return chosenRace.getStages().size();

	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {

		Race chosenRace = findRaceWithID(raceId);

		// Checks if name is valid
		validNameCheck(stageName);
		assert (stageName != null)
				: "Stage name is null, validity check did not work.";
		// Check if the name is legal (not a duplicate)
		// Has to check every stage name on the system, so loops through races
		ArrayList<String> names = new ArrayList<>();
		for (Race raceWithStageNames : races) {
			ArrayList<Stage> stages = raceWithStageNames.getStages();
			// Loops through all stages and gets their names
			for(Stage stageWithName : stages) {
				names.add(stageWithName.getName());
			}
		}
		// Converts to an array to check
		String[] stageNamesArray = new String[names.size()];
		for (int i = 0; i < names.size(); i++) {
			stageNamesArray[i] = names.get(i);
		}
		// Function checks stage name
		legalNameCheck(stageNamesArray, stageName);
		// Checks length is 5 or greater
		if (length < 5) {
			throw new InvalidLengthException("Length of stage is too short, needs to be greater than 5km");
		}

		// Creates the new stage and places it into the corresponding race
		Stage newStage = new Stage(stageName, description, length, startTime, type);
		chosenRace.addStage(newStage);

		return newStage.getStageID();

	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// Finds race (function also throws exception if race does not exist)
		Race chosenRace = findRaceWithID(raceId);

		assert (raceId == chosenRace.getRaceID())
				: "Race gotten does not have the same ID as the ID inputted.";

		return chosenRace.getStageIDs();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		// Finds stage (function also throws exception if race does not exist)
		Stage stageWithLength = findStageWithID(stageId);
		return stageWithLength.getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {

		boolean found = false;
		// Loops through races to find stage
		for (Race raceToCheck : races) {
			for (Stage stage : raceToCheck.getStages()) {
				// As stage contains all info about results/segments, only stage needs to be removed
				if (stageId == stage.getStageID()) {
					raceToCheck.removeStage(stageId);
					found = true;
					break;
				}
			}
		}
		if (!found) {
			throw new IDNotRecognisedException("stage ID doesnt exist, unable to remove stage");
		}

	}



	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {

		// Check stage ID exists, place in stage
		Stage stage = findStageWithID(stageId);

		// Checks location of segment is correct
		double stageLength = stage.getLength();
		if (location > stageLength || location < 0) {
			throw new InvalidLocationException("Climb finishes after stage finish, could not add climb");
		} else if ( (location - length) < 0) {
			throw new InvalidLocationException("Climb starts before the stage begins, could not add climb");
		}

		// Checks the state is correct
		if (stage.getState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is in state of waiting for results, could not add segment");
		}
		// Checks stage type is not a time trial
		else if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Stage is a time trial, cannot include a climb");
		}

		// New climb is created
		Climb newClimb = new Climb(location, type, length, averageGradient);
		stage.addSegment(newClimb);
		return newClimb.getSegmentID();

	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {

		Stage stageForSegment = findStageWithID(stageId);

		assert (stageId == stageForSegment.getStageID())
				: "Inputted ID does not match the one of the stage gotten.";

		// Error checking
		if (stageForSegment.getState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is in state of waiting for results, could not add segment");
		} else if (stageForSegment.getType() == StageType.TT){
			throw new InvalidStageTypeException("Stage is a time trial, cannot include a sprint");
		} else if (location > stageForSegment.getLength() || location < 0) {
			throw new InvalidLocationException("Sprint finishes after stage finish, could not add sprint");
		}

		// Adds new sprint to the stage
		Sprint newSprint = new Sprint(location);

		stageForSegment.addSegment(newSprint);

		return newSprint.getSegmentID();
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {

		boolean found = false;
		// Search all races
		for (Race raceToCheck : races) {
			// Search all stages
			for (Stage stageToCheck : raceToCheck.getStages()) {
				// Once segment found remove it
				for (Segment segmentToCheck : stageToCheck.getSegments()) {

					if (segmentToCheck.getSegmentID() == segmentId) {
						// Check stage state
						if (stageToCheck.getState() == StageState.WAITING_FOR_RESULTS) {
							throw new InvalidStageStateException("Stage is waiting for results, cannot remove segment");
						} else {

							assert (stageToCheck.getState() == StageState.PREPARING)
									: "Stage state is not in preparing phase.";

							// Removes segment
							stageToCheck.removeSegment(segmentId);
							found = true;
							break;
						}
					}
				}
			}
		}
		// If ID not on system exception thrown
		if (!found) {
			throw new IDNotRecognisedException("Given ID does not correspond to any segment, could not remove");
		}
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {

		Stage stageToChange = findStageWithID(stageId);
		// Checks stage isn't already in correct state
		if (stageToChange.getState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage state is already waiting for results");
		}
		// Sets stage state to waiting for results
		stageToChange.setState(StageState.WAITING_FOR_RESULTS);

	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {

		Stage stageToCheck = findStageWithID(stageId);
		return stageToCheck.getSegmentIDs();

	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {

		// checks if team name already in the system
		for (Team teamToCheck : teams) {
			if (teamToCheck.getTeamName().equals(name)) {
				throw new IllegalNameException("Name already exists on the system, please choose another name");
			}
		}
		// Checks if name is valid
		validNameCheck(name);
		assert (name != null)
				: "Team name is null, validity check did not work.";
		// create a new team
		Team newTeam = new Team(name, description);
		// add it to our list of teams
		teams.add(newTeam);
		// return the team ID
		return newTeam.getTeamID();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {

		// checks team exists on system
		Team teamToRemove = findTeamWithID(teamId);
		// As riders are stored inside of teams, all results for riders need to be removed
		// Loop through riders
		for (Rider riderToCheck : teamToRemove.getRiders()) {
			// For each rider loop through races and remove results
			int riderId = riderToCheck.getRiderID();
			// Goes through each race
			for (Race raceToCheck : races) {
				// Goes through each stage and removes results
				for (Stage stageToCheck : raceToCheck.getStages()) {
					// Removes results
					stageToCheck.removeAllRiderResults(riderId);
				}
			}
		}
		teams.remove(teamToRemove);
	}

	@Override
	public int[] getTeams() {

		// Gets all the IDs of currently created races
		int[] teamIDs = new int[teams.size()];

		for (int i=0; i<teams.size(); i++){
			int ID = teams.get(i).getTeamID();
			teamIDs[i] = ID;
		}

		return teamIDs;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {

		Team teamToAccess = findTeamWithID(teamId);
		return teamToAccess.returnRiderIDs();

	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {

		// check rider name and yob
		if (yearOfBirth < 1900){
			throw new IllegalArgumentException("Year of birth is less than 1900");
		}
		if (name == null) {
			throw new IllegalArgumentException("Name is null, please enter a non-empty name");
		}

		Team teamToAddRider = findTeamWithID(teamID);
		// create a new rider
		Rider newRider = new Rider(teamID, name, yearOfBirth);
		// places inside designated team
		teamToAddRider.addRider(newRider);


		return newRider.getRiderID();
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {

		boolean found = false;
		// loop through all teams
		for (Team teamToCheck : teams) {
			// return rider ID
			int[] riderIDs = teamToCheck.returnRiderIDs();

			for (int ID : riderIDs) {
				// if rider ID in list
				if (ID == riderId) {
					// remove rider in team
					teamToCheck.removeRider(riderId);
					found = true;
					break;
				}
			}
		}
		if (!found) {
			throw new IDNotRecognisedException("Rider ID does not exist could not remove rider");
		}
		// Rider results are removed from the system
		else {
			// Goes through each race
			for (Race raceToCheck : races) {
				// Goes through each stage and removes results
				for (Stage stageToCheck : raceToCheck.getStages()) {
					// Removes results
					stageToCheck.removeAllRiderResults(riderId);

					assert(stageToCheck.getResult(riderId) == null)
							: "Rider result has not been properly deleted";

				}
			}
		}
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {

		// Checks the entered rider Id is valid
		checkRiderID(riderId);
		// Stage ID is checked, and correct object is gotten
		Stage stageToCheck = findStageWithID(stageId);
		// Any exceptions are checked
		if(stageToCheck.getState() == StageState.PREPARING) {
			throw new InvalidStageStateException("Stage is still being prepared, could not add result");
		} else if ((stageToCheck.getSegmentIDs().length + 2) != checkpoints.length ){
			throw new InvalidCheckpointsException("Number of checkpoints is not n+2 the number of segments");
		} else if (!stageToCheck.checkNewResult(riderId)) {
			throw new DuplicatedResultException("Result is a duplicate, rider already has results registered");
		}
		else {

			assert(checkpoints.length > 0)
					: "No checkpoints have been inputted";

			// Placed into stage class and put into respective stages
			stageToCheck.processResults(riderId, checkpoints);
		}
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {

		// Checks the entered rider Id is valid
		checkRiderID(riderId);
		// Stage ID is checked, and correct object is gotten
		Stage stageToCheck = findStageWithID(stageId);

		return stageToCheck.getStageResults(riderId);
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {

		// Checks the entered rider Id is valid
		checkRiderID(riderId);
		// Stage ID is checked, and correct object is gotten
		Stage stageToCheck = findStageWithID(stageId);

		return stageToCheck.getAdjustedRiderTime(riderId);

	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {

		// Checks the entered rider Id is valid
		checkRiderID(riderId);
		// Stage ID is checked, and correct object is gotten
		Stage stageToCheck = findStageWithID(stageId);

		stageToCheck.removeAllRiderResults(riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {

		// Check the stage ID exists and outputs correct stage
		Stage stageToCheck = findStageWithID(stageId);

		return stageToCheck.getRankedRiders();
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {

		// Check the stage ID exists and outputs correct stage
		Stage stageToCheck = findStageWithID(stageId);

		stageToCheck.adjustRiderResults();
		// Returns adjusted times
		return stageToCheck.getRankedAdjustedTimes();
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {

		// Check the stage ID exists and outputs correct stage
		Stage stageToCheck = findStageWithID(stageId);

		return stageToCheck.getRidersPointsForStage();
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {

		// Check the stage ID exists and outputs correct stage
		Stage stageToCheck = findStageWithID(stageId);

		return stageToCheck.getMountainPointsForStage();
	}

	@Override
	public void eraseCyclingPortal() {

		teams.clear();
		races.clear();

		// Resets the current IDs
		Team.setCurrentTeamID(1);
		Rider.setCurrentRiderID(1);
		Race.setCurrentRaceID(1);
		Stage.setCurrentStageID(1);
		Segment.setCurrentSegmentID(1);

		assert(teams.size() == 0)
				: "Portal has not been properly erased";

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {

		// Gets all the Ids
		int[] idsToSave = new int[5];
		idsToSave[0] = Team.getCurrentTeamID();
		idsToSave[1] = Rider.getCurrentRiderID();
		idsToSave[2] = Race.getCurrentRaceID();
		idsToSave[3] = Stage.getCurrentStageID();
		idsToSave[4] = Segment.getCurrentSegmentID();

		// Create a store object
		Store objectsToStore = new Store(teams, races, idsToSave);
		// Save it in a serialised
		FileOutputStream fileOut = new FileOutputStream(filename + ".ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(objectsToStore);
		out.close();
		fileOut.close();

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {


		// Loads serialised file
		FileInputStream fileIn = new FileInputStream(filename + ".ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);

		Object obj = in.readObject();
		if (obj instanceof Store objectsToLoad) {

			teams = objectsToLoad.getTeams();
			races = objectsToLoad.getRaces();

			int[] idsToLoad = objectsToLoad.getIds();
			// Sets IDs
			Team.setCurrentTeamID(idsToLoad[0]);
			Rider.setCurrentRiderID(idsToLoad[1]);
			Race.setCurrentRaceID(idsToLoad[2]);
			Stage.setCurrentStageID(idsToLoad[3]);
			Segment.setCurrentSegmentID(idsToLoad[4]);

		} else {
			throw new ClassNotFoundException("File loaded does not contain classes for cycling portal");
		}
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {

		boolean found = false;
		// Checks all races on the system
		for (Race raceToCheck : races) {
			// If name is equal
			if (raceToCheck.getName().equals(name)) {
				// Race ID is gotten
				races.remove(raceToCheck);
				found = true;
				break;
			}
		}

		if(!found) {
			throw new NameNotRecognisedException("Race name does not match any race on the system");
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		// Go through each stage of the race adding up rider times
		// For each rider, add the time to a new result object for that rider
		raceWithResults.collectResults();
		// Output ordering
		return raceWithResults.getRankedAdjustedTimes();
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		raceWithResults.collectResults();
		raceWithResults.collectPoints();

		return raceWithResults.getRacePoints();
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		// Results are recollected on each call, in case a new rider is addec
		raceWithResults.collectResults();
		raceWithResults.collectMountainPoints();

		return raceWithResults.getRacePoints();
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		raceWithResults.collectResults();

		return raceWithResults.getRankedRiders();
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		// Collect results and points
		raceWithResults.collectResults();
		raceWithResults.collectPoints();

		return raceWithResults.getRidersRankedByPoints();
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {

		// Find the correct race
		Race raceWithResults = findRaceWithID(raceId);

		// Collect results and points
		raceWithResults.collectResults();
		raceWithResults.collectMountainPoints();

		return raceWithResults.getRidersRankedByPoints();
	}

	/** Checks if a name already exists, so exception can be thrown
	 *
	 * @param names			List of names that already exist
	 * @param chosenName	Name input by the user
	 * @throws IllegalNameException If name already matches one on the system
	 */
	public void legalNameCheck(String[] names, String chosenName) throws IllegalNameException{
		//Compares name to list of names already in system
		for (String name: names) {
			if (Objects.equals(name, chosenName)) {
				throw new IllegalNameException("Name entered already matches one on the system, enter a new name");
			}
		}
	}

	/** Checks if name is null, empty or greater than 30 chars. Also checks escape characters
	 *  as they are whitespace.
	 * @param name			The name to be checked
	 * @throws InvalidNameException If the name is null, empty, contains more that 30 characters
	 * 								or has any whitespace.
	 */
	public void validNameCheck(String name) throws InvalidNameException{

		if (name == null){ throw new InvalidNameException("Name is null"); }
		else if (name.isEmpty()) { throw new InvalidNameException("Name is empty"); }
		else if (name.length() > 30 ) { throw new InvalidNameException("Name contains more than 30 characters");  }
		else if (name.indexOf(' ') >= 0) {throw new InvalidNameException("Name contains whitespaces");  }

		// Checks if name contains any escape characters, which also count as whitespace
		String[] escapeChars = {"\t", "\b", "\n", "\r", "\f", "\'", "\"", "\\"};
		for (String escapeToCheck : escapeChars) {
			if(name.contains(escapeToCheck)) {
				throw new InvalidNameException("Name contains escape characters. (Whitespace)");
			}
		}

	}

	/** Checks rider exists on the system
	 *
	 * @param riderID ID of the rider to check
	 * @throws IDNotRecognisedException If the ID does not match any rider on the system
	 */
	public void checkRiderID(int riderID) throws IDNotRecognisedException {

		boolean exists = false;

		for(Team teamToCheck : teams) {
			for (int riderIDToCheck : teamToCheck.returnRiderIDs()) {
				if (riderID == riderIDToCheck) {
					exists = true;
					break;
				}
			}
		}
		if (!exists) {
			throw new IDNotRecognisedException("Rider with corresponding ID does not exist on system.");
		}
	}

	/** Finds and validates a Race using a given ID
	 *
	 * @param raceID ID of the race to find
	 * @return Race with corresponding ID
	 * @throws IDNotRecognisedException If the ID does not match to any race
	 * 									in the system.
	 */
	public Race findRaceWithID(int raceID) throws IDNotRecognisedException {

		for (Race raceToCheck : races) {
			if (raceID == raceToCheck.getRaceID()) {
				return raceToCheck;
			}
		}

		throw new IDNotRecognisedException("Race with inputted ID does not exist");
	}

	/** Finds and validates a Stage using a given ID
	 *
	 * @param stageID ID of the stage to find
	 * @return Stage with corresponding ID
	 * @throws IDNotRecognisedException If the ID does not match to any stage
	 * 									in the system.
	 */
	public Stage findStageWithID(int stageID) throws IDNotRecognisedException {

		// Check stage ID exists, place in stage
		// Search through each race
		for (Race raceToCheck : races) {
			// Check each stage
			for (Stage stage : raceToCheck.getStages()) {
				if (stageID == stage.getStageID()) {
					return stage;
				}
			}
		}
		throw new IDNotRecognisedException("Stage with inputted ID does not exist");
	}

	/** Finds and validates a Team using a given ID
	 *
	 * @param teamID ID of the team to find
	 * @return Team with corresponding ID
	 * @throws IDNotRecognisedException If the ID does not match to any team
	 * 								    in the system.
	 */
	public Team findTeamWithID(int teamID) throws IDNotRecognisedException {

		assert(teamID != 0)
				: "Team ID is an invalid number";

		for (Team teamToCheck : teams) {
			if (teamID == teamToCheck.getTeamID()) {
				return teamToCheck;
			}
		}
		throw new IDNotRecognisedException("Race with inputted ID does not exist");
	}

}
