package boimpl;

import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import hibernateSignUp.BiddingApprvalSystem;
import hibernateSignUp.SignUp;
import hibernateSignUp.SignUpReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constantBidding.MessageConstants;
import controller.SignUP;
import notification.SuccessFailureWarnig;
import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingUserVerificationReply;
import standardclasses.BiddingAppravalSystem;
import standardclasses.BiddingUserVerification;
import executequerry.ExecuteQuery;

public class BiddingUserVerificationBO {

	//Succesfully Migrated to hibernate
	/*public BiddingUserVerificationReply insertIntoBiddingVerification(
			BiddingUserVerification bid) {
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply = query.insertIntoBiddingVerification(reply, bid);
		
	 
	 BiddingReply reply = new BiddingReply();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply = query.insertIntoBidding(reply, bidding);
	 
	 
	 
		return reply;
	}*/
	public BiddingUserVerificationReply insertIntoBiddingVerificationHibernate(
			BiddingUserVerification bid) {
		HibernateDao hibernateDao = new HibernateDaoImpl();
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		SignUpReply passingParameter = new SignUpReply();
		passingParameter.getParameterPassing().put(MessageConstants.USERNAME, bid.getUserName());
		passingParameter.getParameterPassing().put(MessageConstants.Designation, bid.getDesignation());
		passingParameter.getParameterPassing().put(MessageConstants.OFFICEADDRESS, bid.getOfficeAddress());
		passingParameter.getParameterPassing().put(MessageConstants.SERVICETAX, bid.getServiceTaxNumber());
		
		passingParameter = hibernateDao.updateSignUP(passingParameter);
		reply.setNotification(passingParameter.getNotification());;
		return reply;
	}
	
	//Succesfully converted into Hibernate
	/*public BiddingUserVerificationReply getSelectAllFromBiddingVerification(){
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		BiddingUserVerification biddingUserVerification = new BiddingUserVerification();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setProduct(new ArrayList<BiddingUserVerification>());
		reply = query.getSelectAllFromBiddingVerification(reply, biddingUserVerification);
		return reply;
	}*/
	
	public BiddingUserVerificationReply getSelectAllFromBiddingVerificationHibernate(){
		HibernateDao signUpDao = new HibernateDaoImpl();
		SignUpReply reply = new SignUpReply();
		Map<String,String> parameter = new HashMap<String, String>();
	
		reply.setParameterPassing(parameter);
		reply = signUpDao.getallFromSignUp(reply);
		
		
		BiddingUserVerificationReply finalReply = new BiddingUserVerificationReply();
		BiddingUserVerification biddingUserVerification = null;
		for(SignUp signUpData : reply.getRegisterUserList()){
			biddingUserVerification = new BiddingUserVerification();
			biddingUserVerification.setDesignation(signUpData.getDesgination());
			biddingUserVerification.setOfficeAddress(signUpData.getOfficeAddress());
			biddingUserVerification.setServiceTaxNumber(signUpData.getServiceTax());
			biddingUserVerification.setUserName(signUpData.getUserName());
			finalReply.getProduct().add(biddingUserVerification);
		}
		finalReply.setNotification(reply.getNotification());
		return finalReply;
	}
	//Successfully converted into hibernate
	/*public BiddingUserVerificationReply getAllFromBiddingVerification(String userName){
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		BiddingUserVerification biddingUserVerification = new BiddingUserVerification();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setProduct(new ArrayList<BiddingUserVerification>());
		reply = query.getAllFromBiddingVerification(reply, biddingUserVerification,userName);
		return reply;
	}*/
	public BiddingUserVerificationReply getAllFromBiddingVerificationHibernate(String userName){
		HibernateDao signUpDao = new HibernateDaoImpl();
		SignUpReply reply = new SignUpReply();
		Map<String,String> parameter = new HashMap<String, String>();
	
		reply.setParameterPassing(parameter);
		reply.getParameterPassing().put(MessageConstants.USERNAME, userName);
		reply = signUpDao.getFromSignUP(reply);
		
		
		BiddingUserVerificationReply finalReply = new BiddingUserVerificationReply();
		BiddingUserVerification biddingUserVerification = null;
		for(SignUp signUpData : reply.getRegisterUserList()){
			biddingUserVerification = new BiddingUserVerification();
			biddingUserVerification.setDesignation(signUpData.getDesgination());
			biddingUserVerification.setOfficeAddress(signUpData.getOfficeAddress());
			biddingUserVerification.setServiceTaxNumber(signUpData.getServiceTax());
			biddingUserVerification.setUserName(signUpData.getUserName());
			biddingUserVerification.setVerfiedUser(signUpData.getVerified());
			biddingUserVerification.setTruestScore(signUpData.getTrustScore());
			finalReply.getProduct().add(biddingUserVerification);
		}
		finalReply.setNotification(reply.getNotification());
		return finalReply;
	}
	
	
/*	public BiddingAppravalSystemReply getSelectAllFromBiddingAppravalSystem(String userName, int bidReferenceNumber){
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		BiddingAppravalSystem biddingAppravalSystem = new BiddingAppravalSystem();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBiddingAppravalSystem(new ArrayList<BiddingAppravalSystem>());
		reply = query.getSelectAllFromBiddingAppravalSystem(reply, biddingAppravalSystem);
		return reply;
	}
	*/
	public BiddingAppravalSystemReply getSelectAllFromBiddingAppravalSystemHibernate(String userName, int bidReferenceNumber){
		String bidReferenceNumber2 = new String(bidReferenceNumber+"");
		HibernateDao biddingApprovalSystem = new HibernateDaoImpl();
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		reply.getParameterPassing().put(MessageConstants.USERNAME, userName);
		reply.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER, bidReferenceNumber2);
		reply = biddingApprovalSystem.getSelectAllFromBiddingAppravalSystem(reply);
		return reply;
	}
	
