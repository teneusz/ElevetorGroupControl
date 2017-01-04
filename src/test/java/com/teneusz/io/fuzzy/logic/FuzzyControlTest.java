package com.teneusz.io.fuzzy.logic;

import com.teneusz.io.elevator.Elevator;
import com.teneusz.io.elevator.ElevatorShaft;
import com.teneusz.io.person.Calling;
import com.teneusz.io.person.Person;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Teneusz on 28.12.2016.
 */
public class FuzzyControlTest {

    @Test
    public void testMethod() {
        ElevatorShaft sh1 = new ElevatorShaft(10);
        ElevatorShaft sh2 = new ElevatorShaft(10);
        Elevator ev1 = new Elevator();
        sh1.setElevator(ev1);

        ev1.setLevel(8);
        Elevator ev2 = new Elevator();
        sh2.setElevator(ev2);

        ev2.setLevel(7);


        Person p1 = new Person(75, 5, Calling.CALL);
        Person p2 = new Person(89, 6,Calling.CALL);

        FuzzyControl.method(Arrays.asList(ev1, ev2), Arrays.asList(p1, p2), 4);
    }
}
