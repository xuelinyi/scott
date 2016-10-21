<%@page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="extjs5.0/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css">  
<script type="text/javascript" src="extjs5.0/ext-all.js"></script>  
<script type="text/javascript" src="extjs5.0/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>  
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<title>EXTJS</title>
<script type="text/javascript">
Ext.onReady(function(){  
    Ext.MessageBox.alert('hello','HELLO WORLD!');  
}); 
</script>
</head>
<body>
<display:table name="resultList" requestURI="/testDisplaytag" />
</body>
</html>