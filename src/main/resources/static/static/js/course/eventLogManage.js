//初始化表格
var logTable = null;
function initLogTable() {
	logTable = $('#logTable').DataTable({
		// url
		"sAjaxSource" : "manage/coursem/queryLogList.do", 
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
					console.log(data)
					if(data.status == "success")
					{
						fnCallback(data.stuData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取课程信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [ {	
			 "title" : "日志日期",  
			 "defaultContent" : "", 
			 "data" :"event_time",
			 "width": "50%",
			 "class" : "text-center",
			   "render": function(data, type, row, meta) {
				 var timestamp = row.event_time;  
				 return new Date(timestamp).toLocaleString().replace(/\//g, "-");
		      }  
		 },
		 {	
			 "title" : "日志名称",  
			 "defaultContent" : "", 
			 "data" :"event_name",
			 "width": "50%",
			 "class" : "text-center"  
		 } ]
	});
}	

$(document).ready(function(){
	initLogTable();
});

