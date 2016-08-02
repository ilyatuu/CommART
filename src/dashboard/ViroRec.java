package dashboard;

public class ViroRec {
	private String _results;
	private String _recid;
	private int _submitedBy;
	private String _patientId;
	
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
}
