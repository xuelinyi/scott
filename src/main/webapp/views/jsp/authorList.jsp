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
<title>作者管理</title>
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
<body>
<!-- BEGIN SECTION --> 
      <section id="container" class="">
	   <!-- BEGIN HEADER -->
      <%@ include file="../jsp/header.jsp"%>
      <!-- END HEADER -->
      <!-- BEGIN SIDEBAR -->
     <%@ include file="../jsp/left.jsp"%>
      <!-- END SIDEBAR -->
		 <!-- BEGIN MAIN CONTENT --> 
         <section id="main-content">
		    <!-- BEGIN WRAPPER  -->
            <section class="wrapper site-min-height">
               <section class="panel">
                  <header class="panel-heading">
                     <span class="label label-primary">作者管理</span>
                     <span class="tools pull-right">
                     <a href="javascript:;" class="fa fa-chevron-down"></a>
                     <a href="javascript:;" class="fa fa-times"></a>
                     </span>
                  </header>
                  <div class="panel-body">
                     <div class="adv-table editable-table ">
                        <div class="clearfix">
                           <div class="btn-group">
                              <button id="editable-sample_new" class="btn btn-success green" onclick="addAuthor()">
                              Add New <i class="fa fa-plus"></i>
                              </button>
                           </div>
                        </div>
                        <div class="space15"></div>
                        <table class="table table-striped table-hover table-bordered" id="editable-sample">
                           <display:table id="author" name="authorList" requestURI="/author/authorList" pagesize="5" class="table">
	                        <display:setProperty name="sort.amount" value="list"></display:setProperty>
	                        <display:column property="id" title="标识" sortable="true" />
							<display:column property="name" title="名字" sortable="true"/>
							<display:column property="age" title="年龄" />
							<display:column property="sex" title="性别" />
							<display:column property="birthday" title="生日" />
							<display:column property="createTime" title="创建时间" />
							<display:column title="操作"><a href="javascript:void(0)" onclick="getBook(<c:out value="${author.id}"/>)">编辑</a></display:column>
						</display:table>
                        </table>
                     </div>
                     <div id="addAndUpdateAuthor" style="display: none;">
							<input id="authorId" style="display: none;">
							名字:<input id=name/><br/>
							年龄:<input id=age/><br/>
							性别:<select id="sex">
										<option value="10000">男</option>
										<option value="10001">女</option>
									</select><br/>
							生日:
							<button onclick="updateBook();">保存</button>
					</div>
                  </div>
               </section>
            </section>
			<!-- END WRAPPER  -->
         </section>
		 <!-- END MAIN CONTENT --> 
		 <!-- BEGIN FOOTER --> 
         <%@ include file="footer.jsp"%>
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
		<script src="../js/author.js" ></script>
	  <!-- END JS --> 
</body>
</html>