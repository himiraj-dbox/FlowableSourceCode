package invoicegenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import constantBidding.MessageConstants;
import invoice.xsd.OwnerRegistration;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.BillInvoiceEntity;
import invoiceGeneratorEntitites.BillInvoiceID;
import invoiceGeneratorEntitites.BillOverviewEntity;
import invoiceGeneratorEntitites.CustomerShopRegistrationEntity;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import invoiceGeneratorEntitites.ShopRegistrationID;
import junit.framework.Assert;
import notification.InvoiceNotification;

public class InvoiceGeneratorSaveTest {
	private InvoiceDao invoiceDao = new InvoiceDaoImpl();


	@Test
	public void saveInvoiceEntity(){
		BillInvoiceID billInvoiceID = new BillInvoiceID();
		BillInvoiceEntity billInvoiceEntity = new BillInvoiceEntity();
		billInvoiceID.setBillInvoiceNumber(1l);
		billInvoiceID.setOwnerShopId(1l);
		billInvoiceID.setCustShopId(1l);
		billInvoiceEntity.setBillInvoiceNumber(1l);
	//	billInvoiceEntity.setAmountDue(1000.0);
		//billInvoiceEntity.setBillDate(new Date());
	//	billInvoiceEntity.setBillType("T");
		billInvoiceEntity.setCost(2000.0);
		billInvoiceEntity.setFinalCost(5000);
		billInvoiceEntity.setItemName("Test");
		billInvoiceEntity.setQuantity(5);
	//	billInvoiceEntity.setSurcharge(500.0);
		billInvoiceEntity.setTax(20.0);
		billInvoiceEntity.setType("TOP");
		InvoiceNotification notification = InvoiceNotification.getInstance();
		notification =invoiceDao.save(notification, billInvoiceEntity);
		InvoiceNotification.release(notification);
		Assert.assertEquals(MessageConstants.SUCCESS, notification.getStatus());
	}
	
	@Test
	public void saveBillOverviewEntity(){
		BillInvoiceID billInvoiceID = new BillInvoiceID();
		BillOverviewEntity billInvoiceEntity = new BillOverviewEntity();
		billInvoiceID.setBillInvoiceNumber(1l);
		billInvoiceID.setCustShopId(1l);
		billInvoiceID.setOwnerShopId(1l);
		billInvoiceEntity.setBillInvoiceID(billInvoiceID);
		billInvoiceEntity.setAmountDue(1000.0);
		billInvoiceEntity.setBillType("Test");
		billInvoiceEntity.setFinalCost(5000);
		billInvoiceEntity.setSurcharge(500.00);
		billInvoiceEntity.setTotalCost(500.00);
		billInvoiceEntity.setTotalQuantity(10);
		billInvoiceEntity.setTotalTax(52.00);
		InvoiceNotification notification = InvoiceNotification.getInstance();
		notification =invoiceDao.save(notification, billInvoiceEntity);
		InvoiceNotification.release(notification);
		Assert.assertEquals(MessageConstants.SUCCESS, notification.getStatus());
	}
	
	@Test
	public void saveOwnerRegistrationEntity(){
		OwnerRegistrationEntity billInvoiceEntity = new OwnerRegistrationEntity();
		billInvoiceEntity.setAddress("Address");;
		billInvoiceEntity.setCity("City");
		billInvoiceEntity.setEmailId("Email2");
		billInvoiceEntity.setEndDate(new Date());
		billInvoiceEntity.setStartDate(new Date());
		billInvoiceEntity.setMobile("8888603855");
		billInvoiceEntity.setName("Name");
		billInvoiceEntity.setPassword("123");
		billInvoiceEntity.setTotalAmountPaid(155);
		billInvoiceEntity.setUserName("122");
		billInvoiceEntity.setVerified("N");
		billInvoiceEntity.setOwnerShopId(4l);
		billInvoiceEntity.setTinNumner(1l);
		InvoiceNotification notification = InvoiceNotification.getInstance();
		notification =invoiceDao.save(notification, billInvoiceEntity);
		InvoiceNotification.release(notification);
		Assert.assertEquals(MessageConstants.SUCCESS, notification.getStatus());
	}
	
