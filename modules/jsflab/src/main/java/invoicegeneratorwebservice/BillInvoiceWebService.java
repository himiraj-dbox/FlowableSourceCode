package invoicegeneratorwebservice;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import invoice.services.InvoiceGenerationService;
import invoice.xsd.BillInvoice;

@Path("/InvoceGeneration") 
public class BillInvoiceWebService {
private InvoiceGenerationService service = new InvoiceGenerationService();
	@POST
	@Path("/createBill")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/InvoceGeneration/createBill
	 public Response createBill(final BillInvoice billInvoice){ 
		BillInvoice invoiceCreated = null;
		try{
			invoiceCreated = service.createBill(billInvoice);
			//customerRecords =	service.getAllCustomer(customerRecords);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(billInvoice).build();

	}
	
	@POST
	@Path("/getMaxBillInvoiceNumner")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/InvoceGeneration/createBill
	 public Response getMaxBillInvoiceNumner(final BillInvoice billInvoice){ 
		BillInvoice invoiceCreated = null;
		try{
			invoiceCreated = service.getMaxBillInvoiceNumner(billInvoice.getBillInvoiceID().getOwnerShopId());
			long billNumber  = invoiceCreated.getBillInvoiceID().getBillInvoiceNumber() +1;
			invoiceCreated = billInvoice;
			invoiceCreated.getBillInvoiceID().setBillInvoiceNumber(billNumber);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(invoiceCreated).build();

	}
	
}
