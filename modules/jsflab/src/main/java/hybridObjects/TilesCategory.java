package hybridObjects;

import java.util.List;

public class TilesCategory {

	private String tilesCategoryCode;
	private String tilesCategoryName;
	private String tilesCategoryImagePth;
	private List<String> tilesSubCategories;
	
	public String getTilesCategoryCode() {
		return tilesCategoryCode;
	}
	public void setTilesCategoryCode(String tilesCategoryCode) {
		this.tilesCategoryCode = tilesCategoryCode;
	}
	public String getTilesCategoryName() {
		return tilesCategoryName;
	}
	public void setTilesCategoryName(String tilesCategoryName) {
		this.tilesCategoryName = tilesCategoryName;
	}
	public String getTilesCategoryImagePth() {
		return tilesCategoryImagePth;
	}
	public void setTilesCategoryImagePth(String tilesCategoryImagePth) {
		this.tilesCategoryImagePth = tilesCategoryImagePth;
	}
	public List<String> getTilesSubCategories() {
		return tilesSubCategories;
	}
	public void setTilesSubCategories(List<String> tilesSubCategories) {
		this.tilesSubCategories = tilesSubCategories;
	}
}
