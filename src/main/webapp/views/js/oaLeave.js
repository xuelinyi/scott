/**
 * 																	account model  start
 * @param accountId
 */
function saveLeaveApply() {
	$.ajax({	
		url:'../leave/start',
		type:"POST",
		async:false,
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"leaveType": "'+$("#leaveType").val()+'","startTime": "'+$("#startTime").val()+'","endTime": "'++$("#endTime").val()++'","reason": "'++$("#reason").val()++'"}',
		success:function(result){
			
		},
		error:function(){
			 console.log("根据ID获取账户失败");
		}
	});	
}
function getAccount(accountId) {
	showdiv();
	$("#myModalTitle").html("编辑用户");
	$("#saveAccountId").unbind("click");
	var roleList = [];
	$.ajax({	
		url:'../system/getAccount',
		type:"GET",
		async:false,
		data:{'accountId': accountId},
		success:function(result){
			$("#addAndUpdateAccount").show();
			$("#accountId").val(result.id);
			$("#name").val(result.name);
			$("#password").val(result.password);
			$("#phoneNumber").val(result.phoneNumber);
			$("#email").val(result.email);
			$("#desc").val(result.desc);
			roleList = result.roleList
			$("#roleListId").html("");
			var allRoleList = findRoleList();
			var num = 0;
			$.each(allRoleList, function (i, role) { 
				if((null!=roleList)&&(0!=roleList.length)) {
					num = 0;
					for(var i=0;i<roleList.length;i++){
						if(roleList[i].id == role.id) {
							jQuery("#roleListId").append("<label class=\"checkbox-inline\"><input type=\"checkbox\" checked=\"checked\" name=\"roleId\" id="+role.id+">"+ role.name+"</label>");
						}else{
							num++;
							if(num==roleList.length) {
								jQuery("#roleListId").append("<label class=\"checkbox-inline\"><input type=\"checkbox\" name=\"roleId\" id="+role.id+">"+ role.name+"</label>");
							}
						}
					}
				}else{
					jQuery("#roleListId").append("<label class=\"checkbox-inline\"><input type=\"checkbox\" name=\"roleId\" id="+role.id+">"+ role.name+"</label>");
				}
			});
			$("#saveAccountId").bind("click",function(){updateAccount();}); 
		},
		error:function(){
			 console.log("根据ID获取账户失败");
		}
	});	
}
function updateAccount() {
	var accountId = $("#accountId").val();
	var name = $("#name").val();
	var password = $("#password").val();
	var phoneNumber = $("#phoneNumber").val();
	var email = $("#email").val();
	var desc = $("#desc").val();
	var parentCheckedNode=$("#roleListId :input[name='roleId']:checked");
	var roleList='[]';
	var roleList = eval('('+roleList+')');
	parentCheckedNode.each(function(){
		var arr  =
	    {
	        "id" :  $(this).attr("id"),
	    }
		roleList.push(arr);
	}); 
	roleList = JSON.stringify(roleList);
	if(roleList=="[]"){
		alert("必须选择角色！");
		return;
	}
	$.ajax({
		url:'../system/updateAccount',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"id": '+accountId+',"name": "'+name+'","password": "'+password+'","phoneNumber": "'+phoneNumber+'","email": "'+email+'","desc": "'+desc+'","roleList": '+roleList+'}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存账户信息失败");
		}
	});		
}

function findRoleList() {
	var allRoleList;
	$.ajax({	
		url:'../system/findRoleList',
		type:"POST",
		async:false,
		data:{},
		success:function(result){
			allRoleList = result;
		},
		error:function(){
			 console.log("查询角色信息失败");
		}
	});		
	return allRoleList;
}
function showAddAccountDiv() {
	$("#saveAccountId").unbind("click");
	var allRoleList = findRoleList();
	$("#roleListId").html("");
	
	$.each(allRoleList, function (i, role) { 
		jQuery("#roleListId").append("<label class=\"checkbox-inline\"><input type=\"checkbox\" name=\"roleId\" id="+role.id+">"+ role.name+"</label>");
	});
	showdiv();
	cleanDivInfo();
	$("#myModalTitle").html("增加用户");
	$("#saveAccountId").bind("click",function(){addAccount();}); 
}
function addAccount() {
	var name = $("#name").val();
	var password = $("#password").val();
	var phoneNumber = $("#phoneNumber").val();
	var email = $("#email").val();
	var desc = $("#desc").val();
	var parentCheckedNode=$("#roleListId :input[name='roleId']:checked");
	var roleList='[]';
	var roleList = eval('('+roleList+')');
	parentCheckedNode.each(function(){
		var arr  =
	    {
	        "id" :  $(this).attr("id"),
	    }
		roleList.push(arr);
	}); 
	roleList = JSON.stringify(roleList);
	if(roleList=="[]"){
		alert("必须选择角色！");
		return;
	}
	$.ajax({
		url:'../system/addAccount',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"name": "'+name+'","password": "'+hex_md5(password)+'","phoneNumber": "'+phoneNumber+'","email": "'+email+'","desc": "'+desc+'","roleList": '+roleList+'}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存账户信息失败");
		}
	});		
}
function delAccount(accountId) {
	$.ajax({	
		url:'../system/deleteAccount',
		type:"GET",
		data:{'accountId': accountId},
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("根据ID删除账户失败");
		}
	});		
}
/**
 * 																	account model  end
 * @param accountId
 */
