package org.kennah.horse.server.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kennah.horse.server.DataReader;
import org.kennah.horse.server.model.Past;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormReader implements Callable<List<Past>>, Supplier<List<Past>> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String url;
	
	public FormReader(String url) {
		this.url = url;
	}
	
	@Override
	public List<Past> get() {
		return scanPast();
	}

	@Override
	public List<Past> call() throws Exception {
		return scanPast();
	}
	
	public List<Past> scanPast(){
		List<Past> past = new ArrayList<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			setAddedPast(past, doc);
		} catch (IOException e) {
			logger.error("   "+ DataReader.class.getSimpleName() + "::scanPast() COULDNT READ VALID PAST " + url);
			scanPast();
		}
		return past;
	}

	public void setAddedPast(List<Past> past, Document doc) {
		Elements links = doc.select("a[href]");
		int count = 0; 
		for (Element link : links) {
			if (link.toString().contains("racing/results/full-result/")) {
				count++;
				if(count<=12){
					Past cp = new Past();
					cp.setUrl("http://www.skysports.com"+link.attr("href"));
					cp.setDate(link.text());
					past.add(cp);
				}
				else{
					break;
				}
			}
		}
	}
}