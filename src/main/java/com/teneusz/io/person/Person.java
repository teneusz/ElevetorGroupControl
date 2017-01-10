package com.teneusz.io.person;

/**
 * Created by Teneusz on 09.12.2016.
 */
public class Person {
    private int weight;
    private int destinationLevel;
    private Calling call;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight > 100) {
            this.weight = 100;
        } else if (weight < 75) {
            this.weight = 75;
        } else {
            this.weight = weight;
        }
    }

    public Person(int weight,int destinationLevel,Calling call)
    {
        setWeight(weight);
        this.destinationLevel = destinationLevel;
        this.call = call;
    }

    public Calling getCall()
    {
        return call;
    }

    public int getDestinationLevel() {
        return destinationLevel;
    }

}
