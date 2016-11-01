function getBook(bookId) {
	$.ajax({	
		url:'../book/getBook',
		type:"GET",
		data:{'bookId': bookId},
		success:function(result){
			$("#addAndUpdateBook").show();
			$("#bookId").val(result.id);
			$("#bookName").val(result.bookName);
			$("#bookType").val(result.bookType);
		},
		error:function(){
			 console.log("根据ID获取书籍失败");
		}
	});		
}
function updateBook() {
	var id = $("#bookId").val();
	var bookName = $("#bookName").val();
	var bookType = $("#bookType").val();
	$.ajax({	
		url:'../book/updateBook',
		type:"POST",
		data:{"id":id,'bookName': bookName,'bookType': bookType},
		success:function(result){
			$("#addAndUpdateBook").hide();
		},
		error:function(){
			 console.log("根据ID获取书籍失败");
		}
	});		
}
function addAuthor() {
	$("#addAndUpdateAuthor").show();
}