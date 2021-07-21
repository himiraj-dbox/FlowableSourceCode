package daoimpl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import dao.DBErpIntegratorDao;
import hibernate.HibernateUtil;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.DBERPINTEGRATER;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;

public class DBErpIntegratorDaoImpl implements DBErpIntegratorDao{
	final static Logger logger = Logger.getLogger(DBErpIntegratorDaoImpl.class);
	@Override
	public <T> Object getSpecifRecord(Class<T> t, Object o) throws Exception {


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
	public List<DBERPINTEGRATER> getExistingOwnerDetails(DBERPINTEGRATER dBERPINTEGRATERObject) throws Exception {
		logger.debug("Inside getExistingOwnerDetails()");
		Session session = null;
		List<DBERPINTEGRATER> dBERPINTEGRATER = null ;
		try {

			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session
				    .createCriteria(DBERPINTEGRATER.class);
			
			Criterion  c1 =Restrictions.eq("poNumber", dBERPINTEGRATERObject.getPoNumber());
			Criterion  c2 =Restrictions.eq("vendorname", dBERPINTEGRATERObject.getVendorname());
			
			criteria.add(Restrictions.and(c1, c2));
			dBERPINTEGRATER =  criteria.list();
			
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
		if(dBERPINTEGRATER.isEmpty()) {
			return null;
		}
		return dBERPINTEGRATER;

	}

}
