package controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import replyclasses.BiddingReply;
import standardclasses.Bidding;
import boimpl.BiddingSearchBo;

@ManagedBean(name="biddingsearch")
@ViewScoped
public class BiddingSearch {
private List<Bidding> bidding;
private String apacheWhereQuery;
private boolean enableQuery;
private  String destination="/jsflab/src/main/resources/imagesProduct";

@PostConstruct
public void init(){
	BiddingReply reply = new BiddingReply();
	BiddingSearchBo boimpl  = new BiddingSearchBo();
	reply =boimpl.getRecordFromBidding(enableQuery, apacheWhereQuery);
	if(reply.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
		setBidding(reply.getBidding());
		}else{
			//show Erro on page Type of Error too	
		}
	
}

public List<Bidding> getBidding() {
	return bidding;
}

public void setBidding(List<Bidding> bidding) {
	this.bidding = bidding;
}

public String getApacheWhereQuery() {
	
	return apacheWhereQuery;
}

public void setApacheWhereQuery(String apacheWhereQuery) {
	this.apacheWhereQuery = apacheWhereQuery;
}

public boolean isEnableQuery() {
	return enableQuery;
}

public void setEnableQuery(boolean enableQuery) {
	this.enableQuery = enableQuery;
}

public String getDestination() {
	return destination;
}

public void setDestination(String destination) {
	this.destination = destination;
}



}
