var logTable = null;
function initLogTable() {
	console.log("LogData")
	logTable = $('#logTable').DataTable({
		// url
		"sAjaxSource" : "sys/queryLogDList", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			/*var aoObj = {};
			for( let i of aoData){
			    aoObj[i.name] = i.value;
			}
			var dataObj = {
					"paramObj":encrypt(JSON.stringify(aoObj),"abcd1234abcd1234")
			}*/
			aoData.push({ "name": "QueryType", "value": $("#cronID").val()}); 
			$.ajax({
				type: "POST",
				url: sSource,
				contentType: "application/json; charset=utf-8",
			    data: JSON.stringify(aoData),
				//dataSrc:JSON.stringify(aoData),
				success: function(data) 
				{	
					if(data.status == "success")
					{
						fnCallback(data.stuData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					console.log("LogData fail")
					showSuccessOrErrorModal("获取日志信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [ {	
			 "title" : "同步日期",  
			 "defaultContent" : "", 
			 "data" :"syn_time",
			 "width": "50%",
			 "class" : "text-center",
			  "render": function(data, type, row, meta) {
				 var timestamp = row.syn_time;  
				 return new Date(timestamp).toLocaleString().replace(/\//g, "-");
		      }  
		 },
		 {	
			 "title" : "同步类型",  
			 "defaultContent" : "", 
			 "data" :"type",
			 "width": "50%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var c = "";  
				 if(data == "0"){
					 c = "定时器"
				 }else if(data == "1"){
					 c = "手动"
				 }else{
					 c = "异常"
				 }

				 return c;
		      }  
		 } ]
	});
}

function queryLog() {//条件查询同步日志
	logTable.ajax.reload();  
}

$(document).ready(function(){
	
	initLogTable()
	//initbasicInfos();
	$("#synchronousData").click(function(e){
		$.ajax({
			url:"sys/synchronousData.do",
			type:'post',
			data:{},
			dataType:'text',
			success:function(data){
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				if(data.status=="success"){
					console.log(data)
					showSuccessOrErrorModal("手动同步数据成功","success");
					logTable.draw(); 
				}else{
					 showSuccessOrErrorModal(data.msg,"error");
					 logTable.draw();
				}
			},
			error:function(e){
				showSuccessOrErrorModal("请求出错了","error"); 
			}
		})
	});
});


