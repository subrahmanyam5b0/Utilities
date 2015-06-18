package com.procurement.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * URL Request Sender
 *  @author   Subrahmanyam
 *  @version 1.0
 */
public class URLRequestSender implements Serializable {
    @SuppressWarnings("compatibility:-5310616249591495893")
    private static final long serialVersionUID = 1L;

    public URLRequestSender() {
        super();
    }
    private static int count;

    /**
     * Send a SMS By Using HTTP API.
     *
     * @param sourceURL HTTP API.
     * @return <code>String</code> Result of the URL Requested
     *                on Successfull Completion of URL Request <code>count</code> contains
     *                Number of SMS Credits Consumed.
     *  @throws  Exception While Sending URL Request.
     */
    public static String sendSMS(String sourceURL) throws Exception {
        count = 0;
        HttpURLConnection updateConnection = null;
        String res = "";
        try {
            URL updateURL = new URL(sourceURL);
            String sou = sourceURL;
            /* Setting Proxy Settings      */
            //System.setProperty("http.proxyHost", "192.168.1.1");
            // System.setProperty("http.proxyPort", "3128");
            updateConnection = (HttpURLConnection)updateURL.openConnection();
            updateConnection.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String line = null;
            int res_c = 0;
            // System.out.println("Reply Content is ");
            boolean chk = true;
            while ((line = in.readLine()) != null) {
                res += line;
                try {
                    res_c += Integer.parseInt(res.substring(res.length() - 3, res.length() - 2));
                    long phone_c =
                        Long.parseLong(sou.substring(sou.indexOf("&To=") + 4, sou.indexOf("&To=") + 4 + 10));
                    //System.out.println("UNITS:" + res_c);
                    //System.out.println("Phone:" + phone_c);
                } catch (Exception e) {
                    chk = false;
                }
            }
            if (chk) {
                count = res_c;
            }

        } catch (Exception e) {
            throw e;
        }        
        return res;
    }

    /**
     * Send an E-Mail.
     *
     * @param sourceURL Source PHP File URL.
     * @return <code>String</code> Result of the URL Requested.
     *
     *  @throws  Exception While Sending URL Request.
     */
    public static String sendMail(String sourceURL, String params) throws Exception {
        HttpURLConnection updateConnection = null;
        String returnString = null;
        if (sourceURL == null || sourceURL.isEmpty())
            sourceURL =
                    "http://greenbudsems.com/MailSender.php?mailContent=SAMPLE CONTENT&mailTo=info@greenbuds.co.in&mailFrom=info@greenbudsems.com&mailSubject=Sample Mail Test";
        if (params == null)
            params = "";
        try {
            //String params = "end="+end+"&companyid="+company+"&start="+start+"&session="+session+"&route="+route+"&zone="+zone+"";

            URL updateURL = new URL(sourceURL);
            // System.setProperty("http.proxyHost", "192.168.1.1");
            //System.setProperty("http.proxyPort", "3128");
            //System.out.println("URL is "+updateURL);
            updateConnection = (HttpURLConnection)updateURL.openConnection();

            updateConnection.setDoOutput(true);

            BufferedOutputStream out = new BufferedOutputStream(updateConnection.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();
            //System.out.println("Reading Response");
            BufferedReader in = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String line = "";
            String response = "";

            while ((line = in.readLine()) != null) {
                response = response + line;
                // System.out.println("Line is :"+line);
            }
            in.close();
            response = response.trim();

            if (updateConnection.getResponseCode() == 200) {
                if (response.indexOf("?exception?") >= 0) {
                    String status = "?exception?";

                    String message = response.substring(response.indexOf(status) + status.length(), response.length());

                    throw new Exception(message);
                }
                returnString = response;
                //System.out.println("" + returnString);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (updateConnection != null) {
                updateConnection.disconnect();
                updateConnection = null;
            }
        }
        // JOptionPane.showMessageDialog(this,""+returnString,"Sales",1);
        return returnString;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
