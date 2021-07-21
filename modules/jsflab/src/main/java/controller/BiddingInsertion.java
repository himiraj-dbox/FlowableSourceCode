package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import replyclasses.BiddingReply;
import standardclasses.Bidding;
import boimpl.BiddingInsertionBO;

@ManagedBean(name="biddinginsertion")
@ViewScoped
public class BiddingInsertion  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String destination="/var/webapp/images/";
	  private String destination2 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images//");
	
	private int bidReferceNo;
	private String productName;
	private Date startingDate;
	private Date endDate;
	private String status;
	private String condition;
	private String locations;
	private String manufacturerName;
	private String warranty;
	private String productId;
	private String itemDexcription;
	private String location;
	private int quantity;
	private String postedBy;
	private String imagePath;
	private String productImagePath;
	private String imagenameFileType;
	private String itemName;
	private List<String> imagesPath = new ArrayList<String>();
	private List<String> galleryImagesPath = new ArrayList<String>();
	public int i = 5;
	private UploadedFile file;
	private double timeStarted;
	private double timeEnd;
	
	
	public BiddingInsertion(){
	FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
	
	public String submitBidding(){
		Bidding biding = new Bidding();
		BiddingReply reply = new BiddingReply();
		biding.setBidReferceNo(getBidReferceNo());
		biding.setCondition(getCondition());
		biding.setItemDexcription(getItemDexcription());
		biding.setLocation(getLocation());
		biding.setManufacturerName(getManufacturerName());
		biding.setProductID(getProductId());
		biding.setStatus(getStatus());
		biding.setWarranty(getWarranty());
		biding.setStartingDate(getStartingDate());
		biding.setEndDate(getEndDate());
		biding.setTimeStarted(getTimeStarted());
		biding.setTimeEnd(getTimeEnd());
		biding.setImagePath(getImagespath());
		biding.setProductName(getProductName());
		biding.setPostedBy(getPostedBy());
		BiddingInsertionBO daoImpl = new BiddingInsertionBO();
		
		reply = daoImpl.insertIntoBiddingHibernate(biding);
		return null;
	}
	private String getImagespath() {
		StringBuilder tempBuilder  = new StringBuilder();
		  i =1;
		for(String tempImages: getImagesPath()){
			i++;
			tempBuilder.append(tempImages);
			
			if(i<=getImagesPath().size()){
				tempBuilder.append(",");
			}
		}
	/*	setProductImagePath(tempBuilder.toString());*/
		return tempBuilder.toString();
	}
	
	public void upload() {  
if(i != 0){
		 if(getBidReferceNo() <=0){
			 FacesMessage msg = new FacesMessage("Success! ", "You can't upload image untill you enter Bid Reference Number" );  
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		else{
	        FacesMessage msg = new FacesMessage("Success! ", getFile().getFileName() + " is uploaded.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        // Do what you want with the file        
	        try {
	            copyFile(getFile().getFileName(), getFile().getInputstream());
	        } catch (Exception e) {
	           // will put in a logger
	        }
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
	            OutputStream out = new FileOutputStream(new File(destination+getBidReferceNo()+"-"+i+"."+tempString.get(0)));
	            // OutputStream out = new FileOutputStream(new File(getBidReferceNo()+i+"."+tempString.get(0)));
	           setProductImagePath(getBidReferceNo()+"-"+i+"."+tempString.get(0));
	           imagesPath.add(""+getBidReferceNo()+"-"+i+"."+tempString.get(0).trim());
	           galleryImagesPath.add(""+getBidReferceNo()+"-"+i+"."+tempString.get(0));
	           setImagenameFileType(destination+getBidReferceNo()+"-"+"."+tempString.get(0));
	             int read = 0;
	             byte[] bytes = new byte[1024];
	           
	             while ((read = in.read(bytes)) != -1) {
	                 out.write(bytes, 0, read);
	             }
	           
	             in.close();
	             out.flush();
	             out.close();
	           
	             i--;
	             } catch (Exception e) {
	             
	             }
	    	
	 }

	public int getBidReferceNo() {
		return bidReferceNo;
	}


	public void setBidReferceNo(int bidReferceNo) {
		this.bidReferceNo = bidReferceNo;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public Date getStartingDate() {
		return startingDate;
	}


	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCondition() {
		return condition;
	}


	public void setCondition(String condition) {
		this.condition = condition;
	}


	public String getLocations() {
		return locations;
	}


	public void setLocations(String locations) {
		this.locations = locations;
	}


	public String getManufacturerName() {
		return manufacturerName;
	}


	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}


	public String getWarranty() {
		return warranty;
	}


	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productID) {
		this.productId = productID;
	}


	public String getItemDexcription() {
		return itemDexcription;
	}


	public void setItemDexcription(String itemDexcription) {
		this.itemDexcription = itemDexcription;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getPostedBy() {
		return postedBy;
	}


	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getProductImagePath() {
		return productImagePath;
	}
	public void setProductImagePath(String productImagePath) {
		this.productImagePath = productImagePath;
	}
	public String getImagenameFileType() {
		return imagenameFileType;
	}
	public void setImagenameFileType(String imagenameFileType) {
		this.imagenameFileType = imagenameFileType;
	}
	public List<String> getImagesPath() {
		return imagesPath;
	}
	public void setImagesPath(List<String> imagesPath) {
		this.imagesPath = imagesPath;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public List<String> getGalleryImagesPath() {
		return galleryImagesPath;
	}
	public void setGalleryImagesPath(List<String> galleryImagesPath) {
		this.galleryImagesPath = galleryImagesPath;
	}

	public String getDestination2() {
		return destination2;
	}

	public void setDestination2(String destination2) {
		this.destination2 = destination2;
	}

	public double getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(double timeStarted) {
		this.timeStarted = timeStarted;
	}

	public double getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(double timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
 
}
