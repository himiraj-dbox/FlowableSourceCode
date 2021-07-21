package invoice.xsd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import invoiceGeneratorEntitites.BillInvoiceID;

@XmlRootElement(name = "invoiceBillOverview") 
public class BillOverview implements Serializable {  
	private static final long serialVersionUID = 1L; 

	private BillInvoiceID billInvoiceID;
	private Integer totalQuantity;
	private Double totalCost;
	private Double totalTax;
	private Double surcharge;
	private Integer finalCost;
	private Double amountDue;
	private String billType;
	
	public BillInvoiceID getBillInvoiceID() {
		return billInvoiceID;
	}
	@XmlElement 
	public void setBillInvoiceID(BillInvoiceID billInvoiceID) {
		this.billInvoiceID = billInvoiceID;
	}	
	public Integer getTotalQuantity() {
		return totalQuantity;
	}
	@XmlElement 
	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	public Double getTotalCost() {
		return totalCost;
	}
	@XmlElement 
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	
	public Double getTotalTax() {
		return totalTax;
	}
	
	@XmlElement 
	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}
	
	public Double getSurcharge() {
		return surcharge;
	}
	
	@XmlElement 
	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}
	
	public Integer getFinalCost() {
		return finalCost;
	}
	
	@XmlElement 
	public void setFinalCost(Integer finalCost) {
		this.finalCost = finalCost;
	}
	
	public Double getAmountDue() {
		return amountDue;
	}
	
	@XmlElement 
	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}
	
	public String getBillType() {
		return billType;
	}
	
	@XmlElement 
	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	



}
