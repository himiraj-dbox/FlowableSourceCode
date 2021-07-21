package invoiceGeneratorDaoImpl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import hibernate.HibernateUtil;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorEntitites.BillInvoiceEntity;
import invoiceGeneratorEntitites.BillInvoiceID;
import invoiceGeneratorEntitites.BillOverviewEntity;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import invoiceGeneratorEntitites.ShopRegistrationID;
import notification.InvoiceNotification;

public class InvoiceDaoImpl implements InvoiceDao{
	final static Logger logger = Logger.getLogger(InvoiceDaoImpl.class);
/*
*******************************************************************************************************

GENERIC METHODS


*******************************************************************************************************

*/
	@Override
	public <T> InvoiceNotification save(InvoiceNotification notification,Object o) {
		logger.debug("Inside Save()");
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			notification.setStatus("SUCCESS");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			notification.setStatus("Failure");
			notification.setMessage(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return notification;
	}
	
	@Override
	public InvoiceNotification update(InvoiceNotification notification, Object o) throws Exception {

		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.update(o);

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
				logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit update");
		return notification;
	
	}
	@Override
	public <T> Object getSpecifRecord(Class<T> t ,Object o) throws Exception {

		logger.debug("Inside Save()");
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			 		 o = session.get(t, (Serializable) o); 
			 		session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return o;
	}
	
	@Override
	public <T> List<T> getAllRecordFromTable(Class<T> t) throws Exception {

		logger.debug("Inside Save()");
		Session session = null;
		 List<T>  records  = null;
		try {
		
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			 Criteria criteria = session.createCriteria(t);
			 records = criteria.list();
			 session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return records;
	}
	
	/*
	*******************************************************************************************************

	CUSTOMERSHOP REGISTRATION


	*******************************************************************************************************

	*/

	@Override
	public Long getMaxCustomerShpId(ShopRegistrationID shopRegistrationID) throws Exception {
		logger.debug("Inside Save()");
		Session session = null;
		Long maxId = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(CustomerShopRegistrationEntity.class);
			criteria.add(Restrictions.eq("shopRegistrationID.ownerShopId", shopRegistrationID.getOwnerShopId()));
			criteria.setProjection(Projections.max("shopRegistrationID.customerShopId"));
				 maxId = (Long)criteria.uniqueResult();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return maxId;

	}
	
	@Override
	public Long getMaxInvoiceNumber(long ownerId) throws Exception {
		logger.debug("Inside Save()");
		Session session = null;
		Long maxId = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(BillInvoiceEntity.class);
			criteria.add(Restrictions.eq("ownerShopId", ownerId));
			criteria.setProjection(Projections.max("billInvoiceNumber"));
				 maxId = (Long)criteria.uniqueResult();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return maxId;

	}
	
	
	@Override
	public List<CustomerShopRegistrationEntity> getAllOwnerCust(List<CustomerShopRegistrationEntity> customerShopRegistrationList,Long ownerId) throws Exception {
		logger.debug("Inside Save()");
		Session session = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(CustomerShopRegistrationEntity.class);
			criteria.add(Restrictions.eq("shopRegistrationID.ownerShopId", ownerId));
			customerShopRegistrationList = criteria.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return customerShopRegistrationList;

	}

	
	
	/*
	*******************************************************************************************************

	BillOverviewEntity


	*******************************************************************************************************

	*/
	
	@Override
	public Long getMaxInvoiceId(BillInvoiceID billInvoiceID) throws Exception {
		logger.debug("Inside Save()");
		Session session = null;
		Long maxId = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(BillOverviewEntity.class);
			criteria.add(Restrictions.eq("billInvoiceID.ownerShopId", billInvoiceID.getOwnerShopId()));
			criteria.add(Restrictions.eq("billInvoiceID.custShopId", billInvoiceID.getCustShopId()));
			criteria.setProjection(Projections.max("billInvoiceID.billInvoiceNumber"));
				 maxId = (Long)criteria.uniqueResult();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return maxId;

	}

	/*@Override
	public List<OwnerRegistrationEntity> getAllOwnerRegistration() throws Exception {

		logger.debug("Inside Save()");
		Session session = null;
		 List<OwnerRegistrationEntity> ownerRegistrationList = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(OwnerRegistrationEntity.class);
			ownerRegistrationList = criteria.list();
			if(null == ownerRegistrationList || ownerRegistrationList.isEmpty()){
				logger.debug("No Data present in owner registration table");
				throw new Exception();
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return ownerRegistrationList;
	}*/

	/*Criteria criteria = getSession().createCriteria(clazz); 
	Criterion rest1= Restrictions.and(Restrictions.eq(A, "X"), 
	           Restrictions.in("B", Arrays.asList("X",Y)));
	Criterion rest2= Restrictions.and(Restrictions.eq(A, "Y"), 
	           Restrictions.eq(B, "Z"));
	criteria.add(Restrictions.or(rest1, rest2));*/
	
	/*
	*******************************************************************************************************

	OwnerRegistrationEntity


	*******************************************************************************************************

	*/
	
	
	@Override
	public List<OwnerRegistrationEntity> getExistingOwnerDetails(OwnerRegistrationEntity _ownerRegistrationEntity) throws Exception {

		logger.debug("Inside Save()");
		Session session = null;
		List<OwnerRegistrationEntity> ownerRegistrationList = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(OwnerRegistrationEntity.class);
			
			Criterion  c1 =Restrictions.eq("userName", _ownerRegistrationEntity.getUserName());
			Criterion  c2 =Restrictions.eq("emailId", _ownerRegistrationEntity.getEmailId());
			Criterion  c3 =Restrictions.eq("mobile", _ownerRegistrationEntity.getMobile());
			criteria.add(Restrictions.or(c1, c2,c3));
			ownerRegistrationList =  criteria.list();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		} finally {
			try {
				session.close();
			} catch (final HibernateException e) {
				 logger.debug("Session was already closed");
			}
			
		}
		logger.debug("Exit Save()");
		return ownerRegistrationList;

	
	}



}
