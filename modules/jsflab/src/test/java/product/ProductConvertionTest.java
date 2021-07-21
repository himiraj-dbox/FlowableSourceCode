package product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Test;

import standardclasses.Product;



public class ProductConvertionTest {
	@Test
	public void ProductMappingTest(){
		Product testing_proper_validation = new Product();
		//putting the product as according to the description
	Map<String,String> basicDescription = new  HashMap<String, String>();
	basicDescription.put("A", "this is test as A");
	basicDescription.put("B", "this is test as B");
	basicDescription.put("C", "this is test as C");
	basicDescription.put("D", "this is test as D");
	basicDescription.put("E", "this is test as E");
	basicDescription.put("F", "this is test as F");
	Set<String> keySet = basicDescription.keySet() ;
	 int size = keySet.size();
	 int i =1;
	 StringBuilder mappingBasicDescription = new StringBuilder();
	for(String map :keySet){
		i++;
		
		mappingBasicDescription.append("(");
		mappingBasicDescription.append(map);
		mappingBasicDescription.append("-");
		mappingBasicDescription.append(basicDescription.get(map));
		mappingBasicDescription.append(")");
		if(i<=size){
			mappingBasicDescription.append(",");
		}
		
	}
	testing_proper_validation.setBasicInformation(mappingBasicDescription.toString());
	
	// Extracting the values again
	String basic_information = testing_proper_validation.getBasicInformation();
	Map<String,String> basicInformationExtracted = new HashMap<String, String>();
	StringTokenizer st = new StringTokenizer(basic_information,"(),");
	
    while(st.hasMoreTokens()){
    	List<String> tempString = new ArrayList<String>();
    	StringTokenizer sTempt = new StringTokenizer(st.nextToken(),"-");
    	while(sTempt.hasMoreTokens()){
    		tempString.add(sTempt.nextToken());
    	}
    	basicInformationExtracted.put(tempString.get(0),tempString.get(1));
    }
  

	
	
	
	}
	
	
	

}
