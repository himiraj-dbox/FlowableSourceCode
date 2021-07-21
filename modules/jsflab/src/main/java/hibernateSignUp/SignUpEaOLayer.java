package hibernateSignUp;

import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import notification.SuccessFailureWarnig;
import constantBidding.MessageConstants;

public class SignUpEaOLayer {
	
	public SuccessFailureWarnig insertIntoLoginTable (SignUpObject signUp) throws Exception{
		HibernateDao signUpDao = new HibernateDaoImpl();
		 Random randomGenerator = new Random();
		SignUp saveIntoSignUp = new SignUp();
		saveIntoSignUp.setAddress(signUp.getAddress());
		saveIntoSignUp.setCity(signUp.getCity());
		saveIntoSignUp.setCompanyName(signUp.getCompanyName());
		saveIntoSignUp.setContactNumber(signUp.getContactNumber());
		saveIntoSignUp.setDateCreated(signUp.getDateCreated());
		saveIntoSignUp.setDesgination(signUp.getDesgination());
		saveIntoSignUp.setEmailAddress(signUp.getEmailAddress());
		saveIntoSignUp.setFirstName(signUp.getFirstName());
		saveIntoSignUp.setLastName(signUp.getLastName());
		saveIntoSignUp.setPassword(signUp.getPassword());
		saveIntoSignUp.setState(signUp.getState());
		saveIntoSignUp.setUserName(signUp.getUserName());
		saveIntoSignUp.setVarifiedUser(signUp.getVarifiedUser());
		saveIntoSignUp.setTrustScore(signUp.getTrustScore());
		saveIntoSignUp.setVerified(signUp.getVerified());
		saveIntoSignUp.setDesgination(signUp.getDesgination());
		saveIntoSignUp.setOfficeAddress(signUp.getOfficeAddress());
		saveIntoSignUp.setServiceTax(signUp.getServiceTax());
		saveIntoSignUp.setActivationCode(""+randomGenerator.nextInt(10000));
		SuccessFailureWarnig notification = signUpDao.insertIntoSignUp(saveIntoSignUp);
	return notification;	
	}
	public SignUpReply getLoginUserData(String userName){
		HibernateDao signUpDao = new HibernateDaoImpl();
		SignUpReply reply = new SignUpReply();
		Map<String,String> parameter = new HashMap<String, String>();
		parameter.put(MessageConstants.USERNAME, userName);
		reply.setParameterPassing(parameter);
		reply = signUpDao.getFromSignUP(reply);
		return reply;
		
	}
	public SignUpReply getLoginUserCriteria(String userName){
		HibernateDao signUpDao = new HibernateDaoImpl();
		SignUpReply reply = new SignUpReply();
		Map<String,String> parameter = new HashMap<String, String>();
		parameter.put(MessageConstants.USERNAME, userName);
		reply.setParameterPassing(parameter);
		reply = signUpDao.checkUserName(reply);
		return reply;
		
		
	}

}
