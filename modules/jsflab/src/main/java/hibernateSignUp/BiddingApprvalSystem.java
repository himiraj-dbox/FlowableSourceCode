package hibernateSignUp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bid_approval_system")
public class BiddingApprvalSystem {
	private int bidReferenceNumber;
	private String userName;
	private String userAproved;
	private String comments;
	
	@Id
	public int getBidReferenceNumber() {
		return bidReferenceNumber;
	}
	public void setBidReferenceNumber(int bidReferenceNumber) {
		this.bidReferenceNumber = bidReferenceNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAproved() {
		return userAproved;
	}
	public void setUserAproved(String userAproved) {
		this.userAproved = userAproved;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}
