/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import static mdpwellness.WellnessDomain.SUBJECT;
import static mdpwellness.WellnessDomain.WEIGHT;

/**
 *
 * @author munna
 */
public class WellnessTerminalFunction implements TerminalFunction {

    public WellnessTerminalFunction() {
    }

    @Override
    public boolean isTerminal(State s) {
        int weight = s.getFirstObjectOfClass(SUBJECT).getIntValForAttribute(WEIGHT);
//        System.out.print("\n checking terminal state for " + weight );
        if(weight <= BodyParams.targetWeight)
        {
//            System.out.println(":TRUE");
            return true;
        }
//        System.out.println(":FALSE");
        return false;
    }
    
    
    
}
