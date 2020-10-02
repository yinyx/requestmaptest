var regulatorTable = null;
var userMap = {};
var userId = 0;
function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "info/queryDeviceList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback)
		{
			aoData.push({ "name": "QueryType", "value": $("#cronID").val()});
			aoData.push({ "name": "QueryType1", "value": $("#cronFactory1").val()});
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
					showSuccessOrErrorModal("获取装置信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	 {
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factoryName",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "所属线路",  
			 "defaultContent" : "", 
			 "data" :"lineName",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "安装序号",  
			 "defaultContent" : "", 
			 "data" :"indexno",
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
		,	 {
			 "title" : "杆塔名称",  
			 "defaultContent" : "", 
			 "data" :"towerName",
			 "width": "10%",
			 "class" : "text-center",    
		 }
		,	 {
			 "title" : "相别",  
			 "defaultContent" : "", 
			 "data" :"ied_phase",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "A";
		            }
					else if(data == 1){
						content = "B";
					}
					else if(data == 2){
						content = "C";
					}
					else{
						content = "异常";
					}
		            return content;
		      }    
		 }
		,	 {
			 "title" : "协议类型",  
			 "defaultContent" : "", 
			 "data" :"protocol_version",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="2018版";
		            if(data == 1){
		            	content = "2019版";
		            }
		            return content;
		      }   
		 }
		,{
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs blue" onclick="showEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>'+
				 '<button class="btn btn-xs green" onclick="moreInfo(\''+row.id+'\')"> 更多信息 </button>';
		         return content;
		      } 
		 }]
	});
}

