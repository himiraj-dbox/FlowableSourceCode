package hibernateSignUp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

@Entity
public class BiddingHistoryHibernate {

private int primaryKeyHistory;
private int bidRefernceNumber;
private int price;
private String bidBy;
private int increaseBy;
private BiddingHibernate biddingTable;
public int getBidRefernceNumber() {
	return bidRefernceNumber;
}
public void setBidRefernceNumber(int bidRefernceNumber) {
	this.bidRefernceNumber = bidRefernceNumber;
}
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public String getBidBy() {
	return bidBy;
}
public void setBidBy(String bidBy) {
	this.bidBy = bidBy;
}
public int getIncreaseBy() {
	return increaseBy;
}
public void setIncreaseBy(int increaseBy) {
	this.increaseBy = increaseBy;
}
@ManyToOne
@JoinColumn(name="bid_Reference_Number")
public BiddingHibernate getBiddingTable() {
	return biddingTable;
}
public void setBiddingTable(BiddingHibernate biddingTable) {
	this.biddingTable = biddingTable;
}
@Id
/*@TableGenerator(name="bidReferencePrimary",table="referencePk",pkColumnName="referenceKey")*/
@GeneratedValue(strategy=GenerationType.AUTO)
public int getPrimaryKeyHistory() {
	return primaryKeyHistory;
}
public void setPrimaryKeyHistory(int primaryKeyHistory) {
	this.primaryKeyHistory = primaryKeyHistory;
}

}
