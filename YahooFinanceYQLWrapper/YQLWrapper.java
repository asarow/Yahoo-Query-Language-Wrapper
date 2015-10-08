package YahooFinanceYQLWrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;
import java.util.ArrayList;

/**
 * A wrapper for the Yahoo! Query Language Finance database.
 * 
 * @author Amandeep Sarow
 */
public class YQLWrapper {
    private static final String YQL_BEGIN = 
	"https://query.yahooapis.com/v1/public/" +
        "yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20" +	
	"symbol%20in%20(%22";
    private static final String YQL_END = 
	"%22)&diagnostics=true&env=store%3A%" +
        "2F%2Fdatatables.org%2Falltableswithkeys";
    private static final String YQL_STATEMENT_BEGIN =
	"https://finance.yahoo.com/q/";
    private static final String YQL_STATEMENT_MID =
	"?s=";

    private static final String 
	FIFTY_DAY_MOVING_AVG      = "<FiftydayMovingAverage>",
	TWOHUNDRED_DAY_MOVING_AVG = "<TwoHundreddayMovingAverage>",
	ASK                       = "<Ask>",
	AVG_DAILY_VOLUME          = "<AverageDailyVolume>",	
	BID                       = "<Bid>",
	BOOK_VALUE                = "<BookValue>",
	CHANGE                    = "<Change>", 
	CHANGE_PERCENT            = "<PercentChange>",
	CHANGE_YEAR_LOW           = "<ChangeFromYearLow>",
	CLOSE                     = "<PreviousClose>",
	DAYS_HIGH                 = "<DaysHigh>",
	DAYS_LOW                  = "<DaysLow>",
	DIVIDEND                  = "<DividendShare>",
	EARNINGS_SHARE            = "<EarningsShare>",
	EBITDA                    = "<EBITDA>",
	EPS_ESTIMATE_CY           = "<EPSEstimateCurrentYear>",
	EPS_ESTIMATE_NQ           = "<EPSEstimateNextQuarter>",
	EPS_ESTIMATE_NY           = "<EPSEstimateNextYear>",
	LAST_TRADE_PRICE          = "<LastTradePriceOnly>",
	MARKETCAP                 = "<MarketCapitalization>",
	NAME                      = "<Name>",
	OPEN                      = "<Open>",
	PE_RATIO                  = "<PERatio>",
	PEG_RATIO                 = "<PEGRatio>",
	SHORT_RATIO               = "<ShortRatio>",
	YEAR_HIGH                 = "<YearHigh>",
	YEAR_LOW                  = "<YearLow>",
	BS                        = "bs",
	IS                        = "is",
	CF                        = "cf";

    /** 
     *	Retrieves the asking price for a given stock ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The last known asking price for a given stock.
     */
    public static double askPrice(String ticker) {
        return Double.parseDouble(findDataInXML(ASK, ticker));
    }
    
    /** 
     * Retrieves the average daily volume traded for a given stock ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The average amount of stock traded in the most previous 
     *               day.
     */
    public static int averageDailyVolume(String ticker) {
	return Integer.parseInt(findDataInXML(AVG_DAILY_VOLUME, ticker));
    }

    /** 
     * Retrieves the last best bid price for a given stock ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The most recent "best" bid price.
     */
    public static double bidPrice(String ticker) {
	return Double.parseDouble(findDataInXML(BID, ticker));
    }
    
    public static double bookValue(String ticker) {
	return Double.parseDouble(findDataInXML(BOOK_VALUE, ticker));
    }

    /** 
     * Retrieves the dollar amount change in the price of a stock in relation
     * to the opening price. The amount is returned as a String due to either
     * a plus (+) or minus (-) indicating the direction of change.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The change in stock price over a day.
     */
    public static String changeInPrice(String ticker) {
	return findDataInXML(CHANGE, ticker);
    }
    
