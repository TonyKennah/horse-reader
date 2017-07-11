package org.kennah.horse.server.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Race implements Serializable{

	private static final long serialVersionUID = 1L;
	private String url;
	private String place;
	private String country;
	private String time;
	private String distance;
	private int distanceInYards;
	private String detail;
	private int runners;
	private List<Horse> horses;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getDistanceInYards() {
		return distanceInYards;
	}

	public void setDistanceInYards(int distanceInYards) {
		this.distanceInYards = distanceInYards;
	}

	public void setDistanceInYards(String distanceInYards) {
		int miles = 0;
		int furlongs = 0;
		int yards = 0;
		List<String> alldist = Arrays.asList(distanceInYards.split(" "));
		for (String s : alldist) {
			if (s.contains("m")) {
				miles = Integer.valueOf(s.replace("m", "").trim());
				miles = miles * 8 * 220;
			} else if (s.contains("f")) {
				furlongs = Integer.valueOf(s.replace("f", "").trim());
				furlongs = furlongs * 220;
			} else if (s.contains("y")) {
				yards = Integer.valueOf(s.replace("y", "").trim());
			}
		}
		this.distanceInYards = miles + furlongs + yards;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getRunners() {
		return runners;
	}

	public void setRunners(int runners) {
		this.runners = runners;
	}

	public List<Horse> getHorses() {
		return horses;
	}

	public void setHorses(List<Horse> horses) {
		this.horses = horses;
	}
	
	@Override
	public String toString() {
		return "\nRace [" + time + " " + place 
				+ ", runners=" + runners 
				+ ", distance=" + distance
				+ ", distanceInYards=" + distanceInYards 
				+ ", country=" + country  
				+ ", detail="+ detail
				+ ", url=" + url
				+ ", HORSES " + horses
				+ "]";
	}
}
