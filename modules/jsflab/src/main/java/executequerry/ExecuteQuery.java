package executequerry;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import replyclasses.BiddingAppravalSystemReply;
import replyclasses.BiddingKeeperReply;
import replyclasses.BiddingReply;
import replyclasses.BiddingUserVerificationReply;
import replyclasses.ProductReply;
import standardclasses.Bidding;
import standardclasses.BiddingAppravalSystem;
import standardclasses.BiddingKeeper;
import standardclasses.BiddingUserVerification;
import standardclasses.Product;



public class ExecuteQuery {
	
	public ProductReply insertIntoProduct(ProductReply reply, Product product){

		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.PRODUCT (product_id,product_image_path,manufacturer_image_path,basic_information,product_description,product_detail_overview,product_manufacturer_id,product_manufacturer_name,variant_id,keyword,hierarchy,rating,price,availability,number_sold,no_times_searched) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			//ps.setInt(1, id);
			ps.setString(1, product.getProductId());
			ps.setString(2, product.getProductImagePath());
			ps.setString(3, product.getManufacturerImagepath());
			ps.setString(4, product.getBasicInformation());
			ps.setString(5, product.getProductDescription());
			ps.setString(6, product.getProductDetail());
			ps.setString(7, product.getProductManufacturerId());
			ps.setString(8, product.getProductmanufacturerName());
			ps.setString(9, product.getVariantId());
			ps.setString(10, product.getKeywords());
			ps.setString(11, product.getHierarchy());
			ps.setInt(12, product.getRating());
			ps.setInt(13, product.getPrice());
			ps.setString(14, product.getAvailability());
			ps.setInt(15, product.getNumberOfSold());
			ps.setInt(16, product.getNumOfTimeSearched());
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;

	}

