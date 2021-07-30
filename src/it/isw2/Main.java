package it.isw2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONException;

import it.isw2.control.CommitController;
import it.isw2.control.TicketController;
import it.isw2.entity.Ticket;
import it.isw2.utility.CSVWriter;
import it.isw2.utility.Utilities;

public class Main {
	
	private static final String PROJ_NAME = "QPID";
	private static String repoDir = "proj/" + PROJ_NAME.toLowerCase();
	private static String repo = repoDir + "/.git";
	
	public static void main(String[] args) throws IOException, JSONException {
		
		List<Ticket> tickets = null;
		List<RevCommit> commits = null;
		
		// Start program
		Utilities.logMsg("Starting program\n");
		
		// Clone selected repository
		Utilities.logMsg("Cloning repository\n");
		try {
			cloneProject(PROJ_NAME);
		} catch (GitAPIException e) {
			Utilities.logError(e);
		}
		
		// Get all project commits
		Utilities.logMsg("Getting commits\n");
		try {
			commits = CommitController.getAllCommits(Paths.get(repo));
		} catch (GitAPIException | IOException e) {
			Utilities.logError(e);
		}
		
		// Get all project tickets with at least one associated commit
		Utilities.logMsg("Getting new feature tickets\n");
		try {
			tickets = TicketController.getFixedNewFeatures(PROJ_NAME);
			CommitController.selectTicketsWithCommit(tickets, commits);
		} catch (IOException e) {
			Utilities.logError(e);
		}

		List<String> dates = new ArrayList<>();

		// Count the occurrences of a month in the fixed new features list
		Utilities.logMsg("Counting occurrences of a month\n");
		for (Ticket b : tickets) {
			if (!dates.contains(b.getDate())) {
				dates.add(b.getDate());
			}
		}
		
		// Fill list and sort
		fillMonthList(dates);
		Collections.sort(dates);

		// Print result on csv file
		Utilities.logMsg("Making file csv\n");
		CSVWriter.printCSV(dates, tickets, PROJ_NAME);
		
		// Stop program
		Utilities.logMsg("Stopping program\n");
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
	
	private static void cloneProject(String projName) throws GitAPIException {
		if (!Files.exists(Paths.get(repoDir))) {
			String url = "https://github.com/apache/" + projName.toLowerCase();
			Git git = Git.cloneRepository().setURI(url).setDirectory(new File(repoDir)).call();
			git.close();
		}
	}
}
