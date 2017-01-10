package com.teneusz.io.ui;

import com.teneusz.io.elevator.Elevator;
import com.teneusz.io.elevator.ElevatorDirection;
import com.teneusz.io.elevator.ElevatorShaft;
import com.teneusz.io.fuzzy.logic.FuzzyControl;
import com.teneusz.io.fuzzy.logic.LinguisticVariables;
import com.teneusz.io.person.Calling;
import com.teneusz.io.person.Person;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controller for main.fxml file
 */
public class MainController {

    private static final Logger LOG = Logger.getLogger(MainController.class);
    @FXML
    GridPane gridPane;
    @FXML
    ComboBox<Integer> destinationLevelComboBox;
    @FXML
    ComboBox<Calling> callingComboBox;
    @FXML
    TextField floorZeroText, floorOneText, floorTwoText, floorThreeText, floorFourText, floorFiveText, floorSixText, floorSevenText, floorEightText, floorNineText;
    List<TextField> floorsText = new ArrayList<>();
    @FXML
    Spinner<Integer> timerValue;
    List<Elevator> elevators = new ArrayList<>();
    List<ElevatorShaft> shafts = new ArrayList<>();
    Map<Integer, List<Person>> persons = new HashMap<>();
    Stage plotStage = new Stage();
    Plots plots;

    Timer timer;

    synchronized private List<Person> getPersons(int key) {
        return persons.get(key);
    }

    private int time = 1000;

