package org.kennah.horse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.kennah.horse.server.DataReader;
import org.kennah.horse.utils.GlobalProperties;
import org.kennah.horse.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class HorseApplication implements CommandLineRunner {
	public final static Logger logger = LoggerFactory.getLogger("HorseApplication");
	@Autowired
	private DataReader reader;
	@Autowired
	private GlobalProperties global;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("   " + HorseApplication.class.getSimpleName() + "::main() - Application StartUp");
		SpringApplication.run(HorseApplication.class, args);
		logger.info("   " + HorseApplication.class.getSimpleName() + "::main() - Application Ready");
	}

	/* (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("   " + HorseApplication.class.getSimpleName() + "::run() CommandLineRunner");
		logger.info("   " + HorseApplication.class.getSimpleName() + "::main() \n" + Utility.getFace(global.getFaceurl()));
		LocalDate date = LocalDate.now().plusDays(0);
		String textfirst = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		logger.info("   " + HorseApplication.class.getSimpleName() + "::run() -> getTheRaces(" + textfirst + ",TK)");
		reader.getTheRaces(textfirst, "TK", 0);
	}
}