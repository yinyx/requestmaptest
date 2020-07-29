var map;
var userMap = {};
var userId = 0;
var regulatorList = {};
var lineList={};
var Overlaymap = {};
var leftTowerMap = {};
var rightTowerMap = {};
var FaultShowFlagMap = {};

var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
    scale: 0.6,//图标缩放大小
    strokeColor:'#fff',//设置矢量图标的线填充颜色
    strokeWeight: '2',//设置线宽
});
var icons = new BMap.IconSequence(sy, '10', '30');

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();
}

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
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "fault/queryFaultListInGis", 
		"bLengthChange":false,//取消显示每页条数
		"info":false,
		"sScrollY":"450px",
		"scrollCollapse":true,
		"pageLength" : 50,
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback)
		{
			aoData.push({ "name": "line",    "value": $("#cronLine").val()});
			aoData.push({ "name": "deal",    "value": ""});
			aoData.push({ "name": "StartTime", "value": $("#StartTime").val()});
			aoData.push({ "name": "EndTime", "value": $("#EndTime").val()});
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
					showSuccessOrErrorModal("获取故障信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	 {
			 "title" : "故障时间",  
			 "defaultContent" : "", 
			 "data" :"occurr_time",
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
			 "title" : "故障描述",  
			 "defaultContent" : "", 
			 "data" :"type",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="数据异常";
		            if(data == -1){
		            	content = "判定失败";
		            }
					else if((data == 2)||(data == 3)){
		            	content = "雷击";
		            }
					else if(data == 4){
		            	content = "非雷击";
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
				  content = '<button class="btn btn-xs blue" onclick="editJob(\''+row.id+'\')"> 定位 </button>' +
                 '<button class="btn btn-xs red" onclick="hideJob(\''+row.id+'\')"> 隐藏 </button>';
		         return content;
		      } 
		 }]
	});
}

function initParent(){
	//var userMap = $.parseJSON(decrypt($.cookie('userMap'),"abcd1234abcd1234"));
	//var userId = userMap.id;
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
		    	var regulatorList1 = data.dataList;
		    	var str = "";
				str+='<option value="">---所有线路---</option>';
		        for (var int = 0; int < regulatorList1.length; int++) {
					str+= '<option value="'+regulatorList1[int].id+'">'+regulatorList1[int].name+'</option>';
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
	initTimeSelect();
	initParent();
	initRegulatorTable();
	initFaultList();
    map = new BMap.Map("container", { minZoom: 4, maxZoom: 15 });	//创建Map实例,设置地图允许的最小/大级别
	queryMap();
});

function initFaultList()
{
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"gis/queryFaultListForGis",
		type:"post",
		data:dataObj,
		dataType:"text",
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		        regulatorList = data.dataList;
				console.log(regulatorList)
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");
		    }
		},
		error:function(e) {
		    showSuccessOrErrorModal("查询GIS页面故障列表请求出错了","error");
		}
	});
}

function editJob(id)
{
	if (FaultShowFlagMap[id])
	{
		return;
	}
	for (var int = 0; int < regulatorList.length; int++) {
		if (id == regulatorList[int].id)
		{
		var point1 = new BMap.Point(regulatorList[int].leftLongitude, regulatorList[int].leftLatitude);
		var point2 = new BMap.Point(regulatorList[int].rightLongitude, regulatorList[int].rightLatitude);
		console.log(regulatorList[int].leftLongitude)
		console.log(regulatorList[int].leftLatitude)
		console.log(regulatorList[int].rightLongitude)
		console.log(regulatorList[int].rightLatitude)
		if ((null==regulatorList[int].leftLongitude)
		     ||(0==regulatorList[int].leftLongitude)
		     ||(null==regulatorList[int].leftLatitude)
			 ||(0==regulatorList[int].leftLatitude)
			 ||(null==regulatorList[int].rightLongitude)
			 ||(0==regulatorList[int].rightLongitude)
			 ||(null==regulatorList[int].rightLatitude)
			 ||(0==regulatorList[int].rightLatitude))
		{
			showSuccessOrErrorModal("请补充相关经纬度信息", "info");
			return;
		}
		//addMarker(point1,point2, regulatorList[int].desc);
		var myIcon = new BMap.Icon("images/电线杆(6).png", new BMap.Size(32,32));
        var leftMarker = new BMap.Marker(point1,{icon:myIcon});
		var rightMarker = new BMap.Marker(point2,{icon:myIcon});
        map.addOverlay(leftMarker);
		map.addOverlay(rightMarker);
        var polyline = new BMap.Polyline([point1,point2], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(polyline);   //地图上渲染
		//var PointList =	polyline.getPath();
		//			console.log(PointList)
        var messageinfo = "故障时间：" + regulatorList[int].occurr_time+"\n";
            messageinfo += "故障描述："+ regulatorList[int].LineName +"-"+ regulatorList[int].FaultDesc+"\n";
            messageinfo += "距"+regulatorList[int].leftTowerName+"距离：" + regulatorList[int].left_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].rightTowerName+"距离：" + regulatorList[int].right_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].LeftStationName+"距离：" + regulatorList[int].left_station_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].RightStationName+"距离：" + regulatorList[int].right_station_distance+"  KM";
		polyline.addEventListener("click", function () {
			showSuccessOrErrorModal(messageinfo, "info");
        });
		map.panTo(point1);
		Overlaymap[id]=polyline;
		leftTowerMap[id]=leftMarker;
		rightTowerMap[id]=rightMarker;
		FaultShowFlagMap[id]=1;
		return;
		}
	}
}

