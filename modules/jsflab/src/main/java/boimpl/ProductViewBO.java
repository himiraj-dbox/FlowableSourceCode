package boimpl;

import java.util.ArrayList;
import java.util.List;

import executequerry.ExecuteQuery;
import notification.SuccessFailureWarnig;
import replyclasses.ProductReply;
import standardclasses.Product;

public class ProductViewBO {
public ProductReply getFromProductTable(Boolean customSearch,String queryApahe){
	ProductReply reply = new ProductReply();
	Product product = new Product();
	SuccessFailureWarnig notifications = new SuccessFailureWarnig();
	List<Product> products = new ArrayList<Product>();
	reply.setNotification(notifications);
	reply.setProduct(products);
	ExecuteQuery query = new ExecuteQuery();
	reply = query.getProduct(reply, product,customSearch,queryApahe);
	
	
	return reply;
}
}
