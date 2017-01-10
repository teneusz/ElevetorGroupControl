package com.teneusz.io.ui;

import com.teneusz.io.fuzzy.logic.LinguisticVariables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Teneusz on 05.12.2016.
 */
public class Initialization extends Application {

    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
            Pane p = fxmlLoader.load();
            MainController fooController = fxmlLoader.getController();
            primaryStage.setResizable(false);
            fooController.initialize(10, 4, 900, 4);
            primaryStage.setTitle("Main window");
            primaryStage.setScene(new Scene(p));
            primaryStage.setOnCloseRequest(event -> {
                fooController.onClose();
            });


            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.print(ex.getMessage());
        }


    }

    public static void main(String... args) {
        launch(args);
    }

}
