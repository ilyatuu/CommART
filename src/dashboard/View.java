package dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class View
 */
public class View extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int rtype;
	private JSONObject json;
	private JSONArray jarr;
	private PrintWriter pw;
	
	DbDetails db;
	private String query;
	private Connection cnn = null;
	private ResultSet rset = null;
	private PreparedStatement pstm = null;
	
	private String tablename= "";
	
	
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public View() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("GET Method");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session;
		
		
		try{
			
			if(request.getParameterMap().containsKey("rtype")){
				rtype = Integer.parseInt(request.getParameter("rtype"));
			}
			response.setContentType("application/json");
			pw = response.getWriter();
			
			session = request.getSession();
			JSONObject usr = new JSONObject();
			usr.put("database", session.getAttribute("dbase").toString());
			usr.put("level", session.getAttribute("level").toString());
			usr.put("levelvalue", session.getAttribute("levelvalue").toString());
		
			
			switch(rtype){
				case 1: //Default View
					
					tablename =  request.getParameter("tablename");
					if(!session.isNew()){
						//pw.print( selectTable( tablename, usr.getString("database") ).toString() );
						pw.print(getSummary(usr).toString());
						pw.close();
					}
				break;
				case 2: //By Filters
					JSONObject opt = new JSONObject();
					opt.put("limit", request.getParameter("limit"));
					opt.put("offset",request.getParameter("offset"));
					opt.put("search",request.getParameter("search"));
					opt.put("searchBy",request.getParameter("searchBy"));
					
					tablename =  request.getParameter("tablename");
					if(!session.isNew()){
						pw.print( selectTableWithFilter( tablename, usr,opt).toString());
						pw.close();
					}
				break;
				case 3: //By Region
					
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected JSONObject selectTable(String tablename, String database){
		try{
			db = new DbDetails();
			cnn = db.getConn(database);
			query = "SELECT * FROM "+tablename+";";
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			java.sql.ResultSetMetaData columns = rset.getMetaData();
			jarr = new JSONArray();
			Integer rows = 0;
			while(rset.next()){
				rows++;
				json = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					json.put( columns.getColumnName(i), rset.getObject(i));
					//System.out.println(columns.getColumnTypeName(i));
				}
				jarr.put(json);
			}
			JSONObject jtable = new JSONObject();
			jtable.put("total", rows);
			jtable.put("rows", jarr);
			return jtable;
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
		
	}
	protected JSONObject selectTableWithFilter(String tablename, JSONObject usr, JSONObject opt){
		try{
			db = new DbDetails();
			cnn = db.getConn(usr.getString("database"));
			
			query = "SELECT COUNT(*) FROM "+tablename+";";
			
			if(!opt.getString("search").isEmpty()){
				query = query.replace(";", "")+" WHERE "+opt.getString("searchBy")+" like '%"+opt.getString("search")+"%';";
			}
			//Integrate user organization group
			if(!usr.getString("level").equalsIgnoreCase("country")){
				if(!opt.getString("search").isEmpty()){
					query = query.replace(";", "")+" AND "+usr.getString("level")+" like '"+ usr.getString("levelvalue")+"';";
				}else{
					query = query.replace(";", "")+" WHERE "+usr.getString("level")+" like '"+ usr.getString("levelvalue")+"';";
				}	
			}
			System.out.println(query);
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			Integer rows=0;
			if(rset.next()){
				rows = rset.getInt(1);
			}
			
			query = query.replace(";", "") + " limit "+opt.getString("limit")+" offset "+opt.getString("offset")+";";
			query = query.replace("COUNT(*)", "*");
			
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			java.sql.ResultSetMetaData columns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					json.put( columns.getColumnName(i), rset.getObject(i));
					//System.out.println(columns.getColumnTypeName(i));
				}
				jarr.put(json);
			}
			JSONObject jtable = new JSONObject();
			jtable.put("total", rows);
			jtable.put("rows", jarr);
			
			//System.out.println( pstm.toString() );
			
			return jtable;
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
		
	}
	
	protected JSONObject getSummary(JSONObject usr){
		try{
			db = new DbDetails();
			cnn = db.getConn(usr.getString("database"));
			
			query = "SELECT COUNT(*) FROM view_table1;";
			
			if(!usr.getString("level").equalsIgnoreCase("country")){
				if(!usr.getString("level").isEmpty() && !usr.getString("level").isEmpty()){
					query = query.replace(";", "")+" WHERE "+usr.getString("level")+" like '"+ usr.getString("levelvalue")+"';";
				}

			}
						
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			System.out.println(query);
			JSONObject json = new JSONObject();
			if(rset.next()){
				json.put("total_rec",rset.getInt(1));
			}
			
			
			query = query.replace(";", "") + " WHERE CTC_NO IS NOT NULL";
			System.out.println(query);
			
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			if(rset.next()){
				json.put("total_ctc",rset.getInt(1));
			}
			
			query = query.replace("CTC_NO", "VIRAL_ID");
			
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			System.out.println(query);
			if(rset.next()){
				json.put("total_viral",rset.getInt(1));
			}
			
			query = query.replace("VIRAL_ID", "VIRAL_ID IS NOT NULL AND VIRAL_RESULTS");
			
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			if(rset.next()){
				json.put("total_results",rset.getInt(1));
			}
			
			return json;
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}

}
