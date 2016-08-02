package dashboard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Servlet implementation class User
 */

public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int rtype;
	private JSONObject json;
	private JSONArray jarr;
	private PrintWriter pw;
	UserRec user;
	
	
	DbDetails db;
	private String query;
	private Connection cnn = null;
	private ResultSet rset = null;
	private PreparedStatement pstm = null;
	
	String colname   = "";
	String tablename = "";
	String database  = "";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		pw = response.getWriter();
        pw.print(UserList().toString());
        pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session;
		try{
			response.setContentType("application/json");
			pw = response.getWriter();
			rtype = Integer.parseInt(request.getParameter("rtype"));
			switch(rtype){
			case 1: //Login
				if(request.getParameterMap().containsKey("username") && request.getParameterMap().containsKey("password")){
					UserRec user = Login(request.getParameter("username"), request.getParameter("password"));
					if(user.isAuthenticated()){
						session = request.getSession(true);
						session.setAttribute("uid", user.getUid());
		                session.setAttribute("uname", user.getUsername());
		                session.setAttribute("fname", user.getFirstname());
		                session.setAttribute("lname", user.getLastname());
		                session.setAttribute("dbase", user.getDBase());
		                session.setMaxInactiveInterval(30*60);
		                
		                response.setContentType("application/json;charset=utf-8");
		                json = new JSONObject();
		                json.put("uid", user.getUid());
		                json.put("uname", user.getUsername());
		                json.put("sid", session.getId());
		                json.put("success", true);
		                json.put("process", "login");
		                json.put("dbase",user.getDBase());
		                
		                switch(user.getRole()){
		                case 1: //Administrator
		                	json.put("page", "index.jsp");
		                	break;
		                case 2: //Data Manager
		                	json.put("page", "dm.jsp");
		                	break;
		                case 3: //Lab Manager
		                	json.put("page", "lab.jsp");
		                	break;
		                default:
		                	json.put("page", "index.jsp");
		                	break;
		                }
		                
		                pw = response.getWriter();
		                pw.print(json.toString());
		                pw.close();
		                
		                System.out.println(json.toString());
					}else{
				    	json = new JSONObject();
		                json.put("success", false);
		                json.put("message", "invalid username or password");
		                pw.print(json.toString());
				    	pw.close();
				    	System.out.println("User login failed for user "+ user.getUsername());
					}
				}
				break;
			case 2: //Logout
				//invalidate the session if exists
		        session = request.getSession(false);
		        System.out.println("User Logout, "+session.getAttribute("uname"));
		        if(session != null){
		            session.invalidate();
		        }
		        response.sendRedirect("../");
		        return;
			case 3: //Print UserList
				try{
	                pw.write(UserList().toString());
	                pw.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			case 4: //Add new user
				user = new UserRec();
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));
				user.setFirstname(request.getParameter("fname"));
				user.setLastname(request.getParameter("lname"));
				user.setRole(Integer.parseInt( (request.getParameter("urole")) ));
				user.setDBase(request.getParameter("dbase"));
				
				json =  new JSONObject();
				if(AddUser(user)){
					json.put("success", true);
					json.put("message", "User added!");
				}else{
					json.put("success", false);
					json.put("message", "Failed to add user");
				}
				pw = response.getWriter();
                pw.print(json.toString());
                pw.close();
				break;
			case 5: //View Table 1
				session = request.getSession();
				tablename =  request.getParameter("tablename");
				database  =  session.getAttribute("dbase").toString();
				if(!session.isNew()){
					pw.print( selectTable( tablename, database ).toString() );
					pw.close();
				}
				break;
			case 6: //Add VIRO Data
				ViroRec viro = new ViroRec();
				viro.setPatientId(request.getParameter("viralid") );
				viro.setResults(request.getParameter("results") );
				viro.setRecId(request.getParameter("recid"));
				viro.setSubmitedBy( (Integer)request.getSession().getAttribute("uid") );
				
				json =  new JSONObject();
				
				database  = request.getSession().getAttribute("dbase").toString();
				tablename = request.getParameter("tablename");
				colname   = request.getParameter("colname");
				
				if(UpdateVIRO(viro, database,tablename,colname) ){
					json.put("success", true);
					pw = response.getWriter();
	                pw.print(json.toString());
	                pw.close();
					System.out.println("VIRO Added");
				}else{
					json.put("success", false);
					pw = response.getWriter();
	                pw.print(json.toString());
	                pw.close();
					System.out.println("VIRO Added");
				}
				
				break;
			case 7: //Select summary
				session = request.getSession();
				tablename =  request.getParameter("tablename");
				database  =  session.getAttribute("dbase").toString();
				if(!session.isNew()){
					pw.print( selectSummary( tablename, database ).toString() );
					pw.close();
				}
				break;
			default:
				break;
			
			}
			
		}catch(JSONException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected UserRec Login(String uname, String upass){
		UserRec user = new UserRec();
		user.setUsername(uname);
		user.setPassword(upass);
		try{
			db = new DbDetails();
			cnn = db.getConn();
			query = "SELECT * FROM _users WHERE uname=? AND upass=?;";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, user.getUsername());
			pstm.setString(2, user.getPassword());
			rset = pstm.executeQuery();
			json = new JSONObject();
			
			System.out.println(pstm.toString());
			
			if(rset.next()){
				//user.setUid( Integer.parseInt( (rset.getObject("id").toString() )));
				user.setUid( rset.getInt("id") );
				user.setFirstname(rset.getObject("fname").toString());
				user.setLastname(rset.getString("lname"));
				user.setDBase(rset.getObject("dbase").toString() );
				user.setRole( rset.getInt("urole") );
				user.setIsAutheticated(true);
			}else{
				user.setIsAutheticated(false);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//finally block used to close resources
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
		return user;
	}
	protected boolean AddUser(UserRec usr){
		try{
			db = new DbDetails();
			cnn = db.getConn();
			query = "INSERT INTO _users(uname,upass,fname,lname,urole,dbase) VALUES(?,?,?,?,?,?);";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, usr.getUsername());
			pstm.setString(2, usr.getPassword());
			pstm.setString(3, usr.getFirstname());
			pstm.setString(4, usr.getLastname());
			pstm.setInt(5, usr.getRole());
			pstm.setString(6, usr.getDBase());
			
			//Log insert
			System.out.println(pstm.toString());
			
			if (pstm.executeUpdate() == 1){
				return true;
			}else{
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			//finally block used to close resources
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
	protected boolean UpdateVIRO(ViroRec viro, String dbase, String dtable, String colname){
		try{
			db = new DbDetails();
			cnn = db.getConn(dbase);
			query = "UPDATE "+dtable+" SET "+colname+"=? WHERE _URI=?;";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, viro.getResults());
			pstm.setString(2, viro.getRecId());
			
			//Log Update
			System.out.println(pstm.toString());
			
			if (pstm.executeUpdate() == 1){
				return true;
			}else{
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
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
	protected JSONObject UserList(){
		try{
			db = new DbDetails();
			cnn = db.getConn();
			query = "SELECT * FROM view_users;";
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
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//finally block used to close resources
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
	protected JSONObject selectSummary(String tablename, String database){
		try{
			db = new DbDetails();
			cnn = db.getConn(database);
			query = "SELECT * FROM "+tablename+";";
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			java.sql.ResultSetMetaData columns = rset.getMetaData();
			
			json = new JSONObject();
			if(rset.next()){
				for (int i=1;i<=columns.getColumnCount();i++){
					json.put( columns.getColumnName(i), rset.getObject(i));
					//System.out.println(columns.getColumnTypeName(i));
				}
				jarr.put(json);
			}
			return json;
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
