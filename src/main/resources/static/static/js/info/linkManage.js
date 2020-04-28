//初始化表格
var schoolUserTable = null;
function initSchoolUserTable() {
	schoolUserTable = $('#schoolUserTable').DataTable({
		// url
		"sAjaxSource" : "manage/info/queryLinksList.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
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
	            targets: [0,1,2,3,4,5],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "链接名",  
			 "defaultContent" : "", 
			 "data" :"linkTitle",
			 "width": "16.66%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "链接url",  
			 "defaultContent" : "", 
			 "data" :"linkUrl",
			 "width": "16.66%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "权重",  
			 "defaultContent" : "", 
			 "data" :"linkSoft",
			 "width": "16.66%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "类别",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "16.66%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				 	if(row.linkType==1)
				 		content = '友情链接';
				 	else
				 		content = '合作伙伴';
			         return content;
		      } 
		 }, {	
			 "title" : "日期",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "16.66%",
			 "class" : "text-center" ,
			 "render": function(data, type, row, meta) {
				 var timestamp = row.updateTime;  
				 var newDate = new Date();  
				 newDate.setTime(timestamp);
				 
				 return newDate.toLocaleDateString();
		      }  
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "16.66%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
					content =  '<button class="btn btn-xs blue" onclick="showUserEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 修改 </button>' +
						'<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
			         return content;
		      } 
		 }]
	});
}
// 点击编辑按钮
function showUserEditModal(linkId){
	startPageLoading();
	$.ajax({
		url:"manage/info/getLinkById.do",
		type:"post",
		data:{"linkId":linkId},
		dataType:"json",
		success:function(data) {
		   if(data.status=="success") {
               var usersData = data.usersData;
               
               $("#linkId").val(linkId);
			   $("#linkTitle").val(usersData.linkTitle);
			   $("#linkUrl").val(usersData.linkUrl);
			   $("#linkDesc").val(usersData.linkDesc);
			   $("#linkSoft").val(usersData.linkSoft);
			   if(usersData.linkType == '1') {
       			   $("#userModal_add").find("input[id='friendshiplink']").attr("checked",'checked'); 
       		   } else {
       			   $("#userModal_add").find("input[id='partnerlink']").attr("checked",'checked'); 
       		   }
               $('#userModal_add').modal('show');
               if(usersData.linkType == '1') {
       			   $("#userModal_add").find("input[id='limkstatus_1_m']").attr("checked",'checked'); 
       		   } else {
       			   $("#userModal_add").find("input[id='linkstatus_q_m']").attr("checked",'checked'); 
       		   } 
                stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取链接信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};
//新增用户按钮
function addSchoolUser(){

	$("#recordId").val("");
	$("#userForm")[0].reset();
	
}
//删除链接
function deleteSchoolUser(linkId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"manage/info/deleteLink.do",
			type:"post",
			data:{"linkId":linkId},
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        schoolUserTable.draw(); //刷新表格
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
$(document).ready(function(){
	initSchoolUserTable();

	// 表单验证(点击submit触发该方法)
	$("#userForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#userForm").serialize();
		data += "&roleId=" +$("#roleSelect_m").val();
		log(data)
		$.ajax({
			url:"manage/info/saveLink.do",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#userModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	});
	
});
function log(formstr){
	var res = {};
	formstr.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
	
}


