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
<title>任务列表</title>
<link href="../css/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../css/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON STYLESHEET -->
<link rel="stylesheet" href="../assets/data-tables/DT_bootstrap.css"><!-- DATATABLE CSS -->
<link href="../css/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
<link href="../css/style-responsive.css" rel="stylesheet"><!-- THEME BASIC RESPONSIVE  CSS --></head>
<link rel="stylesheet" type="text/css" href="../extjs5.0/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css">  
<script type="text/javascript" src="../extjs5.0/ext-all.js"></script>  
<script type="text/javascript" src="../extjs5.0/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<link href="../js/common/plugins/jui/themes/redmond/jquery-ui-1.9.2.custom.css" type="text/css" rel="stylesheet" />
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
                     <span class="label label-primary">任务列表管理</span>
                     <span class="tools pull-right">
                     <a href="javascript:;" class="fa fa-chevron-down"></a>
                     <a href="javascript:;" class="fa fa-times"></a>
                     </span>
                  </header>
                  <div class="panel-body">
                     <div class="adv-table editable-table ">
                        <div class="clearfix">
                        </div>
                        <div class="space15"></div>
                        <table class="table table-striped table-hover table-bordered" id="editable-sample">
                           <display:table id="leave" name="leaveList" pagesize="5" class="table">
	                        <display:setProperty name="sort.amount" value="list"></display:setProperty>
	                        <display:column property="leaveType" title="假种" sortable="true" />
							<display:column property="userId" title="申请人" sortable="true"/>
							<display:column property="applyTime" title="申请时间" />
							<display:column property="startTime" title="开始时间" />
							<display:column property="endTime" title="结束时间" />
							<display:column title="当前节点">
								<a class="trace" href='#' pid="${leave.processInstance.id }" title="点击查看流程图">${leave.task.name }</a>
							</display:column>
							<display:column property="task.createTime" title="任务创建时间" />
							<display:column title="流程状态">
								${leave.processInstance.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${leave.processDefinition.version }</b>
							</display:column>
							<display:column title="操作">
								<c:if test="${empty leave.task.assignee }">
									<a class="claim" href="../leave/claim/${leave.task.id}">签收</a>
								</c:if>
								<c:if test="${not empty leave.task.assignee }">
									<%-- 此处用tkey记录当前节点的名称 --%>
									<a class="handle" tkey='${leave.task.taskDefinitionKey }' tname='${leave.task.name }' oaId='${leave.id }' oaTId='${leave.task.id }' href="#">办理</a>
								</c:if>
							</display:column>
						</display:table>
                        </table>
                     </div>
                  </div>
               </section>
            </section>
			<!-- END WRAPPER  -->
			<!-- 部门领导审批 -->
			<div id="deptLeaderAudit" style="display: none">
		
				<!-- table用来显示信息，方便办理任务 -->
				<%@include file="view-form.jsp" %>
			</div>
		
			<!-- HR审批 -->
			<div id="hrAudit" style="display: none">
		
				<!-- table用来显示信息，方便办理任务 -->
				<%@include file="view-form.jsp" %>
			</div>
			<div id="modifyApply" style="display: none">
				<div class="info" style="display: none"></div>
				<div id="radio">
					<input type="radio" id="radio1" name="reApply" value="true" /><label for="radio1">调整申请</label>
					<input type="radio" id="radio2" name="reApply" checked="checked" value="false" /><label for="radio2">取消申请</label>
				</div>
				<hr />
				<table id="modifyApplyContent" style="display: none">
					<caption>调整请假内容</caption>
					<tr>
						<td>请假类型：</td>
						<td>
							<select id="leaveType" name="leaveType">
								<option>公休</option>
								<option>病假</option>
								<option>调休</option>
								<option>事假</option>
								<option>婚假</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>开始时间：</td>
						<td><input type="text" id="startTime" name="startTime" /></td>
					</tr>
					<tr>
						<td>结束时间：</td>
						<td><input type="text" id="endTime" name="endTime" /></td>
					</tr>
					<tr>
						<td>请假原因：</td>
						<td>
							<textarea id="reason" name="reason" style="width: 250px;height: 50px"></textarea>
						</td>
					</tr>
				</table>
			</div>
		
			<!-- 销假 -->
			<div id="reportBack" style="display: none">
				<!-- table用来显示信息，方便办理任务 -->
				<%@include file="view-form.jsp" %>
				<hr/>
				<table>
					<tr>
						<td>实际请假开始时间：</td>
						<td>
							<input id="realityStartTime" />
						</td>
					</tr>
					<tr>
						<td>实际请假开始时间：</td>
						<td>
							<input id="realityEndTime" />
						</td>
					</tr>
				</table>
			</div>
         </section>
		 <!-- END MAIN CONTENT --> 
		 <!-- BEGIN FOOTER --> 
         <%@ include file="../../footer.jsp"%>
		 <!-- END FOOTER --> 
      </section>
	  <!-- END SECTION -->
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
		<script src="../js/common/plugins/jui/jquery-ui-1.9.2.min.js" type="text/javascript"></script>
		<script src="../js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
		<script src="../js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js" type="text/javascript"></script>
		<script src="../js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
		<script src="../js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
		<script src="../js/common/plugins/blockui/jquery.blockUI.js" type="text/javascript"></script>
		<script src="../js/module/activiti/workflow.js" type="text/javascript"></script>
		<script src="../js/module/oa/leave/leave-todo.js" type="text/javascript"></script> 
		<script src="../js/book.js" ></script>
	  <!-- END JS --> 
	  <div class="modal-backdrop fade in" style="display: none;" id="zhezhaocengId"></div>
</body>
</html>