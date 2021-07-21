package invoice.services;

import java.util.ArrayList;
import java.util.List;

import invoice.xsd.CustomerShopRegistration;
import invoice.xsd.OwnerRegistration;
import invoice.xsd.ShopRegistration;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import invoiceGeneratorEntitites.ShopRegistrationID;
import notification.InvoiceNotification;

public class CustomerRegistrationService {
	private InvoiceDao 	invoiceDao = new 	InvoiceDaoImpl();
	public List<CustomerShopRegistration> getAllCustomer(List<CustomerShopRegistration> customerRecords) throws Exception {
		customerRecords = new ArrayList<CustomerShopRegistration>();
		List<CustomerShopRegistrationEntity> customerdataRecord = invoiceDao.getAllRecordFromTable(CustomerShopRegistrationEntity.class);
		  if(null != customerdataRecord && !customerdataRecord.isEmpty()){
			  for(CustomerShopRegistrationEntity ownerRegistrationEntityObject : customerdataRecord){
				  CustomerShopRegistration customerShopRegistrationObject = entityToXsdCustomer(ownerRegistrationEntityObject);
				  customerRecords.add(customerShopRegistrationObject);
			   }
		  }
		return customerRecords;
	}
	
	
	public List<CustomerShopRegistration> getAllOwnerCustomerList(OwnerRegistration ownerRegistration) throws Exception {
		 List<CustomerShopRegistration> customerShopRegistrationList = null;
		if(null != ownerRegistration && null != ownerRegistration.getOwnerShopId() ){
			List<CustomerShopRegistrationEntity> customerdataRecord  = new ArrayList<CustomerShopRegistrationEntity>();
			customerdataRecord = (List<CustomerShopRegistrationEntity>) invoiceDao.getAllOwnerCust(customerdataRecord, ownerRegistration.getOwnerShopId());
			    customerShopRegistrationList = new ArrayList<CustomerShopRegistration>();
		    for(CustomerShopRegistrationEntity custRecords : customerdataRecord){
		    	CustomerShopRegistration object = new CustomerShopRegistration();
		    	object = entityToXsdCustomer(custRecords);
		    	customerShopRegistrationList.add(object);
		    }
		}
		return customerShopRegistrationList;
	}
	
	private CustomerShopRegistration entityToXsdCustomer(CustomerShopRegistrationEntity ownerRegistrationEntityObject) {
		CustomerShopRegistration customerShopRegistration = new CustomerShopRegistration();
		ShopRegistration shopRegistration = new ShopRegistration();
		shopRegistration.setCustomerShopId(ownerRegistrationEntityObject.getShopRegistrationID().getCustomerShopId());
		shopRegistration.setOwnerShopId(ownerRegistrationEntityObject.getShopRegistrationID().getOwnerShopId());
		customerShopRegistration.setShopRegistrationID(shopRegistration);
		customerShopRegistration.setAddress(ownerRegistrationEntityObject.getAddress());
		customerShopRegistration.setCity(ownerRegistrationEntityObject.getCity());
		customerShopRegistration.setCustomerEmailId(ownerRegistrationEntityObject.getCustomerEmailId());
		customerShopRegistration.setDescription(ownerRegistrationEntityObject.getDescription());
		customerShopRegistration.setLocation(ownerRegistrationEntityObject.getLocation());
		customerShopRegistration.setMobileNumber(ownerRegistrationEntityObject.getMobileNumber());
		customerShopRegistration.setName(ownerRegistrationEntityObject.getName());
		customerShopRegistration.setOwnerShopName(ownerRegistrationEntityObject.getOwnerShopName());
		customerShopRegistration.setTags(ownerRegistrationEntityObject.getTags());
		return customerShopRegistration;
	}


	public void saveCustomerRecord(CustomerShopRegistration customerShopRegistration) throws Exception {
		CustomerShopRegistrationEntity ownerRegistrationEntityObject = xsdToEntityConverter(customerShopRegistration);
		ownerRegistrationEntityObject.getShopRegistrationID().setCustomerShopId(getMaxCustomerID(ownerRegistrationEntityObject.getShopRegistrationID())+1);
		invoiceDao.save(InvoiceNotification.getInstance(), ownerRegistrationEntityObject);
		
	}


	private Long getMaxCustomerID(ShopRegistrationID shopRegistrationID) throws Exception {
		Long maxCustomerForShop =invoiceDao.getMaxCustomerShpId(shopRegistrationID);
		return maxCustomerForShop;
	}


	private CustomerShopRegistrationEntity xsdToEntityConverter(CustomerShopRegistration customerShopRegistration) throws Exception {
		CustomerShopRegistrationEntity ownerRegistrationEntityObject = new CustomerShopRegistrationEntity();
		ShopRegistrationID shopRegistrationID = new ShopRegistrationID();
		ownerRegistrationEntityObject.setAddress(customerShopRegistration.getAddress());
		ownerRegistrationEntityObject.setCity(customerShopRegistration.getCity());
		ownerRegistrationEntityObject.setCustomerEmailId(customerShopRegistration.getCustomerEmailId());
		ownerRegistrationEntityObject.setDescription(customerShopRegistration.getDescription());
		ownerRegistrationEntityObject.setLocation(customerShopRegistration.getLocation());
		ownerRegistrationEntityObject.setMobileNumber(customerShopRegistration.getMobileNumber());
		ownerRegistrationEntityObject.setName(customerShopRegistration.getName());
		ownerRegistrationEntityObject.setOwnerShopName(customerShopRegistration.getOwnerShopName());
		//setting registration shop ID
		shopRegistrationID.setCustomerShopId(customerShopRegistration.getShopRegistrationID().getCustomerShopId());
		shopRegistrationID.setOwnerShopId(customerShopRegistration.getShopRegistrationID().getOwnerShopId());
		ownerRegistrationEntityObject.setShopRegistrationID(shopRegistrationID);
		ownerRegistrationEntityObject.setTags(customerShopRegistration.getTags());
		return ownerRegistrationEntityObject;
	}


	

}
