package org.kennah.horse.server.readers;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PastRaceReader implements Callable<String>, Supplier<String> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String url;

	/**
	 * @param url
	 */
	public PastRaceReader(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public String get() {
		return getPastRaceDetails();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public String call() throws Exception {
		return getPastRaceDetails();
	}

	/**
	 * @return
	 */
	private String getPastRaceDetails() {
		String ret = "";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("   " + PastRaceReader.class.getSimpleName() + "::get() Couldn't connect " + url);
			return "";
		}
		String str = doc.select("strong").first().parent().text();
		if (str.startsWith("Winner")) {
			ret = str.split(" ")[1].replaceAll(",", "");
		}
		return ret;
	}
}