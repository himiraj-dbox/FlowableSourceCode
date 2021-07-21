package invoice.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constantBidding.MessageConstants;
import invoice.xsd.OwnerRegistration;
import invoiceGeneratorDao.InvoiceDao;
import invoiceGeneratorDaoImpl.InvoiceDaoImpl;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;
import notification.InvoiceNotification;

public class OwnerDetailService {
	private InvoiceDao 	invoiceDao = new 	InvoiceDaoImpl();
	
	
	public List<OwnerRegistration> getAllOwnerRegisList(List<OwnerRegistration> ownerRegistration) throws Exception {
		ownerRegistration = new ArrayList<OwnerRegistration>();
		List<OwnerRegistrationEntity> ownerRegistrationEntity = invoiceDao.getAllRecordFromTable(OwnerRegistrationEntity.class);
	      if(null != ownerRegistrationEntity && !ownerRegistrationEntity.isEmpty()){
	    	  for(OwnerRegistrationEntity ownerRegistrationEntityObject : ownerRegistrationEntity){
	    		  OwnerRegistration ownerRegistrationObject = ownerXsdEntityMapping(ownerRegistrationEntityObject);
	    		  ownerRegistration.add(ownerRegistrationObject);
	    	  }
	      }
	      return ownerRegistration;
	}
	
	public void saveOwner(OwnerRegistration ownerRegistration){
		OwnerRegistrationEntity ownerRegistrationEntity  = null;
		if(null != ownerRegistration ){
			ownerRegistrationEntity =ownerEntityXsdMapping(ownerRegistration);
			invoiceDao.save(InvoiceNotification.getInstance(), ownerRegistrationEntity);
		}
	}
	
	public List<OwnerRegistration> getExitingOwnerInfo(OwnerRegistration ownerRegistration) throws Exception{
		List<OwnerRegistration> ownerRegistrationWebList = new ArrayList<OwnerRegistration>();
		List<OwnerRegistrationEntity> ownerRegistrationList = null;
		 OwnerRegistrationEntity ownerRegistrationEntity =  ownerEntityXsdMapping(ownerRegistration);
		 ownerRegistrationList =invoiceDao.getExistingOwnerDetails(ownerRegistrationEntity);
		  if(null != ownerRegistrationList && !ownerRegistrationList.isEmpty()){
	    	  for(OwnerRegistrationEntity ownerRegistrationEntityObject : ownerRegistrationList){
	    		  OwnerRegistration ownerRegistrationObject = ownerXsdEntityMapping(ownerRegistrationEntityObject);
	    		  ownerRegistrationObject.setPassword("XXXXXXXX");
	    		  ownerRegistrationWebList.add(ownerRegistrationObject);
	    	  }
	      }
	      return ownerRegistrationWebList;
	}
	
	public OwnerRegistrationEntity validateUser(OwnerRegistration ownerRegistration) throws Exception{
		boolean flag = false;
		List<OwnerRegistrationEntity> ownerRegistrationList = null;
		 OwnerRegistrationEntity ownerRegistrationEntity =  ownerEntityXsdMapping(ownerRegistration);
		 ownerRegistrationList =invoiceDao.getExistingOwnerDetails(ownerRegistrationEntity);
		  if(null != ownerRegistrationList && !ownerRegistrationList.isEmpty()){
	    	  for(OwnerRegistrationEntity ownerRegistrationEntityObject : ownerRegistrationList){
	    		  if(ownerRegistrationEntityObject.getPassword().equals(ownerRegistration.getPassword())){
	    			  return ownerRegistrationEntityObject;
	    		  }
	    	  }
	      }
	      return null;
	}
	


	public OwnerRegistration ownerXsdEntityMapping(OwnerRegistrationEntity ownerRegistrationEntityObject) {
		OwnerRegistration  ownerRegistrationObject = new OwnerRegistration();
		  ownerRegistrationObject.setAddress(ownerRegistrationEntityObject.getAddress());
		  ownerRegistrationObject.setCity(ownerRegistrationEntityObject.getCity());
		  ownerRegistrationObject.setEmailId(ownerRegistrationEntityObject.getEmailId());
		  ownerRegistrationObject.setEndDate(ownerRegistrationEntityObject.getEndDate());
		  ownerRegistrationObject.setMobile(ownerRegistrationEntityObject.getMobile());
		  ownerRegistrationObject.setName(ownerRegistrationEntityObject.getName());
		  ownerRegistrationObject.setOwnerShopId(ownerRegistrationEntityObject.getOwnerShopId());
		  ownerRegistrationObject.setPassword(ownerRegistrationEntityObject.getPassword());
		  ownerRegistrationObject.setStartDate(ownerRegistrationEntityObject.getStartDate());
		  ownerRegistrationObject.setTotalAmountPaid(ownerRegistrationEntityObject.getTotalAmountPaid());
		  ownerRegistrationObject.setUserName(ownerRegistrationEntityObject.getUserName());
		  ownerRegistrationObject.setVerified(ownerRegistrationEntityObject.getVerified());
		  ownerRegistrationObject.setTinNumner(ownerRegistrationEntityObject.getTinNumner());
		  return ownerRegistrationObject;
	}
	
	

