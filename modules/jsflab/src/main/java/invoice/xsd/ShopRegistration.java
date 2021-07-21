package invoice.xsd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class ShopRegistration implements Serializable{


	private static final long serialVersionUID = 1L;
	private Long ownerShopId;
	private Long customerShopId;
	
	
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	 @XmlElement
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}
	
	public Long getCustomerShopId() {
		return customerShopId;
	}
	 @XmlElement
	public void setCustomerShopId(Long customerShopId) {
		this.customerShopId = customerShopId;
	}

}
