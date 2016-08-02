package dashboard;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserRec {
	private int _uid;
	private String _username;
	private String _password;
	private String _firstname;
	private String _lastname;
	private int _role;
	private String _dbase;
	private String _phone1;
	private String _email;
	private boolean _isAuthenticated;
	
	public int getUid(){
		return _uid;
	}
	public void setUid(int value){
		_uid = value;
	}
	public String getUsername(){
		return _username;
	}
	public void setUsername(String value){
		_username = value;
	}
	public String getPassword(){
		return _password;
	}
	public void setPassword(String password){
		_password = getMD5(password);
	}
	public boolean isAuthenticated(){
		return _isAuthenticated;
	}
	public void setIsAutheticated(boolean value){
		_isAuthenticated = value;
	}
	public void setFirstname(String value){
		_firstname = value;
	}
	public String getFirstname(){
		return _firstname;
	}
	public void setLastname(String value){
		_lastname = value;
	}
	public String getLastname(){
		return _lastname;
	}
	public void setPhone1(String value){
		_phone1 = value;
	}
	public String getPhone1(){
		return _phone1;
	}
	public void setEmail(String value){
		_email = value;
	}
	public String getEmail(){
		return _email;
	}
	public int getRole(){
		return _role;
	}
	public void setRole(int value){
		_role = value;
	}
	public String getDBase(){
		return _dbase;
	}
	public void setDBase(String value){
		_dbase = value;
	}
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
