package dashboard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
	
	String colname	= "";
	String tablename= "";
	String database = ""; 
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	String formatedDate = df.format(date);
	
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
		                session.setAttribute("level", user.getLevel());
		                session.setAttribute("levelvalue", user.getLevelValue());
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
		                
		                //System.out.println(json.toString());
		                System.out.println("Login session for user "+user.getUsername()+" at "+formatedDate);
					}else{
				    	json = new JSONObject();
		                json.put("success", false);
		                json.put("message", user.getError());
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
				user.setLevel((request.getParameter("level")));
				user.setLevelValue((request.getParameter("levelvalue")));
				user.setIsAutheticated(false);
				
				UserRec newUser = AddUser(user);
				json =  new JSONObject();
				if(newUser.isAuthenticated()){
					json.put("success", true);
					json.put("message", newUser.getError());
				}else{
					json.put("success", false);
					json.put("message", newUser.getError());
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
				viro.setComments(request.getParameter("comments"));
				viro.setType(request.getParameter("vtype"));
				viro.setQuality(request.getParameter("squality"));
				viro.setSubmitedBy( (Integer)request.getSession().getAttribute("uid") );
				viro.setSubmitedOn(request.getParameter("tdate"));
				
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
				JSONObject usr = new JSONObject();
				usr.put("database", session.getAttribute("dbase").toString());
				usr.put("level", session.getAttribute("level").toString());
				usr.put("levelvalue", session.getAttribute("levelvalue").toString());
				
				if(!session.isNew()){
					pw.print( selectSummary( usr ).toString() );
					pw.close();
				}
				break;
			case 8://Add CTC Details
				CTCRec rec = new CTCRec();
				rec.setId(request.getParameter("ctcno"));
				rec.setRecId( request.getParameter("recid") );
				
				database  = request.getSession().getAttribute("dbase").toString();
				tablename = request.getParameter("tablename");
				
				rec = UpdateCTC(rec,database,tablename); 
				json =  new JSONObject();
				json.put("success", rec.getSuccess());
				json.put("message", rec.getMessage());
				pw = response.getWriter();
                pw.print(json.toString());
                pw.close();
				System.out.println("CTC No Updated");
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
			
			//System.out.println(pstm.toString());
			
			if(rset.next()){
				//user.setUid( Integer.parseInt( (rset.getObject("id").toString() )));
				user.setUid( rset.getInt("id") );
				user.setFirstname(rset.getObject("fname").toString());
				user.setLastname(rset.getString("lname"));
				user.setDBase(rset.getObject("dbase").toString() );
				user.setRole( rset.getInt("urole") );
				user.setLevel(rset.getString("level"));
				user.setLevelValue(rset.getString("levelvalue"));
				user.setIsAutheticated(true);
			}else{
				user.setIsAutheticated(false);
				user.setError("Invalid user name or password");
			}
			
			//Now update last login column
			query = "UPDATE _users SET lastlogin=? WHERE id=?";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, formatedDate);
			pstm.setInt(2, user.getUid());
			pstm.executeUpdate();
						
		}catch(SQLException e){
			user.setError( "Communication link failure. Check if MySQL is running" );
			user.setIsAutheticated(false);
			e.printStackTrace();
			return user;
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
	protected UserRec AddUser(UserRec usr){
		try{
			db = new DbDetails();
			cnn = db.getConn();
			query = "INSERT INTO _users(uname,upass,fname,lname,urole,level,levelvalue,dbase) VALUES(?,?,?,?,?,?,?,?);";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, usr.getUsername());
			pstm.setString(2, usr.getPassword());
			pstm.setString(3, usr.getFirstname());
			pstm.setString(4, usr.getLastname());
			pstm.setInt(5, usr.getRole());
			pstm.setString(6, usr.getLevel());
			pstm.setString(7, usr.getLevelValue());
			pstm.setString(8, usr.getDBase());
			
			//Log insert
			System.out.println(pstm.toString());
			
			if (pstm.executeUpdate() == 1){
				usr.setIsAutheticated(true);
				usr.setError("New user added");
				return usr;
			}else{
				usr.setError("Failed to add new user");
				return usr;
			}
		}catch(SQLException e){
			//e.printStackTrace();
			
			if( e.getErrorCode()==1062 ){
				usr.setError("Username already exist!");
			}else{
				usr.setError("SQL Error");
			}
			return usr;
		}catch(Exception e){
			e.printStackTrace();
			usr.setError("Exception Error");
			return usr;
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
	protected CTCRec UpdateCTC(CTCRec ctc, String dbase, String dtable){
		try{
			db = new DbDetails();
			cnn = db.getConn(dbase);
			query = "UPDATE "+dtable+" SET CTC_ID=? WHERE _URI=?;";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, ctc.getId());
			pstm.setString(2, ctc.getRecId());
			
			if (pstm.executeUpdate() == 1){
				ctc.setSuccess(true);
				ctc.setMessage("CTC Record Updated");
				return ctc;
			}else{
				ctc.setSuccess(false);
				ctc.setMessage("Failed to update CTC No");
				return ctc;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			ctc.setMessage("SQL Error, Failed to update CTC No");
			return ctc;
		}catch(Exception e){
			e.printStackTrace();
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
		return null;
	}
	protected boolean UpdateVIRO(ViroRec viro, String dbase, String dtable, String colname){
		try{
			db = new DbDetails();
			cnn = db.getConn(dbase);
			query = "UPDATE "+dtable+" SET VIRAL_RESULTS=?, VIRAL_COMMENTS=?,VIRAL_QUALITY=?, VIRAL_TYPE=?, SUBMITED_BY=?, SUBMITTED_ON=? WHERE _URI=?;";
			pstm = cnn.prepareStatement(query);
			pstm.setString(1, viro.getResults());
			pstm.setString(2, viro.getComments());
			pstm.setString(3, viro.getQuality());
			pstm.setString(4, viro.getType());
			pstm.setInt(5, viro.getSubmitedBy());
			pstm.setString(6, viro.getSubmitedON());
			pstm.setString(7, viro.getRecId());
			
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
	protected JSONObject selectSummary(JSONObject usr){
		try{
			db = new DbDetails();
			cnn = db.getConn(usr.getString("database"));
			
			query = "SELECT COUNT(*) FROM view_table1;";
			
			if(!usr.getString("level").equalsIgnoreCase("country")){
				if(!usr.getString("level").isEmpty() && !usr.getString("levelvalue").isEmpty()){
					query = query.replace(";", "")+" WHERE "+usr.getString("level")+" like '"+ usr.getString("levelvalue")+"';";
				}

			}
						
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			JSONObject json = new JSONObject();
			if(rset.next()){
				json.put("total_rec",rset.getInt(1));
			}
			
			if(usr.getString("level").equalsIgnoreCase("country")){
				query = query.replace(";", "") + " WHERE CTC_NO IS NOT NULL";
			}else{
				query = query.replace(";", " AND ") + " CTC_NO IS NOT NULL";
			}
			
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			if(rset.next()){
				json.put("total_ctc",rset.getInt(1));
			}
			query = query.replace("CTC_NO", "VIRAL_ID");
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			
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
