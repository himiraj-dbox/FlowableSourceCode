package invoicegeneratorwebservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.ss.formula.functions.T;

import invoice.services.DBErpIntegraterServices;
import invoiceGeneratorEntitites.DBERPINTEGRATER;
import proofofconcept.User;

@Path("/ERPIntegrater") 
public class DBErpIntegraterWebserivce {
	DBErpIntegraterServices service = new DBErpIntegraterServices();
	@POST
	   @Path("/getPOERPDetails") 
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/jsflab/rest/ERPIntegrater/getPOERPDetails
	 public Response gerERPPODetails(final Object billInvoice){ 
		DBERPINTEGRATER dbObject = null;
		try{
			Map<String, List<String>> test1 = new LinkedHashMap<String, List<String>>();
			Map<String, String> fromLinkedHashmap = new LinkedHashMap<String, String>();
			test1 = (Map<String, List<String>>) billInvoice;
			for (Map.Entry<String, List<String>> entry : test1.entrySet()) {
				try {
				fromLinkedHashmap =  (Map<String, String>) entry.getValue();
				for (Map.Entry<String, String> entry2 : fromLinkedHashmap.entrySet()) {
		String key = entry2.getKey();
			    if(key.equalsIgnoreCase("po_no") || key.equalsIgnoreCase("po_no.") ||  key.equalsIgnoreCase("ponumber")) {
			    	if(null != entry2 && null !=entry2.getValue() ) {
			    	if(null == dbObject) {dbObject = new DBERPINTEGRATER();}
			    	
			    	dbObject.setPoNumber("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("vendorname") || key.equalsIgnoreCase("vendorname.") ||  key.equalsIgnoreCase("vendorname")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBERPINTEGRATER();}
			
			    	dbObject.setVendorname("" +entry2.getValue().toString());
			    	}
			    }
			  }
				}catch(Exception e) {
					System.out.print("Not a form value");
				}
			}
			String basicCheck ="";
			String ccc = basicCheck;
			
			dbObject = service.getPoVendorDetails(dbObject);
	   } catch (Exception e) {
		String message  = e.getMessage();
	} 
		
		return Response.status(200).entity(dbObject).build();

	}
}
