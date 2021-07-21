package replyclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notification.SuccessFailureWarnig;
import standardclasses.BiddingUserVerification;
import standardclasses.Product;

public class BiddingUserVerificationReply {
	private SuccessFailureWarnig notification;
	private List<BiddingUserVerification> product;
	private Map<String,String> parameterPassing;

	public BiddingUserVerificationReply(){
		notification = new SuccessFailureWarnig();
		product = new ArrayList<BiddingUserVerification>();
		parameterPassing = new HashMap<String, String>();
		
	}

	public SuccessFailureWarnig getNotification() {
		return notification;
	}

	public void setNotification(SuccessFailureWarnig notification) {
		this.notification = notification;
	}

	public List<BiddingUserVerification> getProduct() {
		return product;
	}

	public void setProduct(List<BiddingUserVerification> product) {
		this.product = product;
	}

	public Map<String, String> getParameterPassing() {
		return parameterPassing;
	}

	public void setParameterPassing(Map<String, String> parameterPassing) {
		this.parameterPassing = parameterPassing;
	}
}
