package dashboard;

public class ViroRec {
	private String _results;
	private String _recid;
	private int _submitedBy;
	private String _patientId;
	private String _comments;
	private String _type;
	private String _quality;
	private String _submitedOn;
	
	public String getComments(){
		return _comments;
	}
	public void setComments(String value){
		_comments = value;
	}
	public String getType(){
		return _type;
	}
	public void setType(String value){
		_type = value;
	}
	public String getQuality(){
		return _quality;
	}
	public void setQuality(String value){
		_quality = value;
	}
	public String getRecId(){
		return _recid;
	}
	public void setRecId(String value){
		_recid = value;
	}
	public void setResults(String value){
		_results = value;
	}
	public void setPatientId(String value){
		_patientId = value;
	}
	public String getPatientId(){
		return _patientId;
	}
	public String getResults(){
		return _results;
	}
	public void setSubmitedBy(int value){
		_submitedBy = value;
	}
	public int getSubmitedBy(){
		return _submitedBy;
	}
	public String getSubmitedON(){
		return _submitedOn;
	}
	public void setSubmitedOn(String value){
		_submitedOn = value;
	}
}
