package hibernateSignUp;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="signup")
@SecondaryTable(name="bidding_user")

public class SignUp {


	private String userName;
	private String password;
	private String emailAddress;
	private String contactNumber;
	private String varifiedUser;
	private String adminUser;
	private Date dateCreated;
	private String desgination;
	private String companyName;
	private String State;
	private String city;
	private String address;
	private String lastName;
	private String firstName;
	private int trustScore;
	private String verified;
	private String activationCode;
	private String officeAddress;
	private String serviceTax;
	
	


	@Id
	@Column(name="user_name")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="email_address",nullable=false)
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name="user_verified")
	public String getVarifiedUser() {
		return varifiedUser;
	}
	public void setVarifiedUser(String varifiedUser) {
		this.varifiedUser = varifiedUser;
	}
	@Transient
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	@Temporal(TemporalType.DATE)
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDesgination() {
		return desgination;
	}
	public void setDesgination(String desgination) {
		this.desgination = desgination;
	}
	@Column(name="company_name")
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Column(nullable=false)
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	@Column(nullable=false)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Column(nullable=false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="contact_number",nullable=false)
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	@Column(table="bidding_user",name="trust_score")
	public int getTrustScore() {
		return trustScore;
	}
	public void setTrustScore(int trustScore) {
		this.trustScore = trustScore;
	}
	@Column(table="bidding_user")
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	@Column(table="bidding_user",name="office_address")
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	@Column(table="bidding_user",name="service_tax")
	public String getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}
	@Column(table="bidding_user",name="activation_code")
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	

}
