$(document).ready(function(){
	$.ajax({
		url:"manage/info/getFileList.do",
		type:"post",
		dataType:"json",
		success:function(data) {
			console.log(data)
		    if(data.status=="success") {
		    	var fileList = data.files;
		    	var str = "";
		    	for (var i = 0; i < fileList.length; i++) {
					str+="<li> "+(i+1)+". <a href='/upload/resources/files/"+fileList[i]+"'>"+fileList[i]+"</a></li>";
				}
		    	$("#fileDiv").html(str);
		    	var imgList = data.images;
		    	var str = "";
		    	for (var i = 0; i < imgList.length; i++) {
					str+="<li> "+(i+1)+". <a href='/upload/resources/images/"+imgList[i]+"' target='_0'>"+imgList[i]+"</a></li>";
				}
		    	$("#imgDiv").html(str);
		    	
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
});
