package org.kennah.horse.server;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.kennah.horse.server.model.HorseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HorseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	private DataReader reader;

	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException
	 */
	@RequestMapping("/")
	public ModelAndView modelAndViewAllRacesForToday()
			throws IOException, InterruptedException, ExecutionException, ParseException {
		LocalDate date = LocalDate.now().plusDays(0);
		String textfirst = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		logger.info("   " + HorseController.class.getSimpleName() + "::modelAndViewAllRacesForToday() " + textfirst);
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("version", "0.1");
		mav.addObject("date", textfirst);
		mav.addObject("races", reader.getTheRaces(textfirst, "", counter.incrementAndGet()));
		return mav;
	}

	/**
	 * @param date
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException
	 */
	@RequestMapping("/date")
	public ModelAndView modelAndViewAllRacesForDate(
			@RequestParam(value = "date", defaultValue = "10-07-2017") String date,
			@RequestParam(value = "user", defaultValue = "") String user)
			throws IOException, InterruptedException, ExecutionException, ParseException {
		logger.info(
				"   " + HorseController.class.getSimpleName() + "::modelAndViewAllRacesForDate() " + date + " " + user);

		ModelAndView mav = new ModelAndView("index");
		mav.addObject("version", "0.1");
		mav.addObject("date", date);
		mav.addObject("user", user);
		mav.addObject("races", reader.getTheRaces(date, user, counter.incrementAndGet()));
		return mav;
	}

	/**
	 * @param date
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException
	 */
	@RequestMapping("/horses")
	public @ResponseBody HorseResponse restAllRacesForDate(
			@RequestParam(value = "date", defaultValue = "10-07-2017") String date,
			@RequestParam(value = "user", defaultValue = "") String user)
			throws IOException, InterruptedException, ExecutionException, ParseException {
		logger.info("   " + HorseController.class.getSimpleName() + "::restAllRacesForDate() " + date);

		return new HorseResponse(counter.get(), reader.getTheRaces(date, user, counter.incrementAndGet()));
	}
}