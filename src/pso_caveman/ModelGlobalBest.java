/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso_caveman;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author ftm
 */
public class ModelGlobalBest implements Serializable {
    private List<Float> newPosition;
    private Float fitnessValue;

    public List<Float> getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(List<Float> newPosition) {
        this.newPosition = newPosition;
    }

    public Float getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(Float fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    
    
}
