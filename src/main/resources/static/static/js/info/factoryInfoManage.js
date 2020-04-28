var regulatorTable = null;
var userMap = {};
var userId = 0;
function initRegulatorTable() {
	regulatorTable = $('#RegulatorTable').DataTable({
		// url
		"sAjaxSource" : "info/queryFactoryList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "QueryType", "value": $("#regulator_name").val()}); 
			$.ajax({
				type: "POST",
				url: sSource,
				contentType: "application/json; charset=utf-8",
			    data: JSON.stringify(aoData),
				success: function(data) 
				{	
					if(data.status == "success")
					{
						fnCallback(data.infoData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					console.log("LogData fail")
					showSuccessOrErrorModal("获取终端厂商信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	 {	
			 "title" : "厂家编号",  
			 "defaultContent" : "", 
			 "data" :"num",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		 ,	 {	
			 "title" : "名称",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "联系人",  
			 "defaultContent" : "", 
			 "data" :"contact",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "联系电话",  
			 "defaultContent" : "", 
			 "data" :"call1",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "登录名称",  
			 "defaultContent" : "", 
			 "data" :"login_name",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		//,	 {	
		//	 "title" : "登录密码",  
		//	 "defaultContent" : "", 
		//	 "data" :"login_password",
		//	 "width": "10%",
		//	 "class" : "text-center"  
		// }   
		,	 {	
			 "title" : "微服务id",  
			 "defaultContent" : "", 
			 "data" :"uuid",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "采样率",  
			 "defaultContent" : "", 
			 "data" :"sample_Rate",
			 "width": "10%",
			 "class" : "text-center"  
		 }  		 
		,{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs blue" onclick="showUserEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }]
	});
}

//删除用户
function deleteSchoolUser(userId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"info/deleteFactory",
			type:"post",
			data:{"userId":userId},
			dataType:"text",
			success:function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        regulatorTable.draw(); //刷新表格
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
				console.error(e)
			    showSuccessOrErrorModal("请求出错了2","error"); 
			}
		});
	});
	
}

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();  
}

//新增监管单位按钮
function addRegulator(){
	$("#regulatorId").val("");
	$("#regulatorForm")[0].reset();
}

//点击编辑按钮
function showUserEditModal(regulatorId){
	startPageLoading();
	var data = {"regulatorId":regulatorId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/getFactoryById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
			   console.log("111")
               console.log(usersData)
			   console.log("222")
               $("#regulatorId").val(regulatorId);
			   $("#num_m").val(usersData.num);
			   $("#name_m").val(usersData.name);
			   $("#contact_m").val(usersData.contact);
			   $("#call_m").val(usersData.call1);
			   $("#login_name_m").val(usersData.login_name);
			   $("#login_password_m").val(usersData.login_password);
			   $("#uuid_m").val(usersData.uuid);
			   $("#sample_Rate_m").val(usersData.sample_Rate);
			   
               $('#regulatorModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取终端厂商信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error"); 
		}
	});
}

function showTime(){
	var newDateObj = new Date(); 
	var year = newDateObj.getFullYear();
	var month = newDateObj.getMonth()+1;
	if(month==13)
		{
		month =1;
		}
	var day = newDateObj.getDate();
	var week = newDateObj.getDay();
	var arr = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	var hour = newDateObj.getHours();
	var minute = newDateObj.getMinutes();
	var second = newDateObj.getSeconds();
	var showTime = year+"/"+month+"/"+day+" "+arr[week]+" "+hour+((minute<10)?":0":":")
	               +minute+((second<10)?":0":":")+second+((hour>12)?" 下午":" 上午");
	showTime = '<font color=red size=4>'+showTime+'</font>';
	
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	
	$.ajax({
		url:"info/queryMarqueeInfo",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var allDeviceNum = data.allDeviceNum;
				var onlineDeviceNum = data.onlineDeviceNum;
				var offlineDeviceNum = data.offlineDeviceNum;
				var noReadAlarmNum = data.noReadAlarmNum;
				var noReadFaultNum = data.noReadFaultNum;
	            var showDevice ="系统接入设备："+allDeviceNum+"台     系统正常设备："
				           +onlineDeviceNum+"台     系统异常设备："+offlineDeviceNum+"台 ";
	            showDevice = '<font color=blue size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showDevice+'</font>';
	            var showFault = "    未读故障信息："+noReadFaultNum+"条";
	            showFault = '<font color=green size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showFault+'</font>';
	            var showAlarm = "    未读告警信息："+noReadAlarmNum+"条";
	            showAlarm = '<font color=red size=4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+showAlarm+'</font>';
	            var str=/*showTime + */showDevice + showFault + showAlarm;
	            $("#marqueeTitle").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    //showSuccessOrErrorModal("滚动栏请求出错了","error"); 
		}
	});		
}

$(document).ready(function(){
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		userId = userMap.id;
	}else{
		//parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	initRegulatorTable();
	$("#regulatorForm").html5Validate(function() {
	   var data = $("#regulatorForm").serialize();
	   console.log(data);
	   		$.ajax({
			url:"info/saveFactory",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	regulatorTable.draw();
			    	$("#regulatorModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了3","error"); 
			}
		});
	});
});


