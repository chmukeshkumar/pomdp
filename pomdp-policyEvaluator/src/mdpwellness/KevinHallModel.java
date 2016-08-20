/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 *
 * @author munna
 */
public class KevinHallModel implements FirstOrderDifferentialEquations{
    
    private double Ginit = 0.5;
    private double row_G = 4200; // kcal/Kg
    private double row_F = 9488.5277192; // kcal/kg
    private double row_L = 1816.4435936; // kcal/kg
    private final double beta_tef = 0.1;
    private final double beta_at  = 0.14;
    private final double tau_at   = 14; //days
    private final double gammaF = 3.107074568;
    private final double gammaL = 21.988527712;
    private final double etaL   = 229.44550656;
    private final double etaF   = 179.2542975;
    
    private final double _Na_ = 3220;
    private final double xi_Na = 3000;
    private final double Xi_Cl = 4000;
    
    private double kG;
    private double CI_b;
    private double K;
    
    double baseLineCalories;
    
    
    
    double nutritionCalories;
    double[] nutritionDistribution;
    double[] nutritionVariance;
    
    double exercisepal;
    double[] exerciseDistribution;
    double[] exerciseVariance;
    
    double ciFraction;
    double deltaNa;
    double pal_init;
    ArrayList<Double> pal_final;
    
    double initialWeight;
    double height;
    double age;
    String gender;
    
    Random generator;
    
    public KevinHallModel(double nutritionCalories, double[] nutritionDistribution, double[] nutritionVariance, double exerciseCalories, double[] exerciseDistribution, double[] exerciseVariance) 
    {
        this.initialWeight = UserInfo.currentWeight;
        this.ciFraction = 0.5;
        this.deltaNa = 0;
        this.pal_init = UserInfo.pal_init;
        
        this.height = UserInfo.height;
        this.age = UserInfo.age;
        this.gender = UserInfo.gender;
        
        baseLineCalories = getRMR(initialWeight) * pal_init;
        CI_b = baseLineCalories * 0.5;
        kG = CI_b/(Math.pow(Ginit,2));
        
        this.nutritionCalories = nutritionCalories;
        this.nutritionDistribution = nutritionDistribution;
        this.nutritionVariance = nutritionVariance;
        
        this.exercisepal = exerciseCalories;
        this.exerciseDistribution = exerciseDistribution;
        this.exerciseVariance = exerciseVariance;
        
        updateK();
        
        this.generator = new Random();
    }
    
    
    @Override
    public int getDimension() {
        return 5;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        double bodyWeight = y[0] + y[1] + y[2] + y[3];
        double rmr = getRMR(bodyWeight);
        double calories = getCalories();
        
        
        double delta = getDelta(rmr,bodyWeight);
        
        
        double ciIntake = calories*ciFraction;
        double P = getP(y[2]);
        double tef = getTEF(calories);
        double ee = K + gammaF*y[2] + gammaL*y[3] + delta*bodyWeight + tef + y[4] + etaL*yDot[3] + etaF*yDot[2];
        
        yDot[0] = (ciIntake - kG*Math.pow(y[0],2))/row_G;
        yDot[1] = (deltaNa - xi_Na*(y[1] - getInitialECF()) - Xi_Cl*(1 - ciIntake/CI_b))/_Na_;
        yDot[2] = ((1-P)*(calories - ee - row_G*yDot[0] ))/row_F;
        yDot[3] = ((P)*(calories - ee - row_G*yDot[0] ))/row_L;
        yDot[4] = (beta_at*(calories - baseLineCalories) - y[4])/tau_at;
    }
    
    private double getCalories()
    {
        double randomNutritionCalories = getRandom(nutritionDistribution,nutritionVariance);
        return nutritionCalories + randomNutritionCalories;
    }
    
    private double getRandom(double[] probDist, double[] var) {
       
        int id = getRandomID(probDist);
        
        double minValue = 0;
        double maxValue = var[id];
        if(id != 0) {
            minValue = var[id - 1];
        }
        
        double randomValue = minValue + (maxValue - minValue) * generator.nextDouble();
        
        return randomValue;
    }
    
    private int getRandomID(double[] probDist) {
        double random = generator.nextDouble();
        double[] probDistCum = new double[probDist.length];
        double sum = 0;
        for(int i =0;i<probDist.length;i++) {
            sum = sum + probDist[i];
            probDistCum[i] = sum;
        }
        
        int id = 0;
        for(;id<probDistCum.length;id++) {
            if(random < probDistCum[id]) {
                break;
            }
        }
        return id;
    }
    
    private double getPAL(double t)
    {
        int day = (int)t;
        return pal_final.get(day);
    }
    
    private void updateK()
    {
        double tef = getTEF(baseLineCalories);
        double rmr = getRMR(initialWeight);
        double delta = getDelta(pal_init,rmr,initialWeight);
        double at = 0;
        
        double fatMass = getInitialFatMass();
        double leanMass = getInitialLeanMass();
        K = baseLineCalories - (gammaF*fatMass + gammaL*leanMass + delta*initialWeight + tef + at );
    }
    
    public double getInitialLeanMass()
    {
        double fatmass = getInitialFatMass();
        double initECF = getInitialECF();
        return (initialWeight -Ginit - initECF - fatmass);
    }
    
    public double getInitialECF() {
        double initECF = 0.7*0.235*initialWeight;
        return initECF;
    }
    
    double getBaseLineCalories()
    {
        return this.baseLineCalories;
    }

    
    public double getInitialFatMass()
    {
        double fm = 0;
        if(gender.contains("F"))
        {
            fm = initialWeight/100*((0.14*age) + (39.96*Math.log(initialWeight/(Math.pow(height,2))) - 102.01));
        }
        else
        {
            fm = initialWeight/100*((0.14*age) + (37.31*Math.log(initialWeight/(Math.pow(height,2))) - 103.94));
            
        }
        
        return fm;
    }
    
    private double getP(double fatMass)
    {
        double C = 10.4*row_L/row_F;
        return C/(C+fatMass);
    }
    
    
    private double getTEF(double calories)
    {
        return beta_tef*(calories- baseLineCalories);
    }
    
    private double getDelta(double rmr,double BW)
    {
        double random = getRandom(exerciseDistribution, exerciseVariance);
        double pal = exercisepal - random;
        if(pal < 1.0) {
            pal = 1.0;
        }
        double delta = ((1 - beta_tef)*pal - 1)*rmr/BW;
        return delta;
    }
    
    public double getRMR(double weight)
    {
        double rmr = 0;
        if(gender.contains("F"))
        {
            rmr = 9.99*weight + 6.25*(height*100) - 4.92*age - 161;
        }
        else
        {
            rmr = 9.99*weight + 6.25*(height*100) - 4.92*age + 5.0;
        }
        
        return rmr;
    }

//    private double getDelta(double initialWeight) {
//        double r1 = generator.nextDouble();
//        double randomExerciseCalories = getRandomCalories(r1,exerciseDistribution,exerciseVariance);
//        double exCal =  exerciseCalories - randomExerciseCalories;
//        if(exCal < 0 ) {
//            exCal = 0;
//        }
//        return exCal/initialWeight;
//    }

    private double getDelta(double pal, double rmr, double weight) {
        double delta = ((1 - beta_tef)*pal - 1)*rmr/weight;
        return delta;
    }
}