    /** 
     * Retrieves the dollar amount change in the price of a stock over one
     * year. The amount is returned as a String due to either a plus (+) or 
     * minus (-) indicating the direction of change.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The change in stock price over a single year.
     */
    public static String changeFromYearLow(String ticker) {
	return findDataInXML(CHANGE_YEAR_LOW, ticker);
    }

    /** 
     * Retrieves the closing price of a stock from the most recent trading day.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The price of the last stock traded.
     */
    public static double close(String ticker) {
	return Double.parseDouble(findDataInXML(CLOSE, ticker));
    }

    /** 
     * Retrieves the highest price of a stock trade from a single day.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The highest priced stock trade in a given day.
     */
    public static double dayHigh(String ticker) {
	return Double.parseDouble(findDataInXML(DAYS_HIGH, ticker));
    }

    /** 
     * Retrieves the lowest price of a stock trade from a single day.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The lowest priced stock trade in a given day.
     */
    public static double dayLow(String ticker) {
	return Double.parseDouble(findDataInXML(DAYS_LOW, ticker));
    }
    
    /** 
     * Retrieves the most recent dividend payout in dollar amount.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The most recent dividend payout to shareholders.
     */
    public static double dividend(String ticker) {
	return Double.parseDouble(findDataInXML(DIVIDEND, ticker));
    }
    
    /** 
     * Retrieves the earnings-per-share for a given stock.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The most recently published EPS.
     */
    public static double earningsShare(String ticker) {
	return Double.parseDouble(findDataInXML(EARNINGS_SHARE, ticker));
    } 

    /** 
     * Retrieves the most recent calculated EBITDA (Earnings Before Interest
     * Tax, Depreciation, and Amortization). The EBITDA is returned as a String
     * due to specification of millions(M) or Billions(B)
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The calculated EBITDA for the public corporation.
     */
    public static String EBITDA(String ticker) {
	return findDataInXML(EBITDA, ticker);
    }

