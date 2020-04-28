/**
 * 
 */

var ue = UE.getEditor( 'editor', {
	//关闭elementPath
    elementPathEnabled:false,
    initialFrameHeight:283

});
function getarticle(){
	var type=$("#teachPoint").find("option:selected").val();
	console.log(type);
	$.ajax({
		url:"manage/info/queryArticle.do",
		type:"post",
		data:{"type":type},
		dataType:"json",
		success:function(data) {
		    if(data.status=="success") {
		    	console.log(data);
		    	$("#article_title").text(data.usersData.article_title);
		    	$("#article_content").html(data.usersData.article_value);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
function setarticle(){
	var type=$("#teachPoint").find("option:selected").val();
	var content=UE.getEditor('editor').getContent();
//	var content=$(".modal-body").html();
	console.log(type+","+content);
	$.ajax({
		url:"manage/info/saveArticleEdit.do",
		type:"post",
		data:{"type":type,"content":content},
		dataType:"json",
		success:function(data) {
		    if(data.status=="success") {
		    	$("#article_content").html(content);
		    	console.log(data);
		    	$("#article_edit").modal("hide");
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
function article_edit(){
	$(".modal-title").text($("#article_title").text());
	UE.getEditor('editor').setContent($("#article_content").html());
	UE.getEditor('editor').focus();  
	$("#article_edit").modal("show");
}
$(document).ready(function(){
	var type=$("#teachPoint").find("option:selected").val();
	console.log(type);
	getarticle();
	$("#imageFile").change(function(){
		var form = new FormData(document.getElementById("imageFileForm"));
		console.log(form);
		var filePath = $(this).val();  
		if("" != filePath){  
		    var fileType = getFileType(filePath);  
		    console.log(fileType);
		    //判断上传的附件是否为图片  
		    if("jpg"==fileType && "png"==fileType && "docx"==fileType && "doc"==fileType  && "xls"==fileType &&  "xlsx"==fileType && "jpeg"==fileType ){  
		    	$(this).val("");  
		        showSuccessOrErrorModal("请上传JPG,PNG,JPEG,DOC,DOCX,DOC,XLS,XLSX格式的图片","error");
		    }else{  
		        //获取附件大小（单位：KB）  
		        var fileSize = this.files[0].size / 1024;  
		        console.log(fileSize+"KB")
		        if(fileSize > 2048){  
		            showSuccessOrErrorModal("图片大小不能超过2MB","error");
		        }else{
		        	$.ajax({
		    			url:"manage/info/uploadImage.do",
		    			type:"post",
		    			data:form,
		    			processData:false,  
		    	        contentType:false, 
		    	        dataType:"json",
		    			success:function(data) {
		    				console.log(data);
		    			    if(data.status == "success") {
		    			    	console.log(data);
		    			    	var fileData = data.fileData;
		    			    	var dom = "";
		    			    	UE.getEditor('editor').focus();  
		    			    	if(data.fileData.type=="JPEG"||data.fileData.type=="PNG"){
		    			    		dom = '<p style="text-align:center"><img src="'+fileData.url+'" title="" _src="'+fileData.url+'" alt="'+fileData.name+'"></p>';
		    			    	}else{
		    			    		dom = '<p style="line-height: 16px;"><img src=""><a style="font-size:12px; color:#0066cc;" href="'+fileData.url+'" title="'+fileData.name+'">'+fileData.name+'</a></p>'
		    			    	}
		    			    	
		    			    	UE.getEditor('editor').execCommand('inserthtml',''+dom+'');  
		    			    } else {
		    			    	showSuccessOrErrorModal(data.msg,"error");
		    			    }         
		    			},
		    			 
		    			error:function(e) {
		    			    showSuccessOrErrorModal("请求出错了","error"); 
		    			}
		    		});
		        } 
		    }  
		}else{
			showSuccessOrErrorModal("没有选择的文件","error");
		}
	});
	
});
function getFileType(filePath){  
    var startIndex = filePath.lastIndexOf(".");  
    if(startIndex != -1)  
        return filePath.substring(startIndex+1, filePath.length).toLowerCase();  
    else return "";  
}  