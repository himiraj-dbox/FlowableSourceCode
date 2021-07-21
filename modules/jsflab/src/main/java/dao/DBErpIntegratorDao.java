package dao;

import java.util.List;

import invoiceGeneratorEntitites.DBERPINTEGRATER;
import invoiceGeneratorEntitites.OwnerRegistrationEntity;

public interface DBErpIntegratorDao {
	public<T> Object getSpecifRecord(Class<T> t, Object _o) throws Exception;
	public List<DBERPINTEGRATER> getExistingOwnerDetails(DBERPINTEGRATER billOverviewEntity) throws Exception;
}
