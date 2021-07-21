package hibernateDao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import constantBidding.MessageConstants;
import hibernate.HibernateUtil;
import hibernateSignUp.BiddingApprvalSystem;
import hibernateSignUp.BiddingHibernate;
import hibernateSignUp.BiddingHistoryHibernate;
import hibernateSignUp.HireMeTileTilesCatalog;
import hibernateSignUp.SignUp;
import hibernateSignUp.SignUpReply;
import hibernateSignUp.TileUserRegistration;
import notification.SuccessFailureWarnig;
import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import replyclasses.HireMeTilesCatalogReply;

public class HibernateDaoImpl implements HibernateDao {
	private SuccessFailureWarnig notification = new SuccessFailureWarnig();
	
	@Override
	public HireMeTilesCatalogReply fetchChildCatalogData(HireMeTilesCatalogReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(HireMeTileTilesCatalog.class);
			criteria.add(Restrictions.eq(MessageConstants.SHOPTYPE, reply
					.getParameterPassing().get(MessageConstants.SHOPTYPE)));
			criteria.add(Restrictions.eq(MessageConstants.SUBCATEGORY, reply
					.getParameterPassing().get(MessageConstants.SUBCATEGORY)));
			criteria.add(Restrictions.eq(MessageConstants.THIRDCATEGORY, reply
					.getParameterPassing().get(MessageConstants.THIRDCATEGORY)));
			List<?> tileCatallog = criteria.list();
			if (null != tileCatallog && !tileCatallog.isEmpty()) {
				reply.setPeristanTObject(
						(List<HireMeTileTilesCatalog>) tileCatallog);
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("WARNING");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		} finally {
			
		}
		return reply;
	}
	
	@Override
	public boolean hireMeUserAlreadyExists(String mobileNumber) {
		Session session = null;
	
		List<?> value = null;
		try {
			/*

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
		 result = session
	                         .createQuery("from hire_me_usr_reg where mobile_number=:"+MessageConstants.MOBILENUMBER)
	                         .setParameter(MessageConstants.MOBILENUMBER, mobileNumber)
	                         .list();*/
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		value = 
				(List<?>) session.get(
						TileUserRegistration.class,mobileNumber);
		session.getTransaction().commit();
		notification.setStatus("SUCCESS");
		/*notification
				.setMessage("Data Retrived successfully from Sign Up table for User : "
						+ reply.getParameterPassing().get("userName"));*/
		session.close();
		} catch (Exception e) {
			/*notification.setStatus("Failure");
			notification.setMessage(e.getMessage());*/
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				notification.setStatus(MessageConstants.WARNIG);
				notification.setMessage(e.getMessage());
			}
			
		}
		if(value == null ||value.size() == 0  ){
			return true;
		}
	
