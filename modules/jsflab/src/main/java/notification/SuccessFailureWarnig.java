package notification;

public class SuccessFailureWarnig {
	private String status = new String();
	private String message = new String();
	private Integer numeric = 0;
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
	public Integer getNumeric() {
		return numeric;
	}
	public void setNumeric(Integer numeric) {
		this.numeric = numeric;
	}
}
