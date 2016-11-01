function getAuthor(authorId) {
	$.ajax({	
		url:'../author/getAuthor',
		type:"GET",
		data:{'authorId': authorId},
		success:function(result){
			$("#addAndUpdateAuthor").show();
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
function addAuthor() {
	$("#addAndUpdateAuthor").show();
	$("#saveAuthorId").bind("click",function(){addAuthror();}); 
}
function addAuthror() {
	var name = $("#name").val();
	var age = $("#age").val();
	var sex = $("#sex").val();
	var birthDate = $("#birthDate").val();
	$.ajax({	
		url:'../author/addAuthor',
		type:"POST",
		data:{'name': name,'age': age,'sex': sex,'birthday': birthDate},
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