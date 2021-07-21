package replyclasses;

import hibernateSignUp.BiddingHibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notification.SuccessFailureWarnig;
import standardclasses.Bidding;

public class BiddingReply {
private SuccessFailureWarnig notification;
private List<Bidding> bidding;
private Map<String,String> parameterPassing;
private List<BiddingHibernate> hibernateObject;
public BiddingReply(){
	notification = new SuccessFailureWarnig();
	parameterPassing = new HashMap<String, String>();
	bidding = new ArrayList<Bidding>();
	hibernateObject = new ArrayList<BiddingHibernate>();
}
public SuccessFailureWarnig getNotification() {
	return notification;
}
public void setNotification(SuccessFailureWarnig notification) {
	this.notification = notification;
}
public List<Bidding> getBidding() {
	return bidding;
}
public void setBidding(List<Bidding> bidding) {
	this.bidding = bidding;
}
public Map<String, String> getParameterPassing() {
	return parameterPassing;
}
public void setParameterPassing(Map<String, String> parameterPassing) {
	this.parameterPassing = parameterPassing;
}
public List<BiddingHibernate> getHibernateObject() {
	return hibernateObject;
}
public void setHibernateObject(List<BiddingHibernate> hibernateObject) {
	this.hibernateObject = hibernateObject;
}

}
