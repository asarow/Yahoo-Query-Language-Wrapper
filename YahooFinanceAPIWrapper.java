import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

public class YahooFinanceAPIWrapper {
    private static final String YQL_BEGIN = 
	"https://query.yahooapis.com/v1/public/" +
        "yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20" +	
	"symbol%20in%20(%22";
    private static final String YQL_END = 
	"%22)&diagnostics=true&env=store%3A%" +
        "2F%2Fdatatables.org%2Falltableswithkeys";

    //B = Begin, E = End
    private static final String 
	FIFTY_DAY_MOVING_AVG = "<FiftydayMovingAverage>",
	TWOHUNDRED_DAY_MOVING_AVG =  "<TwoHundreddayMovingAverage>",
	ASK = "<Ask>",
	AVG_DAILY_VOLUME = "<AverageDailyVolume>",	
	BID = "<Bid>",
	BOOK_VALUE = "<BookValue>",
	CHANGE = "<Change>", 
	CHANGE_YEAR_LOW = "<ChangeFromYearLow>",
	CHANGE_YEAR_LOW_PERCENT = "<PercentChangeFromYearLow>",
	DAYS_HIGH = "<DaysHigh>",
	DAYS_LOW = "<DaysLow>",
	DIVIDEND = "<DividendShare>",
	EARNINGS_SHARE = "<EarningsShare>",
	EBITDA = "<EBITDA>",
	EPS_ESTIMATE_CY = "<EPSEstimateCurrentYear>",
	EPS_ESTIMATE_NQ = "<EPSEstimateNextQuarter>",
	EPS_ESTIMATE_NY = "<EPSEstimateNextYear>",
	LAST_TRADE_PRICE = "<LastTradePriceOnly>",
	MARKETCAP = "<MarketCapitalization>",
	PE_RATIO = "<PERatio>",
	SHORT_RATIO = "<ShortRatio>";

    public static double askPrice(String ticker) {
	return findDataInXML(ASK, ticker);
    }

    public static double averageDailyVolume(String ticker) {
	return findDataInXML(AVG_DAILY_VOLUME, ticker);
    }

    public static double bidPrice(String ticker) {
	return findDataInXML(BID, ticker);
    }
    
    public static double bookValue(String ticker) {
	return findDataInXML(BOOK_VALUE, ticker);
    }

    public static double changeInPrice(String ticker) {
	return findDataInXML(CHANGE, ticker);
    }
    
    public static double changeFromYearLow(String ticker) {
	return findDataInXML(CHANGE_YEAR_LOW, ticker);
    }

    public static double changeFromYearLowPercentage(String ticker) {
	return findDataInXML(CHANGE_YEAR_LOW_PERCENT, ticker);
    }

    public static double dayHigh(String ticker) {
	return findDataInXML(DAYS_HIGH, ticker);
    }

    public static double dayLow(String ticker) {
	return findDataInXML(DAYS_LOW, ticker);
    }

    public static double dividend(String ticker) {
	return findDataInXML(DIVIDEND, ticker);
    }
    
    public static double earningsShare(String ticker) {
	return findDataInXML(EARNINGS_SHARE, ticker);
    } 

    public static double EBITDA(String ticker) {
	return findDataInXML(EBITDA, ticker);
    }

    public static double EPSEstimateCurrentYear(String ticker) {
	return findDataInXML(EPS_ESTIMATE_CY, ticker);
    }

    public static double EPSEstimateNextQuarter(String ticker) {
	return findDataInXML(EPS_ESTIMATE_NQ, ticker);
    }

    public static double EPSEstimateNextYear(String ticker) {
	return findDataInXML(EPS_ESTIMATE_NY, ticker);
    }

    public static double fiftyDayMovingAverage(String ticker) {
	return findDataInXML(FIFTY_DAY_MOVING_AVG, ticker);
    }

    public static double marketCap(String ticker) {
	return findDataInXML(MARKETCAP, ticker);
    }

    public static double PERatio(String ticker) {
	return findDataInXML(PE_RATIO, ticker);
    }

    public static double shortRatio(String ticker) {
	return findDataInXML(SHORT_RATIO, ticker);
    }

    public static double stockPrice(String ticker) {
	return findDataInXML(LAST_TRADE_PRICE, ticker);
    }

    public static double twoHundredDayMovingAverage(String ticker) {
	return findDataInXML(TWOHUNDRED_DAY_MOVING_AVG, ticker);
    }

    
    /* BEGIN PRIVATE METHODS */

    private static double findDataInXML(String dataToExtract, 
					String stockTicker) {
	URL url;
	InputStream is = null;
	BufferedReader br;
	String XMLDataLine;
	double dataToReturn = 0.00;
       
	try {
	    url = new URL(YQL_BEGIN + stockTicker + YQL_END);

	    try {
		is = url.openStream();
	    } catch (IOException io) {
		System.out.println("The URL failed to retireve the" +
				   "stock ticker from the API");
		io.printStackTrace();
		return 0.00;
	    }
	    
	    br = new BufferedReader(new InputStreamReader(is));
	    
	    while ((XMLDataLine = br.readLine()) != null) {
		if (XMLDataLine.contains(dataToExtract)) {
		    dataToReturn = XMLDataToScrape(dataToExtract, XMLDataLine);
		}
	    }
	} catch (MalformedURLException mue) {
	    mue.printStackTrace();
	    return 0.00;
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    return 0.00;
	}
	return dataToReturn;
    }

    private static double XMLDataToScrape(String dataToExtract, 
					  String XMLDataLine) {
	int firstIndex;
	int lastIndex;
	double dataToReturn = 0.00;
	
	firstIndex = XMLDataLine.indexOf(dataToExtract) + 
	    dataToExtract.length();
	dataToExtract = dataToExtract.substring(0,1) + "/" +
	    dataToExtract.substring(1,dataToExtract.length());
	lastIndex = XMLDataLine.indexOf(dataToExtract);
	dataToReturn = Double.parseDouble(XMLDataLine.substring
					  (firstIndex, lastIndex));
	return dataToReturn;
    }

    public static void main(String[] args) {
	
	System.out.println(stockPrice("AAPL"));
	System.out.println(dividend("GOOG"));
	System.out.println(changeInPrice("AAPL"));
    }
    
}
