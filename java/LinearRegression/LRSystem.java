//Brandon Chan


package LinearRegression;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import util.Order;

// volatility trading system based on linear regression
public class LRSystem {

	private String inputFileName;
	private String resultFileName;
	private double stockArray[][];
	private Date dateArray[];
	private String stockName="";
	private final int OPEN = 0, HIGH = 1, LOW =2, CLOSE = 3;
	
	final int STARTBAR = 51;
	final int HOLDBARS = 5;
	final int STOPBARS = 1;
	//PROFITBARS = HOLDBARS/2 + 1;
	final int PROFITBARS = 3;
	final double positionSize = 5000;
	
	
	
///////////////////////////////////////////////////	
	//main method to run the back test per input File
	public void runBackTestLR() {
	
		final double entryFactor = .25;
		final double profitFactor = .9;
		final double exitFactor = .25;
		final double rangeFactor = 1;
		final double ivFactor = 2;
				
		final int ATRLen = 20;
		final int MALen = 50;
		//final int futureBar = 6;
		//final int regBars = 5;
		
		IndicatorATR thisATR20obj = new IndicatorATR(inputFileName, ATRLen);
		IndicatorMA thisMA50obj = new IndicatorMA(inputFileName,MALen);
		
		// 50 is pos, 5 is number of bars, 2 is low(1 is high), 6 is the future estimated based on the y=b0+b1*x and 6 is X.
		//public double getLRValue(int pos, int bars, int barPrice, int projectedValue) 		
		//	double LRValue = getLRValue(STARTBAR, regBars, LOW, futureBar);	
		//	double LowestHigh = getLowestHigh(STARTBAR, 4);
		//Date dateObj = new Date();	
		//		System.out.println("row 50 range is:"+ getTodayRange(50) +" and atr is: " +(thisATRobj.getATRValue(50)));

		boolean checkRange = false;
		boolean checkMA50 = false;
		boolean otherLRConditions = false;	
		boolean openTrade = false;
		double entryPrice=0;
		double targetOne =0;
		double targetTwo =0;
		double stopLoss = 0;
		
		ArrayList<Order> openOrders = new ArrayList<Order>();
		ArrayList<Order> tradeHistory = new ArrayList<Order>();
		
		SimpleDateFormat dF = new SimpleDateFormat("MM/dd/yyyy");
		
		//	String holdDate="";
		  //Date currentDate = new Date();  
	       //System.out.println("Current date is: "+currentDate); 
	       
	       //String dateShort = DateFormat.getDateInstance(DateFormat.SHORT).format(currentDate);  
			
		// start of iteration of the stock file using dateArray for rows same as stockArray
		for (int i = STARTBAR; i < dateArray.length; i++) {
			
			checkRange = false;
			checkMA50 = false;
			otherLRConditions = false;
			
			/// if there are open orders check all of them and exit the ones with the right conditions
			if (openOrders.size() > 0) {
				
				for (int j = 0; j < openOrders.size(); j++) {
					Order loopOrder = new Order();
					loopOrder = openOrders.get(0);
					
					// sell all first
					if (loopOrder.getTargetTwo() < stockArray[i][HIGH] && !loopOrder.isOrderCompleted()) {
						
						loopOrder.setExitDateInString(dF.format(dateArray[i]));
						
						loopOrder.setExitPrice(loopOrder.getTargetTwo());
						if (loopOrder.getAvgExitPrice() > 0) {
							loopOrder.setExitPrice((loopOrder.getAvgExitPrice()+loopOrder.getTargetTwo())/2);
						}
						
						loopOrder.setPL(loopOrder.getEntryShares()*loopOrder.getExitPrice() - loopOrder.getCost());
						
						loopOrder.setSoldAll(true);
						loopOrder.setSoldHalf(true);
						loopOrder.setOrderCompleted(true);
						
						tradeHistory.add(loopOrder);
												
						continue;
					}
					
					// sell half 
					if (loopOrder.getTargetOne() < stockArray[i][HIGH] && !loopOrder.isSoldHalf()) {
						loopOrder.setSoldHalf(true);
						
						loopOrder.setAvgExitPrice(loopOrder.getTargetOne());
						loopOrder.setShares(loopOrder.getShares() - (int)(loopOrder.getShares()/2));																		
						continue;
					}
					
					// stop exit
					if (loopOrder.getStopLoss() >= stockArray[i][LOW] && !loopOrder.isOrderCompleted()) {
						
						loopOrder.setExitDateInString(dF.format(dateArray[i]));
						
						loopOrder.setExitPrice(loopOrder.getStopLoss());
						
						if (loopOrder.getAvgExitPrice() > 0) {
							loopOrder.setExitPrice((loopOrder.getAvgExitPrice()+loopOrder.getStopLoss())/2);
						}
						
						loopOrder.setPL(loopOrder.getEntryShares()*loopOrder.getExitPrice() - loopOrder.getCost());
						
						loopOrder.setSoldAll(true);
						loopOrder.setSoldHalf(true);
						loopOrder.setOrderCompleted(true);
						
						tradeHistory.add(loopOrder);
						
						
						continue;
						
					}
				}	
			}// end of cycling all open orders
			
			// enter a new order if conditions right from yesterday
			if (openTrade) {
				Order newOrder =new Order();
				
				newOrder.setOID(i);
				newOrder.setEntryPrice(entryPrice);
				newOrder.setEntryDateInString(dF.format(dateArray[i]));
				newOrder.setSymbol(stockName);
				newOrder.setShares((int)(positionSize/entryPrice));
				newOrder.setEntryShares((int)(positionSize/entryPrice));
				newOrder.setCost(newOrder.getEntryShares()*newOrder.getEntryPrice());
				newOrder.setTargetOne(targetOne);
				newOrder.setTargetTwo(targetTwo);
				newOrder.setStopLoss(stopLoss);							
				
				newOrder.setPL(0);
				newOrder.setOrderCompleted(false);
				newOrder.setSoldHalf(false);
				newOrder.setSoldAll(false);
								
				openOrders.add(newOrder);
			
				entryPrice=0;
				targetOne=0;
				targetTwo=0;
				stopLoss=0;
				openTrade = false;
			}	
			
			// remove orders that are completed
			if (openOrders.size() > 0) {
				for (int j = 0; j < openOrders.size(); j++) {
					Order removeThis = new Order();
					removeThis = openOrders.get(j);
					if (removeThis.isOrderCompleted()) {
						openOrders.remove(j);
					}
				}
			}
			
			// check entry rules 3: range < rangefactor*ATR20
			if (getTodayRange(i) < (rangeFactor*thisATR20obj.getATRValue(i))) {
					checkRange = true;
			}
			
			// check entry rules 5: low > ma50
			if (stockArray[i][LOW] > thisMA50obj.getMAValue(i)) {
				checkMA50 = true;	
			}
			
			otherLRConditions = checkLRentryConditions(i);
			
			// check all conditions and next day bar to enter trade
			if (checkRange && checkMA50 && otherLRConditions) {				
				//System.out.println("trades trigger at row: "+ i);
				
				entryPrice = stockArray[i][CLOSE]+(entryFactor * thisATR20obj.getATRValue(i));
				
				if (stockArray[i+1][HIGH] > entryPrice) {											
					
					openTrade = true;
					
					targetOne = stockArray[i][HIGH] + (profitFactor*thisATR20obj.getATRValue(i));
					// target two need more work not accurate yet, ProfitBars ago is: 3 or holdbars/2 +1
					targetTwo = stockArray[i][HIGH] + (2* profitFactor*thisATR20obj.getATRValue(i));
					// stopbars is 1 based on the book. 
					stopLoss = getLowest(i, STOPBARS) - (exitFactor*thisATR20obj.getATRValue(i));
					
				}							
			}// end checking conditions														
		}// end of major cycle for loop
		
		//System.out.println("--------------trades history-------------------------");
		if (tradeHistory.size() > 0) {
			
			File thisFile = new File(resultFileName);
		//	boolean fileNotExist = true;
			String hole4write = "";
			
		//	if (thisFile.exists() & thisFile.isFile()) {
			//	System.out.println("file exists");	
		//		fileNotExist = false;
		//	}
		//	else {
			//	System.out.println("file dont exists");
		//		fileNotExist = true;
		//	}
			
			try {
			
				//hole4write = "blah,blah,blah,blah,blah\n";			
				// Date Enter/Date Exit/Stock/Share/ Entry Price/Exit Price/$ Profit% Gain	Gain/Loss	Reward/Risk
				
				for (Order thisOrder : tradeHistory)
				{
					//System.out.println(rangeOrder.toString());
					hole4write = thisOrder.getEntryDateInString()+","
							+ thisOrder.getExitDateInString()+","
							+ thisOrder.getSymbol()+","
							+ thisOrder.getEntryShares()+","
							+ thisOrder.getEntryPrice()+","
							+ thisOrder.getExitPrice()+","
							+ thisOrder.getPL()+"\n";
					//writing to csv files
					append(hole4write, thisFile);
					//System.out.println(hole4write);					
				}								
				
			} catch (Exception e) {
				System.out.println("can't open file and can't write");
			}
			
			
		}// end of writing all items to files
		
		
		
	}// end back test run
	
	
//////////////////////////////////////////////////
// constructors
	public LRSystem() {	}		
	
