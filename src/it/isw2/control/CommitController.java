package it.isw2.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import it.isw2.entity.Ticket;

public class CommitController {
	
	private CommitController() {
		
	}
	
	// Get commits of a repository
	public static List<RevCommit> getAllCommits(Path repoPath) throws IOException, GitAPIException {
		List<RevCommit> commits = new ArrayList<>();
		try (Git git = Git.open(repoPath.toFile())) {
			Iterable<RevCommit> logs = git.log().all().call();
			for (RevCommit commit : logs) {
				commits.add(commit);
			}
		}

		return commits;
	}
	
	// Filter tickets without associated commit
	public static void selectTicketsWithCommit(List<Ticket> tickets, List<RevCommit> commits) {
		
		List<Ticket> goodTickets = new ArrayList<>();

		for (Ticket ticket : tickets) {
			String ticketID = ticket.getId();
			
			for (RevCommit commit : commits) {
				String message = commit.getFullMessage();
				if (message.contains(ticketID +",") || message.contains(ticketID +"\r") || message.contains(ticketID +"\n") 
						|| message.contains(ticketID + " ") || message.contains(ticketID +":") || message.contains(ticketID +".")
						|| message.contains(ticketID + "/") || message.endsWith(ticketID) || message.contains(ticketID + "]")
						|| message.contains(ticketID+"_") || message.contains(ticketID + "-") || message.contains(ticketID + ")")) {
					goodTickets.add(ticket);
				}
			}
		}
		
		// Remove ticket without associated commit
		Iterator<Ticket> ticket = tickets.iterator();
		while (ticket.hasNext()) {
			Ticket t = ticket.next();
			if (!goodTickets.contains(t)) {
				ticket.remove();
			}
		}
	}
}
