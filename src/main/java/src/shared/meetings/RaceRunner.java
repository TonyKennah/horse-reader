package src.shared.meetings;

import java.util.*;
import java.io.*;

public class RaceRunner implements Serializable {
	private static final long serialVersionUID = 1L;
	private int rank;
	private int movement = 0;
	private String name;
	private String page;
	private String number;
	private String age;
	private String jockey;
	private String allowance = "0";
	private int rating;
	private int copyrating;
	private int lowest;
	private int highest;
	private String draw;
	private String lastRun;
	private String form = "";
	private String weightToday;
	private String weightExtra = "";
	private String colours = "";
	private ArrayList<PastRaces> pastform = new ArrayList<PastRaces>();
	private List<String> odds = new ArrayList<String>();
	private String jockeyprofile = "";
	private String trainer = "";
	private String jt_ratingURL = "http://www.pluckier.co.uk/blank.gif";

	public RaceRunner() {

	}

	public RaceRunner(String name, String page, String number, String lastRun, String form, String weight, String age,
			String jockeyprofile, String jockey, String allowance, String trainer) {
		this.name = name;
		this.page = page;
		this.number = number;
		this.lastRun = lastRun;
		this.form = form;
		this.weightToday = weight;
		this.jockeyprofile = jockeyprofile;
		this.jockey = doRationalise(jockey);
		if (allowance == null) {
			this.allowance = "0";
		} else {
			this.allowance = allowance;
		}
		this.age = age;
		this.trainer = doRationalise(trainer);
		this.odds.add(0, "0");
	}

	public RaceRunner(String colours, String name, String page, String number, String draw, String form) {
		this.colours = colours;
		this.name = name;
		this.page = page;
		this.number = number;
		this.setDraw(draw);
		this.form = form;
		this.odds.add(0, "0");
	}

	private String doRationalise(String str) {
		String firstname = "";
		String last = "";
		String fin = "";
		String[] result = str.split("\\s");
		if (result.length > 1) {
			firstname = result[0];
			if (result[0].length() > 1) {
				fin = result[0].substring(0, 1);
			} else if (result[0].length() == 1) {
				fin = result[0];
			} else {
				fin = "X";
			}
			last = result[result.length - 1];
			if (!firstname.toLowerCase().equals("mr") && !firstname.toLowerCase().equals("mrs")
					&& !firstname.toLowerCase().equals("miss")) {
				if (firstname.toLowerCase().equals("sir")) {
					fin = result[1].substring(0, 1);
				}
			} else {
				fin = result[1].substring(0, 1);
			}
			if (last.toLowerCase().equals("jnr") || last.toLowerCase().equals("snr")) {
				last = result[result.length - 2];
			}
		}
		return fin + " " + last;
	}

	public String getTrainer() {
		return trainer;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getMovement() {
		return movement;
	}

	public void setMovement(int movement) {
		this.movement = movement;
	}

	public String getJt_ratingURL() {
		return jt_ratingURL;
	}

	public void setJt_ratingURL(String jt_ratingURL) {
		this.jt_ratingURL = jt_ratingURL;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public String getJockeyprofile() {
		return jockeyprofile;
	}

	public void setJockeyprofile(String jockeyprofile) {
		this.jockeyprofile = jockeyprofile;
	}

	public String getJockey() {
		return jockey;
	}

	public void setJockey(String jockey) {
		this.jockey = jockey;
	}

	public String getAllowance() {
		return allowance;
	}

	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}

	public String getLastRun() {
		return lastRun;
	}

	public void setOdds(List<String> odds) {
		this.odds = odds;
	}

	public List<String> getAllOdds() {
		return odds;
	}

	public String getOdds() {
		if (odds.size() == 0) {
			odds.add(0, "0");
		}
		return odds.get(0);
	}

	public List<String> getFullOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds.add(0, odds);
	}

	public String getForm() {
		return form;
	}

	public String getColours() {
		return colours;
	}

	public void setColours(String colours) {
		this.colours = colours;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		String retVal = "0";
		if (this.number != null) {
			if (this.number.contains("A") || this.number.contains("B")) {
				retVal = number.substring(0, number.length() - 1);
			} else {
				retVal = number;
			}
		}
		return retVal;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getLowest() {
		return lowest;
	}

	public void setLowest(int lowest) {
		this.lowest = lowest;
	}

	public int getHighest() {
		return highest;
	}

	public void setHighest(int highest) {
		this.highest = highest;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getWeightExtra() {
		return weightExtra;
	}

	public void setWeightExtra(String weightExtra) {
		this.weightExtra = weightExtra;
	}

	public String getWeightToday() {
		return weightToday;
	}

	public void setWeightToday(String weightToday) {
		this.weightToday = weightToday;
	}

	public int getCopyrating() {
		return copyrating;
	}

	public void setCopyrating(int copyrating) {
		this.copyrating = copyrating;
	}

	public String getPage() {
		return page;
	}

	public ArrayList<PastRaces> getPastform() {
		return pastform;
	}

	public void setPastform(ArrayList<PastRaces> pastform) {
		this.pastform = pastform;
	}

	public int getFormAsInt() {
		int totalForm = 0;
		int intform = 0;
		for (int i = 0; i < 3 && i < this.form.length(); i++) {
			if (Character.isDigit(this.form.charAt(this.form.length() - 1 - i))) {
				intform = Integer.parseInt((this.form.charAt(this.form.length() - 1 - i) + ""));
				switch (intform) {
				case 1:
					totalForm += 5;
					break;
				case 2:
					totalForm += 3;
					break;
				case 3:
					totalForm += 1;
					break;
				case 4:
					totalForm += 1;
					break;
				default:
					totalForm += 0;
					break;
				}
			}

		}
		return totalForm;
	}

	public void setDraw(String draw) {
		if (draw.equals("0")) {
			this.draw = "";
		} else {
			this.draw = "(" + draw + ")";
		}
	}

	public String getDraw() {
		return draw;
	}

	@Override
	public String toString() {
		return "\n    RaceRunner [rank=" + rank + ", movement=" + movement + ", name=" + name + ", page=" + page
				+ ", number=" + number + ", age=" + age + ", jockey=" + jockey + ", allowance=" + allowance
				+ ", rating=" + rating + ", copyrating=" + copyrating + ", lowest=" + lowest + ", highest=" + highest
				+ ", draw=" + draw + ", lastRun=" + lastRun + ", form=" + form + ", weightToday=" + weightToday
				+ ", weightExtra=" + weightExtra + ", colours=" + colours + ", odds=" + odds + ", jockeyprofile="
				+ jockeyprofile + ", trainer=" + trainer + ", jt_ratingURL=" + jt_ratingURL + ", pastform=" + pastform
				+ "]";
	}

	public static Comparator<RaceRunner> raceRacerComparator = new Comparator<RaceRunner>() {

		public int compare(RaceRunner s1, RaceRunner s2) {
			int pv1 = 0, pv2 = 0;
			if (s1.getPastform().size() > 0) {
				pv1 = s1.getPastform().get(0).getAdjustedPV();
			}
			if (s2.getPastform().size() > 0) {
				pv2 = s2.getPastform().get(0).getAdjustedPV();
			}
			return pv2 - pv1;
		}
	};

}
