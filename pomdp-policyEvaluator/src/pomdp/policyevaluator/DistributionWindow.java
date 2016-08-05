/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mchippa
 */
class DistributionWindow  extends JFrame {
    private DefaultCategoryDataset beliefStates_exercise;
    private DefaultCategoryDataset beliefStates_nutrition;
    
    
    
    DistributionWindow(double[] nutritionDistribution, double[] exerciseDistribution) {
        ChartPanel nutritionPanel = getNutritionChartPanel();
        ChartPanel exercisePanel  = getExerciseChartPanel();
        
        this.setLayout(new BorderLayout());
        this.add(nutritionPanel,BorderLayout.NORTH);
        this.add(exercisePanel, BorderLayout.SOUTH);
        
        this.setPreferredSize(new Dimension(1000, 750));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        
        this.setVisible(true);
        
        update(nutritionDistribution, exerciseDistribution);
    }
    
    private ChartPanel getExerciseChartPanel() {
        beliefStates_exercise = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart( "Belief State Distribution","Motivation State","Probability",beliefStates_exercise, PlotOrientation.VERTICAL,true,true,false);
        ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    
    private ChartPanel getNutritionChartPanel() {
        beliefStates_nutrition = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart( "Nutrition Belief State Distribution","Motivation State","Probability",beliefStates_nutrition, PlotOrientation.VERTICAL,true,true,false);
        ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    
    public void update(double[] newNutritionDistribution, double[] newExerciseDistribution) {
        
        for(int i=0;i<newNutritionDistribution.length;i++) {
            beliefStates_nutrition.addValue(newNutritionDistribution[i],"M"+i,"p");
        }
        for(int i=0;i<newExerciseDistribution.length;i++) {
            beliefStates_exercise.addValue(newExerciseDistribution[i],"M"+i,"p");
        }
    }
    
}
