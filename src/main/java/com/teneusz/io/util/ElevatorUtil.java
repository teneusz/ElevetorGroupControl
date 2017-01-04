package com.teneusz.io.util;

import com.teneusz.io.person.Person;

import java.util.List;

/**
 * Created by Teneusz on 26.12.2016.
 */
public class ElevatorUtil {

    public static int maxLevelFromPersons(List<Person> personList) {
        int max = -1;
        personList.forEach(p -> Math.max(max, p.getDestinationLevel()));
        return max;
    }

    public static int minLevelFromPersons(List<Person> personList, int maxLevel) {
        personList.forEach(p -> Math.min(maxLevel, p.getDestinationLevel()));
        return maxLevel;
    }

    public static boolean isParsonLeave(Person person, int level) {
        return person.getDestinationLevel() == level;
    }

    public static boolean isSomeoneLeave(List<Person> persons, int level) {
        boolean result = false;
        for (Person person : persons) {
            result = result || isParsonLeave(person, level);
        }
        return result;
    }

}
