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
public class UserInfo {
    static public double currentWeight = 120;
    static public double height = 1.65;
    static public double age = 29;
    static public String gender = "Male";
    static public double pal_init = 1.2;
    static public double targetWeight = 75;

    public static void restoreDefaultValues() {
        currentWeight = 120;
        height = 1.65;
        age = 29;
        gender = "Male";
        pal_init = 1.2;
        targetWeight = 75;
    }
    
    public UserInfo(double bodyWeight, double height, double age, String gender, double pal_init, double targetWeight )
    {
        this.currentWeight = bodyWeight;
        this.height = height;
        this.age = age;
        this.gender = gender; 
        this.pal_init = pal_init;
        this.targetWeight = targetWeight;
    }
}
