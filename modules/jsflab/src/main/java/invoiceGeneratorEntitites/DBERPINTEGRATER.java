package invoiceGeneratorEntitites;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DB_ERP_INTEGRATER")
public class DBERPINTEGRATER {

	private long id;
	private String vendorname;
	private String po_amount;
	private String poNumber;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="vendor_name")
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	
	@Column(name="po_amount")
	public String getPo_amount() {
		return po_amount;
	}
	public void setPo_amount(String po_amount) {
		this.po_amount = po_amount;
	}
	
	@Column(name="po_no")
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
	
}
