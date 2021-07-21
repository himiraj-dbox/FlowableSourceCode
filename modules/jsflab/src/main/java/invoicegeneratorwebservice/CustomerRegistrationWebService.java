package invoicegeneratorwebservice;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import invoice.services.CustomerRegistrationService;
import invoice.services.OwnerDetailService;
import invoice.xsd.CustomerShopRegistration;
import invoice.xsd.OwnerRegistration;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;

@Path("/CustomerRegistration") 
public class CustomerRegistrationWebService {
	private CustomerRegistrationService service = new CustomerRegistrationService();
	
	@GET
	@Path("/fetchAllCustomerList")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/OwnerRegistration/ownerregistration
	 public Response getAllCustomerList(){ 
		List<CustomerShopRegistration> customerRecords = null ;
		try{
			customerRecords =	service.getAllCustomer(customerRecords);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(customerRecords).build();

	}
	
	@POST
	@Path("/fetchAllOwnerCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/CustomerRegistration/fetchAllOwnerCustomer
	 public Response getAllOwnerCustomerList(final OwnerRegistration ownerRegistration){ 
		List<CustomerShopRegistration> customerRecords = null ;
		try{
			customerRecords =	service.getAllOwnerCustomerList(ownerRegistration);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(customerRecords).build();

	}
	
	@POST
	@Path("/saveInvoiceCustomerShop")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/CustomerRegistration/fetchAllOwnerCustomer
	 public Response saveCustomer(final CustomerShopRegistration customerShopRegistration){ 
		CustomerShopRegistration customerRecords = null ;
		try{
				service.saveCustomerRecord(customerShopRegistration);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(customerShopRegistration).build();

	}
	
}
