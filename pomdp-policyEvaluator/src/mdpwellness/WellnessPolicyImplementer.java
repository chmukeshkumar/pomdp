/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author munna
 */
public class WellnessPolicyImplementer {
    
    Random r = new Random();
    
    BodyWeightVisualizer viz = new BodyWeightVisualizer("Body Weight Trajectories");
    
    WellnessPolicyImplementer()
    {
        
    }
    
    private double addUncertainity(double weight)
    {
        double rand = r.nextDouble();
        double sum = 0;
        int i =0;
        for(;i<WellnessAction.uncertainty.length;i++)
        {
            sum = sum + WellnessAction.uncertainty[i];
            if(rand < sum)
            {
                break;
            }
        }

        if(i == 1)
        {
            return weight - 2;
        }
        else if( i == 2)
        {
            return weight + 2;
        }
        
        return weight;
    }
    
    public void implementPolicy(HashMap<Integer,String> policy)
    {
        for(int trial =0;trial<1;trial++)
        {
            System.out.println("-------------------Implementing Policy--------------- ");
            double weight = UserInfo.currentWeight;
            int time = 0;

            XYSeries newSeries = new XYSeries(trial);
            newSeries.add(time,weight);
            do 
            {
                String action = policy.get((int)weight);
                System.out.println(""+(int)weight + " " + action);
                if(action == null)
                {
                    continue;
                }
                double actionCalories = Double.valueOf(action.split("-")[0]);
                double actionPA = Double.valueOf(action.split("-")[1]);       

                KevinHallModelOLD khm = new KevinHallModelOLD(weight,
                                                        UserInfo.height,
                                                        UserInfo.age,
                                                        UserInfo.gender,
                                                        UserInfo.pal_init);

                ArrayList<Double> setCalories = new ArrayList();
                ArrayList<Double> setPA       = new ArrayList();

                int timeStep = WellnessAction.timeStep ;

                for(int i=0;i<timeStep;i++)
                {
                    setCalories.add(actionCalories);
                    setPA.add(actionPA);
                }
                khm.setCurrentParameters(setCalories, setPA);
                double[] y = new double[]{0.5,khm.getInitialECF(),khm.getInitialFatMass(),khm.getInitialLeanMass(),0};
                FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1);
                integrator.integrate(khm, 0, y, (timeStep-1), y);
                weight = addUncertainity(y[0]+y[1]+y[2]+y[3]);
                time = time + timeStep;
//                System.out.println("time " + time + " weight " + weight);
                newSeries.add(time,weight);

            }while (weight > UserInfo.targetWeight );
        
            viz.addSeries(newSeries);
        }
    }
}
