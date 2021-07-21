package replyclasses;

import java.util.List;

import notification.SuccessFailureWarnig;
import standardclasses.SignUp;



public class SignUpReply {
	private SuccessFailureWarnig notifications;
	private List<SignUp> signUp;

	public SuccessFailureWarnig getNotifications() {
		return notifications;
	}
	public void setNotifications(SuccessFailureWarnig notifications) {
		this.notifications = notifications;
	}
	public List<SignUp> getSignUp() {
		return signUp;
	}
	public void setSignUp(List<SignUp> signUp) {
		this.signUp = signUp;
	}
}
