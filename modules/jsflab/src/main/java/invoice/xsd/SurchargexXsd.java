package invoice.xsd;

import javax.xml.bind.annotation.XmlElement;

public class SurchargexXsd {
private String name;
private double cost;
private String industryType;


public String getName() {
	return name;
}

@XmlElement
public void setName(String name) {
	this.name = name;
}
public double getCost() {
	return cost;
}

@XmlElement
public void setCost(double cost) {
	this.cost = cost;
}
public String getIndustryType() {
	return industryType;
}

@XmlElement
public void setIndustryType(String industryType) {
	this.industryType = industryType;
}


}
