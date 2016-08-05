/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;

/**
 *
 * @author munna
 */
public class WellnessRewardFunction implements RewardFunction {

    @Override
    public double reward(State s, GroundedAction a, State sprime) {
        int currentWeight = sprime.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
        int prevWeight    = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
        
        double calories = Double.valueOf(a.action.getName().split("-")[0]);
        int  diff = prevWeight - currentWeight;
        double reward = 0;
//        if(currentWeight <= BodyParams.targetWeight)
//        {
//            reward = 100;
//        }
        if(diff <= 0 )
        {
            return reward = 0;
        }
//        else
//        {
            reward = 100 - Math.abs(diff - 1)*10;
//        }
        
//        reward = 10 + (90/(BodyParams.targetWeight - BodyParams.initialWeight))*(currentWeight -BodyParams.initialWeight);
//        System.out.println("in reward function Before weight" + prevWeight + " Action Name " + a.actionName() + " After weight " + currentWeight + " Reward " + reward );
        
        return reward;
    }
    
    
}
