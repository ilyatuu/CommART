package dashboard;

import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Controls
 */
public class Controls extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DbDetails db;
	private int rtype;
	private String query;
	private Connection cnn = null;
	private ResultSet rset = null;
	private PreparedStatement pstm = null;
	
	private JSONObject json;
	private JSONArray jarr;
	private PrintWriter pw;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controls() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().print(OrgUnits(3));
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
			session = request.getSession();
			JSONObject usr = new JSONObject();
			usr.put("database", session.getAttribute("dbase").toString());
			usr.put("level", session.getAttribute("level").toString());
			usr.put("levelvalue", session.getAttribute("levelvalue").toString());
			
			response.setContentType("application/json");
			pw = response.getWriter();
			
			//Switch statement
			switch(rtype){
			case 1: //load user sites
				pw.print( OrgUnits(3) );
				break;
			case 2:
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected JSONObject OrgUnits(int level){
		try{
			db = new DbDetails();
			cnn = db.getConn();
			query = "SELECT id,name FROM _orgunit WHERE levelid = 1;";
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				json.put("id", rset.getInt("id"));
				json.put("name", rset.getString("name"));
				jarr.put(json);
			}
			
			//Store return value
			JSONObject jtable = new JSONObject();
			jtable.put("regions", jarr);
			
			// Get level 2 Organization Units
			query = "SELECT id,name FROM _orgunit WHERE levelid = 2;";
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				json.put("id", rset.getInt("id"));
				json.put("name", rset.getString("name"));
				jarr.put(json);
			}
			
			//Store return value
			jtable.put("districts", jarr);
			
			// Get level 3 Organization Units
			query = "SELECT id,name FROM _orgunit WHERE levelid = 3;";
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
						
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				json.put("id", rset.getInt("id"));
				json.put("name", rset.getString("name"));
				jarr.put(json);
			}
			//Store return value
			jtable.put("sites", jarr);
			
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

}
