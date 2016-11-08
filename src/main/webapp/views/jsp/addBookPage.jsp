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
<title>新建书籍</title>
<link rel="shortcut icon" href="../img/favicon.ico">
<link href="../css/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../css/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
<link href="../assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON STYLESHEET -->
<link rel="stylesheet" href="../assets/data-tables/DT_bootstrap.css"><!-- DATATABLE CSS -->
<link rel="stylesheet" href="../assets/data-tables/bootstrap-fileupload.css"><!-- DATATABLE CSS -->
<link href="../css/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
<link href="../css/style-responsive.css" rel="stylesheet"><!-- THEME BASIC RESPONSIVE  CSS --></head>
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-fileupload/bootstrap-fileupload.css"><!-- BOOTSTRAP FILEUPLOAD PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-wysihtml5/bootstrap-wysihtml5.css"><!-- BOOTSTRAP WYSIHTML5 PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-datepicker/css/datepicker.css"><!-- BOOTSTRAP DATEPICKER PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-timepicker/compiled/timepicker.css"><!-- BOOTSTRAP TIMEPICKER PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-colorpicker/css/colorpicker.css"><!-- BOOTSTRAP COLORPICKER PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-daterangepicker/daterangepicker-bs3.css"><!-- DATERANGE PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/bootstrap-datetimepicker/css/datetimepicker.css"><!-- DATETIMEPICKER PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../assets/jquery-multi-select/css/multi-select.css"><!-- JQUERY MULTI SELECT PLUGIN CSS -->
<link rel="stylesheet" type="text/css" href="../extjs5.0/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css">  
<script type="text/javascript" src="../extjs5.0/ext-all.js"></script>  
<script type="text/javascript" src="../extjs5.0/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>  
<script src="../js/jquery-1.8.3.min.js" ></script><!-- BASIC JS LIABRARY 1.8.3 -->
<script type="text/javascript" src="../js/jquery.form.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#uploading_book_form').ajaxForm({
	    beforeSend: function() {
	    },
	    uploadProgress: function(event, position, total, percentComplete) {
	       
	    },
	    success: function(data) {
	    	if(data){
	    		alert("上传成功");
	    	}else{
	    		alert("上传失败");
	    	}
	    },
		complete: function(xhr) {
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			console.log("上传文件出错");
		}
		}); 

});

</script>
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
                     <span class="label label-primary">新建书籍</span>
                     <span class="tools pull-right">
                     <a href="javascript:;" class="fa fa-chevron-down"></a>
                     <a href="javascript:;" class="fa fa-times"></a>
                     </span>
                  </header>
                  <div class="panel-body">
                  <form action="../book/addBookSave" method="post" id="uploading_book_form" enctype="multipart/form-data">
                     <div class="adv-table editable-table ">
                        <div class="clearfix">
                        </div>
                        <div class="space15"></div>
                        <div class="form-group">
                      <label class="control-label col-md-3">
                        		选择书籍
                      </label>
                      <div class="controls col-md-9">
                        <div class="fileupload fileupload-new" data-provides="fileupload">
                          <span class="btn btn-white btn-file">
                            <span class="fileupload-new">
                              <i class="fa fa-paper-clip">
                              </i>
                              Select file
                            </span>
                            <span class="fileupload-exists">
                              <i class="fa fa-undo">
                              </i>
                              Change
                            </span>
                            <input type="file" name="bookFile" class="default">
                          </span>
                          <span class="fileupload-preview m-l-5">
                          </span>
                          <a href="#" class="close fileupload-exists" data-dismiss="fileupload">
                          </a>
                        </div>
                      </div>
                    </div>
                    
                    <div class="form-group ">
                      <label for="cname" class="control-label col-lg-2">书籍名称:</label>
                      <div class="col-lg-10">
                         <input class=" form-control" id="bookName" name="bookName" type="text" required="">
                      </div>
                   </div>
                   <div class="form-group ">
                      <label for="cemail" class="control-label col-lg-2">作者:</label>
                      <div class="col-lg-10">
                         <select class="form-control m-bot15"  id="authorId" name="authorId">
                         	<c:forEach var="item" items="${authorList}" varStatus="status">
							         <option value="${item.id}">${item.name}</option>
							</c:forEach> 
                         </select>
                      </div>
                   </div>
                   <div class="form-group ">
                      <label for="curl" class="control-label col-lg-2">书籍类型:</label>
                      <div class="col-lg-10">
                         <select class="form-control m-bot15"  id="bookType" name="bookType">
                         		<option value="10000">言情</option>
								<option value="10001">动作</option>
                         </select>
                      </div>
                   </div>
                   <div class="form-group ">
                      <label for="curl" class="control-label col-lg-2">书籍简介:</label>
                      <div class="col-lg-10">
                      	<textarea rows="5" cols="157" id="bookSynopsis" name="bookSynopsis"></textarea>
                      </div>
                   </div>
                   
                     </div>
                     <div class="modal-footer" align="center" style="margin-top: 304px;">
		                <button class="btn btn-success" type="submit" style="margin-right: 800px;">
		                  Submit
		                </button>
		              </div>
                    </form>
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
		
		<script src="../js/bootstrap.min.js" ></script><!-- BOOTSTRAP JS  -->
		<script src="../js/jquery.dcjqaccordion.2.7.js"></script><!-- ACCORDING JS -->
		<script src="../js/jquery.scrollTo.min.js" ></script><!-- SCROLLTO JS  -->
		<script src="../js/jquery.nicescroll.js" > </script><!-- NICESCROLL JS  -->
		<script src="../assets/data-tables/jquery.dataTables.js"></script><!-- DATATABLE JS  -->
		<script src="../assets/data-tables/DT_bootstrap.js"></script><!-- DATATABLE JS  -->
		<script src="../js/respond.min.js" ></script><!-- RESPOND JS  -->
		<script src="../js/common-scripts.js" ></script><!-- BASIC COMMON JS  -->
		<script src="../js/editable-table.js" ></script><!-- EDITABLE TABLE JS  -->
	
		<script src="../js/book.js" ></script>
	  <!-- END JS --> 
	  <div class="modal-backdrop fade in" style="display: none;" id="zhezhaocengId"></div>
</body>
</html>