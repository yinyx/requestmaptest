var regulatorTable = null;
var userMap = {};
var userId = 0;
function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "fault/queryWaveList", 
		"sScrollY":"370px",
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "factory", "value": $("#cronFactory").val()}); 
			aoData.push({ "name": "line",    "value": $("#cronLine").val()});  
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
		"columns" : [	 /* {	
			 "title" : "波形编号",  
			 "defaultContent" : "", 
			 "data" :"wave_num",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		, */	 {	
			 "title" : "装置标识",  
			 "defaultContent" : "", 
			 "data" :"device",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "所属线路",  
			 "defaultContent" : "", 
			 "data" :"line",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factory",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "杆塔",  
			 "defaultContent" : "", 
			 "data" :"tower",
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
		            if(data == 0){
		            	content = "A";
		            }
					else if(data == 1){
						content = "B";
					}
					else if(data == 2){
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
						content = "未知";
					}
		            return content;
		      }  
		 }    
		,	 {	
			 "title" : "波形时间",  
			 "defaultContent" : "", 
			 "data" :"start_time",
			 "width": "10%",
			 "class" : "text-center",    
		 } 
		,	
		 {	
			 "title" : "创建时间",  
			 "defaultContent" : "", 
			 "data" :"create_time",
			 "width": "10%",
			 "class" : "text-center",    
		 } ,
		 	{	
			 "title" : "采样率",  
			 "defaultContent" : "", 
			 "data" :"sample_rate",
			 "width": "10%",
			 "class" : "text-center",    
		 } 	
		,	 {	
			 "title" : "波形长度",  
			 "defaultContent" : "", 
			 "data" :"wave_length",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "波形类型",  
			 "defaultContent" : "", 
			 "data" :"wave_type",
			 "width": "10%",
			 "class" : "text-center",   
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "行波";
		            }
					else if (data == 1)
						{
							content = "工频";
						}
		            return content;
		      } 
		 } 	
		,	 {	
			 "title" : "量程",  
			 "defaultContent" : "", 
			 "data" :"wave_range",
			 "width": "10%",
			 "class" : "text-center",   
			 "render": function(data, type, row, meta) {
		            var content ="";
		            if(data == 0){
		            	content = "高量程";
		            }
					else if (data == 1)
						{
							content = "低量程";
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
				  content = '<button class="btn btn-xs blue" onclick="showEditModal(\''+row.id+'\') " data-toggle="modal" data-target="userModal_add"> 波形分析 </button>';
		         return content;
		      } 
		 }]
	});
}

// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));

//点击编辑按钮
function showEditModal(recordId){
    //var path = "25_1_2019-14_48_47.911926";
	startPageLoading();
	var data = {"recordId":recordId,"si":0,"ei":3000};
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
				//console.log("start")
				//console.log(dataList[0][1])
				//console.log("end")
				var maxIndex = findMaxIndex(dataList[0]);
				//console.log(maxIndex)
				var dataMap = data.data;
				var df2 = (dataMap.df2)?(dataMap.df2):("");
				option = {
		                title: {
		                      //text: "15/09/2019,00:00:04.407324654",
		                	  text: dataMap.time+"    最大值（index:"+maxIndex+"  value:"+dataList[0][maxIndex]+"）",
		                    x: 'center'
		                },
		                tooltip: {
		                    trigger: 'axis',
		                    axisPointer: {
		                        animation: false
		                    }
		                },
		                //legend: {
		                   // data:['通道1'],
		                   // x: 'left'
		              ///  },
		                axisPointer: {
		                    link: {xAxisIndex: 'all'}
		                },
		                dataZoom: {
		                        show: true,
		                        realtime: true,
		                        start: 0,
		                        end: 100,
		                       // xAxisIndex: [0, 1]
		                    },
		                xAxis : 
		                    {
		                        type : 'category',
		                        boundaryGap : false,
		                        axisLine: {onZero: true},
		                        data: dataList[2],
		                        //show:false
		                    },
		                yAxis : 
		                    {
		                        name : '通道1',
		                        type : 'value'
		                    },
		                series : [
		                    {
		                        name:'通道1',
		                        type:'line',
		                        symbolSize: 8,
		                        hoverAnimation: false,
		                        data:dataList[0]
		                        //data:[ 0,0.0174524,0.5,0.8660,1,0.8660,0.5,0],
		                    }
		                ]
		            };
					myChart.setOption(option);
					$('#userModal_add').modal('show');
					stopPageLoading()
			} else {
				stopPageLoading()
				showSuccessOrErrorModal(data.msg, "error");
			}
			
		},
		error:function(e) {
			stopPageLoading()
		    showSuccessOrErrorModal("请求显示波形图表出错了","error"); 
		}
		});
}

function queryWave() {//条件查询同步日志
	regulatorTable.ajax.reload();  
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

function queryWave1(){
	$("#main").attr("style","display:none;");
}

function closeModal(){
	$("#main").attr("style","display:none;");
	$("#wave_pwd").val("");
}

function findMaxIndex(waveArray){
	var maxIndex = 0;
	var maxValue = waveArray[maxIndex];
	for (var int = 1; int < waveArray.length; int++) {
         if (waveArray[int]>maxValue)
		 {
			 maxIndex = int;
			 maxValue = waveArray[int];
		 }			 
	}
	return maxIndex;
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
	initFactory();
	initRegulatorTable();
	$("#main").attr("style","display:none;");
	$("#regulatorForm").html5Validate(function() {
	   var data = $("#regulatorForm").serialize();
	   data+="&userId="+userId;
	   console.log(data);
	   		$.ajax({
			url:"fault/queryWavePwd",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
					$("#main").attr("style","display:;");
			    	//showSuccessOrErrorModal(data.msg,"success"); 
			    	//regulatorTable.draw();
			    	//$("#regulatorModal_add").modal("hide");
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


