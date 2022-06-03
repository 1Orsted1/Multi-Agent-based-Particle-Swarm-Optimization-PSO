package pso_caveman;

import java.io.Serializable;
import java.util.List;

/**
 * @author : ftm
 * @created : 2022-05-31
 *
 */
public class ModelParticle implements Serializable {

    private String name;
    private int testPositon;
    private List<Float> newPosition;
    private List<Float> bestPosition;
    private List<Float> velocity;
    private Float newFitnessValue;
    private Float bestFitnessValue;

    public Float getNewFitnessValue() {
        return newFitnessValue;
    }

    public void setNewFitnessValue(Float newFitnessValue) {
        this.newFitnessValue = newFitnessValue;
    }

    public Float getBestFitnessValue() {
        return bestFitnessValue;
    }

    public void setBestFitnessValue(Float bestFitnessValue) {
        this.bestFitnessValue = bestFitnessValue;
    }

    public List<Float> getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(List<Float> newPosition) {
        this.newPosition = newPosition;
    }

    public List<Float> getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(List<Float> bestPosition) {
        this.bestPosition = bestPosition;
    }

    public List<Float> getVelocity() {
        return velocity;
    }

    public void setVelocity(List<Float> velocity) {
        this.velocity = velocity;
    }

    private boolean stop;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }

    public int getTestPositon() {
        return testPositon;
    }

    public void setTestPositon(int newPosition) {
        this.testPositon = newPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
