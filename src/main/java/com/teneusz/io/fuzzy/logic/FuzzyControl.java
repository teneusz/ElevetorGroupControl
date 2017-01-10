package com.teneusz.io.fuzzy.logic;

import com.sun.istack.internal.NotNull;
import com.teneusz.io.elevator.Elevator;
import com.teneusz.io.elevator.ElevatorDirection;
import com.teneusz.io.person.Calling;
import com.teneusz.io.person.Person;
import com.teneusz.io.ui.Plots;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose of class is add station to elevators based on results from Conclusion class
 */
public class FuzzyControl {
    private static boolean call, callUp, callDown;
    private static final Logger LOG = Logger.getLogger(FuzzyControl.class);
    private static final Conclusion conclusion = new Conclusion(new LinguisticVariables());

    public static void method(@NotNull List<Elevator> elevators, @NotNull List<Person> personsOnFloor, @NotNull int level, @NotNull Plots plots) {
        //Declaration and definition of conclusion array
        float conclusionArray[] = new float[elevators.size()];
        //Fill conclusion array with results from Conclusion class
        for (int i = 0; i < conclusionArray.length; i++) {
            conclusionArray[i] = conclusion.findTheBestSolution(elevators.get(i), level);
        }
        Platform.runLater(() -> plots.addValues(conclusionArray));
        //Set call flags on false
        callUp = false;
        callDown = false;
        call = false;
        //Set call flags based on persons calls
        personsOnFloor.forEach(p -> {
            Calling personCall = p.getCall();
            callUp = callUp || personCall == Calling.CALL_UP;
            callDown = callDown || personCall == Calling.CALL_DOWN;
            call = call || personCall == Calling.CALL;
        });

        if (callUp) {
            callingUp(elevators, level, conclusionArray);
        }
        if (callDown) {
            callingDown(elevators, level, conclusionArray);
        }
        if (call) {
            calling(elevators, level, conclusionArray);
        }
    }

    /**
     * @param elevators       List of elevators in building
     * @param level           Current level
     * @param conclusionArray Array with results from {@link Conclusion} class
     */
    private static void calling(List<Elevator> elevators, int level, float[] conclusionArray) {
        LOG.debug("Start calling");
        List<Elevator> subList = new ArrayList<>();
        elevators.stream().filter(e -> {
            boolean callDown = (e.getDirection() == ElevatorDirection.UP || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() > level;
            boolean callUp = (e.getDirection() == ElevatorDirection.DOWN || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() < level;
            return callDown || callUp;
        }).forEach(subList::add);
        findBestElevator(elevators, level, conclusionArray, subList);
    }

    private static void callingUp(List<Elevator> elevators, int level, float[] conclusionArray) {
        LOG.debug("Start callingDown");
        List<Elevator> subList = new ArrayList<>();
        elevators.stream().filter(e -> (e.getDirection() == ElevatorDirection.UP || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() > level).forEach(subList::add);
        findBestElevator(elevators, level, conclusionArray, subList);

    }

    private static void callingDown(List<Elevator> elevators, int level, float[] conclusionArray) {
        LOG.debug("Start callingUP");
        List<Elevator> subList = new ArrayList<>();
        elevators.stream().filter(e -> (e.getDirection() == ElevatorDirection.DOWN || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() < level).forEach(subList::add);
        findBestElevator(elevators, level, conclusionArray, subList);
    }

    private static void findBestElevator(List<Elevator> elevators, int level, float[] conclusionArray, List<Elevator> subList) {
        Elevator elevator = elevators.get(0);
        float result = Float.MAX_VALUE;
        for (Elevator ev : subList) {
            LOG.debug("Elevator id: " + ev.getId());
            float tmp = conclusionArray[elevators.indexOf(ev)];
            if (tmp < result) {
                elevator = ev;
                result = tmp;
            }
        }
        if (elevator != null) {
            LOG.debug("Add station: " + level);
            elevator.addStation(level);
        }
    }

}