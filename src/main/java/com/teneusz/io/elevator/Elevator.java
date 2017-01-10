package com.teneusz.io.elevator;

import com.teneusz.io.person.Person;
import com.teneusz.io.util.ElevatorUtil;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Teneusz on 09.12.2016.
 */
public class Elevator {
    private static final Logger LOG = Logger.getLogger(Elevator.class);
    private final int id;
    public static final int EMPTY_ELEVATOR_WEIGHT = 500;

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
    private int weight = EMPTY_ELEVATOR_WEIGHT;
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

    private Set<Integer> stations = new LinkedHashSet<>();

    private boolean stopOnLevel;

    /**
     * Constructor of Elevator class with parameters
     *
     * @param capacity   Weight capacity of elevator
     * @param maxPersons Persons capacity of elevator
     */
    public Elevator(int capacity, int maxPersons, int id) {
        this.capacity = capacity;
        this.maxPersons = maxPersons;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Check if elevator is over loaded
     *
     * @return true if elevator is over loaded
     */
    public boolean isOverLoaded() {

        boolean result = (persons.size() > maxPersons) || (getWeight() > capacity);
        LOG.info("weight > capacity = " + (getWeight() > capacity));
        LOG.info("persons.size() > maxPersons = " + (persons.size() > maxPersons));
        return result;
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
        stopOnLevel = stations.contains(level);
        List<Person> toRemove = new ArrayList<>();
        persons.stream().filter(p -> ElevatorUtil.isParsonLeave(p, level)).forEach(toRemove::add);
        persons.removeAll(toRemove);
        stations.remove(level);
        Platform.runLater(() -> shaft.repaint());
    }

    public boolean isStopOnLevel() {
        return stopOnLevel;
    }

    public void addStation(int station) {
        LOG.debug("Dodaj station: " + station);
        stations.add(station);
        if (direction == ElevatorDirection.STOP) {
            if (level > station) {
                direction = ElevatorDirection.UP;
            } else {
                direction = ElevatorDirection.DOWN;
            }

        }
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
     * Get current weight of elevator
     *
     * @return weight of elevator
     */
    public int getWeight() {
        weight = EMPTY_ELEVATOR_WEIGHT;
        persons.forEach(p -> weight += p.getWeight());
        LOG.info("weight = " + weight);
        return weight;
    }

    public ElevatorDirection getDirection() {
        return direction;
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
     * Move elevator one level up if current level is not max
     */
    private void up() {
        LOG.debug("com.teneusz.io.elevator.Elevator try go UP");
        if (level > 0) {
            setLevel(level - 1);
        }
    }

    /**
     * Move elevator one level down if current level is not 0
     */
    private void down() {
        LOG.debug("com.teneusz.io.elevator.Elevator try go down");
        if (shaft.getMaxLevel() - 1 > level) {
            setLevel(level + 1);
        }
    }

    private void recalculateDirection() {
        int minStation = ElevatorUtil.getMinStation(new ArrayList<>(stations));
        int maxStation = ElevatorUtil.getMaxStation(new ArrayList<>(stations));
        if (stations.isEmpty()) {
            direction = ElevatorDirection.STOP;
        } else if (direction == ElevatorDirection.UP && minStation > level) {
            direction = ElevatorDirection.DOWN;
        } else if (direction == ElevatorDirection.DOWN && maxStation < level) {
            direction = ElevatorDirection.UP;
        }
        LOG.info("Elevator " + id + " " + direction.toString() + ", min station: " + minStation + "; maxStation: " + maxStation);
    }


    public void move() {
        if (direction != ElevatorDirection.STOP) {
            if (direction == ElevatorDirection.UP) {
                up();
            } else if (direction == ElevatorDirection.DOWN) {
                down();
            }
            recalculateDirection();
            Platform.runLater(() -> shaft.repaint());
            LOG.debug("ELEVATOR level: " + level);
            stations.forEach(s -> LOG.debug("Station :" + s));
        }
    }

    public void addPerson(Person person) {
        if (person.getDestinationLevel() == level) {
            return;
        }
        int destinationLevel = person.getDestinationLevel();
        persons.add(person);
        addStation(destinationLevel);
        if (direction == ElevatorDirection.STOP) {
            if (destinationLevel > level) {
                direction = ElevatorDirection.DOWN;
            } else {
                direction = ElevatorDirection.UP;
            }
        }
    }

    @Override
    public String toString() {
        return "com.teneusz.io.elevator.Elevator{" +
                "capacity=" + capacity +
                ", maxPersons=" + maxPersons +
                ", weight=" + weight +
                '}';
    }


}
