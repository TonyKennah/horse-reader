package org.kennah.horse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.kennah.horse.server.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class HorseApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DataReader reader;
	
	public static void main(String[] args) {
		SpringApplication.run(HorseApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		logger.info("   "+ HorseApplication.class.getSimpleName() + "::run() CommandLineRunner");
		LocalDate date = LocalDate.now().plusDays(0);
		String textfirst = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		logger.info("   "+ HorseApplication.class.getSimpleName() + "::run() -> getTheRaces(" +textfirst+")");
		reader.getTheRaces(textfirst, 0);
	}
}