//Brandon Chan

package LinearRegression;

import java.io.File;
import java.util.Scanner;

public class IndicatorATR {

	private String oFileName = "";
	private int oArrayLength = 0;
	private int oRowCounter = 0;
	private int atrLen = 0;
	double ATRArray[][];
	String dateArray[];

	
////////////////////////////////////////////////////
	public double getATRValue(int pos) {
		if (pos >= oRowCounter) {
			System.out.println("out of boundary");
			return 0;
		}
		if (pos < 0) {
			System.out.println("ATR position need to be greater than 0");
			return 0;
		}
		return ATRArray[pos][1];
	}

//////////////////////////////////////
	protected void loadATRArray() {

		String fileName = getoFileName();
		File stockFile = new File(fileName);

		try {
			Scanner inputData = new Scanner(stockFile);
			inputData.next();
			while (inputData.hasNext()) {
				inputData.next();
				oRowCounter++;
			}

			inputData.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("no such file");
		} // end catcher

		ATRArray = new double[oRowCounter][2];
		dateArray = new String[oRowCounter];
	}

////////////////////////////////////
	
	public void calculateATRdata(int ATRLength) {


		loadATRArray();
		atrLen = ATRLength;

		String fileName = getoFileName();
		File stockFile = new File(fileName);
		double stockArrayData[][];
		stockArrayData = new double[oRowCounter][5];

		int aCounter = 0;
		String[] valueString;
		double highP = 0, lowP = 0, closeP = 0;
		double lastCP = 0;
		double dailyTRavg = 0;

		try {
			Scanner inputData = new Scanner(stockFile);
			inputData.next();
			while (inputData.hasNext()) {

				dailyTRavg = 0;
				String data = inputData.next();
				valueString = data.split(",");

				dateArray[aCounter] = valueString[0];

				// openP = Double.parseDouble(valueString[1]);
				highP = Double.parseDouble(valueString[2]);
				lowP = Double.parseDouble(valueString[3]);
				closeP = Double.parseDouble(valueString[4]);
				// volume = Double.parseDouble(valueString[5]);

				dailyTRavg = Math.max(highP - lowP, dailyTRavg);
				if (aCounter > 0) {
					dailyTRavg = Math.max(Math.abs(highP - lastCP), dailyTRavg);
					dailyTRavg = Math.max(Math.abs(lowP - lastCP), dailyTRavg);
				}
				lastCP = closeP;

				ATRArray[aCounter][0] = dailyTRavg;
				// System.out.println("TR for file start with " + aCounter + " is: " +
				// ATRArray[aCounter][0]);
				aCounter++;
			}

			inputData.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("no such file");
		} // end catche

		// hereATRlength = getAtrLen();
		double hereSum = 0;

		for (int i = 0; i < (getAtrLen() - 1); i++) {

			if (i < 18) {
				ATRArray[i][1] = 0;
			}
			hereSum += ATRArray[i][0];
			// System.out.println("hereSum in loop:" + hereSum);
			// System.out.println("ATRArray inside loop:" + ATRArray[i][1]);
		}

		hereSum /= (getAtrLen() - 1);
		ATRArray[getAtrLen() - 2][1] = hereSum;

		for (int i = getAtrLen() - 1; i < oRowCounter; i++) {

			hereSum = (hereSum * 19 + ATRArray[i][0]) / 20;
			ATRArray[i][1] = hereSum;
			// System.out.println("inloop: here sum is: " + ATRArray[i][1]);
		}

		// System.out.println("end of ATR calculation");

	}


	///////////////////////////////////////////// constructors
	public IndicatorATR(String fileName) {
		setoFileName(fileName);
	}

	public IndicatorATR(String fileName, int atrLen) {
		setoFileName(fileName);
		setAtrLen(atrLen);
		calculateATRdata(atrLen);
	}

	public IndicatorATR(double sa[][], int atrlen) {
		this.ATRArray = sa;
		setAtrLen(atrlen);
	/*	System.out.println("open: "+ATRArray[0][0]+
				" high: "+ATRArray[0][1]+
				" low: "+ATRArray[0][2]+
				" close: "+ATRArray[0][3]+
				" volume: "+ATRArray[0][4]+
				"");*/		
	}
	

	///////////////////////////////////////////// constructors
	public String getoFileName() {
		return oFileName;
	}

	public void setoFileName(String oFileName) {
		this.oFileName = oFileName;
	}

	public int getoArrayLength() {
		return oArrayLength;
	}

	public void setoArrayLength(int oArrayLength) {
		this.oArrayLength = oArrayLength;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return oRowCounter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.oRowCounter = counter;
	}

	/**
	 * @return the atrLen
	 */
	public int getAtrLen() {
		return atrLen;
	}

	/**
	 * @param atrLen the atrLen to set
	 */
	public void setAtrLen(int atrLen) {
		this.atrLen = atrLen;
	}


}
