package com.teneusz.io.elevator;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

/**
 * Created by Teneusz on 05.12.2016.
 */
public class ElevatorShaft extends VBox {

    private static final Logger LOG = Logger.getLogger(ElevatorShaft.class);

    private int maxLevel;
    private Elevator elevator;

    public ElevatorShaft(int levelAmount) {
        setMaxLevel(levelAmount);
        this.setRotate(180);
    }

    public ElevatorShaft() {
        this.maxLevel = 0;
    }

    private void addLevels() {
        LOG.debug("START addLevel");
        this.getChildren().clear();
        for (int i = 0; i < maxLevel; i++) {
            Rectangle textArea = new Rectangle();
            textArea.setWidth(this.getWidth());
            textArea.setHeight(this.getHeight() / maxLevel);
            textArea.setFill(Color.WHITE);
            textArea.setStroke(Color.BLACK);
            Text tx = new Text(String.valueOf(i));
            tx.setRotate(180);
            StackPane sp = new StackPane();
            sp.getChildren().addAll(textArea, tx);
            this.getChildren().add(sp);

        }
        LOG.debug("STOP addLevel");
    }

    public void setMaxLevel(int maxLevel) {
        LOG.debug("START setMaxLevel");
        this.maxLevel = maxLevel;
        addLevels();
        LOG.debug("STOP setMaxLevel");
    }

    @Override
    public void setWidth(double value) {
        LOG.debug("START setWidth");
        super.setWidth(value);
        LOG.debug("New shaft width: " + value);
        for (Node rec : getChildren()) {
            Rectangle rectangle = (Rectangle) ((StackPane) rec).getChildren().get(0);
            ((StackPane) rec).setMinWidth(value);
            ((StackPane) rec).setMaxWidth(value);
            ((StackPane) rec).setPrefWidth(value);

            rectangle.setWidth(value);
        }
        LOG.debug("STOP setWidth");
    }

    @Override
    public void setHeight(double value) {
        LOG.debug("START setHeight");
        super.setHeight(value);
        double height = value / getChildren().size();
        LOG.info("value: " + value);
        LOG.info("height: " + height);
        for (Node rec : getChildren()) {
            Rectangle rectangle = (Rectangle) ((StackPane) rec).getChildren().get(0);

            ((StackPane) rec).setMinHeight(height);
            ((StackPane) rec).setMaxHeight(height);
            ((StackPane) rec).setPrefHeight(height);
            rectangle.setHeight(height);
        }
        LOG.debug("STOP setHeight");
    }

    public void repaint() {
        LOG.debug("START repaint");
        LOG.debug("Current level: " + elevator.getLevel());
        this.getChildren().stream().filter(f -> ((Rectangle) ((StackPane) f).getChildren().get(0)).getFill() != Color.WHITE).forEach(c -> ((Rectangle) ((StackPane) c).getChildren().get(0)).setFill(Color.WHITE));
        ((Rectangle) ((StackPane) this.getChildren().get(elevator.getLevel())).getChildren().get(0)).setFill(elevator.isOverLoaded() ? Color.RED : Color.BLUE);
        LOG.debug("STOP repaint");
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setElevator(Elevator elevator) {
        LOG.debug("START setElevator");
        LOG.debug(elevator.toString());
        this.elevator = elevator;
        elevator.setShaft(this);
        LOG.debug("STOP setElevator");
    }
}