    /** 
     * Retrieves the forecasted EPS estimate for the current year.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The most recent published EPS estimate for the current 
     *               year.
     */
    public static double EPSEstimateCurrentYear(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_CY, ticker));
    }

    /** 
     * Retrieves the forecasted EPS estimate for the next quarter.
     *
     * @param ticker The stock ticker for the public corporation..
     * @return       The most recent published EPS estimate for the next 
     *               quarter.
     */
    public static double EPSEstimateNextQuarter(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_NQ, ticker));
    }

    /** 
     * Retrieves the forecasted EPS estimate for the next year.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The most recent published EPS estimate for the next year.
     */
    public static double EPSEstimateNextYear(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_NY, ticker));
    }

    /** 
     * Retrieves the moving average stock price over the past 50 days.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The 50-day moving average stock price.
     */
    public static double fiftyDayMovingAverage(String ticker) {
	return Double.parseDouble(findDataInXML(FIFTY_DAY_MOVING_AVG, ticker));
    }

    /** 
     * Retrieves the most recent calculated market capitalization of the public
     * corporation for the given ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The market capitalization of the public corporation.
     */
    public static String marketCap(String ticker) {
	return findDataInXML(MARKETCAP, ticker);
    }

    /** 
     * Retrieves the corporate name for a given stock ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The corporate name.
     */
    public static String companyName(String ticker) {
	return findDataInXML(NAME, ticker);
    }

    /** 
     * Retrieves the opening stock price from the most recent trading day.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The opening stock price from the trading day.
     */
    public static double open(String ticker) {
	return Double.parseDouble(findDataInXML(OPEN, ticker));
    }

    /** 
     * Retrieves the most recent calculated Price-Earnings ratio.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The Price-Earnings ratio.
     */
    public static double PERatio(String ticker) {
	return Double.parseDouble(findDataInXML(PE_RATIO, ticker));
    }

    /** 
     * Retrieves the most recent calculated Price-Earnings Growth ratio.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The Price-Earnings Growth ratio.
     */
    public static double PEGRatio(String ticker) {
	return Double.parseDouble(findDataInXML(PEG_RATIO, ticker));
    }

    /** 
     * Retrieves the most recent calculated Short-Interest ratio.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The Short-Interest ratio.
     */
    public static double shortRatio(String ticker) {
	return Double.parseDouble(findDataInXML(SHORT_RATIO, ticker));
    }

    /** 
     * Retrieves the last trade price for a given stock ticker.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The last trade price for the given stock ticker.
     */
    public static double stockPrice(String ticker) {
	return Double.parseDouble(findDataInXML(LAST_TRADE_PRICE, ticker));
    }

    /** 
     * Retrieves the moving average stock price over the past 200 days.
     *
     * @param ticker The stock ticker for the public company.
     * @return       The 200-day moving average stock price.
     */
    public static double twoHundredDayMovingAverage(String ticker) {
	return Double.parseDouble(findDataInXML(TWOHUNDRED_DAY_MOVING_AVG,
						ticker));
    }

    /** 
     * Retrieves the highest trade price of a stock in the past year.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The highest trade price YTD.
     */
    public static double yearHigh(String ticker) {
	return Double.parseDouble(findDataInXML(YEAR_HIGH, ticker));
    }

    /** 
     * Retrieves the lowest trade price of a stock in the past year.
     *
     * @param ticker The stock ticker for the public corporation.
     * @return       The lowest trade price YTD.
     */
    public static double yearLow(String ticker) {
	return Double.parseDouble(findDataInXML(YEAR_LOW, ticker));
    }

    /**
     * Retrieves the complete income statement for a public corporation.
     *
     * @param ticker     The stock ticker for the public corporation.
     * @param periodType The type of statement period (quarterly, annual).
     * @return           The income statement in a two-dimensional ArrayList.
     */
    public static ArrayList<ArrayList<String>> incomeStatement(String ticker, String periodType)
    {
        ArrayList<ArrayList<String>> incomeStatementData;
	String urlToPass = YQL_STATEMENT_BEGIN + IS + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	
        incomeStatementData = retrieveFinancialStatementData(urlToPass, periodType, IS);
	return incomeStatementData;
    }

    /**
     * Retrieves the complete balance sheet for a public corporation.
     *
     * @param ticker     The stock ticker for the public corporation.
     * @param periodType The type of statement period (quarterly, annual).
     * @return           The balance statement in a two-dimensional ArrayList.
     */
    public static ArrayList<ArrayList<String>> balanceSheet(String ticker, String periodType) {
	List<String> balanceSheetData;
	String urlToPass = YQL_STATEMENT_BEGIN + BS + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	balanceSheetData = retrieveFinancialStatementData(urlToPass, periodType, BS);
	return balanceSheetData;
    }
    
    /**
     * Retrieves the complete cash flows statement for a public corporation.
     *
     * @param ticker     The stock ticker for the public corporation.
     * @param periodType The type of statement period (quarterly, annual).
     * @return           The cash flows statement in a two-dimensional ArrayList.
     */
    public static ArrayList<ArrayList<String>> statementOfCashFlows(String ticker, String
						    periodType) {
	List<String> StatementOfCashFlowsData;
	String urlToPass = YQL_STATEMENT_BEGIN + CF + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	statementOfCashFlows = retrieveFinancialStatementData(urlToPass, periodType, CF);
	return statementOfCashFlows;
    }
    
    /* BEGIN PRIVATE METHODS */

    /* Obtains data through the Yahoo! Query Language */
    private static String findDataInXML(String dataToExtract, 
					String stockTicker) {
	URL url;
	InputStream is = null;
	BufferedReader br;
	String XMLDataLine;
	String dataToReturn = "0.00";
       
	try {
	    url = new URL(YQL_BEGIN + stockTicker + YQL_END);

	    try {
		is = url.openStream();
	    } catch (IOException io) {
		System.out.println("The URL failed to retireve the" +
				   "stock ticker from the API");
		io.printStackTrace();
		return null;
	    }
	    
	    br = new BufferedReader(new InputStreamReader(is));
	    
	    while ((XMLDataLine = br.readLine()) != null) {
		if (XMLDataLine.contains(dataToExtract)) {
		    dataToReturn = XMLDataToScrape(dataToExtract, XMLDataLine);
		}
	    }
	} catch (MalformedURLException mue) {
	    mue.printStackTrace();
	    return null;
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
	
	return dataToReturn;
    }

    /* Scrapes the specified data from the XML page returned by the YQL */
    private static String XMLDataToScrape(String dataToExtract, 
						 String XMLDataLine) {
	int firstIndex;
	int lastIndex;

	firstIndex = XMLDataLine.indexOf(dataToExtract) + 
	    dataToExtract.length();
	dataToExtract = dataToExtract.substring(0,1) + "/" +
	    dataToExtract.substring(1,dataToExtract.length());
	lastIndex = XMLDataLine.indexOf(dataToExtract);
	return XMLDataLine.substring(firstIndex, lastIndex);
    }

    /* Scrapes the HTML page for lines of relevant financial data */
    private static ArrayList<ArrayList<String>> retrieveFinancialStatementData(String urlToOpen,
							       String periodType,
							       String statementType) {
	URL url;
	InputStream istream = null;
	BufferedReader reader;
	ArrayList<ArrayList<String>> statementDataToReturn = new ArrayList<ArrayList<String>>();
	String lineOfHTMLData;
	boolean startScraping = false;
	boolean startIncrement = false;
	int i = 0;

	try {
	    url = new URL(urlToOpen);
	    try {
		istream = url.openStream();
	    } catch (IOException e) {
		System.out.println("Failed to retrieve URL.");
		e.printStackTrace();
	    }

	    reader = new BufferedReader(new InputStreamReader(istream));

	    try {
		while((lineOfHTMLData = reader.readLine()) != null &&
		      (periodType.equals("quarterly") && i != 17) ||
		      (periodType.equals("annual") && i != 14)) {

		    /*Start scraping at either the 14th or 17th line of
		      HTML */
		    if (lineOfHTMLData.contains("Period Ending"))
			startScraping = true;

		    if (lineOfHTMLData.contains("Net Income Applicable To Common Shares"))
			startIncrement = true;

		    if (startScraping == true) {
			String returnedHTML = scrapeExcessHTML(lineOfHTMLData);
			if (returnedHTML.length() > 0 ) {
			    ArrayList<String> newList = buildFinancialDataList(returnedHTML.split(" "),
									       statementType);
			    if (newList.size() > 0)
				statementDataToReturn.add(newList);
			    
			}
			
		    }

		    if (startIncrement == true)
			i++;
		}// end While loop
	    } catch (IOException e) {
		System.out.println("Failed to read from the URL.");
		e.printStackTrace();
	    }
	} catch (MalformedURLException e) {
	    System.out.println("Invalid URL provided.");
	    e.printStackTrace();
	    return null;
	}
	return statementDataToReturn;

    }

    /* Removes excess HTML characters/code from a given String */
    private static String scrapeExcessHTML(String lineOfHTMLData) {
	while (lineOfHTMLData.contains("<") || lineOfHTMLData.contains(">")) {
	    int firstIndex = lineOfHTMLData.indexOf("<");
	    int nextIndex = lineOfHTMLData.indexOf(">")+1;
	    if (firstIndex == -1 || nextIndex == -1)
		break;
	    
	    String lineToRemove = lineOfHTMLData.substring(firstIndex, nextIndex);
	    lineOfHTMLData = lineOfHTMLData.replace(lineToRemove, " ");
	    lineOfHTMLData.trim();
	}
	
	while(lineOfHTMLData.contains("&nbsp;")) {
	    lineOfHTMLData = lineOfHTMLData.replaceAll("&nbsp", "").replaceAll
		(";", "");
	}
	return lineOfHTMLData.trim();
    }

    /* Builds an ArrayList containing a financial statement */
    private static ArrayList<String> buildFinancialDataList(String[] line, String statementType) {
	ArrayList<String> dataToReturn = new ArrayList<String>();

	/* Empty lines do not contain any
	   data so they are ignored */
	if (line.length == 0)
	    return null;

	/* If the length is of 1, it is data that is not currently present
	   which is denoted with a dash ( - ). Thus we no longer need to
	   continue attempting to build the data */
	if (line.length == 1 || line[0].equals("-")) {
	    dataToReturn.add(line[0]);
	    return dataToReturn;
	}

	/* Some of the HTML data will contain headers which represent
	   the type of data. These must be split and recombined as they
	   are located adjacently within the line[] array */
	String combine = "";
	boolean printAgain = true;
	boolean numeric = false;

	/* From this point forward, the data is likely spread across
	   the array and thus the array must be iterated through in order
	   to return the data correctly. */

	for (int i = 0; i < line.length; i++) {
	    if (line[i].isEmpty())
		continue;

	    /* Each array index must be checked to see if it is either
	       a header or numeric data */
	    /* If the data is alphabetic, it is a header that must be
	       combined together by iterating through the array since the
	       header is split apart in adjacent array indices */
	    if (Character.isAlphabetic(line[i].charAt(0))) {
		if (line[i].equals("Get")) {
		    boolean periodEndingFound = false;
		    int counter = 0;
		    String financialStatementPeriod = "";

		    for (int j = 0; j < line.length; j++) {
			if (line[j].equals("Ending"))
			    periodEndingFound  = true;

			if (periodEndingFound  == true && !line[j].isEmpty()
			    && (line[j].length() == 3 ||
				line[j].length() == 4)) {
			    counter++;
			    financialStatementPeriod += line[j] + " ";

			    if (counter == 3) {
				dataToReturn.add(financialStatementPeriod);
				counter = 0;
				financialStatementPeriod = "";
			    }
			}// end periodEndingFound if-block
			if (j == line.length-1 && statementType.equals(BS)) {
			    dataToReturn.add("Assets");
			    dataToReturn.add("CurrentAssets");
			}
			    
		    }// end inner for-loop

		    if (statementType.equals(IS)) {
			dataToReturn.add(0, "Income Statement");
		    } else if (statementType.equals(BS)) {
			dataToReturn.add(0, "Balance Sheet");
		    } else {
			dataToReturn.add(0, "Statement of Cash Flows");
		    }
		    return dataToReturn;
		}

		if (printAgain == false && numeric == true) {
		    printAgain = true;
		    numeric = false;
		}

		combine += line[i] + " ";
		
		if (checkIfHeader(combine) == true) {
		    System.out.println(combine);
		    dataToReturn.add(combine);
		    combine = "";
		}
	    } // end main if-block, check for header complete.

	    /* Checking for financial statements header is complete
	       and checking for data begins  */
	    if (line[i].length() > 1 && (Character.isDigit(line[i].charAt(0)) ||
					 Character.isDigit(line[i].charAt(1)))) {
		numeric = true;

		if (printAgain == true) {
		    dataToReturn.add(combine);
		    combine = "";
		    dataToReturn.add(line[i]);
		    printAgain = false;
		} else {
		    dataToReturn.add(line[i]);
		}
	    } // end checking if data exists
	}// end outer for-loop

	return dataToReturn;
    }

     /** 
     * Compares known financial headers to the String parameter.
     *
     * @return true if there is a match, false otherwise.
     */
    private static boolean checkIfHeader(String currentHeaderWord) {
	String[] headerWords = {"Assets", "Liabilities", "Stockholders' Equity",
		 "Operating Activities, Cash Flows Provided By or Used In",
	         "Investing Activities, Cash Flows Provided By or Used In",
	          "Financing Activities, Cash Flows Provided By or Used In"};
	for (int i = 0; i < headerWords.length; i++) {
	    if (currentHeaderWord.trim().equals(headerWords[i])) {
		return true;
	    }
	} return false;
    }
}
