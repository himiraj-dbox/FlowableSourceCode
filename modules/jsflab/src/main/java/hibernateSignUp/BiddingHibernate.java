package hibernateSignUp;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
public class BiddingHibernate {
private int bidReferenceNumber;
private Date StartingDate;
private Date endDate;
private String productName;
private String itemDescription;
private String status;
private String condition;
private String postedBy;
private String location;
private String manufacturerName;
private String warranty;
private String productId;
private int quantity;
private String itemName;
private String imageAssociated;
private List<BiddingHistoryHibernate> biddingHistoryList;

@Id
public int getBidReferenceNumber() {
	return bidReferenceNumber;
}
public void setBidReferenceNumber(int bidReferenceNumber) {
	this.bidReferenceNumber = bidReferenceNumber;
}
public Date getStartingDate() {
	return StartingDate;
}
public void setStartingDate(Date startingDate) {
	StartingDate = startingDate;
}
public Date getEndDate() {
	return endDate;
}
public void setEndDate(Date endDate) {
	this.endDate = endDate;
}
public String getProductName() {
	return productName;
}
public void setProductName(String productName) {
	this.productName = productName;
}
public String getItemDescription() {
	return itemDescription;
}
public void setItemDescription(String itemDescription) {
	this.itemDescription = itemDescription;
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
public String getPostedBy() {
	return postedBy;
}
public void setPostedBy(String postedBy) {
	this.postedBy = postedBy;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
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
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
public String getImageAssociated() {
	return imageAssociated;
}
public void setImageAssociated(String imageAssociated) {
	this.imageAssociated = imageAssociated;
}

@OneToMany(targetEntity=BiddingHistoryHibernate.class, mappedBy="bidRefernceNumber",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
public List<BiddingHistoryHibernate> getBiddingHistoryList() {
	return biddingHistoryList;
}
public void setBiddingHistoryList(
		List<BiddingHistoryHibernate> biddingHistoryList) {
	this.biddingHistoryList = biddingHistoryList;
}
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}


}
