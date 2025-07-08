package com.zettel;

import com.zettel.controller.TabController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpeningScreen.fxml"));
//        System.out.println(Main.class.getResource("/view/OpeningScreen.fxml"));
		AnchorPane root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Zettel");
		primaryStage.show();
		Platform.runLater(() -> {
			for (int i = 0; i < 10; i++)
				TabController.generateHtmlWarmup();
		});
	}

	public static void main(String[] args) {
//    	NoteManager m = new NoteManager();
		launch(args);
	}
}
