/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.procurement.utils;

import java.text.DecimalFormat;

/**
 * Utility Class to Convert Amount to Word
 * <p>
 * Example Usage
 * <PRE>
 *  AmountUtils a=new AmountUtils();  
 *  String  s=a.AmountToWord(311947.33);
 *  System.out.println(s);
 * </PRE>
 * <p>
 *  The above would print:
 * <PRE>
 *  RUPEES THREE LAKH ELEVEN THOUSAND NINE HUNDRED 
 *  AND FORTY SEVEN AND THIRTY THREE PAISA ONLY.
 * </PRE>
 * @author USER
 */
public class AmountUtils {
    /**
     * Convert  Amount to Word
     * @param amo amount to Convert
     * @return String contains Amount in Words
     */
    public  String AmountToWord(double amo) {        
        int len, q = 0, r = 0;
        DecimalFormat format_a = new DecimalFormat("#.##");
        String amount = "" + format_a.format(amo);
        String ltr = " ";
        String Str = "RUPEES";
        if(amo==0){
            return Str+" ZERO ";
        }
        constNumtoLetter n = new constNumtoLetter();
        int num = 0;
        String sd = "";
        boolean paisa = false;
        boolean paisa1 = false;
        String st = amount;
        try {
            try {
                num = Integer.parseInt(st);
            } catch (Exception e) {
                num = Integer.parseInt(st.substring(0, st.indexOf(".")));
                sd = st.substring(st.indexOf(".") + 1);
                if (Integer.parseInt(sd) == 0) {
                    sd = "";
                } else if (sd.length() == 1) {
                    sd += "0";
                }
            }
            if (num <= 0) {
                Str += " Zero";
                num = Integer.parseInt(sd);
                paisa = true;
                paisa1 = true;
                sd = "";
            }
            while (num > 0) {
                if (paisa1) {
                    Str += " and";
                    paisa1 = false;
                }
                len = n.numberCount(num);

                //Take the length of the number and do letter conversion
                switch (len) {
                    case 8:
                        q = num / 10000000;
                        r = num % 10000000;
                        ltr = n.twonum(q);
                        Str = Str + ltr + n.digit[4];
                        num = r;
                        break;
                    case 7:
                    case 6:
                        q = num / 100000;
                        r = num % 100000;
                        ltr = n.twonum(q);
                        Str = Str + ltr + n.digit[3];
                        num = r;
                        break;
                    case 5:
                    case 4:
                        q = num / 1000;
                        r = num % 1000;
                        ltr = n.twonum(q);
                        Str = Str + ltr + n.digit[2];
                        num = r;
                        break;
                    case 3:
                        if (len == 3) {
                            r = num;
                        }
                        ltr = n.threenum(r);
                        Str = Str + ltr;
                        num = 0;
                        break;
                    case 2:
                        ltr = n.twonum(num);
                        Str = Str + ltr;
                        num = 0;
                        break;
                    case 1:
                        Str = Str + n.unitdo[num];
                        num = 0;
                        break;
                    default:
                        num = 0;
                        System.out.println("Exceeding Crore....No conversion");
                        System.exit(1);
                }
                if (num == 0 && !sd.equals("")) {
                    num = Integer.parseInt(sd);
                    paisa = true;
                    paisa1 = true;
                    sd = "";
                }
                if (num == 0) {
                    //System.out.print(""+Str.toUpperCase());
                    if (paisa) {
                        Str += " paisa";
                    }
                    Str += " only.";
                }
            }
        } catch (Exception e) {
            Str = "Conversion Error :" + e.getMessage();
        }
        return Str.toUpperCase();
    }

    class constNumtoLetter {

        String[] unitdo = {"", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten", " Eleven",
            " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen", " Eighteen", " Nineteen"};
        String[] tens = {"", "Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"};
        String[] digit = {"", " Hundred", " Thousand", " Lakh", " Crore"};
        int r;

        //Count the number of digits in the input number
        int numberCount(int num) {
            int cnt = 0;
            while (num > 0) {
                r = num % 10;
                cnt++;
                num = num / 10;
            }
            return cnt;
        }

        //Function for Conversion of two digit
        String twonum(int numq) {
            int numr, nq;
            String ltr = "";
            nq = numq / 10;
            numr = numq % 10;
            if (numq > 19) {
                ltr = ltr + tens[nq] + unitdo[numr];
            } else {
                ltr = ltr + unitdo[numq];
            }
            return ltr;
        }

        //Function for Conversion of three digit
        String threenum(int numq) {
            int numr, nq;
            String ltr = "";
            nq = numq / 100;
            numr = numq % 100;
            if (numr == 0) {
                ltr = ltr + unitdo[nq] + digit[1];
            } else {
                ltr = ltr + unitdo[nq] + digit[1] + " and" + twonum(numr);
            }
            return ltr;
        }
    }
}
