package invoiceGeneratorDao;

import java.util.List;

import invoiceGeneratorEntitites.BillInvoiceID;
import invoiceGeneratorEntitites.BillOverviewEntity;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import invoiceGeneratorEntitites.ShopRegistrationID;
import notification.InvoiceNotification;
import notification.SuccessFailureWarnig;

public interface InvoiceDao {
	//Generic
	public <T> InvoiceNotification save(InvoiceNotification notification,Object o) ;
	public<T> Object getSpecifRecord(Class<T> t, Object _o) throws Exception;
	//BillInvoiceEntity
	public Long getMaxInvoiceId(BillInvoiceID billInvoiceID) throws Exception;
	
	//BillOverviewEntity
	
	//CustomerShopRegistrationEntity
	public Long getMaxCustomerShpId(ShopRegistrationID customerShopRegistrationEntity) throws Exception;

    //OwnerRegistrationEntity
	//public List<OwnerRegistrationEntity> getAllOwnerRegistration() throws Exception;
	public List<OwnerRegistrationEntity> getExistingOwnerDetails(OwnerRegistrationEntity billOverviewEntity) throws  Exception;
	public InvoiceNotification update(InvoiceNotification notification, Object o) throws Exception;
	public<T> List<T> getAllRecordFromTable(Class<T> t) throws Exception;
	public List<CustomerShopRegistrationEntity> getAllOwnerCust(
			List<CustomerShopRegistrationEntity> customerShopRegistrationList, Long ownerId) throws Exception;
	public Long getMaxInvoiceNumber(long ownerId) throws Exception;
	
}
 