package org.kennah.horse.server.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kennah.horse.server.model.Past;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormReader implements Callable<List<Past>>, Supplier<List<Past>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String url;

	/**
	 * @param url
	 */
	public FormReader(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public List<Past> get() {
		return scanPast();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public List<Past> call() throws Exception {
		return scanPast();
	}

	/**
	 * @return
	 */
	public List<Past> scanPast() {
		List<Past> past = new ArrayList<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			setAddedPast(past, doc);
		} catch (IOException e) {
			logger.error("   " + FormReader.class.getSimpleName() + "::scanPast() Couldn't connect " + url);
			scanPast();
		}
		return past;
	}

	/**
	 * @param past
	 * @param doc .
	 */
	public void setAddedPast(List<Past> past, Document doc) {
		Elements links = doc.select("a[href]");
		int count = 0;
		for (Element link : links) {
			if (link.toString().contains("racing/results/full-result/")) {
				count++;
				if (count <= 12) {
					List<String> details = Arrays.asList(link.parent().parent().text().split(" "));
					boolean beatenMatched = false;
					boolean trackMatched = false;
					boolean weightMatched = false;
					Past pastrace = new Past();
					if (details.get(1).matches("[A-Z][A-Z]") || details.get(1).matches("[A-Z]")) {
						pastrace.setPosition(details.get(1));
						pastrace.setDistBeaten("99.0");
						beatenMatched = true;
					}
					if (details.get(1).contains("/")) {
						pastrace.setPosition(details.get(1));
					}
					if (details.get(1).startsWith("1/")) {
						pastrace.setDistBeaten("0.0");
						beatenMatched = true;
					}

					for (String s : details) {
						if (!weightMatched)
							if (s.contains("-")) {
								pastrace.setWeight(s);
								weightMatched = true;
							}

						if (!beatenMatched)
							if (s.matches("[a-z]\\.[a-z]") && details.indexOf(s) > 5) {
								pastrace.setDistBeaten(s);
								beatenMatched = true;
							}

						if (!beatenMatched)
							if (s.matches("[a-z][a-z]") && details.indexOf(s) > 5) {
								pastrace.setDistBeaten(s);
								beatenMatched = true;
							}

						if (!beatenMatched)
							if (s.matches("\\d+\\.\\d+") && details.indexOf(s) > 5) {
								pastrace.setDistBeaten(s);
								beatenMatched = true;
							}

						if (!trackMatched)
							if (s.matches("[A-Z][a-z][a-z][a-z]?")) {
								pastrace.setCourse(s);
								pastrace.setDistance(details.get(details.indexOf(s) + 1));
								pastrace.setGoing(details.get(details.indexOf(s) + 2));
								int raceclass = 0;
								String tmp = details.get(details.indexOf(s) + 3);
								try {
									raceclass = Integer.valueOf(tmp);
								} catch (Exception e) {
									tmp = "XXXXX";
								}

								if (tmp.length() < 2)
									pastrace.setRaceClass(raceclass + "");
								else
									pastrace.setRaceClass("X");
								trackMatched = true;
							}
					}
					pastrace.setUrl("http://www.skysports.com" + link.attr("href"));
					pastrace.setDate(link.text());
					past.add(pastrace);
				} else {
					break;
				}
			}
		}
	}
}
