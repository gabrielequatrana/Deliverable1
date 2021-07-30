package it.isw2.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import it.isw2.entity.Ticket;

public class CSVWriter {
	
	private static final String DELIM = ";";
	
	private CSVWriter() {
		
	}

	// Save on a CSV file the Process Control Chart data
	public static void printCSV(List<String> dates, List<Ticket> tickets, String projName) throws IOException {
		try (var fileWriter = new FileWriter("out/" + projName.toLowerCase() + ".csv")) {
			var line = "Date;NumNewFeature\n";
			fileWriter.append(line.replace(";", DELIM));
			
			for (String date : dates) {
				var count = 0;
				for (Ticket ticket : tickets) {
					if (ticket.getDate().equals(date)) {
						count++;
					}
				}
				
				fileWriter.append(date);
				fileWriter.append(DELIM);
				fileWriter.append(String.valueOf(count));
				fileWriter.append("\n");
			}
		}
	}
}