//点击更多信息按钮
function moreInfo(recordId){
	startPageLoading();
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/getMoreDeviceById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
			   $("#BigWaveRange_m").attr("disabled", true);
			   $("#BigWaveRange_m").val(usersData.maxCoefficient);
			   $("#SmallWaveRange_m").attr("disabled", true);
			   $("#SmallWaveRange_m").val(usersData.minCoefficient);
			   $("#zero_drift_comps_small_m").attr("disabled", true);
			   $("#zero_drift_comps_small_m").val(usersData.zero_drift_comps_small);
			   $("#FrequencyFactor_m").attr("disabled", true);
			   $("#FrequencyFactor_m").val(usersData.PowerFrequencyCoefficient);
			   $("#freSampleTime_m").attr("disabled", true);
			   $("#freSampleTime_m").val(usersData.freSampleTime);
			   $("#freSampleFre_m").attr("disabled", true);
			   $("#freSampleFre_m").val(usersData.freSampleFre);
			   $("#waveSampleTime_m").attr("disabled", true);
			   $("#waveSampleTime_m").val(usersData.waveSampleTime);
			   $("#waveSampleFre_m").attr("disabled", true);
			   $("#waveSampleFre_m").val(usersData.waveSampleFre);
			   $("#fpgaRatio_m").attr("disabled", true);
			   $("#fpgaRatio_m").val(usersData.fpgaRatio);
			   $("#device_time_m").attr("disabled", true);
			   $("#device_time_m").val(usersData.device_time);
			   $("#antenna_sta_m").attr("disabled", true);
			   //$("#antenna_sta_m").val(usersData.antenna_sta);
			   if (1==usersData.antenna_sta)
			   {
				   $("#antenna_sta_m").val("OK");
			   }
			   else if (2==usersData.antenna_sta)
			   {
				   $("#antenna_sta_m").val("OPEN");
			   }
			   else if (3==usersData.antenna_sta)
			   {
				   $("#antenna_sta_m").val("SHORT");
			   }
			   else
			   {
				   $("#antenna_sta_m").val("未收到天线报文");
			   }
			   $("#gps_valid_m").attr("disabled", true);
			   //$("#gps_valid_m").val(usersData.gps_valid);
			   if (0==usersData.gps_valid)
			   {
				   $("#gps_valid_m").val("对时无效");
			   }
			   else if (1==usersData.gps_valid)
			   {
				   $("#gps_valid_m").val("对时有效");
			   }
			   else
			   {
				   $("#gps_valid_m").val("未收到对时报文");
			   }
			   $("#last_sync_time_m").attr("disabled", true);
			   $("#last_sync_time_m").val(usersData.last_sync_time);
			   $("#work_voltage_m").attr("disabled", true);
			   $("#work_voltage_m").val(usersData.work_voltage);
			   $("#work_temperature_m").attr("disabled", true);
			   $("#work_temperature_m").val(usersData.work_temperature);
			   $("#pf_current_valid_m").attr("disabled", true);
			   $("#pf_current_valid_m").val(usersData.pf_current_valid);
			   $("#battery_status_m").attr("disabled", true);
			   //$("#battery_status_m").val(usersData.battery_status);
			   if (1==usersData.battery_status)
			   {
				   $("#battery_status_m").val("电池供电");
			   }
			   else if (2==usersData.battery_status)
			   {
				   $("#battery_status_m").val("太阳能供电");
			   }
			   else
			   {
				   $("#battery_status_m").val("感应电源供电");
			   }
			   $("#battery_vol_m").attr("disabled", true);
			   $("#battery_vol_m").val(usersData.battery_vol);
			   $("#battery_current_m").attr("disabled", true);
			   $("#battery_current_m").val(usersData.battery_current);
			   $("#battery_temperature_m").attr("disabled", true);
			   $("#battery_temperature_m").val(usersData.battery_temperature);
			   $("#battery_soc_m").attr("disabled", true);
			   $("#battery_soc_m").val(usersData.battery_soc);
			   $("#battery_bj_m").attr("disabled", true);
			   $("#battery_bj_m").val(usersData.battery_bj);
			   $("#ad_ctflyback_v_m").attr("disabled", true);
			   $("#ad_ctflyback_v_m").val(usersData.ad_ctflyback_v);
			   $("#ad_dcdcbus_v_m").attr("disabled", true);
			   $("#ad_dcdcbus_v_m").val(usersData.ad_dcdcbus_v);
			   $("#AD_BAT1_V_m").attr("disabled", true);
			   $("#AD_BAT1_V_m").val(usersData.AD_BAT1_V);
			   $("#AD_BAT1_I_m").attr("disabled", true);
			   $("#AD_BAT1_I_m").val(usersData.AD_BAT1_I);
			   $("#AD_BAT2_V_m").attr("disabled", true);
			   $("#AD_BAT2_V_m").val(usersData.AD_BAT2_V);
			   $("#AD_BAT2_I_m").attr("disabled", true);
			   $("#AD_BAT2_I_m").val(usersData.AD_BAT2_I);
			   $("#AD_USB_V_m").attr("disabled", true);
			   $("#AD_USB_V_m").val(usersData.AD_USB_V);
               $('#MoreInfoModal').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取更多装置信息失败","error");
		   }

		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("获取更多装置信息请求出错了","error");
		}
	});
}

