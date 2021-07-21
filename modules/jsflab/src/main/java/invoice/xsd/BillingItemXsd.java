package invoice.xsd;

import javax.xml.bind.annotation.XmlElement;

public class BillingItemXsd {
	private String itemName;
	private String type;
	private Integer quantity;
	private Double cost;
	private Double tax;
	private Integer finalCost;
	
	
	public String getItemName() {
		return itemName;
	}
	@XmlElement
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getType() {
		return type;
	}
	@XmlElement
	public void setType(String type) {
		this.type = type;
	}
	public Integer getQuantity() {
		return quantity;
	}
	@XmlElement
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getCost() {
		return cost;
	}
	@XmlElement
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getTax() {
		return tax;
	}
	@XmlElement
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public Integer getFinalCost() {
		return finalCost;
	}
	@XmlElement
	public void setFinalCost(Integer finalCost) {
		this.finalCost = finalCost;
	}
}
