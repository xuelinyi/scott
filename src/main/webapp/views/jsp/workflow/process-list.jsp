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
<title>流程列表</title>
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
                     <span class="label label-primary">流程列表</span>
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
                        <div style="text-align: right;padding: 2px 1em 2px">
							<div id="message" class="info" style="display:inline;"><b>提示：</b>点击xml或者png链接可以查看具体内容！</div>
							<a id='deploy' href='#'>部署流程</a>
							<a id='redeploy' href='../workflow/redeploy/all'>重新部署流程</a>
						</div>
						<fieldset id="deployFieldset" style="display: none">
							<legend>部署新流程</legend>
							<div><b>支持文件格式：</b>zip、bar、bpmn、bpmn20.xml</div>
							<form action="../workflow/deploy" method="post" enctype="multipart/form-data">
								<input type="file" name="file" />
								<input type="submit" value="Submit" />
							</form>
						</fieldset>
                        <table class="table table-striped table-hover table-bordered" id="editable-sample">
                           <display:table id="object" name="objectList" pagesize="5" class="table">
	                        <display:setProperty name="sort.amount" value="list"></display:setProperty>
	                        <display:column title="ProcessDefinitionId" sortable="true">
	                        		${object[0].id }
	                        </display:column>
							<display:column title="DeploymentId" sortable="true" >
								${object[0].deploymentId }
	                        </display:column>
							<display:column title="名称" >
								${object[0].name }
	                        </display:column>
							<display:column title="KEY" sortable="true" >
								${object[0].key }
	                        </display:column>
							<display:column title="版本号" sortable="true" >
								${object[0].version }
	                        </display:column>
							<display:column title="XML" >
								<a target="_blank" href='../resource/read?processDefinitionId=${object[0].id}&resourceType=xml'>${object[0].resourceName }</a>
	                        </display:column>
							<display:column title="图片" sortable="true">
								<a target="_blank" href='../resource/read?processDefinitionId=${object[0].id}&resourceType=image'>${object[0].diagramResourceName }</a>
							</display:column>
							<display:column title="部署时间" sortable="true" >
								${object[1].deploymentTime }
							</display:column>
							<display:column title="是否挂起" >
								${object[0].suspended} |
								<c:if test="${object[0].suspended }">
									<a href="../processdefinition/update/active/${object[0].id}">激活</a>
								</c:if>
								<c:if test="${!object[0].suspended }">
									<a href="../processdefinition/update/suspend/${object[0].id}">挂起</a>
								</c:if>
							</display:column>
							<display:column title="操作" sortable="true">
								 <a href='../process/delete?deploymentId=${object[0].deploymentId}'>删除</a>
                        		 <a href='../workflow/process/convert-to-model/${object[0].id}'>转换为Model</a>
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
    $(function() {
    	$('#redeploy').button({
    		icons: {
    			primary: 'ui-icon-refresh'
    		}
    	});
    	$('#deploy').button({
    		icons: {
    			primary: 'ui-icon-document'
    		}
    	}).click(function() {
    		$('#deployFieldset').toggle('normal');
    	});
    });
</script>
</body>
</html>