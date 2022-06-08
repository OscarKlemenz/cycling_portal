import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the CyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) throws NameNotRecognisedException, ClassNotFoundException, IOException, IllegalNameException,InvalidStageStateException, DuplicatedResultException, InvalidCheckpointsException, InvalidNameException, InvalidStageStateException, IDNotRecognisedException, InvalidNameException, InvalidLengthException, InvalidLocationException, InvalidStageTypeException{
		System.out.println("The system compiled and started the execution...");

		CyclingPortalInterface portal = new CyclingPortal();

		assert (portal.getRaceIds().length == 0)
				: "Initial SocialMediaPlatform not empty as required or not returning an empty array.";


		//testAddIntermediateSprintToStage();
		megaTest();

	}

	public static void testAllRaceIds() throws IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();

		portal.createRace("kebab", "race to test");
		portal.createRace("chips", "race to test");

		int[] allRaceIds = portal.getRaceIds();
		for(int raceIds: allRaceIds) {
			System.out.println(raceIds);
		}
	}

	public static void testCreateRace() throws IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
	}

	public static void testViewRaceDetails() throws IDNotRecognisedException, IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.viewRaceDetails(4));

	}

	public static void testRemoveRaceById() throws IDNotRecognisedException, IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		portal.removeRaceById(1);
		assert(portal.getRaceIds().length == 0) : "Race was not deleted";
	}

	public static void testGetNumberOfStages() throws IDNotRecognisedException, IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.getNumberOfStages(1));
	}

	public static void testAddStageToRace() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.getNumberOfStages(1));
		System.out.println(portal.addStageToRace(1, "Stage2Test", "This is a stage test", 5, LocalDateTime.now().minusDays(1), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.getNumberOfStages(1));
		int[] stages = portal.getRaceStages(1);
		for (int stage : stages) {
			System.out.println(stage);
		}


	}

	public static void testGetRaceStages() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.addStageToRace(1, "Stage2Test", "This is a stage test", 9, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		assert(portal.getRaceStages(1).length == 2) : "There are not two stages in race";

	}

	public static void testGetStageLength() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		assert(portal.getStageLength(1) == 5) : "Stage length is incorrect";
	}

	public static void testRemoveStageById() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.getNumberOfStages(1));
		portal.removeStageById(1);
		assert(portal.getRaceStages(1).length == 0): "number of stages greater than zero";
		System.out.println(portal.getNumberOfStages(1));
	}

	public static void testAddCategorizedClimbToStage() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException, InvalidLocationException, InvalidStageStateException,  InvalidStageTypeException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.addCategorizedClimbToStage(1, 4.0, cycling.SegmentType.C4, 0.7, 2.0 ));
	}

	public static void testAddIntermediateSprintToStage()  throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException, InvalidLocationException, InvalidStageStateException,  InvalidStageTypeException  {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.addIntermediateSprintToStage(1, 4.0));
		System.out.println(portal.addIntermediateSprintToStage(1, 3.0));

		for (int segment : portal.getStageSegments(1)) {
			System.out.println(segment);
		}
	}

	public static void testRemoveSegment() throws IDNotRecognisedException,  IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException, InvalidLocationException, InvalidStageStateException,  InvalidStageTypeException  {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.addIntermediateSprintToStage(1, 4.0));
		portal.removeSegment(1);
	}

	public static void testConcludeStagePreparation() throws InvalidStageStateException, InvalidLengthException, IllegalNameException,  InvalidNameException, IDNotRecognisedException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		portal.concludeStagePreparation(1);
	}

	public static void testGetStageSegments() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException, InvalidLocationException, InvalidStageStateException,  InvalidStageTypeException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createRace("kebab", "race to test"));
		System.out.println(portal.addStageToRace(1, "Stage1Test", "This is a stage test", 5, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN));
		System.out.println(portal.addCategorizedClimbToStage(1, 4.0, cycling.SegmentType.C4, 0.7, 2.0 ));
		System.out.println(portal.getStageSegments(1).length);
	}

	public static void testCreateTeam() throws IllegalNameException, InvalidNameException{
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));
	}

	public static void testRemoveTeam() throws IllegalNameException, InvalidNameException, IDNotRecognisedException{
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));
		portal.removeTeam(1);
	}

	public static void testGetTeams() throws IllegalNameException, InvalidNameException{
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createTeam("kebab2", "team to test"));
		System.out.println(portal.getTeams().length);
	}

	public static void testGetTeamRiders() throws IllegalNameException, InvalidNameException, IDNotRecognisedException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));

		System.out.println(portal.getTeamRiders(1).length);
	}

	public static void testCreateRider() throws IDNotRecognisedException, IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createRider(1, "kevin", 1901));

		System.out.println(portal.getTeamRiders(1).length);
	}

	public static void testRemoveRider() throws  IDNotRecognisedException, IllegalNameException, InvalidNameException {
		CyclingPortalInterface portal = new CyclingPortal();
		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createRider(1, "kevin", 1901));

		System.out.print("Num of riders: ");
		System.out.println(portal.getTeamRiders(1).length);
		portal.removeRider(1);
		System.out.print("Num of riders: ");
		System.out.println(portal.getTeamRiders(1).length);

	}

	public static void testSaveCyclingPortal() throws IOException, IllegalNameException, InvalidNameException {

		CyclingPortalInterface portal = new CyclingPortal();

		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createRace("kebab", "race to test"));

		portal.saveCyclingPortal("kebab");
	}

	public static void testLoadCyclingPortal() throws IOException, IllegalNameException, InvalidNameException, ClassNotFoundException {

		CyclingPortalInterface portal = new CyclingPortal();

		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createRace("kebab", "race to test"));

		portal.saveCyclingPortal("kebab");

		portal.loadCyclingPortal("kebab");
	}


	public static void testCreateClimb() {

		SegmentType newType = SegmentType.C4;

		Sprint newSeg = new Sprint(1);
		Climb newSegtwo = new Climb(1, newType,1 ,1);

		System.out.println(newSeg.toString());
		System.out.println(newSegtwo.toString());
	}

	public static void testTeamRemove() throws IllegalNameException, InvalidNameException, IDNotRecognisedException {

		CyclingPortalInterface portal = new CyclingPortal();

		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createTeam("kebab2", "team to test"));
		System.out.println(portal.createTeam("kebab3", "team to test"));

		portal.removeTeam(3);

		int [] teamIDs = portal.getTeams();
		for(int team : teamIDs){
			System.out.print(" " + Integer.toString(team));
		}


	}

	public static void testAddRemoveRider() throws IllegalNameException, IDNotRecognisedException, InvalidNameException {


		CyclingPortalInterface portal = new CyclingPortal();

		System.out.println(portal.createTeam("kebab", "team to test"));
		System.out.println(portal.createRider(1, "olive", 200));


		portal.removeRider(1);

	}

	public static void megaTest() throws IOException,NameNotRecognisedException, ClassNotFoundException, IllegalNameException, DuplicatedResultException, InvalidCheckpointsException, InvalidNameException, IDNotRecognisedException,
			InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException {
		CyclingPortalInterface portal = new CyclingPortal();

		portal.createRace("testRace" , "this is to test");

		// Initialise first stage
		portal.addStageToRace(1, "testStage1", "this is a test" , 20, LocalDateTime.now() , StageType.MEDIUM_MOUNTAIN);
		portal.addCategorizedClimbToStage(1, 6.0, SegmentType.HC, 5.0, 1.0);
		portal.addIntermediateSprintToStage(1, 7.0);

		// Initialise second race
		portal.addStageToRace(1, "testStage2", "this is a test" , 20, LocalDateTime.now() , StageType.FLAT);
		portal.addIntermediateSprintToStage(2, 10.0);
		portal.addIntermediateSprintToStage(2, 11.0);

		System.out.println(portal.viewRaceDetails(1));

		portal.createTeam("TestTeam", "TestDesc");
		portal.createRider(1, "TestRider", 2000);
		portal.createRider(1, "TestRider2", 2000);
		portal.createRider(1, "TestRider3", 2000);

		portal.concludeStagePreparation(1);
		portal.concludeStagePreparation(2);


		// Results for first stage
		portal.registerRiderResultsInStage(1, 1, LocalTime.of(12, 00, 00), LocalTime.of(12, 5, 00), LocalTime.of(12, 10, 00), LocalTime.of(12, 15, 00));
		portal.registerRiderResultsInStage(1, 2, LocalTime.of(12, 00, 00), LocalTime.of(12, 6, 00), LocalTime.of(12, 7, 00), LocalTime.of(12, 16, 00));
		portal.registerRiderResultsInStage(1, 3, LocalTime.of(12, 00, 00), LocalTime.of(12, 4, 00), LocalTime.of(12, 11, 00), LocalTime.of(12, 18, 00));

		// Results for second stage
		portal.registerRiderResultsInStage(2, 1, LocalTime.of(10, 00, 00), LocalTime.of(10, 10, 00), LocalTime.of(10, 20, 00), LocalTime.of(10, 30, 00));
		portal.registerRiderResultsInStage(2, 2, LocalTime.of(10, 00, 00), LocalTime.of(10, 9, 00), LocalTime.of(10, 21, 00), LocalTime.of(10, 29, 00));
		portal.registerRiderResultsInStage(2, 3, LocalTime.of(10, 00, 00), LocalTime.of(10, 11, 00), LocalTime.of(10, 19, 00), LocalTime.of(10, 31, 00));


		System.out.println("Riders ranked by times: ");

		int[] rankedRiders = portal.getRidersGeneralClassificationRank(1);

		for(int rider : rankedRiders) {
			System.out.println(rider);
		}

		System.out.println("Rider times: ");

		LocalTime[] adjusted = portal.getGeneralClassificationTimesInRace(1);
		for(LocalTime point : adjusted) {
			System.out.println(point.toString());
		}

		System.out.println("Point ranked by time: ");

		int[] points = portal.getRidersPointsInRace(1);
		for (int point : points) {
			System.out.println(point);
		}

		System.out.println("Mountain point ranked by time: ");

		int[] rankedPoints = portal.getRidersMountainPointsInRace(1);

		for (int point: rankedPoints) {
			System.out.println(point);
		}

		System.out.println("Point rank: ");

		int[] rankedPoints2 = portal.getRidersPointClassificationRank(1);

		for (int point: rankedPoints2) {
			System.out.println(point);
		}

		System.out.println("Mountain point rank: ");

		int[] rankedPoints3 = portal.getRidersMountainPointClassificationRank(1);

		for (int point: rankedPoints3) {
			System.out.println(point);
		}

	}


}
