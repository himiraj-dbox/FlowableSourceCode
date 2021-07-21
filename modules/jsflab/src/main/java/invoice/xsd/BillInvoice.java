package invoice.xsd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoiceBillInvoice") 
public class BillInvoice implements Serializable {  
	private static final long serialVersionUID = 1L; 
	private BillInvoiceID billInvoiceID;
	private Integer totalquantity;
	private Double totalcost;
	private Double totaltax;
	private Double totalsurcharge;
	private Integer finalCost;
	private Double amountDue;
	private Date billDate;
	private String billType;
	private Double totalOverallCost;
	private List<SurchargexXsd> surcharges;
	private List<BillingItemXsd> billingItems;

	public BillInvoiceID getBillInvoiceID() {
		return billInvoiceID;
	}
	
	@XmlElement 
	public void setBillInvoiceID(BillInvoiceID billInvoiceID) {
		this.billInvoiceID = billInvoiceID;
	}
	
	
	
	
	
	public Integer getFinalCost() {
		return finalCost;
	}
	
	@XmlElement 
	public void setFinalCost(Integer finalCost) {
		this.finalCost = finalCost;
	}
	
	public Date getBillDate() {
		return billDate;
	}
	
	@XmlElement 
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	
	public String getBillType() {
		return billType;
	}
	
	@XmlElement 
	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	public Double getAmountDue() {
		return amountDue;
	}
	
	@XmlElement 
	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}


	public List<SurchargexXsd> getSurcharges() {
		return surcharges;
	}

	@XmlElement 
	public void setSurcharges(List<SurchargexXsd> surcharges) {
		this.surcharges = surcharges;
	}

	public List<BillingItemXsd> getBillingItems() {
		return billingItems;
	}

	@XmlElement 
	public void setBillingItems(List<BillingItemXsd> billingItems) {
		this.billingItems = billingItems;
	}

	public Integer getTotalquantity() {
		return totalquantity;
	}

	@XmlElement 
	public void setTotalquantity(Integer totalquantity) {
		this.totalquantity = totalquantity;
	}

	public Double getTotalcost() {
		return totalcost;
	}

	@XmlElement 
	public void setTotalcost(Double totalcost) {
		this.totalcost = totalcost;
	}

	public Double getTotaltax() {
		return totaltax;
	}

	@XmlElement 
	public void setTotaltax(Double totaltax) {
		this.totaltax = totaltax;
	}

	public Double getTotalsurcharge() {
		return totalsurcharge;
	}

	@XmlElement 
	public void setTotalsurcharge(Double totalsurcharge) {
		this.totalsurcharge = totalsurcharge;
	}

	public Double getTotalOverallCost() {
		return totalOverallCost;
	}
	@XmlElement 
	public void setTotalOverallCost(Double totalOverallCost) {
		this.totalOverallCost = totalOverallCost;
	}
	

}
