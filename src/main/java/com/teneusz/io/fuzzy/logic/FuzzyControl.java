package com.teneusz.io.fuzzy.logic;

import com.sun.istack.internal.NotNull;
import com.teneusz.io.elevator.Elevator;
import com.teneusz.io.elevator.ElevatorDirection;
import com.teneusz.io.person.Calling;
import com.teneusz.io.person.Person;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teneusz on 28.12.2016.
 */
public class FuzzyControl {
    private static boolean call, callUp, callDown;
    private static final Logger LOG = Logger.getLogger(FuzzyControl.class);
    private static final Conclusion conclusion = new Conclusion(new LinguisticVariables());

    public static void method(@NotNull List<Elevator> elevators, @NotNull List<Person> personsOnFloor, @NotNull int level) {
        float conclusionArray[] = new float[elevators.size()];
        for (int i = 0; i < conclusionArray.length; i++) {
            conclusionArray[i] = conclusion.regula2(elevators.get(i), level);
            LOG.debug("Winda " + i + " => " + conclusionArray[i]);
        }
        callUp = false;
        callDown = false;
        call = false;
        personsOnFloor.forEach(p -> {
            callUp = callUp || p.getCall() == Calling.CALL_UP;
            callDown = callDown || p.getCall() == Calling.CALL_DOWN;
            call = call || p.getCall() == Calling.CALL;
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

    private static void calling(List<Elevator> elevators, int level, float[] wnioskowanieArray) {
        LOG.debug("Start calling");
        List<Elevator> subList = new ArrayList<>();
        Elevator elevator = null;
        float result = Float.MAX_VALUE;
        elevators.stream().filter(e -> {
            boolean callDown = (e.getDirection() == ElevatorDirection.UP || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() < level;
            boolean callUp = (e.getDirection() == ElevatorDirection.DOWN || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() > level;
            return callDown || callUp;
        }).forEach(subList::add);
        LOG.debug("SubList: " + subList.size());
        for (Elevator ev : subList) {
            float tmp = wnioskowanieArray[elevators.indexOf(ev)];
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

    private static void callingDown(List<Elevator> elevators, int level, float[] wnioskowanieArray) {
        LOG.debug("Start callingDown");
        List<Elevator> subList = new ArrayList<>();
        Elevator elevator = null;
        float result = Float.MAX_VALUE;
        elevators.stream().filter(e -> (e.getDirection() == ElevatorDirection.UP || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() < level).forEach(subList::add);
        for (Elevator ev : subList) {
            float tmp = wnioskowanieArray[elevators.indexOf(ev)];
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

    private static void callingUp(List<Elevator> elevators, int level, float[] wnioskowanieArray) {
        LOG.debug("Start callingUP");
        List<Elevator> subList = new ArrayList<>();
        Elevator elevator = null;
        float result = Float.MAX_VALUE;
        elevators.stream().filter(e -> (e.getDirection() == ElevatorDirection.DOWN || e.getDirection() == ElevatorDirection.STOP) && e.getLevel() > level).forEach(subList::add);
        for (Elevator ev : subList) {
            float tmp = wnioskowanieArray[elevators.indexOf(ev)];
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
/**
 * Jeżeli na piątym piętrze jest wezwanie windy z dołu to muszę do windy z dołu która porusza się do góry lub stoi dopisać przystanek na piątym piętrze
 * Jeżeli na piątym piętrze jest wezwanie windy z góry to muszę do windy z góry która porusza się na dół lub stoi dopisać przystanek na piątym piętrze
 * Jeżeli na piątym piętrze jest wezwanie windy to muszę znaleść windę która może podjechać na piąte piętro i dodać jej przystanek na piątym piętrze
 */
