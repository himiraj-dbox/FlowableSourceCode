package controller;

import hibernateSignUp.BiddingHistoryHibernate;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import notification.SuccessFailureWarnig;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import standardclasses.Bidding;
import standardclasses.BiddingKeeper;
import boimpl.BiddingInsertionBO;
import boimpl.BiddingKeeperBO;

@ManagedBean(name="biddingKeeper")
@ViewScoped
public class BidKeeper {
private List<Bidding> biddingList;
private int bidReferenceNumber;
private int amount;
private int increaseBy;
private List<BiddingKeeper> bidKeeper;


public BidKeeper(){
	biddingList = new ArrayList<Bidding>();
	BiddingInsertionBO bo  = new BiddingInsertionBO();
	BiddingReply reply =  bo.getAllFromBiddingHibernate();
	if(reply.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
		for(int i =0; i<= reply.getHibernateObject().size()-1;i++){
			Bidding tempBidForPopulation  = new Bidding();
			
			tempBidForPopulation.setBidReferceNo(reply.getHibernateObject().get(i).getBidReferenceNumber());
			tempBidForPopulation.setCondition(reply.getHibernateObject().get(i).getCondition());
			tempBidForPopulation.setEndDate(reply.getHibernateObject().get(i).getEndDate());
			tempBidForPopulation.setImagePath(reply.getHibernateObject().get(i).getImageAssociated());
			tempBidForPopulation.setItemDexcription(reply.getHibernateObject().get(i).getItemDescription());
			tempBidForPopulation.setLocation(reply.getHibernateObject().get(i).getLocation());
			tempBidForPopulation.setManufacturerName(reply.getHibernateObject().get(i).getManufacturerName());
			tempBidForPopulation.setPostedBy(reply.getHibernateObject().get(i).getPostedBy());
			tempBidForPopulation.setProductID(reply.getHibernateObject().get(i).getProductId());
			tempBidForPopulation.setProductName(reply.getHibernateObject().get(i).getProductName());
			tempBidForPopulation.setQuantity(reply.getHibernateObject().get(i).getQuantity());
			tempBidForPopulation.setStartingDate(reply.getHibernateObject().get(i).getStartingDate());
			tempBidForPopulation.setStatus(reply.getHibernateObject().get(i).getStatus());
			//tempBidForPopulation.setTimeEnd(reply.getHibernateObject().get(i).get());
			//tempBidForPopulation.setTimeStarted(reply.getBidding().get(i).getTimeStarted());
			tempBidForPopulation.setWarranty(reply.getHibernateObject().get(i).getWarranty());
			biddingList.add(tempBidForPopulation);
	}
	}
	getAllBiddingKeeper();
}

 public void getAllBiddingKeeper(){
	 BiddingKeeperBO bokeep = new BiddingKeeperBO();
	 bidKeeper = new ArrayList<BiddingKeeper>();
		BiddingKeeperReply replyK  = bokeep.getAllFromBiddingHibernate();
		if(replyK.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
			for(BiddingHistoryHibernate conHiberToUi : replyK.getBiddingHistoryHibernate()){
				BiddingKeeper tempBidKeeper = new BiddingKeeper();
				tempBidKeeper.setBidReferenceNumber(conHiberToUi.getBidRefernceNumber());
				tempBidKeeper.setIncreaseBy(conHiberToUi.getIncreaseBy());
				tempBidKeeper.setPrice(conHiberToUi.getPrice());
				tempBidKeeper.setUserId(conHiberToUi.getBidBy());
				bidKeeper.add(tempBidKeeper);
			}
			
		}
 }
public void submit(ActionEvent event){
	
	BiddingKeeper biddingkeeper = new BiddingKeeper();
	biddingkeeper.setBidReferenceNumber(getBidReferenceNumber());
	biddingkeeper.setPrice(getAmount());
	biddingkeeper.setIncreaseBy(getIncreaseBy());
	BiddingKeeperBO bo = new BiddingKeeperBO();
	BiddingKeeperReply reply   = bo.insertUpdateBiddingKeeperHibernate(biddingkeeper);
	getAllBiddingKeeper();
}

public List<Bidding> getBiddingList() {
	return biddingList;
}

public void setBiddingList(List<Bidding> biddingList) {
	this.biddingList = biddingList;
}


public int getBidReferenceNumber() {
	return bidReferenceNumber;
}


public void setBidReferenceNumber(int bidReferenceNumber) {
	this.bidReferenceNumber = bidReferenceNumber;
}


public int getAmount() {
	return amount;
}


public void setAmount(int amount) {
	this.amount = amount;
}

public List<BiddingKeeper> getBidKeeper() {
	return bidKeeper;
}

public void setBidKeeper(List<BiddingKeeper> bidKeeper) {
	this.bidKeeper = bidKeeper;
}

public int getIncreaseBy() {
	return increaseBy;
}

public void setIncreaseBy(int increaseBy) {
	this.increaseBy = increaseBy;
}




}
