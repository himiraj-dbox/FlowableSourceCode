package replyclasses;

import hibernateSignUp.BiddingHistoryHibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notification.SuccessFailureWarnig;
import standardclasses.BiddingKeeper;

public class BiddingKeeperReply {
private List<BiddingKeeper> bidKeeperList;
private SuccessFailureWarnig notification;
private List<BiddingHistoryHibernate> biddingHistoryHibernate;
private Map<String,String> parameterPassing;
public BiddingKeeperReply(){
	bidKeeperList = new ArrayList<BiddingKeeper>();
	notification = new SuccessFailureWarnig();
	parameterPassing = new HashMap<String, String>();
	biddingHistoryHibernate = new ArrayList<BiddingHistoryHibernate>();
}

public List<BiddingKeeper> getBidKeeperList() {
	return bidKeeperList;
}
public void setBidKeeperList(List<BiddingKeeper> bidKeeperList) {
	this.bidKeeperList = bidKeeperList;
}
public SuccessFailureWarnig getNotification() {
	return notification;
}
public void setNotification(SuccessFailureWarnig notification) {
	this.notification = notification;
}
public Map<String, String> getParameterPassing() {
	return parameterPassing;
}
public void setParameterPassing(Map<String, String> parameterPassing) {
	this.parameterPassing = parameterPassing;
}

public List<BiddingHistoryHibernate> getBiddingHistoryHibernate() {
	return biddingHistoryHibernate;
}

public void setBiddingHistoryHibernate(
		List<BiddingHistoryHibernate> biddingHistoryHibernate) {
	this.biddingHistoryHibernate = biddingHistoryHibernate;
}


}
