package org.kennah.horse.server.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kennah.horse.server.model.Horse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorseReader implements Callable<List<Horse>>, Supplier<List<Horse>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	String url;

	/**
	 * @param url
	 */
	public HorseReader(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public List<Horse> call() throws Exception {
		return scanHorse();
	}

	/* (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public List<Horse> get() {
		return scanHorse();
	}

	/**
	 * @return
	 */
	public List<Horse> scanHorse() {
		List<Horse> horses = new ArrayList<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("   " + HorseReader.class.getSimpleName() + "::scanHorse() Couldn't connect  " + url);
			scanHorse();
		}
		try
		{
			Elements test = doc.select("a[href]");
		}
		catch(Exception e)
		{
			logger.error("   " + HorseReader.class.getSimpleName() + "::scanHorse() NULL  " + url);
			scanHorse();
		}
		if(doc != null){
			Elements links = doc.select("a[href]");
			Set<String> checker = new HashSet<>();
			for (Element link : links) {
				if (link.toString().contains("form-profiles/horse") && checker.add(link.text())) {
					Horse ch = new Horse();
					ch.setName(link.text());
					scanMore(doc, ch);
					horses.add(ch);
				}
			}
		} 
		else 
		{
		    scanHorse();
		}
		return horses;
	}

	/**
	 * @param doc
	 * @param ch
	 */
	private void scanMore(Document doc, Horse ch) {
		String name = ch.getName();
		if (ch.getName().contains("(")) {
			name = ch.getName().substring(0, ch.getName().lastIndexOf("(")).trim();
		}
		name = name.replace("'", "\\S");
		Elements e = doc.select("a:matches(" + name + ")");
		for (Element ele : e) {
			if (ele.outerHtml().contains("form-profiles/horse") && ele.outerHtml().contains("v5-table-form")) {
				ch.setProfile("http://www.skysports.com"
						+ ele.outerHtml().replace("href=\"", "").replace("\"", "").split(" ")[1]);
				for (Element x : ele.siblingElements()) {
					ch.setLastRun(x.text());
				}
				List<String> details = new ArrayList<>();
				for (Element x : ele.parent().parent().siblingElements()) {
					details.add(x.text());
					if (x.outerHtml().contains("/tsilks/")) {
						ch.setSilks(x.outerHtml().replace("src=\"", "").replace("\"", "").split(" ")[3]);
					}
				}
				String[] test = details.get(0).split(" ");
				if (test.length > 0) {
					ch.setNumber(test[0]);
					ch.setDraw("");
				}
				if (test.length > 1)
					ch.setDraw(test[1]);

				ch.setForm(details.get(1));
				ch.setAge(details.get(3));
				String weight = details.get(4);
				if (weight.contains(" ")) {
					weight = weight.split(" ")[0];
				}
				ch.setWeight(weight);
				ch.setTrainer(details.get(5));
				String jock = details.get(6);
				if (jock.contains("(")) {
					String claim = jock.substring(jock.indexOf("("), jock.length());
					claim = claim.replace("(", "").replace(")", "");
					ch.setAllowance(claim);
					jock = jock.substring(0, jock.lastIndexOf("(") - 1);
				}
				ch.setJockey(jock);
			}
		}
	}
}
