package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import boimpl.HireMeTenseFreeBO;
import boimpl.HireMeTenseFreeBOImpl;
import hibernateSignUp.HireMeTileTilesCatalog;
import hybridObjects.TilesCategory;
import request.FetchTilesRecordRequest;
import request.HireMeTilesCatalogRequest;

@ManagedBean(name="tilesHomepage")
@ViewScoped
public class TilesController {
	
	private List<TilesCategory> tilesCategory;
	private List<HireMeTilesCatalogRequest> tilesCatalog;



	public TilesController(){
		List<String> floorTilesSubCategry = new ArrayList<String>();
		List<String> wallTilesSubCategry = new ArrayList<String>();
		List<String> mosaicTilesSubCategry = new ArrayList<String>();
		floorTilesSubCategry.add("Travertine");
		floorTilesSubCategry.add("Marble");
		floorTilesSubCategry.add("Slate");
		floorTilesSubCategry.add("Ceramic");
		floorTilesSubCategry.add("Porcelain");
		floorTilesSubCategry.add("Faux Wood");
		floorTilesSubCategry.add("Quartize");
		floorTilesSubCategry.add("Granite");
		floorTilesSubCategry.add("Stones and pebbles");
		floorTilesSubCategry.add("Onyx");
		floorTilesSubCategry.add("Cement");
		//wall tile
		wallTilesSubCategry.add("Travertine");
		wallTilesSubCategry.add("Marble");
		wallTilesSubCategry.add("Slate");
		wallTilesSubCategry.add("Ceramic");
		wallTilesSubCategry.add("Porcelain");
		wallTilesSubCategry.add("Faux Wood");
		wallTilesSubCategry.add("Quartize");
		wallTilesSubCategry.add("Granite");
		wallTilesSubCategry.add("Stones and pebbles");
		wallTilesSubCategry.add("Onyx");
		wallTilesSubCategry.add("Cement");
		//moziac tile
		mosaicTilesSubCategry.add("Glass");
		mosaicTilesSubCategry.add("Stone");
		mosaicTilesSubCategry.add("Metal");
		mosaicTilesSubCategry.add("Porcelain M");
		tilesCategory = new ArrayList<TilesCategory>();
		TilesCategory floorTiles = new TilesCategory();
		TilesCategory wallTiles = new TilesCategory();
		TilesCategory mosaics = new TilesCategory();
		floorTiles.setTilesCategoryName("Floor Tiles");
		floorTiles.setTilesCategoryImagePth("floorTilesCategory.jpg");
		floorTiles.setTilesSubCategories(floorTilesSubCategry);
		wallTiles.setTilesCategoryName("Wall Tiles");
		wallTiles.setTilesSubCategories(wallTilesSubCategry);
		mosaics.setTilesCategoryName("Mosaics Tiles");
		mosaics.setTilesSubCategories(mosaicTilesSubCategry);
		tilesCategory.add(floorTiles);
		tilesCategory.add(wallTiles);
		tilesCategory.add(mosaics);

		
		
	}

    public void  onCLickTileCategory(String flootTab,String category ){
    	
    	String a = "a";
    	String b = category.substring(0, 1) + "T";
    	String c = a;
    	HireMeTenseFreeBO hireMeTenseFfree  =  new HireMeTenseFreeBOImpl();
    	FetchTilesRecordRequest request = new FetchTilesRecordRequest();
    	request.setShopType("Tile_shop");
    	request.setTileSubCategory(flootTab);
    	request.setTileType(b);
    	tilesCatalog = hireMeTenseFfree.fetchTilesRecord(request);
    	 /*if(!FacesContext.getCurrentInstance().isPostback()) {*/
        /* }*/
    
    }
    
public String nav(String flootTab,String category ){
    	
    	/*String a = "a";
    	String b = category.substring(0, 1) + "T";
    	String c = a;
    	HireMeTenseFreeBO hireMeTenseFfree  =  new HireMeTenseFreeBOImpl();
    	FetchTilesRecordRequest request = new FetchTilesRecordRequest();
    	request.setShopType("Tile_shop");
    	request.setTileSubCategory(flootTab);
    	request.setTileType(b);
    	tilesCatalog = hireMeTenseFfree.fetchTilesRecord(request);
    	 if(!FacesContext.getCurrentInstance().isPostback()) {
         }*/
    	return "//ContactUs.xhtml?faces-redirect=true";
    }
   public void onCLickTileCategory(ActionEvent event){
    	
    	String a = "a";
    	//String b = category.substring(0, 1) + "T";
    	String c = a;
    	HireMeTenseFreeBO hireMeTenseFfree  =  new HireMeTenseFreeBOImpl();
    	FetchTilesRecordRequest request = new FetchTilesRecordRequest();
    	request.setShopType("Tile_shop");
    	//request.setTileSubCategory(flootTab);
    //	request.setTileType(b);
    	tilesCatalog = hireMeTenseFfree.fetchTilesRecord(request);
    	 /*if(!FacesContext.getCurrentInstance().isPostback()) {*/
        /* }*/
    }

    
	public List<TilesCategory> getTilesCategory() {
		return tilesCategory;
	}


	public void setTilesCategory(List<TilesCategory> tilesCategory) {
		this.tilesCategory = tilesCategory;
	}

	public List<HireMeTilesCatalogRequest> getTilesCatalog() {
		return tilesCatalog;
	}

	public void setTilesCatalog(List<HireMeTilesCatalogRequest> tilesCatalog) {
		this.tilesCatalog = tilesCatalog;
	}

	
	

}
