package boimpl;

import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import hibernateSignUp.BiddingHibernate;

import java.util.ArrayList;

import constantBidding.MessageConstants;
import executequerry.ExecuteQuery;
import notification.SuccessFailureWarnig;
import replyclasses.BiddingReply;
import standardclasses.Bidding;

public class BiddingInsertionBO {
	
	//Succesfully Migrated to the Hibernate
	/*public BiddingReply insertIntoBidding(Bidding bidding){
		BiddingReply reply = new BiddingReply();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		
		reply = query.insertIntoBidding(reply, bidding);
		return reply;
	}*/
	
	
		public BiddingReply insertIntoBiddingHibernate(Bidding bidding){
			BiddingReply reply = new BiddingReply();
			BiddingHibernate saveBid = new BiddingHibernate();
			saveBid.setBidReferenceNumber(bidding.getBidReferceNo());
			saveBid.setCondition(bidding.getCondition());
			saveBid.setEndDate(bidding.getEndDate());
			saveBid.setImageAssociated(bidding.getImagePath());
			saveBid.setItemDescription(bidding.getItemDexcription());
			saveBid.setLocation(bidding.getLocation());
			saveBid.setManufacturerName(bidding.getManufacturerName());
			saveBid.setPostedBy(bidding.getPostedBy());
			saveBid.setProductId(bidding.getProductID());
			saveBid.setProductName(bidding.getProductName());
			saveBid.setQuantity(bidding.getQuantity());
			saveBid.setStartingDate(bidding.getStartingDate());
			saveBid.setStatus(bidding.getStatus());
			saveBid.setWarranty(bidding.getWarranty());
			saveBid.setItemName(bidding.getItemName());
			HibernateDao signUpDao = new HibernateDaoImpl();
			reply.setNotification(signUpDao.insertIntoBiddingHibernate(saveBid));
			
			return reply;
		}
		public int getBidrefrenceNumberHibernate(Integer bidReferenceNumber){
			int reply = -1;
			BiddingReply biddingObject = new BiddingReply();
			biddingObject.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER,bidReferenceNumber.toString());
			HibernateDao biddingDao = new HibernateDaoImpl();
			biddingDao.checkBidReference(biddingObject);
			if(biddingObject.getNotification().getStatus().equalsIgnoreCase(MessageConstants.SUCCESS)){
				if(biddingObject.getNotification().getMessage().equalsIgnoreCase(MessageConstants.RECORDFOUND)){
					reply = 1;
				}
			}
			
			return reply;
		}
	
	//Successfully converted in hibernate
/*	public int getBidrefrenceNumberFromBidding(int bidReferenceNumber){
		int reply = -1;
		ExecuteQuery query = new ExecuteQuery();
	
		reply = query.getBidReferenceNumber(bidReferenceNumber);
		return reply;
	}*/
		public BiddingReply getAllFromBiddingHibernate(){
			BiddingReply reply = null;
			BiddingReply biddingObject = new BiddingReply();
			HibernateDao biddingDao = new HibernateDaoImpl();
			reply = biddingDao.getFromBiddingtable(biddingObject);
			return reply;
		}
		//SuccessFully Converted Into Hibernate
	/*public BiddingReply getAllFromBidding(){
		BiddingReply reply = new BiddingReply();
		Bidding bidding = new Bidding();
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBidding(new ArrayList<Bidding>());
		reply = query.getAllFromBidding(reply, bidding);
		return reply;
	}*/
	public BiddingReply getAllFromBiddingWithBid(String bid){
		BiddingReply reply = new BiddingReply();
		if(bid != null && !bid.isEmpty()){
		int bidreference = Integer.parseInt(bid);
		
		Bidding bidding = new Bidding();
		bidding.setBidReferceNo(bidreference);
		reply.setNotification(new SuccessFailureWarnig());
		ExecuteQuery query = new ExecuteQuery();
		reply.setBidding(new ArrayList<Bidding>());
		reply = query.getAllFromBiddingWithBid(reply, bidding);
		}
		return reply;
		
	}
	public BiddingReply getAllFromBiddingWithBidHibernate(String bid){
		;
		BiddingReply reply = new BiddingReply();
		reply.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER,bid);
		HibernateDao biddingDao = new HibernateDaoImpl();
		biddingDao.checkBidReference(reply);
		for(BiddingHibernate covrtgIntoObject : reply.getHibernateObject()){
			Bidding tempBiddingObject  = new Bidding();
			tempBiddingObject.setBidReferceNo(covrtgIntoObject.getBidReferenceNumber());
			tempBiddingObject.setCondition(covrtgIntoObject.getCondition());
			tempBiddingObject.setEndDate(covrtgIntoObject.getEndDate());
			tempBiddingObject.setImagePath(covrtgIntoObject.getImageAssociated());
			tempBiddingObject.setItemDexcription(covrtgIntoObject.getItemDescription());
			tempBiddingObject.setLocation(covrtgIntoObject.getLocation());
			tempBiddingObject.setLocations(covrtgIntoObject.getLocation());
			tempBiddingObject.setManufacturerName(covrtgIntoObject.getManufacturerName());
			tempBiddingObject.setPostedBy(covrtgIntoObject.getPostedBy());
			tempBiddingObject.setProductID(covrtgIntoObject.getProductId());
			tempBiddingObject.setProductName(covrtgIntoObject.getProductName());
			tempBiddingObject.setQuantity(covrtgIntoObject.getQuantity());
			tempBiddingObject.setStartingDate(covrtgIntoObject.getStartingDate());
			tempBiddingObject.setStatus(covrtgIntoObject.getStatus());
			tempBiddingObject.setItemName(covrtgIntoObject.getItemName());
			//tempBiddingObject.setTimeEnd(covrtgIntoObject.get);
			//tempBiddingObject.setTimeStarted(timeStarted);
			tempBiddingObject.setWarranty(covrtgIntoObject.getWarranty());
			reply.getBidding().add(tempBiddingObject);
		}
		return reply;
		
	}


}
