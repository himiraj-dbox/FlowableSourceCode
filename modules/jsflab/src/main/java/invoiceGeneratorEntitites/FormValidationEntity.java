package invoiceGeneratorEntitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ACT_FO_FORM_VALIDATION")
public class FormValidationEntity {
	private String invoiceNumber;
	
	@Id
	@Column(name="invoiceNumber")
public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}


}