	@Test
	public void saveCustomerRegistrationEntity(){
		InvoiceNotification notification = InvoiceNotification.getInstance();
		CustomerShopRegistrationEntity billInvoiceEntity = new CustomerShopRegistrationEntity();
		for(long i =10l ; i<20l;i++){
		ShopRegistrationID shopRegistrationID = new ShopRegistrationID();
		shopRegistrationID.setCustomerShopId(i);
		shopRegistrationID.setOwnerShopId(2l);
		billInvoiceEntity.setShopRegistrationID(shopRegistrationID);
		billInvoiceEntity.setAddress("Test");
		billInvoiceEntity.setCity("City");
		billInvoiceEntity.setCustomerEmailId("test");
		billInvoiceEntity.setDescription("Desc");
		billInvoiceEntity.setLocation("Location");
		billInvoiceEntity.setMobileNumber("Mobile");
		billInvoiceEntity.setName("Name");
		billInvoiceEntity.setOwnerShopName("OwnerName");
		billInvoiceEntity.setTags("asas");
		
		notification =invoiceDao.save(notification, billInvoiceEntity);
		}
		InvoiceNotification.release(notification);
		Assert.assertEquals(MessageConstants.SUCCESS, notification.getStatus());
	}
	
	@Test
	public void getMaxCustomerShoId() throws Exception{
		Long maxId = null;
		ShopRegistrationID shopRegistrationID = new ShopRegistrationID();
		shopRegistrationID.setCustomerShopId(1l);
		shopRegistrationID.setOwnerShopId(2l);
		 maxId =invoiceDao.getMaxCustomerShpId(shopRegistrationID);
		Assert.assertNotNull(maxId);
	}
	@Test
	public void getMaxInvoicenumber() throws Exception{
		Long maxId = null;
		BillInvoiceID billInvoiceID = new BillInvoiceID();
		billInvoiceID.setCustShopId(1l);
		billInvoiceID.setOwnerShopId(1l);
		 maxId =invoiceDao.getMaxInvoiceId(billInvoiceID);
		Assert.assertNotNull(maxId);
	}
	
	@Test
	public void getAllRecordsFromOwnertable() throws Exception{
		 List<OwnerRegistrationEntity> record =invoiceDao.getAllRecordFromTable(OwnerRegistrationEntity.class);
		Assert.assertNotNull(record);
	}
	
	@Test
	public void getOwnerRegistrationDetails() throws Exception{
		List<OwnerRegistrationEntity> billInvoiceList = new ArrayList<OwnerRegistrationEntity>();
		OwnerRegistrationEntity billInvoiceEntity = new OwnerRegistrationEntity();
		 billInvoiceEntity.setAddress("Address");;
		billInvoiceEntity.setCity("City");
		billInvoiceEntity.setEmailId("Email1");
		billInvoiceEntity.setEndDate(new Date());
		billInvoiceEntity.setStartDate(new Date());
		billInvoiceEntity.setMobile("8888603853");
		billInvoiceEntity.setName("Name");
		billInvoiceEntity.setPassword("123");
		billInvoiceEntity.setTotalAmountPaid(155);
		//billInvoiceEntity.setUserName("1268");
		billInvoiceEntity.setVerified("N");
		InvoiceNotification notification = InvoiceNotification.getInstance();
		billInvoiceList =invoiceDao.getExistingOwnerDetails(billInvoiceEntity);
		InvoiceNotification.release(notification);
		Assert.assertNotNull(billInvoiceEntity);
	}
	
	@Test
	public void updateOwnerRegistrationDetails() throws Exception{
		List<OwnerRegistrationEntity> billInvoiceList = new ArrayList<OwnerRegistrationEntity>();
		OwnerRegistrationEntity billInvoiceEntity = new OwnerRegistrationEntity();
		billInvoiceEntity.setOwnerShopId(1l);
		billInvoiceEntity.setAddress("Modified");;
		billInvoiceEntity.setCity("City");
		billInvoiceEntity.setEmailId("Email");
		billInvoiceEntity.setEndDate(new Date());
		billInvoiceEntity.setStartDate(new Date());
		billInvoiceEntity.setMobile("8888603853");
		billInvoiceEntity.setName("Name");
		billInvoiceEntity.setPassword("123");
		billInvoiceEntity.setTotalAmountPaid(155);
		billInvoiceEntity.setUserName("1268");
		billInvoiceEntity.setVerified("N");
		InvoiceNotification notification = InvoiceNotification.getInstance();
		notification =invoiceDao.update(notification, billInvoiceEntity);
		InvoiceNotification.release(notification);
		Assert.assertNotNull(billInvoiceEntity);
	}
	
	@Test
	public void getSpecificRecord() throws Exception{
		List<OwnerRegistrationEntity> billInvoiceList = new ArrayList<OwnerRegistrationEntity>();
		OwnerRegistrationEntity billInvoiceEntity = (OwnerRegistrationEntity) invoiceDao.getSpecifRecord(OwnerRegistrationEntity.class,1l);
		Assert.assertNotNull(billInvoiceEntity);
	}
	
	
}
