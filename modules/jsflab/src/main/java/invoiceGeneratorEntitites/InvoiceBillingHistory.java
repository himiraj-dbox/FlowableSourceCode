package invoiceGeneratorEntitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice_billing_history")
public class InvoiceBillingHistory {
	private long sequence;
	private Long billInvoiceNumber;
	private Long ownerShopId;
	private Long custShopId;
	private String description;
	private double amountPaid; 
	private double totalAmount; 
	private double remainingAmount; 
	private String comments;

	@Id
	@Column(name="sequence")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
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
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="amount_paid")
	public double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	@Column(name="total_amount")
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Column(name="remaining_amount")
	public double getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
	@Column(name="comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
