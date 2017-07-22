package src.shared.meetings;

import java.io.*;
import java.util.Comparator;

public class PastRaces implements Serializable {
	private static final long serialVersionUID = 1L;
	private String page;
	private String position;
	private String weight;
	private String track;
	private String going;
	private String raceDist;
	private String distOffWin = "20.00";
	private String penaltyValue = "0";
	private String raceClass;
	private String added = "0";
	private String date;
	private int tempAdjustedPV = 0;
	private int adjustedPV = 0;
	private int claim = 0;
	private boolean done = false;

	public PastRaces() {

	}

	public PastRaces(String page, String position, String weight, String track, String going, String raceDist,
			String distOffWin, String date, String raceClass) {
		this.page = page;
		this.position = position;
		this.weight = weight;
		this.track = track;
		this.going = going;
		this.raceDist = raceDist;
		this.distOffWin = distOffWin;
		this.date = date;
		this.raceClass = raceClass;
	}

	public String getDistOffWin() {
		return distOffWin;
	}

	public String getPage() {
		return page;
	}

	public String getPenaltyValue() {
		return penaltyValue;
	}

	public void setPenaltyValue(String penaltyValue) {
		this.penaltyValue = penaltyValue;
	}

	public String getDate() {
		return date;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getAdjustedPV() {
		return adjustedPV;
	}

	public void setAdjustedPV(int adjustedPV) {
		this.adjustedPV = adjustedPV;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGoing() {
		return going;
	}

	public void setGoing(String going) {
		this.going = going;
	}

	public String getPosition() {
		return position;
	}

	public String getRaceDist() {
		return raceDist;
	}

	public String getRaceClass() {
		return raceClass;
	}

	public void setRaceClass(String raceClass) {
		this.raceClass = raceClass;
	}

	public String getRacedate() {
		return date;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public String getTrack() {
		return track;
	}

	public String getWeight() {
		return weight;
	}

	public int getClaim() {
		return claim;
	}

	public void setClaim(int claim) {
		this.claim = claim;
	}

	public int getTempAdjustedPV() {
		return tempAdjustedPV;
	}

	public void setTempAdjustedPV(int tempAdjustedPV) {
		this.tempAdjustedPV = tempAdjustedPV;
	}

	public static Comparator<PastRaces> pastRaceComparator = new Comparator<PastRaces>() {

		public int compare(PastRaces s1, PastRaces s2) {
			int pv1 = s1.getAdjustedPV();
			int pv2 = s2.getAdjustedPV();
			return pv2 - pv1;
		}
	};

	@Override
	public String toString() {
		return "\n      PastRaces [page=" + page + ", position=" + position + ", weight=" + weight + ", track=" + track
				+ ", going=" + going + ", raceDist=" + raceDist + ", distOffWin=" + distOffWin + ", penaltyValue="
				+ penaltyValue + ", raceClass=" + raceClass + ", added=" + added + ", date=" + date
				+ ", tempAdjustedPV=" + tempAdjustedPV + ", adjustedPV=" + adjustedPV + ", claim=" + claim + ", done="
				+ done + "]";
	}

}
