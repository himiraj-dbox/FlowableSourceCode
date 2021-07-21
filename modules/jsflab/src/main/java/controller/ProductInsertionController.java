package controller;

import hybridObjects.KeyValue;
import hybridObjects.simpleString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import replyclasses.ProductReply;
import standardclasses.Product;
import boimpl.ProductInsertionBo;


@ManagedBean(name="productinsertions")
@ViewScoped

public class ProductInsertionController implements Serializable{
	private String productId;
	private String productImagePath;
	private String  manufacturerImagepath;
	private String basicInformation;
	private String productDescription;
	private String productDetail;
	private String productManufacturerId;
	private String productmanufacturerName;
	private String variantId;
	private String keywords;
	private String hierarchy;
	private int rating;
	private int price;
	private String availability;
	private int numberOfSold;
	private int numOfTimeSearched;
	private List<KeyValue> basicInformationList;
	private List<KeyValue> detailDescriptionList;
	private  String destination="/jsflab/src/main/resources/imagesProduct/";
	private List<simpleString> keywordsList;
	private UploadedFile uploadedFile;
	private String imagenameFileType;
	 
	 private UploadedFile file;
	 
	    public UploadedFile getFile() {
	        return uploadedFile;
	    }
	 
	    public void setFile(UploadedFile file) {
	        this.uploadedFile = file;
	    }
     
