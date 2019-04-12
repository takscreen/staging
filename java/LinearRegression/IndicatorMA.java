//Brandon Chan

package LinearRegression;

import java.io.File;
import java.util.Scanner;

import util.Statistics;

public class IndicatorMA {

	protected String objStringFileName;
	protected int MALength = 0;
	private int objRowCounter = 0;
	private double stockArray[][];
	protected String dateArray[];

//////////////////////////////////////////////////////////
	public double getMAValue(int pos) {
		if (pos < MALength) {
			System.out.println("MA position must be >=0");
			return 0;
		}
		return stockArray[pos][5];
	}

//////////////////////////////////////
	public void calculateMA(int mALen) { // calculate moving average(MA) on closed price
		MALength = mALen;
		loadStockArray();
		// stockArray[objRowCounter][3] = closeP;
		// stockArray[objRowCounter][5] = 0; moving average colume

		//// System.out.println("objrowcounter:" + objRowCounter);

		double tempSum = 0;

		for (int i = 0; i < objRowCounter; i++) {

			tempSum += stockArray[i][3];
			if (i >= (MALength - 1)) {
				stockArray[i][5] = tempSum / MALength;
				tempSum -= stockArray[i - (MALength - 1)][3];
				// System.out.println("position I: " + i + " and MA: " + stockArray[i][5]);
			}

		}

		//// System.out.println("inside calculateMA method");
		// System.out.println("end of MA indicator");
	}

////////////////////////////////////////

	public double getMedianPrice(int pos) {
		double medianPrice = 0;
		Statistics medianObj = new Statistics();

		double[] tempArray;
		tempArray = new double[MALength];
		int j=0;
		
		//runs 50 times
		for (int i = 0; i < MALength; i++) {
			tempArray[i] = stockArray[pos-i][3]; // 3 for the closed price			
			
		}
		
		medianObj.setData(tempArray);
		medianPrice = medianObj.median();

		return medianPrice;
	}

//////////////////////////////////////
	protected void loadStockArray() {
		// System.out.println("MA length is: " + getMALenght());
		String fileName = getObjStringFileName();
		File stockFile = new File(fileName);
		
		try {
			Scanner inputData = new Scanner(stockFile);
			inputData.next(); // skip header line
			while (inputData.hasNext()) {
				inputData.next();
				objRowCounter++;
			}

			inputData.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("LR MA no such file");
		} // end catcher
		
		// set stock array size
		stockArray = new double[objRowCounter][6];
		dateArray = new String[objRowCounter];

		// split and insert data
		objRowCounter = 0;
		File stockFileData = new File(fileName);
		double openP, highP, lowP, closeP, volume;
		String[] valueString;
		String tempData;

		try {
			Scanner inputData = new Scanner(stockFileData);
			inputData.next(); // skip header line
			while (inputData.hasNext()) {
				tempData = inputData.next(); // load string to data
				valueString = tempData.split(",");

				openP = Double.parseDouble(valueString[1]);
				highP = Double.parseDouble(valueString[2]);
				lowP = Double.parseDouble(valueString[3]);
				closeP = Double.parseDouble(valueString[4]);
				volume = Double.parseDouble(valueString[5]);
				
				dateArray[objRowCounter] = valueString[0];
				//// System.out.println("date: " + dateArray[objRowCounter]);
				stockArray[objRowCounter][0] = openP;
				stockArray[objRowCounter][1] = highP;
				stockArray[objRowCounter][2] = lowP;
				stockArray[objRowCounter][3] = closeP;
				stockArray[objRowCounter][4] = volume;
				stockArray[objRowCounter][5] = 0;
				objRowCounter++;
			}

			inputData.close();
			//System.out.println("ma load data?");
		} catch (Exception e) {
		
			System.out.println("LR MA no such file");
		} // end catcher

		// System.out.println("working in loadStockArray now ; at the end");
	}

	/////////////////// constructors, getters and setters

	public IndicatorMA(String objStringFileName) {
		super();
		this.objStringFileName = objStringFileName;
		loadStockArray();

	}

	public IndicatorMA(String objStringFileName, int maLen) {
		super();
		this.objStringFileName = objStringFileName;
		this.MALength = maLen;
		loadStockArray();
		calculateMA(maLen);
	}

	public IndicatorMA() {
		
	}

	public int getMALength() {
		return MALength;
	}

	public String getObjStringFileName() {
		return objStringFileName;
	}

}
