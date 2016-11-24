<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%  
    String path = request.getContextPath();  
    String basePath = request.getScheme() + "://"  
            + request.getServerName() + ":" + request.getServerPort()  
            + path + "/";  
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IP地址管理</title>
<link rel="shortcut icon" href="../img/favicon.ico">
<link href="../css/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../css/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON STYLESHEET -->
<link rel="stylesheet" href="../assets/data-tables/DT_bootstrap.css"><!-- DATATABLE CSS -->
<link href="../css/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
<link href="../css/style-responsive.css" rel="stylesheet"><!-- THEME BASIC RESPONSIVE  CSS --></head>
<link rel="stylesheet" type="text/css" href="../extjs5.0/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css">  
<script type="text/javascript" src="../extjs5.0/ext-all.js"></script>  
<script type="text/javascript" src="../extjs5.0/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>  
<body>
<!-- BEGIN SECTION --> 
      <section id="container" class="">
	   <!-- BEGIN HEADER -->
      <%@ include file="../../../jsp/header.jsp"%>
      <!-- END HEADER -->
      <!-- BEGIN SIDEBAR -->
     <%@ include file="../../../jsp/left.jsp"%>
      <!-- END SIDEBAR -->
		 <!-- BEGIN MAIN CONTENT --> 
         <section id="main-content">
		    <!-- BEGIN WRAPPER  -->
            <section class="wrapper site-min-height">
               <section class="panel">
                  <header class="panel-heading">
                     <span class="label label-primary">请假申请</span>
                     <span class="tools pull-right">
                     <a href="javascript:;" class="fa fa-chevron-down"></a>
                     <a href="javascript:;" class="fa fa-times"></a>
                     </span>
                  </header>
                  <div class="panel-body">
                     <div class="adv-table editable-table ">
                        <div class="space15"></div>
                        	<div class="modal-dialog">
					            <div class="modal-content">
					              <div class="modal-header">
					                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="cancleDiv()">
					                  &times;
					                </button>
					                <h4 class="modal-title" id="myModalTitle">
					                  		请假申请
					                </h4>
					              </div>
					              <div class="panel-body">
					              		<div class="form-group ">
					                      <label for="cname" class="control-label col-lg-2">请假类型:</label>
					                      <div class="col-lg-10">
					                      	<select id="leaveType" class="form-control m-bot15" name="leaveType">
												<option>公休</option>
												<option>病假</option>
												<option>调休</option>
												<option>事假</option>
												<option>婚假</option>
											</select>
					                      </div>
					                   </div>
					                   <div class="form-group ">
					                      <label for="cname" class="control-label col-lg-2">开始时间:</label>
					                      <div class="col-lg-10">
					                      	<input id="startTime" value="" class="Wdate" type="text" onclick="WdatePicker()" style="width: 445.2px;height:30px;">
					                      </div>
					                   </div>
					                   <div class="form-group ">
					                      <label for="cname" class="control-label col-lg-2">结束时间:</label>
					                      <div class="col-lg-10">
					                      	 <input id="endTime" value="" class="Wdate" type="text" onclick="WdatePicker()" style="width: 445.2px;height:30px;">
					                      </div>
					                   </div>
					                    <div class="form-group ">
					                      <label for="cname" class="control-label col-lg-2">事由:</label>
					                      <div class="col-lg-10">
					                         <input class=" form-control" id="reason" name="reason" type="text" required="">
					                      </div>
					                   </div>
					              </div>
					              <div class="modal-footer">
					                <button class="btn btn-success" type="button" id="saveAccountIpId" onclick="saveLeaveApply()">
					                  Submit
					                </button>
					              </div>
					            </div>
					          </div>
                     </div>
                  </div>
               </section>
            </section>
			<!-- END WRAPPER  -->
         </section>
		 <!-- END MAIN CONTENT --> 
		 <!-- BEGIN FOOTER --> 
         <%@ include file="../../footer.jsp"%>
		 <!-- END FOOTER --> 
      </section>
	  <!-- END SECTION -->
	  <!-- MODAL -->
        <div  id="myModal" class="modal fade" style="display: none;margin-top: 100px;">
          
        </div>
		<!-- END MODAL -->
<!-- BEGIN JS --> 	  
		<script src="../js/jquery-1.8.3.min.js" ></script><!-- BASIC JS LIABRARY 1.8.3 -->
		<script src="../js/bootstrap.min.js" ></script><!-- BOOTSTRAP JS  -->
		<script src="../js/jquery.dcjqaccordion.2.7.js"></script><!-- ACCORDING JS -->
		<script src="../js/jquery.scrollTo.min.js" ></script><!-- SCROLLTO JS  -->
		<script src="../js/jquery.nicescroll.js" > </script><!-- NICESCROLL JS  -->
		<script src="../assets/data-tables/jquery.dataTables.js"></script><!-- DATATABLE JS  -->
		<script src="../assets/data-tables/DT_bootstrap.js"></script><!-- DATATABLE JS  -->
		<script src="../js/respond.min.js" ></script><!-- RESPOND JS  -->
		<script src="../js/common-scripts.js" ></script><!-- BASIC COMMON JS  -->
		<script src="../js/editable-table.js" ></script><!-- EDITABLE TABLE JS  -->
		<script src="../js/oaLeave.js" ></script>
	  <!-- END JS --> 
	  <div class="modal-backdrop fade in" style="display: none;" id="zhezhaocengId"></div>
</body>
</html>