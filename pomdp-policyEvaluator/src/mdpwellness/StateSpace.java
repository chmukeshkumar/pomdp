/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import java.util.HashMap;
import java.util.Map;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;

/**
 *
 * @author mchippa
 */
public class StateSpace {
    HashMap<Integer,State> states  = new HashMap();
    Domain domain;
    ObjectInstance subject;
    public void generateStateSpace(int lowerLimit, int upperLimit, Domain domain) {
        this.domain = domain;
        
        
        for(int weight = upperLimit; weight >= lowerLimit ; weight-- ) {
            addState(weight);
        }
        
        for(int weight = upperLimit; weight >= lowerLimit ; weight-- ) {
            State s = states.get(weight);
            ObjectInstance oc = s.getFirstObjectOfClass(SUBJECT);
            int w = oc.getIntValForAttribute(WEIGHT);
//            System.out.println("weight " + weight + " state value " + w);
        } 
        
    }
    
    protected State addState(int weight) {
        State s = new State();
        ObjectInstance subject = new ObjectInstance(domain.getObjectClass(SUBJECT),SUBJECT);
        subject.setValue(WEIGHT, weight);
        s.addObject(subject);
        states.put(new Integer(weight),s);
        
        return s;
    }
    
    protected State getInitialState() {
        return states.get((int)UserInfo.currentWeight);
    }
    
    protected State getState(int weight) {
        return states.get(weight);
    } 
            
}
