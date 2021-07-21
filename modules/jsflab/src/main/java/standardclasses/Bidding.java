package standardclasses;

import java.util.Date;
import java.util.List;



public class Bidding {
	private int bidReferceNo;
	private String productName;
	private Date startingDate;
	private Date endDate;
	private String status;
	private String condition;
	private String locations;
	private String manufacturerName;
	private String warranty;
	private String productID;
	private String itemDexcription;
	private String location;
	private int quantity;
	private String postedBy;
	private String imagePath;
	private double timeStarted;
	private double timeEnd;
	private String itemName;
	private List<String> imageForCrousel;
	public int getBidReferceNo() {
		return bidReferceNo;
	}
	public void setBidReferceNo(int bidReferceNo) {
		this.bidReferceNo = bidReferceNo;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getItemDexcription() {
		return itemDexcription;
	}
	public void setItemDexcription(String itemDexcription) {
		this.itemDexcription = itemDexcription;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getPostedBy() {
		return postedBy;
	}
	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}
	public double getTimeStarted() {
		return timeStarted;
	}
	public void setTimeStarted(double timeStarted) {
		this.timeStarted = timeStarted;
	}
	public double getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(double timeEnd) {
		this.timeEnd = timeEnd;
	}
	public List<String> getImageForCrousel() {
		return imageForCrousel;
	}
	public void setImageForCrousel(List<String> imageForCrousel) {
		this.imageForCrousel = imageForCrousel;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
