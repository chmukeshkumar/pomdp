/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import mdpwellness.KevinHallModel;
import mdpwellness.UserInfo;
import mdpwellness.MDPWellness;
import mdpwellness.WellnessAction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 *
 * @author munna
 */
public class PomdpPolicyEvaluator {
    
    ArrayList<PolicyVector> policyVector_nutrition = new ArrayList();
    ArrayList<PolicyVector> policyVector_exercise = new ArrayList();
    
    
    private PomdpParams nutritionParams = new PomdpParams(5,3,2);
    private PomdpParams exerciseParams  = new PomdpParams(5,3,2);
    
    DistributionWindow nutritionDistributionWindow;
    DistributionWindow exerciseDistributionWindow;
    
    private Random generator = new Random();
    
    double nutritionVariance[] = {1000,750,500,250,100};
    double exerciseVariance[] = {1.0,0.75,0.5,0.25,0.1};
    
    double desireToPerform_nutrition = 1.0;
    double desireToPerform_exercise  = 1.0;
    
    ArrayList<Double> weightTrajectory = new ArrayList();
    
    PomdpPolicyEvaluator(String policyFileNutrition, String policyFileExercise) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(policyFileNutrition));
        String line;
        while((line = reader.readLine()) != null ) {
            int actionNumber = Integer.valueOf(line);
            String v  = reader.readLine();
            String[] tokens = v.split(" ");
            ArrayList<Double> vector = new ArrayList();
            for(int i=0;i<tokens.length;i++) {
                double value = Double.valueOf(tokens[i]);
                vector.add(value);
            }
            PolicyVector pv = new PolicyVector(actionNumber, vector);
            policyVector_nutrition.add(pv);
            reader.readLine();
        }
        
        reader = new BufferedReader(new FileReader(policyFileExercise));
        while((line = reader.readLine()) != null ) {
            int actionNumber = Integer.valueOf(line);
            String v  = reader.readLine();
            String[] tokens = v.split(" ");
            ArrayList<Double> vector = new ArrayList();
            for(int i=0;i<tokens.length;i++) {
                double value = Double.valueOf(tokens[i]);
                vector.add(value);
            }
            PolicyVector pv = new PolicyVector(actionNumber, vector);
            policyVector_exercise.add(pv);
            reader.readLine();
        }
        