	    return false;
	}
    
	@Override
	public <T> SuccessFailureWarnig save(Class<T> t,SuccessFailureWarnig notification,Object o) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(o);

			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			/*notification
					.setMessage("Data Succesfully saved in signUp table for  user name : "
							+ signUp.getUserName());*/
			/*session.close();*/
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				notification.setStatus(MessageConstants.WARNIG);
				notification.setMessage(e.getMessage());
			}
			
		}
		return notification;
	}
	
	@Override
	public <T> SuccessFailureWarnig update(Class<T> t) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.update(t);

			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			/*notification
					.setMessage("Data Succesfully saved in signUp table for  user name : "
							+ signUp.getUserName());*/
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				notification.setStatus(MessageConstants.WARNIG);
				notification.setMessage(e.getMessage());
			}
			
		}
		return notification;
	}
	
	@SuppressWarnings("finally")
	public SuccessFailureWarnig insertIntoSignUp(SignUp signUp) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(signUp);

			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			notification
					.setMessage("Data Succesfully saved in signUp table for  user name : "
							+ signUp.getUserName());
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				notification.setStatus(MessageConstants.WARNIG);
				notification.setMessage(e.getMessage());
			}
			return notification;
		}
	}

	@SuppressWarnings({ "finally", "unchecked" })
	public SignUpReply getFromSignUP(SignUpReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			reply.getRegisterUserList().add(
					(SignUp) session.get(
							SignUp.class,
							reply.getParameterPassing().get(
									MessageConstants.USERNAME)));
			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			notification
					.setMessage("Data Retrived successfully from Sign Up table for User : "
							+ reply.getParameterPassing().get("userName"));
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
		} finally {
			return reply;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public SignUpReply checkUserName(SignUpReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(SignUp.class);
			criteria.add(Restrictions.eq(MessageConstants.USERNAME, reply
					.getParameterPassing().get(MessageConstants.USERNAME)));
			if (null != criteria.list() && !criteria.list().isEmpty()) {
				reply.getRegisterUserList().addAll(
						(Collection<? extends SignUp>) criteria.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		} finally {
			return reply;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public SignUpReply getallFromSignUp(SignUpReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(SignUp.class);
			if (null != criteria.list() && !criteria.list().isEmpty()) {
				reply.getRegisterUserList().addAll(
						(Collection<? extends SignUp>) criteria.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		} finally {
			return reply;
		}
	}

	@Override
	public SuccessFailureWarnig insertIntoBiddingHibernate(
			BiddingHibernate saveBid) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(saveBid);

			session.getTransaction().commit();
			notification.setStatus(MessageConstants.SUCCESS);
			notification.setMessage(MessageConstants.BIDDINGSAVED
					+ saveBid.getBidReferenceNumber());
			session.close();
		} catch (Exception e) {
			notification.setStatus(MessageConstants.FAILURE);
			notification.setMessage(e.getMessage());
		}
		return notification;
	}

	@Override
	public BiddingReply checkBidReference(BiddingReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(BiddingHibernate.class);
			criteria.add(Restrictions.eq(
					MessageConstants.BIDREFERENCENUMBER,
					Integer.parseInt(reply.getParameterPassing().get(
							MessageConstants.BIDREFERENCENUMBER))));
			if (null != criteria.list() && !criteria.list().isEmpty()) {
				reply.getHibernateObject().addAll(criteria.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}

	@Override
	public BiddingReply getFromBiddingtable(BiddingReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			if (null != session.createCriteria(BiddingHibernate.class).list()
					&& !session.createCriteria(BiddingHibernate.class).list()
							.isEmpty()) {
				reply.getHibernateObject().addAll(
						session.createCriteria(BiddingHibernate.class).list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}

	@Override
	public SuccessFailureWarnig insertIntoBiddingKeeperHibernate(
			BiddingHistoryHibernate saveBiddingHistory) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(saveBiddingHistory);

			session.getTransaction().commit();
			notification.setStatus(MessageConstants.SUCCESS);
			notification.setMessage(MessageConstants.BIDDINGSAVED
					+ saveBiddingHistory.getBidRefernceNumber());
			session.close();

		} catch (Exception e) {
			notification.setStatus(MessageConstants.FAILURE);
			notification.setMessage(e.getMessage());
		}
		
		return notification;
	}

	@Override
	public BiddingKeeperReply getFromBiddingHistorytable(
			BiddingKeeperReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria cr = session.createCriteria(BiddingHistoryHibernate.class);
			if (null != cr.list() && !cr.list().isEmpty()) {
				reply.getBiddingHistoryHibernate().addAll(cr.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}

	public BiddingKeeperReply getMaxFromBiddingHistorytable(
			BiddingKeeperReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Query query = session
					.createSQLQuery(
							"SELECT * FROM BIDDINGHISTORYHIBERNATE where bidreferncenumber = :bidReferenceNumber and price = (SELECT MAX(price) FROM BIDDINGHISTORYHIBERNATE where bidreferncenumber = :bidReferenceNumber)")
					.addEntity(BiddingHistoryHibernate.class);
			query.setInteger(
					"bidReferenceNumber",
					Integer.parseInt(reply.getParameterPassing().get(
							MessageConstants.BIDREFERENCENUMBERHISTORY)));
			query.list();
			if (null != query.list() && !query.list().isEmpty()) {
				reply.getBiddingHistoryHibernate().addAll(query.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}

	@SuppressWarnings("finally")
	public SignUpReply updateSignUP(SignUpReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			SignUp update = (SignUp) session.get(SignUp.class, reply
					.getParameterPassing().get(MessageConstants.USERNAME));
			update.setDesgination(reply.getParameterPassing().get(
					MessageConstants.Designation));
			update.setOfficeAddress(reply.getParameterPassing().get(
					MessageConstants.OFFICEADDRESS));
			update.setServiceTax(MessageConstants.SERVICETAX);
			session.update(update);
			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			notification
					.setMessage("Data Retrived successfully from Sign Up table for User : "
							+ reply.getParameterPassing().get("userName"));
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				notification.setStatus("Falure");
			}
			return reply;
		}
	}

	@Override
	public BiddingAppravalSystemReply getSelectAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(BiddingApprvalSystem.class);

			if (null != criteria.list() && !criteria.list().isEmpty()) {
				reply.getBiddingAppravalSystem().addAll(criteria.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}

	@Override
	public BiddingAppravalSystemReply getAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
					.createCriteria(BiddingApprvalSystem.class);
			criteria.add(Restrictions.eq(MessageConstants.USERNAME, reply
					.getParameterPassing().get(MessageConstants.USERNAME)));
			criteria.add(Restrictions.eq(
					MessageConstants.BIDREFERENCENUMBER,
					Integer.parseInt(reply.getParameterPassing().get(
							MessageConstants.BIDREFERENCENUMBER))));
			if (null != criteria.list() && !criteria.list().isEmpty()) {
				reply.getHibernateBiddingApproval().addAll(criteria.list());
				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.RECORDFOUND);
				reply.setNotification(notification);
			} else {

				notification.setStatus("SUCCESS");
				notification.setMessage(MessageConstants.NORecordFound);
				reply.setNotification(notification);
			}
			session.close();
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return reply;
	}
	

	@Override
	@SuppressWarnings("finally")
	public SuccessFailureWarnig insertIntoBiddingApprovalSystem(
			BiddingApprvalSystem biddingApprvalSystem) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(biddingApprvalSystem);

			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
			notification.setMessage(MessageConstants.SUCCESS);

		} 
		catch (Exception e) {
			notification.setStatus(MessageConstants.FAILURE);
			notification.setMessage(e.getMessage());
			session.close();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				notification.setStatus(MessageConstants.WARNIG);
				notification.setMessage(e.getMessage());
			}
			return notification;
		}
	}

	@Override
	public SuccessFailureWarnig updateBiddingHistory(BiddingUserVerificationReply reply) {
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Query query = session
					.createSQLQuery(
							"UPDATE Bidding_User SET verified =  :verifiedUser ,trust_score = :rating  where user_name = :userName")
					.addEntity(SignUp.class);
			query.setInteger(
					"rating",
					Integer.parseInt(reply.getParameterPassing().get(
							MessageConstants.TRUSTSCORE)));
			query.setString(
					"verifiedUser",
					reply.getParameterPassing().get(
							MessageConstants.VERIFID));
			query.setString(
					"userName",
					reply.getParameterPassing().get(
							MessageConstants.USERNAME));
			query.executeUpdate();
			notification.setStatus(MessageConstants.SUCCESS);
			notification.setMessage(MessageConstants.UPDATERECORD);
			session.close();
			
		} catch (Exception e) {
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			reply.setNotification(notification);
		}
		return notification;
	}

	
}
