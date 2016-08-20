/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.Domain;

/**
 *
 * @author munna
 */
public class ActionSet {

    Domain domain;
    
    static double minCalories = 500;
    static double maxCalories = 5000;
    
    static double minPA = 1;
    static double maxPA = 3;
    
    int nutritionMotivationLevel;
    int exerciseMotivationLevel;
    
    private StateSpace stateSpace;
    
    ActionSet(int nutritionMotivationLevel, int exerciseMotivationLevel, Domain domain, StateSpace stateSpace) {
        this.nutritionMotivationLevel = nutritionMotivationLevel;
        this.exerciseMotivationLevel  = exerciseMotivationLevel;
        this.domain = domain;
        this.stateSpace = stateSpace;
    }
    
    public void createDefaultActionSet()
    {
        double calorieLimits[] = getCalorieBounds(this.nutritionMotivationLevel);
        double paLimits[]      = getPABounds(this.exerciseMotivationLevel);
        
        for(double calories = calorieLimits[0] ; calories <= calorieLimits[1] ; calories += 250)
        {
//            double pa = BodyParams.initialPA;
            for(double pa = paLimits[0] ; pa <= paLimits[1] ; pa += 0.1 )
            {
                new WellnessAction((int)calories+"-"+pa,calories, pa,this.domain, this.stateSpace);
            }
        }
//        new WellnessAction(minCalories+"-"+minPA,minCalories,minPA,this.domain);
//        new WellnessAction(minCalories+"-"+maxPA,minCalories,maxPA,this.domain);
//        
    }
    
    static public double[] getCalorieBounds(int motivationLevel) {
        switch(motivationLevel) {
            case 0:
                return new double[]{2900,maxCalories};
            case 1:
                return new double[]{1500,2800};
            case 2:
                return new double[]{minCalories,1400};
            default:
                break;
        }
        System.out.println("ERROR!!! Shouldnot reeach here un expected motivation Level in getCalorie Bounds " + motivationLevel);
        return new double[]{0,0};
    }
    
    static public double[] getPABounds(int motivationLevel) {
        switch(motivationLevel) {
            case 0:
                return new double[]{minPA,1.5};
            case 1:
                return new double[]{1.5,2.0};
            case 2:
                return new double[]{2.1,maxPA};
            default:
                break;
        }
        System.out.println("ERROR!!! Shouldnot reeach here un expected motivation Level in getCalorie Bounds " + motivationLevel);
        return new double[]{0,0};
    }
}
