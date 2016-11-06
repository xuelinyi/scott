
<%@page pageEncoding="UTF-8" language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  lang="en">
	<head>
		<title>Olive Admin - Flat & Responsive Bootstrap Admin Template</title>
		<meta content="login.jsp" />
		<link href="css/bootstrap.min.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
		<link href="css/bootstrap-reset.css" rel="stylesheet"><!-- BOOTSTRAP CSS -->
		<link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet"><!-- FONT AWESOME ICON CSS -->
		<link href="css/style.css" rel="stylesheet"><!-- THEME BASIC CSS -->
		<link href="css/style-responsive.css" rel="stylesheet"><!-- THEME RESPONSIVE CSS -->
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/md5.js"></script>
	</head>
  <body class="login-screen"  id="login">
    <!-- BEGIN SECTION -->
    <div class="container">
      <form class="form-signin" id="loginForm" method="post" action="<c:url value='security_check'/>">
      <input type="hidden" name="${_csrf.parameterName}" 	value="${_csrf.token}" />
        <h2 class="form-signin-heading">
          sign in now
        </h2>
		<!-- LOGIN WRAPPER  -->
        <div class="login-wrap">
          <input type="text"  name="username" id="username"  class="form-control" placeholder="User ID" autofocus>
          <input type="password"  name="password" id="password" class="form-control" placeholder="Password">
          <c:if test="${requestScope['SPRING_SECURITY_LAST_EXCEPTION'].message eq 'Bad credentials'}">
			<h3 style="font-size:20; color:#FF1C19;">
							用户名或密码错误!
			</h3>
		</c:if>
		<c:if test="${requestScope['SPRING_SECURITY_LAST_EXCEPTION'].message eq 'User account is locked'}">
			<h3 style="font-size:20; color:#FF1C19;">
							用户已锁定!
			</h3>
		</c:if>
          <label class="checkbox">
            <input type="checkbox" value="remember-me">
            Remember me
            <span class="pull-right">
              <a data-toggle="modal" href="#myModal">
                Forgot Password?
              </a>
            </span>
          </label>
          <button class="btn btn-lg btn-login btn-block" type="submit"  onmousedown="toPwdMD5();" onclick="$('#loginForm').submit();">
            Sign in
          </button>
          <p>
            or you can sign in via social network
          </p>
          <div class="login-social-link" style="margin-left: 89px;">
            <a href="https://xuelinyi.github.io/" class="facebook"  target="_blank">
              <i class="fa fa-facebook">
              </i>
              GitHub
            </a>
          </div>
        </div>
		<!-- END LOGIN WRAPPER -->
		<!-- MODAL -->
        <div  id="myModal" class="modal fade">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title">
                  Forgot Password ?
                </h4>
              </div>
              <div class="modal-body">
                <p>
                  Enter your e-mail address below to reset your password.
                </p>
                <input type="text" name="email" placeholder="Email" autocomplete="off" class="form-control placeholder-no-fix">
              </div>
              <div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button" id="cancelForGot">
                  Cancel
                </button>
                <button class="btn btn-success" type="button" onclick="forgotPassword()">
                  Submit
                </button>
              </div>
            </div>
          </div>
        </div>
		<!-- END MODAL -->
      </form>
    </div>
    <!-- END SECTION -->
    <!-- BEGIN JS -->
    <script src="js/jquery.js" ></script><!-- BASIC JQUERY LIB. JS -->
	<script src="js/bootstrap.min.js" ></script><!-- BOOTSTRAP JS -->
    <!-- END JS -->
  </body>
		<script type="text/javascript">
		$('#login').keypress(function (e) {
			if (e.which == 13) {
				$("#password").val(hex_md5($("#password").val()));
				$('#loginForm').submit();
			}
		});
		function toPwdMD5(){
			$("#password").val(hex_md5($("#password").val()));
		}
		function forgotPassword() {
			var email = $("input[name='email']").val();
			$.ajax({	
				url:'system/forgotPassword',
				type:"GET",
				async:false,
				data:{'email': email},
				success:function(result){
 					if(result == 10001) {
						alert("邮箱不存在！！"); 
					 }else if(result == 10002) {
						 alert("邮箱错误！！"); 
					 }else{
						 $("#cancelForGot").trigger('click');
						 $("input[name='email']").val("");
						 alert("密码修改成功。请在邮件中查看新密码"); 
					 }
				},
				error:function(){
					
				}
			});	
		}
	</script>
</html>