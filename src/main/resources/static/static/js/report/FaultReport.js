var regulatorTable11 = null;
var userMap = {};
var userId = 0;

var selector;
//返回被选择radio的value属性值
function whichRadioValueChecked(selector){
    var rtn = "";
    selector.each(function(){
    	if($(this).prop("checked")){
			
    		rtn = $(this).attr("value");
            return rtn;
    	}
    });
    return rtn;
}

//selector = $('input[type="radio"]');
var selectValue; 

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

function initRegulatorTable() {

	regulatorTable11 = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "fault/queryFaultList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "Regulator",    "value": $("#cronRegulator").val()}); 
			aoData.push({ "name": "VoltageLevel", "value": $("#cronVoltageLevel").val()}); 
			aoData.push({ "name": "line",    "value": $("#cronLine").val()}); 
			aoData.push({ "name": "Kind",    "value": $("#cronKind").val()}); 
			aoData.push({ "name": "StartTime", "value": $("#StartTime").val()}); 
			aoData.push({ "name": "EndTime", "value": $("#EndTime").val()});
			aoData.push({ "name": "deal",    "value": $("#cronDeal").val()}); 
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
		"columns" : [	 {	
			 "title" : "故障编号",  
			 "defaultContent" : "", 
			 "data" :"fault_num",
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
			 "title" : "故障时间",  
			 "defaultContent" : "", 
			 "data" :"occurr_time",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "持续时间（S）",  
			 "defaultContent" : "", 
			 "data" :"duration",
			 "width": "10%",
			 "class" : "text-center",    
		 } 	
		,	 {	
			 "title" : "相别",  
			 "defaultContent" : "", 
			 "data" :"phase",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 1){
		            	content = "A";
		            }
					else if(data == 2){
						content = "B";
					}
					else if(data == 3){
						content = "C";
					}
					else if(data == 4){
						content = "AB";
					}
					else if(data == 5){
						content = "BC";
					}
					else if(data == 6){
						content = "AC";
					}
					else{
						content = "异常";
					}
		            return content;
		      }  
		 }  
		,	 {	
			 "title" : "是否雷击",  
			 "defaultContent" : "", 
			 "data" :"isLightning",
			 "width": "10%",
			 "class" : "text-center",  
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "非雷击";
		            }
					else{
						content = "雷击";
					}
		            return content;
		      }   
		 } 
		,	 {	
			 "title" : "雷击性质",  
			 "defaultContent" : "", 
			 "data" :"lightning_strike",
			 "width": "10%",
			 "class" : "text-center", 
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "非雷击";
		            }
					else if(data == 1){
						content = "直击";
					}
					else if(data == 2){
						content = "绕击";
					}
					else if(data == 3){
						content = "反击";
					}
		            return content;
		      }     
		 } 	
/* 		,	 {	
			 "title" : "是否跳闸",  
			 "defaultContent" : "", 
			 "data" :"isTrip",
			 "width": "10%",
			 "class" : "text-center"  
		 }  */ 
		,	 {	
			 "title" : "故障描述",  
			 "defaultContent" : "", 
			 "data" :"desc",
			 "width": "10%",
			 "class" : "text-center",    
		 } 	
		,	 {	
			 "title" : "是否处理",  
			 "defaultContent" : "", 
			 "data" :"isRead",
			 "width": "10%",
			 "class" : "text-center",    
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "未处理";
		            }
					else if(data == 1){
						content = "已处理";
					}
					else{
						content = "异常";
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
				  content = '<button class="btn btn-xs blue" onclick="showEditModal1(\''+row.id+'\') " data-toggle="modal" data-target="#"> 定位 </button>' +
                 '<button class="btn btn-xs red" onclick="showEditModal(\''+row.id+'\')"> 报告 </button>'+
				 '<button class="btn btn-xs green" onclick="deal(\''+row.id+'\')"> 处理 </button>';
		         return content;
		      } 
		 }]
	});
}

//点击处理按钮
function deal(recordId){
	startPageLoading();
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"fault/setDealFaultById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
			   showSuccessOrErrorModal(data.msg,"success"); 
			   regulatorTable11.draw();
               stopPageLoading()
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
}

//点击定位按钮
function showEditModal1(recordId){
	startPageLoading();
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"fault/getFaultById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
               $("#recordId").val(recordId);
			   $("#left_tower_m").val(usersData.leftTowerName);
			   $("#left_distance_m").val(usersData.left_distance);
               $("#right_tower_m").val(usersData.rightTowerName);	
               $("#right_distance_m").val(usersData.right_distance);				   
               $('#regulatorModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取故障信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error"); 
		}
	});
}

// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));