//        ActionWindow actionWindow = new ActionWindow(3);
        nutritionParams.setInitialDistribution(new double[]{0.2,0.2,0.2,0.2,0.2});
        exerciseParams.setInitialDistribution(new double[]{0.2,0.2,0.2,0.2,0.2});
        
        nutritionParams.addTransitionProbability(0, 0, new double[] {0.75,0.25,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 0, new double[] {0.25,0.5,0.25,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 0, new double[] {0.0,0.1,0.8,0.1,0.0} );
        nutritionParams.addTransitionProbability(3, 0, new double[] {0.0,0.0,0.1,0.8,0.1} );
        nutritionParams.addTransitionProbability(4, 0, new double[] {0.0,0.0,0.0,0.1,0.9} );
        
        nutritionParams.addTransitionProbability(0, 1, new double[] {0.9,0.1,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 1, new double[] {0.3,0.4,0.3,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 1, new double[] {0.0,0.1,0.4,0.5,0.0} );
        nutritionParams.addTransitionProbability(3, 1, new double[] {0.0,0.0,0.1,0.3,0.6} );
        nutritionParams.addTransitionProbability(4, 1, new double[] {0.0,0.0,0.1,0.4,0.5} );
        
        nutritionParams.addTransitionProbability(0, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(3, 2, new double[] {0.0,0.0,0.2,0.4,0.4} );
        nutritionParams.addTransitionProbability(4, 2, new double[] {0.0,0.0,0.0,0.2,0.8} );
        
        exerciseParams.addTransitionProbability(0, 0, new double[] {0.75,0.25,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 0, new double[] {0.25,0.5,0.25,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 0, new double[] {0.0,0.1,0.8,0.1,0.0} );
        exerciseParams.addTransitionProbability(3, 0, new double[] {0.0,0.0,0.1,0.8,0.1} );
        exerciseParams.addTransitionProbability(4, 0, new double[] {0.0,0.0,0.0,0.1,0.9} );
        
        exerciseParams.addTransitionProbability(0, 1, new double[] {0.9,0.1,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 1, new double[] {0.3,0.4,0.3,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 1, new double[] {0.0,0.1,0.4,0.5,0.0} );
        exerciseParams.addTransitionProbability(3, 1, new double[] {0.0,0.0,0.1,0.3,0.6} );
        exerciseParams.addTransitionProbability(4, 1, new double[] {0.0,0.0,0.1,0.4,0.5} );
       
        exerciseParams.addTransitionProbability(0, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(3, 2, new double[] {0.0,0.0,0.2,0.4,0.4} );
        exerciseParams.addTransitionProbability(4, 2, new double[] {0.0,0.0,0.0,0.2,0.8} );
        
        nutritionParams.addObservationProbability(0,0,new double[] {0.1,0.9} );
        nutritionParams.addObservationProbability(1,0,new double[] {0.8,0.2} );
        nutritionParams.addObservationProbability(2,0,new double[] {1.0,0.0} );
        nutritionParams.addObservationProbability(3,0,new double[] {1.0,0.0} );
        nutritionParams.addObservationProbability(4,0,new double[] {1.0,0.0} );
        
        nutritionParams.addObservationProbability(0,1,new double[] {0.1,0.9} );
        nutritionParams.addObservationProbability(1,1,new double[] {0.3,0.7} );
        nutritionParams.addObservationProbability(2,1,new double[] {0.5,0.5} );
        nutritionParams.addObservationProbability(3,1,new double[] {1.0,0.0} );
        nutritionParams.addObservationProbability(4,1,new double[] {1.0,0.0} );
        
        nutritionParams.addObservationProbability(0,2,new double[] {0.0,1.0} );
        nutritionParams.addObservationProbability(1,2,new double[] {0.0,1.0} );
        nutritionParams.addObservationProbability(2,2,new double[] {0.0,1.0} );
        nutritionParams.addObservationProbability(3,2,new double[] {0.6,0.4} );
        nutritionParams.addObservationProbability(4,2,new double[] {0.8,0.2} );
        
        exerciseParams.addObservationProbability(0,0,new double[] {0.1,0.9} );
        exerciseParams.addObservationProbability(1,0,new double[] {0.8,0.2} );
        exerciseParams.addObservationProbability(2,0,new double[] {1.0,0.0} );
        exerciseParams.addObservationProbability(3,0,new double[] {1.0,0.0} );
        exerciseParams.addObservationProbability(4,0,new double[] {1.0,0.0} );
        
        exerciseParams.addObservationProbability(0,1,new double[] {0.1,0.9} );
        exerciseParams.addObservationProbability(1,1,new double[] {0.3,0.7} );
        exerciseParams.addObservationProbability(2,1,new double[] {0.5,0.5} );
        exerciseParams.addObservationProbability(3,1,new double[] {1.0,0.0} );
        exerciseParams.addObservationProbability(4,1,new double[] {1.0,0.0} );
        
        exerciseParams.addObservationProbability(0,2,new double[] {0.0,1.0} );
        exerciseParams.addObservationProbability(1,2,new double[] {0.0,1.0} );
        exerciseParams.addObservationProbability(2,2,new double[] {0.0,1.0} );
        exerciseParams.addObservationProbability(3,2,new double[] {0.6,0.4} );
        exerciseParams.addObservationProbability(4,2,new double[] {0.8,0.2} );
        
        
//        nutritionDistributionWindow = new DistributionWindow("Nutrition Distribution",nutritionParams.getBeliefDistribution());
//        exerciseDistributionWindow  = new DistributionWindow("Exercise Distribution", exerciseParams.getBeliefDistribution());
        String path = "./pomdpEvaluations/"+desireToPerform_nutrition+"-"+desireToPerform_exercise ;
        new File(path).mkdirs();
        int trailNumber = 1;
        for(;trailNumber <= 20;)
        {
            int time = 0;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path+"/trail"+trailNumber+".dat"));
            writer.write("Nutrition\tExercise\tWeight\n");
            writer.write("-\t-\t"+UserInfo.currentWeight+"\n" );
            double finalWeight = UserInfo.currentWeight;
            while(finalWeight > UserInfo.targetWeight) {
                int actionNumber_nutrition = getActionNumber(nutritionParams.getBeliefDistribution(),policyVector_nutrition);
                int actionNumber_exercise = getActionNumber(exerciseParams.getBeliefDistribution(),policyVector_exercise);
                System.out.println("Selected IntensityLevel Nutrition " + actionNumber_nutrition + 
                                   " Exercise " + actionNumber_exercise);

                MDPWellness mdpWellness = new MDPWellness(actionNumber_nutrition, actionNumber_exercise);
                String selectedAction = mdpWellness.getAction((int)UserInfo.currentWeight);
    //            actionWindow.setActionSelected(selectedAction);
                System.out.println(selectedAction);

                double nutritionCalories = Double.valueOf(selectedAction.split("-")[0]);
                double exerciseCalories  = Double.valueOf(selectedAction.split("-")[1]);


                int[] performanceEvaluation = getPerformance();

                int observationNumber_nutrition = performanceEvaluation[0];
                int observationNumber_exercise = performanceEvaluation[1];

                nutritionParams.updateDistribution(actionNumber_nutrition, observationNumber_nutrition);
                exerciseParams.updateDistribution(actionNumber_exercise, observationNumber_exercise);

                finalWeight = executeAction(nutritionCalories,nutritionParams.beliefDistribution,nutritionVariance,exerciseCalories, exerciseParams.beliefDistribution, exerciseVariance );
                System.out.println("Final Weight " + finalWeight);
                UserInfo.currentWeight = finalWeight;
                UserInfo.age += WellnessAction.timeStep/365.0;
                time = time + WellnessAction.timeStep;
                
                writer.write(nutritionCalories+"\t"+exerciseCalories+"\t"+finalWeight+"\n");
                printBeliefDistribution();
                
                if(time >= 2000) {
                    break;
                }
                
            }
            if(UserInfo.currentWeight <= UserInfo.targetWeight ) {
                trailNumber++;
            }
            writer.close();
            UserInfo.restoreDefaultValues();
//            nutritionDistributionWindow.update(nutritionParams.getBeliefDistribution());
//            exerciseDistributionWindow.update(exerciseParams.getBeliefDistribution());
        }
        
        
        
    }
    
    double executeAction(double nutritionCalories, double[] nutritionBelief, double[] nutritionVariance, 
                        double exerciseCalories,   double[] exerciseBelief,  double[] exerciseVariance) 
    {
        KevinHallModel model = new KevinHallModel(nutritionCalories, nutritionBelief, nutritionVariance,
                                                  exerciseCalories, exerciseBelief, exerciseVariance) ;
        
        StepHandler stepHandler = new StepHandler(){ 

            @Override
            public void init(double d, double[] doubles, double d1) {
                
            }

            @Override
            public void handleStep(StepInterpolator si, boolean bln) throws MaxCountExceededException {
                double t = si.getCurrentTime();
                double[] yy = si.getInterpolatedState();
                double weight = yy[0]+yy[1]+yy[2]+yy[3] ;
                weightTrajectory.add(weight);
            }
            
        };
        double[] y = new double[]{0.5,model.getInitialECF(),model.getInitialFatMass(),model.getInitialLeanMass(),0};
        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.1);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(model, 0, y, (WellnessAction.timeStep-1), y);
        double weight = y[0]+y[1]+y[2]+y[3] ;
        return weight;
    }
    
    int[] getPerformance() {
        int performance[] = new int[2];
         
        
        double random = generator.nextDouble();
        if(random < desireToPerform_nutrition ) {
            performance[0] = 0;
        }
        else {
            performance[0] = 1;
        }
        random = generator.nextDouble();
        if(random < desireToPerform_exercise ) {
            performance[1] = 0;
        }
        else {
            performance[1] = 1;
        }
        
//        System.out.println("Observation Nutrition:" + performance[0] + " Exercise "+performance[1]);
//        Scanner in = new Scanner(System.in);
//       
//        performance[0] = in.nextInt();
//        performance[1] = in.nextInt();
//       
        
        return performance;
    }
    
    void printBeliefDistribution() {
        System.out.println("-------Belief Distribution--------------");
        double nutritionBelief[] = nutritionParams.getBeliefDistribution();
        double exerciseBelief[]  = exerciseParams.getBeliefDistribution();
        
        for (int i=0;i<nutritionBelief.length;i++) {
            System.out.print(" " + nutritionBelief[i]);
        }
        System.out.println("");
        for (int i=0;i<exerciseBelief.length;i++) {
            System.out.print(" " + exerciseBelief[i]);
        }
        System.out.println("\n---------------------------------------");
    }
    
    int getActionNumber(double[] beliefDistribution, ArrayList<PolicyVector> policyVectors ) {
        double maxValue = Double.MIN_VALUE;
        int actionNumber = -1;
        for (PolicyVector pv : policyVectors ) {
            double value = pv.getBeliefValue(beliefDistribution);
            if(value > maxValue ) {
                maxValue = value;
                actionNumber = pv.getActionNumber();
            }
        }
        return actionNumber;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String policyFile_nutrition = args[0];
        String policyFile_exercise  = args[1];
        new PomdpPolicyEvaluator(policyFile_nutrition, policyFile_exercise);
    }
}
