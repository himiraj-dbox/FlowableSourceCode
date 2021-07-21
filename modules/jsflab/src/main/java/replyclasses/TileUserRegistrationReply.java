package replyclasses;

import java.util.List;
import java.util.Map;

import hibernateSignUp.TileUserRegistration;
import notification.SuccessFailureWarnig;
import request.TileUserRegistrationRequest;

public class TileUserRegistrationReply {
	private SuccessFailureWarnig notification;
	private List<TileUserRegistrationRequest> userList;
	private Map<String,String> parameterPassing;
	private List<TileUserRegistration> peristanTObject;
	
	public SuccessFailureWarnig getNotification() {
		return notification;
	}
	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}
	public List<TileUserRegistrationRequest> getUserList() {
		return userList;
	}
	public void setUserList(List<TileUserRegistrationRequest> userList) {
		this.userList = userList;
	}
	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}
	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}
	public List<TileUserRegistration> getPeristanTObject() {
		return peristanTObject;
	}
	public void setPeristanTObject(List<TileUserRegistration> peristanTObject) {
		this.peristanTObject = peristanTObject;
	}
}
