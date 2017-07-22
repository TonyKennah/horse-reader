package org.kennah.horse.server.model;

import java.io.Serializable;

public class Past implements Serializable {

	private static final long serialVersionUID = 1L;
	private String url;
	private String date;
	private String winner;
	private String position;
	private String weight;
	private String course;
	private String distance;
	private String going;
	private String raceClass;
	private String distBeaten;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getGoing() {
		return going;
	}

	public void setGoing(String going) {
		this.going = going;
	}

	public String getRaceClass() {
		return raceClass;
	}

	public void setRaceClass(String raceClass) {
		this.raceClass = raceClass;
	}

	public String getDistBeaten() {
		return distBeaten;
	}

	public void setDistBeaten(String distBeaten) {
		this.distBeaten = distBeaten;
	}

	public static void correct(Past p) {
		if (p.getDistBeaten() == null) {
			if (p.getPosition() == null) {
				//System.out.println(p);
				p.setPosition("5/??");
				
			}
			int pos = 999;
			try {
				pos = Integer.valueOf(p.getPosition().split("/")[0]);
			} catch (NumberFormatException e) {
				System.out.println("MASSIVE ERROR " + p);
			}
			p.setDistBeaten(pos + ".0");
		}
		
		if(p.getDistance() == null || p.getDistance().equals("")){
			p.setDistance("8f");
		}
		
		if(p.getCourse() == null || p.getCourse().equals("")){
			p.setCourse("Duno");
		}

		if(p.getGoing() == null || p.getGoing().equals("")){
			p.setGoing("Gd");
		}
		
		if (p.getDistBeaten().equals("")) {
			p.setDistBeaten("1.0");
		}
	}

	@Override
	public String toString() {
		return "\n          Past [url=" + url + ", date=" + date + ", winner=" + winner + ", position=" + position
				+ ", weight=" + weight + ", course=" + course + ", distance=" + distance + ", going=" + going
				+ ", raceClass=" + raceClass + ", distBeaten=" + distBeaten + "]";
	}

}