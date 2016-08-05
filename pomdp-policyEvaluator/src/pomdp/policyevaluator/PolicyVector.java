/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.util.ArrayList;

/**
 *
 * @author munna
 */
public class PolicyVector {
    
    int actionNumber;
    ArrayList<Double> vector = new ArrayList();
    
    PolicyVector(int actionNumber, ArrayList<Double> vector) {
        this.actionNumber = actionNumber;
        this.vector.addAll(vector);
    }
    
    public int getActionNumber() {
        return this.actionNumber;
    }
    
    public double getBeliefValue(double[] beliefState ) {
        double sum = 0;
        if(beliefState.length != vector.size() ) {
            System.err.println("Belief vector size and vector sizes not equal !!! belief vector size "  + beliefState.length + " value vector size " + vector.size() );
        }
            
        for(int i=0;i<beliefState.length;i++ ) {
            sum += vector.get(i) * beliefState[i];
        }
        return sum;
    }
    
}
