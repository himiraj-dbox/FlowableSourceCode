package controller;

import hibernateSignUp.SignUpBo;
import hibernateSignUp.SignUpBoImpl;
import hibernateSignUp.SignUpObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import constantBidding.MessageConstants;
import notification.SuccessFailureWarnig;
import daoLayer.SignUPDaoImpl;


@ManagedBean(name="signup")
@ViewScoped
public class SignUP{
	@ManagedProperty(value="#{bidding}") 
	private BiddingHeader header = new BiddingHeader();
	private String luserName;
	private String lpassword;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String companyName;
	private String designation;
	private String contactNumber;
	private String emailID;
	private boolean errormessage;
	private String ermessage;
	private boolean errorRendered;
	private String verifiedUser;
	private String errorMessage;

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
			String EMAIL_PATTERN = 
					"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
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
			SignUPDaoImpl daoCall = new SignUPDaoImpl();
			if(daoCall.getUsername(luserName) !=0){
				if(getLpassword().equals(daoCall.getpassword(luserName))){
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggenUserName", luserName);
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userLoggedIn", true);
					header.setUserLoggedIn(true);
					header.setLoggenUserName(luserName);
				}
				else
				{
					setErrorRendered(true);
					setErrorMessage("Username Already Exist");
					return temp;
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
	public String register(){

		String temp ="";
		SuccessFailureWarnig successFailure = new SuccessFailureWarnig() ;
		if(validate()){
			SignUPDaoImpl daoCall = new SignUPDaoImpl();
			if(daoCall.getUsername(userName) ==0){
				
				SignUpObject signUp = new SignUpObject();
				signUp.setFirstName(firstName);
				signUp.setLastName(lastName);
				signUp.setEmailAddress(emailID);
				signUp.setAddress(address);
				signUp.setCity(city);
				signUp.setState(state);
				signUp.setCompanyName(companyName);
				signUp.setDesgination(designation);
				signUp.setContactNumber(contactNumber);
				signUp.setUserName(userName);
				signUp.setPassword(password);
				SignUpBo signBo = new SignUpBoImpl();
				try{
					successFailure = signBo.registerUser(signUp);
				}
				catch(Exception e){
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage( null, new FacesMessage( MessageConstants.GENERICERROR ));
				}
			}
			else{
				setErrorRendered(true);
				setErrorMessage("Username Already Exist");
				return temp;
			}
			if(!successFailure.getStatus().equalsIgnoreCase("SUCCESS")){
				setErrorRendered(true);
				setErrorMessage(successFailure.getMessage());
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
		if(userName !=null && !userName.isEmpty()
				&& password !=null && !password.isEmpty()
				&& firstName !=null && !firstName.isEmpty()
				&& lastName !=null && !lastName.isEmpty()
				&& city !=null && !city.isEmpty()
				&& state !=null && !state.isEmpty()
				&& contactNumber !=null && !contactNumber.isEmpty() 
				&& emailID!=null && !emailID.isEmpty()
				){
			Matcher matcher;
			String EMAIL_PATTERN = 
					"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern p = Pattern.compile(EMAIL_PATTERN);
			matcher = p.matcher(emailID);
			if(matcher.matches() && isInteger(contactNumber) && contactNumber.length() ==10){
				flag = true;
			}else{
				flag = false;
			}


		}else{
			flag = false;
		}
		return flag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public boolean isErrormessage() {
		return errormessage;
	}
	public void setErrormessage(boolean errormessage) {
		this.errormessage = errormessage;
	}
	public String getErmessage() {
		return ermessage;
	}
	public void setErmessage(String ermessage) {
		this.ermessage = ermessage;
	}

	public boolean isErrorRendered() {
		return errorRendered;
	}

	public void setErrorRendered(boolean errorRendered) {
		this.errorRendered = errorRendered;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public BiddingHeader getHeader() {
		return header;
	}
	public void setHeader(BiddingHeader header) {
		this.header = header;
	}
	public String getLpassword() {
		return lpassword;
	}
	public void setLpassword(String lpassword) {
		this.lpassword = lpassword;
	}
	public String getLuserName() {
		return luserName;
	}
	public void setLuserName(String luserName) {
		this.luserName = luserName;
	}
	public String getVerifiedUser() {
		return verifiedUser;
	}
	public void setVerifiedUser(String verifiedUser) {
		this.verifiedUser = verifiedUser;
	}

}


