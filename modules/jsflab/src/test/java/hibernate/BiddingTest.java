package hibernate;

import java.util.Date;

import org.junit.Test;

import constantBidding.MessageConstants;
import junit.framework.Assert;
import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import hibernateSignUp.BiddingHibernate;
import replyclasses.BiddingReply;
import standardclasses.Bidding;
import boimpl.BiddingInsertionBO;

public class BiddingTest {
private BiddingInsertionBO biddingInsertionMethods ;


public BiddingInsertionBO getBiddingInsertionMethods() {
	return biddingInsertionMethods;
}
@Test
public void insertIntoBiddingMethodsTest() {
	BiddingReply reply = new BiddingReply();
	BiddingHibernate saveBid = new BiddingHibernate();
	saveBid.setBidReferenceNumber(1234567);
	saveBid.setCondition("New");
	saveBid.setEndDate(new Date());
	saveBid.setImageAssociated("abcdef");
	saveBid.setItemDescription("abcdegfhtr asdasd");
	saveBid.setLocation("asdsadas");
	saveBid.setManufacturerName("xyzAbcdEgf");
	saveBid.setPostedBy("ancdefghtr");
	saveBid.setProductId("nokia asd");
	saveBid.setProductName("Sexfsedfs");
	saveBid.setQuantity(566);
	saveBid.setStartingDate(new Date());
	saveBid.setStatus("Y");
	saveBid.setWarranty("yes");
	HibernateDao signUpDao = new HibernateDaoImpl();
	reply.setNotification(signUpDao.insertIntoBiddingHibernate(saveBid));
	Assert.assertEquals(MessageConstants.SUCCESS, reply.getNotification().getStatus());
}
@Test
public void getBidReferenceNUmberHibernate(){
	Integer bidReferenceNumber = 1234567;
	BiddingReply biddingObject = new BiddingReply();
	biddingObject.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER,bidReferenceNumber.toString());
	HibernateDao biddingDao = new HibernateDaoImpl();
	biddingDao.checkBidReference(biddingObject);
	Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
}
@Test
public void getAllBidReferenceNUmberHibernate(){
	BiddingReply biddingObject = new BiddingReply();
	HibernateDao biddingDao = new HibernateDaoImpl();
	biddingDao.getFromBiddingtable(biddingObject);
	Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
}
@Test
public void getBidReferenceNUmberHibernate2(){

	BiddingReply biddingObject = new BiddingReply();
	biddingObject.getParameterPassing().put(MessageConstants.BIDREFERENCENUMBER,"1234567");
	HibernateDao biddingDao = new HibernateDaoImpl();
	biddingDao.checkBidReference(biddingObject);
	Assert.assertEquals(MessageConstants.SUCCESS, biddingObject.getNotification().getStatus());
}
}
