package invoice.xsd;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errorCode") 
public class InvoiceErrorCodes {
	private String errorCode;
	private String errorMessage;
	private String Descriptions;
	public String getErrorCode() {
		return errorCode;
	}
	@XmlElement 
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	@XmlElement 
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getDescriptions() {
		return Descriptions;
	}
	@XmlElement 
	public void setDescriptions(String descriptions) {
		Descriptions = descriptions;
	}
	

}
