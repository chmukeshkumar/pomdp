/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.QValue;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.statehashing.DiscretizingStateHashFactory;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;

/**
 *
 * @author munna
 */
public class MDPWellness {

    ValueIteration vi;
    Policy p;
    
    public MDPWellness(int nutritionMotivationLevel, int exerciseMotivationLevel)
    {
        WellnessDomain wellnessDomain = new WellnessDomain();
        Domain domain = wellnessDomain.generateDomain();
        
        StateSpace stateSpace = new StateSpace();
        stateSpace.generateStateSpace((int)UserInfo.targetWeight,(int) UserInfo.currentWeight, domain);
        
        ActionSet      actionSet = new ActionSet(nutritionMotivationLevel, exerciseMotivationLevel , domain, stateSpace);
        actionSet.createDefaultActionSet();
        
        WellnessRewardFunction rf = new WellnessRewardFunction();
        WellnessTerminalFunction tf = new WellnessTerminalFunction();
        DiscretizingStateHashFactory hashingFactory = new DiscretizingStateHashFactory(1.0);
        
        State initialState = getInitialState(domain);
        
        vi = new ValueIteration(domain,rf,tf,0.5,hashingFactory,0.01,100);
        vi.planFromState(initialState);
        
        
        p = new GreedyQPolicy((QComputablePlanner)vi);
//  
       
        
//        PolicyVisualizer viz = new PolicyVisualizer("Wellness Policy");
//        viz.addData(wellnessPolicy);
//        
//        WellnessPolicyImplementer policyImplementer  = new WellnessPolicyImplementer();
//        policyImplementer.implementPolicy(wellnessPolicy);
    }
    
    public String getAction(int weight) {
        
        List<State> allStates = vi.getAllStates();
        
        for(State s : allStates)
        {
            int w = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
            if(w == weight) {
                
            AbstractGroundedAction a = p.getAction(s);
            
            String actionName = a.actionName();
           
            return actionName;
            }
        }
        
        return null;
    }
    
    private State getInitialState(Domain domain)
    {
        State s = new State();
        ObjectInstance subject = new ObjectInstance(domain.getObjectClass(SUBJECT),SUBJECT);
        subject.setValue(WEIGHT, (int) UserInfo.currentWeight);
        s.addObject(subject);
        
        return s;
    }
   
    
}
