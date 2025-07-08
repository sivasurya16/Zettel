package com.zettel.notemanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class Note {
	public String title;
	public String textContent;
	private URI location;
	private boolean updated;

	public Note(String title, String textContent, URI location) {
		this.title = title;
		this.textContent = textContent;
		this.location = location;
	}

	public void edit(String textContent) {
		this.textContent = textContent;
		this.updated = true;
		System.out.println("Edited");
//		System.out.println(textContent);
	}

	public void save() {
		if (!updated) {
			return;
		}
		File f = new File(location);
		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.write(textContent);
			fw.flush();
			fw.close();
			System.out.println("Written");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLocation() {
		return location.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Note other = (Note) obj;
		return Objects.equals(title, other.title);
	}

}
