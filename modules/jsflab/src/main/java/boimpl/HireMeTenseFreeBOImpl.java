package boimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constantBidding.MessageConstants;
import hibernateDao.HibernateDao;
import hibernateDao.HibernateDaoImpl;
import hibernateSignUp.HireMeTileTilesCatalog;
import hibernateSignUp.TileUserRegistration;
import hibernateSignUp.TileUserTaskManager;
import notification.SuccessFailureWarnig;
import replyclasses.HireMeTilesCatalogReply;
import replyclasses.TileUserRegistrationReply;
import request.FetchTilesRecordRequest;
import request.HireMeTilesCatalogRequest;

public class HireMeTenseFreeBOImpl implements HireMeTenseFreeBO {
	private HibernateDao hiremetenseDao = new HibernateDaoImpl();
	@Override
	public SuccessFailureWarnig saveNewUser(TileUserRegistrationReply request) {
	 SuccessFailureWarnig notification = new SuccessFailureWarnig();
		if(checkIfUserAlreadyExist(request.getParameterPassing().get(MessageConstants.MOBILENUMBER))){
     
       TileUserRegistration persistentObject = new TileUserRegistration();
       persistentObject.setCity(request.getUserList().get(0).getCity());
       persistentObject.setEmailId(request.getUserList().get(0).getEmailId());
       persistentObject.setLocality(request.getUserList().get(0).getLocality());
       persistentObject.setMobileNumber(request.getUserList().get(0).getMobileNumber());
       persistentObject.setName(request.getUserList().get(0).getName());
       persistentObject.setRegistrationDate(new Date());
       persistentObject.setState(request.getUserList().get(0).getState());
       notification = hiremetenseDao.save(persistentObject.getClass(), notification,persistentObject);
       notification = saveIntoHireMeTaskManger(request);
		} else {
			notification = saveIntoHireMeTaskManger(request);
		}
		return notification;
	}

	private SuccessFailureWarnig saveIntoHireMeTaskManger(TileUserRegistrationReply request) {
		TileUserTaskManager persistentObject = new TileUserTaskManager();
		 SuccessFailureWarnig notification = new SuccessFailureWarnig();
		persistentObject.setMobileNumber(request.getUserList().get(0).getMobileNumber());
		persistentObject.setStatus(MessageConstants.NOTSTARTEDCODE);
		   notification = hiremetenseDao.save(persistentObject.getClass(), notification,persistentObject);
		return notification;
		
	}

	public boolean checkIfUserAlreadyExist(String mobileNumber){
		boolean flag = hiremetenseDao.hireMeUserAlreadyExists(mobileNumber);
		return flag;
	}
	
	@Override
	public SuccessFailureWarnig updateExistingUser(TileUserRegistrationReply request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileUserRegistrationReply getAllUser(TileUserRegistrationReply request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileUserRegistrationReply getSpecificUser(TileUserRegistrationReply request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SuccessFailureWarnig saveTileCatalogHirem(List<HireMeTileTilesCatalog> request) {
	 SuccessFailureWarnig notification = new SuccessFailureWarnig();
	try{	
	 for(HireMeTileTilesCatalog hireMeTense : request){
			if(notification.getStatus().equalsIgnoreCase(MessageConstants.FAILURE)){
				throw new Exception();
				
			}
       notification = hiremetenseDao.save(String.class, notification,hireMeTense);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return notification;
	}
	
	@Override
 public List<HireMeTilesCatalogRequest> fetchTilesRecord(FetchTilesRecordRequest request){
	 HibernateDao tileCatalogDao = new HibernateDaoImpl();
	 HireMeTilesCatalogReply reply = new HireMeTilesCatalogReply();
		Map<String,String> parameter = new HashMap<String, String>();
		parameter.put(MessageConstants.SHOPTYPE, request.getShopType());
		parameter.put(MessageConstants.SUBCATEGORY, request.getTileSubCategory());
		parameter.put(MessageConstants.THIRDCATEGORY, request.getTileType());
		reply.setParameterPassing(parameter);
		reply = tileCatalogDao.fetchChildCatalogData(reply);
		List<HireMeTilesCatalogRequest> userList = new ArrayList<HireMeTilesCatalogRequest>();
		if(null != reply.getPeristanTObject() && !reply.getPeristanTObject().isEmpty()){
		for(HireMeTileTilesCatalog hireMeTileTilesCatalog : reply.getPeristanTObject()){
			HireMeTilesCatalogRequest requestObject = new HireMeTilesCatalogRequest();
			requestObject.setApplications(hireMeTileTilesCatalog.getApplications());
			requestObject.setBrand(hireMeTileTilesCatalog.getBrand());
			requestObject.setDescription(hireMeTileTilesCatalog.getDescription());
			requestObject.setDimensionCategory(hireMeTileTilesCatalog.getDimensionCategory());
			requestObject.setEdgeType(hireMeTileTilesCatalog.getEdgeType());
			requestObject.setImagePath(hireMeTileTilesCatalog.getImagePath());
			requestObject.setMaterialType(hireMeTileTilesCatalog.getMaterialType());
			requestObject.setPrice(hireMeTileTilesCatalog.getPrice());
			requestObject.setProductCode(hireMeTileTilesCatalog.getProductCode());
			requestObject.setShopType(hireMeTileTilesCatalog.getShopType());
			requestObject.setSubCategory(hireMeTileTilesCatalog.getSubCategory());
			requestObject.setThirdCategory(hireMeTileTilesCatalog.getThirdCategory());
			userList.add(requestObject);
		}}
		return userList;
 }

}
