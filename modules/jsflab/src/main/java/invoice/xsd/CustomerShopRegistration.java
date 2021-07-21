package invoice.xsd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import invoiceGeneratorEntitites.ShopRegistrationID;

//http://localhost:1006/jsflab/rest/UserService/users
@XmlRootElement(name = "invoiceCustomerShopRegistration") 
public class CustomerShopRegistration implements Serializable {  
	private static final long serialVersionUID = 1L; 
	private ShopRegistration shopRegistrationID;
	private String name;
	private String ownerShopName;
	private String address;
	private String mobileNumber;
	private String customerEmailId;
	private String city;
	private String location;
	private String description;
	private String tags;
	
	public ShopRegistration getShopRegistrationID() {
		return shopRegistrationID;
	}
	
	 @XmlElement 
	public void setShopRegistrationID(ShopRegistration shopRegistrationID) {
		this.shopRegistrationID = shopRegistrationID;
	}
	
	public String getName() {
		return name;
	}
	
	 @XmlElement 
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOwnerShopName() {
		return ownerShopName;
	}
	
	 @XmlElement 
	public void setOwnerShopName(String ownerShopName) {
		this.ownerShopName = ownerShopName;
	}
	
	public String getAddress() {
		return address;
	}
	
	 @XmlElement 
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	 @XmlElement 
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	
	 @XmlElement 
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	
	public String getCity() {
		return city;
	}
	
	 @XmlElement 
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getLocation() {
		return location;
	}
	
	 @XmlElement 
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}
	
	 @XmlElement 
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTags() {
		return tags;
	}
	
	 @XmlElement 
	public void setTags(String tags) {
		this.tags = tags;
	}


}