function hideJob(id)
{
	//hideAll();
	FaultShowFlagMap[id]=0;
	map.removeOverlay(Overlaymap[id]);
	map.removeOverlay(leftTowerMap[id]);
	map.removeOverlay(rightTowerMap[id]);
	/*
	for (var int = 0; int < regulatorList.length; int++) {
		if (id == regulatorList[int].id)
		{
		var point1 = new BMap.Point(regulatorList[int].leftLongitude, regulatorList[int].leftLatitude);
		var point2 = new BMap.Point(regulatorList[int].rightLongitude, regulatorList[int].rightLatitude);
		//addMarker(point1,point2, regulatorList[int].desc);
        var polyline = new BMap.Polyline([point1,point2], {strokeColor:"yellow", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(polyline);   //地图上渲染
        var messageinfo = "故障描述：" + regulatorList[int].desc+"\n";
	        messageinfo += "左杆塔坐标：" + point1.lng + ",   " + point1.lat+"\n";
            messageinfo += "右杆塔坐标：" + point2.lng + ",   " + point2.lat+"\n";
			messageinfo += "距左杆塔距离：" + regulatorList[int].left_distance+"\n";
			messageinfo += "距右杆塔距离：" + regulatorList[int].right_distance;
		polyline.addEventListener("click", function () {
			showSuccessOrErrorModal(messageinfo, "info");
        });
		}
	}
	*/


/* 	var leftLongitude;
	var leftLatitude;
	var rightLongitude;
	var rightLatitude;
	for (var int = 0; int < regulatorList.length; int++) {
		if (id == regulatorList[int].id)
		{
		    leftLongitude = regulatorList[int].leftLongitude;
			leftLatitude = regulatorList[int].leftLatitude;
			rightLongitude = regulatorList[int].rightLongitude;
			rightLatitude = regulatorList[int].rightLatitude;
			console.log(leftLongitude)
			console.log(leftLatitude)
			console.log(rightLongitude)
			console.log(rightLatitude)
		    break;
		}
	}
	var PointList;
	var allOverlay = map.getOverlays();
    for (var i = 0; i < allOverlay.length; i++){
		var Polyline1 = allOverlay[i];
		PointList =	Polyline1.getPath();
		console.log(PointList)
    }  */
}

function showAll()
{
	for (var int = 0; int < regulatorList.length; int++) {
		var point1 = new BMap.Point(regulatorList[int].leftLongitude, regulatorList[int].leftLatitude);
		var point2 = new BMap.Point(regulatorList[int].rightLongitude, regulatorList[int].rightLatitude);
        addMarker(point1,point2, regulatorList[int].desc, regulatorList[int].left_distance, regulatorList[int].right_distance);

		if(0==int)
		{
			map.panTo(point1);
		}
	}
}

function hideAll()
{
 	var allOverlay = map.getOverlays();
    for (var i = 0; i < allOverlay.length; i++){
			allOverlay[i].hide();
    }
}

    //标注函数
    function addMarker(point1, point2, name, left_distance, right_distance) {
	    var myIcon = new BMap.Icon("images/电线杆(6).png", new BMap.Size(32,32));
        var leftMarker = new BMap.Marker(point1,{icon:myIcon});
		var rightMarker = new BMap.Marker(point2,{icon:myIcon});
        map.addOverlay(leftMarker);
		map.addOverlay(rightMarker);
        var polyline = new BMap.Polyline([point1,point2], {strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(polyline);   //地图上渲染
        var messageinfo = "故障时间：" + regulatorList[int].occurr_time+"\n";
		    messageinfo += "故障描述："+ regulatorList[int].LineName +"-"+ regulatorList[int].FaultDesc+"\n";
            messageinfo += "距"+regulatorList[int].leftTowerName+"距离：" + regulatorList[int].left_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].rightTowerName+"距离：" + regulatorList[int].right_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].LeftStationName+"距离：" + regulatorList[int].left_station_distance+"  KM"+"\n";
            messageinfo += "距"+regulatorList[int].RightStationName+"距离：" + regulatorList[int].right_station_distance+"  KM";
		polyline.addEventListener("click", function () {
			showSuccessOrErrorModal(messageinfo, "info");
        });

    }

function queryMap(){
 //百度地图API功能
    map.centerAndZoom(new BMap.Point(109.436848,37.527379), 10);	//初始化地图,设置中心点坐标和地图级别

	map.enableScrollWheelZoom(true);						//开启鼠标滚轮缩放
    map.addControl(new BMap.NavigationControl());			//缩放按钮
    //showAll();
}
