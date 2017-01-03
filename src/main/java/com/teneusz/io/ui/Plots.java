package com.teneusz.io.ui;

import com.teneusz.io.fuzzy.logic.LinguisticVariables;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Teneusz on 26.12.2016.
 */
public class Plots {

    private final LinguisticVariables zm;
    private final Stage stage;

    public Plots(LinguisticVariables lv, Stage stage)
    {
        this.zm = lv;
        this.stage = stage;
    }

    public void initialize()
    {

        final NumberAxis xAxisWeight = new NumberAxis();
        final NumberAxis yAxisWeight = new NumberAxis();

        final NumberAxis xAxisPietra = new NumberAxis();
        final NumberAxis yAxisPietra = new NumberAxis();

        xAxisWeight.setLabel("Weight");
        yAxisWeight.setLabel("Fuzzy value");

        xAxisPietra.setLabel("Level");
        yAxisPietra.setLabel("Fuzzy value");

        final LineChart<Number,Number> weightChart = new LineChart<>(xAxisWeight,yAxisWeight);
        final LineChart<Number,Number> pietraChart = new LineChart<>(xAxisPietra,yAxisPietra);

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

        for (int i = 0; i < zm.elevatorWeight.length; i++) {
            weightLowSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i],zm.elevatorWeightLow[i]));
            weightAvgSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i],zm.elevatorWeightAvg[i]));
            weightHighSeries.getData().add(new XYChart.Data(zm.elevatorWeight[i],zm.elevatorWeightHigh[i]));
        }
        for (int i = 0; i < zm.pietra.length; i++) {
            pietraMalaSeries.getData().add(new XYChart.Data(zm.pietra[i],zm.pietraMala[i]));
            pietraSredniaSeries.getData().add(new XYChart.Data(zm.pietra[i],zm.pietraSrednia[i]));
            pietraDuzaSeries.getData().add(new XYChart.Data(zm.pietra[i],zm.pietraDuza[i]));
        }
        HBox hBox = new HBox();
        hBox.getChildren().addAll(weightChart,pietraChart);
        Scene scene = new Scene(hBox,800,600);
        weightChart.getData().add(weightLowSeries);
        weightChart.getData().add(weightAvgSeries);
        weightChart.getData().add(weightHighSeries);
        pietraChart.getData().add(pietraMalaSeries);
        pietraChart.getData().add(pietraSredniaSeries);
        pietraChart.getData().add(pietraDuzaSeries);
        weightChart.setMinWidth(500);
        stage.setScene(scene);
        stage.show();
    }

}
