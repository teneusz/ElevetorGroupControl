package com.teneusz.io.fuzzy.logic;

import com.teneusz.io.elevator.Elevator;
import org.apache.log4j.Logger;


/**
 * Created by Teneusz on 26.12.2016.
 */
public class Conclusion {

    private final LinguisticVariables variables;
    private static final Logger LOG = Logger.getLogger(Conclusion.class);

    public Conclusion(LinguisticVariables lv) {
        this.variables = lv;
    }

    /**
     * Return fuzzy value for elevator based on the best solution rules
     *
     * @param elevator {@link Elevator} instance
     * @param destinationLevel destination of person
     * @return fuzzy value
     */
    public float findTheBestSolution(Elevator elevator, int destinationLevel) {
        float result = -1;
        if (!elevator.isMaxPersons() && !elevator.isOverLoaded()) {
            int elevatorWeight = elevator.getWeight() - Elevator.EMPTY_ELEVATOR_WEIGHT;
            int levelDelta = Math.abs(elevator.getLevel() - destinationLevel);

            float weightLow = variables.elevatorWeightLow[elevatorWeight];
            float weightAvg = variables.elevatorWeightAvg[elevatorWeight];
            float weightHigh = variables.elevatorWeightHigh[elevatorWeight];

            float floorLow = variables.pietraMala[levelDelta];
            float floorAvg = variables.pietraSrednia[levelDelta];
            float floorHigh = variables.pietraDuza[levelDelta];

            float r1 = Math.min(floorLow, weightLow);
            float r2 = Math.min(floorLow, weightAvg);
            float r3 = Math.min(floorLow, weightHigh);

            float r4 = Math.min(floorAvg, weightLow);
            float r5 = Math.min(floorAvg, weightAvg);
            float r6 = Math.min(floorAvg, weightHigh);

            float r7 = Math.min(floorHigh, weightLow);
            float r8 = Math.min(floorHigh, weightAvg);
            float r9 = Math.min(floorHigh, weightHigh);

            float vg = Math.max(Math.max(r1, r2), r9);
            float b = Math.max(r3, r8);
            float g = r4;
            float a = Math.max(r5, r7);
            float vb = r6;

            //Weighted arithmetic mean
            //Metoda środka ciężkości
            result = ((vg * variables.energyUsage.get(EnergyUsage.VERY_GOOD)) +
                    (g * variables.energyUsage.get(EnergyUsage.GOOD)) +
                    (a * variables.energyUsage.get(EnergyUsage.AVG)) +
                    (b * variables.energyUsage.get(EnergyUsage.BAD)) +
                    (vb * variables.energyUsage.get(EnergyUsage.VERY_BAD))
            ) / (vg + g + a + b + vb);
        }
        return result;
    }

    /**
     * Return fuzzy value based on the worst solution rules
     *
     * @param elevator {@link Elevator} instance
     * @param destinationLevel destination of person
     * @return fuzzy value
     */
    public float findTheWorstSolution(Elevator elevator, int destinationLevel) {
        float result = -1;
        if (!elevator.isMaxPersons()) {
            int elevatorWeight = elevator.getWeight();
            int levelDelta = Math.abs(elevator.getLevel() - destinationLevel);

            float weightLow = variables.elevatorWeightLow[elevatorWeight];
            float weightAvg = variables.elevatorWeightAvg[elevatorWeight];
            float weightHigh = variables.elevatorWeightHigh[elevatorWeight];

            float floorLow = variables.pietraMala[levelDelta];
            float floorAvg = variables.pietraSrednia[levelDelta];
            float floorHigh = variables.pietraDuza[levelDelta];

            float r1 = Math.min(floorLow, weightLow);
            float r2 = Math.min(floorLow, weightAvg);
            float r3 = Math.min(floorLow, weightHigh);

            float r4 = Math.min(floorAvg, weightLow);
            float r5 = Math.min(floorAvg, weightAvg);
            float r6 = Math.min(floorAvg, weightHigh);

            float r7 = Math.min(floorHigh, weightLow);
            float r8 = Math.min(floorHigh, weightAvg);
            float r9 = Math.min(floorHigh, weightHigh);

            float a = Math.max(r1, Math.max(r2, r4));
            float vb = Math.max(r3, Math.max(r6, Math.max(r8, r9)));
            float b = Math.max(r5, r7);

            //Weighted arithmetic mean
            result = ((a * variables.energyUsage.get(EnergyUsage.AVG)) +
                    (b * variables.energyUsage.get(EnergyUsage.BAD)) +
                    (vb * variables.energyUsage.get(EnergyUsage.VERY_BAD))
            ) / (a + b + vb);
        }

        return result;
    }
}
