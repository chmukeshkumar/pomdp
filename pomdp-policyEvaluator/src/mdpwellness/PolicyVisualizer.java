/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.behavior.singleagent.Policy;
import java.awt.Dimension;
import java.util.HashMap;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author munna
 */
public class PolicyVisualizer extends ApplicationFrame{

    public PolicyVisualizer(String title) {
        super(title);
        
    }
    
    public void addData(HashMap<Integer,String>  policy)
    {
        JPanel jpanel = createDemoPanel(policy);
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
    
    private static JFreeChart createChart(CategoryDataset categorydataset)
    {
		JFreeChart jfreechart = ChartFactory.createStackedBarChart("Stacked Bar Chart Demo 1", "Category", "Value", categorydataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot categoryplot = (CategoryPlot)jfreechart.getPlot();
		StackedBarRenderer stackedbarrenderer = (StackedBarRenderer)categoryplot.getRenderer();
		stackedbarrenderer.setDrawBarOutline(false);
		stackedbarrenderer.setBaseItemLabelsVisible(true);
		stackedbarrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		return jfreechart;
    }
    
    private static CategoryDataset createDataset(HashMap<Integer,String> policy)
	{
            DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
            
            for(int weight = (int)UserInfo.currentWeight ; weight > (int) UserInfo.targetWeight; weight -- )
            {
                String actionName = policy.get(weight);
                if(actionName == null)
                {
                    continue;
                }
                 String tokens[] = actionName.split("-");
                double calories = Double.valueOf(tokens[0]);
                double pa = Double.valueOf(tokens[1]);

                double normCalories = calories/ActionSet.maxCalories;
                double normPa       = pa/ActionSet.maxPA;
                
                defaultcategorydataset.addValue(normCalories, "Calories", ""+ weight);
                defaultcategorydataset.addValue(normPa, "PA", ""+weight);
            }
//            for(WellnessPolicy p : policy)
//            {
//                
//            }
            
            return defaultcategorydataset;
	}
    
    public static JPanel createDemoPanel(HashMap<Integer,String>  policy)
    {
        JFreeChart jfreechart = createChart(createDataset(policy));
        return new ChartPanel(jfreechart);
    }
    
}
