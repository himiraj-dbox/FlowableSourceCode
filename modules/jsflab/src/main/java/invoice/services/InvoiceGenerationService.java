package invoice.services;

import java.util.ArrayList;
import java.util.List;

import invoice.xsd.BillInvoice;
import invoice.xsd.BillingItemXsd;
import invoice.xsd.SurchargexXsd;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.BillInvoiceEntity;
import invoiceGeneratorEntitites.BillInvoiceID;
import invoiceGeneratorEntitites.BillOverviewEntity;
import invoiceGeneratorEntitites.SurchargeInvoiceEntity;
import notification.InvoiceNotification;

public class InvoiceGenerationService {
	private InvoiceDao 	invoiceDao = new 	InvoiceDaoImpl();
	
	public BillInvoice createBill(BillInvoice billInvoice) throws Exception {
		BillOverviewEntity billOverviewEntity = convertToBillOverViewEntity(billInvoice);
		List<BillInvoiceEntity> itemList = converToBillInvoiceEntityList(billInvoice);
		List<SurchargeInvoiceEntity> surchageList = convertToSurchageEntity(billInvoice);
		createBill(billOverviewEntity,itemList,surchageList);
		//BillInvoiceEntity entity = convertToBillInvoice(billInvoice);
		return billInvoice;
	}

	private void createBill(BillOverviewEntity billOverviewEntity, List<BillInvoiceEntity> itemList,
			List<SurchargeInvoiceEntity> surchageList) {
		invoiceDao.save(InvoiceNotification.getInstance(), billOverviewEntity);
		for(BillInvoiceEntity billInvoiceEntity :itemList){
			invoiceDao.save(InvoiceNotification.getInstance(), billInvoiceEntity);
		}
		for(SurchargeInvoiceEntity surchageEntity : surchageList){
			invoiceDao.save(InvoiceNotification.getInstance(), surchageEntity);
		}
		
	}

	private List<SurchargeInvoiceEntity> convertToSurchageEntity(BillInvoice billInvoice) {
		List<SurchargeInvoiceEntity> surchageList = new ArrayList<SurchargeInvoiceEntity>();
		
		for(SurchargexXsd surchargeXsd : billInvoice.getSurcharges()){
			SurchargeInvoiceEntity surchargeInvoiceEntity = convertToSurchargeEntityList(surchargeXsd);
			surchargeInvoiceEntity.setBillInvoiceNumber(billInvoice.getBillInvoiceID().getBillInvoiceNumber());
			surchargeInvoiceEntity.setCustShopId(billInvoice.getBillInvoiceID().getCustShopId());
			surchargeInvoiceEntity.setOwnerShopId(billInvoice.getBillInvoiceID().getOwnerShopId());
			surchageList.add(surchargeInvoiceEntity);
		}
		return surchageList;
	}

	private SurchargeInvoiceEntity convertToSurchargeEntityList(SurchargexXsd surchargeXsd) {
		SurchargeInvoiceEntity surchargeInvoiceEntity = new SurchargeInvoiceEntity();
		surchargeInvoiceEntity.setCost(surchargeXsd.getCost());
		surchargeInvoiceEntity.setIndustryIndicator(surchargeXsd.getIndustryType());
		surchargeInvoiceEntity.setName(surchargeXsd.getName());
		return surchargeInvoiceEntity;
	}

	private List<BillInvoiceEntity> converToBillInvoiceEntityList(BillInvoice billInvoice) throws Exception{
		List<BillInvoiceEntity> items = new ArrayList<BillInvoiceEntity>();
		for(BillingItemXsd itemXsd : billInvoice.getBillingItems()){
			BillInvoiceEntity billInvoiceEntity = convertToBillInvoiceEntity(itemXsd);
			billInvoiceEntity.setBillInvoiceNumber(billInvoice.getBillInvoiceID().getBillInvoiceNumber());
			billInvoiceEntity.setCustShopId(billInvoice.getBillInvoiceID().getCustShopId());
			billInvoiceEntity.setOwnerShopId(billInvoice.getBillInvoiceID().getOwnerShopId());
			items.add(billInvoiceEntity);
		}
		return items;
	}

	private BillInvoiceEntity convertToBillInvoiceEntity(BillingItemXsd itemXsd) {
		BillInvoiceEntity billInvoiceEntity = new BillInvoiceEntity();
		
		billInvoiceEntity.setCost(itemXsd.getCost());
		billInvoiceEntity.setItemName(itemXsd.getItemName());
		billInvoiceEntity.setQuantity(itemXsd.getQuantity());
		billInvoiceEntity.setTax(itemXsd.getTax());
		billInvoiceEntity.setType(itemXsd.getType());
		return billInvoiceEntity;
	}

	private BillOverviewEntity convertToBillOverViewEntity(BillInvoice billInvoice) throws Exception{
		BillOverviewEntity billOverviewEntity = new BillOverviewEntity();
		BillInvoiceID billInvoiceID = new BillInvoiceID();
		billInvoiceID.setBillInvoiceNumber(billInvoice.getBillInvoiceID().getBillInvoiceNumber());
		billInvoiceID.setCustShopId(billInvoice.getBillInvoiceID().getCustShopId());
		billInvoiceID.setOwnerShopId( billInvoice.getBillInvoiceID().getOwnerShopId());
		billOverviewEntity.setAmountDue(billInvoice.getAmountDue());
		billOverviewEntity.setBillInvoiceID(billInvoiceID);
		billOverviewEntity.setBillType(billInvoice.getBillType());
		billOverviewEntity.setFinalCost(billInvoice.getFinalCost());
		billOverviewEntity.setSurcharge(billInvoice.getTotalsurcharge());
		billOverviewEntity.setTotalCost(billInvoice.getTotalcost());
		billOverviewEntity.setTotalQuantity(billInvoice.getTotalquantity());
		billOverviewEntity.setTotalOverallCost(billInvoice.getTotalOverallCost());
		billOverviewEntity.setTotalTax(billInvoice.getTotaltax());
		return billOverviewEntity;
	}

	public BillInvoice getMaxBillInvoiceNumner(Long ownerShopId)  throws Exception{
		BillInvoice billInvoice = new BillInvoice();
		invoice.xsd.BillInvoiceID billInvoiceID = new invoice.xsd.BillInvoiceID();
		Long maxInvoiceNumber = invoiceDao.getMaxInvoiceNumber(ownerShopId);
		if(null == maxInvoiceNumber || 0l == maxInvoiceNumber){
			maxInvoiceNumber = 1l;
		}
		billInvoiceID.setBillInvoiceNumber(maxInvoiceNumber);
		billInvoice.setBillInvoiceID(billInvoiceID);
		return billInvoice;
	}

	/*private BillInvoiceEntity convertToBillInvoice(BillInvoice billInvoice) {
		BillInvoiceEntity billInvoiceEntity = new BillInvoiceEntity();
		billInvoiceEntity.setAmountDue(billInvoice.getAmountDue());
		billInvoiceEntity.setBillDate(new Date());
		billInvoiceEntity.setBillInvoiceID(billInvoice.getBillInvoiceID().getBillInvoiceNumber());;
		billInvoiceEntity.setBillType(billInvoice.getBillType());
		billInvoiceEntity.setCost(cost);
		billInvoiceEntity.setItemName(itemName);
		billInvoiceEntity.setQuantity(quantity);
		billInvoiceEntity.
		return billInvoiceEntity;
	}*/

}
