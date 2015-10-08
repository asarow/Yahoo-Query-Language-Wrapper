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
	BS                        = "balancesheet",
	IS                        = "incomestatement",
	CF                        = "cashflow";

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

    public static List<String> incomeStatement(String ticker, String periodType)
    {
        List<String> incomeStatementData;
	String urlToPass = YQL_STATEMENT_BEGIN + IS + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	System.out.println(urlToPass);
	return null;
    }

    public static List<String> balanceSheet(String ticker, String periodType) {
	List<String> incomeStatementData;
	String urlToPass = YQL_STATEMENT_BEGIN + BS + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	return null;
    }

    public static List<String> statementOfCashFlows(String ticker, String
						    periodType) {
	List<String> incomeStatementData;
	String urlToPass = YQL_STATEMENT_BEGIN + CF + YQL_STATEMENT_MID + ticker
	    + "&" + periodType;
	return null;
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

    private static
}
