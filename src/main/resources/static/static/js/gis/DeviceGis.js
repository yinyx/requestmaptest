var map;
var userMap = {};
var userId = 0;
var regulatorList = {};

function queryLog() {//条件查询同步日志
	regulatorTable.ajax.reload();  
}

function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "gis/queryDeviceList", 
		"bLengthChange":false,//取消显示每页条数
		"info":false,
		"sScrollY":"450px",
		"scrollCollapse":true,
		"pageLength" : 50,
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "line", "value": $("#cronLine").val()}); 
			aoData.push({ "name": "device_id", "value": $("#device_id").val()});
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
					showSuccessOrErrorModal("获取装置信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [	 {	
			 "title" : "装置标识",  
			 "defaultContent" : "", 
			 "data" :"name",
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
		    showSuccessOrErrorModal("请求线路列表出错了","error"); 
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
	initRegulatorTable();
	initDeviceList();
    map = new BMap.Map("container", { minZoom: 4, maxZoom: 15 });	//创建Map实例,设置地图允许的最小/大级别
	queryMap();
});

function initDeviceList()
{
	var data = {"userId":userId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"gis/queryDeviceListForGis",
		type:"post",
		data:dataObj,
		dataType:"text",
		async:false,
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.status=="success") {
		        regulatorList = data.dataList;
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求装置列表出错了","error"); 
		}
	});
}

function editJob(id)
{
 	var allOverlay = map.getOverlays();
    for (var i = 0; i < allOverlay.length -1; i++){
        if(allOverlay[i].getLabel().getTitle() == id){
			allOverlay[i].show();
					var p = allOverlay[i].getPosition();       //获取marker的位置
					console.log(p.lng)
					console.log(p.lat)
					if ((0==p.lng)||(null==p.lng)||(0==p.lat)||(null==p.lat))
					{
						showSuccessOrErrorModal("请补充相关经纬度信息", "info");
			            return;
					}
					map.panTo(p);  
            return false;
        }
    } 
} 

function hideJob(id)
{
 	var allOverlay = map.getOverlays();
    for (var i = 0; i < allOverlay.length -1; i++){
		var overlaytitle = allOverlay[i].getLabel().getTitle();
        if(allOverlay[i].getLabel().getTitle() == id){
			allOverlay[i].hide();
            return false;
        }
    } 
} 

function showAll()
{
 	var allOverlay = map.getOverlays();
    for (var i = 0; i < allOverlay.length; i++){
			allOverlay[i].show();
			if (0==i)
			{
					var p = allOverlay[i].getPosition();       //获取marker的位置
					map.panTo(p); 
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
    function addMarker(point, id, name) {
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
	    var label = new BMap.Label(id,{offset:new BMap.Size(20,-10)});
		label.setContent("坐标：<br>" + point.lng + "," + point.lat + "<br>唯一主键："+id);
		label.setTitle(id)
        label.setStyle({
    	   display:"none"
    	});
        marker.setLabel(label);
		//marker.hide();
        var messageinfo = "装置标识：" + name+"\n";
	        messageinfo += "坐标：" + point.lng + ",   " + point.lat;

		marker.addEventListener("click", function () {
			showSuccessOrErrorModal(messageinfo, "info");
        });

    }

function queryMap(){
 //百度地图API功能
    map.centerAndZoom(new BMap.Point(110.08139, 37.477501), 10);	//初始化地图,设置中心点坐标和地图级别
	
	map.enableScrollWheelZoom(true);						//开启鼠标滚轮缩放
    map.addControl(new BMap.NavigationControl());			//缩放按钮

	for (var int = 0; int < regulatorList.length; int++) {
		var point = new BMap.Point(regulatorList[int].longitude, regulatorList[int].latitude);
        addMarker(point,regulatorList[int].id, regulatorList[int].name);
	}
}