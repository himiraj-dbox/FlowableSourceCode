package validator;

import hibernateSignUp.SignUpEaOLayer;
import hibernateSignUp.SignUpReply;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;
import org.primefaces.validate.ClientValidator;

import constantBidding.MessageConstants;

@ApplicationScoped
@FacesValidator("signUpValidator1")
public class SignUpValidator implements Validator, ClientValidator {

	@Override
	public Map<String, Object> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidatorId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if(value.toString().trim() != null && !value.toString().trim().isEmpty()){
		SignUpEaOLayer eaoObject = new SignUpEaOLayer();
		SignUpReply reply = eaoObject.getLoginUserCriteria(value.toString().trim());
		if(reply.getNotification().getStatus().equals(MessageConstants.SUCCESS)){
		if(reply.getNotification().getMessage().equalsIgnoreCase(MessageConstants.RECORDFOUND)){
			 EditableValueHolder input = (EditableValueHolder) component;
			    input.resetValue();
			   // RequestContext.getCurrentInstance().execute("alert('This onload script is added from backing bean.')");
			    RequestContext.getCurrentInstance().execute("document.getElementById('"+component.getClientId()+"').value = ''");
			    RequestContext.getCurrentInstance().execute("document.getElementById('"+component.getClientId()+"').focus()");
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,MessageConstants.USERNAMEVALIDATION, 
	                value + " is not a valid email;"));
		}
		
		} else {
			 EditableValueHolder input = (EditableValueHolder) component;
			    input.setValue("");;
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,MessageConstants.GENERICERROR, 
	                value + " is not a valid email;"));
		}
		
	}else {
		 EditableValueHolder input = (EditableValueHolder) component;
		    input.setValue("");;
		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,MessageConstants.USERNAMEMPTYCHECK, 
                value + " is not a valid email;"));
	}

}
}
