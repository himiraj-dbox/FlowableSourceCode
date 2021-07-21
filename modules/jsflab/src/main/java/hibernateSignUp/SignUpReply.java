package hibernateSignUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notification.SuccessFailureWarnig;

public class SignUpReply {
	private SuccessFailureWarnig notification ;
	private List<SignUp> registerUserList;
	private Map<String,String> parameterPassing;
	
	public SignUpReply(){
		notification = new SuccessFailureWarnig();
		registerUserList = new ArrayList<SignUp>();
		parameterPassing = new HashMap<String, String>();
	}
	public SuccessFailureWarnig getNotification() {
		return notification;
	}
	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}
	public List<SignUp> getRegisterUserList() {
		return registerUserList;
	}
	public void setRegisterUserList(List<SignUp> registerUserList) {
		this.registerUserList = registerUserList;
	}
	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}
	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}


	
	
}
