package hibernate;


	import junit.framework.Assert;

import org.junit.Test;

import replyclasses.BiddingUserVerificationReply;
import standardclasses.BiddingUserVerification;
import boimpl.BiddingUserVerificationBO;
import constantBidding.MessageConstants;

	public class BiddingUserTest2 {
		@Test
		public void updateSignUpHibernate(){
			BiddingUserVerificationReply biddingObject = new BiddingUserVerificationReply();
			BiddingUserVerification object = new BiddingUserVerification();
			object.setUserName("gamerboy");
			object.setDesignation("heyasdsd");
			object.setServiceTaxNumber("sdsd");
			object.setOfficeAddress("khatrewali gali");
			BiddingUserVerificationBO bo  = new BiddingUserVerificationBO();
			bo.insertIntoBiddingVerificationHibernate(object);
			Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
		}
		@Test
		public void getAllFromSignUp(){
			BiddingUserVerificationReply biddingObject = new BiddingUserVerificationReply();
			BiddingUserVerification object = new BiddingUserVerification();
			BiddingUserVerificationBO bo  = new BiddingUserVerificationBO();
			bo.getSelectAllFromBiddingVerificationHibernate();
			Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
		}
		//@Test
		/*public void getFromSignUp(){
			BiddingUserVerificationReply biddingObject = new BiddingUserVerificationReply();
			BiddingUserVerification object = new BiddingUserVerification();
			BiddingUserVerificationBO bo  = new BiddingUserVerificationBO();
			biddingObject = 	bo.getAllFromBiddingVerificationHibernate("gamerboy");
			Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
		}*/
		@Test
		public void getFromSignUp(){
			BiddingUserVerificationReply biddingObject = new BiddingUserVerificationReply();
			BiddingUserVerification object = new BiddingUserVerification();
			BiddingUserVerificationBO bo  = new BiddingUserVerificationBO();
			biddingObject = 	bo.getAllFromBiddingVerificationHibernate("gamerboy");
			Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
		}

	}


