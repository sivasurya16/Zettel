package com.zettel.events;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
	public List<Event> events;

	public EventManager() {
		events = new ArrayList<>();
	}

	public void remindAll() {
		for (Event e : events) {
			e.remind();
		}
	}

	public void addEvent(String eventName, int year, Month month, int dayOfMonth, int hour, int minute) {
		Event e = new Event(eventName, year, month, dayOfMonth, hour, minute);
		events.add(e);
	}

	public boolean removeEvent(String eventName) {
		boolean flag = false;
		for (Event e : events) {
			if (e.eventName.equals(eventName)) {
				events.remove(e);
				flag = true;
				break;
			}
		}
		return flag;
	}

}
