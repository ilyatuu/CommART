package dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DbDetails {
	private static Properties props;
	public Connection getConn() throws Exception
    {
        try
        {
        	//This will allow to read input stream only once
	        synchronized(DbDetails.class){
	        	if(props==null){
	        		//Load properties
	        		InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
	        		
	        		System.out.println("reading db.properties");
	        		props = new Properties();
	        		props.load(in);
	        		
	        		//System.out.println("is prop empty? "+props.isEmpty());
	        		in.close();
	        		
	        	}
	        	String driver = props.getProperty("db.driver");  
    	        if (driver != null)  
    	        {  
    	        	
    	            //System.setProperty("com.mysql.jdbc.Driver", driver);
    	            
    	            try{
	        			Class.forName("com.mysql.jdbc.Driver").newInstance();
	        		} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	           
    	        }  
    	        String db = props.getProperty("db.name");
    	        String url = props.getProperty("db.url");  
    	        String user = props.getProperty("db.user");  
    	        String pass = props.getProperty("db.pass");
    	        
	        	return DriverManager.getConnection(url+db, user, pass); 
	        }
        }
        catch (SQLException e)
        {
                throw e;
        }
    }
	public Connection getConn(String database) throws IOException, SQLException{
		 try
	        {
	        	//This will allow to read input stream only once
		        synchronized(DbDetails.class){
		        	if(props==null){
		        		//Load properties
		        		InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
		        		
		        		System.out.println("reading db.properties");
		        		props = new Properties();
		        		props.load(in);
		        		
		        		//System.out.println("is prop empty? "+props.isEmpty());
		        		in.close();
		        		
		        	}
		        	String driver = props.getProperty("db.driver");  
	    	        if (driver != null)  
	    	        {  
	    	        	
	    	            //System.setProperty("com.mysql.jdbc.Driver", driver);
	    	            
	    	            try{
		        			Class.forName("com.mysql.jdbc.Driver").newInstance();
		        		} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	           
	    	        }  
	    	        String db = database;
	    	        String url = props.getProperty("db.url");  
	    	        String user = props.getProperty("db.user");  
	    	        String pass = props.getProperty("db.pass");
	    	        
		        	return DriverManager.getConnection(url+db, user, pass); 
		        }
	        }
	        catch (SQLException e)
	        {
	                throw e;
	        }
	}
}
