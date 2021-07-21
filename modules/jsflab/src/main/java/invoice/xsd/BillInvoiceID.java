package invoice.xsd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlElement;

public class BillInvoiceID  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long billInvoiceNumber;
	private Long ownerShopId;
	private Long custShopId;
	

	public Long getBillInvoiceNumber() {
		return billInvoiceNumber;
	}
	 @XmlElement
	public void setBillInvoiceNumber(Long billInvoiceNumber) {
		this.billInvoiceNumber = billInvoiceNumber;
	}
	
	public Long getOwnerShopId() {
		return ownerShopId;
	}
	
	 @XmlElement
	public void setOwnerShopId(Long ownerShopId) {
		this.ownerShopId = ownerShopId;
	}
	public Long getCustShopId() {
		return custShopId;
	}
	
	 @XmlElement
	public void setCustShopId(Long custShopId) {
		this.custShopId = custShopId;
	}
}


