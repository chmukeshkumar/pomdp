/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

/**
 *
 * @author munna
 */
class WellnessPolicy {
    String stateName;
    double calories;
    double pa;
    
    WellnessPolicy(String s, double c, double pa)
    {
        this.stateName = s;
        this.calories = c;
        this.pa = pa;
    }
}
