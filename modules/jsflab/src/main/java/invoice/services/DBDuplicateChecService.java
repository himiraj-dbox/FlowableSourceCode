package invoice.services;

import dao.DBDuplicateCheckDao;
import daoimpl.DBDuplicateCheckDaoImpl;
import invoiceGeneratorEntitites.DBDuplicateCheck;
import notification.NotificationObject;

public class DBDuplicateChecService {
	private DBDuplicateCheckDao  daoImpl= new DBDuplicateCheckDaoImpl();
	public NotificationObject saveFormDetails(DBDuplicateCheck dbObject) {
		NotificationObject notification = new NotificationObject();
		//dbObject.setProcessInstanceId(daoImpl.getProcessinstanceID(dbObject.getProcessInstanceId()));
		notification = daoImpl.save(notification, dbObject);
		return notification;
	}

	public DBDuplicateCheck checkValueForProcessInstanceId(DBDuplicateCheck dbObject) throws Exception {
		dbObject.setProcessInstanceId(daoImpl.getProcessinstanceID(dbObject.getProcessInstanceId()));
		return daoImpl.getRecordForProcessInstance(dbObject);
	}

	public DBDuplicateCheck checkDuplicateValues(DBDuplicateCheck dbObject) {
		dbObject.setProcessInstanceId(daoImpl.getProcessinstanceID(dbObject.getProcessInstanceId()));
		return daoImpl.checkDuplicateValues(dbObject);
	}
	public DBDuplicateCheck doubleCheckDuplicateValues(DBDuplicateCheck dbObject, String taskID) {
		String currentProcessinstanceId  = daoImpl.getProcessinstanceID(taskID);
		DBDuplicateCheck duplcateObjec =   daoImpl.checkDuplicateValues(dbObject);
		if(null != duplcateObjec && !currentProcessinstanceId.equalsIgnoreCase(currentProcessinstanceId) ) {
			return null;
		}
		return new DBDuplicateCheck() ;
	}

}
