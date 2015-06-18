package utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;

/**
 *
 * @author GB2
 */
public class PortFinder {  

    public PortFinder() {
        
    }    
    
    public ArrayList<String> getAvailablePorts(){
        ArrayList<String> result=new ArrayList<String>();
        Enumeration portList= CommPortIdentifier.getPortIdentifiers();
        while(portList.hasMoreElements()){           
             CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();                     
               if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {                    
                   result.add(portId.getName());
               }
        }        
        return result;
    }
}
