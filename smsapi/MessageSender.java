/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smsapi;

/**
 *
 * @author USER
 */
public class MessageSender {
    public static void main(String[] args) {
        Sender.port="COM8";
    }
    
    @SuppressWarnings("empty-statement")
    public String sendMessage(String phone, String message) {
        String result;
        try {
            SMSClient sm = new SMSClient(0);
            sm.sendMessage(phone, message);
            while (sm.getMyThread().isAlive());
            result = Sender.reply;
        } catch (Exception e) {
            result="Error :"+ e.getMessage();
        }
        return result;
    }
    
}
