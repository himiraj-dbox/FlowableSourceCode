package invoice.services;

import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.collections4.map.HashedMap;

import constantBidding.MessageConstants;

public class InvoiceCommonUtility {
	
	public static boolean MaxCharacter(Integer limit, String value){
		if(value.length() > limit){
			return false;
		}
		return true;
	}

	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException e) {
		      result = false;
		   }catch (Exception e) {
			      result = false;
			   }
		   return result;
		}
	public static boolean isNumber(String value){
		boolean flag  = true;
		try{
			 Long.parseLong(value.trim());
		}catch(NumberFormatException e)  
		  {  
		    flag =  false;  
		  } catch(Exception e)  
		  {  
			    flag =  false;  
			  }   
		
		return flag;
	}
	
	public static boolean isDouble(String value){
		boolean flag  = true;
		try{
			 Double.parseDouble(value);
		}catch(NumberFormatException e)  
		  {  
		    flag =  false;  
		  } catch(Exception e)  
		  {  
			    flag =  false;  
			  }   
		
		return flag;
	}
	
	public static Integer getMaxValueList(String Value){
		Map<String,Integer> maxValue = new HashedMap<String,Integer>();	
		maxValue.put(MessageConstants.MOBILENUMBER, 10);
		
		return maxValue.get(MessageConstants.MOBILENUMBER);
	}
	
	public static boolean isNotNullObject(Object o){
		boolean flag = false;
		if(!(null == o || o.toString().equalsIgnoreCase(""))){
			flag = true;
		}
		return flag;
	}
}
