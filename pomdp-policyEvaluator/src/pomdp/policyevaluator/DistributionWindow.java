/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
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
    private DefaultCategoryDataset beliefStates;
    
    
    
    
    DistributionWindow(String title, double[] nutritionDistribution) {
        ChartPanel Panel = getNutritionChartPanel(title);
        
        
        this.setLayout(new BorderLayout());
        this.add(Panel,BorderLayout.CENTER);
        
        
        this.setPreferredSize(new Dimension(1000, 750));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        
        this.setVisible(true);
        
        update(nutritionDistribution);
    }
    
   
    
    private ChartPanel getNutritionChartPanel(String title) {
        beliefStates = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart( title ,"Motivation Distribution","Probability",beliefStates, PlotOrientation.VERTICAL,true,true,false);
        chart.removeLegend();
        
        CategoryPlot xyplot = (CategoryPlot) chart.getCategoryPlot();
        
        ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
        chartPanel.setMouseWheelEnabled(true);
        
        xyplot.getDomainAxis().setLabelFont(new Font("Ariel",Font.BOLD,18));
        return chartPanel;
    }
    
    public void update(double[] newNutritionDistribution) {
        
        beliefStates.addValue(newNutritionDistribution[0],"","Pre-contemplation");
        beliefStates.addValue(newNutritionDistribution[1],"","Contemplation");
        beliefStates.addValue(newNutritionDistribution[2],"","Preparation");
        beliefStates.addValue(newNutritionDistribution[3],"","Action");
        beliefStates.addValue(newNutritionDistribution[4],"","Maintenance");
        
    }
    
}
