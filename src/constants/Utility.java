package constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Utility {
	 public static boolean isValidEmailAddress(String email) {
         String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
         java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
         java.util.regex.Matcher m = p.matcher(email);
         return m.matches();
  }
	 public static boolean isValidPhoneNumber(String phoneNo) {
		 if (phoneNo.matches("\\d{10}")) return true;
			//validating phone number with -, . or spaces
			else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
			//validating phone number with extension length from 3 to 5
			else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
			//validating phone number where area code is in braces ()
			else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
			//return false if nothing matches the input
			else return false;
  }
	 public static boolean isDateValid(String date) 
	 {		  String DATE_FORMAT = "MM/dd/yyyy";
	         try {
	             DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	             df.setLenient(false);
	             Date date2=df.parse(date);
	             if (date2.after(new Date())) {
	            	  return false; 
	             }
	             return true;
	         } catch (ParseException e) {
	             return false;
	         }
	 }
}
