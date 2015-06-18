/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smsapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author USER
 */
public class MicrosoftMessageSender {

    public String sendSMS(String phone, String message) {
        String result = "";
        try {
            String[] command = new String[3];
            command[0] = "cmd";
            command[1] = "/c";
            command[2] = "C: && cd Program Files && cd Microsoft SMS Sender && SMSSender.exe /i /p:+91" + phone + " /m:\"" + message + "\" ";

            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String s;
            while ((s = stdError.readLine()) != null) {
                if (result.isEmpty()) {
                    result += "Error :";
                }
                result += s + "\n";
            }
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

}
