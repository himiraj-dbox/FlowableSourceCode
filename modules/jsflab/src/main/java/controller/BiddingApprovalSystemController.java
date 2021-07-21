package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean(name="biddingapprovalsystem")
@ViewScoped
public class BiddingApprovalSystemController {
	private int bidReferenceNumber;
	private String userName;
	private String userAproved;
	private String comments;
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
