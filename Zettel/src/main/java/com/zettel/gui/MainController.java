package com.zettel.gui;

import java.io.IOException;

import com.zettel.notemanager.Note;
import com.zettel.notemanager.NoteManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainController {

	@FXML
	private TreeView<String> treeView;
	@FXML
	private TabPane tabPane;

	@FXML
	private NoteManager noteManager;
	public void initialize() {
		noteManager = new NoteManager();
		setupTreeView();
	}

	private void setupTreeView() {
		TreeItem<String> rootItem = new TreeItem<>("Notes");
		rootItem.setExpanded(true);
		
		for (String title : NoteManager.availableNotes.keySet()) {
			TreeItem<String> note = new TreeItem<>(title);
			rootItem.getChildren().add(note);
		}
//		TreeItem<String> note1 = new TreeItem<>("Note 1");
//		TreeItem<String> note2 = new TreeItem<>("Note 2");
//		rootItem.getChildren().addAll(note1, note2);

		treeView.setRoot(rootItem);

		treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null && newVal != rootItem) {
				openNewTab(newVal.getValue());
			}
		});
	}

	private void openNewTab(String title) {
		// Check if tab already exists
		for (Tab tab : tabPane.getTabs()) {
			if (tab.getText().equals(title)) {
				tabPane.getSelectionModel().select(tab);
				return;
			}
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/TabContent.fxml"));
			StackPane content = loader.load();
			

			// Get the controller and set the content
			TabController tabController = loader.getController();
			Note n = NoteManager.availableNotes.get(title);
			tabController.setTitle(title);
			tabController.addActionListeners();
			tabController.textArea.setText(n.textContent);
			

			
			// Wrap the content in StackPane to make it fully expandable
			StackPane tabContainer = new StackPane(content);
			StackPane.setMargin(content, new Insets(0));
			

			// Create new tab
			Tab newTab = new Tab(title);
			newTab.setContent(tabContainer);
			newTab.setClosable(true);
			
			newTab.setOnCloseRequest(event -> {
	            n.save();
	            System.out.println("saved");
	        });

			tabPane.getTabs().add(newTab);
			tabPane.getSelectionModel().select(newTab);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
