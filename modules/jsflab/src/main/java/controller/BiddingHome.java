package controller;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;








import org.primefaces.context.RequestContext;

import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import standardclasses.Bidding;
import standardclasses.BiddingAppravalSystem;
import boimpl.BiddingInsertionBO;
import boimpl.BiddingUserVerificationBO;

@ManagedBean(name="biddinghome")
@ViewScoped
public class BiddingHome {
	private List<Bidding> previousBid;
	private List<Bidding> ongoingBid;
	private List<Bidding> upcomingBid;
	private List<Bidding> biddingList;
	private boolean productViewpopup;
	private Bidding productViewBid;
	private String bidRefPara;
	private BiddingInsertionBO bo;
	private boolean registerButtonPopup;
	private boolean userAlreadyRegisteredForBid;
	private boolean userNotLogged;
	private boolean userVerified;
	private boolean userNotAlreadyRegistered;
	private boolean needToRegisterUser;


	public BiddingHome(){
		 bo  = new BiddingInsertionBO();
		 biddingList = new ArrayList<Bidding>();
		BiddingReply reply =  bo.getAllFromBiddingHibernate();
		if(reply.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
			for(int i =0; i<= reply.getHibernateObject().size()-1;i++){
				Bidding tempBidForPopulation  = new Bidding();
				
				tempBidForPopulation.setBidReferceNo(reply.getHibernateObject().get(i).getBidReferenceNumber());
				tempBidForPopulation.setCondition(reply.getHibernateObject().get(i).getCondition());
				tempBidForPopulation.setEndDate(reply.getHibernateObject().get(i).getEndDate());
				tempBidForPopulation.setImagePath(reply.getHibernateObject().get(i).getImageAssociated());
				tempBidForPopulation.setItemDexcription(reply.getHibernateObject().get(i).getItemDescription());
				tempBidForPopulation.setLocation(reply.getHibernateObject().get(i).getLocation());
				tempBidForPopulation.setManufacturerName(reply.getHibernateObject().get(i).getManufacturerName());
				tempBidForPopulation.setPostedBy(reply.getHibernateObject().get(i).getPostedBy());
				tempBidForPopulation.setProductID(reply.getHibernateObject().get(i).getProductId());
				tempBidForPopulation.setProductName(reply.getHibernateObject().get(i).getProductName());
				tempBidForPopulation.setQuantity(reply.getHibernateObject().get(i).getQuantity());
				tempBidForPopulation.setStartingDate(reply.getHibernateObject().get(i).getStartingDate());
				tempBidForPopulation.setStatus(reply.getHibernateObject().get(i).getStatus());
				tempBidForPopulation.setItemName(reply.getHibernateObject().get(i).getItemName());
				//tempBidForPopulation.setTimeEnd(reply.getHibernateObject().get(i).get());
				//tempBidForPopulation.setTimeStarted(reply.getBidding().get(i).getTimeStarted());
				tempBidForPopulation.setWarranty(reply.getHibernateObject().get(i).getWarranty());
				biddingList.add(tempBidForPopulation);
			}
		
		}
		populatngBiddingList(biddingList);
	}


