package com.thida.smartcontractsample.dto;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@DataType
public class Account {
	@Property()
	@Size(min = 3, max = 50)
	private String accountID;
	
	@Property()
	@Min(value = 20_000)
	private double amount;
	
	public Account(String accountID, double amount) {
		super();
		this.accountID = accountID;
		this.amount = amount;
	}
	
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Account other = (Account) obj;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "Account [accountID=" + accountID + ", amount=" + amount + "]";
	}
		
}
