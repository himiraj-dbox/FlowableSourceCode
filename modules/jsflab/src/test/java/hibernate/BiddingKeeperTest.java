package hibernate;

import junit.framework.Assert;

import org.junit.Test;

import constantBidding.MessageConstants;
import boimpl.BiddingKeeperBO;
import replyclasses.BiddingKeeperReply;
import standardclasses.BiddingKeeper;

public class BiddingKeeperTest {
	@Test
	public void insertIntoBiddingHistory(){
		BiddingKeeper biddingObjext = new BiddingKeeper();
		biddingObjext.setBidReferenceNumber(1234567);
		biddingObjext.setIncreaseBy(500);
		biddingObjext.setPrice(1000);
		biddingObjext.setUserId("gauravLalwani90@gmail.com");
		BiddingKeeperBO bo = new BiddingKeeperBO();
		BiddingKeeperReply reply = bo.insertUpdateBiddingKeeperHibernate(biddingObjext);
		Assert.assertEquals(MessageConstants.SUCCESS, reply.getNotification().getStatus());
	}
	
	@Test
	public void getAllBiddingHistory(){
		BiddingKeeperBO bo = new BiddingKeeperBO();
		BiddingKeeperReply reply = new BiddingKeeperReply();
		reply = bo.getAllFromBiddingHibernate();
		Assert.assertEquals(MessageConstants.SUCCESS, reply.getNotification().getStatus());
	}
	@Test
	public void getMaxFromBiddingHistory(){
		BiddingKeeperBO bo = new BiddingKeeperBO();
		BiddingKeeperReply reply = new BiddingKeeperReply();
		reply = bo.getMaxFromBiddingHibernate(1234567);
		Assert.assertEquals(MessageConstants.SUCCESS, reply.getNotification().getStatus());
	}
}
