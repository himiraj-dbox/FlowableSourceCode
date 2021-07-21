package controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import replyclasses.ProductReply;
import standardclasses.Product;
import boimpl.ProductViewBO;

@ManagedBean(name="productsearch")
@ViewScoped
public class ProductSearch {
private String apacheWhereQuery;
private boolean enableQuery;
private List<Product> productList;
private  String destination="/jsflab/src/main/resources/imagesProduct";
@PostConstruct
	public void init(){
	
	ProductViewBO boImpl = new ProductViewBO();
	ProductReply reply =boImpl.getFromProductTable(enableQuery,apacheWhereQuery);
	if(reply.getNotification().getStatus().equalsIgnoreCase("SUCCESS")){
	setProductList(reply.getProduct());
	}else{
		//show Erro on page Type of Error too	
	}
	
}

public String getApacheWhereQuery() {
	return apacheWhereQuery;
}

public void setApacheWhereQuery(String apacheWhereQuery) {
	this.apacheWhereQuery = apacheWhereQuery;
}

public boolean isEnableQuery() {
	return enableQuery;
}

public void setEnableQuery(boolean enableQuery) {
	this.enableQuery = enableQuery;
}

public List<Product> getProductList() {
	return productList;
}

public void setProductList(List<Product> productList) {
	this.productList = productList;
}

public String getDestination() {
	return destination;
}

public void setDestination(String destination) {
	this.destination = destination;
}
}