/**
 * 																	role model  start
 * @param accountId
 */
function getRole(roleId) {
	showdiv();
	$("#myModalTitle").html("编辑角色");
	$("#saveRoleId").unbind("click");
	var permissionList = [];
	$.ajax({	
		url:'../system/getRole',
		type:"GET",
		async:false,
		data:{'roleId': roleId},
		success:function(result){
			$("#addAndUpdateRole").show();
			$("#roleId").val(result.id);
			$("#name").val(result.name);
			$("#code").val(result.code);
			$("#desc").val(result.desc);
			permissionList = result.permissionList
			$("#permissionListListId").html("");
			var allPermissionList = findPermissionList();
			var num = 0;
			$.each(allPermissionList, function (i, permission) { 
				if((null!=permissionList)&&(0!=permissionList.length)) {
					num = 0;
					for(var i=0;i<permissionList.length;i++){
						if(permissionList[i].id == permission.id) {
							jQuery("#permissionListListId").append("<span><input type=\"checkbox\" checked=\"checked\" name=\"permissionId\" id="+permission.id+">"+ permission.name+"</span><br/>");
						}else{
							num++;
							if(num==permissionList.length) {
								jQuery("#permissionListListId").append("<span><input type=\"checkbox\" name=\"permissionId\" id="+permission.id+">"+ permission.name+"</span><br/>");
							}
						}
					}
				}else{
					jQuery("#permissionListListId").append("<span><input type=\"checkbox\" name=\"permissionId\" id="+permission.id+">"+ permission.name+"</span><br/>");
				}
			});
			$("#saveRoleId").bind("click",function(){updateRole();}); 
		},
		error:function(){
			 console.log("根据ID获取角色失败");
		}
	});	
}
function updateRole() {
	var roleId = $("#roleId").val();
	var name = $("#name").val();
	var code = $("#code").val();
	var desc = $("#desc").val();
	var parentCheckedNode=$("#permissionListListId :input[name='permissionId']:checked");
	var permissionList='[]';
	permissionList = eval('('+permissionList+')');
	parentCheckedNode.each(function(){
		var arr  =
	    {
	        "id" :  $(this).attr("id"),
	    }
		permissionList.push(arr);
	}); 
	permissionList = JSON.stringify(permissionList);
	if(permissionList=="[]"){
		alert("必须选择权限！");
		return;
	}
	$.ajax({
		url:'../system/updateRole',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"id": '+roleId+',"name": "'+name+'","code": "'+code+'","desc": "'+desc+'","permissionList": '+permissionList+'}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存角色信息失败");
		}
	});		
}
function findPermissionList() {
	var allPermissionList;
	$.ajax({	
		url:'../system/findPermissionList',
		type:"POST",
		async:false,
		data:{},
		success:function(result){
			allPermissionList = result;
		},
		error:function(){
			 console.log("查询权限信息失败");
		}
	});		
	return allPermissionList;
}
function showAddRoleDiv() {
	showdiv();
	$("#myModalTitle").html("增加角色");
	cleanDivInfo();
	$("#saveRoleId").unbind("click");
	var allPermissionList = findPermissionList();
	$("#permissionListListId").html("");
	
	$.each(allPermissionList, function (i, permission) { 
		jQuery("#permissionListListId").append("<span><input type=\"checkbox\" name=\"permissionId\" id="+permission.id+">"+ permission.name+"</span><br/>");
	});
	$("#addAndUpdateRole").show();
	$("#saveRoleId").bind("click",function(){addRole();}); 
}
function addRole() {
	var name = $("#name").val();
	var code = $("#code").val();
	var desc = $("#desc").val();
	var parentCheckedNode=$("#permissionListListId :input[name='permissionId']:checked");
	var permissionList='[]';
	permissionList = eval('('+permissionList+')');
	parentCheckedNode.each(function(){
		var arr  =
	    {
	        "id" :  $(this).attr("id"),
	    }
		permissionList.push(arr);
	}); 
	permissionList = JSON.stringify(permissionList);
	if(permissionList=="[]"){
		alert("必须选择权限！");
		return;
	}
	$.ajax({
		url:'../system/addRole',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"name": "'+name+'","code": "'+code+'","desc": "'+desc+'","permissionList": '+permissionList+'}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存角色信息失败");
		}
	});			
}
function delRole(roleId) {
	$.ajax({	
		url:'../system/deleteRole',
		type:"GET",
		data:{'roleId': roleId},
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("删除角色信息失败！");
		}
	});		
}
/**
 * 																	role model  end
 * @param accountId
 */
