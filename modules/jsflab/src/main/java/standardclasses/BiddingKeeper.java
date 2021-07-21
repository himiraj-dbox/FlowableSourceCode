package standardclasses;

public class BiddingKeeper {
private int bidReferenceNumber;
private int price;
private String userId;
private int increaseBy;
public int getBidReferenceNumber() {
	return bidReferenceNumber;
}
public void setBidReferenceNumber(int bidReferenceNumber) {
	this.bidReferenceNumber = bidReferenceNumber;
}
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public int getIncreaseBy() {
	return increaseBy;
}
public void setIncreaseBy(int increaseBy) {
	this.increaseBy = increaseBy;
}

}
