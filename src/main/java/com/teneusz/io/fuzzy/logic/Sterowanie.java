package com.teneusz.io.fuzzy.logic;

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
public class Sterowanie {
    private static boolean call, callUp, callDown;
    private static Logger LOG = Logger.getLogger(Sterowanie.class);
    private static Wnioskowanie wnioskowanie = new Wnioskowanie(new LinguisticVariables());

    public static void method(List<Elevator> elevators, List<Person> personsOnFloor, int level) {
        float wnioskowanieArray[] = new float[elevators.size()];
        for (int i = 0; i < wnioskowanieArray.length; i++) {
            wnioskowanieArray[i] = wnioskowanie.regula2(elevators.get(i), level);
            LOG.debug("Winda " + i + " => " + wnioskowanieArray[i]);
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
            callingUp(elevators,level,wnioskowanieArray);
        }
        if(callDown)
        {
            callingDown(elevators,level,wnioskowanieArray);
        }
        if(!(callUp || callDown) && call){
            if(!callingUp(elevators,level,wnioskowanieArray)){
                callingDown(elevators,level,wnioskowanieArray);
            }
        }
    }

    private static boolean callingDown(List<Elevator> elevators, int level, float[] wnioskowanieArray) {
        List<Elevator> subList = new ArrayList<>();
        Elevator elevator = null;
        float result = Float.MAX_VALUE;
        elevators.stream().filter(e->e.getDirection()==ElevatorDirection.UP && e.getLevel() < level).forEach(subList::add);
        if (subList.isEmpty()) {
            elevators.stream().filter(e -> e.getLevel() < level && e.getDirection() == ElevatorDirection.STOP).forEach(subList::add);
            if (!subList.isEmpty()) {

                for (Elevator ev : subList) {
                    float tmp = wnioskowanieArray[elevators.indexOf(ev)];
                    if (tmp < result) {
                        elevator = ev;
                        result = tmp;
                    }
                }
                if (elevator != null) {
                    elevator.setDirection(ElevatorDirection.DOWN);
                    return true;
                }
            }else{
                //TODO: I don't know what to do
            }
        }
        return false;
    }

    private static boolean callingUp(List<Elevator> elevators,int level,float[] wnioskowanieArray){
        List<Elevator> subList = new ArrayList<>();
        Elevator elevator = null;
        float result = Float.MAX_VALUE;
        elevators.stream().filter(e -> e.getDirection() == ElevatorDirection.DOWN && e.getLevel() > level).forEach(subList::add);
        if (subList.isEmpty()) {
            elevators.stream().filter(e -> e.getLevel() > level && e.getDirection() == ElevatorDirection.STOP).forEach(subList::add);
            if (!subList.isEmpty()) {

                for (Elevator ev : subList) {
                    float tmp = wnioskowanieArray[elevators.indexOf(ev)];
                    if (tmp < result) {
                        elevator = ev;
                        result = tmp;
                    }
                }
                if (elevator != null) {
                    elevator.setDirection(ElevatorDirection.UP);
                    return true;
                }
            }else{
                //TODO: I don't know what to do
            }
        }
        return false;
    }

}
