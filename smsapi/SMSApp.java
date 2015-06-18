/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smsapi;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import smsapi.panels.SMSPanels;
import utils.PortFinder;

/**
 *
 * @author USER
 */
public class SMSApp extends JFrame {
    public static void main(String[] args) {        
        ArrayList<String> ports= new PortFinder().getAvailablePorts();
        JOptionPane.showMessageDialog(null,""+ports.toString(),"Ports",1);
    }

    public SMSApp(){
    }
    private SMSPanels p;
    
}
