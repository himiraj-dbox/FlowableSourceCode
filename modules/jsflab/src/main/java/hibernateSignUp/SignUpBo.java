package hibernateSignUp;

import notification.SuccessFailureWarnig;

public interface SignUpBo {
public SuccessFailureWarnig registerUser(SignUpObject signup) throws Exception;

}
