<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Insert Project Title Below -->
	<title>SOAR::Users</title>
	<!-- Google Fonts -->
	<!--
		//Disabled: Currently using local copy
		<link href='http://fonts.googleapis.com/css?family=Cuprum' rel='stylesheet' type='text/css'> 
	 -->
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/bootstrap-table.min.css" rel="stylesheet">
    <link href="../css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- Default styles for this template -->
    <link href="../css/styles.css" rel="stylesheet" type="text/css">
    <link href="../css/brain-theme.css" rel="stylesheet" type="text/css">
    
<%
String sid1   = "";
String sid2   = "";
String uname  = "";
String fname  = "";
String lname  = "";
if(!session.isNew() && session.getAttribute("uname") != null){
	if(request.getParameterMap().containsKey("sid")){
		sid1 = request.getSession().getId();
		sid2 = request.getParameter("sid");
		uname  = session.getAttribute("uname").toString();
		fname  = session.getAttribute("fname").toString();
		lname  = (session.getAttribute("lname") == null) ? "":session.getAttribute("lname").toString();
		fname  = fname + " " + lname;
		if( !sid1.equals(sid2) ){
			response.sendRedirect("index.html?msg='Forbidden 1'");
		}
		
	}else{
		response.sendRedirect("../403.html?msg='Forbidded 2'");
	}
	
}else{
	response.sendRedirect("../403.html?MSG='Forbidden 3'");
}

%>
</head>
<body>
	<!-- Navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
            	<div class="hidden-lg pull-right">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-right">
                        <span class="sr-only">Toggle navigation</span>
                        <i class="fa fa-chevron-down"></i>
                    </button>

                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar">
                        <span class="sr-only">Toggle sidebar</span>
                        <i class="fa fa-bars"></i>
                    </button>
                </div>
                <ul class="nav navbar-nav navbar-right-custom">
                    <li class="user dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown">
                            <span> <% out.print(fname); %></span><i class="caret"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
                            <li><a href="#"><i class="fa fa-tasks"></i> Tasks</a></li>
                            <li><a id="logout" href="#"><i class="fa fa-mail-forward"></i> Logout</a></li>
                        </ul>
                    </li>
                    <li><a class="nav-icon sidebar-toggle"><i class="fa fa-bars"></i></a></li>
                </ul>
            </div>
        </div>
    </div>
    <!-- /navbar -->
    <!-- Page header -->
    <div class="container-fluid">
        <div class="page-header">
            <ul class="middle-nav">
                <li><a href="index.jsp?sid=<% out.print(sid1); %>" class="btn btn-default"><i class="fa fa-home"></i> <span>Home</span></a></li>
            </ul>
        </div>
    </div>
    <!-- /page header -->
    <!-- Page container -->
    <div class="page-container container-fluid">
    	<!-- Sidebar -->
        <div class="sidebar collapse">
        	<ul class="navigation">
            	<li class="active"><a href="index.jsp"><i class="fa fa-laptop"></i> Dashboard</a></li>
            	<li>
        			<a href="#" class="expand"><i class="fa fa-table"></i> Tables</a>
					<ul>
                		<li><a href="#">Table 1</a></li>
                		<li><a href="#">Table 2</a></li>
                	</ul>
        		</li>
                <li>
                	<a href="#" class="expand"><i class="fa fa-bar-chart-o"></i>Charts</a>
                	<ul>
                		<li><a href="#">Chart 1</a></li>
                		<li><a href="#">Chart 2</a></li>
                	</ul>
                </li>
                <li>
                    <a href="#" class="expand"><i class="fa fa-align-justify"></i>Edit Forms</a>
                    <ul>
                        <li><a href="#">Form 1</a></li>
                        <li><a href="#">Form 2</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /sidebar -->
        <!-- Page Content -->
        <div class="page-content">
            <!-- Page title -->
        	<div class="page-title">
                <h5><i class="fa fa-bars"></i> System Users</h5>
            </div>
            <!-- /page title -->
            
            <!-- Add User Form -->
            <form id="frmAddUsr" action="../User" method="post" class="form-horizontal" role="form">
            	<div class="panel panel-default">
            		<div class="panel-heading">
            			<h6 class="panel-title">Add New User</h6>
            		</div>
            		<div class="panel-body">
            			<div class="form-group">
            				<div class="col-md-3">
                                <input name="username" type="text" class="form-control" placeholder="Username">
                            </div>
                            <div class="col-md-3">
                                <input name="password" type="password" class="form-control" placeholder="Password">
                            </div>
            			</div>
            			<div class="form-group">
            				<div class="col-md-3">
                                <input name="fname" type="text" class="form-control" placeholder="First Name">
                            </div>
                            <div class="col-md-3">
                                <input name="lname" type="text" class="form-control" placeholder="Last Name">
                            </div>
                            <div class="col-md-3">
                                <select id="urole" name="urole" class="select">
                                	<option value="0">Select Role</option>
                                	<option value="1">Administrator</option>
                                	<option value="2">Data Manager</option>
                                	<option value="2">Lab Technician</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <select id="orgunit" name="orgunit" class="select">
                                	<option value="0">Select Organization</option>
                                </select>
                            </div>
            			</div>
            			<div class="form-actions text-right">
            				<input type="reset" value="Reset" class="btn btn-primary">
                            <input type="submit" value="Add User" class="btn btn-primary">
                            <input type="hidden" name="rtype" value="4">
                            <input type="hidden" name="dbase" value="odk_viro">
                            <input type="hidden" name="level" value="">
                            <input type="hidden" name="levelvalue" value="">
                        </div>
            		</div>
            	</div>
            </form>
            <!-- /Add User -->
            
             <!-- User List -->
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h6 class="panel-title">List of Users</h6>
                </div>
                <div class="panel-body">
                   	<table id="tblUserList"
				        data-toggle="tblUserList"
				        data-pagination="true"
				        data-page-size="5"
				        data-page-list="[5, 10, 20]"
				        data-search="true" >         
				   	</table>
                </div>
            </div>
            <!-- /simple chart -->
            
             <!-- Large modal -->
            <div id="large_modal" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h5 class="modal-title">User List</h5>
                        </div>
                        
                        <div class="modal-footer">
                            <button class="btn btn-warning" data-dismiss="modal">Close</button>
                            <button class="btn btn-primary">Save</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /large modal -->
        	
        </div>
    </div>
    <!-- /page container -->

