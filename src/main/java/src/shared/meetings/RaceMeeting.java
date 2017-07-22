package src.shared.meetings;

import java.util.*;
import java.io.*;

public class RaceMeeting implements Serializable {

	private static final long serialVersionUID = 1L;
	private String meetingName;
	private String RacecardPage;
	private ArrayList<RaceCard> card;

	public RaceMeeting() {

	}

	public RaceMeeting(String name, String page) {
		this.meetingName = name;
		this.RacecardPage = page;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public String getRacecardPage() {
		return RacecardPage;
	}

	public ArrayList<RaceCard> getRaceCard() {
		return card;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public void setCard(ArrayList<RaceCard> card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return "\nRaceMeeting [meetingName=" + meetingName + ", RacecardPage=" + RacecardPage + ", card=" + card + "]";
	}
}
