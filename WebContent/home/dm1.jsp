<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Insert Project Title Below -->
	<title>SOAR::DM HOME</title>
	<!-- Google Fonts -->
	<!--
		//Disabled: Currently using local copy
		<link href='http://fonts.googleapis.com/css?family=Cuprum' rel='stylesheet' type='text/css'> 
	 -->
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/brain-theme.css" rel="stylesheet" type="text/css">
    <link href="../css/styles.css" rel="stylesheet" type="text/css">
    <link href="../css/bootstrap-table.min.css" rel="stylesheet">
    <link href="../css/font-awesome.min.css" rel="stylesheet" type="text/css">  
<%
String sid1   = "";
String sid2   = "";
String uname  = "";
String fname  = "";
String lname  = "";
String dbase  = "";
if(!session.isNew() && session.getAttribute("uname") != null){
	if(request.getParameterMap().containsKey("sid")){
		sid1 = request.getSession().getId();
		sid2 = request.getParameter("sid");
		uname  = session.getAttribute("uname").toString();
		dbase  = session.getAttribute("dbase").toString();
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
                <li><a href="#" class="btn btn-default"><i class="fa fa-cog"></i> <span>Settings</span></a></li>
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
                <h5><i class="fa fa-bars"></i>DM Dashboard <small>Welcome, <% out.print(fname); %> </small></h5>
            </div>
            <!-- /page title -->
            
            <!-- Statistics -->
             <ul class="row stats">
                <li class="col-xs-3"><a id="idusers" href="#" class="btn btn-default">13</a> <span>Active Users</span></li>
                <li class="col-xs-3"><a id="idctcno" href="#" class="btn btn-default">52</a> <span>Records with CTC Number</span></li>
                <li class="col-xs-3"><a id="idviral" href="#" class="btn btn-default">14</a> <span>Records with VIRAL Load</span></li>
                <li class="col-xs-3"><a id="idtotal" href="#" class="btn btn-default">48</a> <span>Total Records</span></li>
            </ul>
            <!-- /statistics -->
            
             <!-- Form submission data -->
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h6 class="panel-title">Form Submissions</h6>
                </div>
                <div class="panel-body">
                    <table id="tblOne"
				        data-toggle="tblUserList"
				        data-side-pagination="server"
				        data-pagination="true"
				        data-page-size="5"
				        data-page-list="[5, 10, 20]"
				        data-search="true" >         
				   	</table>
                </div>
            </div>
            <!-- /form submission data -->
            
             <!-- Edit Link: Form Modal -->
            <div id="frmViroLoad" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h5 class="modal-title">VIRAL Load Details</h5>
                        </div>

                        <!-- Form inside modal -->
                        <form id="frmViro" action="../User" method="post" role="form">

                            <div class="modal-body has-padding">
                                <div class="form-group">
                                    <div class="row">
                                    <div class="col-sm-6">
                                        <label for="sitename">Facility Name</label>
                                        <input name="sitename" type="text" placeholder="Eugene" class="form-control" readonly="readonly">
                                    </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <label for="viralid">Patient ID</label>
                                            <input name="viralid" type="text" placeholder="XYZ-99-999" class="form-control" readonly="readonly">
                                        </div>

                                        <div class="col-sm-6">
                                            <label>CTC Number</label>
                                            <input name="results" type="text" placeholder="CTC Number" class="form-control">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                	<div class="row">
                                		<div class="col-sm-6">
                                			<label for="tdate">Todays Date</label>
                                            <input name="tdate" type="text" placeholder="Date" class="form-control" readonly="readonly">
                                		</div>
                                		<div class="col-sm-4">
                                			<label>Physician Name</label>
                                            <input name="phyname" type="text" value="<% out.print(fname); %>" class="form-control" readonly="readonly">
                                		</div>
                                		<div class="col-sm-2">
                                			<label for="initials">Initials</label>
                                            <input name="initials" type="text" placeholder="IL" class="form-control" maxlength=2 >
                                		</div>
                                	</div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Submit form</button>
                                <input type="hidden" name="rtype" value="6">
                                <input type="hidden" name="tablename" value="">
                                <input type="hidden" name="colname" value="CTC_No">
                                <input type="hidden" name="recid" value="">
                                <input type="hidden" name="dbase" value="<% out.print(dbase); %>">
                            </div>

                        </form>
                    </div>
                </div>
            </div>
            <!-- /form modal -->
        	
        </div><!-- /page content -->
    </div><!-- /page container -->

<!-- Script Files -->
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery-ui.min.js"></script>
<script src="../js/jquery.bootstrap.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/collapsible.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table.min.js"></script>
<script type="text/javascript" src="../js/plugins/interface/validate.min.js"></script>
<script>
	$(document).ready(function(){
		var today = new Date();
		var RowIndex;
		loadTable1();
		//loadSummary();
		$("#logout").click(function(){
			$.ajax({
				url:"../User",
				method: "POST",
				data: { "rtype":2 }
			});
			window.location.replace("../");
		});
		$('#frmViroLoad').on('show.bs.modal', function () {
			$("input[name='tdate']").val( today.toDateString() );
			$("input[name='results']").val( "" );
		});
		//Row Click
	    $("#tblOne").on('click-row.bs.table', function(e, row, $element){
	    	$("input[name='recid']").val(row['_URI']);
	    	$("input[name='sitename']").val(row['HEALTH_FACILITY']);
	    	$("input[name='tablename']").val(row['TABLE_NAME']);
	    	$("input[name='viralid']").val(row['VIRAL_ID']);
	    	RowIndex = $element.index();
	    });
		
		$("#frmViro").validate({
			rules:{
				viro:{
					required: true
				},
				initials:{
					required: true,
					minlength:2
				}
			},
			messages:{
				viro: {
					required: "Required field"
				},
				initials:{
					required: "Required field",
					minlength: "Invalid"
				}
			},
			submitHandler: function(form){
				var validator = this;
				$.ajax({
					url : $(form).attr('action'),
					type: $(form).attr('method'),
				   	data: $(form).serialize(),
				   	dataType : 'json',
				   	success: function(data) {
				   		updateTableCell(RowIndex,"CTC_NO",$("input[name='results']").val());
	                    $("#frmViroLoad").modal('hide');
	                    loadSummary();
	                },
	                error: function(xhr, ajaxOptions, thrownError){
	                    validator.showErrors( {"viro": xhr.status + " " + thrownError });
	                }
				});
				//form.preventDefault();
				return false; 
			}
		});
		function loadSummary(){
			$.ajax({
				url	: "../User",
				type: "POST",
				data: "rtype=7&tablename=view_summary1",
				datatype: "json",
				success: function(data){
					$("#idtotal").text(data.COUNT_REC);
					$("#idctcno").text(data.COUNT_CTC);
					$("#idviral").text(data.COUNT_VIRAL);
				},
				error: function(data){
					
				}
			});
		}
		function loadTable1(){
			$.ajax({
				url : "../View",
				type: "POST",
				data: "rtype=2&tablename=view_table1",
				datatype: "json",
				async: false,
				success: function(data){
					$("#tblOne").bootstrapTable({
						data: data.rows,
						columns: [{
					    	field: 'REGION',
					    	title: 'Region'
					    },{
					    	field: 'DISTRICT',
					    	title: 'District',
					    	sortable: true,
					    	align: 'left'
					    },{
					    	field: 'FACILITY',
					    	title: 'Health Facility',
					    	sortable: true,
					    	align: 'left'
					    },{
					    	field: 'VIRAL_ID',
					    	title: 'Viral ID',
					    	sortable: true,
					    	align: 'left'
					    },{
					    	field: 'EDIT',
					    	title: 'Edit',
					    	align: 'center'
					    },{
					    	field: 'CTC_NO',
					    	title: 'CTC Number',
					    	sortable: true,
					    	align: 'center'
					    }]
					})
				}
				
			});
		}
		function updateTableCell(index,cellid,cellval){
			$("#tblOne").bootstrapTable('updateCell', {
				index: index,
				field: cellid,
				value: cellval
			});
		}
	});
</script>
<script src="../js/application_blank.js"></script>
<script src="../js/application.js"></script>
</body>
</html>