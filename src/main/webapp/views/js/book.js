function getBook(bookId) {
	showdiv();
	$.ajax({	
		url:'../book/getTemporaryBook',
		type:"GET",
		data:{'bookId': bookId},
		success:function(result){
			$("#addAndUpdateBook").show();
			if(null != result.bookTemporary) {
				$("#bookId").val(result.bookTemporary.id);
				$("#bookName").val(result.bookTemporary.bookName);
				$("#bookType").val(result.bookTemporary.bookType);
			}
			if(null != result.authorList) {
				$.each(result.authorList, function (i, item) { 
					jQuery("#authorId").append("<option value="+ item.id+">"+ item.name+"</option>"); 
				}); 
			}
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
	var authorId = $("#authorId").val();
	$.ajax({	
		url:'../book/updateTemporaryBook',
		type:"POST",
		data:{"id":id,'bookName': bookName,'bookType': bookType,'author.id':authorId},
		success:function(result){
			location.reload();
		},
		error:function(){
			 console.log("根据ID获取书籍失败");
		}
	});		
}