//点击报告按钮
function showEditModal(recordId){
    //var path = "25_1_2019-14_48_47.911926";
	var data = {"recordId":recordId,"si":0,"ei":12000};
	var dataObj = {
		"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"sys/queryWaveData",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			console.log(data)
			if (data.status == "success") {
				var dataList = data.dataList;
				var dataMap = data.data;
				var df2 = (dataMap.df2)?(dataMap.df2):("");
				option = {
		                title: {
		                    text: dataMap.time,
		                    x: 'center'
		                },
		                tooltip: {
		                    trigger: 'axis',
		                    axisPointer: {
		                        animation: false
		                    }
		                },
		                legend: {
		                    data:['通道1','通道2'],
		                    x: 'left'
		                },
		                axisPointer: {
		                    link: {xAxisIndex: 'all'}
		                },
		                dataZoom: [
		                    {
		                        show: true,
		                        realtime: true,
		                        start: 30,
		                        end: 70,
		                        xAxisIndex: [0, 1]
		                    },
		                    {
		                        type: 'inside',
		                        realtime: true,
		                        start: 30,
		                        end: 70,
		                        xAxisIndex: [0, 1]
		                    }
		                ],
		                grid: [{
		                    left: 50,
		                    right: 50,
		                    height: '35%'
		                }, {
		                    left: 50,
		                    right: 50,
		                    top: '55%',
		                    height: '35%'
		                }],
		                xAxis : [
		                    {
		                        type : 'category',
		                        boundaryGap : false,
		                        axisLine: {onZero: true},
		                        data: dataList[2],
		                        show:false
		                    },
		                    {
		                        gridIndex: 1,
		                        type : 'category',
		                        boundaryGap : false,
		                        axisLine: {onZero: true},
		                        data: dataList[2],
		                        position: 'top'
		                    }
		                ],
		                yAxis : [
		                    {
		                        name : '通道1',
		                        type : 'value'
		                    },
		                    {
		                        gridIndex: 1,
		                        name : '通道2 ' + df2,
		                        type : 'value'
		                    }
		                ],
		                series : [
		                    {
		                        name:'通道1',
		                        type:'line',
		                        symbolSize: 8,
		                        hoverAnimation: false,
		                        data:dataList[0]
		                    },
		                    {
		                        name:'通道2',
		                        type:'line',
		                        xAxisIndex: 1,
		                        yAxisIndex: 1,
		                        symbolSize: 8,
		                        hoverAnimation: false,
		                        data:dataList[1]
		                    }
		                ]
		            };
					myChart.setOption(option);
					$('#userModal_add').modal('show');
			} else {
				showSuccessOrErrorModal(data.msg, "error");
			}
			
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了12","error"); 
		}
		});
}

function queryLog() {//条件查询同步日志
	selector = $('input[type="radio"]');
    //selectValue = whichRadioValueChecked(selector); 
	//alert(selectValue)
	regulatorTable11.ajax.reload();  
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

function initRegulator(){
	$.ajax({
		url:"info/queryRegulator",
		type:"post",
		data:{},
		dataType:"text",
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				str+='<option value="">---所有单位---</option>';
		        for (var int = 0; int < regulatorList.length; int++) {
					str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				}
		        $("#cronRegulator").html(str);
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询监管单位列表请求出错了","error"); 
		}
	});	
}

/* function test(){
	selectValue = whichRadioValueChecked(selector); 
	regulatorTable11.ajax.reload();  
}

function initTimer(){
		setInterval(test, 30000);
} */

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



$("select#cronRegulator").change(function(){
    //console.log($(this).val());
	var userMap = $.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234"));
	var userId = userMap.id;
	var regulatorId = $(this).val();
	var voltageId = $("#cronVoltageLevel").val();
	var data = {"userId":userId,"regulatorId":regulatorId,"voltageId":voltageId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	//console.log(userId)
	$.ajax({
		url:"info/queryLineByMultiCondition",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				if (0==regulatorList.length)
				{
					str+='<option value="0">---无线路---</option>';
				}
			    else
				{
					str+='<option value="">---所有线路---</option>';
		            for (var int = 0; int < regulatorList.length; int++) 
					{
					     str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				    }
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
});

$("select#cronVoltageLevel").change(function(){
	var userMap = $.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234"));
	var userId = userMap.id;
	var regulatorId = $("#cronRegulator").val();
	var voltageId = $(this).val();
	var data = {"userId":userId,"regulatorId":regulatorId,"voltageId":voltageId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"info/queryLineByMultiCondition",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		    	var regulatorList = data.dataList;
		    	var str = "";
				if (0==regulatorList.length)
				{
					str+='<option value="0">---无线路---</option>';
				}
			    else
				{
					str+='<option value="">---所有线路---</option>';
		            for (var int = 0; int < regulatorList.length; int++) 
					{
					     str+= '<option value="'+regulatorList[int].id+'">'+regulatorList[int].name+'</option>';
				    }
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
});

$(document).ready(function(){
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		userId = userMap.id;
	}else{
		//parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	selector = $('input[type="radio"]');
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	initTimeSelect();
	initParent();
	initRegulator();
	initFactory()
	initRegulatorTable();
	//initTimer();

	$("#regulatorForm").html5Validate(function() {
	   var data = $("#regulatorForm").serialize();
	   data+="&line="+$("#cronID1").val();
	   console.log(data);
	   		$.ajax({
			url:"info/saveTower",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	regulatorTable11.draw();
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


