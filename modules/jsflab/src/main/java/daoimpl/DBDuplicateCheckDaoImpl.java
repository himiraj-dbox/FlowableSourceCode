package daoimpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

import dao.DBDuplicateCheckDao;
import hibernate.HibernateUtil;
import invoiceGeneratorEntitites.DBDuplicateCheck;
import invoiceGeneratorEntitites.DBERPINTEGRATER;
import notification.InvoiceNotification;
import notification.NotificationObject;

public class DBDuplicateCheckDaoImpl implements DBDuplicateCheckDao {
	final static Logger logger = Logger.getLogger(DBDuplicateCheckDaoImpl.class);
	@Override
	public DBDuplicateCheck getRecordForProcessInstance(DBDuplicateCheck dBDuplicateCheckRequest) throws Exception {
		logger.debug("Inside getExistingOwnerDetails()");
		Session session = null;
		List<DBDuplicateCheck> dBDuplicateCheck = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(DBDuplicateCheck.class);
			
			Criterion  c1 =Restrictions.eq("processInstanceId",dBDuplicateCheckRequest.getProcessInstanceId());
			criteria.add(c1);
			dBDuplicateCheck =  criteria.list();
			
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
		if(dBDuplicateCheck.isEmpty()) {
			return null;
		}
		return dBDuplicateCheck.get(0);
	}
	@Override
	public <T>  NotificationObject save(NotificationObject notification, Object o) {

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
	public DBDuplicateCheck checkDuplicateValues(DBDuplicateCheck dbObject) {

		logger.debug("Inside getExistingOwnerDetails()");
		Session session = null;
		List<DBDuplicateCheck> dBDuplicateCheck = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(DBDuplicateCheck.class);
			
			Criterion  c1 =Restrictions.eq("vendorname",dbObject.getVendorname());
			Criterion  c2 =Restrictions.eq("invoiceAmount",dbObject.getInvoiceAmount());
			Criterion  c3 =Restrictions.eq("poNumber",dbObject.getPoNumber());
			Criterion  c4 =Restrictions.eq("invoiceNumber",dbObject.getInvoiceNumber());
			criteria.add(Restrictions.and(c1, c2,c3,c4));
			dBDuplicateCheck =  criteria.list();
			
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
		if(dBDuplicateCheck.isEmpty()) {
			return null;
		}
		return dBDuplicateCheck.get(0);
	
	}
	
	
	@Override
	public String getProcessinstanceID(String  taskId) {
		String sql = "select PROC_INST_ID_ as instanceid from ACT_HI_TASKINST where id_ = ";
		sql = sql + "'" +taskId+ "' "   ;
		logger.debug("Inside getExistingOwnerDetails()");
		Session session = null;
		String processInstanceId  = null;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql).addScalar("instanceid", new StringType());
			
			List<String> rows  =  query.list();
			for(String row : rows){
				
				processInstanceId =((row));
			
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
		
		return processInstanceId;
	
	}

}
