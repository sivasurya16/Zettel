package com.zettel.events;

import java.time.LocalDateTime; // import the LocalDate class
import java.time.Month;

public class Event {
	static int ctr = 1;
	int eventId;
	String eventName;
	LocalDateTime eventTime;
	boolean reminded;

	public Event(String eventName, int year, Month month, int dayOfMonth, int hour, int minute) {
		this.eventId = ctr;
		ctr++;
		this.eventName = eventName;
		this.eventTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
	}

	@Override
	public String toString() {
		return eventName;
	}

	public boolean remind() {
		LocalDateTime curTime = LocalDateTime.now();
		if (reminded) {
			return false;
		}
		if (eventTime.compareTo(curTime) <= 0) {
			System.out.println("Time to do this event : " + this.toString());
			reminded = true;
			return true;
		}
		return false;
	}

}
