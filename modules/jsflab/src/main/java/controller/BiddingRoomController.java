package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.behavior.ajax.AjaxBehavior;

import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import standardclasses.Bidding;
import standardclasses.BiddingKeeper;
import boimpl.BiddingInsertionBO;
import boimpl.BiddingKeeperBO;
import boimpl.BiddingUserVerificationBO;


@ManagedBean(name="biddingroom")
@SessionScoped
public class BiddingRoomController extends Bidding {
	private BiddingInsertionBO boBiddingInsertion;
	private BiddingKeeperBO boBiddingKeeper;
	private boolean showBiddingRoom;
	private String validateUser;
	private boolean showBiddingRoomDialog;
	private List<String> biddingRoomImages;
	private int amount;
	private int increaseBy;
	public BiddingRoomController(){
		boBiddingInsertion = new BiddingInsertionBO();
		boBiddingKeeper = new BiddingKeeperBO();
	}
	public String closeBiddingRoomPopup(){
		String navigation = "biddingHome";
		showBiddingRoomDialog = false;
		validateUser ="";
		return navigation;
	}
	public void autoRefresh(){
		BiddingKeeperReply tempBiddingKeeper = getBoBiddingKeeper().getMaxFromBiddingHibernate(getBidReferceNo());
		if(tempBiddingKeeper !=null && tempBiddingKeeper.getNotification() !=null && tempBiddingKeeper.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
			BiddingKeeper tempkeeper = tempBiddingKeeper.getBidKeeperList().get(0);
			setAmount(tempkeeper.getPrice());
			setIncreaseBy(tempkeeper.getIncreaseBy());
		}
	}
	
	public  synchronized  void  clickOnBid(){
		Map<String,String> params = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	  String loginUser = params.get("loginUser");
		
		BiddingKeeper bk = new BiddingKeeper();
		bk.setPrice(getIncreaseBy());
		bk.setBidReferenceNumber(getBidReferceNo());
		bk.setIncreaseBy(getIncreaseBy()-getAmount());
		bk.setUserId(loginUser);
		BiddingKeeperReply tempKeeper =getBoBiddingKeeper().insertUpdateBiddingKeeperHibernate(bk);
		if(tempKeeper != null  && tempKeeper.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
			BiddingKeeperReply tempBiddingKeeper = getBoBiddingKeeper().getMaxFromBiddingHibernate(getBidReferceNo());
			BiddingKeeper tempkeeper = tempBiddingKeeper.getBidKeeperList().get(0);
			setAmount(tempkeeper.getPrice());
			setIncreaseBy(tempkeeper.getIncreaseBy());
		}
	}
	
