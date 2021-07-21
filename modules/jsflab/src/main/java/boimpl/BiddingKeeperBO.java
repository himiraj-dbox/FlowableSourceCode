package boimpl;

import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import hibernateSignUp.BiddingHibernate;
import hibernateSignUp.BiddingHistoryHibernate;

import java.util.ArrayList;

import constantBidding.MessageConstants;
import notification.SuccessFailureWarnig;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import standardclasses.Bidding;
import standardclasses.BiddingKeeper;
import executequerry.ExecuteQuery;

public class BiddingKeeperBO {

	//Successfully migrated to hibernate
	/*public BiddingKeeperReply insertUpdateBiddingKeeper(BiddingKeeper bk){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply = query.insertIntoBiddingKeeper(reply, bk);
		return reply;
	}*/
	
	public BiddingKeeperReply insertUpdateBiddingKeeperHibernate(BiddingKeeper bk){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		BiddingHistoryHibernate saveHistory = new BiddingHistoryHibernate();
		saveHistory.setBidBy(bk.getUserId());
		saveHistory.setBidRefernceNumber(bk.getBidReferenceNumber());
		saveHistory.setIncreaseBy(bk.getIncreaseBy());
		saveHistory.setPrice(bk.getPrice());
		HibernateDao signUpDao = new HibernateDaoImpl();
		reply.setNotification(signUpDao.insertIntoBiddingKeeperHibernate(saveHistory));
		return reply;
	}
	
	/*public BiddingKeeperReply getAllFromBidding(){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		BiddingKeeper bidding = new BiddingKeeper();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBidKeeperList((new ArrayList<BiddingKeeper>()));
		reply = query.getAllFromBiddingKeeper(reply, bidding);
		return reply;
	}*/
	//Successfully migrated to hibernate
	public BiddingKeeperReply getAllFromBiddingHibernate(){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		HibernateDao signUpDao = new HibernateDaoImpl();
		reply = signUpDao.getFromBiddingHistorytable(reply);
		return reply;
	}
/*	public BiddingKeeperReply getMaxAmountFromBidding(int bid){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		BiddingKeeper bidding = new BiddingKeeper();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBidKeeperList((new ArrayList<BiddingKeeper>()));
		reply = query.getMaxAmountFromBiddingKeeper(reply, bidding,bid);
		return reply;
	}*/
	//Successfully migrated to hibernate
	public BiddingKeeperReply getMaxFromBiddingHibernate(Integer bid){
		BiddingKeeperReply reply = new BiddingKeeperReply();
		reply.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBERHISTORY, bid.toString());
		HibernateDao signUpDao = new HibernateDaoImpl();
		reply = signUpDao.getMaxFromBiddingHistorytable(reply);
		for(BiddingHistoryHibernate cnvrtgHibernateToUI : reply.getBiddingHistoryHibernate()){
			BiddingKeeper tempObjectForConvertion = new BiddingKeeper();
			tempObjectForConvertion.setBidReferenceNumber(cnvrtgHibernateToUI.getBidRefernceNumber());
			tempObjectForConvertion.setIncreaseBy(cnvrtgHibernateToUI.getIncreaseBy());
			tempObjectForConvertion.setPrice(cnvrtgHibernateToUI.getPrice());
			tempObjectForConvertion.setUserId(cnvrtgHibernateToUI.getBidBy());
			reply.getBidKeeperList().add(tempObjectForConvertion);
		}
		return reply;
	}
	
	
}
