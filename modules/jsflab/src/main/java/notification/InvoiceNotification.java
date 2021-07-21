package notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class InvoiceNotification {
	final static Logger logger = Logger.getLogger(InvoiceNotification.class);
	private String status;
	private String message;
	private volatile static  List<InvoiceNotification> notificationPool;
	
	private InvoiceNotification(){
		
	}
	private static void  createNewObject(){
		try{
		logger.debug("Inside InvoiceNotification");
		if(null == notificationPool ){
			notificationPool = new ArrayList<InvoiceNotification>();
		}
		InvoiceNotification invoiceNotification = new InvoiceNotification();
		notificationPool.add(invoiceNotification);
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static synchronized InvoiceNotification getInstance(){
		try{
		if(null == notificationPool || !notificationPool.isEmpty()){
			 createNewObject();
		}
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return notificationPool.get(0);
	}
	
	public static synchronized void release(InvoiceNotification invoiceNotification){
		try{
		notificationPool.add(invoiceNotification);
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


}
