package dao;

import java.util.List;

import invoiceGeneratorEntitites.DBDuplicateCheck;
import notification.InvoiceNotification;
import notification.NotificationObject;


public interface DBDuplicateCheckDao {
	public <T> NotificationObject save(NotificationObject notification,Object o) ;
	public DBDuplicateCheck getRecordForProcessInstance(DBDuplicateCheck dBDuplicateCheck) throws Exception;
	public DBDuplicateCheck checkDuplicateValues(DBDuplicateCheck dbObject);
	public String getProcessinstanceID(String taskId);
}
