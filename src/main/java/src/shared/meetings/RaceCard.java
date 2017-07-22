package src.shared.meetings;

import java.util.*;
import java.io.*;

public class RaceCard implements Serializable {
	private static final long serialVersionUID = 1L;
	private String racePage;
	private String raceTime;
	private String raceDistance;
	private int raceDistanceAsNumber;
	private String raceType;
	private String raceGoing;
	private String racePV;
	private String racePlace;
	private int activeRace;

	private ArrayList<RaceRunner> runners;

	public RaceCard() {

	}

	public RaceCard(String racePage, String raceType) {
		this.racePage = racePage;
		this.raceTime = raceType.substring(0, 5);
		this.raceType = raceType;
	}

	public RaceCard(String racePage, String raceTime, String raceDistance, String raceType) {
		this.racePage = racePage;
		this.raceTime = raceTime;
		this.raceDistance = raceDistance;
		this.raceType = raceType;
	}

	public int getActiveRace() {
		return activeRace;
	}

	public void setActiveRace(int activeRace) {
		this.activeRace = activeRace;
	}

	public String getRaceDistance() {
		return raceDistance;
	}

	public int getRaceDistanceAsNumber() {
		return raceDistanceAsNumber;
	}

	public void setRaceDistanceAsNumber(int raceDistanceAsNumber) {
		this.raceDistanceAsNumber = raceDistanceAsNumber;
	}

	public String getRaceGoing() {
		return raceGoing;
	}

	public void setRaceDistance(String raceDistance) {
		this.raceDistance = raceDistance;
	}

	public String getRacePV() {
		return racePV;
	}

	public void setRacePV(String racePV) {
		this.racePV = racePV;
	}

	public void setRaceGoing(String raceGoing) {
		this.raceGoing = raceGoing;
	}

	public String getRacePage() {
		return racePage;
	}

	public String getRaceTime() {
		return raceTime;
	}

	public String getRaceType() {
		return raceType;
	}

	public ArrayList<RaceRunner> getRunners() {
		return runners;
	}

	public void setRunners(ArrayList<RaceRunner> runners) {
		this.runners = runners;
	}

	public String getRacePlace() {
		return racePlace;
	}

	public void setRacePlace(String racePlace) {
		this.racePlace = racePlace;
	}

	public void setRaceTime(String raceTime) {
		this.raceTime = raceTime;
	}

	public void setRaceType(String raceType) {
		this.raceType = raceType;
	}

	@Override
	public String toString() {
		return "\n  RaceCard [racePage=" + racePage + ", raceTime=" + raceTime + ", raceDistance=" + raceDistance
				+ ", raceDistanceAsNumber=" + raceDistanceAsNumber + ", raceType=" + raceType + ", raceGoing="
				+ raceGoing + ", racePV=" + racePV + ", racePlace=" + racePlace + ", activeRace=" + activeRace
				+ ", runners " + runners + "]";
	}

	// @Override
	// public String toString() {
	// return "\n" + raceTime+ " " + raceType + " " + raceGoing + " " + runners;
	// }

}
