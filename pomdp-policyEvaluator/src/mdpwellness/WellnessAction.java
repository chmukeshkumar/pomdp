/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

/**
 *
 * @author munna
 */
public class WellnessAction extends Action 
{    
    double calories;
    double pa;
    StateSpace stateSpace;
    static double uncertainty[] = {0.8,0.1,0.1};
    Random r = new Random();
    
    public static int timeStep = 21; // days
    
    WellnessAction(String name,double calories, double pa, Domain domain,StateSpace stateSpace)
    {
        super(name,domain,"");
        this.calories = calories;
        this.pa       = pa;
        this.stateSpace = stateSpace;
    }
    
    
    private double performAction(double currentWeight)
    {
        KevinHallModelOLD khm = new KevinHallModelOLD(currentWeight,
                                                UserInfo.height,
                                                UserInfo.age,
                                                UserInfo.gender,
                                                UserInfo.pal_init);
        
        double actionCalories;
        double actionPA;       
        if(this.calories == 0 )
        {
            actionCalories = khm.getBaseLineCalories();
        }
        else
        {
            actionCalories = this.calories;
        }
        
        if(this.pa == 0 )
        {
            actionPA = UserInfo.pal_init;
        }
        else
        {
            actionPA = this.pa;
        }
        ArrayList<Double> setCalories = new ArrayList();
        ArrayList<Double> setPA       = new ArrayList();
        
        for(int i=0;i<timeStep;i++)
        {
            setCalories.add(actionCalories);
            setPA.add(actionPA);
        }
        khm.setCurrentParameters(setCalories, setPA);
        double[] y = new double[]{0.5,khm.getInitialECF(),khm.getInitialFatMass(),khm.getInitialLeanMass(),0};
        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1);
        integrator.integrate(khm, 0, y, (timeStep-1), y);
        double finalWeight = (y[0]+y[1]+y[2]+y[3]);
                    
//        System.out.println(" initial Weight "+currentWeight+" "+calories+" "+pa + " final Weight" + finalWeight );
        return finalWeight;
    }
    
    

    @Override
    protected State performActionHelper(State s, String[] params) 
    {
        int currentWeight = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
        double newWeight     = performAction(currentWeight);
        
        double rand = r.nextDouble();
        double sum = 0;
        int i=0;
        for(;i<uncertainty.length;i++)
        {
            sum+=uncertainty[i];
            if(rand < sum)
            {
                break;
            }
        }
        
        if(i == 1)
        {
            newWeight -= 2; // reduce 2 lbs
        }
        else if( i == 2)
        {
            newWeight += 2;
        }
        
        s.getFirstObjectOfClass(SUBJECT).setValue(WEIGHT, (int)newWeight);
        
        return s;
    }
    
    @Override
    public List<TransitionProbability> getTransitions(State s, String[] params)
    {
        List<TransitionProbability> result = new ArrayList();
        int currentWeight = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
        int finalWeight   = (int)performAction(currentWeight);
        
//        if(finalWeight > currentWeight )
//        {
//            return result;
//        }
        
        if(currentWeight >= UserInfo.targetWeight && currentWeight <= UserInfo.currentWeight)
        {
//             System.out.println(" Adding new state at " + currentWeight + " for Action " + this.calories + " " + this.pa + " for " + finalWeight);
            State newState1 = getOrCreateState(finalWeight);
            result.add(new TransitionProbability(newState1, uncertainty[0]));
            
            if(uncertainty[1] != 0)
            {
//                System.out.println(" Adding new state at " + currentWeight + " for Action " + this.calories + " " + this.pa + " for " + (finalWeight - 2));
                State newState2 = getOrCreateState(finalWeight - 2);
                result.add(new TransitionProbability(newState2, uncertainty[1]));
            }
            
            if(uncertainty[2] != 0)
            {
//                System.out.println(" Adding new state at " + currentWeight + " for Action " + this.calories + " " + this.pa + " for " + (finalWeight + 2));
                State newState3 = getOrCreateState(finalWeight + 2);
                result.add(new TransitionProbability(newState3, uncertainty[2]));
            }
        }
        return result;
    }
    
    State getOrCreateState(int weight) {
        State s = stateSpace.getState(weight);
        if(s == null) {
            s = stateSpace.addState(weight);
        }
        return s;
    }
}