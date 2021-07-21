package boimpl;

import replyclasses.ProductReply;
import standardclasses.Product;
import executequerry.ExecuteQuery;

public class ProductInsertionBo {
public ProductReply insertProductData(Product product) throws Exception{
	ProductReply reply = new ProductReply();
	ExecuteQuery query = new ExecuteQuery();
	reply = query.insertIntoProduct(reply, product);
	return reply;
}
}
