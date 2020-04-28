//初始化表格
var articleManageTable= null;
var ue = UE.getEditor( 'editor', {
	//关闭elementPath
    elementPathEnabled:false,
});
function initarticleManageTable() {
	articleManageTable = $('#articleManageTable').DataTable({
		// url
		"sAjaxSource" : "manage/info/queryArticlesList.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{	
			aoData.push( { "name": "articleType", "value":$("#articleType").val()} );
			
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(aoData),
				success: function(data) 
				{
					
					if(data.status == "success")
					{
						fnCallback(data.usersData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取用户信息失败","error");
				}
			});
		},
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [3],
	        	
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "文章标题",  
			 "defaultContent" : "", 
			 "data" :"articleTitle",
			 "width": "20%",
			 "class" : "text-center"  
		 },{	
			 "title" : "文章描述",  
			 "defaultContent" : "", 
			 "data" :"articleDesc",
			 "width": "50%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "创建人",  
			 "defaultContent" : "", 
			 "data" :"creBy",
			 "width": "10%",
			 "class" : "text-center"  
		 } ,{	
			 "title" : "修改日期",  
			 "defaultContent" : "", 
			 "data" :"upTime",
			 "width": "10%",
			 "class" : "text-center" ,
			 "render": function(data, type, row, meta) {
        		 var timestamp = row.upTime;  
				 var newDate = new Date();  
				 newDate.setTime(timestamp);
				 return newDate.toLocaleDateString();
		     }
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
					content = '<button class="btn btn-xs blue" onclick="showArticleEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
		            '<button class="btn btn-xs red" onclick="deleteArticle(\''+row.id+'\')"> 删除 </button>';
			         return content;
		      } 
		 }]
	});
}	

//查询功能，刷新表格
function queryArticlesList(){
	articleManageTable.draw();
}

// 点击编辑按钮
function showArticleEditModal(articleId){
	startPageLoading();
	$.ajax({
		url:"manage/info/getArticleById.do",
		type:"post",
		data:{"articleId":articleId},
		dataType:"json",
		success:function(data) {
		   if(data.status=="success") {
               var articleData = data.usersData;
               
               
               $('#articleId').val(articleId);
               $('#articleTitle').val(articleData.articleTitle);
               $('#articleDesc').val(articleData.articleDesc);
              // $("#editor").html(articleData.articleValue);
               
               UE.getEditor('editor').setContent(articleData.articleValue);
              
                $('#article_edit').modal('show');
                stopPageLoading()
		   } else {
			   stopPageLoading();
			   showSuccessOrErrorModal("获取用户信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}

//保存文章信息
function setarticle(){
	if($("#articleTitle").val() == ""){
		$("#articleTitle").testRemind("文章标题不能为空!")
		return;
	}else if($("#articleDesc").val() == ""){
		$("#articleDesc").testRemind("文章描述不能为空!")
		return;
	}
	
	var data= $("#userForm").serialize();//id,desc,title,type
	data+="&editor="+ue.getContent+"&articleType="+$('#articleType').val();
	data+="&articleTypeText="+$('#articleType option:selected').text();
	$.ajax({
		url:"manage/info/saveArticle.do",
		type:"post",
		data:data,
		dataType:"json",
		success:function(data) {
		    if(data.status=="success") {
		    	showSuccessOrErrorModal(data.msg,"success"); 
		    	articleManageTable.draw();
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

//删除文章
function deleteArticle(articleId){

	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"manage/info/deleteArticle.do",
			type:"post",
			data:{"articleId":articleId},
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        articleManageTable.draw(); //刷新表格
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	});
	
}
//新增文章
function addArticle(){
	  $('#articleId').val("");
	  $('#articleTitle').val("");
	  $('#articleDesc').val("");
	  ue.setContent("");
	  $('#article_edit').modal('show');

}
$(document).ready(function(){
	initarticleManageTable();
	$("#imageFile").change(function(){
		var form = new FormData(document.getElementById("imageFileForm"));
		
		var filePath = $(this).val();  
		if("" != filePath){  
		    var fileType = getFileType(filePath);  
		    
		    //判断上传的附件是否为图片  
		    if("jpg"==fileType && "png"==fileType && "docx"==fileType && "doc"==fileType  && "xls"==fileType &&  "xlsx"==fileType && "jpeg"==fileType ){  
		    	$(this).val("");  
		        showSuccessOrErrorModal("请上传JPG,PNG,JPEG,DOC,DOCX,DOC,XLS,XLSX格式的图片","error");
		    }else{  
		        //获取附件大小（单位：KB）  
		        var fileSize = this.files[0].size / 1024;  
		        
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
		    				
		    			    if(data.status == "success") {
		    			    	
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