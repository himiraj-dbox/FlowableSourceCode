package hibernate;

import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import junit.framework.Assert;

import org.junit.Test;

import replyclasses.BiddingUserVerificationReply;
import standardclasses.BiddingUserVerification;
import boimpl.BiddingUserVerificationBO;
import constantBidding.MessageConstants;

public class BiddingUserTest {
	@Test
	public void updateSignUpHibernate(){
		BiddingUserVerificationReply biddingObject = new BiddingUserVerificationReply();
		BiddingUserVerification object = new BiddingUserVerification();
		BiddingUserVerificationBO bo  = new BiddingUserVerificationBO();
		bo.insertIntoBiddingVerificationHibernate(object);
		Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
	}

}
