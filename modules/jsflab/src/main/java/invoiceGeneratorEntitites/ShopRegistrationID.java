package invoiceGeneratorEntitites;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class ShopRegistrationID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ownerShopId;
	private Long customerShopId;
	
	
	@Column(name="owner_shop_id")
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}
	
	@Column(name="customer_shop_id")
	public Long getCustomerShopId() {
		return customerShopId;
	}
	public void setCustomerShopId(Long customerShopId) {
		this.customerShopId = customerShopId;
	}
}
