package org.release.database.setup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ihtsdo.sql.parser.SqlFileParser;
import org.ihtsdo.xml.elements.Script;

public class ImportLogger {
	private static Logger logger = Logger.getLogger(ImportLogger.class);
    final private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
	public long startTime() {
		StringBuffer str = new StringBuffer();
		Date d = new Date();
		String startDate = "'" + dateFormatter.format(d) + "'";
		str.append("\tStartTime: " + startDate);
		logger.info(str.toString());
		return d.getTime();
	}

	
	
	public String endTime(long startTime) {
		StringBuffer str = new StringBuffer();
		Date d = new Date();
		String endDate = "'" + dateFormatter.format(d) + "'";

		String processingTime = compareTimes(startTime, d.getTime());
		
		str.append("\tEndTime: " + endDate);
		str.append("\t\tProcessingTime: " + processingTime);
		
		logger.info(str.toString());
		return str.toString();
	}
	
	
	
	public Date initializeProcess() {
		Date testingStartDate = new Date();
		String testingStartTime = "'" + dateFormatter.format(testingStartDate) + "'";
		logger.info("Starting ImportFileToDB Mojo at: " + testingStartTime );
		
		return testingStartDate;
	}

	public void finalizeProcess(Date testingStartDate) {
		Date testingEndDate = new Date();
		String testingEndTime = "'" + dateFormatter.format(testingEndDate) + "'";
		String testingProcessingTime = compareTimes(testingStartDate.getTime(), testingEndDate.getTime());
		logger.info("Completed ImportFileToDB Mojo at " + testingEndTime + " for a total processing time of: " + testingProcessingTime);
	}

	

	private String compareTimes(long startTime, long endTime) {
		long ellapsedTime = endTime - startTime;
		long time = ellapsedTime / 1000;
		String milliseconds = Integer.toString((int)ellapsedTime % 1000);
		String seconds = Integer.toString((int)(time % 60));
		String minutes = Integer.toString((int)((time % 3600) / 60));
		String hours = Integer.toString((int)(time / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
		
		return hours + ":" + minutes + ":" + seconds + ":" + milliseconds;
	}

	
	public void logError(String msg) {
		logger.error(msg);
	}

	public void logInfo(String msg) {
		logger.info(msg);
	}


}