    public LRSystem(String inputFileName, String resultFileName) {
		super();
		this.inputFileName = inputFileName;
		this.resultFileName = resultFileName;
		loadStockArray(inputFileName);
	}

    
//////////////////////////////////////////////

    public double getLowest(int pos, int nBars) {
		
		double nextLowest = 0;
		
		nextLowest = stockArray[pos][LOW];
		
		for (int i = 0; i < nBars; i++) {
			if (nextLowest > stockArray[pos-i-1][LOW]) {
				nextLowest = stockArray[pos-i-1][LOW];
			}
		}
		
		return nextLowest;
	}
    
    
    //////////////////////
 public boolean checkLRentryConditions(int pos) {
	boolean enterTrade = false;
	final int monAndTue = 2;
	final int lhbars = 4;
	final int futureBar = 6;
	final int regBars = 5;
	
	// check entry rules 1
	Date dateObj = new Date();
	dateObj = dateArray[pos];	
	boolean checkDayofWeek = false;
	if (dateObj.getDay() <= monAndTue) {
		checkDayofWeek = true;
	}

	// if current high < lowest high of last 4 bars
	// check entry rules 2
	boolean checkHigh = false;
	if (stockArray[pos][HIGH] < getLowestHigh(pos, lhbars)) {
		checkHigh = true;
	}

	// check Low > LR5
	// check entry rules 4
	double LRValue = getLRValue(pos, regBars, LOW, futureBar);
	boolean checkLRvsLow = false;
	if (stockArray[pos][LOW] > LRValue) {
		checkLRvsLow = true;
	} 
	
	boolean yesterdayLow=false;
	if (stockArray[pos][HIGH] > stockArray[pos-1][LOW]) {
		yesterdayLow=true;
	}
	
	if (yesterdayLow && checkDayofWeek && checkHigh && checkLRvsLow) {
		enterTrade = true;
	}
	
	return enterTrade;
}
 
