package com.zettel.notemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class NoteManager {
	public static URI BASEURI;
//	public List<Note> availableNotes;
	public static Map<String,Note> availableNotes;

	public NoteManager() {
		System.out.println("hey");
		try {
			BASEURI = new URI("file:///C:/Users/wwwsi/Documents/notes");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		availableNotes = new HashMap<>();

		getNotes();
	}

	public Map<String,Note> getAvailableNotes() {
		return availableNotes;
	}

	public void getNotes() {
		File directory = new File(BASEURI);

		if (!directory.exists() || !directory.isDirectory()) {
			System.err.println("Invalid directory: " + BASEURI);
			return;
		}

		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				String title = file.getName();
				URI location = file.toURI();
				StringBuilder textContent = new StringBuilder();

				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = br.readLine()) != null) {
						textContent.append(line).append("\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Note n = new Note(title, textContent.toString().trim(), location);
				availableNotes.put(title,n);
			}
		}

	}

}
