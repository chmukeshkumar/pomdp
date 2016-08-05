/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import mdpwellness.BodyParams;
import mdpwellness.MDPWellness;

/**
 *
 * @author munna
 */
public class PomdpPolicyEvaluator {
    
    ArrayList<PolicyVector> policyVector_nutrition = new ArrayList();
    ArrayList<PolicyVector> policyVector_exercise = new ArrayList();
    
    
    private PomdpParams nutritionParams = new PomdpParams(5,3,2);
    private PomdpParams exerciseParams  = new PomdpParams(5,3,2);
    
    DistributionWindow distributionWindow;
    
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
        
        ActionWindow actionWindow = new ActionWindow(3);
        nutritionParams.setInitialDistribution(new double[]{0.2,0.2,0.2,0.2,0.2});
        exerciseParams.setInitialDistribution(new double[]{0.2,0.2,0.2,0.2,0.2});
        
        nutritionParams.addTransitionProbability(0, 0, new double[] {0.5,0.5,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 0, new double[] {0.25,0.5,0.25,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 0, new double[] {0.0,0.0,1.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(3, 0, new double[] {0.0,0.0,0.0,1.0,0.0} );
        nutritionParams.addTransitionProbability(4, 0, new double[] {0.0,0.0,0.0,0.0,1.0} );
        
        nutritionParams.addTransitionProbability(0, 1, new double[] {1.0,0.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 1, new double[] {0.0,0.5,0.5,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 1, new double[] {0.0,0.2,0.4,0.4,0.0} );
        nutritionParams.addTransitionProbability(3, 1, new double[] {0.0,0.0,0.2,0.2,0.6} );
        nutritionParams.addTransitionProbability(4, 1, new double[] {0.0,0.0,0.0,0.1,0.9} );
        
        nutritionParams.addTransitionProbability(0, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(1, 2, new double[] {0.0,1.0,0.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(2, 2, new double[] {0.0,0.0,1.0,0.0,0.0} );
        nutritionParams.addTransitionProbability(3, 2, new double[] {0.0,0.0,0.2,0.4,0.4} );
        nutritionParams.addTransitionProbability(4, 2, new double[] {0.0,0.0,0.0,0.2,0.8} );
        
        exerciseParams.addTransitionProbability(0, 0, new double[] {0.5,0.5,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 0, new double[] {0.25,0.5,0.25,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 0, new double[] {0.0,0.0,1.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(3, 0, new double[] {0.0,0.0,0.0,1.0,0.0} );
        exerciseParams.addTransitionProbability(4, 0, new double[] {0.0,0.0,0.0,0.0,1.0} );
        
        exerciseParams.addTransitionProbability(0, 1, new double[] {1.0,0.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 1, new double[] {0.0,0.5,0.5,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 1, new double[] {0.0,0.2,0.4,0.4,0.0} );
        exerciseParams.addTransitionProbability(3, 1, new double[] {0.0,0.0,0.2,0.2,0.6} );
        exerciseParams.addTransitionProbability(4, 1, new double[] {0.0,0.0,0.0,0.1,0.9} );
       
        exerciseParams.addTransitionProbability(0, 2, new double[] {1.0,0.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(1, 2, new double[] {0.0,1.0,0.0,0.0,0.0} );
        exerciseParams.addTransitionProbability(2, 2, new double[] {0.0,0.0,1.0,0.0,0.0} );
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
        
        
        distributionWindow = new DistributionWindow(nutritionParams.getBeliefDistribution(),
                                                    exerciseParams.getBeliefDistribution());
        
        Scanner input = new Scanner(System.in);
        
        while(true) {
            int actionNumber_nutrition = getActionNumber(nutritionParams.getBeliefDistribution(),policyVector_nutrition);
            int actionNumber_exercise = getActionNumber(exerciseParams.getBeliefDistribution(),policyVector_exercise);
            System.out.println("Selected IntensityLevel Nutrition " + actionNumber_nutrition + 
                               " Exercise " + actionNumber_exercise);
            MDPWellness mdpWellness = new MDPWellness(actionNumber_nutrition, actionNumber_exercise);
            String selectedAction = mdpWellness.getAction((int)BodyParams.initialWeight);
            actionWindow.setActionSelected(selectedAction);
            System.out.println("selected action " + selectedAction);
            System.out.println("Enter Actual Nutrition Intensity Level ( 0-2) ");
            int observationNumber_nutrition = input.nextInt();
            System.out.println("Enter Actual Exercise Intensity Level ( 0-2) ");
            int observationNumber_exercise = input.nextInt();
            
            
            nutritionParams.updateDistribution(actionNumber_nutrition, observationNumber_nutrition);
            exerciseParams.updateDistribution(actionNumber_exercise, observationNumber_exercise);
            
            printBeliefDistribution();
            
            distributionWindow.update(nutritionParams.getBeliefDistribution(),
                    exerciseParams.getBeliefDistribution());
        }
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
