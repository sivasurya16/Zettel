package com.zettel.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import com.zettel.notemanager.Note;
import com.zettel.notemanager.NoteManager;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {

	@FXML
	private TreeView<File> treeView;
	@FXML
	private TabPane tabPane;
	private Map<String, Tab> opened;

	private NoteManager noteManager;
	private volatile boolean running;
	private WatchService watchService;

	@FXML
	public void initialize() {
		noteManager = new NoteManager();
		opened = new HashMap<>();
		setupTreeView();
		setupTabPane();
		startWatcher();
	}

	private void setupTabPane() {
		Runnable addEmptyTab = () -> {
			try {
				FXMLLoader newLoader = new FXMLLoader(getClass().getResource("/view/emptyTab.fxml"));
				VBox newTabContent = newLoader.load();

				EmptyTabController emptyTabController = newLoader.getController();
				emptyTabController.setMainController(this);

				Tab newTab = new Tab("New Tab", newTabContent);
				newTab.setClosable(true);
				tabPane.getTabs().add(newTab);
				tabPane.getSelectionModel().select(newTab);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		addEmptyTab.run();
		// load empty tab when no tabs open
		tabPane.getTabs().addListener((ListChangeListener<? super Tab>) c -> {
//			System.out.println("Hmm");
			if (c.getList().isEmpty()) {
				addEmptyTab.run();
			}
		});

	}

	private String fileNameWithoutExtension(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			index = fileName.length();
		}
		return fileName.substring(0, index);
	}

	private void setupTreeView() {
		TreeItem<File> rootItem = NoteManager.rootNode;
		rootItem.setExpanded(true);

		treeView.setCellFactory(tv -> new TreeCell<>() {
			@Override
			protected void updateItem(File item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
				} else {
					String fileName = item.getName();
					if (fileName.isEmpty()) {
						fileName = item.getAbsolutePath();
					}
					setText(fileNameWithoutExtension(fileName));
				}
			}
		});

		treeView.setRoot(rootItem);

		treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null && newVal != rootItem && !opened.containsKey(newVal.getValue().toURI().toString())) {
				Tab newTab = openNewTab(newVal.getValue());
				opened.put(newVal.getValue().toURI().toString(), newTab);

			} else {
				if (newVal != null) {
					Tab newTab = opened.getOrDefault(newVal.getValue().toURI().toString(), null);
					if (newTab != null) {
						tabPane.getSelectionModel().select(newTab);
					}
				}
			}
			PauseTransition pause = new PauseTransition(Duration.millis(250));
			pause.setOnFinished(e -> treeView.getSelectionModel().clearSelection());
			pause.play();
		});
	}

	public void refreshTreeView() {
		noteManager.updateRootNode();
		treeView.setRoot(NoteManager.rootNode);
	}

	private Tab openNewTab(File file) {
		String title = fileNameWithoutExtension(file.getName());

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TabContent.fxml"));
			StackPane content = loader.load();

			// Get the controller and set the content
			TabController tabController = loader.getController();
			String text = noteManager.getNoteContent(file);
			Note n = new Note(title, text, file.toURI());

			tabController.setTitle(title);
			tabController.setNote(n);

			tabController.initializeView(text);

			// Create new tab
			Tab newTab = new Tab(title);
			newTab.setContent(content);
			newTab.setClosable(true);

			newTab.setOnCloseRequest(event -> {
				n.save();
				opened.remove(n.getLocation());
				System.out.println("saved " + n.title);
			});

			tabPane.getTabs().add(newTab);
			tabPane.getSelectionModel().select(newTab);

			return newTab;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void startWatcher() {
		Thread th = new Thread() {
			@Override
			public void run() {
				Path basePath = Path.of(NoteManager.BASEURI);
				running = true;
				try {
					watchService = FileSystems.getDefault().newWatchService();
					basePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_DELETE);

					while (running) {
						WatchKey watchKey = watchService.take();
						watchKey.pollEvents();
						Platform.runLater(() -> refreshTreeView());
						watchKey.reset();
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				} catch (ClosedWatchServiceException e) {
					System.out.println("Stopped Watcher");
				}
			}
		};

		th.start();
	}

	public void stopWatcher() {
		this.running = false;
		try {
			if (watchService != null) {
				watchService.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
