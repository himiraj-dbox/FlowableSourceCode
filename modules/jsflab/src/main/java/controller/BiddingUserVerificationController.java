package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import standardclasses.Bidding;
import standardclasses.BiddingUserVerification;
import boimpl.BiddingInsertionBO;
import boimpl.BiddingUserVerificationBO;

@ManagedBean(name="biddinguserverification")
@ViewScoped
public class BiddingUserVerificationController {
	private int truestScore;
	private String verfiedUser;
	private String designation;
	private String officeAddress;
	private String serviceTaxNumber;
	private String userName;
	private BiddingUserVerificationBO bo;
	private List<BiddingUserVerification> bidVerificationList ;
	
	public BiddingUserVerificationController(){
		 bidVerificationList = new ArrayList<BiddingUserVerification>();
		  bo = new BiddingUserVerificationBO();
		 if(bo.getSelectAllFromBiddingVerificationHibernate().getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
		 bidVerificationList = bo.getSelectAllFromBiddingVerificationHibernate().getProduct();
		 }
		 
	}
	
	public void updateVerifyUser(ActionEvent event){

		String userName =""+event.getComponent().getAttributes().get("userName");
		String verified =""+event.getComponent().getAttributes().get("verified");
		String ratingString =""+event.getComponent().getAttributes().get("rating");
		int rating = Integer.parseInt(ratingString);
		bo.updateBiddingVerificationHibernate(verified, rating, userName);
	}
	
	public void updateInformation(ActionEvent event){
		String userName =""+event.getComponent().getAttributes().get("userName");
		if(null != userName && !userName.isEmpty()){
			BiddingUserVerification bid  = new BiddingUserVerification();
			BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
			bid.setUserName(userName);
			bid.setServiceTaxNumber(getServiceTaxNumber());
			bid.setOfficeAddress(getOfficeAddress());
			bid.setDesignation(getDesignation());
			BiddingUserVerificationBO boImpl = new BiddingUserVerificationBO();
			reply = boImpl.insertIntoBiddingVerificationHibernate(bid);
			
		}
	}
	public int getTruestScore() {
		return truestScore;
	}
	public void setTruestScore(int truestScore) {
		this.truestScore = truestScore;
	}
	public String getVerfiedUser() {
		return verfiedUser;
	}
	public void setVerfiedUser(String verfiedUser) {
		this.verfiedUser = verfiedUser;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getServiceTaxNumber() {
		return serviceTaxNumber;
	}
	public void setServiceTaxNumber(String serviceTaxNumber) {
		this.serviceTaxNumber = serviceTaxNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<BiddingUserVerification> getBidVerificationList() {
		return bidVerificationList;
	}
	public void setBidVerificationList(
			List<BiddingUserVerification> bidVerificationList) {
		this.bidVerificationList = bidVerificationList;
	}

	public BiddingUserVerificationBO getBo() {
		return bo;
	}

	public void setBo(BiddingUserVerificationBO bo) {
		this.bo = bo;
	}
}