	public String populateFrmBidding(){
		String navigation = "biddingRoom";;
		Map<String,String> params = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	  String bid = params.get("bidReferenceNumber");
	 
	  String loginUser = params.get("userNamw");
	  if(null != bid && !bid.isEmpty()){
			int bidRef = Integer.parseInt(bid.trim());
		if(loginUser == null || loginUser.isEmpty() || loginUser.equalsIgnoreCase("Login/SignUp")){
			setValidateUser("NL");
			showBiddingRoomDialog = true;
		}
		else {
			
			BiddingUserVerificationBO bo = new BiddingUserVerificationBO();
			BiddingUserVerificationReply temp = bo.getAllFromBiddingVerificationHibernate(loginUser);
			BiddingAppravalSystemReply tempApproval = bo.getAllFromBiddingAppravalSystemHibernate(loginUser,bidRef);
			if(temp == null || temp.getProduct().isEmpty() || temp.getProduct().get(0).getUserName() == null
					|| temp.getProduct().get(0).getUserName().isEmpty() || temp.getProduct().get(0).getUserName().equalsIgnoreCase("Login/SignUp") ){
				showBiddingRoomDialog = true;
				setValidateUser("NR");
			}
			else if( temp.getProduct() == null || temp.getProduct().isEmpty() || temp.getProduct().get(0).getVerfiedUser()==null || !temp.getProduct().get(0).getVerfiedUser().equalsIgnoreCase("Y")){
				showBiddingRoomDialog = true;
				setValidateUser("NR");
			}
			else if(tempApproval == null || tempApproval.getBiddingAppravalSystem() == null || tempApproval.getBiddingAppravalSystem().isEmpty() || tempApproval.getBiddingAppravalSystem().get(0).getUserName() == null 
					|| tempApproval.getBiddingAppravalSystem().get(0).getUserName().isEmpty() 
					){
				showBiddingRoomDialog = true;
				setValidateUser("NR");
			}
			else{
		BiddingReply tempBiddingInsertion = getBoBiddingInsertion().getAllFromBiddingWithBidHibernate(bid);
		
		BiddingKeeperReply tempBiddingKeeper = getBoBiddingKeeper().getMaxFromBiddingHibernate(Integer.parseInt(bid));
		if(tempBiddingInsertion != null  && tempBiddingInsertion.getNotification() != null && tempBiddingInsertion.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
			Bidding tempBiddingObject = tempBiddingInsertion.getBidding().get(0);
			setBidReferceNo(tempBiddingObject.getBidReferceNo());
			setProductID(tempBiddingObject.getProductID());
			setProductName(tempBiddingObject.getProductName());
			setCondition(tempBiddingObject.getCondition());
			setWarranty(tempBiddingObject.getWarranty());
			setItemDexcription(tempBiddingObject.getItemDexcription());
			List<String> imageAfterConvertion = new ArrayList<String>();
			if(null != tempBiddingObject.getImagePath() && !tempBiddingObject.getImagePath().isEmpty() ){
				
				 StringTokenizer st = new StringTokenizer(tempBiddingObject.getImagePath(),",");
		    	 while(st.hasMoreTokens()){
		    		 imageAfterConvertion.add(st.nextToken());
		     	}
			}
			setBiddingRoomImages(imageAfterConvertion);
			showBiddingRoom= true;
			
			if(tempBiddingKeeper !=null && tempBiddingKeeper.getNotification() !=null && tempBiddingKeeper.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
				BiddingKeeper tempkeeper = tempBiddingKeeper.getBidKeeperList().get(0);
				setAmount(tempkeeper.getPrice());
				setIncreaseBy(tempkeeper.getIncreaseBy());
			}
			
			
		}
			}
		}
	  }
		return navigation;
	}

	public BiddingInsertionBO getBoBiddingInsertion() {
		return boBiddingInsertion;
	}

	public void setBoBiddingInsertion(BiddingInsertionBO boBiddingInsertion) {
		this.boBiddingInsertion = boBiddingInsertion;
	}

	public boolean isShowBiddingRoom() {
		return showBiddingRoom;
	}

	public void setShowBiddingRoom(boolean showBiddingRoom) {
		this.showBiddingRoom = showBiddingRoom;
	}

	public String getValidateUser() {
		return validateUser;
	}

	public void setValidateUser(String validateUser) {
		this.validateUser = validateUser;
	}

	public boolean isShowBiddingRoomDialog() {
		return showBiddingRoomDialog;
	}

	public void setShowBiddingRoomDialog(boolean showBiddingRoomDialog) {
		this.showBiddingRoomDialog = showBiddingRoomDialog;
	}
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getIncreaseBy() {
		return increaseBy;
	}
	public void setIncreaseBy(int increaseBy) {
		this.increaseBy = increaseBy;
	}
	public BiddingKeeperBO getBoBiddingKeeper() {
		return boBiddingKeeper;
	}
	public void setBoBiddingKeeper(BiddingKeeperBO boBiddingKeeper) {
		this.boBiddingKeeper = boBiddingKeeper;
	}
	public List<String> getBiddingRoomImages() {
		return biddingRoomImages;
	}
	public void setBiddingRoomImages(List<String> biddingRoomImages) {
		this.biddingRoomImages = biddingRoomImages;
	}

}
