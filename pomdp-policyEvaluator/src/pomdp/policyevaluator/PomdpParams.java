/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

/**
 *
 * @author munna
 */
public class PomdpParams {
    double transitionProbability[][][];
    double observationProbability[][][];
    
    double beliefDistribution[];
    
    PomdpParams(int states, int actions, int observations) {
        transitionProbability = new double[states][states][actions];
        observationProbability = new double[states][observations][actions];
    }
    
    void setInitialDistribution(double[] initialDistribution) {
        this.beliefDistribution = new double[initialDistribution.length];
        for(int i =0 ;i<initialDistribution.length;i++) {
            this.beliefDistribution[i] = initialDistribution[i];
        }
    }
    
    void addTransitionProbability(int fromState, int action, double[] probabilities) {
        for(int i=0;i<probabilities.length;i++) {
            transitionProbability[fromState][i][action] = probabilities[i];
        }
    }
    
    
    double getTransitionProbability(int fromState, int action, int toState) {
        return transitionProbability[fromState][toState][action];
    }
    
    void addObservationProbability(int fromState, int action, double[] probabilities) {
        for(int i=0;i<probabilities.length;i++) {
            observationProbability[fromState][i][action] = probabilities[i];
        }
    }
    
    double getObservationProbability(int fromState, int action, int observation) {
        return observationProbability[fromState][observation][action];
    }

    void updateDistribution(int actionNumber, int observationNumber) {
        double newBeliefDistribution[] = new double[beliefDistribution.length];
        for(int stateNumber=0;stateNumber<beliefDistribution.length;stateNumber++) {
            double ob1 = getObservationProbability(stateNumber, actionNumber, observationNumber);
            double stateSum = getTransitionSum(actionNumber, stateNumber);
            double normalization = getNormalization(actionNumber, observationNumber);
            
            newBeliefDistribution[stateNumber] = ob1 * stateSum / normalization;
        }
        
        for(int i=0;i<beliefDistribution.length;i++) {
            this.beliefDistribution[i] = newBeliefDistribution[i];
        }
    }
    
     double getNormalization(int actionNumber, int observationNumber) {
        double sum = 0;
        for(int s=0;s<beliefDistribution.length;s++) {
            for(int sprime = 0;sprime<beliefDistribution.length;sprime++) {
               double p1 = getObservationProbability(sprime, actionNumber, observationNumber);
               double p2 = getTransitionProbability(s,actionNumber, sprime);
               double p3 = beliefDistribution[s];
               sum += (p1 * p2 * p3);
            }
        }
        return sum;
    }
    
    double getTransitionSum(int actionNumber ,int stateNumber) {
        double sum = 0;
        for(int s=0;s<beliefDistribution.length;s++ ) {
            double t = getTransitionProbability(s,actionNumber,stateNumber);
            double p = beliefDistribution[s];
             sum += (t * p);
        }
        return sum;
    }
    
    double[] getBeliefDistribution() {
        return this.beliefDistribution;
    }
    
    
             
    
    
    
}
