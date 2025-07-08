package com.zettel.controller;

import java.io.File;
import java.io.IOException;

import com.zettel.notemanager.NoteManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class EmptyTabController {

	private MainController mainController;

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void onNewNoteClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Create New Note");
		fileChooser.setInitialDirectory(new File(NoteManager.BASEURI));
		File newFile = fileChooser.showSaveDialog(null);
		if (newFile == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,"No File Created");
			alert.showAndWait();
			return;
		}
		boolean fileCreated = false;
		try {
			fileCreated = newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!fileCreated) {
			Alert alert = new Alert(Alert.AlertType.WARNING,"File Already Exist");
			alert.show();
			return;
		}
		mainController.refreshTreeView();
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
