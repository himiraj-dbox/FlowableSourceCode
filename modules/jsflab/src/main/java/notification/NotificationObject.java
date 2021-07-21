package notification;

import org.apache.log4j.Logger;

public class NotificationObject {
	final static Logger logger = Logger.getLogger(InvoiceNotification.class);
	private String status;
	private String message;
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