	public ProductReply getProduct(ProductReply reply, Product productObject,
			Boolean customSearch, String queryApahe) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query =customSearch? "Select * from BIDDING.PRODUCT "+"where "+queryApahe :"Select * from BIDDING.PRODUCT";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				productObject = new Product();
				productObject.setAvailability(rs.getString("availability"));
				productObject.setBasicInformation(rs.getString("basic_information"));
				productObject.setHierarchy(rs.getString("hierarchy"));
				productObject.setKeywords(rs.getString("keyword"));
				productObject.setManufacturerImagepath(rs.getString("manufacturer_image_path"));
				productObject.setNumberOfSold(rs.getInt("number_sold"));
				productObject.setNumOfTimeSearched(rs.getInt("no_times_searched"));
				productObject.setPrice(rs.getInt("price"));
				productObject.setProductDescription(rs.getString("product_description"));
				productObject.setProductDetail(rs.getString("product_detail_overview"));
				productObject.setProductId(rs.getString("product_id"));
				productObject.setProductImagePath(rs.getString("product_image_path"));
				productObject.setProductManufacturerId(rs.getString("product_manufacturer_id"));
				productObject.setProductmanufacturerName(rs.getString("product_manufacturer_name"));
				productObject.setRating(rs.getInt("rating"));
				productObject.setVariantId(rs.getString("variant_id"));
				reply.getProduct().add(productObject);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
	}

	public BiddingReply insertIntoBidding(BiddingReply reply, Bidding bidding) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.Bidding (bid_reference,itemdescription,status,condition,posted_by,location,product_manufacturer_name,warranty,product_id,quantity,starting_date,end_date,images_associated,product_name,start_time,end_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			//ps.setInt(1, id);
			ps.setInt(1, bidding.getBidReferceNo());
		
			ps.setString(2, bidding.getItemDexcription());
			ps.setString(3, bidding.getStatus());
			ps.setString(4,bidding.getCondition());
			ps.setString(5,bidding.getProductID());
			ps.setString(6, bidding.getLocation());
			ps.setString(7,bidding.getManufacturerName());
			ps.setString(8, bidding.getWarranty());
			ps.setString(9,bidding.getProductID());
			ps.setInt(10,bidding.getQuantity());
			ps.setDate(11, new Date(bidding.getStartingDate().getYear(),bidding.getStartingDate().getMonth(),bidding.getStartingDate().getDate()));
			ps.setDate(12, new Date(bidding.getEndDate().getYear(),bidding.getEndDate().getMonth(),bidding.getEndDate().getDate()));
			ps.setString(13,bidding.getImagePath());
			ps.setString(14,bidding.getProductName());
			ps.setDouble(15,bidding.getTimeStarted());
			ps.setDouble(16,bidding.getTimeEnd());
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;
	}

	public BiddingReply getFromBidding(BiddingReply reply, Bidding bidding,
			Boolean customSearch, String queryApahe) {
		
			Connection conn = null;
			String driver = "org.apache.derby.jdbc.ClientDriver";
			String query =customSearch? "Select * from BIDDING.Bidding "+"where "+queryApahe :"Select * from BIDDING.Bidding";
			try {
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
				
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					
					bidding = new Bidding();
					bidding.setBidReferceNo(rs.getInt("bid_reference"));
					bidding.setProductName(rs.getString("product_nameo"));
					bidding.setStartingDate(rs.getDate("starting_date"));
					bidding.setEndDate(rs.getDate("end_date"));
					bidding.setItemDexcription(rs.getString("itemdescription"));
					bidding.setStatus(rs.getString("status"));
					bidding.setCondition(rs.getString("condition"));
					bidding.setPostedBy(rs.getString("posted_by"));
					bidding.setLocation(rs.getString("location"));
					bidding.setManufacturerName(rs.getString("product_manufacturer_name"));
					bidding.setWarranty(rs.getString("warranty"));
					bidding.setProductID(rs.getString("product_id"));
					bidding.setQuantity(rs.getInt("quantity"));
					bidding.setImagePath(rs.getString("images_associated"));
					reply.getBidding().add(bidding);
				}
				ps.close();
				rs.close();
				
				reply.getNotification().setStatus("SUCCESS");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
			} catch (Exception e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

			} finally{
				try {
					conn.close();
				} catch (SQLException e) {
					reply.getNotification().setStatus("FAILURE");
					reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
				}
			}
			return reply;
		

}
	
	//get bid reference number 
	
	
	public int getBidReferenceNumber(int bidreferenceNumber
			) {
		
			Connection conn = null;
			String driver = "org.apache.derby.jdbc.ClientDriver";
			String query ="Select * from BIDDING.Bidding "+"where Bid_reference = "+bidreferenceNumber ;
			int reply = -1;
			try {
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
				
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					
					
					reply =(rs.getInt("bid_reference"));
					
				}
				ps.close();
				rs.close();
				

			} catch (Exception e) {
				

			} finally{
				try {
					conn.close();
				} catch (SQLException e) {
					
				}
			}
			return reply;
		

}
	public BiddingReply getAllFromBidding(BiddingReply reply, Bidding bidding) {
		
			Connection conn = null;
			String driver = "org.apache.derby.jdbc.ClientDriver";
			String query ="Select * from BIDDING.Bidding";
			try {
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
				
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					
					bidding = new Bidding();
					bidding.setBidReferceNo(rs.getInt("bid_reference"));
					bidding.setProductName(rs.getString("product_name"));
					bidding.setStartingDate(rs.getDate("starting_date"));
					bidding.setEndDate(rs.getDate("end_date"));
					/*bidding.setItemDexcription(rs.getString("itemdescription"));
					bidding.setStatus(rs.getString("status"));
					bidding.setCondition(rs.getString("condition"));
					bidding.setPostedBy(rs.getString("posted_by"));
					bidding.setLocation(rs.getString("location"));
					bidding.setManufacturerName(rs.getString("product_manufacturer_name"));
					bidding.setWarranty(rs.getString("warranty"));
					bidding.setProductID(rs.getString("product_id"));
					bidding.setQuantity(rs.getInt("quantity"));
					bidding.setImagePath(rs.getString("images_associated"));*/
					bidding.setTimeStarted(rs.getDouble("start_time"));
					bidding.setTimeEnd(rs.getDouble("end_time"));
					reply.getBidding().add(bidding);
				}
				ps.close();
				rs.close();
				
				reply.getNotification().setStatus("SUCCESS");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
			} catch (Exception e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

			} finally{
				try {
					conn.close();
				} catch (SQLException e) {
					reply.getNotification().setStatus("FAILURE");
					reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
				}
			}
			return reply;
		

}

	public BiddingKeeperReply insertIntoBiddingKeeper(BiddingKeeperReply reply, BiddingKeeper bidding) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.BiddingHistory (bid_reference,price,bid_by,increase_by) values(?,?,?,?)");
			//ps.setInt(1, id);
			ps.setInt(1, bidding.getBidReferenceNumber());
			ps.setInt(2, bidding.getPrice());
			ps.setString(3, bidding.getUserId());
			ps.setInt(4,bidding.getIncreaseBy());
			
			
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;
	}

	public BiddingKeeperReply getAllFromBiddingKeeper(BiddingKeeperReply reply,
			BiddingKeeper bidding) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.BiddingHistory";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				bidding = new BiddingKeeper();
				bidding.setBidReferenceNumber(rs.getInt("bid_reference"));
				bidding.setPrice(rs.getInt("price"));
				bidding.setUserId(rs.getString("bid_by"));
				bidding.setIncreaseBy(rs.getInt("increase_by"));
				reply.getBidKeeperList().add(bidding);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
	}
	
	public BiddingKeeperReply getMaxAmountFromBiddingKeeper(BiddingKeeperReply reply,BiddingKeeper bidding,int bidReferenceNumber) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="SELECT * FROM Bidding.BIDDINGHISTORY where bid_reference = "+bidReferenceNumber +" and price = (SELECT MAX(price) FROM Bidding.BIDDINGHISTORY where bid_reference = "+bidReferenceNumber+")";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				bidding = new BiddingKeeper();
				bidding.setBidReferenceNumber(rs.getInt("bid_reference"));
				bidding.setPrice(rs.getInt("price"));
				bidding.setUserId(rs.getString("bid_by"));
				bidding.setIncreaseBy(rs.getInt("increase_by"));
				reply.getBidKeeperList().add(bidding);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
	}
	

	public BiddingReply getAllFromBiddingWithBid(BiddingReply reply,
			Bidding bidding) {

		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding where bid_reference = "+bidding.getBidReferceNo() ;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				bidding = new Bidding();
				bidding.setBidReferceNo(rs.getInt("bid_reference"));
				bidding.setProductName(rs.getString("product_name"));
				bidding.setStartingDate(rs.getDate("starting_date"));
				bidding.setEndDate(rs.getDate("end_date"));
				/*bidding.setItemDexcription(rs.getString("itemdescription"));
				bidding.setStatus(rs.getString("status"));
				bidding.setCondition(rs.getString("condition"));
				bidding.setPostedBy(rs.getString("posted_by"));
				bidding.setLocation(rs.getString("location"));
				bidding.setManufacturerName(rs.getString("product_manufacturer_name"));
				bidding.setWarranty(rs.getString("warranty"));
				bidding.setProductID(rs.getString("product_id"));
				bidding.setQuantity(rs.getInt("quantity"));
				bidding.setImagePath(rs.getString("images_associated"));*/
				bidding.setTimeStarted(rs.getDouble("start_time"));
				bidding.setTimeEnd(rs.getDouble("end_time"));
				reply.getBidding().add(bidding);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
		
	}

	public BiddingUserVerificationReply insertIntoBiddingVerification(
			BiddingUserVerificationReply reply, BiddingUserVerification bid) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.Bidding_User (user_name,designation,office_address,service_tax) values(?,?,?,?)");
			//ps.setInt(1, id);
			ps.setString(1, bid.getUserName());
			ps.setString(2, bid.getDesignation());
			ps.setString(3, bid.getOfficeAddress());
			ps.setString(4, bid.getServiceTaxNumber());
			
			
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;
		
	}
	
	public BiddingUserVerificationReply getSelectAllFromBiddingVerification(
			BiddingUserVerificationReply reply,
			BiddingUserVerification biddingUserVerification, String username) {


		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding_user";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				biddingUserVerification = new BiddingUserVerification();
				biddingUserVerification.setUserName(rs.getString("user_name"));
				biddingUserVerification.setVerfiedUser(rs.getString("verified"));
				biddingUserVerification.setTruestScore(rs.getInt("trust_score"));
				biddingUserVerification.setServiceTaxNumber(rs.getString("service_tax"));
				biddingUserVerification.setOfficeAddress(rs.getString("office_address"));
				biddingUserVerification.setDesignation(rs.getString("designation"));
				reply.getProduct().add(biddingUserVerification);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;	
	}
	
	
	/*UPDATE EMPLOYEE
	  SET JOB=NULL, SALARY=0, BONUS=0, COMM=0
	  WHERE WORKDEPT = 'E21' AND JOB <> 'MANAGER'*/
	public BiddingUserVerificationReply updateBiddingVerification(
			BiddingUserVerificationReply reply,String verified, int rating, String userName) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("UPDATE BIDDING.Bidding_User SET varified = '" +verified+"'"+",trust_score = "+rating +" where user_name = '"+userName+"'" );
			//ps.setInt(1, id);
			
			
			
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;
		
	}
	public BiddingUserVerificationReply getAllFromBiddingVerification(
			BiddingUserVerificationReply reply,
			BiddingUserVerification biddingUserVerification, String username) {


		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding_user where user_name = '"+username+"'" ;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				biddingUserVerification = new BiddingUserVerification();
				biddingUserVerification.setUserName(rs.getString("user_name"));
				biddingUserVerification.setVerfiedUser(rs.getString("varified"));
				biddingUserVerification.setTruestScore(rs.getInt("trust_score"));
				biddingUserVerification.setServiceTaxNumber(rs.getString("service_tax"));
				biddingUserVerification.setOfficeAddress(rs.getString("office_address"));
				biddingUserVerification.setDesignation(rs.getString("designation"));
				reply.getProduct().add(biddingUserVerification);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
		
	
	
	}
	public BiddingUserVerificationReply getSelectAllFromBiddingVerification(
			BiddingUserVerificationReply reply,
			BiddingUserVerification biddingUserVerification) {


		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding_USER";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				biddingUserVerification = new BiddingUserVerification();
				biddingUserVerification.setUserName(rs.getString("user_name"));
				biddingUserVerification.setVerfiedUser(rs.getString("varified"));
				biddingUserVerification.setTruestScore(rs.getInt("trust_score"));
				biddingUserVerification.setServiceTaxNumber(rs.getString("service_tax"));
				biddingUserVerification.setOfficeAddress(rs.getString("office_address"));
				biddingUserVerification.setDesignation(rs.getString("designation"));
				reply.getProduct().add(biddingUserVerification);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
		
	
	
	}

	public BiddingAppravalSystemReply getAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply,
			BiddingAppravalSystem biddingAppravalSystem, String userName,
			int bidReferenceNumber) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding_approval_system where user_name = '"+userName+"' and bid_reference = " + bidReferenceNumber ;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				biddingAppravalSystem = new BiddingAppravalSystem();
				biddingAppravalSystem.setBidReferenceNumber(rs.getInt("bid_reference"));
				biddingAppravalSystem.setUserName(rs.getString("user_name"));
				biddingAppravalSystem.setUserAproved(rs.getString("user_approved"));
				biddingAppravalSystem.setComments(rs.getString("comments"));
				
				reply.getBiddingAppravalSystem().add(biddingAppravalSystem);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
		
	
	}

	public BiddingAppravalSystemReply getSelectAllFromBiddingAppravalSystem(
			BiddingAppravalSystemReply reply,
			BiddingAppravalSystem biddingAppravalSystem) {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String query ="Select * from BIDDING.Bidding_approval_system";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				biddingAppravalSystem = new BiddingAppravalSystem();
				biddingAppravalSystem.setBidReferenceNumber(rs.getInt("bid_reference"));
				biddingAppravalSystem.setUserName(rs.getString("user_name"));
				biddingAppravalSystem.setUserAproved(rs.getString("user_approved"));
				biddingAppravalSystem.setComments(rs.getString("comments"));
				
				reply.getBiddingAppravalSystem().add(biddingAppravalSystem);
			}
			ps.close();
			rs.close();
			
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			}
		}
		return reply;
		
	
	}
	
	public BiddingAppravalSystemReply insertIntoBiddingAppravalSystem(
			BiddingAppravalSystemReply reply,
			BiddingAppravalSystem bidAppravalSystem) {

		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.Bidding_approval_system (bid_reference,user_name,user_approved,comments) values(?,?,?,?)");
			//ps.setInt(1, id);
			ps.setInt(1, bidAppravalSystem.getBidReferenceNumber());
			ps.setString(2, bidAppravalSystem.getUserName());
			ps.setString(3, bidAppravalSystem.getUserAproved());
			ps.setString(4, bidAppravalSystem.getComments());
			
			
			ps.execute();
			ps.close();
			conn.commit();
			reply.getNotification().setStatus("SUCCESS");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Bid Approval  inserted status: success" );
		} catch (Exception e) {
			reply.getNotification().setStatus("FAILURE");
			reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status: failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				reply.getNotification().setStatus("FAILURE");
				reply.getNotification().setMessage(ExecuteQuery.class.getName() + " - " + "Product inserted status:"+ e.getMessage() );

			}
		}

		return reply;
		
	
	}
}


