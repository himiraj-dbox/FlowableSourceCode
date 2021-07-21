package invoicegeneratorwebservice;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import invoice.xsd.BillInvoice;

@Path("/DBValidation") 
public class DBValidationWebservice {

	@POST
	@Path("/validateForm")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/InvoceGeneration/createBill
	 public Response createBill(final Object billInvoice){ 
		
		try{
			String basicCheck ="";
			String ccc = basicCheck;
			//customerRecords =	service.getAllCustomer(customerRecords);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(billInvoice).build();

	}
}
