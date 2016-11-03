function getAccount(accountId) {
	$.ajax({	
		url:'../system/getAccount',
		type:"GET",
		data:{'accountId': accountId},
		success:function(result){
			$("#addAndUpdateAccount").show();
			$("#authorId").val(result.id);
			$("#name").val(result.name);
			$("#age").val(result.age);
			$("#sex").val(result.sex);
			$("#birthDate").val(result.birthday);
			$("#saveAuthorId").bind("click",function(){updateAuthror();}); 
		},
		error:function(){
			 console.log("根据ID获取书籍失败");
		}
	});		
}
function updateAuthror() {
	var id = $("#authorId").val();
	var name = $("#name").val();
	var age = $("#age").val();
	var sex = $("#sex").val();
	var birthday = $("#birthday").val();
	$.ajax({	
		url:'../author/updateAuthor',
		type:"POST",
		data:{"id":id,'name': name,'age': age,'sex': sex,'birthday': birthday},
		success:function(result){
			$("#addAndUpdateAuthor").hide();
			cleanDivInfo();
			location.reload();
		},
		error:function(){
			 console.log("编辑作者信息失败。");
		}
	});		
}
function cleanDivInfo() {
	$("#addAndUpdateAuthor input").val("");
}
function findRoleList() {
	$.ajax({	
		url:'../system/findRoleList',
		type:"POST",
		data:{},
		success:function(result){
			$.each(result, function (i, role) { 
				jQuery("#roleListId").append("<span><input type=\"checkbox\" name=\"roleId\" id="+role.id+">"+ role.name+"</span><br/>"); 
			}); 
		},
		error:function(){
			 console.log("保存作者信息失败");
		}
	});		
}
function showAddAccountDiv() {
	findRoleList();
	$("#addAndUpdateAccount").show();
	$("#saveAccountId").bind("click",function(){addAccount();}); 
}
function addAccount() {
	var name = $("#name").val();
	var password = $("#password").val();
	var phoneNumber = $("#phoneNumber").val();
	var email = $("#email").val();
	var desc = $("#desc").val();
	var parentCheckedNode=$("#roleListId :input[name='roleId']:checked");
	var roleList = [];
	parentCheckedNode.each(function(){
		var role =  {};
		role.id = $(this).attr("id");
		roleList.push(role);
	}); 
	roleList = JSON.stringify(roleList);
	$.ajax({
		url:'../system/addAccount',
		type:"POST",
		async:true,
		contentType: "application/json; charset=utf-8",
		data:'{"name": '+name+',"password": '+password+',"phoneNumber": '+phoneNumber+',"email": '+email+',"desc": '+desc+',"roleList": '+roleList+'}',
		success:function(result){
			$("#addAndUpdateAuthor").hide();
			cleanDivInfo();
			location.reload();
		},
		error:function(){
			 console.log("保存作者信息失败");
		}
	});		
}
function delAuthor(authorId) {
	$.ajax({	
		url:'../author/deleteAuthor',
		type:"GET",
		data:{'authorId': authorId},
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("根据ID获取书籍失败");
		}
	});		
}