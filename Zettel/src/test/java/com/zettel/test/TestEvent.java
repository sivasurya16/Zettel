package com.zettel.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.zettel.events.Event;

class TestEvent {

	@Test
	void testRemind() {
		// Future event (no need to remind)
		Event e1 = new Event("Birthday", 2025, Month.MAY, 16, 0, 0);
		// Event needs to be reminded
		Event e2 = new Event("SEP class", 2025, Month.MARCH, 4, 9, 45);

		assertFalse(e1.remind());
		assertTrue(e2.remind());
		// Don't remind twice
		assertFalse(e2.remind());
	}

}
