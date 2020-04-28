var regulatorTable = null;
var sideRegulatorTable = null;
var userMap = {};
var userId = 0;

function initTimeSelect(){
    $("#StartTime").datetimepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: true,//今日按钮
        format: "yyyy-mm-dd hh:ii"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
    });
    $("#EndTime").datetimepicker({
        language: "zh-CN",
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: true,//今日按钮
        format: "yyyy-mm-dd hh:ii"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
    });
}

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();  
}


function initSideRegulatorTable() {

	sideRegulatorTable = $('#mTable').DataTable({
		// url
		"sAjaxSource" : "state/queryWorkConditionList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "factory", "value": $("#cronFactory").val()}); 
			aoData.push({ "name": "device_id", "value": $("#device_id").val()}); 
			aoData.push({ "name": "StartTime", "value": $("#StartTime").val()}); 
			aoData.push({ "name": "EndTime", "value": $("#EndTime").val()});
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
					showSuccessOrErrorModal("获取波形数据失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	
			/*
			{	
			 "title" : "工况编号",  
			 "defaultContent" : "", 
			 "data" :"workcondition_num",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		 ,*/	 
		 {	
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factory",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		 ,	 {	
			 "title" : "装置ID编号",  
			 "defaultContent" : "", 
			 "data" :"device",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "装置标识",  
			 "defaultContent" : "", 
			 "data" :"deviceName",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "工况时间",  
			 "defaultContent" : "", 
			 "data" :"workcondition_time",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		/*
		 ,	 {	
			 "title" : "通信状态",  
			 "defaultContent" : "", 
			 "data" :"comm_state",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	*/
		,	 {	
			 "title" : "供电状态",  
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
			 "title" : "电池电压(v)",  
			 "defaultContent" : "", 
			 "data" :"battery_vol",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "设备温度(℃)",  
			 "defaultContent" : "", 
			 "data" :"battery_temperature",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "环境温度(℃)",  
			 "defaultContent" : "", 
			 "data" :"work_temperature",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "电流有效值(A)",  
			 "defaultContent" : "", 
			 "data" :"current_valid_value",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "GPS有效值",  
			 "defaultContent" : "", 
			 "data" :"gps_valid_value",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "太阳能板电压(V)",  
			 "defaultContent" : "", 
			 "data" :"solarPanel_voltage",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "脉动电压幅值(V)",  
			 "defaultContent" : "", 
			 "data" :"pulse_voltage_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		
		,	 {	
			 "title" : "脉动电流幅值(A)",  
			 "defaultContent" : "", 
			 "data" :"pulse_current_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "基波电流幅值(A)",  
			 "defaultContent" : "", 
			 "data" :"base_current_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "相角",  
			 "defaultContent" : "", 
			 "data" :"phase",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "GPRS信号强度",  
			 "defaultContent" : "", 
			 "data" :"GPRS_strength",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		 ]
	});
}




function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "state/queryWorkConditionList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "factory", "value": $("#cronFactory").val()}); 
			aoData.push({ "name": "device_id", "value": $("#device_id").val()}); 
			aoData.push({ "name": "StartTime", "value": $("#StartTime").val()}); 
			aoData.push({ "name": "EndTime", "value": $("#EndTime").val()});
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
					showSuccessOrErrorModal("获取波形数据失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	
			/*
			{	
			 "title" : "工况编号",  
			 "defaultContent" : "", 
			 "data" :"workcondition_num",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		 ,*/	 
		 {	
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factory",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		 ,	 {	
			 "title" : "装置ID编号",  
			 "defaultContent" : "", 
			 "data" :"device",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "装置标识",  
			 "defaultContent" : "", 
			 "data" :"deviceName",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "工况时间",  
			 "defaultContent" : "", 
			 "data" :"workcondition_time",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		/*
		 ,	 {	
			 "title" : "通信状态",  
			 "defaultContent" : "", 
			 "data" :"comm_state",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	*/
		,	 {	
			 "title" : "供电状态",  
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
			 "title" : "电池电压(v)",  
			 "defaultContent" : "", 
			 "data" :"battery_vol",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "设备温度(℃)",  
			 "defaultContent" : "", 
			 "data" :"device_tempature",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "环境温度(℃)",  
			 "defaultContent" : "", 
			 "data" :"env_tempature",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "电流有效值(A)",  
			 "defaultContent" : "", 
			 "data" :"current_valid_value",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "GPS有效值",  
			 "defaultContent" : "", 
			 "data" :"gps_valid",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "太阳能板电压(V)",  
			 "defaultContent" : "", 
			 "data" :"solarPanel_voltage",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "脉动电压幅值(V)",  
			 "defaultContent" : "", 
			 "data" :"pulse_voltage_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 		
		,	 {	
			 "title" : "脉动电流幅值(A)",  
			 "defaultContent" : "", 
			 "data" :"pulse_current_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "基波电流幅值(A)",  
			 "defaultContent" : "", 
			 "data" :"base_current_amplitude",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "相角",  
			 "defaultContent" : "", 
			 "data" :"ied_phase",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	
		,	 {	
			 "title" : "GPRS信号强度",  
			 "defaultContent" : "", 
			 "data" :"gprs_strength",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		 ]
	});
}

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
	initParent();
	initTimeSelect();
	initFactory();
	initRegulatorTable();
//	initSideRegulatorTable();
});


