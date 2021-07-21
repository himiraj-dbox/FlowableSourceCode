	package utilityclasses;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.model.UploadedFile;

import boimpl.HireMeTenseFreeBO;
import boimpl.HireMeTenseFreeBOImpl;
import hibernateSignUp.HireMeTileTilesCatalog;
import request.HireMeTilesCatalogRequest;

@ManagedBean(name="fileUploadView")
	@ViewScoped
public class FileUploadUtility {
	
	     
	    private UploadedFile file;
	 
	    public UploadedFile getFile() {
	        return file;
	    }
	 
	    public void setFile(UploadedFile file) {
	        this.file = file;
	    }
	     
	    public void upload() {
	        if(file != null) {
	        	WorkBookUtility wb  = new WorkBookUtility();
	        	try {
	        		List<HireMeTileTilesCatalog> tileCatalog = wb.readBooksFromExcelFile(file.getInputstream());
	        		HireMeTenseFreeBO saveCatalog = new HireMeTenseFreeBOImpl();
	        		saveCatalog.saveTileCatalogHirem(tileCatalog);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
	    }
}
