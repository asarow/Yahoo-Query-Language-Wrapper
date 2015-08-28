import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A wrapper for the Yahoo! Query Language Finance database. Documentation
 * is available on GitHub @: 
 * https://github.com/asarow/Yahoo-Query-Language-Wrapper
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
	YEAR_LOW                  = "<YearLow>";

    public static double askPrice(String ticker) {
        return Double.parseDouble(findDataInXML(ASK, ticker));
    }
    
    public static int averageDailyVolume(String ticker) {
	return Integer.parseInt(findDataInXML(AVG_DAILY_VOLUME, ticker));
    }

    public static double bidPrice(String ticker) {
	return Double.parseDouble(findDataInXML(BID, ticker));
    }
    
    public static double bookValue(String ticker) {
	return Double.parseDouble(findDataInXML(BOOK_VALUE, ticker));
    }

    public static String changeInPrice(String ticker) {
	return findDataInXML(CHANGE, ticker);
    }
    
    public static String changeFromYearLow(String ticker) {
	return findDataInXML(CHANGE_YEAR_LOW, ticker);
    }

    public static double close(String ticker) {
	return Double.parseDouble(findDataInXML(CLOSE, ticker));
    }

    public static double dayHigh(String ticker) {
	return Double.parseDouble(findDataInXML(DAYS_HIGH, ticker));
    }

    public static double dayLow(String ticker) {
	return Double.parseDouble(findDataInXML(DAYS_LOW, ticker));
    }

    public static double dividend(String ticker) {
	return Double.parseDouble(findDataInXML(DIVIDEND, ticker));
    }
    
    public static double earningsShare(String ticker) {
	return Double.parseDouble(findDataInXML(EARNINGS_SHARE, ticker));
    } 

    public static String EBITDA(String ticker) {
	return findDataInXML(EBITDA, ticker);
    }

    public static double EPSEstimateCurrentYear(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_CY, ticker));
    }

    public static double EPSEstimateNextQuarter(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_NQ, ticker));
    }

    public static double EPSEstimateNextYear(String ticker) {
	return Double.parseDouble(findDataInXML(EPS_ESTIMATE_NY, ticker));
    }

    public static double fiftyDayMovingAverage(String ticker) {
	return Double.parseDouble(findDataInXML(FIFTY_DAY_MOVING_AVG, ticker));
    }

    public static String marketCap(String ticker) {
	return findDataInXML(MARKETCAP, ticker);
    }

    public static String companyName(String ticker) {
	return findDataInXML(NAME, ticker);
    }

    public static double open(String ticker) {
	return Double.parseDouble(findDataInXML(OPEN, ticker));
    }

    public static double PERatio(String ticker) {
	return Double.parseDouble(findDataInXML(PE_RATIO, ticker));
    }

    public static double PEGRatio(String ticker) {
	return Double.parseDouble(findDataInXML(PEG_RATIO, ticker));
    }

    public static double shortRatio(String ticker) {
	return Double.parseDouble(findDataInXML(SHORT_RATIO, ticker));
    }

    public static double stockPrice(String ticker) {
	return Double.parseDouble(findDataInXML(LAST_TRADE_PRICE, ticker));
    }

    public static double twoHundredDayMovingAverage(String ticker) {
	return Double.parseDouble(findDataInXML(TWOHUNDRED_DAY_MOVING_AVG,
						ticker));
    }

    public static double yearHigh(String ticker) {
	return Double.parseDouble(findDataInXML(YEAR_HIGH, ticker));
    }

    public static double yearLow(String ticker) {
	return Double.parseDouble(findDataInXML(YEAR_LOW, ticker));
    }
    
    /* BEGIN PRIVATE METHODS */

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
}
