package pso_caveman;

/**
 * @author : ftm
 * @created : 2022-05-31
 *
 */
import java.util.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import math_caveman.utility;

public class PositionMatrix extends Agent {

    int iterations = 0;
    int maxIt = 50;
    //max population
    int n = 5;
    Float c1 = (float) 2.0;
    Float c2 = (float) 2.0;

    utility utilitiMath = new utility();

    List<ModelParticle> _NEWPOSITIONS = new ArrayList<>();
    ModelGlobalBest globalBest = new ModelGlobalBest();

    @Override
    protected void setup() {
        addBehaviour(new returnParticle());
        addBehaviour(new getParticles());

    }

    private class getParticles extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("Control");

            ACLMessage msg = receive();
            String name;
            if (msg != null) {
                try {
                    name = ((ModelParticle) msg.getContentObject()).getName();
                    ModelParticle particle = ((ModelParticle) msg.getContentObject());
                    _NEWPOSITIONS.add(particle);
                } catch (UnreadableException ex) {
                    System.out.println(getLocalName() + ">>> Algo salio mal ");

                }
            }
        }
    }

    private class returnParticle extends CyclicBehaviour {

        @Override
        public void action() {
            if (_NEWPOSITIONS.size() == n) {

                if (iterations == 0) {
                    globalBest.setFitnessValue(_NEWPOSITIONS.get(0).getNewFitnessValue());
                    globalBest.setNewPosition(_NEWPOSITIONS.get(0).getNewPosition());
                    globalBest = utilitiMath.calculateGlobalBest(_NEWPOSITIONS, globalBest, true);
                    System.out.println("\n\n" + getLocalName() + ">>> SOLO ENTRA LA PRIMERA VEZ: \nglobalBest:" + globalBest.getFitnessValue());
                    System.out.println("Posiciones: ");
                    System.out.println(getLocalName() + ">>> posiciones del  global best:");
                    utilitiMath.showArray(globalBest.getNewPosition());
                    System.out.println("\n");
                } else {
                    ModelGlobalBest mgbh = new ModelGlobalBest();
                    mgbh.setFitnessValue(globalBest.getFitnessValue());
                    mgbh.setNewPosition(globalBest.getNewPosition());
                    globalBest = utilitiMath.calculateGlobalBest(_NEWPOSITIONS, globalBest, true);
                    if (!globalBest.getFitnessValue().equals(mgbh.getFitnessValue())) {
                        System.out.println("\n\n" + getLocalName() + ">>> \nglobalBest:" + globalBest.getFitnessValue());
                        System.out.println("Posiciones: ");
                        System.out.println(getLocalName() + ">>> posiciones del  global best:");
                        utilitiMath.showArray(globalBest.getNewPosition());
                        System.out.println("\n");
                    } else {
                    }
                }

                utilitiMath.recalculateVelocity(_NEWPOSITIONS, globalBest, c1, c2);

                for (ModelParticle p : _NEWPOSITIONS) {

                    //status:
                    if (iterations == maxIt) {
                        p.setStop(true);
                    }

                    AID id = new AID();
                    id.setLocalName(p.getName());

                    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.addReceiver(id);
                    try {
                        mensaje.setContentObject(p);
                    } catch (IOException ex) {
                        Logger.getLogger(Particle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    send(mensaje);
                }

                _NEWPOSITIONS.clear();
                iterations += 1;

            }
        }

    }

}
