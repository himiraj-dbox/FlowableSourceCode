package invoiceGeneratorEntitites;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice_owner_registration")
public class OwnerRegistrationEntity implements Serializable {
	/**
	 * 
	 */
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
	
	@Column(name="name",nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="address",nullable=false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="contact_number",nullable=false,unique=true)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name="email_id" ,nullable=false,unique=true)
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	@Column(name="city",nullable=false)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	@Column(name="user_name" ,nullable=false,unique=true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="password" ,nullable=false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name="verified")
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	@Column(name="total_amount_paid")
	public Integer getTotalAmountPaid() {
		return totalAmountPaid;
	}
	public void setTotalAmountPaid(Integer totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}
	
	@Id
	@Column(name="owner_shop_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}
	
	@Column(name="tin_number")
	public Long getTinNumner() {
		return tinNumner;
	}
	public void setTinNumner(Long tinNumner) {
		this.tinNumner = tinNumner;
	}
	

}
