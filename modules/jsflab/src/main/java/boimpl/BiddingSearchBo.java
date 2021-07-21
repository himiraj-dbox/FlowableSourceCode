package boimpl;

import java.util.ArrayList;
import java.util.List;

import notification.SuccessFailureWarnig;
import replyclasses.BiddingReply;
import replyclasses.ProductReply;
import standardclasses.Bidding;
import standardclasses.Product;
import executequerry.ExecuteQuery;

public class BiddingSearchBo {
	public BiddingReply getRecordFromBidding(Boolean customSearch,String queryApahe){
		BiddingReply reply = new BiddingReply();
		Bidding bidding = new Bidding();
		SuccessFailureWarnig notifications = new SuccessFailureWarnig();
		List<Bidding> biddings = new ArrayList<Bidding>();
		biddings.add(bidding);
		reply.setNotification(notifications);
		reply.setBidding(biddings);
		ExecuteQuery query = new ExecuteQuery();
		reply = query.getFromBidding(reply, bidding,customSearch,queryApahe);
		return reply;
	}
}