	public OwnerRegistrationEntity ownerEntityXsdMapping(OwnerRegistration ownerRegistration
			) {
		OwnerRegistrationEntity ownerRegistrationEntity = new OwnerRegistrationEntity();
		ownerRegistrationEntity.setAddress(ownerRegistration.getAddress());
		ownerRegistrationEntity.setCity(ownerRegistration.getCity());
		ownerRegistrationEntity.setEmailId(ownerRegistration.getEmailId());
		ownerRegistrationEntity.setEndDate(ownerRegistration.getEndDate());
		ownerRegistrationEntity.setMobile(ownerRegistration.getMobile());
		ownerRegistrationEntity.setName(ownerRegistration.getName());
		ownerRegistrationEntity.setPassword(ownerRegistration.getPassword());
		ownerRegistrationEntity.setStartDate(ownerRegistration.getStartDate());
		ownerRegistrationEntity.setTotalAmountPaid(ownerRegistration.getTotalAmountPaid());
		ownerRegistrationEntity.setUserName(ownerRegistration.getUserName());
		ownerRegistrationEntity.setVerified(ownerRegistration.getVerified());
		ownerRegistrationEntity.setTinNumner(ownerRegistration.getTinNumner());
		return ownerRegistrationEntity;
	}

	public boolean validateOwnerSave(OwnerRegistration ownerRegistration) throws Exception {
		boolean flag  = true;
		if(!(InvoiceCommonUtility.isNotNullObject(ownerRegistration.getAddress()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getCity()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getEmailId()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getMobile()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getName()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getPassword()) &&
				InvoiceCommonUtility.isNotNullObject(ownerRegistration.getUserName()) )){
			flag = false;
		}
		if(flag && !InvoiceCommonUtility.isValidEmailAddress(ownerRegistration.getEmailId())){
			flag = false;
		}
		if(flag && !InvoiceCommonUtility.MaxCharacter(InvoiceCommonUtility.getMaxValueList(MessageConstants.MOBILENUMBER), ownerRegistration.getMobile())){
			flag = false;
		}
		if(flag && !InvoiceCommonUtility.isNumber(ownerRegistration.getMobile())){
			flag = false;
		}
		return flag;
	}

	public OwnerRegistration processOwenerSave(OwnerRegistration ownerRegistration) {
		ownerRegistration.setVerified("Y");
		ownerRegistration.setStartDate(new Date());
		ownerRegistration.setEndDate(new Date());
		return ownerRegistration;
	}

	public String buildDplcateRecords(List<OwnerRegistration> exitingUser, OwnerRegistration ownerRegistration) {
		StringBuilder exisUser = new StringBuilder(MessageConstants.DUPLICATEINVOICEMESSAGE);
		for(OwnerRegistration exisUserObject : exitingUser){
			if(ownerRegistration.getUserName().equalsIgnoreCase(exisUserObject.getUserName())){
				exisUser.append(MessageConstants.INVOICE_OWNER_USERNAMEMESSAGE).append(ownerRegistration.getUserName()).append(",");
			}
			if(ownerRegistration.getMobile().equalsIgnoreCase(exisUserObject.getMobile())){
				exisUser.append(MessageConstants.INVOICE_OWNER_MOBILE_MESSAGE_EXIST).append(exisUserObject.getUserName()).append(",");
			}
			if(ownerRegistration.getEmailId().equalsIgnoreCase(exisUserObject.getEmailId())){
				exisUser.append(MessageConstants.INVOICE_OWNER_EMAIL_MESSAGE_EXIST).append(exisUserObject.getUserName()).append(",");
			}
		}
		exisUser.deleteCharAt(exisUser.length()-1);
		exisUser.append(".");
		
		return exisUser.toString();
	}
	
}
