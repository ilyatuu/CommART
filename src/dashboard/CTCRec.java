package dashboard;

public class CTCRec {
	private String _Id;
	private String _recId;
	private boolean _success;
	private String _message;

	public String getId(){
		return _Id;
	}
	public void setId(String value){
		_Id = value;
	}
	public String getRecId(){
		return _recId;
	}
	public void setRecId(String value){
		_recId = value;
	}
	public boolean getSuccess(){
		return _success;
	}
	public void setSuccess(boolean value){
		_success = value;
	}
	public String getMessage(){
		return _message;
	}
	public void setMessage(String value){
		_message = value;
	}
	
}
