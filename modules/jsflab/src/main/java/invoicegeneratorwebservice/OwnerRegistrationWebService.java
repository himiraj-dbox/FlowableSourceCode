package invoicegeneratorwebservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.spi.ErrorCode;

import constantBidding.MessageConstants;
import invoice.services.OwnerDetailService;
import invoice.xsd.InvoiceErrorCodes;
import invoice.xsd.OwnerRegistration;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import notification.InvoiceNotification;

@Path("/OwnerRegistration") 
public class OwnerRegistrationWebService {
	
	private OwnerDetailService service = new OwnerDetailService();
	
	@GET
	@Path("/ownerregistration")
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/OwnerRegistration/ownerregistration
	 public Response getAllOwnerList(){ 
		List<OwnerRegistration> ownerRegistration = null ;
		try{
			ownerRegistration =	service.getAllOwnerRegisList(ownerRegistration);
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(ownerRegistration).build();

	}

	
	
	@POST
	@Path("/saveInvoiceOwner")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/OwnerRegistration/saveInvoiceOwner
	 public Response saveOwner(final OwnerRegistration ownerRegistration){ 
		 List<OwnerRegistration> exitingUser;
		 InvoiceErrorCodes errorCode = new InvoiceErrorCodes();
		try{
			if(!(service.validateOwnerSave(ownerRegistration))){
				return Response.status(400).header("Access-Control-Allow-Origin", "*").build();
			}
				exitingUser = service.getExitingOwnerInfo(ownerRegistration);
				if(!(null == exitingUser || exitingUser.isEmpty())){
					
					errorCode.setErrorCode(MessageConstants.DUPLICATE_RECORD);
					errorCode.setErrorMessage(service.buildDplcateRecords(exitingUser,ownerRegistration));
					return Response.status(MessageConstants.DUPLICATE_RECORD_INT).entity(errorCode).header("Access-Control-Allow-Origin", "*").build();
				}
				;
				service.saveOwner(service.processOwenerSave(ownerRegistration));
		} catch (Exception e) {
			return Response.status(MessageConstants.DUPLICATE_RECORD_INT).header("Access-Control-Allow-Origin", "*").build();
	} 
		return Response.status(MessageConstants.INVOICE_SUCCESS_CODE).entity(errorCode).build();

	}
	
	@POST
	@Path("/getExistingOwnerDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/OwnerRegistration/getExistingOwnerDetails
	 public Response getExistingOwnerDetails(final OwnerRegistration _ownerRegistration){ 
		List<OwnerRegistration> ownerRegistration = null;
		try{
			ownerRegistration = service.getExitingOwnerInfo(_ownerRegistration) ;
			
	     
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(200).entity(ownerRegistration).build();

	}
	
	@POST
	@Path("/validateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	//http://localhost:1006/jsflab/rest/OwnerRegistration/getExistingOwnerDetails
	 public Response validateUser(final OwnerRegistration _ownerRegistration){ 
		OwnerRegistration ownerRegistration = null;
		InvoiceErrorCodes errorCode =new InvoiceErrorCodes();
		try{
			OwnerRegistrationEntity ownerDetails = service.validateUser(_ownerRegistration);
			if(null != ownerDetails){
				ownerRegistration =service.ownerXsdEntityMapping(ownerDetails);
				ownerRegistration.setPassword("XXXXXXXXXX");
				return Response.status(200).entity(ownerRegistration).build();
			}
			errorCode.setErrorCode(MessageConstants.DUPLICATE_RECORD);
			errorCode.setErrorMessage("Check User Name and Password");
		
	     
	   } catch (Exception e) {
		// TODO: handle exception
	} 
		return Response.status(MessageConstants.INVOICE_INVALID_CREDENTIALS).header("Access-Control-Allow-Origin", "*").build();

	}
	
	
}