    /**
     * initialize controller
     *
     * @param levels      amount of levels in building
     * @param shaftsInter amount of shafts in building
     * @param capacity    capacity of elevators
     * @param maxPersons  how many persons can be in elevator
     */
    public void initialize(int levels, int shaftsInter, int capacity, int maxPersons) {
        Collections.addAll(floorsText, new TextField[]{floorZeroText, floorOneText, floorTwoText, floorThreeText, floorFourText, floorFiveText, floorSixText, floorSevenText, floorEightText, floorNineText});
        timer = new Timer();
        timerValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, time));
        timerValue.valueProperty().addListener((observable, oldValue, newValue) -> {
                    setTime(newValue);
                    stopTimer();
                    startTimer();
                }
        );
        callingComboBox.getItems().addAll(Calling.values());
        callingComboBox.getSelectionModel().selectFirst();
        timerValue.setEditable(true);
        for (int i = 0; i < levels; i++) {
            destinationLevelComboBox.getItems().add(i);
            persons.put(i, new ArrayList<>());
        }
        destinationLevelComboBox.getSelectionModel().selectFirst();

        for (int i = 0; i < shaftsInter; i++) {

            LOG.debug("Add shaft No. " + i);
            ElevatorShaft shaft = new ElevatorShaft(levels);
            Elevator elevator = new Elevator(capacity, maxPersons, i);
            shaft.setElevator(elevator);

            shafts.add(shaft);
            elevators.add(elevator);

            shaft.setHeight(gridPane.getHeight());
            shaft.setMinHeight(gridPane.getMinHeight());
            shaft.setMaxHeight(gridPane.getMaxHeight());
            shaft.setPrefHeight(gridPane.getPrefHeight());
            gridPane.add(shaft, i, 0, 1, levels);
            gridPane.setAlignment(Pos.CENTER);

        }

        LOG.debug("GridPane height: " + gridPane.getPrefHeight());
        plots = new Plots(new LinguisticVariables(), plotStage, elevators.size());
        plots.initialize();

        startTimer();
    }

    @FXML
    public void addPersonOnZero() {
        persons.get(0).add(createPerson());
        updateFloorText(0);
    }

    @FXML
    public void addPersonOnOne() {
        persons.get(1).add(createPerson());
        updateFloorText(1);
    }

    @FXML
    public void addPersonOnTwo() {
        persons.get(2).add(createPerson());
        updateFloorText(2);
    }

    @FXML
    public void addPersonOnThree() {
        persons.get(3).add(createPerson());
        updateFloorText(3);
    }

    @FXML
    public void addPersonOnFour() {
        persons.get(4).add(createPerson());
        updateFloorText(4);
    }

    @FXML
    public void addPersonOnFive() {
        persons.get(5).add(createPerson());
        updateFloorText(5);
    }

    @FXML
    public void addPersonOnSix() {
        persons.get(6).add(createPerson());
        updateFloorText(6);
    }

    @FXML
    public void addPersonOnSeven() {
        persons.get(7).add(createPerson());
        updateFloorText(7);
    }

    @FXML
    public void addPersonOnEight() {
        persons.get(8).add(createPerson());
        updateFloorText(8);
    }

    @FXML
    public void addPersonOnNine() {
        persons.get(9).add(createPerson());
        updateFloorText(9);
    }

    private Person createPerson() {
        int weight = ThreadLocalRandom.current().nextInt(75, 100);
        return new Person(weight, destinationLevelComboBox.getSelectionModel().getSelectedItem(), callingComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void stopTimer() {
        try {
            timer.cancel();
        } catch (Exception e) {
            LOG.debug("Can't cancel Timer object");
        }
    }

    @FXML
    public void startTimer() {
        try {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerClass(), time, time);
        } catch (Exception e) {
            LOG.debug("Can't start Timer object");
        }
    }

    public void setTime(int milliseconds) {
        this.time = milliseconds;
    }

    public void onClose() {
        stopTimer();
        LOG.debug("STOP motherfucker");
    }

    private void updateFloorText(int floor) {
        int callCount = 0;
        int callUpCount = 0;
        int callDownCount = 0;
        for (Person person : getPersons(floor)) {
            switch (person.getCall()) {
                case CALL_UP:
                    callUpCount++;
                    break;
                case CALL_DOWN:
                    callDownCount++;
                    break;
                case CALL:
                    callCount++;
                    break;
                default:
                    break;
            }
        }
        String text = String.format("Call: %d; Call up: %d; Call down: %d;", callCount, callUpCount, callDownCount);
        floorsText.get(floor).setText(text);
    }


    @FXML
    public void closeApplication() {
        onClose();
        Platform.exit();
    }

    @FXML
    public void openPlots() {
        if (plotStage.isShowing()) {
            plotStage.hide();
        } else {
            plotStage.show();
        }
    }

    @FXML
    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Projekt na zajęcia \"Zastosowanie sztucznej inteligencji\"");
        alert.setContentText("Temat projektu: Dynamic fuzzy logic elevator group control system\n\n\n" +
                "Projekt wykonali:\nPrzemysław Potaczek\nMeronk Krzysztof");
        alert.show();
    }

    class TimerClass extends TimerTask {

        @Override
        public void run() {
            for (Elevator elevator : elevators) {
                int elevatorLevel = elevator.getLevel();
                List<Person> tmpList = persons.get(elevatorLevel);
                List<Person> toRemove = new ArrayList<>();
                if (elevator.isStopOnLevel()) {
                    if (elevator.getDirection() == ElevatorDirection.STOP && !getPersons(elevatorLevel).isEmpty()) {
                        LOG.debug("Elevator direction is equals STOP");
                        elevator.addPerson(getPersons(elevatorLevel).remove(0));
                    }
                    OnDirectionUp(elevator, tmpList, toRemove);
                    OnDirectionDown(elevator, tmpList, toRemove);
                }
                if (tmpList != null) {
                    tmpList.removeAll(toRemove);
                }
                persons.put(elevatorLevel, tmpList);
            }

            //Run fuzzy logic
            LOG.debug("Run fuzzy logic");
            for (Map.Entry<Integer, List<Person>> entry : persons.entrySet()) {
                if (!persons.isEmpty()) {
                    FuzzyControl.method(elevators, entry.getValue(), entry.getKey(), plots);
                }
                updateFloorText(entry.getKey());

            }
            //Move elevators
            LOG.debug("Move elevators");
            elevators.forEach(Elevator::move);
        }

        /**
         * Run method if elevator is going up
         *
         * @param elevator elevator object
         * @param tmpList  temporary list of Persons
         * @param toRemove list of persons to remove
         */
        private void OnDirectionUp(Elevator elevator, List<Person> tmpList, List<Person> toRemove) {
            if (elevator.getDirection() == ElevatorDirection.UP) {
                LOG.debug("Elevator direction is equals UP");
                getPersons(elevator.getLevel()).stream().filter(p -> p.getDestinationLevel() <= elevator.getLevel()).forEach(p -> {
                    if (!elevator.isMaxPersons()) {
                        LOG.info("Add person to list of passengers to elevator id = " + elevator.getId());
                        elevator.addPerson(p);
                        LOG.info("Elevator size = " + elevator.getPersons().size());
                        LOG.debug("Add person to temporary list toRemove");
                        toRemove.add(p);
                    }
                });
            }
            clearPersons(tmpList, toRemove);
        }

        /**
         * Run method if elevator is going down
         *
         * @param elevator elevator object
         * @param tmpList  temporary list of Persons
         * @param toRemove list of persons to remove
         */
        private void OnDirectionDown(Elevator elevator, List<Person> tmpList, List<Person> toRemove) {
            if (elevator.getDirection() == ElevatorDirection.DOWN) {
                LOG.debug("Elevator direction is equals DOWN");
                getPersons(elevator.getLevel()).stream().filter(p -> p.getDestinationLevel() >= elevator.getLevel()).forEach(p -> {
                    if (!elevator.isMaxPersons()) {
                        LOG.debug("Add person to list of passengers");
                        elevator.addPerson(p);
                        LOG.debug("Add person to temporary list toRemove");
                        toRemove.add(p);
                    }
                });
            }
            clearPersons(tmpList, toRemove);
        }

        /**
         * remove toRemove from tmpList
         *
         * @param tmpList  temporary list of Persons
         * @param toRemove list of persons to remove
         */
        private void clearPersons(List<Person> tmpList, List<Person> toRemove) {
            if (tmpList != null && toRemove != null) {
                tmpList.removeAll(toRemove);
            }
            if (toRemove != null) {
                toRemove.clear();
            }
        }

    }


}
