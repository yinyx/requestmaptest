//初始化表格
var lineTable = null;
var polylines_temp = [];
var pointList = [];
var line_range = "";
var saveAcmtId = "";
var saveAcmtDid = "";

// 点击编辑按钮
function showLineEditModal(acmtId,acmtDid){
	//清空绘图版
	for(var i = 0; i < polylines_temp.length; i++) {
		map.removeOverlay(polylines_temp[i]);
	}
	polylines_temp = [];
	for(var i = 0; i < pointList.length; i++) {
		map.removeOverlay(pointList[i]);
	}
	pointList = [];
	
	saveAcmtId = acmtId;
	saveAcmtDid = acmtDid!="-1"?acmtDid:"";
	var data = {
		"acmtId" : acmtId
	};
	var dataObj = {
		"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"line/queryDeviceByAcmtId.do",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    if(data.result=="success") {
		    	console.log(data)
		    	let dList = data.dList;
		    	for (var i = 0; i < dList.length; i++) {
		    		var point = new BMap.Point(dList[i].longitude,dList[i].latitude);
		    		pointList.push(point)
		    		addMarker(point,dList[i].device_name);
		    	}
		    	let p1 = new BMap.Point(dList[1].longitude,dList[1].latitude);
		    	map.centerAndZoom(p1, 17);
		    	if(data.linePic.acmt_line!=""){
		    		polylinesArr = $.parseJSON( data.linePic.acmt_line );
		    		console.log(polylinesArr);
		    		var points = new Array();
					for(var i = 0; i < polylinesArr.length; i++) {
						points.push(new BMap.Point(polylinesArr[i].lng, polylinesArr[i].lat));
					}
					var polylineObj = new BMap.Polyline(points);
					polylines_temp.push(polylineObj);
					map.addOverlay(polylineObj);
					
		    	}
		    	
		    	
		    	
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了2","error"); 
		}
	});
	$('#lineEditAddModal').modal('show');
}
var styleOptions = {
	strokeColor: "blue", //边线颜色。
	fillColor: "red", //填充颜色。当参数为空时，圆形将没有填充效果。
	strokeWeight: 3, //边线的宽度，以像素为单位。
	strokeOpacity: 0.8, //边线透明度，取值范围0 - 1。
	fillOpacity: 0.6, //填充的透明度，取值范围0 - 1。
	strokeStyle: 'solid' //边线的样式，solid或dashed。
}
var map = new BMap.Map("map", {
	enableMapClick: false
});

//创建标注
function addMarker(point,title){
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
	marker.setTitle(title);
}

$(document).ready(function(){
	initLineTable();
	map.enableScrollWheelZoom();
	map.centerAndZoom("南京", 13);
	map.disableDoubleClickZoom(true);
	map.addControl(new BMap.NavigationControl());
	//比例尺
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
	map.addControl(top_left_control);
	var overlaycomplete = function(e) {
		console.log(e);
		if('polyline' == e.drawingMode) { //线路示意图
			if(polylines_temp.length > 0) {//判断是否重新绘制
				map.removeOverlay(e.overlay);
				
			} else {
				polylines_temp.push(e.overlay);
				var longitude = (e.overlay.Ou.Ge + e.overlay.Ou.Le) / 2;
				var latitude = (e.overlay.Ou.Fe + e.overlay.Ou.Ke) / 2;
				longitude_temp = longitude.toFixed(6);
				latitude_temp = latitude.toFixed(6);
				line_range = JSON.stringify(e.overlay.ia);
				console.log(line_range);
			}
		}
	};
	//实例化鼠标绘制工具
	var drawingManager = new BMapLib.DrawingManager(map, {
		isOpen: false, //是否开启绘制模式
		enableDrawingTool: true, //是否显示工具栏
		drawingToolOptions: {
			anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
			offset: new BMap.Size(5, 5), //偏离值
			drawingModes: [
				BMAP_DRAWING_POLYLINE
				//BMAP_DRAWING_MARKER
				//BMAP_DRAWING_MARKER
			]
		},
		circleOptions: styleOptions, //圆的样式
		polylineOptions: styleOptions, //线的样式
		polygonOptions: styleOptions, //多边形的样式
		rectangleOptions: styleOptions //矩形的样式
	});

	drawingManager.setDrawingMode(BMAP_DRAWING_POLYLINE);

	//添加鼠标绘制工具监听事件，用于获取绘制结果
	drawingManager.addEventListener('overlaycomplete', overlaycomplete);
	
	
});
//保存线路示意图
function saveLinePic(){
	var data = {
			"acmtid":saveAcmtId,
			"acmtDid":saveAcmtDid,
			"line_range":line_range
			};
	console.log(data)
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"line/saveLinePic.do",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		    console.log(data)
			if(data.result=="success") {
		    	//TODO 
				schoolUserTable.draw();
				$("#lineEditAddModal").modal("hide");
		    } else {
		    	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了2","error"); 
		}
	});

}



//重新绘制
function sketchMapAgain() {
	for(var i = 0; i < polylines_temp.length; i++) {
		map.removeOverlay(polylines_temp[i]);
	}
	polylines_temp.length = 0
}
//验证密码
function verifyWavePwd(pwd) {
	var oldPwd = userMap.wave_psd;
	if (pwd == oldPwd) {
		return true;
	} else {
		return false;
	}
}
function initLineTable() {
	schoolUserTable = $('#lineTable').DataTable({
		// url
		"sAjaxSource" : "line/queryLineList.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			
			var aoObj = {};
			for( let i of aoData){
			    aoObj[i.name] = i.value;
			}
			var dataObj = {
					"paramObj":encrypt(JSON.stringify(aoObj),"abcd1234abcd1234")
			}
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "text",
				//contentType: "application/json",
				data: dataObj,
				success: function(data) 
				{
					data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
					//console.log(JSON.stringify(data))
					if(data.status == "success")
					{
						fnCallback(data.usersData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取用户信息失败","error");
				}
			});
		},
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [0],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "visible":false,
			 "title" : "id",  
			 "data" :"id"
			 
		 }            
		,  {	
			 "title" : "线路名称",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 } ,{	
			 "title" : "线路示意图",  
			 "defaultContent" : "", 
			 "data" :"acmtDid",
			 "width": "40%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ='<span style="color:red">未绘制</span>';
		            if(data != "-1"){
		            	content = "已绘制";
		            }
		            return content;
		      }   
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :"id",
			 "width": "40%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ='<button class="btn btn-xs blue" onclick="showLineEditModal(\''+row.id+'\',\''+row.acmtDid+'\')" data-toggle="modal" data-target="#"> 添加 </button>';
		            if(row.acmtDid != "-1"){
		            	content = '<button class="btn btn-xs blue" onclick="showLineEditModal(\''+row.id+'\',\''+row.acmtDid+'\')" data-toggle="modal" data-target="#"> 查看 </button>';
		            }
		            return content;
		      }   
		 }
		 ]
	});
}	