package Utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
	
	private static String statementComparePriceAnd52Weeks1 = "For Company<'$companyName$'> - StockCode<'$stockCode$'>, "
																+ "Current Price <'$currentPriceVal$'> is $Hper$% $Hlowhigh$ than the 52 week high<'$52WeekHighVal$'> "
																 + "and $Lper$% $Llowhigh$ than the 52 week low<'$52WeekLowVal$'>.";
	
	private static String statementCompareEPS1 = "Company< Name: '$companyHName$', StockCode: '$HCode$', EPS: '$Heps$'> "
												 + "having higher EPS than Company< Name: '$companyLName$', StockCode: '$LCode$', EPS: '$Leps$'>";
	
	/**
	 * 
	 * @return timestampString in the format yyyyMMddhhmmssSSS
	 */
	public static String getTimeStamp()
	{
		return new SimpleDateFormat ("yyyyMMddhhmmssSSS").format(new Date( ));
	}

	
	public static String compareCurrntPriceAnd52Week(String compName, String code, String curPrice, String low52Week, String high52Week)
	{
		String statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks1;
		statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks.replace("$companyName$", compName)
										.replace("$stockCode$", code)
											.replace("$currentPriceVal$", curPrice)
												.replace("$52WeekLowVal$", low52Week)
													.replace("$52WeekHighVal$", high52Week);
		
		double low52Wdo = Double.parseDouble(low52Week);
        double high52Wdo = Double.parseDouble(high52Week);
        
        //compare current price and 52 week low
        double resuLow = comapreCurrentP(Double.parseDouble(curPrice),low52Wdo);        
		if(resuLow<0)
			statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks.replace("$Llowhigh$", "lower").replace("$Lper$", absAndRoundDouble(resuLow));
		else
			statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks.replace("$Llowhigh$", "higher").replace("$Lper$", absAndRoundDouble(resuLow));
        
		//compare current price and 52 week high
		double resuHigh = comapreCurrentP(Double.parseDouble(curPrice),high52Wdo);
		if(resuHigh<0)
			statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks.replace("$Hlowhigh$", "lower").replace("$Hper$", absAndRoundDouble(resuHigh));
		else
			statementComparePriceAnd52Weeks = statementComparePriceAnd52Weeks.replace("$Hlowhigh$", "higher").replace("$Hper$", absAndRoundDouble(resuHigh));
		
		
		return statementComparePriceAnd52Weeks;
	}
	
	private static double comapreCurrentP(double curP, double wP)
    {	
    	return ((curP*100.0f/wP)-100.0f);
    }
	
    private static String absAndRoundDouble(double value)
    {	
    	DecimalFormat df = new DecimalFormat("#.##");
    	df.setRoundingMode(RoundingMode.CEILING);
    	return df.format(Math.abs(value));
    }
    
    
    
    
    public static String compareEPS(String compName1, String code1, String compName2, String code2, String eps1, String eps2)
	{
    	String statementCompareEPS = statementCompareEPS1;
    	if(Double.parseDouble(eps1)>Double.parseDouble(eps2))
    		statementCompareEPS = statementCompareEPS.replace("$companyHName$", compName1)
    								.replace("$HCode$", code1)
    									.replace("$Heps$", eps1)
    									  .replace("$companyLName$", compName2)
    									  	.replace("$LCode$", code2)
    									  		.replace("$Leps$", eps2);
    	else
    		statementCompareEPS = statementCompareEPS.replace("$companyHName$", compName2)
									.replace("$HCode$", code2)
										.replace("$Heps$", eps2)
										  .replace("$companyLName$", compName1)
										  	.replace("$LCode$", code1)
										  		.replace("$Leps$", eps1);

    	return statementCompareEPS; 
	}
}
