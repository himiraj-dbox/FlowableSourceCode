package invoiceGeneratorEntitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice_surcharges")
public class SurchargeInvoiceEntity {
	private long sequence;
	private Long billInvoiceNumber;
	private Long ownerShopId;
	private Long custShopId;
	private String Name;
	private double cost; 
	private String industryIndicator;

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
	@Column(name="name")
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	@Column(name="cost")
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	@Column(name="industry_indicator")
	public String getIndustryIndicator() {
		return industryIndicator;
	}
	public void setIndustryIndicator(String industryIndicator) {
		this.industryIndicator = industryIndicator;
	}
}