<!-- Script Files -->
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery-ui.min.js"></script>
<script src="../js/jquery.bootstrap.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/collapsible.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/validate.min.js"></script>
<script type="text/javascript" src="../js/plugins/forms/select2.min.js"></script>
<script>
	$(document).ready(function(){
		loadUsers();
		loadControls();
	
		$("#orgunit").select2({ width: '100%' });		//Other have used resolve instead of 100%
		$("#urole").select2({ width: '100%' });
		
		
		//Get the user level value from optgroup
		$("#orgunit").on('change', function() {
			
			var opt = $(this).find(':selected');
		    var sel = opt.text();					
		    var og = opt.closest('optgroup').attr('name');	//or you can use attribute label
		    
		    $('input[name="level"]').val( og ); //assign the value to hidden input control
		    $('input[name="levelvalue"]').val( sel ); //assign the value to hidden input control
		    
		});
		
		$("#logout").click(function(){
			$.ajax({
				url:"../User",
				method: "POST",
				data: { "rtype":2 }
			});
			window.location.replace("../");
		});
		
		$("#frmAddUsr").validate({
			ignore: "",
			rules:{
				username: {
					required: true,
					minlength: 5,
					email: true
				},
				password:{
					required: true,
					minlength: 5
				},
				fname:{
					required: true
				},
				urole:{
					selectRole: true
				},
				orgunit:{
					selectSite: true
				}
			},
			messages:{
				username:{
					required: "Username is required",
					minlength: "Minimum length is not met"
				},
				password:{
					required: "Password is required",
					minlength: "Minimum length is not met"
				},
				fname:{
					required: "First name is required"
				}
			},
			submitHandler: function(form){
				var validator = this;
				$.ajax({
					type: $(form).attr('method'),
				  	url: $(form).attr('action'),
				   	data: $(form).serialize(),
				   	dataType : 'json'
					}).done(function (response) {
				   	if (!response.success) {
				 		validator.showErrors( {"password": response.message});
				 	}else{
				 		$("#frmAddUsr")[0].reset();
				 	}
				   	
				})
				
				//Refresh table
				//$(form).reset();
				$("#tblUserList").bootstrapTable('refresh');
				return false; // required to block normal submit since you used ajax
				
			}
		});
		
		jQuery.validator.addMethod('selectRole', function (value) {
	        return (value != '0');
	    }, "Role is required");
		
		jQuery.validator.addMethod('selectSite', function (value) {
	        return (value != '0');
	    }, "User site is required");
		
		function loadControls(){
			$.ajax({
				url: "../Controls",
				type: "POST",
				data: "rtype=1",
				datatype: "json",
				success: function(data){
					//Region
					html = "<optgroup name='region' label='Region'>";
					$.each(data.regions, function(key, val){
						html += "<option value=" + val.id + ">" + val.name + "</option>";
				    })
				    html +="</optgroup>"
				    $("#orgunit").append(html);
					//District
					html = "<optgroup name='district' label='District'>";
					$.each(data.districts, function(key, val){
						html += "<option value=" + val.id + ">" + val.name + "</option>";
				    })
				    html +="</optgroup>"
				    $("#orgunit").append(html);
					//Site
					html = "<optgroup name='site' label='Site'>";
					$.each(data.sites, function(key, val){
						html += "<option value=" + val.id + ">" + val.name + "</option>";
				    })
				    html +="</optgroup>"
				    $("#orgunit").append(html);
				}
			});
		}
		function loadUsers(){
			$.ajax({
				url : "../User",
				type: "POST",
				data: "rtype=3",
				datatype: "json",
				async: false,
				success: function(data){
					$("#tblUserList").bootstrapTable({
						data: data.rows,
						columns: [{
					    	field: 'uname',
					    	title: 'Username'
					    },{
					    	field: 'upass',
					    	title: 'Password'
					    },{
					    	field: 'fname',
					    	title: 'First Name'
					    },{
					    	field: 'lname',
					    	title: 'Last Name'
					    },{
					    	field: 'urole',
					    	title: 'User Role'
					    },{
					    	field: 'levelvalue',
					    	title: 'Home Directory'
					    },{
					    	field: 'dbase',
					    	title: 'Data Base'
					    },{
					    	field: 'active',
					    	title: 'Active'
					    },{
					    	field: 'edit',
					    	title: 'Edit'
					    }]
					})
				}
				
			});
		}
	});
</script>
<script src="../js/application_blank.js"></script>
<script src="../js/application.js"></script>
</body>
</html>