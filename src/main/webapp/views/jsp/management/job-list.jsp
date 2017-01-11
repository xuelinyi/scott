<%@page import="org.activiti.engine.RepositoryService"%>
<%@page import="com.comverse.timesheet.web.util.ProcessDefinitionCache"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.*,org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.ObjectUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
<title>Job列表</title>
<link href="../css/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../css/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON STYLESHEET -->
<link rel="stylesheet" href="../assets/data-tables/DT_bootstrap.css"><!-- DATATABLE CSS -->
<link href="../css/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
<link href="../css/style-responsive.css" rel="stylesheet"><!-- THEME BASIC RESPONSIVE  CSS --></head>
<link rel="stylesheet" type="text/css" href="extjs5.0/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css">  
<script type="text/javascript" src="../extjs5.0/ext-all.js"></script>  
<script type="text/javascript" src="../extjs5.0/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<link href="../js/common/plugins/jui/themes/redmond/jquery-ui-1.9.2.custom.css" type="text/css" rel="stylesheet" />
<body>

<!-- BEGIN SECTION --> 
      <section id="container" class="">
	   <!-- BEGIN HEADER -->
      <%@ include file="../../jsp/header.jsp"%>
      <!-- END HEADER -->
      <!-- BEGIN SIDEBAR -->
     <%@ include file="../../jsp/left.jsp"%>
      <!-- END SIDEBAR -->
		 <!-- BEGIN MAIN CONTENT --> 
         <section id="main-content">
		    <!-- BEGIN WRAPPER  -->
            <section class="wrapper site-min-height">
               <section class="panel">
                  <header class="panel-heading">
                     <span class="label label-primary">Job列表</span>
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
                           <display:table id="job" name="jobList" pagesize="5" class="table">
	                        <display:setProperty name="sort.amount" value="list"></display:setProperty>
	                        <display:column property="id" title="作业ID" sortable="true" />
							<display:column  title="作业类型" >
								${JOB_TYPES[job.jobHandlerType]}
							</display:column>
							<display:column title="预定时间" >
								<fmt:formatDate value="${job.duedate}" pattern="yyyy-MM-dd hh:mm:ss"/>
							</display:column>
							<display:column property="retries" title="可重试次数" />
							<display:column property="processDefinitionId" title="流程定义ID" />
							<display:column property="processInstanceId" title="流程实例ID" />
							<display:column property="executionId" title="执行ID" />
							<display:column title="异常消息" >
								${job.exceptionMessage}
							</display:column>
							<display:column title="作业配置信息" >
								<c:if test="${job.jobHandlerType == 'async-continuation'}">
				                    到期时间：<fmt:formatDate value="${job.lockExpirationTime}" pattern="yyyy-MM-dd hh:mm:ss"/>
				                    <br/>
				                    锁标示(UUID)：${job.lockOwner}
				                </c:if>
				                ${job.jobHandlerConfiguration}
							</display:column>
							<display:column title="操作">
								 <div class="btn-group">
				                    <a class="btn btn-small btn-danger dropdown-toggle" data-toggle="dropdown" href="#">操作
				                        <span class="caret"></span>
				                    </a>
				                    <ul class="dropdown-menu" style="min-width: 100px;margin-left: -70px;">
				                        <li><a href="../managementexecuteJob/${job.id}"><i class="icon-play"></i>执行</a></li>
				                        <li><a href="../managementdeleteJob/${job.id}"><i class="icon-trash"></i>删除</a></li>
				                        <li><a href="#" title="更改执行次数" data-jobid="${job.id}" class="change-retries"><i class="icon-wrench"></i>执行次数</a></li>
				                    </ul>
				                </div>
							</display:column>
						</display:table>
                        </table>
                     </div>
                  </div>
               </section>
            </section>
         </section>
		 <!-- END MAIN CONTENT -->
		 <!-- BEGIN FOOTER --> 
         <%@ include file="../footer.jsp"%>
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
		<script src="../js/module/form/dynamic/dynamic-process-list.js" type="text/javascript"></script> 
		<script src="../js/common/common.js" type="text/javascript"></script>
		<script src="../js/common/plugins/validate/jquery.validate.pack.js" type="text/javascript"></script>
		<script src="../js/common/plugins/validate/messages_cn.js" type="text/javascript"></script>
	  <!-- END JS --> 
	  <div class="modal-backdrop fade in" style="display: none;" id="zhezhaocengId"></div>
	  	<!-- 办理任务对话框 -->
	<div id="handleTemplate" class="template"></div>
	<script type="text/javascript">
	<script type="text/javascript">
    $(function() {
        $('.change-retries').click(function() {
            var retries = prompt('请输入重试次数：');
            if (isNaN(retries)) {
                alert('请输入数字!');
            } else {
                location.href = '../management/changeJobRetries/' + $(this).data('jobid') + "?retries=" + retries;
            }
        });
    });
</script>
    </script>
</body>
</html>