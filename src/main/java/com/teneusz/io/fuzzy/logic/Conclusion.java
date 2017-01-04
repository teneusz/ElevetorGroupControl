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

    public float regulyJeden(Elevator elevator, int destLevel) {

        float result = -1;
        if (!elevator.isMaxPersons()) {
            int elevatorWeight = elevator.getWeight();
            int levelDelta = Math.abs(elevator.getLevel() - destLevel);
            LOG.debug("Delta => "+ levelDelta);

            float masaMala = variables.elevatorWeightLow[elevatorWeight];
            float masaAvg = variables.elevatorWeightAvg[elevatorWeight];
            float masaDuza = variables.elevatorWeightHigh[elevatorWeight];

            float pietraMala = variables.pietraMala[levelDelta];
            float pietraAvg = variables.pietraSrednia[levelDelta];
            float pietraDuza = variables.pietraDuza[levelDelta];

            float r1 = Math.min(pietraMala, masaMala);
            float r2 = Math.min(pietraMala, masaAvg);
            float r3 = Math.min(pietraMala, masaDuza);

            float r4 = Math.min(pietraAvg, masaMala);
            float r5 = Math.min(pietraAvg, masaAvg);
            float r6 = Math.min(pietraAvg, masaDuza);

            float r7 = Math.min(pietraDuza, masaMala);
            float r8 = Math.min(pietraDuza, masaAvg);
            float r9 = Math.min(pietraDuza, masaDuza);

            float vg = Math.max(Math.max(r1, r2), r9);
            float b = Math.max(r3, r8);
            float g = r4;
            float a = Math.max(r5, r7);
            float vb = r6;

            LOG.debug(String.format("\nmasaMala: %f\nmasaAvg: %f\nmasaDuza: %f\npietraMala: %f\npietraAvg: %f\npietraDuza: %f",masaMala,masaAvg,masaDuza,pietraMala,pietraAvg,pietraDuza));
            LOG.debug(String.format("\nR1: %f\nR2: %f\nR3: %f\nR4: %f\nR5: %f\nR6: %f\nR7: %f\nR8: %f\nR9: %f\n",r1,r2,r3,r4,r5,r6,r7,r8,r9));

            result = ((vg * variables.energyUsage.get(EnergyUsage.VERY_GOOD)) +
                    (g * variables.energyUsage.get(EnergyUsage.GOOD)) +
                    (a * variables.energyUsage.get(EnergyUsage.AVG)) +
                    (b * variables.energyUsage.get(EnergyUsage.BAD)) +
                    (vb * variables.energyUsage.get(EnergyUsage.VERY_BAD))
            ) / (vg + g + a + b + vb);
        }
        return result;
    }

    public float regula2(Elevator elevator, int destLevel) {
        float result = -1;
        if (!elevator.isMaxPersons()) {
            int elevatorWeight = elevator.getWeight();
            int levelDelta = Math.abs(elevator.getLevel() - destLevel);

            float masaMala = variables.elevatorWeightLow[elevatorWeight];
            float masaAvg = variables.elevatorWeightAvg[elevatorWeight];
            float masaDuza = variables.elevatorWeightHigh[elevatorWeight];

            float pietraMala = variables.pietraMala[levelDelta];
            float pietraAvg = variables.pietraSrednia[levelDelta];
            float pietraDuza = variables.pietraDuza[levelDelta];

            float r1 = Math.min(pietraMala, masaMala);
            float r2 = Math.min(pietraMala, masaAvg);
            float r3 = Math.min(pietraMala, masaDuza);

            float r4 = Math.min(pietraAvg, masaMala);
            float r5 = Math.min(pietraAvg, masaAvg);
            float r6 = Math.min(pietraAvg, masaDuza);

            float r7 = Math.min(pietraDuza, masaMala);
            float r8 = Math.min(pietraDuza, masaAvg);
            float r9 = Math.min(pietraDuza, masaDuza);

            float a = Math.max(r1, Math.max(r2, r4));
            float vb = Math.max(r3, Math.max(r6, Math.max(r8, r9)));
            float b = Math.max(r5, r7);
            LOG.debug(String.format("\nmasaMala: %f\nmasaAvg: %f\nmasaDuza: %f\npietraMala: %f\npietraAvg: %f\npietraDuza: %f",masaMala,masaAvg,masaDuza,pietraMala,pietraAvg,pietraDuza));
            LOG.debug(String.format("\nR1: %f\nR2: %f\nR3: %f\nR4: %f\nR5: %f\nR6: %f\nR7: %f\nR8: %f\nR9: %f\n",r1,r2,r3,r4,r5,r6,r7,r8,r9));

            result = ((a * variables.energyUsage.get(EnergyUsage.AVG)) +
                    (b * variables.energyUsage.get(EnergyUsage.BAD)) +
                    (vb * variables.energyUsage.get(EnergyUsage.VERY_BAD))
            ) / (a + b + vb);
        }

        return result;
    }
}
