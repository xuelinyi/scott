
<%@page pageEncoding="UTF-8" language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>登录页面</title>
		<meta content="login.jsp" />
		<link href="css/login.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/md5.js"></script>
	</head>
	<body id="login">
	<form id="loginForm" method="post" action="<c:url value='security_check'/>">
	<input type="hidden" name="${_csrf.parameterName}" 	value="${_csrf.token}" />
		<div class="dsp_box">		
			<div class="dsp_main">
				<div class="login_box clearfix">
				  	<div class="login_left">
					    <div class="logo">
					    </div>
					</div>
				  	<div class="login_right">
				  	
				     	<h1 class="login_title">账户登录</h1>
				     	<div class="clear"></div>
					    <div style="width: 350px;height: 120px;margin-top:70px">
					    	<div class="input_box">
						    	<span>用户名：</span>
						       	<input type="text" name="username" id="username" class="user" placeholder="请输入您的用户名"/>
						    </div>
						    <div class="input_box">
						    	<span>密码：</span>
						       	<input type="password" name="password" id="password" class="password" placeholder="请输入您的密码" />
						    </div>
						    
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
						</div>
				    	<button class="login_btn" onmousedown="toPwdMD5();" onclick="$('#loginForm').submit();"> 登录</button>
				   
				  	</div>
				
				</div>
			</div>
		</div>
		 </form>
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
	</script>
</html>