package it.isw2.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.isw2.entity.Ticket;
import it.isw2.utility.Utilities;

public class TicketController {

	private TicketController() {

	}

	/**
	 * Create a list of Ticket based on the Rest query
	 * @param projName
	 * @return tickets
	 * @throws JSONException
	 * @throws IOException
	 */
	public static List<Ticket> getFixedNewFeatures(String projName) throws JSONException, IOException {

		Integer j = 0;
		Integer i = 0;
		Integer total = 1;

		List<Ticket> tickets = new ArrayList<>();
		while (i < total) {
			j = i + 1000;

			// Rest API Query to get all the Fixed New Features
			String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22" + projName
					+ "%22AND%22issueType%22=%22New%20Feature%22AND(%22status%22=%22closed%22OR"
					+ "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
					+ i.toString() + "&maxResults=" + j.toString();

			// Get the Json Object
			JSONObject json = Utilities.readJsonFromUrl(url);
			JSONArray issues = json.getJSONArray("issues");
			total = json.getInt("total");

			// Create the Ticket Object from JSON and add it to the list
			for (; i < total && i < j; i++) {
				String date = issues.getJSONObject(i % 1000).getJSONObject("fields").get("resolutiondate").toString()
						.substring(0, 7);
				String key = issues.getJSONObject(i % 1000).get("key").toString();
				Ticket entry = new Ticket(key, date);
				tickets.add(entry);
			}
		}

		// Order the Tickets in the List comparing on the Resolution Date
		Comparator<Ticket> ticketComparator = Comparator.comparing(Ticket::getDate);
		tickets.sort(ticketComparator);
		return tickets;
	}
}
