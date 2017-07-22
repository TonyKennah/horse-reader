package org.kennah.horse.server.readers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kennah.horse.server.model.Race;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaceReader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String TODAY;
	private String PATH;
	private String URI;

	/**
	 * @param uri
	 * @param path
	 * @param today
	 */
	public RaceReader(String uri, String path, String today) {
		this.TODAY = today;
		this.PATH = path;
		this.URI = uri;
	}

	/**
	 * @return
	 */
	public List<Race> get() {
		Elements links = getRacesFromDate(TODAY);
		links.addAll(getRacesFromDate(minusDay(TODAY)));
		Map<String, String> goings = getRacesGoing(TODAY);
		goings.putAll(getRacesGoing(minusDay(TODAY)));

		Map<String, Race> races = new HashMap<>();
		for (Element link : links) {
			Race r = new Race();
			if (link.text().matches("\\d\\d:\\d\\d") && !link.attr("href").trim().matches(".*?\\d{6}/$")) {
				r.setUrl(URI + link.attr("href"));
				String message = capitalise(link.attr("href").split("/")[3]);
				r.setPlace(Character.toUpperCase(message.charAt(0)) + message.substring(1));
				r.setGoing(goings.get(r.getPlace()));
				r.setTime(link.text());
				races.put(r.getUrl(), r);
			} else {
				String temp = URI + link.attr("href");
				if (races.containsKey(temp)) {
					r = races.get(URI + link.attr("href"));
					r.setDetail(link.text());
					List<String> d = Arrays.asList(
							link.text().substring(link.text().lastIndexOf("("), link.text().length()).split(","));
					String runners = d.get(d.size() - 1).replace("(", "").replace(" runners)", "").trim();
					if (d.size() < 2) {
						races.remove(URI + link.attr("href"));
						continue;
					}
					r.setDistance(d.get(d.size() - 2).trim());
					r.setDistanceInYards(r.getDistance());
					r.setRunners(Integer.valueOf(runners));
					races.put(r.getUrl(), r);
				}
			}
		}
		List<Race> raceList = races.values().stream().collect(Collectors.toList());
		Comparator<Race> sortByPlace = (p, o) -> p.getPlace().compareToIgnoreCase(o.getPlace());
		Comparator<Race> sortByTime = (p, o) -> p.getTime().compareToIgnoreCase(o.getTime());

		return raceList.stream().sorted(sortByPlace.thenComparing(sortByTime)).collect(Collectors.toList());
	}

	/**
	 * @param date
	 * @return
	 */
	private String minusDay(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).minusDays(1)
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	/**
	 * @param day
	 * @return
	 */
	public Elements getRacesFromDate(String day) {
		Elements returnLinks = new Elements();
		Document doc = null;
		try {
			doc = Jsoup.connect(URI + PATH + day).get();
		} catch (IOException e) {
			logger.error("   " + RaceReader.class.getSimpleName() + "::getRacesFromDate() Couldn't connect " + URI
					+ PATH + day);
		}
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").contains(TODAY) && link.attr("href").contains("racecards")
					&& !link.attr("href").endsWith(TODAY) && !link.text().matches("\\d\\d:\\d\\d.*?[A-Z].*?")) {
				returnLinks.add(link);
			}
		}
		return returnLinks;
	}

	/**
	 * @param day
	 * @return
	 */
	private Map<String, String> getRacesGoing(String day) {
		Map<String, String> ret = new HashMap<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(URI + PATH + day).get();
		} catch (IOException e) {
			logger.error("   " + RaceReader.class.getSimpleName() + "::getRacesFromDate() Couldn't connect " + URI
					+ PATH + day);
		}
		Elements e = doc.getElementsByClass("hdr-cpt");
		for (Element ele : e) {
			String place = ele.text();
			String going = ele.parent().nextElementSibling().text().trim();
			if (going.contains("("))
				going = going.substring(0, going.lastIndexOf("(") - 1).trim();
			if (going.equals("")) {
				going = "Good";
			} else {
				going = going.replace("Going: ", "");
			}
			ret.put(place, going);
		}
		return ret;
	}

	/**
	 * @param str
	 * @return
	 */
	private String capitalise(String str) {
		Pattern pattern = Pattern.compile("-\\w");
		Matcher matcher = pattern.matcher(str);
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, " " + matcher.group().toUpperCase().replace("-", ""));
		}
		matcher.appendTail(result);
		return result.toString();
	}
}
