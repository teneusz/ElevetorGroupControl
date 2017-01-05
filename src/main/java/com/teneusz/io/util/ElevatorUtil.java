package com.teneusz.io.util;

import com.sun.istack.internal.NotNull;
import com.teneusz.io.person.Person;

import java.util.List;

/**
 * Created by Teneusz on 26.12.2016.
 */
public class ElevatorUtil {
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

    public static int getMaxStation(@NotNull List<Integer> station)
    {
        int result = -1;
        for(Integer value:station)
        {
            result = Math.max(value.intValue(),result);
        }
        return  result;
    }

    public static int getMinStation(@NotNull List<Integer> station)
    {
        int result = -1;
        for(Integer value:station)
        {
            result = Math.min(value.intValue(),result);
        }
        return  result;
    }

}
