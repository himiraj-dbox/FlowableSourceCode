package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.taglibs.standard.tag.el.fmt.RequestEncodingTag;
import org.junit.runner.Request;

import boimpl.HireMeTenseFreeBO;
import boimpl.HireMeTenseFreeBOImpl;
import constantBidding.MessageConstants;
import replyclasses.TileUserRegistrationReply;
import request.TileUserRegistrationRequest;
import utilityclasses.ApplicationConstants;
import utilityclasses.BiddingUtility;

@ManagedBean(name="hiremetensefree")
@ViewScoped
public class HireMeTenseFreeController {

	private String customerName;
	private String customerMobileNumber;
	private String customerEmailId;
	private String locality;
	private String workDescription;
	
	
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getWorkDescription() {
		return workDescription;
	}
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
	
	public void onClickSubmit(ActionEvent event){
		String a = getCustomerName() + getCustomerMobileNumber(); 
		BiddingUtility.SendEmail("gauravlalwani90@gmail.com", "HireMeTenseFree.com", getCustomerName()+"-"+getCustomerMobileNumber() +"-" +getCustomerEmailId()+"-" +getLocality()+"-"+getWorkDescription());
		HireMeTenseFreeBO response = new HireMeTenseFreeBOImpl();
		List<TileUserRegistrationRequest> requestList = new ArrayList<TileUserRegistrationRequest>();
		TileUserRegistrationRequest objectToPass  = new TileUserRegistrationRequest();
		objectToPass.setMobileNumber(getCustomerMobileNumber());
		objectToPass.setEmailId(getCustomerEmailId());
	/*	objectToPass.setCity(get);*/
		objectToPass.setLocality(getLocality());
		objectToPass.setName(getCustomerName());
		/*objectToPass.setState(state);*/
		requestList.add(objectToPass);
		TileUserRegistrationReply request = new TileUserRegistrationReply();
		request.setUserList(requestList);
		Map<String,String> parmater = new HashMap<String,String>();
		parmater.put(MessageConstants.MOBILENUMBER, getCustomerMobileNumber());
		request.setParameterPassing(parmater);
		response.saveNewUser(request);
		addMessage("Your Request is Placed, Out representatives will call you shortly");
		 
    }
     
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
	
	
	
}
