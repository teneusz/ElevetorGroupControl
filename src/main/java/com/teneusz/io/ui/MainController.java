package com.teneusz.io.ui;

import com.teneusz.io.elevator.Elevator;
import com.teneusz.io.elevator.ElevatorDirection;
import com.teneusz.io.elevator.ElevatorShaft;
import com.teneusz.io.fuzzy.logic.Sterowanie;
import com.teneusz.io.person.Calling;
import com.teneusz.io.person.Person;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Teneusz on 10.12.2016.
 */
public class MainController {

    private static final Logger LOG = Logger.getLogger(MainController.class);
    @FXML
    GridPane gridPane;
    @FXML
    ComboBox<Integer> destinationLevelComboBox;
    @FXML
    ComboBox<Calling> callingComboBox;
    List<Elevator> elevators = new ArrayList<>();
    List<ElevatorShaft> shafts = new ArrayList<>();
    Map<Integer, List<Person>> persons = new HashMap<>();

    Timer timer;

    synchronized private List<Person> getPersons(int key) {
        return persons.get(key);
    }

    private int time = 1000;

    public void initialize(int levels, int shaftsInter, int capacity, int maxPersons) {
        gridPane.setPrefHeight(levels * 25);
        gridPane.setMaxHeight(levels * 25);
        gridPane.setMinHeight(levels * 25);
        timer = new Timer();

        callingComboBox.getItems().addAll(Calling.values());
        for (int i = 0; i < shaftsInter; i++) {

            LOG.debug("Add shaft No. " + i);
            ElevatorShaft shaft = new ElevatorShaft(levels);
            Elevator elevator = new Elevator(capacity, maxPersons);
            shaft.setElevator(elevator);
            shaft.setPadding(new Insets(0, 0, 0, 0));
            shaft.setStyle("-fx-padding: 0px;-fx-margin: 5px;");

            shafts.add(shaft);
            elevators.add(elevator);
            shaft.setMinWidth(25);
            shaft.setMaxWidth(25);
            shaft.setPrefWidth(25);
            shaft.setWidth(25.0);
            shaft.setHeight(levels * 24);
            shaft.setMinHeight(levels * 24);
            shaft.setMaxHeight(levels * 24);
            shaft.setPrefHeight(levels * 24);
            gridPane.add(shaft, i, 0, 1, levels);
            shaft.setLayoutX(-50.0);
            shaft.setLayoutY(0.0);
            gridPane.setAlignment(Pos.CENTER);

        }

        for (int i = 0; i < levels; i++) {
            destinationLevelComboBox.getItems().add(new Integer(i));
            persons.put(i, new ArrayList<>());
        }

        LOG.debug("GridPane height: " + gridPane.getPrefHeight());
        startTimer();
    }

    @FXML
    public void addPersonOnZero() {
        persons.get(0).add(createPerson());
    }

    @FXML
    public void addPersonOnOne() {
        persons.get(1).add(createPerson());
    }

    @FXML
    public void addPersonOnTwo() {
        persons.get(2).add(createPerson());
    }

    @FXML
    public void addPersonOnThree() {
        persons.get(3).add(createPerson());
    }

    @FXML
    public void addPersonOnFour() {
        persons.get(4).add(createPerson());
    }

    @FXML
    public void addPersonOnFive() {
        persons.get(5).add(createPerson());
    }

    @FXML
    public void addPersonOnSix() {
        persons.get(6).add(createPerson());
    }

    @FXML
    public void addPersonOnSeven() {
        persons.get(7).add(createPerson());
    }

    @FXML
    public void addPersonOnEight() {
        persons.get(8).add(createPerson());
    }

    @FXML
    public void addPersonOnNine() {
        persons.get(9).add(createPerson());
    }

    private Person createPerson() {
        int weight = ThreadLocalRandom.current().nextInt(75, 100);
        return new Person(weight, destinationLevelComboBox.getSelectionModel().getSelectedItem(), callingComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void stopTimer() {
        try{
        timer.cancel();}
        catch(Exception e){
            LOG.debug("Can't cancel Timer object");
        }
    }

    @FXML
    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerClass(), time, time);

    }

    public int getTime() {
        return time;
    }

    public void setTime(int seconds) {
        this.time = seconds * 1000;
    }

    public void onClose() {
        stopTimer();
        LOG.debug("STOP motherfucker");
    }

    class TimerClass extends TimerTask {

        @Override
        public void run() {
            for (Elevator elevator : elevators) {
                List<Person> tmpList = persons.get(elevator.getLevel());
                List<Person> toRemove = new ArrayList<>();
                //Remove persons from elevator
                elevator.getPersons().stream().filter(f -> f.getDestinationLevel() == elevator.getLevel()).filter(elevator.getPersons()::remove);
                //Add persons to elevator
                if (elevator.getDirection() == ElevatorDirection.UP) {
                    tmpList.stream().filter(f -> f.getCall() == Calling.CALL_UP).forEach(p -> {
                        if (!elevator.isMaxPersons()) {
                            elevator.getPersons().add(p);
                            toRemove.add(p);
                        }
                    });
                    tmpList.removeAll(toRemove);
                    toRemove.clear();
                    tmpList.stream().filter(f -> f.getCall() == Calling.CALL && f.getDestinationLevel() > elevator.getLevel()).forEach(person -> {
                        if (!elevator.isMaxPersons()) {
                            elevator.getPersons().add(person);
                            toRemove.add(person);

                        }
                    });
                    tmpList.removeAll(toRemove);
                    toRemove.clear();
                }

                if (elevator.getDirection() == ElevatorDirection.UP) {
                    tmpList.stream().filter(f -> f.getCall() == Calling.CALL_DOWN).forEach(p -> {
                        if (!elevator.isMaxPersons()) {
                            elevator.getPersons().add(p);
                            toRemove.add(p);

                        }
                    });
                    tmpList.removeAll(toRemove);
                    toRemove.clear();
                    tmpList.stream().filter(f -> f.getCall() == Calling.CALL && f.getDestinationLevel() < elevator.getLevel()).forEach(person -> {
                        if (!elevator.isMaxPersons()) {
                            elevator.getPersons().add(person);
                            toRemove.add(person);

                        }
                    });
                    tmpList.removeAll(toRemove);
                    toRemove.clear();
                }
                persons.put(elevator.getLevel(),tmpList);
                if (elevator.getPersons().isEmpty()) {
                    elevator.setDirection(ElevatorDirection.STOP);
                }
                persons.put(elevator.getLevel(), tmpList);
            }

            //Run fuzzy logic
            for (Map.Entry<Integer, List<Person>> entry : persons.entrySet()) {
                Sterowanie.method(elevators, entry.getValue(), entry.getKey());
            }
            //Move elevators
            elevators.forEach(e -> e.move());
        }
    }


}
