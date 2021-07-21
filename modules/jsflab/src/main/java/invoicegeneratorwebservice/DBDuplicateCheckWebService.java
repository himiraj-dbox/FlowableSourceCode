package invoicegeneratorwebservice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import invoice.services.DBDuplicateChecService;
import invoiceGeneratorEntitites.DBDuplicateCheck;
import invoiceGeneratorEntitites.DBERPINTEGRATER;
import notification.NotificationObject;

@Path("/DBDuplicateCheck") 
public class DBDuplicateCheckWebService {
	DBDuplicateChecService service = new DBDuplicateChecService();
	@POST
	@Path("/save") 
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/jsflab/rest/DBDuplicateCheck/save
	 public Response gerERPPODetails(final Object object){ 
		DBDuplicateCheck dbObject = null;
		try{
			Map<String, List<String>> test1 = new LinkedHashMap<String, List<String>>();
			Map<String, String> fromLinkedHashmap = new LinkedHashMap<String, String>();
			test1 = (Map<String, List<String>>) object;
			for (Map.Entry<String, List<String>> entry : test1.entrySet()) {
				try {
				fromLinkedHashmap =  (Map<String, String>) entry.getValue();
				for (Map.Entry<String, String> entry2 : fromLinkedHashmap.entrySet()) {
		String key = entry2.getKey();
			    if(key.equalsIgnoreCase("po_no") || key.equalsIgnoreCase("po_no.") ||  key.equalsIgnoreCase("ponumber")) {
			    	if(null != entry2 && null !=entry2.getValue() ) {
			    	if(null == dbObject) {dbObject = new DBDuplicateCheck();}
			    	
			    	dbObject.setPoNumber("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("vendorname") || key.equalsIgnoreCase("vendorname.") ||  key.equalsIgnoreCase("vendorname")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setVendorname("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("invoiceamount") || key.equalsIgnoreCase("invoiceamount.") ||  key.equalsIgnoreCase("invoiceamount")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setInvoiceAmount("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("invoicenumber") || key.equalsIgnoreCase("invoicenumber.") ||  key.equalsIgnoreCase("invoicenumber")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setInvoiceNumber("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("taskIdActual") || key.equalsIgnoreCase("taskIdActual.") ||  key.equalsIgnoreCase("taskIdActual")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setProcessInstanceId("" +entry2.getValue().toString());
			    	}
			    }
			  }
				}catch(Exception e) {
					System.out.print("Not a form value");
				}
			}
			String taskID  = dbObject.getProcessInstanceId();
			DBDuplicateCheck checkInDB = service.checkValueForProcessInstanceId(dbObject);
			DBDuplicateCheck doubleCheckForMultipleProcess = service.doubleCheckDuplicateValues(dbObject,taskID);
			if(null == checkInDB && null == doubleCheckForMultipleProcess ) {
			NotificationObject message = service.saveFormDetails(dbObject);
			}
	   } catch (Exception e) {
		String message  = e.getMessage();
	} 
		
		return Response.status(200).entity(dbObject).build();

	}
	
	
	@POST
	@Path("/checkForDuplicate") 
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:8080/jsflab/rest/DBDuplicateCheck/checkForDuplicate
	 public Response checkDuplicate(final Object object){ 
		DBDuplicateCheck dbObject = null;
		try{
			Map<String, List<String>> test1 = new LinkedHashMap<String, List<String>>();
			Map<String, String> fromLinkedHashmap = new LinkedHashMap<String, String>();
			test1 = (Map<String, List<String>>) object;
			for (Map.Entry<String, List<String>> entry : test1.entrySet()) {
				try {
				fromLinkedHashmap =  (Map<String, String>) entry.getValue();
				for (Map.Entry<String, String> entry2 : fromLinkedHashmap.entrySet()) {
		String key = entry2.getKey();
			    if(key.equalsIgnoreCase("po_no") || key.equalsIgnoreCase("po_no.") ||  key.equalsIgnoreCase("ponumber")) {
			    	if(null != entry2 && null !=entry2.getValue() ) {
			    	if(null == dbObject) {dbObject = new DBDuplicateCheck();}
			    	
			    	dbObject.setPoNumber("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("vendorname") || key.equalsIgnoreCase("vendorname.") ||  key.equalsIgnoreCase("vendorname")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setVendorname("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("invoiceamount") || key.equalsIgnoreCase("invoiceamount.") ||  key.equalsIgnoreCase("invoiceamount")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setInvoiceAmount("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("invoicenumber") || key.equalsIgnoreCase("invoicenumber.") ||  key.equalsIgnoreCase("invoicenumber")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setInvoiceNumber("" +entry2.getValue().toString());
			    	}
			    }
			    if(key.equalsIgnoreCase("processinstanceid") || key.equalsIgnoreCase("processinstanceid.") ||  key.equalsIgnoreCase("process_id")) {
			    	if(null != entry2 && null !=entry2.getValue()) {
			    	if(null == dbObject) {
			    	dbObject = new DBDuplicateCheck();}
			
			    	dbObject.setProcessInstanceId("" +entry2.getValue().toString());
			    	}
			    }
			  }
				}catch(Exception e) {
					System.out.print("Not a form value");
				}
			}
			String currentProcessInsatnceId  = dbObject.getProcessInstanceId();
			DBDuplicateCheck checkInDB = service.checkDuplicateValues(dbObject);
			if(null != checkInDB) {
			if(!currentProcessInsatnceId.equalsIgnoreCase(checkInDB.getProcessInstanceId())) {
				dbObject.setMessage("FAILED");
			}else {
				dbObject.setMessage("SUCCESS");
			}
			}else {
				dbObject = new DBDuplicateCheck();
				dbObject.setMessage("EXCEPTION");
			}
	   } catch (Exception e) {
		String message  = e.getMessage();
	} 
		
		return Response.status(200).entity(dbObject).build();

	}
}
