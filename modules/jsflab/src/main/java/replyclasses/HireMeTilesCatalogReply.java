package replyclasses;

import java.util.List;
import java.util.Map;

import hibernateSignUp.HireMeTileTilesCatalog;
import notification.SuccessFailureWarnig;
import request.HireMeTilesCatalogRequest;

public class HireMeTilesCatalogReply {
	private SuccessFailureWarnig notification;
	private List<HireMeTilesCatalogRequest> userList;
	private Map<String,String> parameterPassing;
	private List<HireMeTileTilesCatalog> peristanTObject;
	public SuccessFailureWarnig getNotification() {
		return notification;
	}
	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}
	public List<HireMeTilesCatalogRequest> getUserList() {
		return userList;
	}
	public void setUserList(List<HireMeTilesCatalogRequest> userList) {
		this.userList = userList;
	}
	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}
	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}
	public List<HireMeTileTilesCatalog> getPeristanTObject() {
		return peristanTObject;
	}
	public void setPeristanTObject(List<HireMeTileTilesCatalog> peristanTObject) {
		this.peristanTObject = peristanTObject;
	}
}
