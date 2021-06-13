package it.isw2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import it.isw2.control.JiraController;
import it.isw2.entity.Ticket;
import it.isw2.utility.CSVWriter;

public class Main {
	
	private static String projName = "QPID";
	
	public static void main(String[] args) throws IOException, JSONException {
		
		List<Ticket> tickets = JiraController.getFixedNewFeatures(projName);
		List<String> dates = new ArrayList<>();

		// Count the occurrences of a month in the fixed new features list
		for (Ticket b : tickets) {
			if (!dates.contains(b.getDate())) {
				dates.add(b.getDate());
			}
		}
		
		// Fill list and sort
		fillMonthList(dates);
		Collections.sort(dates);

		// Print result on csv file
		CSVWriter.printCSV(dates, tickets, projName);
	}
	
	// Add to month list the dates not included in tickets
	private static void fillMonthList(List<String> dates) {
		String date = dates.get(0);
		String lastDate = dates.get(dates.size()-1);
		
		while (!date.equals(lastDate)) {
			var year = Integer.parseInt(date.substring(0, 4));
			var month = Integer.parseInt(date.substring(5));
			
			int newMonth = month+1;
			int newYear = year;
			if (newMonth == 13) {
				newYear++;
				newMonth = 1;
			}
			
			if (newMonth < 10) {
				date = newYear + "-0" + newMonth;
			} 
			else {
				date = newYear + "-" + newMonth;
			}
			
			if (!dates.contains(date)) {
				dates.add(date);
			}
		}
	}
}
