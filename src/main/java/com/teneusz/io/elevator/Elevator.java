package com.teneusz.io.elevator;

import com.teneusz.io.person.Person;
import com.teneusz.io.util.ElevatorUtil;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teneusz on 09.12.2016.
 */
public class Elevator {
    private static Logger LOG = Logger.getLogger(Elevator.class);

    /**
     * Weight capacity of elevator - default 900
     */
    private int capacity = 900;
    /**
     * Persons capacity of elevator - default 4 persons
     */
    private int maxPersons = 4;
    /**
     * List of persons in elevator
     */
    private List<Person> persons = new ArrayList<>();
    /**
     * Weight of elevator
     */
    private int weight = 500;
    /**
     * Elevator shaft
     */
    private ElevatorShaft shaft;
    /**
     * Current position of elevator
     */
    private int level = 0;
    /**
     * Moving direction of elevator
     */
    private ElevatorDirection direction = ElevatorDirection.STOP;

    /**
     * Constructor of Elevator class with parameters
     *
     * @param capacity   Weight capacity of elevator
     * @param maxPersons Persons capacity of elevator
     */
    public Elevator(int capacity, int maxPersons) {
        this.capacity = capacity;
        this.maxPersons = maxPersons;
    }

    /**
     * Constructor of Elevator class without parameters
     */
    public Elevator() {
        super();
    }

    /**
     * Check if elevator is over loaded
     *
     * @return true if elevator is over loaded
     */
    public boolean isOverLoaded() {
        weight = 0;
        persons.stream().forEach(p -> weight += p.getWeight());
        return (persons.size() > maxPersons) || (weight > capacity);
    }

    /**
     * Set shaft to elevator
     *
     * @param shaft instance of elevator shaft
     */
    public void setShaft(ElevatorShaft shaft) {
        this.shaft = shaft;
        setLevel(shaft.getMaxLevel() - 1);
    }

    /**
     * Set current level of elevator
     *
     * @param level level in shaft
     */
    public void setLevel(int level) {
        this.level = level;
        List<Person> toRemove = new ArrayList<>();
        persons.stream().filter(p -> ElevatorUtil.isParsonLeave(p, level)).forEach(toRemove::add);
        persons.removeAll(toRemove);
        shaft.repaint();
    }

    /**
     * Get persons list
     *
     * @return persons list in elevator
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * Get current level of elevator
     *
     * @return level in shaft
     */
    public int getLevel() {
        return level;
    }

    /**
     * Move elevator one level up if current level is not max
     */
    public void up() {
        LOG.debug("com.teneusz.io.elevator.Elevator try go UP");
        if (level > 0 && direction == ElevatorDirection.UP) {
            setLevel(level - 1);
        }

    }

    /**
     * Move elevator one level down if current level is not 0
     */
    public void down() {
        LOG.debug("com.teneusz.io.elevator.Elevator try go down");
        if (shaft.getMaxLevel() > level && direction == ElevatorDirection.DOWN) {
            setLevel(level + 1);
        }
    }

    /**
     * Get current weight of elevator
     *
     * @return weight of elevator
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Check if elevator has max persons on board
     *
     * @return true if number of person in side elevator is equals to persons capacity of elevator
     */
    public boolean isMaxPersons() {
        return persons.size() == maxPersons;
    }

    /**
     * Set direction of elevator to STOP
     */
    public void stop() {
        direction = ElevatorDirection.STOP;
    }

    public void move() {
        LOG.debug("ELEVATOR level: " + level);
        if (direction == ElevatorDirection.UP) {
            up();
        } else if (direction == ElevatorDirection.DOWN) {
            down();
        }
        Platform.runLater(() -> shaft.repaint());
        LOG.debug("ELEVATOR level: " + level);
    }

    @Override
    public String toString() {
        return "com.teneusz.io.elevator.Elevator{" +
                "capacity=" + capacity +
                ", maxPersons=" + maxPersons +
                ", weight=" + weight +
                '}';
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }
}
