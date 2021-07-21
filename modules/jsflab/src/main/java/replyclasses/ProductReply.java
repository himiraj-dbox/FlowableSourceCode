package replyclasses;

import java.util.ArrayList;
import java.util.List;

import notification.SuccessFailureWarnig;
import standardclasses.Product;

public class ProductReply {

	
private SuccessFailureWarnig notification;
private List<Product> product;

public ProductReply(){
	notification = new SuccessFailureWarnig();
	product = new ArrayList<Product>();
	
	
}
public SuccessFailureWarnig getNotification() {
	return notification;
}
public void setNotification(SuccessFailureWarnig notification) {
	this.notification = notification;
}
public List<Product> getProduct() {
	return product;
}
public void setProduct(List<Product> product) {
	this.product = product;
}



}