/*	public BiddingAppravalSystemReply getAllFromBiddingAppravalSystem(String userName, int bidReferenceNumber){
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		BiddingAppravalSystem biddingAppravalSystem = new BiddingAppravalSystem();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBiddingAppravalSystem(new ArrayList<BiddingAppravalSystem>());
		reply = query.getAllFromBiddingAppravalSystem(reply, biddingAppravalSystem,userName,bidReferenceNumber);
		return reply;
	}*/
	public BiddingAppravalSystemReply getAllFromBiddingAppravalSystemHibernate(String userName, int bidReferenceNumber){
		String bidReferenceNumber2 = new String(bidReferenceNumber+"");
		HibernateDao biddingApprovalSystem = new HibernateDaoImpl();
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		reply.getParameterPassing().put(MessageConstants.USERNAME, userName);
		reply.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER, bidReferenceNumber2);
		
		reply = biddingApprovalSystem.getAllFromBiddingAppravalSystem(reply);
		for(BiddingApprvalSystem cnvtgHibNateToUI : reply.getHibernateBiddingApproval()){
			BiddingAppravalSystem tempBiddingAprova = new BiddingAppravalSystem();
			tempBiddingAprova.setBidReferenceNumber(cnvtgHibNateToUI.getBidReferenceNumber());
			tempBiddingAprova.setComments(cnvtgHibNateToUI.getComments());
			tempBiddingAprova.setUserAproved(cnvtgHibNateToUI.getUserAproved());
			tempBiddingAprova.setUserName(cnvtgHibNateToUI.getUserName());
			reply.getBiddingAppravalSystem().add(tempBiddingAprova);
		}
		return reply;
	}
/*	public BiddingAppravalSystemReply insertIntoBiddingAppravalSystem(
			BiddingAppravalSystem bidAppravalSystem) {
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply = query.insertIntoBiddingAppravalSystem(reply, bidAppravalSystem);
		return reply;
	}*/
	
	public BiddingAppravalSystemReply insertIntoBiddingAppravalSystemHibernate(
			BiddingAppravalSystem bidAppravalSystem) {
		BiddingApprvalSystem biddingApprvalSystemObject  = new BiddingApprvalSystem();
		HibernateDao biddingApprovalSystem = new HibernateDaoImpl();
		BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
		biddingApprvalSystemObject.setBidReferenceNumber(bidAppravalSystem.getBidReferenceNumber());
		biddingApprvalSystemObject.setComments(bidAppravalSystem.getComments());
		biddingApprvalSystemObject.setUserAproved(bidAppravalSystem.getUserAproved());
		biddingApprvalSystemObject.setUserName(bidAppravalSystem.getUserName());
		reply.setNotification(biddingApprovalSystem.insertIntoBiddingApprovalSystem(biddingApprvalSystemObject));
		return reply;
	}
	public BiddingUserVerificationReply updateBiddingVerification(String verified, int rating, String userName){
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setProduct(new ArrayList<BiddingUserVerification>());
		reply = query.updateBiddingVerification(reply, verified, rating, userName);
		return reply;
	}
	public BiddingUserVerificationReply updateBiddingVerificationHibernate(String verified, int rating, String userName){
		String ratingString = new String(rating+"");
		HibernateDao biddingApprovalSystem = new HibernateDaoImpl();
		BiddingUserVerificationReply reply = new BiddingUserVerificationReply();
		reply.getParameterPassing().put(MessageConstants.USERNAME, userName);
		reply.getParameterPassing().put(MessageConstants.TRUSTSCORE, ratingString);
		reply.getParameterPassing().put(MessageConstants.VERIFID, verified);
		reply.setNotification(biddingApprovalSystem.updateBiddingHistory(reply));
		return reply;
	}
	

}
