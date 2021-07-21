package hibernate;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import hibernateEntryProgram.HibernateMainProgrm;
import hibernateSignUp.HireMeTileTilesCatalog;
import hibernateSignUp.TileUserRegistration;
import hibernateSignUp.TileUserTaskManager;
import invoiceGeneratorEntitites.BillInvoiceEntity;
import invoiceGeneratorEntitites.BillOverviewEntity;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;
import invoiceGeneratorEntitites.DBDuplicateCheck;
import invoiceGeneratorEntitites.DBERPINTEGRATER;
import invoiceGeneratorEntitites.InvoiceBillingHistory;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import invoiceGeneratorEntitites.SurchargeInvoiceEntity;


public class HibernateUtil implements Serializable {
	/**
	 * Modified By Gaurav Lalwani
	 * 8888603853
	 * gauravlalwani90@gmail.com
	 * The purpose of this class is to generate Singleton Object of Session Factory.
	 */
	final static Logger logger = Logger.getLogger(HibernateMainProgrm.class);
	private static final long serialVersionUID = 1L;
	private volatile static SessionFactory sessionFactory;
	private HibernateUtil(){
		
		if(sessionFactory == null){
			try{	
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		
      /*  configuration.addAnnotatedClass(SignUp.class);
        configuration.addAnnotatedClass(BiddingHistoryHibernate.class);
        configuration.addAnnotatedClass(BiddingHibernate.class);
        configuration.addAnnotatedClass(BiddingApprvalSystem.class);*/
		/*configuration.addAnnotatedClass(HireMeTileTilesCatalog.class);
		configuration.addAnnotatedClass(TileUserRegistration.class);
        configuration.addAnnotatedClass(TileUserTaskManager.class);*/
        
     /*   configuration.addAnnotatedClass(TileUserTaskManager.class);
        configuration.addAnnotatedClass(TileUserTaskStatus.class);*/
		//bill Invoice change started

		configuration.addAnnotatedClass(DBERPINTEGRATER.class);
		configuration.addAnnotatedClass(DBDuplicateCheck.class);
        configuration.configure();
        new SchemaExport(configuration).drop(false,false);
        ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
        registry.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
		}
		catch(Exception e){
			logger.error("Error Occured while Saving : "+ e.getMessage());
			
		}
		}
		else{
			throw new RuntimeException("You can not create new  instance of Sessoin Factory");
		}
	
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	private Object readResolve() throws ObjectStreamException{
		if(logger.isDebugEnabled()){
		    logger.debug("Overriding Default Serialization Method");
		}
		return sessionFactory;
	}
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null){
			synchronized (HibernateUtil.class) {
				if(sessionFactory == null){
				new HibernateUtil();
				}
			}
			
		}
        return sessionFactory;
    }
}
