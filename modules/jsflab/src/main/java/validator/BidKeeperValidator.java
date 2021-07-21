package validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import boimpl.BiddingInsertionBO;

@ApplicationScoped
@FacesValidator("bidkeepervalidator")
public class BidKeeperValidator implements Validator, ClientValidator {


	public Map<String, Object> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValidatorId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
	BiddingInsertionBO bo = new BiddingInsertionBO();
	int i = bo.getBidrefrenceNumberHibernate(Integer.parseInt(value.toString()));
	if(i == -1){
		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Bid with given below reference Number", 
                value + " is not a valid email;"));
		
	}
	
		
	}


}
