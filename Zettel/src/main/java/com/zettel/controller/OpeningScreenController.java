package com.zettel.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OpeningScreenController {
	@FXML
	private Button openButton;

	public void initialize() {
	}

	public void changeToMainScreen(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
		BorderPane root = loader.load();

		Scene scene = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		MainController controller = loader.getController();
		window.setScene(scene);
		window.show();
		window.setOnCloseRequest(e -> {
			controller.stopWatcher();
		});
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Zettel");
//        primaryStage.show();
	}
}
