package hibernateDao;

import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import replyclasses.HireMeTilesCatalogReply;
import hibernateSignUp.BiddingApprvalSystem;
import hibernateSignUp.BiddingHibernate;
import hibernateSignUp.BiddingHistoryHibernate;
import hibernateSignUp.SignUp;
import hibernateSignUp.SignUpReply;
import notification.SuccessFailureWarnig;

public interface HibernateDao {
	public SuccessFailureWarnig insertIntoSignUp(SignUp signUp) ;
	public SignUpReply getFromSignUP(SignUpReply reply);
	public SignUpReply checkUserName(SignUpReply userName);
	public SuccessFailureWarnig insertIntoBiddingHibernate(
			BiddingHibernate saveBid);
	public BiddingReply checkBidReference(BiddingReply reply);
	public BiddingReply getFromBiddingtable(BiddingReply reply);
	public SuccessFailureWarnig insertIntoBiddingKeeperHibernate(
			BiddingHistoryHibernate saveHistory);
	public BiddingKeeperReply getFromBiddingHistorytable(BiddingKeeperReply reply);
	public BiddingKeeperReply getMaxFromBiddingHistorytable(BiddingKeeperReply reply);
	public SignUpReply updateSignUP(SignUpReply passringParameter);
	public SignUpReply getallFromSignUp(SignUpReply reply);
	public BiddingAppravalSystemReply getSelectAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply);
	public BiddingAppravalSystemReply getAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply);
	public SuccessFailureWarnig insertIntoBiddingApprovalSystem(
			BiddingApprvalSystem biddingApprvalSystem);
	public SuccessFailureWarnig updateBiddingHistory(BiddingUserVerificationReply reply);
	public <T> SuccessFailureWarnig save(Class<T> t,SuccessFailureWarnig notification,Object o) ;
	public <T> SuccessFailureWarnig update(Class<T> t);
	public boolean hireMeUserAlreadyExists(String mobileNumber);
	public HireMeTilesCatalogReply fetchChildCatalogData(HireMeTilesCatalogReply reply);
	
}
