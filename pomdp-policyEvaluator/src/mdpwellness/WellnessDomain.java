/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.singleagent.SADomain;

/**
 *
 * @author munna
 */
class WellnessDomain {
    
    public static String WEIGHT = "weight";
    public static String SUBJECT = "subject";
    
    WellnessDomain()
    {
        
    }
    
    public Domain generateDomain()
    {
        SADomain domain = new SADomain();
        
        Attribute weight = new Attribute(domain,WEIGHT,AttributeType.INT);
        weight.setLims((int)UserInfo.targetWeight,(int)UserInfo.currentWeight);
        
        ObjectClass subject = new ObjectClass(domain,SUBJECT);
        subject.addAttribute(weight);
        
        
        return domain;
    }
    
}
