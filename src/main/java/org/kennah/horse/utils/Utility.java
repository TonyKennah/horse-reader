package org.kennah.horse.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kennah.horse.server.model.Horse;
import org.kennah.horse.server.model.Past;
import org.kennah.horse.server.model.Race;
import org.kennah.horse.server.odds.BetfairFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import src.shared.meetings.PastRaces;
import src.shared.meetings.RaceCard;
import src.shared.meetings.RaceMeeting;
import src.shared.meetings.RaceRunner;

public class Utility {

	private final static Logger logger = LoggerFactory.getLogger(Utility.class.getName());

	/**
	 * @param springStuff
	 * @param fileName
	 */
	public static void postPluckierFormat(List<Race> springStuff, String fileName) {
		logger.info("   " + Utility.class.getSimpleName() + "::convert() start conversion...");
		ArrayList<RaceMeeting> pluckier = new ArrayList<>();
		Set<String> checkSet = new HashSet<>();
		for (Race r : springStuff) {
			if (checkSet.add(r.getPlace())) {
				RaceMeeting meet = new RaceMeeting(r.getPlace(), "");
				pluckier.add(meet);

				ArrayList<RaceCard> rc = new ArrayList<>();
				for (Race sr : springStuff) {
					if (meet.getMeetingName().equals(sr.getPlace())) {
						String det = sr.getDetail();
						String typ = det.substring(0, det.lastIndexOf("(") - 1);
						det = det.substring(det.lastIndexOf("("), det.length()).replace("(", "").replace(")", "");
						RaceCard card = new RaceCard("", sr.getTime(), det, typ);
						card.setRacePlace(sr.getPlace());
						card.setRaceGoing(sr.getGoing());
						rc.add(card);
					}
				}
				meet.setCard(rc);
			}
		}

		for (Race sr : springStuff) {
			for (RaceMeeting pr : pluckier) {
				for (RaceCard c : pr.getRaceCard()) {
					if (c.getRaceTime().equals(sr.getTime()) && c.getRacePlace().equals(sr.getPlace())) {
						ArrayList<RaceRunner> runners = new ArrayList<>();
						for (Horse h : sr.getHorses()) {
							RaceRunner runner = new RaceRunner(h.getName(), "", h.getNumber(), h.getLastRun(),
									h.getForm(), h.getWeight(), h.getAge(), "", h.getJockey(), h.getAllowance(),
									h.getTrainer());
							runner.setDraw(h.getDraw().replace("(", "").replace(")", ""));
							runner.setColours(h.getSilks());
							ArrayList<PastRaces> past = new ArrayList<>();

							for (Iterator<Past> it = h.getPast().iterator(); it.hasNext();) {
								Past s = it.next();
								if (s.getWinner().equals("")) {
									it.remove();
								}
							}

							for (Past p : h.getPast()) {
								PastRaces ptk = new PastRaces("", p.getPosition(), p.getWeight(), p.getCourse(),
										p.getGoing(), p.getDistance(), p.getDistBeaten(), p.getDate(),
										p.getRaceClass());
								ptk.setPenaltyValue(p.getWinner());
								past.add(ptk);
							}
							runner.setPastform(past);
							runners.add(runner);
						}
						c.setRunners(runners);
					}
				}
			}
		}
		logger.info("   " + Utility.class.getSimpleName() + "::convert() write Pluckier file " + fileName);
		writePluckierObjectFile(pluckier, fileName);
	}

	/**
	 * @param fileName
	 */
	public static void postFileToServer(String fileName, GlobalProperties global) {
		logger.info("   " + Utility.class.getSimpleName() + "::postFileToServer() posting " + fileName + ".data");
		File textFile = new File(fileName + ".data");

		if (textFile.length() > 500) {
			try {
				String boundary = Long.toHexString(System.currentTimeMillis());
				URLConnection connection = new URL(global.getHorseserver()).openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				OutputStream output = connection.getOutputStream();
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);
				writer.append("--" + boundary).append("\r\n");
				writer.append(
						"Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"")
						.append("\r\n");
				writer.append("Content-Type: text/plain; charset=" + "UTF-8").append("\r\n");
				writer.append("\r\n").flush();
				Files.copy(textFile.toPath(), output);
				output.flush();
				writer.append("\r\n").flush();
				writer.append("--" + boundary + "--").append("\r\n").flush();
				int responseCode = ((HttpURLConnection) connection).getResponseCode();
				logger.info(
						"   " + Utility.class.getSimpleName() + "::postFileToServer() server response " + responseCode);
				writer.close();
				output.close();
			} catch (Exception e) {
				System.out.println("EEEEEEEEk");
			}
		}
	}

	/**
	 * @param obj
	 * @param nameOfFile
	 */
	public static void writeObjectFile(List<Race> obj, String nameOfFile) {
		try (FileOutputStream f_out = new FileOutputStream(nameOfFile + "_S.data", false);
				ObjectOutputStream obj_out = new ObjectOutputStream(f_out);) {
			obj_out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param obj
	 * @param nameOfFile
	 */
	public static void writePluckierObjectFile(ArrayList<RaceMeeting> obj, String nameOfFile) {
		try (FileOutputStream f_out = new FileOutputStream(nameOfFile + ".data", false);
				ObjectOutputStream obj_out = new ObjectOutputStream(f_out)) {
			obj_out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nameOfFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Race> readObjectFile(String nameOfFile) {
		nameOfFile = nameOfFile + "_S.data";
		List<Race> meet = null;
		File file = new File(nameOfFile);
		if (file.exists()) {
			try (FileInputStream fin = new FileInputStream(file);
					ObjectInputStream objIn = new ObjectInputStream(fin);) {
				Object obj = objIn.readObject();
				if (obj instanceof List) {
					meet = (List<Race>) obj;
				}
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				System.out.println("Exception " + cnfe);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
				System.out.println("Exception " + fnfe);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println("Exception " + ioe);
			}
		} else {
			meet = new ArrayList<>();
		}
		return meet;
	}

	/**
	 * @param date
	 */
	public static void postOdds(String date, GlobalProperties global){
		new BetfairFace().setGlobal(global).postOdds(date);
	}
	
	/**
	 * @return
	 */
	public static String getFace(String url) {
		StringBuffer sb = new StringBuffer();
		try {
			URL face = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(face.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine + "\n");
			in.close();
			sb.deleteCharAt(sb.length()-1);
		} catch (Exception e) {
			System.out.println("EEEEEEK");
		}
		return sb.toString();
	}
}
