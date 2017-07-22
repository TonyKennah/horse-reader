package org.kennah.horse.server.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Race implements Serializable {

	private static final long serialVersionUID = 1L;
	private String url;
	private String place;
	private String country;
	private String time;
	private String going;
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

	public String getGoing() {
		return going;
	}

	public void setGoing(String going) {
		this.going = going;
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
	
	public static String transformGoing(String str){
		if(str==null)
			return "Gd";
    	str = str.trim();
        String rtnStr = "Gd";
        if(str.contains("Good to Soft")) rtnStr = "Gd/Sft";
        else if(str.contains("Soft to Heavy")) rtnStr = "Sft/Hvy";
        else if(str.contains("Yeilding to Soft")) rtnStr = "Yld/Sft";
        else if(str.contains("Good to Yeilding")) rtnStr = "Gd/Yld";
        else if(str.contains("Yeilding to Soft")) rtnStr = "Yld/Sft";
        else if(str.contains("Good to Firm")) rtnStr = "Gd/Fm";
        else if(str.contains("Standard")) rtnStr = "std";
        else if(str.contains("Soft")) rtnStr = "Sft";
        else if(str.contains("Yeilding")) rtnStr = "Yld";
        else if(str.contains("Heavy")) rtnStr = "Hvy";
        else if(str.contains("Firm")) rtnStr = "Fm";
        else if(str.contains("Good")) rtnStr = "Gd";
        else if(str.contains("Fast")) rtnStr = "Fast";
        else if(str.contains("Sloppy")) rtnStr = "Sloppy"; 
        return rtnStr;
    }

	@Override
	public String toString() {
		return "\nRace [" + time + " " + place + ", runners=" + runners + ", distance=" + distance + ", going=" + going
				+ ", distanceInYards=" + distanceInYards + ", country=" + country + ", detail=" + detail + ", url="
				+ url + ", HORSES " + horses + "]";
	}
}
