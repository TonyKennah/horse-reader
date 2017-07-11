package org.kennah.horse.server.readers;

import java.io.IOException;
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

public class RaceReader {
	
	private String TODAY;
	private String PATH;
	private String URI;
	
	public RaceReader(String uri, String path, String today){
		this.TODAY = today;
		this.PATH = path;
		this.URI = uri;
	}
	
	public List<Race> get(){
		Document doc = null;
		try {
			doc = Jsoup.connect(URI + PATH + TODAY).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> places = this.getImagesViaJSoup(doc);
		Map<String, Race> races = new HashMap<>();
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			if (link.attr("href").contains(TODAY) && link.attr("href").contains("racecards")
					&& !link.attr("href").endsWith(TODAY)) {
				Race r = new Race();
				if (link.text().matches("\\d\\d:\\d\\d")) {
					r.setUrl(URI + link.attr("href"));
					String message = capitalise(link.attr("href").split("/")[3]);
					r.setPlace(Character.toUpperCase(message.charAt(0)) + message.substring(1));
					r.setCountry(places.get(r.getPlace()));
					r.setTime(link.text());
					races.put(r.getUrl(), r);
				} else if (races.containsKey(URI + link.attr("href"))) {
					r = races.get(URI + link.attr("href"));
					r.setDetail(link.text());
					List<String> d = Arrays.asList(link.text().substring(link.text().lastIndexOf("("), link.text().length()).split(","));
					String runners = d.get(d.size() - 1).replace("(", "").replace(" runners)", "").trim();
					//System.out.println(r);
					if(d.size()<2){
						races.remove(URI + link.attr("href"));
						break;
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
	
	private String capitalise(String str){
		Pattern pattern = Pattern.compile("-\\w");
		Matcher matcher = pattern.matcher(str);
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, " " + matcher.group().toUpperCase().replace("-", ""));
		}
		matcher.appendTail(result);
		return result.toString();
	}

	private Map<String, String> getImagesViaJSoup(Document doc) {
		Map<String, String> flags = new HashMap<>();
		Elements elements = doc.getElementsByTag("IMG");
		for (Element s : elements)
			if (s.attr("src").contains("flags"))
				flags.put(s.parent().text(), s.attr("src").split("/")[5].replace(".png", "").replace("-", " "));
		return flags;
	}

}
