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

    //numeros de iteraciones realizadas
    int iterations = 0;
    //maximo numero de iteraciones
    int maxIt = 50;
    //max population
    int n = 5;
    Float c1 = (float) 2.0;
    Float c2 = (float) 2.0;

    utility utilitiMath = new utility();

    List<ModelParticle> _NEWPOSITIONS = new ArrayList<>();
    ModelGlobalBest globalBest = new ModelGlobalBest();
    //List<ModelParticle> _LASTPOSITIONS = new ArrayList<>();

    @Override
    protected void setup() {
        addBehaviour(new returnParticle());
        addBehaviour(new getParticles());

    }

    private class getParticles extends CyclicBehaviour {

        @Override
        public void action() {
            //System.out.println(" Preparandose para recibir");    
            AID id = new AID();
            id.setLocalName("Control");

            ACLMessage msg = receive();
            String name;
            if (msg != null) {
                try {
                    name = ((ModelParticle) msg.getContentObject()).getName();
                    //System.out.println(getLocalName() + ">>> acaba de recibir un mensaje de " + name + " ");
                    ModelParticle particle = ((ModelParticle) msg.getContentObject());
                    _NEWPOSITIONS.add(particle);

                    ///calculo el personal global besr
                    //Here i want to get the first name of the object Person
                } catch (UnreadableException ex) {
                    System.out.println(getLocalName() + ">>> Algo salio mal ");
                    //Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }
    }

    private class returnParticle extends CyclicBehaviour {

        @Override
        public void action() {
            if (_NEWPOSITIONS.size() == n) {

                //si es primera iteracion y new positions esta lleno
                if (iterations == 0) {
                    //calcular global best:(se pone primero que cualquira, en este caso el
                    //primer valor del Vector de particulas es el best y ya despues se ajusta)
                    globalBest.setFitnessValue(_NEWPOSITIONS.get(0).getNewFitnessValue());
                    globalBest.setNewPosition(_NEWPOSITIONS.get(0).getNewPosition());
                    globalBest = utilitiMath.calculateGlobalBest(_NEWPOSITIONS, globalBest, true);

                    System.out.println("\n\n" + getLocalName() + ">>> SOLO ENTRA LA PRIMERA VEZ: \nglobalBest:" + globalBest.getFitnessValue());
                    System.out.println("Posiciones: ");
                    System.out.println(getLocalName() + ">>> posiciones del  global best:");
                    utilitiMath.showArray(globalBest.getNewPosition());
                    System.out.println("\n");
                } else {
                    //no es la primera vez y me traes las posiciones actualizadas junto con el new fitnes value
                    ModelGlobalBest mgbh = new ModelGlobalBest();
                    mgbh.setFitnessValue(globalBest.getFitnessValue());
                    mgbh.setNewPosition(globalBest.getNewPosition());
                    globalBest = utilitiMath.calculateGlobalBest(_NEWPOSITIONS, globalBest, true);
                    //System.out.println("They are equals?: "+globalBest.getFitnessValue().equals(mgbh.getFitnessValue()));
                    if (!globalBest.getFitnessValue().equals(mgbh.getFitnessValue())) {
                        System.out.println("\n\n" + getLocalName() + ">>> \nglobalBest:" + globalBest.getFitnessValue());
                        System.out.println("Posiciones: ");
                        System.out.println(getLocalName() + ">>> posiciones del  global best:");
                        utilitiMath.showArray(globalBest.getNewPosition());
                        System.out.println("\n");
                    } else {
                        //System.out.println("iteracion: " + iterations);
                    }
                }

                //Se calcula la nueva velocidad aqui
                utilitiMath.recalculateVelocity(_NEWPOSITIONS, globalBest, c1, c2);

                for (ModelParticle p : _NEWPOSITIONS) {

                    //status:
                    if (iterations == maxIt) {
                        p.setStop(true);
                    }

                    //position:
                    //p.setTestPositon(p.getTestPositon() + 1);
                    //System.out.println(getLocalName() + ">>> Se enviara a " + p.getName() + " una nueva velocidad. ya se ha calculado el global best");

                    //preparando para enviar el mensaje
                    //System.out.println(getLocalName() + ">>> Preparandose para enviar un mensaje a \'" + p.getName() + "\'");
                    AID id = new AID();
                    id.setLocalName(p.getName());

                    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
                    mensaje.addReceiver(id);
                    try {
                        mensaje.setContentObject(p);
                    } catch (IOException ex) {
                        Logger.getLogger(Particle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //envia mensaje
                    send(mensaje);
                }

                _NEWPOSITIONS.clear();
                //System.out.println(getLocalName() + ">>> particulas limpiadas, iteracion:(" + iterations + ") \n\n");
                iterations += 1;

            }
        }

    }

}