	private void populatngBiddingList(List<Bidding> biddingList) {
		previousBid = new ArrayList<Bidding>();
		ongoingBid = new ArrayList<Bidding>();
		upcomingBid = new ArrayList<Bidding>();
		Date currentDate = new Date();

		int year = currentDate.getYear();
		int month = currentDate.getMonth();
		int day = currentDate.getDate();
		double hour  = currentDate.getHours();
		double minutes = currentDate.getMinutes();
		for(Bidding tempBid : biddingList){
			int i = 0;
			if(tempBid.getStartingDate().getYear() > year) {
				i =1;
			}

			else if(tempBid.getStartingDate().getYear() == year) {
				if(tempBid.getStartingDate().getMonth() == month){
					if(tempBid.getStartingDate().getDate() == day){
						i =0;
					}
					else if(tempBid.getStartingDate().getDate() > day){
						i = 1;
					}
					else if(tempBid.getStartingDate().getDate() < day){
						i = -1;
					}

				}
				else if(tempBid.getStartingDate().getMonth() > month){
					i = 1;
				}
				else if(tempBid.getStartingDate().getMonth() > month){
					i = -1;
				}
			}
			else if(tempBid.getStartingDate().getYear() < year) {
				i = -1;
			}
			if(i == 0){
				ongoingBid.add(tempBid);
			}
			else if(i == 1){
				upcomingBid.add(tempBid);
			}
			else{
				previousBid.add(tempBid);
			}
		}				
	}
	public void closeRegisterButtonPopup(ActionEvent event){
		registerButtonPopup = false;
		userNotLogged = false;
		userAlreadyRegisteredForBid= false;
		userVerified = false;
		needToRegisterUser = false;
	}
	public void openRegisterButtonPopup(ActionEvent event){
		/*Map<String,String> params = 
	            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();*/
		String loginUser =""+event.getComponent().getAttributes().get("loginUser");
		String bidReference =""+event.getComponent().getAttributes().get("bidReference");
		
		if(null != bidReference && !bidReference.isEmpty()){
			int bidRef = Integer.parseInt(bidReference.trim());
		if(loginUser == null || loginUser.isEmpty() || loginUser.equalsIgnoreCase("Login/SignUp")){
			registerButtonPopup = true;
			userNotLogged = true;
			userNotAlreadyRegistered = false;
			
			
			
		}
		else {
			userVerified = true;
			BiddingUserVerificationBO bo = new BiddingUserVerificationBO();
			BiddingUserVerificationReply temp = bo.getAllFromBiddingVerificationHibernate(loginUser);
			BiddingAppravalSystemReply tempApproval = bo.getAllFromBiddingAppravalSystemHibernate(loginUser,bidRef);
			
			
			if(temp == null || temp.getProduct().isEmpty() || temp.getProduct().get(0).getUserName() == null
					|| temp.getProduct().get(0).getUserName().isEmpty() || temp.getProduct().get(0).getUserName().equalsIgnoreCase("Login/SignUp") ){
			registerButtonPopup = true;
			userNotAlreadyRegistered = true;
			}
			else if( temp.getProduct() == null || temp.getProduct().isEmpty() || temp.getProduct().get(0).getVerfiedUser()==null || !temp.getProduct().get(0).getVerfiedUser().equalsIgnoreCase("Y")){
				registerButtonPopup = true;
				userVerified = false;
			}
			else if(tempApproval == null || tempApproval.getBiddingAppravalSystem() == null || tempApproval.getBiddingAppravalSystem().isEmpty() || tempApproval.getBiddingAppravalSystem().get(0).getUserName() == null 
					|| tempApproval.getBiddingAppravalSystem().get(0).getUserName().isEmpty() 
					){
				registerButtonPopup = true;
				
				needToRegisterUser = true;
				BiddingAppravalSystemReply reply = new BiddingAppravalSystemReply();
				BiddingAppravalSystem tempBiddingAppravlal = new BiddingAppravalSystem();
				tempBiddingAppravlal.setBidReferenceNumber(Integer.parseInt(bidReference));
				tempBiddingAppravlal.setUserName(loginUser);
				bo.insertIntoBiddingAppravalSystemHibernate(tempBiddingAppravlal);
			}
			else{
				registerButtonPopup = true;
				userAlreadyRegisteredForBid = true;
			}
		}
		}
	}
	
public void openProductViewPopup(ActionEvent event){
	Map<String,String> params = 
            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	String bidReference =""+event.getComponent().getAttributes().get("bidReference");
	BiddingReply reply = getBo().getAllFromBiddingWithBidHibernate(bidReference.trim());
	if(reply!=null && reply.getNotification() != null && reply.getNotification().getStatus() !=null && reply.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
		String imagePathForCrousel = reply.getBidding().get(0).getImagePath();
		List<String> imageAfterConvertion = new ArrayList<String>();
		if(null != imagePathForCrousel && !imagePathForCrousel.isEmpty() ){
			
			 StringTokenizer st = new StringTokenizer(imagePathForCrousel,",");
	    	 while(st.hasMoreTokens()){
	    		 imageAfterConvertion.add(st.nextToken());
	     	}
		}
		productViewBid = reply.getBidding().get(0);
		productViewBid.setImageForCrousel(imageAfterConvertion);
		setProductViewpopup(true);
		/*RequestContext context = RequestContext.getCurrentInstance();
	
		  context.execute("PF('showproductCustom').show();"); */
	}
		/* Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", true);
	        options.put("draggable", false);
	        options.put("resizable", false);
	        options.put("contentHeight", 320);
	        
		 RequestContext.getCurrentInstance().openDialog("biddingKeeper", options, null);*/
}
public void closeProductViewPopup(ActionEvent event){
	setProductViewpopup(false);
}

