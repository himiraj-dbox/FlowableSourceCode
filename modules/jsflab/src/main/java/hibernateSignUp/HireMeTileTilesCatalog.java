package hibernateSignUp;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hire_tile_catalog")
public class HireMeTileTilesCatalog {
	private String shopType;
	private String subCategory;
	private String thirdCategory;
	private long productCode;
	private String dimensionCategory;
	private String description;
	private String materialType;
	private String brand;
	private String edgeType;
	private String applications;
	private String imagePath;
	private String price;
	
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="product_code")
	public long getProductCode() {
		return productCode;
	}
	public void setProductCode(long productCode) {
		this.productCode = productCode;
	}
	@Column(name="shop_type")
	public String getShopType() {
		return shopType;
	}
	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	
	@Column(name="sub_category")
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	@Column(name="third_category")
	public String getThirdCategory() {
		return thirdCategory;
	}
	public void setThirdCategory(String thirdCategory) {
		this.thirdCategory = thirdCategory;
	}
	
	@Column(name="dimention_category")
	public String getDimensionCategory() {
		return dimensionCategory;
	}
	public void setDimensionCategory(String dimensionCategory) {
		this.dimensionCategory = dimensionCategory;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="material_type")
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	
	@Column(name="brand")
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Column(name="edge_type")
	public String getEdgeType() {
		return edgeType;
	}
	public void setEdgeType(String edgeType) {
		this.edgeType = edgeType;
	}
	
	@Column(name="application")
	public String getApplications() {
		return applications;
	}
	public void setApplications(String applications) {
		this.applications = applications;
	}
	
	@Column(name="images_path")
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Column(name="price")
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

}