 ///////////////////
 public double getTodayRange(int pos) {
	
	return (stockArray[pos][HIGH] - stockArray[pos][LOW]);

	
}
 
 
 /////////////////
 
 public double getLowestHigh(int pos, int nBars) {
	double lowestHigh = 0;
	int high = 1;
	
	lowestHigh = stockArray[pos-1][high];
	
	for (int i = 1; i < nBars+1; i++) {
		if (lowestHigh > stockArray[pos-i][high]) {
			lowestHigh = stockArray[pos-i][high];
		}
	}
	
//	System.out.println("lowest high of the last " + nBars + " bars is: " + lowestHigh);
	
	return lowestHigh;
}
    
    
   public double getLRValue(int pos, int bars, int barPrice, int projectedValue) {
	
	  // x = the independ data points, bars
	   // y = prices
	   // equation y = b0 + b1*x
	   // to get b1: 
	   //		1. get average of X
	   //		2. get average of y
	   // 		3. difference from x for each point (x - Xavg) and sum all of them
	   //		4. difference from y for each point (y - Yavg) and sum all of them
	   // 		5. b1 = sum(y-Yavg) / sum(x-Xavg)
	   //
	   // to get b0, first get b1, then plug into the equation.  hence: Yavg = b0 + b1*Xavg
	   // once b0 and b1 is obtain, than calculate Y(price) with the next(future) point projection.  
	   // If bars if 5, next is 6*b1 + b0 = y(future/next price) 
	   
	   // barprice: open=0, high =1, low=2, close 3
	   
	   int[] xPos = new int[bars];
	   double[] yPrice = new double[bars];
	   
	   int xsum = 0;
	   double ysum = 0;
	   
	   for (int i = 0; i < bars; i++) {	   
		   xsum += bars-i;
		   xPos[i] = i+1;
		   
		   // barPrice is low so it should 2 in this case
		   ysum += stockArray[pos-i][barPrice];
		   yPrice[bars-i-1] = stockArray[pos-i][barPrice];  		   
	   }
	   
	   int xavg = xsum / bars;
	   double yavg = ysum / bars;
  	   	   
	//   System.out.println("x avg: "+ xavg + ", y avg: "+ yavg);
	   
	   //int[] xDiff = new int[bars];
	   //double[] yDiff = new double[bars];
	   
	   //int[] xSqDiff = new int[bars];
	   //double[] ySqDiff = new double[bars];
	   
	   int xSqSum =0;
	   double xySum = 0;
	   double slope = 0; // b1
	   double intercept =0; // b0
	   
	   for (int i = 0; i < bars; i++) {
		   xSqSum += (xPos[i]- xavg ) * (xPos[i]- xavg );
		   xySum += (xPos[i]- xavg )* (yPrice[i] - yavg);		   			   
	   }
	   
	   //System.out.println("XSQSUM: "+ xSqSum + "  YSQSUM: " + xySum);
	   
	   slope = xySum / xSqSum;
	   intercept = yavg - slope*xavg;
	   
	   //System.out.println("slope is b1: "+ xySum/xSqSum + " intercept b0: " + intercept);
	   double projection=0;
	   projection =intercept +(slope * projectedValue);
	   
	  // System.out.println("estimated value in postion:"+projectedValue+"  is: "+ projection);
	   
	   return projection;
}    // end getLRValue
   
   
   public void loadStockArray(String fName)
    {
    	String fileName = fName;
		File stockFile = new File(fileName);
		int rowCounter = 0;

		try {
			Scanner inputData = new Scanner(stockFile);
			inputData.next(); // skip header line
			while (inputData.hasNext()) {
				inputData.next();
				rowCounter++;
			}
			inputData.close();
		} catch (Exception e) {
			System.out.println("LR load array: no such file");
		} // end catcher
    	
		stockArray = new double[rowCounter][5];  // five for open, high, low, close, volume
		dateArray = new Date[rowCounter];

		try {
			
			double open, high, low, close, volume;			
			
			String[] valueString;
			String tempData;

			// use to parse date from strings
			SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
			Date tempDate = new Date();
			
			Scanner inputData = new Scanner(stockFile);
			int newObjCounter = 0;
			inputData.next(); // skip header line
			
			while (inputData.hasNext()) {

				tempData = inputData.next(); // load string to tempData
				valueString = tempData.split(","); // split of data at ","

				tempDate = formatDate.parse(valueString[0]);			
				open = Double.parseDouble(valueString[1]);
				high = Double.parseDouble(valueString[2]);
				low = Double.parseDouble(valueString[3]);
				close = Double.parseDouble(valueString[4]);
				volume = Double.parseDouble(valueString[5]);
				
				// load into class data
				dateArray[newObjCounter] = tempDate;				
				stockArray[newObjCounter][0] = open;
				stockArray[newObjCounter][1] = high;
				stockArray[newObjCounter][2] = low;
				stockArray[newObjCounter][3] = close;
				stockArray[newObjCounter][4] = volume;
				newObjCounter++;
			}
			inputData.close();
		} catch (Exception e) {

			System.out.println("LR load array: no such file");
		} // end catcher
    	
 	  	
    }
         
	private void write(String s, File f) throws IOException {
		FileWriter fw = new FileWriter(f);
		fw.write(s);
		fw.close();

	}

	private void append(String s, File f) throws IOException {
		FileWriter fw = new FileWriter(f, true);
		fw.write(s);
		fw.close();	
	}

////////////////////////////////////////////
	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}




	public String getResultFileName() {
		return resultFileName;
	}




	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}


	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
}
