/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomdp.policyevaluator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import mdpwellness.ActionSet;

/**
 *
 * @author munna
 */
class ActionWindow extends JFrame {
    
    ArrayList<MyJButton> buttons = new ArrayList();
    
    ActionWindow(int totalMotivationLevels) {
        this.setLayout(new GridLayout(0,10));
        for(int i=0;i<totalMotivationLevels;i++) {
            double calorieBounds[] = ActionSet.getCalorieBounds(i);
            double paBounds[]      = ActionSet.getPABounds(i);
            for(int calories = (int)calorieBounds[0] ; calories<=calorieBounds[1];calories+=100) {
                for(double pa = paBounds[0];pa<=paBounds[1];pa+=0.5) {
                    Color buttonColor = getActionIntensityColor(i);
                    MyJButton newButton = new MyJButton(""+calories+"-"+pa,buttonColor);
                    newButton.setOpaque(true);
                    newButton.setBackground(buttonColor);
                    newButton.setOpaque(true);
                    this.add(newButton);
                    buttons.add(newButton);
                }
            }
        }
        this.setPreferredSize(new Dimension(1000,500));
        this.pack();
        this.setVisible(true);
        
    }
    
    void setActionSelected(String selectedAction) {
        for(MyJButton button : buttons ) {
            if(button.getText().equals(selectedAction)) {
                button.setBackground(Color.BLUE);
            }
            else  {
                button.setBackground(button.defaultColor);
            }
        }
    }
    
    Color getActionIntensityColor(int intensityLevel) {
        if(intensityLevel == 0 ) {
            return Color.GREEN;
        }
        if(intensityLevel == 1) {
            return Color.YELLOW;
        }
        if(intensityLevel == 2) {
            return Color.RED;
        }
        return Color.GRAY;
    }

    private static class MyJButton extends JButton {

        Color defaultColor;
        public MyJButton(String text,Color color) {
            super(text);
            this.defaultColor = color;
        }
    }
    
}
