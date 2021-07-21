package invoice.services;

import dao.DBErpIntegratorDao;
import daoimpl.DBErpIntegratorDaoImpl;
import invoiceGeneratorEntitites.DBERPINTEGRATER;

public class DBErpIntegraterServices {
private DBErpIntegratorDao  daoImpl= new DBErpIntegratorDaoImpl();

public DBERPINTEGRATER getPoVendorDetails(DBERPINTEGRATER o) throws Exception {
	DBERPINTEGRATER dBERPINTEGRATER = null;
	if(null != daoImpl.getExistingOwnerDetails(o) && !daoImpl.getExistingOwnerDetails(o).isEmpty()) {
		dBERPINTEGRATER = daoImpl.getExistingOwnerDetails(o).get(0);
	}return dBERPINTEGRATER;
} 
}
