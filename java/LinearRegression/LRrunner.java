//Brandon Chan

package LinearRegression;

import java.io.File;

public class LRrunner {

	public static void main(String[] args) {
		
		// input and out folder paths
		final String INPUTFILELOCATION = "C:\\IBAPI\\TESTDATA\\input\\";
		final String OUTPUTFILELOCATION = "C:\\IBAPI\\TESTDATA\\output\\";
		
		// list all input files in the folder
		File listFile = new File("C:\\IBAPI\\TESTDATA\\input");
		String[] fileInFolder = listFile.list();

		String[] stockName;
		String stockNametemp = "";

		String eachStockFileToOpen = "";
		String output2CSVFile = "_TradeResult.csv";
		
		//String tempStockFile = "";
		String tempResultFile = OUTPUTFILELOCATION + "LR" + output2CSVFile;
		
		System.out.println("LR begins");
		
///////////////////////////////////////////////////////
		// major cycle through all files
		
		for (String eachFile : fileInFolder) {
			
			stockName = eachFile.split("\\.");
			stockNametemp = stockName[0];		
			System.out.println("working on: " + stockName[0]);

			eachStockFileToOpen = INPUTFILELOCATION + eachFile;
			
			LRSystem thisLRobj = new LRSystem(eachStockFileToOpen, tempResultFile);
			thisLRobj.setStockName(stockNametemp);
			thisLRobj.runBackTestLR();		
			//System.out.println("completed: " + stockName[0]);

		}				
			
///////////////////////////////////////////////////////
// this portion test for only one data file SPX3YEARS.CSV
		
	//	String tempStockFile = "C:\\IBAPI\\TESTDATA\\input\\SPX3YEARS.CSV";
//		String tempResultFile = OUTPUTFILELOCATION + "LR" + output2CSVFile;
				
		
		//LRSystem thisLRobj = new LRSystem(tempStockFile, tempResultFile);
	//	thisLRobj.setStockName("SPX");
	//	thisLRobj.runBackTestLR();
		
		
/////////////////////////////////////////////////////
		System.out.println("LR ends");
		
	}// end of main
	
}// end of class