//点击编辑按钮
function showEditModal(recordId){
	startPageLoading();
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/getDeviceById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
			   $("#device_m").attr("disabled", true);
			   $("#device_m").val(usersData.device);
			   $("#IP_m").val(usersData.remark);
               $("#recordId").val(recordId);
			   $("#name_m").val(usersData.name);
				var s2 = document.getElementById("cronFactory");
				var ops2 = s2.options;
				for(var i=0;i<ops2.length; i++){
				    var tempValue2 = ops2[i].value;
				    if(tempValue2 == usersData.manufacture) //这里是你要选的值
				    {
				       ops2[i].selected = true;
				       break;
				    }
				}
				var s = document.getElementById("cronLine");
				var ops = s.options;
				for(var i=0;i<ops.length; i++){
				    var tempValue = ops[i].value;
				    if(tempValue == usersData.line_name) //这里是你要选的值
				    {
				       ops[i].selected = true;
				       break;
				    }
				}
				initTower();
				var s1 = document.getElementById("cronTower");
				var ops1 = s1.options;
				for(var i=0;i<ops1.length; i++){
				    var tempValue1 = ops1[i].value;
				    if(tempValue1 == usersData.tower_id) //这里是你要选的值
				    {
				       ops1[i].selected = true;
				       break;
				    }
				}
				var s3 = document.getElementById("cronProtocalType");
				var ops3 = s3.options;
				for(var i=0;i<ops3.length; i++){
				    var tempValue3 = ops3[i].value;
				    if(tempValue3 == usersData.protocol_version) //这里是你要选的值
				    {
				       ops3[i].selected = true;
				       break;
				    }
				}
				var s4 = document.getElementById("cronPhase");
				var ops4 = s4.options;
				for(var i=0;i<ops4.length; i++){
				    var tempValue4 = ops4[i].value;
				    if(tempValue4 == usersData.ied_phase) //这里是你要选的值
				    {
				       ops4[i].selected = true;
				       break;
				    }
				}
               var s5 = document.getElementById("SystemVersion_m");
               var ops5 = s5.options;
               for(var i=0;i<ops5.length; i++){
                   var tempValue5 = ops5[i].value;
                   if(tempValue5 == usersData.system_version) //这里是你要选的值
                   {
                       ops5[i].selected = true;
                       break;
                   }
               }
               $("#InstallIndex_m").val(usersData.indexno);
			   $("#phase_m").val(usersData.ied_phase);
               $("#IedType_m").val(usersData.ied_type);
			   $("#version_m").val(usersData.version);
               $("#ManuDate_m").val(usersData.manu_date);
			   $("#InstallTime_m").val(usersData.install_time);
               $("#longitude_m").val(usersData.longitude);
			   $("#latitude_m").val(usersData.latitude);
               $("#altitude_m").val(usersData.altitude);
			   //$("#FrequencyFactor_m").val(usersData.PowerFrequencyCoefficient);
               //$("#BigWaveRange_m").val(usersData.maxCoefficient);
			   //$("#SmallWaveRange_m").val(usersData.minCoefficient);
			   //$("#zero_drift_comps_small_m").val(usersData.zero_drift_comps_small);
               $('#deviceModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取装置信息失败","error");
		   }

		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error");
		}
	});
}

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();
}

//新增监管单位按钮
function addRegulator(){
	$("#deviceForm")[0].reset();
	$("#recordId").val("");
	$("#device_m").attr("disabled", false);
	initParent1();
	initTower();
}

function initParent(){
    var data = {"userId":userId};
    var dataObj = {
        "paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
    }
	$.ajax({
		url:"info/queryLineByUser",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+='<option value="">---所有线路---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronID").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询线路列表请求出错了","error");
		}
	});
}

function initParent1(){
    var data = {"userId":userId};
    var dataObj = {
        "paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
    }
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
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronLine").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询线路列表请求出错了","error");
		}
	});
}

$("select#cronLine").change(function(){
	initTower();
});

function initTower(){
	var lineId = $("#cronLine").val();
	var data = {"lineId":lineId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/queryTowerByLine",
		//url:"info/getDeviceById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+= '<option value="0">无杆塔</option>';
				//str+='<option value="">---所有线路---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronTower").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询线路上的杆塔列表请求出错了","error");
		}
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

function initFactory1(){
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
		        $("#cronFactory1").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询厂家列表请求出错了","error");
		}
	});
}

//删除用户
function deleteSchoolUser(userId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"info/deleteDevice",
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

function exportDevice()
{
	var line = $("#cronID").val();
	var factory = $("#cronFactory1").val();
	var data = {
			"line":line,
			"factory" : factory
		};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"sys/createDeviceExcel",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	//showSuccessOrErrorModal(data.msg,"success");
				//var urlName = "http://"+serverip+":8080/faultdetect-01/state/downloadLogById?file_name="+FileName+"&pathname="+PathName;
				var urlName = "http://localhost:8082/sys/export";
				console.log(urlName)
				window.open(urlName);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求导出设备信息出错了","error");
		}
	});
}

