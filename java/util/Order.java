// Brandon Chan

package util;

import java.util.Date;

public class Order {

	private int OID;
	private Date entryDate;
	private String entryDateInString;
	private Date exitDate;
	private String exitDateInString;
	private String symbol;
	private int shares;
	private int entryShares;
	private double entryPrice;
	private double exitPrice;
	private double PL;
	
	private double targetOne;
	private double targetTwo;
	private double stopLoss;
	private double avgExitPrice;
	private double cost;
	
	private boolean orderCompleted;
	private boolean soldHalf;
	private boolean soldAll;	

///////////// constructors
	public Order() {
		OID = 0;
		entryDate = null;
		entryDateInString="";
		exitDate=null;
		exitDateInString="";
		symbol="";
		shares=0;
		entryPrice=0;
		exitPrice=0;
		PL=0;

		targetOne=0;
		targetTwo=0;
		stopLoss=0;
		avgExitPrice=0;
		cost=0;

		orderCompleted=false;
		soldHalf=false;
		soldAll=false;
	}
//////////////////////////
	public int getOID() {
		return OID;
	}

	public void setOID(int oID) {
		OID = oID;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getEntryDateInString() {
		return entryDateInString;
	}

	public void setEntryDateInString(String entryDateInString) {
		this.entryDateInString = entryDateInString;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public String getExitDateInString() {
		return exitDateInString;
	}

	public void setExitDateInString(String exitDateInString) {
		this.exitDateInString = exitDateInString;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public double getEntryPrice() {
		return entryPrice;
	}

	public void setEntryPrice(double entryPrice) {
		this.entryPrice = entryPrice;
	}

	public double getExitPrice() {
		return exitPrice;
	}

	public void setExitPrice(double exitPrice) {
		this.exitPrice = exitPrice;
	}

	public double getPL() {
		return PL;
	}

	public void setPL(double pL) {
		PL = pL;
	}

	public double getTargetOne() {
		return targetOne;
	}

	public void setTargetOne(double targetOne) {
		this.targetOne = targetOne;
	}

	public double getTargetTwo() {
		return targetTwo;
	}

	public void setTargetTwo(double targetTwo) {
		this.targetTwo = targetTwo;
	}

	public double getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}

	public double getAvgExitPrice() {
		return avgExitPrice;
	}

	public void setAvgExitPrice(double avgExitPrice) {
		this.avgExitPrice = avgExitPrice;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isOrderCompleted() {
		return orderCompleted;
	}

	public void setOrderCompleted(boolean orderCompleted) {
		this.orderCompleted = orderCompleted;
	}

	public boolean isSoldHalf() {
		return soldHalf;
	}

	public void setSoldHalf(boolean soldHalf) {
		this.soldHalf = soldHalf;
	}

	public boolean isSoldAll() {
		return soldAll;
	}

	public void setSoldAll(boolean soldAll) {
		this.soldAll = soldAll;
	}
	public int getEntryShares() {
		return entryShares;
	}
	public void setEntryShares(int entryShares) {
		this.entryShares = entryShares;
	}

}
