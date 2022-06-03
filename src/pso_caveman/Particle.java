package pso_caveman;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import math_caveman.utility;

/**
 * @author : ftm
 * @created : 2022-05-31
 *
 */
public class Particle extends Agent {

    /*first
    8,9,1
    9,6,1
    3,5,10
    10,2,10
    10,5,8
     */
    
    /*
    (82.5, 31.2)
    (82.4, 32.3)
    (81.2, 33.2)
    (42.9, 29.9)
    (44.2, 32.7)
    (63.2, 30.8)
    */

    enum MessageStatus {
        SEND,
        WAIT
    }

    MessageStatus ms = MessageStatus.WAIT;
    ModelParticle p = new ModelParticle();
    utility utilitiMath = new utility();

    protected void setup() {
        addBehaviour(new sendParticle());
    }

    private class sendParticle extends SimpleBehaviour {

        boolean fin = false;

        ///We get the parameters here  
        public void onStart() {
            Object[] args = getArguments();
            if (args != null) {
                //set the age
                System.out.println("\n");
                List<Float> initialPop = new ArrayList<>();
                p.setTestPositon(Integer.parseInt(args[0].toString()));
                p.setStop(false);
                p.setName(getLocalName());
                //set initial pop:
                for (int i = 0; i < args.length; ++i) {
                    initialPop.add(Float.parseFloat(args[i].toString()));
                }
                //calculate initial velocity:
                p.setVelocity(utilitiMath.initializateVelocity(initialPop));
                p.setNewPosition(utilitiMath.calculateNewPosition(initialPop, p.getVelocity()));
                p.setBestPosition(utilitiMath.calculateNewPosition(initialPop, p.getVelocity()));
                p.setNewFitnessValue(utilitiMath.calculateFitnessValue(p.getNewPosition()));
                p.setBestFitnessValue(utilitiMath.calculateFitnessValue(p.getNewPosition()));
                System.out.println(getLocalName() + "--- Data inicial: \n|nombre: " + p.getName() + "\n|posicion:" + p.getTestPositon() + "\n|will end? " + p.isStop());
                System.out.println(getLocalName() + "--- pop inicial");
                utilitiMath.showArray(initialPop);
                System.out.println(getLocalName() + "--- Velocidad inicial");
                utilitiMath.showArray(p.getVelocity());
                System.out.println(getLocalName() + "--- posicion inicial y mejor posicion(hasta ahora)");
                utilitiMath.showArray(p.getNewPosition());
                System.out.println(getLocalName() + "--- nuevo valor fitness:\n" + p.getNewFitnessValue());
                System.out.println(getLocalName() + "--- el mejor valor fitness(hasta ahora que empezamos):\n" + p.getBestFitnessValue());
                System.out.println("\n");
                ms = MessageStatus.SEND;
            } else {
                System.out.println("UNKNOWN >>>Particula inutil");
            }

        }

        public void action() {
            while (!p.isStop()) {
                if (ms == MessageStatus.SEND) {
                    ms = MessageStatus.WAIT;
                    AID id = new AID();
                    id.setLocalName("Receiver");
                    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.addReceiver(id);
                    try {
                        mensaje.setContentObject(p);
                    } catch (IOException ex) {
                        Logger.getLogger(Particle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    send(mensaje);

                } else {
                    {
                        AID id = new AID();
                        id.setLocalName(getLocalName());
                        ACLMessage msg = receive();
                        if (msg != null) {
                            try {
                                p = ((ModelParticle) msg.getContentObject());
                                p.setNewPosition(utilitiMath.calculateNewPosition(p.getNewPosition(), p.getVelocity()));
                                p.setNewFitnessValue(utilitiMath.calculateFitnessValue(p.getNewPosition()));
                                p.setBestFitnessValue(utilitiMath.calculateFitnessValue(p.getBestPosition()));
                                if (p.getNewFitnessValue() < p.getBestFitnessValue()) {
                                    p.setBestPosition(p.getNewPosition());
                                }
                                ms = MessageStatus.SEND;
                            } catch (UnreadableException ex) {
                                System.out.println(getLocalName() + "--- Algo salio mal ");
                            }
                        }

                    }
                }
            }
            System.out.println(getLocalName() + "--- Particula terminada");
            fin = true;

        }

        public boolean done() {
            return fin;
        }
    }
}
