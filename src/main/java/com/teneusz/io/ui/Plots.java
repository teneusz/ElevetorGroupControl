package com.teneusz.io.ui;

import com.teneusz.io.fuzzy.logic.EnergyUsage;
import com.teneusz.io.fuzzy.logic.LinguisticVariables;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Created by Teneusz on 26.12.2016.
 */
public class Plots {

    private final LinguisticVariables zm;
    private final Stage stage;

    public Plots(LinguisticVariables lv, Stage stage) {
        this.zm = lv;
        this.stage = stage;
    }

    public void initialize() {

        final NumberAxis xAxisWeight = new NumberAxis(500, 900, 50);
        final NumberAxis yAxisWeight = new NumberAxis();

        final NumberAxis xAxisPietra = new NumberAxis(0, 10, 1);
        final NumberAxis yAxisPietra = new NumberAxis();

        final CategoryAxis xAxisOutput = new CategoryAxis();
        final NumberAxis yAxisOutput = new NumberAxis();


        xAxisWeight.setLabel("Weight");
        yAxisWeight.setLabel("Fuzzy value");

        xAxisPietra.setLabel("Level");
        yAxisPietra.setLabel("Fuzzy value");

        final LineChart<Number, Number> weightChart = new LineChart<>(xAxisWeight, yAxisWeight);
        final LineChart<Number, Number> pietraChart = new LineChart<>(xAxisPietra, yAxisPietra);
        final BarChart<String, Number> outputChart = new BarChart<>(xAxisOutput, yAxisOutput);
        XYChart.Series weightLowSeries = new XYChart.Series();
        XYChart.Series weightAvgSeries = new XYChart.Series();
        XYChart.Series weightHighSeries = new XYChart.Series();

        weightLowSeries.setName("Weight Low");
        weightAvgSeries.setName("Weight Average");
        weightHighSeries.setName("Weight High");

        XYChart.Series pietraMalaSeries = new XYChart.Series();
        XYChart.Series pietraSredniaSeries = new XYChart.Series();
        XYChart.Series pietraDuzaSeries = new XYChart.Series();

        pietraMalaSeries.setName("Level low");
        pietraSredniaSeries.setName("Level average");
        pietraDuzaSeries.setName("Level high");

        XYChart.Series outputSeries = new XYChart.Series();

        for (int i = 0; i < zm.elevatorWeight.length; i++) {
            weightLowSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i], zm.elevatorWeightLow[i]));
            weightAvgSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i], zm.elevatorWeightAvg[i]));
            weightHighSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i], zm.elevatorWeightHigh[i]));
        }
        for (int i = 0; i < zm.pietra.length; i++) {
            pietraMalaSeries.getData().add(new XYChart.Data(zm.pietra[i], zm.pietraMala[i]));
            pietraSredniaSeries.getData().add(new XYChart.Data(zm.pietra[i], zm.pietraSrednia[i]));
            pietraDuzaSeries.getData().add(new XYChart.Data(zm.pietra[i], zm.pietraDuza[i]));
        }

        for (Map.Entry<EnergyUsage, Float> entry : zm.energyUsage.entrySet()) {
            outputSeries.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(weightChart, pietraChart, outputChart);

        Scene scene = new Scene(new ScrollPane(flowPane), 800, 600);
        weightChart.getData().addAll(weightLowSeries, weightAvgSeries, weightHighSeries);
        pietraChart.getData().addAll(pietraMalaSeries, pietraSredniaSeries, pietraDuzaSeries);
        outputChart.getData().add(outputSeries);
        weightChart.setMinWidth(750);
        pietraChart.setMinWidth(750);
        outputChart.setMinWidth(750);
        stage.setScene(scene);
        stage.show();
    }

}