function sync()
{
	$.ajax({
		url:"sys/syncDeviceProcess",
		type:"post",
		data:{},
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	showSuccessOrErrorModal(data.msg,"success");
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了","error");
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
	            var str=/*showTime + */showDevice;
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
	initParent1();
	//initTower();
	initFactory();
	initFactory1();
	initRegulatorTable();
	$("#deviceForm").html5Validate(function() {
			   console.log("1");
	   var data = $("#deviceForm").serialize();
	   console.log("2");
	   data+="&line="+$("#cronLine").val();
	   data+="&tower="+$("#cronTower").val();
	   data+="&factory="+$("#cronFactory").val();
	   data+="&ProtocalType="+$("#cronProtocalType").val();
	   data+="&system_version="+$("#SystemVersion_m").val();
	   data+="&phase="+$("#cronPhase").val();
	   console.log(data);
	   		$.ajax({
			url:"info/saveDevice",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success");
			    	regulatorTable.draw();
			    	$("#deviceModal_add").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");
			    }
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了3","error");
			}
		});
	}, {
		validate : function() {
			var self = $("#InstallIndex_m").val();
			if (isNaN(self)||(self.indexOf('.')!=-1)||(parseInt(self)<=0)) {
				$("#InstallIndex_m").testRemind("此处应填写正整数!");
				return false;
			}/*  else if (isNaN($("#BigWaveRange_m").val())||(parseFloat($("#BigWaveRange_m").val())<=0)) {
				$("#BigWaveRange_m").testRemind("此处应填写非负数!");
				return false;
			} else if (isNaN($("#SmallWaveRange_m").val())||(parseFloat($("#SmallWaveRange_m").val())<=0)) {
				$("#SmallWaveRange_m").testRemind("此处应填写非负数!");
				return false;
			}else if (isNaN($("#zero_drift_comps_small_m").val())||(parseFloat($("#zero_drift_comps_small_m").val())<=0)) {
				$("#zero_drift_comps_small_m").testRemind("此处应填写非负数!");
				return false;
			} */
			return true;
		}
	});

	$("#InstallIndex_m").on('change blur',function(e){
				var self = this;
				if(isNaN(self.value)||(self.value.indexOf('.')!=-1)||(parseInt(self.value)<=0))
	            {
					     $(self).testRemind("此处应填写正整数!");
		 		         $(self).focus();
				}
	});

/* 	$("#FrequencyFactor_m").on('change blur',function(e){
				var self = this;
				if(isNaN(self.value)||(parseFloat(self.value)<=0))
	            {
					     $(self).testRemind("此处应填写非负数!");
		 		         $(self).focus();
				}
	});

	$("#BigWaveRange_m").on('change blur',function(e){
				var self = this;
				if(isNaN(self.value)||(parseFloat(self.value)<=0))
	            {
					     $(self).testRemind("此处应填写非负数!");
		 		         $(self).focus();
				}
	});

	$("#SmallWaveRange_m").on('change blur',function(e){
				var self = this;
				if(isNaN(self.value)||(parseFloat(self.value)<=0))
	            {
					     $(self).testRemind("此处应填写非负数!");
		 		         $(self).focus();
				}
	});

	$("#zero_drift_comps_small_m").on('change blur',function(e){
				var self = this;
				if(isNaN(self.value)||(parseFloat(self.value)<=0))
	            {
					     $(self).testRemind("此处应填写非负数!");
		 		         $(self).focus();
				}
	}); */

	$("#editWavePwd").html5Validate(function() {
		var password_old = $("#password_wave").val();
		var password_new = $("#password_wave_new").val();
		var data = {
				"id":userMap.id,
				"password_old":password_old,
				"password" : password_new
			};
		var dataObj = {
				"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
		}
		$.ajax({
			url : "sys/saveDevicePassword",
			type : "post",
			data :dataObj,
			dataType : "text",
			success : function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				if (data.status == "success") {
					showSuccessOrErrorModal(data.msg, "success");
					$("#editWavePwd")[0].reset();
					$("#waveModal").modal("hide");
				} else {
					showSuccessOrErrorModal(data.msg, "error");
				}
			},
			error : function(e) {
				showSuccessOrErrorModal("保存密码请求出错了", "error");
			}
		})
	}, {
		validate : function() {
			if ($("#password_wave_new").val() != $("#password_wave_new_c").val()) {
				$("#password_wave_new_c").testRemind("您2次输入的密码不相同!");
				return false;
			}
			return true;
		}
	});

});


