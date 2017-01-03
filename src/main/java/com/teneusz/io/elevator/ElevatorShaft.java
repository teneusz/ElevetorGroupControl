package com.teneusz.io.elevator;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Logger;

/**
 * Created by Teneusz on 05.12.2016.
 */
public class ElevatorShaft extends VBox {

    private static Logger LOG = Logger.getLogger(ElevatorShaft.class);

    private int maxLevel;
    private Elevator elevator;

    public ElevatorShaft(int levelAmount) {
        setMaxLevel(levelAmount);
    }

    public ElevatorShaft() {
        this.maxLevel = 0;
    }

    private void addLevels() {
        LOG.debug("START addLevel");
        this.getChildren().clear();
        for (int i = 0; i <=maxLevel; i++) {
            Rectangle textArea = new Rectangle();
            textArea.setWidth(this.getWidth());
            textArea.setHeight(this.getHeight() / maxLevel);
            textArea.setFill(Color.WHITE);
            textArea.setStroke(Color.BLACK);
            this.getChildren().add(textArea);

        }
        LOG.debug("STOP addLevel");
    }

    public void setMaxLevel(int maxLevel) {
        LOG.debug("START setMaxLevel");
        this.maxLevel = maxLevel;
        addLevels();
        LOG.debug("STOP setMaxLevel");
    }

    public void setActiveLevel(int activeLevel) {
        if (activeLevel > elevator.getLevel()) {
            elevator.down();
        } else if (activeLevel < elevator.getLevel()) {
            elevator.up();
        }
    }

    @Override
    public void setWidth(double value) {
        LOG.debug("START setWidth");
        super.setWidth(value);
        LOG.debug("New shaft width: "+value);
        for (Node rec : getChildren()) {
            ((Rectangle) rec).setWidth(value);
        }
        LOG.debug("STOP setWidth");
    }

    @Override
    public void setHeight(double value) {
        LOG.debug("START setHeight");
        super.setHeight(value);
        LOG.debug("New shaft height: "+value);
        for (Node rec : getChildren()) {
            ((Rectangle) rec).setHeight(value / getChildren().size());
        }
        LOG.debug("New level height: "+(value/getChildren().size()));
        LOG.debug("STOP setHeight");
    }

    public void repaint() {
        LOG.debug("START repaint");
        LOG.debug("Current level: " + elevator.getLevel());
        this.getChildren().stream().filter(f -> ((Rectangle) f).getFill() != Color.WHITE).forEach(c -> ((Rectangle) c).setFill(Color.WHITE));
        ((Rectangle) this.getChildren().get(elevator.getLevel())).setFill(elevator.isOverLoaded() ? Color.RED : Color.BLUE);
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
