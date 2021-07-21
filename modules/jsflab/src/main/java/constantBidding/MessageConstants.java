package constantBidding;

import javax.ws.rs.core.Response.Status;

public final class MessageConstants {
public static final String MOBILENUMBER = "MOBILE_NUMBER";
public static final String NOTSTARTEDCODE = "NS";
public static final String SHOPTYPE = "shopType";
public static final String SUBCATEGORY = "subCategory";
public static final String THIRDCATEGORY = "thirdCategory";
public static final String DUPLICATE_RECORD = "401";
public static final int DUPLICATE_RECORD_INT = Integer.parseInt(DUPLICATE_RECORD);
public static final String DUPLICATEINVOICEMESSAGE = "Record already exist for -";
public static final String INVOICE_OWNER_USERNAMEMESSAGE = " User Name: ";
public static final String INVOICE_OWNER_MOBILE_MESSAGE_EXIST = "Mobile Number Already Exist For User :";
public static final String INVOICE_OWNER_EMAIL_MESSAGE_EXIST = "Email ID Already Exist For User :";
public static final Integer INVOICE_SUCCESS_CODE = 200;
public static final Integer INVOICE_INVALID_CREDENTIALS = 402;
public  static String SUCCESS= "SUCCESS";
public  static String FAILURE = "FAILURE";
public  static String ERROR= "ERROR";
public static String USERNAME = "userName";
public static String BIDREFERENCENUMBER = "bidReferenceNumber";
public static String BIDREFERENCENUMBERHISTORY = "bidRefernceNumber";
public static String USERNAMEVALIDATION = " User Name is already present, contact us If you have forgot the password";
public static String USERNAMEMPTYCHECK = " User Name is mendatory";
public static String NORecordFound = " No Record Found";
public static String RECORDFOUND = "Record found";
public static String WARNIG = "Warning";
public static String GENERICERROR ="Oops Something went wrong with our system, we will be fixing the issue soon. Try refreshing the page and if problem persists please contact us";
public static String USERSAVED ="You have been Registeres to the system Successfully.";
public static String PASSWORDNOTMATCHED = "Password did not match, contact us If you have forgot the password";
public static String BIDDINGSAVED ="Bid Succesfully Created.";
public static String Designation ="designation";
public static String OFFICEADDRESS ="officeAddress";
public static String SERVICETAX ="serviceTax";
public static String VERIFID ="verified";
public static String TRUSTSCORE ="trust_score";
public static String OFFICEADDRESSUSER ="office_address";
public static String UPDATERECORD ="Record updated Succesfully";


}
