var regulatorTable = null;
var userMap = {};
var userId = 0;
function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "state/querySelfCheckList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "factory", "value": $("#cronFactory").val()}); 
			aoData.push({ "name": "device_id", "value": $("#device_id").val()});
			aoData.push({ "name": "CommState", "value": $("#cronCommState").val()}); 
			aoData.push({ "name": "CommLine", "value": $("#cronLine").val()});
			aoData.push({ "name": "userID",  "value": userId});
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
					showSuccessOrErrorModal("获取装置自检数据失败","error");
				}
			});
		},
		// 列属性
		"columns" : [{	
			 "title" : "通信状态",  
			 "defaultContent" : "", 
			 "data" :"comm_state",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "在线";
		            }
					else if(data == 1){
						content = '<font color=red>离线</font>';
					}
					else {
						content = "未知";
					}
		            return content;
		      }   
		 } 
		 ,	 {	
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factory",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		 ,	 {	
			 "title" : "装置ID编号",  
			 "defaultContent" : "", 
			 "data" :"id",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "装置标识",  
			 "defaultContent" : "", 
			 "data" :"name",
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
				  content = '<button class="btn btn-xs red" onclick="readSelfCheck(\''+row.id+'\')"> 读取自检 </button>'+
                 '<button class="btn btn-xs green" onclick="downloadLog(\''+row.id+'\')"> 上召日志 </button>';
		         return content;
		      } 
		 }	
		,	 {	
			 "title" : "装置时间",  
			 "defaultContent" : "", 
			 "data" :"device_time",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "天线状态",  
			 "defaultContent" : "", 
			 "data" :"antenna_sta",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 1){
		            	content = "OK";
		            }
					else if(data == 2){
						content = "OPEN";
					}
					else if(data == 3){
						content = "SHORT";
					}
					else if(data == 4){
						content = "未收到天线报文";
					}
					else {
						content = "未知";
					}
		            return content;
		      }  
		 }   
		,	 {	
			 "title" : "装置对时有效性",  
			 "defaultContent" : "", 
			 "data" :"gps_valid",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "对时无效";
		            }
					else if(data == 1){
						content = "对时有效";
					}
					else if(data == 3){
						content = "未收到对时报文";
					}
					else {
						content = "未知";
					}
		            return content;
		      }   
		 }   
		,	 {	
			 "title" : "上次对时时间",  
			 "defaultContent" : "", 
			 "data" :"last_sync_time",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "自检经度",  
			 "defaultContent" : "", 
			 "data" :"longitude",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "自检纬度",  
			 "defaultContent" : "", 
			 "data" :"latitude",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "工作电压",  
			 "defaultContent" : "", 
			 "data" :"work_voltage",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "工作温度",  
			 "defaultContent" : "", 
			 "data" :"work_temperature",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "工频电流有效值",  
			 "defaultContent" : "", 
			 "data" :"pf_current_valid",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "电池供电状态",  
			 "defaultContent" : "", 
			 "data" :"battery_status",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "感应电源供电";
		            }
					else if(data == 1){
						content = "电池供电";
					}
					else if(data == 2){
						content = "太阳能供电";
					}
					else {
						content = "未知";
					}
		            return content;
		      }     
		 }   
		,	 {	
			 "title" : "电池包1电压",  
			 "defaultContent" : "", 
			 "data" :"battery_vol",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "电池包1电流",  
			 "defaultContent" : "", 
			 "data" :"battery_current",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "电池包1温度",  
			 "defaultContent" : "", 
			 "data" :"battery_temperature",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "电池包1容量(%)",  
			 "defaultContent" : "", 
			 "data" :"battery_soc",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "电池包1告警",  
			 "defaultContent" : "", 
			 "data" :"battery_bj",
			 "width": "10%",
			 "class" : "text-center"  
		 }
            ,	 {
                "title" : "电池包2电压",
                "defaultContent" : "",
                "data" :"battery2_vol",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
                "title" : "电池包2电流",
                "defaultContent" : "",
                "data" :"battery2_current",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
                "title" : "电池包2温度",
                "defaultContent" : "",
                "data" :"battery2_temperature",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
                "title" : "电池包2容量(%)",
                "defaultContent" : "",
                "data" :"battery2_soc",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
                "title" : "电池包2告警",
                "defaultContent" : "",
                "data" :"battery2_bj",
                "width": "10%",
                "class" : "text-center"
            }
            ,	 {
                "title" : "放电逻辑",
                "defaultContent" : "",
                "data" :"pwr_stat_val",
                "width": "10%",
                "class" : "text-center"
            }
		 /*
		,	 {	
			 "title" : "取能线圈电压",  
			 "defaultContent" : "", 
			 "data" :"ad_ctflyback_v",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		,	 {	
			 "title" : "板间直流输入电压",  
			 "defaultContent" : "", 
			 "data" :"ad_dcdcbus_v",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		 	*/
		 	,	 {
			 "title" : "电池1电压",  
			 "defaultContent" : "", 
			 "data" :"AD_BAT1_V",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		,	 {	
			 "title" : "电池1电流",  
			 "defaultContent" : "", 
			 "data" :"AD_BAT1_I",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		,	 {	
			 "title" : "电池2电压",  
			 "defaultContent" : "", 
			 "data" :"AD_BAT2_V",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		,	 {	
			 "title" : "电池2电流",  
			 "defaultContent" : "", 
			 "data" :"AD_BAT2_I",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		,	 {	
			 "title" : "USB电压",  
			 "defaultContent" : "", 
			 "data" :"AD_USB_V",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		 
		 ]
	});
}

function readSelfCheck(recordId){
	showConfirmModal("该操作会读取装置自检信息，是否确定继续！",function(){
			   $("#device_access_m1").val("");
			   $("#regulatorId").val(recordId);
			   $('#accessDeviceModal').modal('show');
	});
}

function downloadLog(recordId){
	showConfirmModal("该操作会上召装置自检日志，是否确定继续！",function(){
			   $("#device_access_m2").val("");
			   $("#regulatorId").val(recordId);
			   $('#accessDeviceForResetModal').modal('show');
	});
}

/*
function readSelfCheck(recordId){
	showConfirmModal("该操作会读取装置自检信息，是否确定继续！",function(){
	startPageLoading();
	var data = {"recordId":recordId, "userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"state/readSelfCheckById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   stopPageLoading()
			   if (data.result=="0")
			   {
				  regulatorTable.ajax.reload();
				  showSuccessOrErrorModal("读取装置自检信息成功","success");
			   }
			   else if (data.result=="1")
			   {
				  showSuccessOrErrorModal("读取装置自检信息失败","error");
			   }
			   else if (data.result=="2")
			   {
				  showSuccessOrErrorModal("读取装置自检信息超时","error");
			   }
			   else if (data.result=="3")
			   {
				  showSuccessOrErrorModal("读取装置自检信息重复","error");
			   }
			   else if (data.result=="-1")
			   {
				  showSuccessOrErrorModal("读取装置自检信息状态未知","error");
			   }

		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求读取装置自检信息出错了","error"); 
		}
	});
	});
}

function downloadLog(recordId){
	showConfirmModal("该操作会上召装置自检日志，是否确定继续！",function(){
		$(this).modal("hide");
	startPageLoading();
	var data = {"recordId":recordId, "userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"state/sendDownloadLogById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
 		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));   
		   if(data.status=="success") {
			   stopPageLoading()
			   if (data.result=="0")
			   {
				  regulatorTable.ajax.reload();
				  var PathName = data.PathName;
				  var FileName = data.FileName;
				  if (null!=FileName)
				  {
				      var urlName = "http://localhost:8082/state/downloadLogById?file_name="+FileName+"&pathname="+PathName;
				      console.log(urlName)
				      window.open(urlName);
				  }
				  else
				  {
					  showSuccessOrErrorModal("对应日志不存在","error");
				  }
			   }
			   else if (data.result=="1")
			   {
				  showSuccessOrErrorModal("上召日志失败","error");
			   }
			   else if (data.result=="2")
			   {
				  showSuccessOrErrorModal("上召日志超时","error");
			   }
			   else if (data.result=="3")
			   {
				  showSuccessOrErrorModal("上召日志重复","error");
			   }
			   else if (data.result=="-1")
			   {
				  showSuccessOrErrorModal("上召日志状态未知","error");
			   }

		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   } 
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal(data.msg,"error"); 
		}
	});
	});
}
*/

function initFactory(){
	$.ajax({
		url:"info/queryFactory",
		type:"post",
		data:{},
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+='<option value="">---所有厂家---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronFactory").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询厂家列表请求出错了","error"); 
		}
	});	
}

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();  
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