 public void upload(FileUploadEvent event) {  
	 if(getProductId() == null || getProductId().isEmpty() ){
		 FacesMessage msg = new FacesMessage("Success! ", "You can't upload image untill you enter product ID" );  
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	else{
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        // Do what you want with the file        
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (Exception e) {
           // will put in a logger
        }
	}
    }  
    public void copyFile(String fileName, InputStream in) {
    	
        try {
        	
           
             // write the inputStream to a FileOutputStream
        	 List<String> tempString = new ArrayList<String>();
        	 StringTokenizer st = new StringTokenizer(fileName,".");
        	 while(st.hasMoreTokens()){
         		tempString.add(st.nextToken());
         	}
        	 Collections.reverse(tempString);
             OutputStream out = new FileOutputStream(new File(destination+getProductId()+"."+tempString.get(0)));
           setProductImagePath(getProductId()+"."+tempString.get(0));
           setImagenameFileType(destination+getProductId()+"."+tempString.get(0));
             int read = 0;
             byte[] bytes = new byte[1024];
           
             while ((read = in.read(bytes)) != -1) {
                 out.write(bytes, 0, read);
             }
           
             in.close();
             out.flush();
             out.close();
           
             
             } catch (Exception e) {
             
             }
    	
 }

	public  ProductInsertionController(){
		
		basicInformationList = new ArrayList<KeyValue>();
		
		basicInformationList.add(new KeyValue());
		detailDescriptionList = new ArrayList<KeyValue>();
		detailDescriptionList.add(new KeyValue());
		keywordsList = new ArrayList<simpleString>();
		
		keywordsList.add(new simpleString());
		
	}
	
	public String onClickSubmit(){
		 if(file != null) {
	            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
		Product product = new Product();
		ProductReply reply = new ProductReply();
		Map<String,String> basicDescription = new  HashMap<String, String>();
		//Setting Basic Information
		for(KeyValue temp : basicInformationList){
			basicDescription.put(temp.getKey(), temp.getValue());
		}
			Set<String> keySet = basicDescription.keySet() ;
			 int size = keySet.size();
			 int i =1;
			 StringBuilder mappingBasicDescription = new StringBuilder();
			for(String map :keySet){
				i++;
				
				mappingBasicDescription.append("(");
				mappingBasicDescription.append(map);
				mappingBasicDescription.append("-");
				mappingBasicDescription.append(basicDescription.get(map));
				mappingBasicDescription.append(")");
				if(i<=size){
					mappingBasicDescription.append(",");
				}
				
			}
			product.setBasicInformation(mappingBasicDescription.toString());
			//Setting Detail Description
			basicDescription = new  HashMap<String, String>();
			for(KeyValue temp2 : detailDescriptionList){
				basicDescription.put(temp2.getKey(), temp2.getValue());
			}
				Set<String> keySet2 = basicDescription.keySet() ;
				  size = keySet2.size();
				  i =1;
				  mappingBasicDescription = new StringBuilder();
				for(String map :keySet2){
					i++;
					
					mappingBasicDescription.append("(");
					mappingBasicDescription.append(map);
					mappingBasicDescription.append("-");
					mappingBasicDescription.append(basicDescription.get(map));
					mappingBasicDescription.append(")");
					if(i<=size){
						mappingBasicDescription.append(",");
					}
					
				}
				product.setProductDetail(mappingBasicDescription.toString());
		
		// Setting keywords
		i=0;
		mappingBasicDescription = new StringBuilder();
		for(simpleString temp3: keywordsList){
			if(i!=0){
				mappingBasicDescription.append(",");
			}
			mappingBasicDescription.append(temp3.getStringVariable());
		}
		product.setKeywords(mappingBasicDescription.toString());
		product.setProductId(getProductId());
		product.setProductManufacturerId(getProductManufacturerId());;
		product.setProductmanufacturerName(getProductmanufacturerName());
		product.setProductDescription(getProductDescription());
		product.setPrice(getPrice());
		product.setProductImagePath(getProductImagePath());
		ProductInsertionBo boImpl = new ProductInsertionBo();
		try {
			reply = boImpl.insertProductData(product);
		} catch (Exception e) {
			
			// Will implement it when I will put logger in the application
		}
		return null;
	}
	public String addBasicInformation(){
		
		basicInformationList.add(new KeyValue());
	return null;
	}
	public void addDetailDescription(){
		detailDescriptionList.add(new KeyValue());
	}
	public void addKeywords(){
		keywordsList.add(new simpleString());
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductImagePath() {
		return productImagePath;
	}
	public void setProductImagePath(String productImagePath) {
		this.productImagePath = productImagePath;
	}
	public String getManufacturerImagepath() {
		return manufacturerImagepath;
	}
	public void setManufacturerImagepath(String manufacturerImagepath) {
		this.manufacturerImagepath = manufacturerImagepath;
	}
	public String getBasicInformation() {
		return basicInformation;
	}
	public void setBasicInformation(String basicInformation) {
		this.basicInformation = basicInformation;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	public String getProductManufacturerId() {
		return productManufacturerId;
	}
	public void setProductManufacturerId(String productManufacturerId) {
		this.productManufacturerId = productManufacturerId;
	}
	public String getProductmanufacturerName() {
		return productmanufacturerName;
	}
	public void setProductmanufacturerName(String productmanufacturerName) {
		this.productmanufacturerName = productmanufacturerName;
	}
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public int getNumberOfSold() {
		return numberOfSold;
	}
	public void setNumberOfSold(int numberOfSold) {
		this.numberOfSold = numberOfSold;
	}
	public int getNumOfTimeSearched() {
		return numOfTimeSearched;
	}
	public void setNumOfTimeSearched(int numOfTimeSearched) {
		this.numOfTimeSearched = numOfTimeSearched;
	}
	public List<KeyValue> getBasicInformationList() {
		return basicInformationList;
	}
	public void setBasicInformationList(List<KeyValue> basicInformationList) {
		this.basicInformationList = basicInformationList;
	}
	public List<KeyValue> getDetailDescriptionList() {
		return detailDescriptionList;
	}
	public void setDetailDescriptionList(List<KeyValue> detailDescriptionList) {
		this.detailDescriptionList = detailDescriptionList;
	}
	public List<simpleString> getKeywordsList() {
		return keywordsList;
	}
	public void setKeywordsList(List<simpleString> keywordsList) {
		this.keywordsList = keywordsList;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getImagenameFileType() {
		return imagenameFileType;
	}

	public void setImagenameFileType(String imagenameFileType) {
		this.imagenameFileType = imagenameFileType;
	}
	

}
