package com.zettel.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Month;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.zettel.events.EventManager;

class TestEventManager {
	static EventManager manager;

	@BeforeAll
	static void setupBeforeClass() {
		manager = new EventManager();
		manager.addEvent("Birthday", 2025, Month.MAY, 16, 0, 0);
		manager.addEvent("SEP class", 2025, Month.MARCH, 4, 9, 45);
		manager.addEvent("Event to delete", 2027, Month.MARCH, 4, 0, 0);
	}
	
	@Test
	void testDelete() {
		assertTrue(manager.removeEvent("Event to delete"));
		assertFalse(manager.removeEvent("Event to delete"));
		assertFalse(manager.removeEvent("Non existing event"));
	}

}
