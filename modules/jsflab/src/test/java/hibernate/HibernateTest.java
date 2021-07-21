package hibernate;

import hibernateSignUp.SignUpEaOLayer;
import hibernateSignUp.SignUpObject;
import hibernateSignUp.SignUpReply;

import java.util.Date;

import notification.SuccessFailureWarnig;

import org.junit.Test;

public class HibernateTest {
@Test
public void RegisterUser() throws Exception{
SuccessFailureWarnig status = null;

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
object.setUserName("gauravLalwani90@gmail.com");
object.setVarifiedUser("for Future Reference");
object.setTrustScore(0);
object.setVerified("F");
object.setOfficeAddress("Plot 101 Syntel GDC Talwade Nigdi Pune");
object.setServiceTax("b10111111");
	SignUpEaOLayer saveSignUp = new SignUpEaOLayer();
	status = saveSignUp.insertIntoLoginTable(object);
	
	
	

}
public void insertIntoSignUp(){
	SignUpEaOLayer signUpEao = new SignUpEaOLayer();
	SignUpReply reply = signUpEao.getLoginUserData("gauravLalwani91@gmail.com");
}
}
