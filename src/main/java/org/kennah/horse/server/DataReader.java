package org.kennah.horse.server;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.kennah.horse.server.model.Past;
import org.kennah.horse.server.model.Race;
import org.kennah.horse.server.readers.FormReader;
import org.kennah.horse.server.readers.HorseReader;
import org.kennah.horse.server.readers.PastRaceReader;
import org.kennah.horse.server.readers.RaceReader;
import org.kennah.horse.utils.GlobalProperties;
import org.kennah.horse.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataReader {

	private GlobalProperties global;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Object lock = new Object();
	private Map<String, List<Race>> allraces = new HashMap<>();
	private Set<String> active = new HashSet<>();

	/**
	 * 
	 */
	public DataReader() {
		logger.info("   " + DataReader.class.getSimpleName() + "::Constructor");
	}

	/**
	 * @param global
	 */
	@Autowired
	public void setGlobal(GlobalProperties global) {
		this.global = global;
	}

	/**
	 * Returns a List of Races 
	 * {@link URL}. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method always returns immediately, whether or not the 
	 * image exists. When this applet attempts to draw the image on
	 * the screen, the data will be loaded. The graphics primitives 
	 * that draw the image will incrementally paint on the screen. 
	 * @param date
	 * @param user
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException
	 */
	public List<Race> getTheRaces(String date, String user, long id)
			throws IOException, InterruptedException, ExecutionException, ParseException {

		if (allraces.keySet().contains(date)) {
			logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() Returning cached races " + id + " "
					+ date);
			return allraces.get(date);
		}

		List<Race> races = null;
		if (active.contains(date)) {
			synchronized (lock) {
				while (active.contains(date)) {
					logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() waiting...... " + id + " "
							+ date);
					lock.wait();
				}
			}
			logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() wakingup...... " + id + " " + date);
		} else {
			active.add(date);
			logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() added to active queue " + id + " "
					+ date);
			String revDate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MM-yyyy").parse(date));
			races = Utility.readObjectFile(revDate);
			logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() reading races from file " + id + " "
					+ date);
			if (races.size() < 1 && user.equals("TK")) {
				logger.info("   " + DataReader.class.getSimpleName()
						+ "::getTheRaces() not found in file >>> GENERATING! " + id + " " + date);
				races = new RaceReader(global.getTargeturl(), global.getTargetpath(), date).get();

				races.parallelStream()
					.peek(race -> race.setGoing(Race.transformGoing(race.getGoing())))
					.peek(race -> race.setHorses(new HorseReader(race.getUrl()).get()))
					.flatMap(race -> race.getHorses().parallelStream())
					.peek(horse -> horse.setPast(new FormReader(horse.getProfile()).get()))
					.flatMap(horse -> horse.getPast().parallelStream())
					.peek(past -> past.setWinner(new PastRaceReader(past.getUrl()).get()))
					.forEach(Past::correct);
				
				Utility.postPluckierFormat(races, revDate);
				Utility.postFileToServer(revDate, global);
				Utility.postOdds(revDate, global);
				Utility.writeObjectFile(races, revDate);
			}
			if (races.size() > 1)
				allraces.put(date, races);
			active.remove(date);
			logger.info("   " + DataReader.class.getSimpleName() + "::getTheRaces() removed from active queue " + id
					+ " " + date);
			synchronized (lock) {
				logger.info(
						"   " + DataReader.class.getSimpleName() + "::getTheRaces() notifyAll() " + id + " " + date);
				lock.notifyAll();
			}
		}
		return allraces.get(date);
	}
}