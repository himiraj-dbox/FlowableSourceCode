package invoice.xsd;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//http://localhost:1006/jsflab/rest/UserService/users
@XmlRootElement(name = "invoiceOwnerRegistration") 
public class OwnerRegistration implements Serializable {  
	private static final long serialVersionUID = 1L; 
	private String name;
	private String address;
	private String mobile;
	private String emailId;
	private String city;
	private String userName;
	private String password;
	private Date startDate;
	private Date endDate;
	private String verified;
	private Integer totalAmountPaid;
	private Long ownerShopId;
	private Long tinNumner;
	
	public String getName() {
		return name;
	}
	
	 @XmlElement 
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	 @XmlElement 
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	
	 @XmlElement 
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmailId() {
		return emailId;
	}
	
	 @XmlElement 
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCity() {
		return city;
	}
	
	 @XmlElement 
	public void setCity(String city) {
		this.city = city;
	}
	
	
	public String getUserName() {
		return userName;
	}
	
	 @XmlElement 
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	
	 @XmlElement 
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getStartDate() {
		return startDate;
	}
	
	 @XmlElement 
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	
	 @XmlElement 
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getVerified() {
		return verified;
	}
	
	 @XmlElement 
	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	public Integer getTotalAmountPaid() {
		return totalAmountPaid;
	}
	
	 @XmlElement 
	public void setTotalAmountPaid(Integer totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}
	
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	
	 @XmlElement 
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}

	 @XmlElement 
	public Long getTinNumner() {
		return tinNumner;
	}

	public void setTinNumner(Long tinNumner) {
		this.tinNumner = tinNumner;
	}
	



}
