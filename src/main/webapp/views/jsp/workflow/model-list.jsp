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
                        <div style="text-align: right"><button id="create">创建</button></div>
                        <table class="table table-striped table-hover table-bordered" id="editable-sample">
                           <display:table id="model" name="modelList" pagesize="5" class="table">
	                        <display:setProperty name="sort.amount" value="list"></display:setProperty>
	                        <display:column property="id" title="ID" sortable="true" />
							<display:column property="key" title="KEY" sortable="true"/>
							<display:column property="name" title="Name" />
							<display:column property="version" title="Version" />
							<display:column property="createTime" title="创建时间" />
							<display:column property="lastUpdateTime" title="最后更新时间" />
							<display:column property="metaInfo" title="元数据" />
							<display:column title="操作">
								 <a href=''>编辑</a>
                        		 <a href=''>部署</a>
                        		 <a href=''>导出</a>
                        		 <a href=''>删除</a>
							</display:column>
						</display:table>
                        </table>
                     </div>
                  </div>
               </section>
            </section>
         </section>
		 <!-- END MAIN CONTENT -->
		<div id="createModelTemplate" title="创建模型" class="template">
        <form id="modelForm" action="../model/create" target="_blank" method="post">
		<table>
			<tr>
				<td>名称：</td>
				<td>
					<input id="name" name="name" type="text" />
				</td>
			</tr>
			<tr>
				<td>KEY：</td>
				<td>
					<input id="key" name="key" type="text" />
				</td>
			</tr>
			<tr>
				<td>描述：</td>
				<td>
					<textarea id="description" name="description" style="width:300px;height: 50px;"></textarea>
				</td>
			</tr>
		</table>
        </form>
	</div> 
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
    	$('#create').button({
    		icons: {
    			primary: 'ui-icon-plus'
    		}
    	}).click(function() {
    		$('#createModelTemplate').dialog({
    			modal: true,
    			width: 500,
    			buttons: [{
    				text: '创建',
    				click: function() {
    					if (!$('#name').val()) {
    						alert('请填写名称！');
    						$('#name').focus();
    						return;
    					}
                        setTimeout(function() {
                            location.reload();
                        }, 1000);
    					$('#modelForm').submit();
    				}
    			}]
    		});
    	});
    });
    </script>
</body>
</html>