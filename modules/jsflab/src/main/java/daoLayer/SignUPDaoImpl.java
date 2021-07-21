package daoLayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import notification.SuccessFailureWarnig;
import replyclasses.SignUpReply;
import standardclasses.SignUp;

public class SignUPDaoImpl {

	/*ApplicationContext context  = new ClassPathXmlApplicationContext("BiddingDaoConfiguration.xml");
	
	private DataSource dataSource = (DataSource) context.getBean("dataSource") ;*/
	public SuccessFailureWarnig registerUser(String name,String sur_name,String email_id,String address,String city,String state,String company_name,String designation,String contact_number,String userName,String password){
		
		SuccessFailureWarnig message = new SuccessFailureWarnig(); 
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Insert into BIDDING.SIGNUP (first_name,last_name,contact_num,emailid,address,state,city,designation,created_date,user_name,password) values(?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, sur_name);
			ps.setString(3, contact_number);
			ps.setString(4,email_id);
			ps.setString(5, address);
			ps.setString(6, state);
			ps.setString(7, city);
			ps.setString(8, designation	);
			ps.setDate(9, new Date(0));
			ps.setString(10,userName);
			ps.setString(11, password);
			ps.execute();
			
			ps.close();
			conn.commit();
			message.setStatus("SUCCESS");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is success" );
		} catch (Exception e) {
			message.setStatus("FAILURE");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				message.setStatus("FAILURE");
				message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );

			}
		}
		return message;
	}
	public SignUpReply getallRecords(){
		SignUpReply reply = new SignUpReply();
		List<SignUp> signUp = new ArrayList<SignUp>();
		SuccessFailureWarnig message = new SuccessFailureWarnig(); 
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\Gaurav\\MyDB;create=true");
			/*conn = dataSource.getConnection();*/
			PreparedStatement ps = conn.prepareStatement("Select * from SIG.SIGNUP");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SignUp singUpObject = new SignUp();
				singUpObject.setName(rs.getString("first_name"));
				singUpObject.setSur_name(rs.getString("last_name"));
				singUpObject.setContact_number(rs.getString("contact_num"));
				singUpObject.setEmail_Id(rs.getString("emailid"));
				singUpObject.setAddress(rs.getString("address"));
				singUpObject.setState(rs.getString("state"));
				singUpObject.setCity(rs.getString("city"));
				singUpObject.setDesignation(rs.getString("designation"));
				singUpObject.setDate(rs.getDate("created_date"));
				signUp.add(singUpObject);
			}
			ps.close();
			rs.close();
			message.setStatus("SUCCESS");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is success" );
			reply.setNotifications(message);
			reply.setSignUp(signUp);
		} catch (Exception e) {
			message.setStatus("FAILURE");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			reply.setNotifications(message);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				message.setStatus("FAILURE");
				message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
				reply.setNotifications(message);
			}
		}
		return reply;
	}
	
	public int getUsername(String userName){
		SignUpReply reply = new SignUpReply();
		List<SignUp> signUp = new ArrayList<SignUp>();
		SuccessFailureWarnig message = new SuccessFailureWarnig(); 
		Connection conn = null;
		int count=0;
		
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			/*conn = dataSource.getConnection();*/
			String Query = ("Select count(*) from BIDDING.SIGNUP where user_name =" + "'" +userName+"'");
			PreparedStatement ps = conn.prepareStatement(Query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				
				count++;
			}
		
			ps.close();
			rs.close();
			message.setStatus("SUCCESS");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is success" );
			reply.setNotifications(message);
			reply.setSignUp(signUp);
		} catch (Exception e) {
			message.setStatus("FAILURE");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
			reply.setNotifications(message);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				message.setStatus("FAILURE");
				message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
				reply.setNotifications(message);
			}
		}
		return count;
	}
	
	public String getpassword(String userName){
		
		SuccessFailureWarnig message = new SuccessFailureWarnig(); 
		String password = null;
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.ClientDriver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:derby://localhost:1527/MyDB","admin","123");
			/*conn = dataSource.getConnection();*/
			String Query = ("Select password from BIDDING.SIGNUP where user_name =" +"'" + userName+"'");
			PreparedStatement ps = conn.prepareStatement(Query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				 
				password =(rs.getString("password"));
				
			}
			ps.close();
			rs.close();
			
			
		} catch (Exception e) {
			message.setStatus("FAILURE");
			message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
		
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				message.setStatus("FAILURE");
				message.setMessage(SignUPDaoImpl.class.getName() + " - " + "registerUser is failure/n"+ e.getMessage() );
				
			}
		}
		return password;
	}

}
