package org.kennah.horse.server.odds;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.kennah.horse.utils.GlobalProperties;
import org.kennah.horse.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.betfair.aping.api.ApiNgJsonRpcOperations;
import com.betfair.aping.api.ApiNgOperations;
import com.betfair.aping.entities.EventTypeResult;
import com.betfair.aping.entities.MarketBook;
import com.betfair.aping.entities.MarketCatalogue;
import com.betfair.aping.entities.MarketFilter;
import com.betfair.aping.entities.PriceProjection;
import com.betfair.aping.entities.Runner;
import com.betfair.aping.entities.RunnerCatalog;
import com.betfair.aping.entities.TimeRange;
import com.betfair.aping.enums.MarketProjection;
import com.betfair.aping.enums.MarketSort;
import com.betfair.aping.enums.MatchProjection;
import com.betfair.aping.enums.OrderProjection;
import com.betfair.aping.enums.PriceData;
import com.betfair.aping.exceptions.APINGException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BetfairFace{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private GlobalProperties global;
	private String session = "";
	//private String status = "";
	private ApiNgOperations jsonOperations = ApiNgJsonRpcOperations.getInstance();
	private HashMap<Long, MyRunner> mine = new HashMap<Long, MyRunner>();
	
	/**
	 * @param date
	 */
	public void postOdds(String date){
		betfairLogin();
		String fileCalled = date+"-ODDSlatest.data";
		File file = createTheFile(fileCalled ,start(date));
		Utility.postFileToServer(file.getName().replace(".data", ""), global);
		betfairLogout();
	}
	
	/**
	 * @param global
	 */
	public BetfairFace setGlobal(GlobalProperties global) {
		this.global = global;
		return this;
	}
	
	/**
	 * 
	 */
	private void betfairLogin()
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			keyStore.load(new FileInputStream(new File("client-2048.p12")), global.getCtpw().toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, global.getCtpw().toCharArray());
			KeyManager[] keyManagers = kmf.getKeyManagers();
			ctx.init(keyManagers, null, new SecureRandom());
			SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());
			ClientConnectionManager manager = httpClient.getConnectionManager();
			manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
			HttpPost httpPost = new HttpPost("https://identitysso.betfair.com/api/certlogin");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", global.getBfun()));
			nvps.add(new BasicNameValuePair("password", global.getBfpw()));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpPost.setHeader("X-Application", global.getAppid());
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				String responseString = EntityUtils.toString(entity);
				responseString = responseString.replace("}", "").replace("{", "").replace("\"", "");
				String[] params = responseString.split(",");
				for (String s : params)
				{
					String[] pv = s.split(":");
					for (int i = 0; i < pv.length; i++)
					{
						if (pv[i].equals("sessionToken"))
						{
							session = pv[i + 1];
						}
						if (pv[i].equals("loginStatus"))
						{
							//status = pv[i + 1];
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Eception Caught : " + e.getMessage());
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * @param date
	 * @return
	 */
	private HashMap<Long, MyRunner> start(String date)
	{
		//this.applicationKey = appid;
		//this.sessionToken = session;
		try
		{
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();
			Set<String> eventTypeIds = new HashSet<String>();
			System.out.println("Whats this ");
			System.out.println(global.getAppid());
			List<EventTypeResult> r = jsonOperations.listEventTypes(marketFilter, global.getAppid(), session);

			for (EventTypeResult eventTypeResult : r)
			{
				if (eventTypeResult.getEventType().getName().equals("Horse Racing"))
				{
					eventTypeIds.add(eventTypeResult.getEventType().getId().toString());
				}
			}
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			Date end = cal.getTime();
			if (!date.equals(""))
			{
				String[] params = date.split("-");
				cal.set(Calendar.YEAR, Integer.parseInt(params[0]));
				cal.set(Calendar.MONTH, Integer.parseInt(params[1]) - 1);
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(params[2]));
				cal.set(Calendar.HOUR_OF_DAY, 01);
				now = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				end = cal.getTime();
			}

			TimeRange time = new TimeRange();
			time.setFrom(now);
			time.setTo(end);
			Set<String> countries = new HashSet<String>();
			countries.add("GB");
			countries.add("IE");
			countries.add("ZA");
			countries.add("FR");
			countries.add("AE");
			countries.add("US");
			// countries.add("HKG");
			Set<String> typesCode = new HashSet<String>();
			typesCode.add("WIN");
			marketFilter = new MarketFilter();
			marketFilter.setEventTypeIds(eventTypeIds);
			marketFilter.setMarketStartTime(time);
			marketFilter.setMarketCountries(countries);
			marketFilter.setMarketTypeCodes(typesCode);
			marketFilter.setInPlayOnly(false);
			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.EVENT);
			marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
			String maxResults = "1000";
			List<MarketCatalogue> marketCatalogueResult = jsonOperations.listMarketCatalogue(marketFilter, marketProjection, MarketSort.FIRST_TO_START, maxResults, global.getAppid(), session);
			for (MarketCatalogue mc : marketCatalogueResult)
			{
				printMarketCatalogue(mc);
			}

			int loops = (marketCatalogueResult.size() / 20) + 1;
			ArrayList<List<MarketCatalogue>> chunked = new ArrayList<List<MarketCatalogue>>();
			for (int i = 0; i < loops; i++)
			{
				List<MarketCatalogue> tobeused = marketCatalogueResult.subList(Math.max(marketCatalogueResult.size() - 20, 0), marketCatalogueResult.size());
				List<MarketCatalogue> chunk = new ArrayList<MarketCatalogue>(tobeused);
				tobeused.clear();
				chunked.add(chunk);
			}

			for (int i = 0; i < loops; i++)
			{
				List<String> marketIds = new ArrayList<String>();
				for (MarketCatalogue mc : chunked.get(i))
				{
					marketIds.add(mc.getMarketId());
				}
				PriceProjection priceProjection = new PriceProjection();
				Set<PriceData> priceData = new HashSet<PriceData>();
				priceData.add(PriceData.EX_BEST_OFFERS);
				priceProjection.setPriceData(priceData);
				OrderProjection orderProjection = null;
				MatchProjection matchProjection = null;
				String currencyCode = null;
				List<MarketBook> marketBookReturn = jsonOperations.listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode, global.getAppid(), session);
				for (MarketBook mb : marketBookReturn)
				{
					printBookCatalogue(mb);
				}
			}
		}
		catch (APINGException apiExc)
		{
			System.out.println("\n\n\n\n\nAPINGException!!!!!!!!!!!\n\n\n\n\n HERE WHATS CAUGHT: " + apiExc.toString());
		}
		return mine;
	}
	
	/**
	 * @param mk
	 */
	private void printMarketCatalogue(MarketCatalogue mk)
	{
		List<RunnerCatalog> runners = mk.getRunners();
		if (runners != null)
		{
			for (RunnerCatalog rCat : runners)
			{
				//System.out.println(rCat.getRunnerName());
				String name = rCat.getRunnerName();
				if (Character.isDigit(rCat.getRunnerName().charAt(1)))
				{
					name = rCat.getRunnerName().substring(4, rCat.getRunnerName().length());
				}
				else if (Character.isDigit(rCat.getRunnerName().charAt(0)))
				{
					name = rCat.getRunnerName().substring(3, rCat.getRunnerName().length());
				}
				mine.put(rCat.getSelectionId(), new MyRunner(name));
			}
		}
	}

	/**
	 * @param mb
	 */
	private void printBookCatalogue(MarketBook mb)
	{
		List<Runner> runners = mb.getRunners();
		if (runners != null)
		{
			for (Runner rCat : runners)
			{
				if (rCat.getEx().getAvailableToBack().size() > 0)
				{
					mine.get(rCat.getSelectionId()).setOdds(rCat.getEx().getAvailableToBack().get(0).getPrice());
				}
			}
		}
	}

	/**
	 * 
	 */
	private void betfairLogout()
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try
		{
			HttpPost httpPost = new HttpPost("https://identitysso.betfair.com/api/logout");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("X-Authentication", session);
			httpPost.setHeader("X-Application", global.getAppid());
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				String responseString = EntityUtils.toString(entity);
				responseString = responseString.replace("}", "").replace("{", "").replace("\"", "");
				String[] params = responseString.split(",");
				for (String s : params)
				{
					String[] pv = s.split(":");
					for (int i = 0; i < pv.length; i++)
					{
						if (pv[i].equals("status"))
						{
							//status = pv[i + 1];
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Eception Caught : " + e.getMessage());
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * @param nameOfFile
	 * @param bd
	 * @return
	 */
	private File createTheFile(String nameOfFile, HashMap<Long, MyRunner> bd)
	{
		File file = new File(nameOfFile);
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// test
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(bd);
			int length = baos.toByteArray().length;
			logger.info("   " + BetfairFace.class.getSimpleName() + "::createTheFile() length of file is: " + length + " bytes");
			if (length > 500 && bd instanceof HashMap)
			{
				for (Long keys : bd.keySet())
				{
					Double odd = 0.0;
					if (mine.get(keys).getOdds() != null)
					{
						odd = mine.get(keys).getOdds();
					}
					bw.write(mine.get(keys).getName() + "#" + odd + "\n");
				}
				bw.close();
				// System.out.println("-----CREATED----");

			}
			else
			{
				logger.error("   " + BetfairFace.class.getSimpleName() + "::createTheFile() file is too shot: " + length + " bytes");
			}
			fw.close();
		}
		catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
			System.out.println("Exception " + fnfe);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println("Exception " + ioe);
		}
		return file;
	}
}
