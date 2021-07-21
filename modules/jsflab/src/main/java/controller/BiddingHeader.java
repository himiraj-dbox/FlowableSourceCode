package controller;

import hibernateSignUp.SignUpBo;
import hibernateSignUp.SignUpBoImpl;
import hibernateSignUp.SignUpEaOLayer;
import hibernateSignUp.SignUpObject;
import hibernateSignUp.SignUpReply;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import notification.SuccessFailureWarnig;

import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.context.RequestContext;

import standardclasses.SignUp;
import utilityclasses.ApplicationConstants;
import utilityclasses.BiddingUtility;
import constantBidding.MessageConstants;
import daoLayer.SignUPDaoImpl;

@ManagedBean(name="biddingHeader")
@SessionScoped
public class BiddingHeader extends SignUp implements ApplicationConstants {
	private boolean userLoggedIn;
	private String loggenUserName;
	private boolean userSignup;
	private int eastHeaderSize = 200;

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public String getLoggenUserName() {
		if(loggenUserName == null || loggenUserName.isEmpty()){
			loggenUserName ="Join us.";
		}
		
		return loggenUserName;
	}

	public void setLoggenUserName(String loggenUserName) {
		this.loggenUserName = loggenUserName;
	}

	public boolean isUserSignup() {
		return userSignup;
	}

	public void setUserSignup(boolean userSignup) {
		this.userSignup = userSignup;
	}
/*	public void openSignUpPop(ActionEvent event){
		  
		        Map<String,Object> options = new HashMap<String, Object>();
		        options.put("resizable", false);
		        options.put("draggable", false);
		        options.put("modal", true);
		        RequestContext.getCurrentInstance().openDialog("openSignUp", options, null);
		    }*/
	
	public void closeSignUpDialog(){
		   RequestContext.getCurrentInstance().execute("PF('signuplogin').hide()");
		
	}
	public void closePopup(ActionEvent event){
		setErrorRendered(false);
	}
	public static boolean isInteger(String s) {
		try { 
			Long.parseLong(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public void checkEmailAvailability(AjaxBehaviorEvent event){
		if(!getEmailID().isEmpty()){
			Matcher matcher;
			/*String EMAIL_PATTERN = 
					"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
			Pattern p = Pattern.compile(EMAIL_PATTERN);
			matcher = p.matcher(getEmailID());
			if(!matcher.matches()){
				setErrorRendered(true);
				setErrorMessage("Username Already Exist");
			}
		}
		else{
			setErrorRendered(true);
			setErrorMessage("Please correct the values");
		}
	}

public String loggin() throws Exception{
		String temp="";
		//SuccessFailureWarnig successFailure = new SuccessFailureWarnig() ;
		if(getLuserName() !=null && !getLuserName().isEmpty() && getLpassword() !=null && !getLpassword().isEmpty()){
			SignUpEaOLayer eaoObject = new SignUpEaOLayer();
			SignUpReply reply = eaoObject.getLoginUserCriteria(getLuserName().toString().trim());
			if(reply.getNotification().getStatus().equals(MessageConstants.SUCCESS)){
				if(reply.getNotification().getMessage().equalsIgnoreCase(MessageConstants.RECORDFOUND)){
				if(getLpassword().equals(reply.getRegisterUserList().get(0).getPassword())){
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggenUserName", getLuserName());
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLoggedIn", true);
					setUserLoggedIn(true);
					setLoggenUserName(getLuserName());
					
					setEastHeaderSize(600);
					
					
				} else{
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage( null, new FacesMessage( MessageConstants.PASSWORDNOTMATCHED ));
				}
					
				}
				else
				{
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage( "panel2", new FacesMessage( MessageConstants.NORecordFound ));
				}
			}
			else{
				setErrorRendered(true);
				setErrorMessage("Username Already Exist");
				return temp;
			}
		}
		return temp;
	}
public String  logOut(){
	String returnStringToUi = "";
	
	setUserLoggedIn(false);
	setLoggenUserName(null);
	
	setEastHeaderSize(200);
	
	return returnStringToUi;
}
	public String register(){

		String temp ="";
		SuccessFailureWarnig successFailure = new SuccessFailureWarnig() ;
		if(validate()){
			
		
				
				SignUpObject signUp = new SignUpObject();
				signUp.setFirstName(getFirstName());
				signUp.setLastName(getLastName());
				signUp.setEmailAddress(getEmailID());
				signUp.setAddress(getAddress());
				signUp.setCity(getCity());
				signUp.setState(getState());
				signUp.setCompanyName(getCompanyName());
				signUp.setDesgination(getDesignation());
				signUp.setContactNumber(getContactNumber());
				signUp.setUserName(getUserName());
				signUp.setPassword(getPassword());
				SignUpBo signBo = new SignUpBoImpl();
				try{
					successFailure = signBo.registerUser(signUp);
					if(!successFailure.getStatus().equalsIgnoreCase(MessageConstants.ERROR)){
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage( null, new FacesMessage( MessageConstants.USERSAVED ));
						BiddingUtility.SendEmail("gauravlalwani90@gmail.com", "testing","dfdf");
						closeSignUpDialog();
						return temp;
					}
				}
				catch(Exception e){
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage( null, new FacesMessage( MessageConstants.GENERICERROR ));
				}
			
			
			if(!successFailure.getStatus().equalsIgnoreCase("SUCCESS")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage( null, new FacesMessage( MessageConstants.USERSAVED ));
				closeSignUpDialog();
				return temp;
			}

		}
		else{
			setErrorMessage("Data is not proper please correct and try again");
			setErrorRendered(true);
			return temp;
		}
		return temp;
	}
	private boolean validate() {
		boolean flag = false;
		if(getUserName() !=null && !getUserName().isEmpty()
				&& getPassword() !=null && !getPassword().isEmpty()
				&& getFirstName() !=null && !getFirstName().isEmpty()
				&& getLastName() !=null && !getLastName().isEmpty()
				&& getCity() !=null && !getCity().isEmpty()
				&& getState() !=null && !getState().isEmpty()
				&& getContactNumber() !=null && !getContactNumber().isEmpty() 
				&& getEmailID()!=null && !getEmailID().isEmpty()
				){
			Matcher matcher;
			String EMAIL_PATTERN = 
					"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern p = Pattern.compile(EMAIL_PATTERN);
			matcher = p.matcher(getEmailID());
			if(matcher.matches() && isInteger(getContactNumber()) && getContactNumber().length() ==10){
				flag = true;
			}else{
				flag = false;
			}


		}else{
			flag = false;
		}
		return flag;
	}

	public int getEastHeaderSize() {
		return eastHeaderSize;
	}

	public void setEastHeaderSize(int eastHeaderSize) {
		this.eastHeaderSize = eastHeaderSize;
	}
	
}