/**
 * 																	account model  start
 * @param accountId
 */
function getAccountIp(accountIpId) {
	$("#myModalTitle").html("编辑IP地址");
	showdiv();
	$("#saveAccountIpId").unbind("click");
	$.ajax({	
		url:'../system/getAccountIp',
		type:"GET",
		async:false,
		data:{'accountIpId': accountIpId},
		success:function(result){
			$("#addAndUpdateAccountIp").show();
			$("#accountIpId").val(result.id);
			$("#accountName").val(result.accountName);
			$("#ip").val(result.ip);
			$("#netmask").val(result.netmask);
			$("#saveAccountIpId").bind("click",function(){updateAccountIp();}); 
			var allAccountDTOList = findAccountDTOList();
			if(null != allAccountDTOList) {
				jQuery("#accountId").append("<option value=所有用户 selected=\"selected\">所有用户</option>"); 
				$.each(allAccountDTOList, function (i, item) { 
					if(item.name == result.accountName) {
						jQuery("#accountId").append("<option value="+ item.name+" selected=\"selected\">"+ item.name+"</option>");
					}else{
						jQuery("#accountId").append("<option value="+ item.name+">"+ item.name+"</option>");
					}
				}); 
			}	
		},
		error:function(){
			 console.log("根据ID获取账户IP地址失败！！！");
		}
	});	
}
function updateAccountIp() {
	var accountIpId = $("#accountIpId").val();
	var accountName = $("#accountId").val();
	var ip = $("#ip").val();
	var netmask = $("#netmask").val();
	$.ajax({
		url:'../system/updateAccountIp',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"id": '+accountIpId+',"accountName": "'+accountName+'","ip": "'+ip+'","netmask": "'+netmask+'"}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存账户IP信息失败");
		}
	});		
}
function showAddAccountIpDiv() {
	$("#myModalTitle").html("增加IP地址");
	showdiv();
	cleanDivInfo();
	$("#saveAccountIpId").unbind("click");
	$("#addAndUpdateAccountIp").show();
	$("#saveAccountIpId").bind("click",function(){addAccountIp();}); 
	var allAccountDTOList = findAccountDTOList();
	if(null != allAccountDTOList) {
		jQuery("#accountId").append("<option value=所有用户>所有用户</option>"); 
		$.each(allAccountDTOList, function (i, item) { 
			jQuery("#accountId").append("<option value="+ item.name+">"+ item.name+"</option>"); 
		}); 
	}
}
function addAccountIp() {
	var accountName = $("#accountId").val();
	var ip = $("#ip").val();
	var netmask = $("#netmask").val();
	$.ajax({
		url:'../system/addAccountIp',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"accountName": "'+accountName+'","ip": "'+ip+'","netmask": "'+netmask+'"}',
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("保存账户IP信息失败");
		}
	});		
}
function delAccountIp(accountIpId) {
	$.ajax({	
		url:'../system/deleteAccountIp',
		type:"GET",
		data:{'accountIpId': accountIpId},
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("根据ID删除IP地址失败");
		}
	});		
}
function findAccountDTOList() {
	var allAccountDTOList;
	$.ajax({	
		url:'../system/findAccountDTO',
		type:"POST",
		async:false,
		data:{},
		success:function(result){
			allAccountDTOList = result;
		},
		error:function(){
			 console.log("查询角色信息失败");
		}
	});		
	return allAccountDTOList;
}
/**
 * 																	account model  end
 * @param accountId
 */

/**
 * 																	sysConfig model  start
 * @param accountId
 */
function getSysConfigure(sysConfigureId) {
	showdiv();
	$("#myModalTitle").html("编辑系统参数");
	$("#saveSysConfigureId").unbind("click");
	$.ajax({	
		url:'../system/getSysConfigure',
		type:"GET",
		async:false,
		data:{'sysConfigureId': sysConfigureId},
		success:function(result){
			$("#addAndUpdateSysConfigure").show();
			$("#id").val(result.id);
			$("#name").val(result.name);
			$("#value").val(result.value);
			$("#saveSysConfigureId").bind("click",function(){updateSysConfigure();}); 
		},
		error:function(){
			 console.log("根据ID获取系统参数失败！！！");
		}
	});	
}
function updateSysConfigure() {
	var id = $("#id").val();
	var name = $("#name").val();
	var value = $("#value").val();
	$.ajax({
		url:'../system/updateSysConfigure',
		type:"POST",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:'{"id": "'+id+'","name": "'+name+'","value": "'+value+'"}',
		success:function(result){
			$("#addAndUpdateSysConfigure").hide();
			location.reload();
		},
		error:function(){
			 console.log("保存系统参数信息失败");
		}
	});		
}
/**
 * 																	sysConfig model  end
 * @param accountId
 */