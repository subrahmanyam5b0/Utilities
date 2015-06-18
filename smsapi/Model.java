/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smsapi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author USER
 */
public class Model {
    
    private boolean checked=true;
    private String provierName;
    private String serviceCentre;
    private int centreId;
    private String centreCode;
    private String centreName;
    private String routeCode;
    private String routeName;
    private String unitCode;
    private String unitName;
    private String tDate;
    private String session;
    private String milkType;
    private double kgs;
    private double fat;
    private double snf;
    private double milkValue;
    private double netValue;
    private double benfits;
    private String phone;
    private String president;
    private String port;
    private String message;
    private String otherMessage;
    private double kgfat;
    private double kgsnf;
    private double _totKgFat;
    private double _totKgSnf;
    
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format_d=new SimpleDateFormat("dd/MM/yy");
    DecimalFormat format_de = new DecimalFormat("#.###");
    public String getMessage() {
        message="";
        try{
        Date d=format.parse(gettDate());
        String _d=format_d.format(d);
        message=unitName+","+centreCode+":"+centreName+","+_d+","+session+","+milkType+",KGS:"+new java.util.Formatter().format("%.1f", getKgs());
        message+=",FAT:"+new java.util.Formatter().format("%.1f", getFat());
        message+=",SNF:"+new java.util.Formatter().format("%.2f", getSnf());
        if(SMSApplet.showAmount)
        message+=",AMO:"+new java.util.Formatter().format("%.2f", getMilkValue());
        }
        catch(Exception e){
        }
        return message;
    }
    public String getMessage_unit() {
        message="";
        try{
        Date d=format.parse(gettDate());
        String _d=format_d.format(d);
        if(session.equalsIgnoreCase("%"))
            session="AM&PM";
        message=unitName+","+_d+","+session+","+milkType+",KGS:"+new java.util.Formatter().format("%.1f", getKgs());
        message+=",KGFAT:"+new java.util.Formatter().format("%.1f", getTotKgFat());
        message+=",KGSNF:"+new java.util.Formatter().format("%.2f", getTotKgSnf());
        if(SMSApplet.showAmount)
        message+=",AMO:"+new java.util.Formatter().format("%.2f", getMilkValue());
        }
        catch(Exception e){
            
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

    public String getProvierName() {
        return provierName;
    }

    public void setProvierName(String provierName) {
        this.provierName = provierName;
    }

    public String getServiceCentre() {
        return serviceCentre;
    }

    public void setServiceCentre(String serviceCentre) {
        this.serviceCentre = serviceCentre;
    }

    public int getCentreId() {
        return centreId;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
    }

    public String getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(String centreCode) {
        this.centreCode = centreCode;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public double getKgs() {
        return kgs;
    }

    public void setKgs(double kgs) {
        this.kgs = kgs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSnf() {
        return snf;
    }

    public void setSnf(double snf) {
        this.snf = snf;
    }

    public double getMilkValue() {
        return milkValue;
    }

    public void setMilkValue(double milkValue) {
        this.milkValue = milkValue;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public double getBenfits() {
        return benfits;
    }

    public void setBenfits(double benfits) {
        this.benfits = benfits;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getOtherMessage() {
        return otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }

    public double getKgfat() {
        kgfat = Double.valueOf(format_de.format(kgs * fat / 100));
        return kgfat;
    }

    public void setKgfat(double kgfat) {
        this.kgfat = kgfat;
    }

    public double getKgsnf() {
        kgsnf = Double.valueOf(format_de.format(kgs * snf / 100));
        return kgsnf;
    }

    public void setKgsnf(double kgsnf) {
        this.kgsnf = kgsnf;
    }

    public double getTotKgFat() {
        return _totKgFat;
    }

    public void setTotKgFat(double _totKgFat) {
        this._totKgFat = _totKgFat;
    }

    public double getTotKgSnf() {
        return _totKgSnf;
    }

    public void setTotKgSnf(double _totKgSnf) {
        this._totKgSnf = _totKgSnf;
    }
    
            
}
