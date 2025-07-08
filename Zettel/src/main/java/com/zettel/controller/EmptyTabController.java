package com.zettel.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.zettel.notemanager.NoteManager;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EmptyTabController {

	private MainController mainController;

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void onNewNoteClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Create New Note");
		fileChooser.setInitialDirectory(new File(NoteManager.BASEURI));
		ExtensionFilter extFilter = new ExtensionFilter("Markdown Files (*.md)", "*.md");
		fileChooser.getExtensionFilters().add(extFilter);
		File newFile = fileChooser.showSaveDialog(null);
		if (newFile == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "No File Created");
			alert.showAndWait();
			return;
		}
		if (!isInsideBaseDirectory(newFile)) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Create file within base directory");
			alert.show();
			return;
		}

		boolean fileCreated = false;
		try {
			fileCreated = newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!fileCreated) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "File Already Exist");
			alert.show();
			return;
		}
		mainController.refreshTreeView();
	}

	public boolean isInsideBaseDirectory(File file) {
		File baseDir = new File(NoteManager.BASEURI);
		try {
			Path basePath = baseDir.getCanonicalFile().toPath();
			Path filePath = file.getCanonicalFile().toPath();
			return filePath.startsWith(basePath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onChangeBaseDirectoryClicked() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Base Directory");
		File newBaseURL = directoryChooser.showDialog(null);
//		System.out.println(newBaseURL);
		if (newBaseURL != null) {
			NoteManager.changeBaseDirectory(newBaseURL);
			mainController.refreshTreeView();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Choose a valid Directory");
			alert.showAndWait();
		}
	}
}
