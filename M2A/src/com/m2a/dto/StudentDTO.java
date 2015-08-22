package com.m2a.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class StudentDTO extends CommonDTO {

	private int serialNo;
	private double salaryAmount;
	private String name;
	private Date dateOfBirth;
	private long mobileNo;
	private byte[] signature;
	private BigDecimal annualSalary;  
	
	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public double getSalaryAmount() {
		return salaryAmount;
	}
	public void setSalaryAmount(double salaryAmount) {
		this.salaryAmount = salaryAmount;
	}
	
	
	
	public long getMobileNo() {
		return mobileNo;
	}
	
	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public BigDecimal getAnnualSalary() {
		return annualSalary;
	}
	public void setAnnualSalary(BigDecimal annualSalary) {
		this.annualSalary = annualSalary;
	}
	@Override
	public String toString() {
		return "StudentDTO [serialNo=" + serialNo + ", salaryAmount="
				+ salaryAmount + ", name=" + name + ", dateOfBirth="
				+ dateOfBirth + ", mobileNo=" + mobileNo + ", signature="
				+ signature.length + ", annualSalary=" + annualSalary
				+ ", getErrMsg()=" + getErrMsg() + "]";
	}
	
	
}
