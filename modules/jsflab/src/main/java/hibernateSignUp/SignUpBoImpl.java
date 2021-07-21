package hibernateSignUp;

import notification.SuccessFailureWarnig;

public class SignUpBoImpl implements SignUpBo {

	@Override
	public SuccessFailureWarnig registerUser(SignUpObject signup)
			throws Exception {
		SuccessFailureWarnig notification = new SuccessFailureWarnig();
		SignUpEaOLayer signUpEaoLayer = new SignUpEaOLayer();
		notification = signUpEaoLayer.insertIntoLoginTable(signup);
		return notification;
	}

}
