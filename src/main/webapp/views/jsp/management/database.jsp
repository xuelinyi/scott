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
<title>引擎数据库</title>
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
                     <span class="label label-primary">引擎数据库</span>
                     <span class="tools pull-right">
                     <a href="javascript:;" class="fa fa-chevron-down"></a>
                     <a href="javascript:;" class="fa fa-times"></a>
                     </span>
                  </header>
                  <div class="panel-body">
                     <div class="adv-table editable-table "  style="width: 400px;float: left;">
                        <div class="clearfix">
                        </div>
       <div class='page-title ui-corner-all'>表名</div>
            <ul class="unstyled">
                <c:forEach items="${tableCount}" var="table">
                    <li><a href="?tableName=${table.key}">${table.key}（${table.value}）</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="span8" style="float: left;">
            <c:if test="${not empty tableMetadata}">
            <div class='page-title ui-corner-all'>记录（${tableMetadata.tableName}）</div>
            <table width="100%" class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>行</th>
                        <c:forEach items="${tableMetadata.columnNames}" var="data" varStatus="row">
                            <th title="字段类型：${tableMetadata.columnTypes[row.index]}">${data}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                <c:set var="counter" value="1" />
                    <c:forEach items="${tablePages}" var="data">
                    <tr>
                        <td style="padding: 0 10px 0 10px">${counter}</td>
                        <c:set var="counter" value="${counter+1}" />
                        <c:forEach items="${tableMetadata.columnNames}" var="column">
                            <td>${data[column]}</td>
                        </c:forEach>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            </c:if>

            <c:if test="${empty tableMetadata}">
                <p class="text-info text-center" style="margin-top: 50%;font-size: 20px;">单击左边的表名可以查看记录！</p>
            </c:if>
        </div>
                     </div>
                  </div>
               </section>
            </section>
         </section>
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