package replyclasses;

import hibernateSignUp.BiddingApprvalSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notification.SuccessFailureWarnig;
import standardclasses.BiddingAppravalSystem;

public class BiddingAppravalSystemReply {
	private SuccessFailureWarnig notification;
	private List<BiddingAppravalSystem> biddingAppravalSystem;
	private Map<String,String> parameterPassing;
	private List<BiddingApprvalSystem> hibernateBiddingApproval;
	
	public BiddingAppravalSystemReply(){
		notification = new SuccessFailureWarnig();
		biddingAppravalSystem = new ArrayList<BiddingAppravalSystem>();
		parameterPassing = new HashMap<String, String>();
		hibernateBiddingApproval = new ArrayList<BiddingApprvalSystem>();
	}
	public SuccessFailureWarnig getNotification() {
		return notification;
	}
	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}
	public List<BiddingAppravalSystem> getBiddingAppravalSystem() {
		return biddingAppravalSystem;
	}
	public void setBiddingAppravalSystem(
			List<BiddingAppravalSystem> biddingAppravalSystem) {
		this.biddingAppravalSystem = biddingAppravalSystem;
	}
	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}
	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}
	public List<BiddingApprvalSystem> getHibernateBiddingApproval() {
		return hibernateBiddingApproval;
	}
	public void setHibernateBiddingApproval(
			List<BiddingApprvalSystem> hibernateBiddingApproval) {
		this.hibernateBiddingApproval = hibernateBiddingApproval;
	}
	

	

}
