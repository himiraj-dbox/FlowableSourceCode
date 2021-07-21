package replyclasses;

import java.util.List;
import java.util.Map;

import hibernateSignUp.TileUserRegistration;
import hibernateSignUp.TileUserTaskManager;
import notification.SuccessFailureWarnig;
import request.TileUserTaskManagerRequest;

public class TileUserTaskManagerReply {
	private SuccessFailureWarnig notification;
	private List<TileUserTaskManagerRequest> userList;
	private Map<String,String> parameterPassing;
	private List<TileUserTaskManager> peristanTObject;
	
	public SuccessFailureWarnig getNotification() {
		return notification;
	}
	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}
	public List<TileUserTaskManagerRequest> getUserList() {
		return userList;
	}
	public void setUserList(List<TileUserTaskManagerRequest> userList) {
		this.userList = userList;
	}
	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}
	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}
	public List<TileUserTaskManager> getPeristanTObject() {
		return peristanTObject;
	}
	public void setPeristanTObject(List<TileUserTaskManager> peristanTObject) {
		this.peristanTObject = peristanTObject;
	}
}
