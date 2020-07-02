var regulatorTable = null;
var userMap = {};
var userId = 0;
function initRegulatorTable() {
	regulatorTable = $('#RegulatorTable').DataTable({
		// url
		"sAjaxSource" : "info/queryLineList", 
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
					showSuccessOrErrorModal("获取线路信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	 {
			 "title" : "名称",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "监管单位",  
			 "defaultContent" : "", 
			 "data" :"regulatorName",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "线路长度",  
			 "defaultContent" : "", 
			 "data" :"length",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "电压等级",  
			 "defaultContent" : "", 
			 "data" :"voltage_level",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "电流类型",  
			 "defaultContent" : "", 
			 "data" :"ac_dc",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="交流";
		            if(data == 0){
		            	content = "直流";
		            }
		            return content;
		      }     
		 }
/* 		,	 {
			 "title" : "左变电站",  
			 "defaultContent" : "", 
			 "data" :"leftStationName",
			 "width": "10%",
			 "class" : "text-center"  
		 }
		,	 {
			 "title" : "右变电站",  
			 "defaultContent" : "", 
			 "data" :"rightStationName",
			 "width": "10%",
			 "class" : "text-center"  
		 } 	 */
		,{
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
				  content = '<button class="btn btn-xs blue" onclick="showEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 编辑 </button>' +
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }]
	});
}

//点击编辑按钮
function showEditModal(recordId){
	startPageLoading();
	console.log("recordId")
	console.log(recordId)
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/getLineById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
               $("#recordId").val(recordId);
			   $("#name_m").val(usersData.name);
				var s = document.getElementById("cronID");
				var ops = s.options;
				for(var i=0;i<ops.length; i++){
				    var tempValue = ops[i].value;
				    if(tempValue == usersData.regulator) //这里是你要选的值
				    {
				       ops[i].selected = true;
				       break;
				    }
				}
				var s1 = document.getElementById("cronLeftStation");
				var ops1 = s1.options;
				for(var i=0;i<ops1.length; i++){
				    var tempValue1 = ops1[i].value;
				    if(tempValue1 == usersData.left_station) //这里是你要选的值
				    {
				       ops1[i].selected = true;
				       break;
				    }
				}
				var s2 = document.getElementById("cronRightStation");
				var ops2 = s2.options;
				for(var i=0;i<ops2.length; i++){
				    var tempValue2 = ops2[i].value;
				    if(tempValue2 == usersData.right_station) //这里是你要选的值
				    {
				       ops2[i].selected = true;
				       break;
				    }
				}
               $("#length_m").val(usersData.length);
			   $("#voltage_level_m").val(usersData.voltage_level);
			   if(usersData.ac_dc == '0') {
       			   $("#r_zhi").prop("checked",true);
       		   } else {
       			   $("#r_jiao").prop("checked",true);
       		   }
               $('#regulatorModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取监管单位信息失败","error");
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
	$("#recordId").val("");
	$("#regulatorForm")[0].reset();
}

function initParent(){
	$.ajax({
		url:"info/queryRegulator",
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
		        $("#cronID").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询监管单位请求出错了","error");
		}
	});
}

function initStation(){
	$.ajax({
		url:"info/queryStation",
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
		        $("#cronLeftStation").html(str);
				$("#cronRightStation").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询变电站请求出错了","error");
		}
	});
}

//删除用户
function deleteSchoolUser(userId){

	if (userMap.role !=2)
	{
        showSuccessOrErrorModal("当前用户没有删除线路的权限，请联系超级管理员！","warning");
	    return;
	}
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"info/deleteLine",
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

function sync()
{
	$.ajax({
		url:"sys/syncProcess",
		type:"post",
		data:{},
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	showSuccessOrErrorModal(data.msg,"success");
		    } else {
		        showSuccessOrErrorModal("同步线路数据成功","success");
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
	console.log(userMap)
	if(userMap){//成功登录
		userId = userMap.id;
	}else{
		//parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	initParent();
	initStation();
	initRegulatorTable();
	$("#regulatorForm").html5Validate(function() {
	   var data = $("#regulatorForm").serialize();
	   data+="&regulator="+$("#cronID").val();
	   data+="&LeftStation="+$("#cronLeftStation").val();
	   data+="&RightStation="+$("#cronRightStation").val();
	   console.log(data);
	   		$.ajax({
			url:"info/saveLine",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success");
			    	initParent();
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
	}, {
		validate : function() {
			var self = $("#length_m").val();
			if (isNaN(self)||(parseInt(self)<=0)) {
				$("#length_m").testRemind("线路长度应填写正数!");
				return false;
			}
			return true;
		}
	});

	$("#length_m").on('change blur',function(e){
				var length = this.value;
				var self = this;
				if(isNaN(length)||(parseInt(length)<=0))
	            {
					     $(self).testRemind("线路长度应填写正数!");
		 		         $(self).focus();
				}
	});
});


