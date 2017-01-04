package com.teneusz.io.ui;

import com.teneusz.io.exception.handel.ExceptionHandler;
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

    LinguisticVariables zm = new LinguisticVariables();

    Stage plotStage = new Stage();

    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
            Pane p = fxmlLoader.load();
            MainController fooController = fxmlLoader.getController();
            fooController.setTime(2);

            fooController.initialize(10, 4, 1200, 10);

            primaryStage.setScene(new Scene(p));

            new Plots(zm, plotStage).initialize();

            primaryStage.setOnCloseRequest(event -> {
                plotStage.close();
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
