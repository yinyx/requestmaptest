//初始化表格
var schoolUserTable = null;
function initSchoolUserTable() {
	schoolUserTable = $('#schoolUserTable').DataTable({
		// url
		"sAjaxSource" : "manage/info/queryContentsList.do", 
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
					//console.log(JSON.stringify(data))
					if(data.status == "success")
					{
						fnCallback(data.usersData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取留言  信息失败","error");
				}
			});
		},
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [0,1,2,3,4],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "留言者",  
			 "defaultContent" : "", 
			 "data" :"contentName",
			 "width": "20%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "联系方式",  
			 "defaultContent" : "", 
			 "data" :"contentPhone",
			 "width": "20%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "内容",  
			 "defaultContent" : "", 
			 "data" :"contentValue",
			 "width": "20%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "时间",  
			 "defaultContent" : "", 
			 "data" :"contentTime",
			 "width": "20%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var timestamp = row.contentTime;  
				 var newDate = new Date();  
				 newDate.setTime(timestamp);
				 return newDate.toLocaleDateString();
		      }  
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "20%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
					content = '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
			         return content;
		      } 
		 }]
	});
}	

//刷新表格
function querySchoolUser()
{	
	schoolUserTable.draw(); 
};

//删除留言
function deleteSchoolUser(contentId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"manage/info/deleteContent.do",
			type:"post",
			data:{"contentId":contentId},
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

	
});
function log(formstr){
	var res = {};
	formstr.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
	console.log(res)
}


