package math_caveman;

import java.util.*;
import static jdk.nashorn.internal.objects.NativeMath.round;
import pso_caveman.ModelGlobalBest;
import pso_caveman.ModelParticle;

/**
 *
 * @author ftm
 */
public class utility {

    //step 1 (the initial pop is going to be the values given by the user):
    //calculate initial velocity
    public List<Float> initializateVelocity(List<Float> initialPop) {
        List<Float> velocityVector = new ArrayList<>();

        for (Float value : initialPop) {
            Float nv = (float) (Math.round((value * 0.1) * 100.0) / 100.0);
            velocityVector.add(nv);

        }

        return velocityVector;
    }

    //step 2 
    //calculate initial position
    public List<Float> calculateNewPosition(List<Float> initialPosition, List<Float> velocity) {
        List<Float> posotionVector = new ArrayList<>();
        int counter = 0;
        for (Float value : initialPosition) {
            Float nv = velocity.get(counter);
            Float r = value + nv;
            posotionVector.add(r);
            counter++;
        }

        return posotionVector;
    }

    //step 3 calculate fitness value:
    public Float calculateFitnessValue(List<Float> positions) {
        Float fitnessValue = (float) 0.0;
        Float counter = (float) 1.0;
        Float decena = (float) 10.0;
        for (Float value : positions) {
            fitnessValue += ((decena * counter) * (float) Math.pow(value - counter, 2));
            counter += (float) 1.0;
        }

        return fitnessValue * (float) 0.001;
    }

    //step 4 calculate the best global fitness with its position:
    public ModelGlobalBest calculateGlobalBest(List<ModelParticle> particles, ModelGlobalBest lastGB, boolean updated) {
        ModelGlobalBest myBest = lastGB;
        for (ModelParticle p : particles) {
            if (p.getNewFitnessValue() < myBest.getFitnessValue()) {
                System.out.println("##################################################################");
                myBest.setFitnessValue(p.getNewFitnessValue());
                myBest.setNewPosition(p.getNewPosition());
                updated = true;
            }
        }
        return myBest;
    }

    //step 5 recalculate velocity(on loop)
    public void recalculateVelocity(List<ModelParticle> pv, ModelGlobalBest gb, Float af1, Float af2) {
        for (ModelParticle particle : pv) {
            int numVar = particle.getVelocity().size();
            int iteracion = 0;
            List<Float> newVelocity = new ArrayList<>();
            while (iteracion < numVar) {
                float rand1 = (float) 0.9;
                float rand2 = (float) Math.round((Math.random()) * 10) / 10;
                float rand3 = (float) Math.round((Math.random()) * 10) / 10;
                float personalOperation = particle.getBestPosition().get(iteracion) - particle.getNewPosition().get(iteracion);
                float globalOperation = gb.getNewPosition().get(iteracion) - particle.getNewPosition().get(iteracion);
                newVelocity.add(rand1 * particle.getVelocity().get(iteracion) + (af1 * rand2 * personalOperation) + (af2 * rand3 * globalOperation));
                iteracion++;
            }
            particle.setVelocity(newVelocity);

        }
    }

    public boolean calculateNewBestPosition(Float last, Float best) {
        return (last < best);
    }

    ///------------------------
    //here some visual helpers:
    public void showArray(List<Float> myArray) {
        System.out.print("[");
        for (Float value : myArray) {
            System.out.print(value + ",");
        }
        System.out.println("]");

    }

    ///------------------------
}