function initParent(){
	var userMap = $.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234"));
	var userId = userMap.id;
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	console.log(userId)
	$.ajax({
		url:"info/queryLineByUser",
		type:"post",
		data:dataObj,
		dataType:"text",
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+='<option value="">---所有线路---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronLine").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了12121","error"); 
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
	initParent();
	initFactory();
	initRegulatorTable();
	
	$("#accessDeviceModal").html5Validate(function() {
	   $("#accessDeviceModal").modal("hide");
	   startPageLoading();
	   var recordId = $("#regulatorId").val();
	   var devicePassword = $("#device_access_m1").val();
	   var data = {"recordId":recordId, "userId":userId, "devicePassword":devicePassword};
	   var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	   }
	   $.ajax({
		url:"state/readSelfCheckById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   stopPageLoading()
			   if (data.result=="0")
			   {
				  regulatorTable.ajax.reload();
				  showSuccessOrErrorModal("读取装置自检信息成功","success");
			   }
			   else if (data.result=="1")
			   {
				  showSuccessOrErrorModal("读取装置自检信息失败","error");
			   }
			   else if (data.result=="2")
			   {
				  showSuccessOrErrorModal("读取装置自检信息超时","error");
			   }
			   else if (data.result=="3")
			   {
				  showSuccessOrErrorModal("读取装置自检信息重复","error");
			   }
			   else if (data.result=="-1")
			   {
				  showSuccessOrErrorModal("读取装置自检信息状态未知","error");
			   }
			   else if (data.result=="-2")
			   {
				  showSuccessOrErrorModal("访问设备密码错误","error");
			   }
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error"); 
		}
	});
	});
	
	$("#accessDeviceForResetModal").html5Validate(function() {
	   $("#accessDeviceForResetModal").modal("hide");
	   startPageLoading();
	   var recordId = $("#regulatorId").val();
	   var devicePassword = $("#device_access_m2").val();
	   var data = {"recordId":recordId, "userId":userId, "devicePassword":devicePassword};
	   var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	   }
	   $.ajax({
		url:"state/sendDownloadLogById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   stopPageLoading()
			   if (data.result=="0")
			   {
				  regulatorTable.ajax.reload();
				  var PathName = data.PathName;
				  var FileName = data.FileName;
				  var serverip = data.serverip;
				  console.log(serverip)
				  if (null!=FileName)
				  {
				      var urlName = "http://"+serverip+":8080/faultdetect-01/state/downloadLogById?file_name="+FileName+"&pathname="+PathName;
				      console.log(urlName)
				      window.open(urlName);
				  }
				  else
				  {
					  showSuccessOrErrorModal("对应日志不存在","error");
				  }
			   }
			   else if (data.result=="1")
			   {
				  showSuccessOrErrorModal("上召日志失败","error");
			   }
			   else if (data.result=="2")
			   {
				  showSuccessOrErrorModal("上召日志超时","error");
			   }
			   else if (data.result=="3")
			   {
				  showSuccessOrErrorModal("上召日志重复","error");
			   }
			   else if (data.result=="-1")
			   {
				  showSuccessOrErrorModal("上召日志状态未知","error");
			   }
			   else if (data.result=="-2")
			   {
				  showSuccessOrErrorModal("访问设备密码错误","error");
			   }
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal(data.msg,"error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error"); 
		}
	});
	});
});


