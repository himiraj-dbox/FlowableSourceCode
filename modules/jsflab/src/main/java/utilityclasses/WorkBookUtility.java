package utilityclasses;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import hibernateSignUp.HireMeTileTilesCatalog;
import request.HireMeTilesCatalogRequest;

public class WorkBookUtility {
	/*public static void main (String[] args)  {
		try{
		WorkBookUtility workbooktemp = new WorkBookUtility();
		workbooktemp.readBooksFromExcelFile("F:\\hiremetensefree.xlsx");
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}*/
	public List<HireMeTileTilesCatalog> readBooksFromExcelFile(InputStream excelFilePath) throws Exception {
		List<HireMeTileTilesCatalog> listBooks = new ArrayList<HireMeTileTilesCatalog>();
		
		try{
		
	    InputStream inputStream = excelFilePath;
	 
	    Workbook workbook = new XSSFWorkbook(inputStream);
	    Sheet firstSheet = workbook.getSheetAt(0);
	    Iterator<org.apache.poi.ss.usermodel.Row> iterator = firstSheet.iterator();
//Skipping the first row
	    //	 if(iterator.hasNext()){
//		 iterator.next();
//	 }
	    while (iterator.hasNext()) {
	        Row nextRow = (Row) iterator.next();
	        Iterator<Cell> cellIterator = ((org.apache.poi.ss.usermodel.Row) nextRow).cellIterator();
	        HireMeTileTilesCatalog hireMeTilesCatalogRequest = new HireMeTileTilesCatalog();
	 
	        while (cellIterator.hasNext()) {
	            Cell nextCell = cellIterator.next();
	            int columnIndex = nextCell.getColumnIndex();
	 
	           switch (columnIndex) {
	            case 0:
	            	hireMeTilesCatalogRequest.setShopType(nextCell.getStringCellValue());
	                break;
	            case 1:
	            	hireMeTilesCatalogRequest.setSubCategory(nextCell.getStringCellValue());
	                break;
	            case 2:
	            	hireMeTilesCatalogRequest.setThirdCategory(nextCell.getStringCellValue());
	                break;
	            case 3:
	            	//hireMeTilesCatalogRequest.setProductCode(Long.parseLong(nextCell.getStringCellValue()));
	            	break;
	            case 4:
	            	hireMeTilesCatalogRequest.setDimensionCategory(nextCell.getStringCellValue());
	            	break;
	            case 5:
	            	hireMeTilesCatalogRequest.setDescription(nextCell.getStringCellValue());
	            	break;
	            case 6:
	            	hireMeTilesCatalogRequest.setMaterialType(nextCell.getStringCellValue());
	            	break;
	            case 7:
	            	hireMeTilesCatalogRequest.setBrand(nextCell.getStringCellValue());
	            	break;
	            case 8:
	            	hireMeTilesCatalogRequest.setEdgeType(nextCell.getStringCellValue());
	            	break;
	            case 9:
	            	hireMeTilesCatalogRequest.setApplications(nextCell.getStringCellValue());
	            	break;
	            case 10:
	            	hireMeTilesCatalogRequest.setImagePath(nextCell.getStringCellValue());
	            	break;
	            case 11:
	            	hireMeTilesCatalogRequest.setPrice(nextCell.getStringCellValue());
	            	break;
	            default :
	            	break;
	            	
	            }
	 
	 
	        }
	        listBooks.add(hireMeTilesCatalogRequest);
	    }
	 
	    workbook.close();
	    inputStream.close();
	 
		}catch (Exception e) {
			throw new Exception(e);
		}
	    return listBooks;
	}
}