	public List<Bidding> getPreviousBid() {
		return previousBid;
	}


	public void setPreviousBid(List<Bidding> previousBid) {
		this.previousBid = previousBid;
	}


	public List<Bidding> getOngoingBid() {
		return ongoingBid;
	}


	public void setOngoingBid(List<Bidding> ongoingBid) {
		this.ongoingBid = ongoingBid;
	}


	public List<Bidding> getUpcomingBid() {
		return upcomingBid;
	}


	public void setUpcomingBid(List<Bidding> upcomingBid) {
		this.upcomingBid = upcomingBid;
	}


	public List<Bidding> getBiddingList() {
		return biddingList;
	}


	public void setBiddingList(List<Bidding> biddingList) {
		this.biddingList = biddingList;
	}


	public boolean isProductViewpopup() {
		return productViewpopup;
	}


	public void setProductViewpopup(boolean productViewpopup) {
		this.productViewpopup = productViewpopup;
	}


	public Bidding getProductViewBid() {
		return productViewBid;
	}


	public void setProductViewBid(Bidding productViewBid) {
		this.productViewBid = productViewBid;
	}


	public String getBidRefPara() {
		return bidRefPara;
	}


	public void setBidRefPara(String bidRefPara) {
		this.bidRefPara = bidRefPara;
	}


	public BiddingInsertionBO getBo() {
		return bo;
	}


	public void setBo(BiddingInsertionBO bo) {
		this.bo = bo;
	}


	public boolean isRegisterButtonPopup() {
		return registerButtonPopup;
	}


	public void setRegisterButtonPopup(boolean registerButtonPopup) {
		registerButtonPopup = registerButtonPopup;
	}


	

	public boolean isUserNotLogged() {
		return userNotLogged;
	}


	public void setUserNotLogged(boolean userNotLogged) {
		this.userNotLogged = userNotLogged;
	}


	public boolean isUserVerified() {
		return userVerified;
	}


	public void setUserVerified(boolean userVerified) {
		this.userVerified = userVerified;
	}


	public boolean isUserAlreadyRegisteredForBid() {
		return userAlreadyRegisteredForBid;
	}


	public void setUserAlreadyRegisteredForBid(boolean userAlreadyRegisteredForBid) {
		this.userAlreadyRegisteredForBid = userAlreadyRegisteredForBid;
	}


	public boolean isUserNotAlreadyRegistered() {
		return userNotAlreadyRegistered;
	}


	public void setUserNotAlreadyRegistered(boolean userNotAlreadyRegistered) {
		this.userNotAlreadyRegistered = userNotAlreadyRegistered;
	}


	public boolean isNeedToRegisterUser() {
		return needToRegisterUser;
	}


	public void setNeedToRegisterUser(boolean needToRegisterUser) {
		this.needToRegisterUser = needToRegisterUser;
	}


	

	




}
