package invoiceGeneratorEntitites;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BillInvoiceID implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long billInvoiceNumber;
	private Long ownerShopId;
	private Long custShopId;
	

	@Column(name="bill_invoice")
	public Long getBillInvoiceNumber() {
		return billInvoiceNumber;
	}
	public void setBillInvoiceNumber(Long billInvoiceNumber) {
		this.billInvoiceNumber = billInvoiceNumber;
	}
	
	@Column(name="owner_shop_id")
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}
	@Column(name="customer_shop_id")
	public Long getCustShopId() {
		return custShopId;
	}
	public void setCustShopId(Long custShopId) {
		this.custShopId = custShopId;
	}
}
