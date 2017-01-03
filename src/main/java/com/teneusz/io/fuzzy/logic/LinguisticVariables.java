package com.teneusz.io.fuzzy.logic;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Teneusz on 19.12.2016.
 */
public class LinguisticVariables {

    Logger LOG = Logger.getLogger(LinguisticVariables.class);

    public int[] elevatorWeight = new int[401];
    public float[] elevatorWeightLow = new float[401];
    public float[] elevatorWeightAvg = new float[401];
    public float[] elevatorWeightHigh = new float[401];

    public int[] pietra = new int[11];
    public float[] pietraMala = new float[11];
    public float[] pietraSrednia = new float[11];
    public float[] pietraDuza = new float[11];

    public Map<Float, EnergyUsage> zuzycieEnergi = new HashMap<>();
    public Map<EnergyUsage,Float> energyUsage = new HashMap<>();

    private void fillEnergyUsage()
    {
        zuzycieEnergi.put(-0.5f, EnergyUsage.VERY_GOOD);
        energyUsage.put(EnergyUsage.VERY_GOOD,-0.5f);

        zuzycieEnergi.put(-0.25f, EnergyUsage.GOOD);
        energyUsage.put(EnergyUsage.GOOD,-0.25f);

        zuzycieEnergi.put(0f,EnergyUsage.AVG);
        energyUsage.put(EnergyUsage.AVG,0f);

        zuzycieEnergi.put(0.25f,EnergyUsage.BAD);
        energyUsage.put(EnergyUsage.BAD,0.25f);

        zuzycieEnergi.put(0.5f,EnergyUsage.VERY_BAD);
        energyUsage.put(EnergyUsage.VERY_BAD,0.5f);



    }
    /**
     * Fill tables with fuzzy values for elevator width
     */
    private void fillElevatorWeight() {
        for (int i = 0; i < elevatorWeight.length; i++) {

            float currentWeight = i + 500;

            elevatorWeight[i] = (int) currentWeight;
            if (currentWeight <= 580) {
                elevatorWeightLow[i] = 1f;
            } else if (currentWeight <= 700) {
                elevatorWeightLow[i] = fallingEdge(currentWeight, 700, 580);
            } else {
                elevatorWeightLow[i] = 0f;
            }


            if (currentWeight < 700 && currentWeight >= 600) {
                elevatorWeightAvg[i] = risingEdge(currentWeight, 600, 700);
            } else if (currentWeight >= 700 && currentWeight <= 800) {
                elevatorWeightAvg[i] = fallingEdge(currentWeight, 800, 700);
            } else {
                elevatorWeightAvg[i] = 0f;
            }

            if (currentWeight >= 700 && currentWeight < 800) {
                elevatorWeightHigh[i] = risingEdge(currentWeight, 700, 800);
            } else if (currentWeight >= 800 && currentWeight <= 900) {
                elevatorWeightHigh[i] = 1f;
            } else {
                elevatorWeightHigh[i] = 0f;
            }

        }
    }

    private void wypelnijPietra() {
        for (int i = 0; i < pietra.length; i++) {

            pietra[i] = i;

            if ((float) i <= 3) {
                pietraMala[i] = fallingEdge((float) i, 3, 0);
            } else {
                pietraMala[i] = 0f;
            }

            if ((float) i > 1 && (float) i <= 5) {
                pietraSrednia[i] = risingEdge((float) i, 1, 5);
            } else if ((float) i >= 5 && (float) i <= 9) {
                pietraSrednia[i] = fallingEdge((float) i, 9, 5);
            } else {
                pietraSrednia[i] = 0f;
            }

            if ((float) i >= 5 && (float) i <= 7) {
                pietraDuza[i] = risingEdge((float) i, 5, 7);
            } else if ((float) i >= 7 && (float) i <= 10) {
                pietraDuza[i] = 1f;
            } else {
                pietraDuza[i] = 0f;
            }
        }
    }


    public LinguisticVariables() {
        fillElevatorWeight();
        wypelnijPietra();
        fillEnergyUsage();
    }


    private float risingEdge(float x, float a, float x0) {
        return (x - a) / (x0 - a);
    }

    private float fallingEdge(float x, float b, float x0) {
        return (b - x) / (b - x0);
    }
}
