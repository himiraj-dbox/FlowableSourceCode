package hibernateEntryProgram;



import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class HibernateMainProgrm {

	final static Logger logger = Logger.getLogger(HibernateMainProgrm.class);
	public static void main(String[] args) {
		
		
	
		
	}
		

}
		/*
		if(logger.isDebugEnabled()){
		    logger.debug("Entering Into Insert Method of Hibernate Program");
		}
		
	try{
			SessionFactory session = HibernateUtil.getSessionFactory();
			if(logger.isDebugEnabled()){
			    logger.debug("Exit : insertIntoLoginTable");
			}
			SignUpEaOLayer signUpEao = new SignUpEaOLayer();
			SignUpReply reply = signUpEao.getLoginUserData("gauravLalwani91@gmail.com");
			}
			 catch(Exception e){
				 logger.error("Exception Thrown : " + e.getMessage());
			 }
	*/
/* public SuccessFailureWarnig  insertIntoLoginTable() {
	 SuccessFailureWarnig status = null;
	 
			HibernateMainProgrm staring = new HibernateMainProgrm();
			SuccessFailureWarnig notification = staring.insertIntoLoginTable();
			if(notification == null || !notification.getStatus().equalsIgnoreCase("Success")){
				logger.error("This is error : " + notification.getMessage());
			}
	 SignUpObject object = new SignUpObject();
		object.setAddress("Lig 205/206 HBC Teela Jamalpura Bhopal");
		object.setCity("Bhopal");
		object.setCompanyName("Syntel");
		object.setContactNumber("8888603853");
		object.setDateCreated(new Date());
		object.setDesgination("Software Developer");
		object.setEmailAddress("gauravlalwani92@gmail.com");
		object.setFirstName("Gaurav");
		object.setLastName("Lalwani");
		object.setPassword("india911");
		object.setState("Madhya Pradesh");
		object.setUserName("gauravLalwani91@gmail.com");
		object.setVarifiedUser("for Future Reference");
		object.setTrustScore(0);
		object.setVerified("F");
		object.setOfficeAddress("Plot 101 Syntel GDC Talwade Nigdi Pune");
		object.setServiceTax("b10111111");
		
		SignUpEaOLayer saveSignUp = new SignUpEaOLayer();
		status = saveSignUp.insertIntoLoginTable(object);
		
	 }
	 catch(Exception e){
		
	 }
	 finally{
		 return status;
	 }
 }*/



