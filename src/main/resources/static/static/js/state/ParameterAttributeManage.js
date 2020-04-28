var regulatorTable = null;
//var userMap = {};
//var userId = 0;
var factoryId = 0;

function cancelModifyWavePassword()
{
	$("#name_m").val("");
	$("#password_m").val("");
}

function closeModal(){
	$("#name_m").val("");
	$("#password_m").val("");
}

function initRegulatorTable() {
	regulatorTable = $('#mainTable').DataTable({
		// url
		"sAjaxSource" : "state/queryParameterAttrList", 
		"bLengthChange":false,//取消显示每页条数
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({ "name": "factory", "value": factoryId}); 
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
					showSuccessOrErrorModal("获取参数属性数据失败","error");
				}
			});
		},
		// 列属性
		"columns" : [{	
			 "title" : "所属厂家",  
			 "defaultContent" : "", 
			 "data" :"factory",
			 "width": "10%",
			 "class" : "text-center"  
		 } 
		,	 {	
			 "title" : "参数序号",  
			 "defaultContent" : "", 
			 "data" :"indexno",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		,	 {	
			 "title" : "参数名称",  
			 "defaultContent" : "", 
			 "data" :"name",
			 "width": "10%",
			 "class" : "text-center"  
		 }  
		,	 {	
			 "title" : "参数类型",  
			 "defaultContent" : "", 
			 "data" :"type",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="int";
		            if(data == 0){
		            	content = "float";
		            }
		            return content;
		      }    
		 }   
		,	 {	
			 "title" : "是否私有",  
			 "defaultContent" : "", 
			 "data" :"isPrivate",
			 "width": "10%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
		            var content ="私有";
		            if(data == 0){
		            	content = "公有";
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
                 '<button class="btn btn-xs red" onclick="deleteSchoolUser(\''+row.id+'\')"> 删除 </button>';
		         return content;
		      } 
		 }			   
		 ]
	});
}

// 点击编辑按钮
function showEditModal(recordId){
	startPageLoading();
	var data = {"recordId":recordId};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"state/getParamAttrById",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
		   data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
		   if(data.status=="success") {
               var usersData = data.usersData;
               console.log(usersData)
			   console.log(recordId)
               $("#recordId").val(recordId);
			   $("#indexno_m").val(usersData.indexno);
			   $("#parmattrname_m").val(usersData.name);
			   if(usersData.type == '0') {
       			   $("#r_String").prop("checked",true); 
       		   } else {
       			   $("#r_Int").prop("checked",true); 
       		   }
			   if(usersData.isPrivate == '1') {
       			   $("#s_private").prop("checked",true); 
       		   } else {
       			   $("#s_public").prop("checked",true); 
       		   }
               $('#regulatorModal_add').modal('show');
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取参数属性信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了1","error"); 
		}
	});
}

function deleteSchoolUser(recordId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"state/deleteParamAttrById",
			type:"post",
			data:{"recordId":recordId},
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

function addRegulator()
{
	$("#recordId").val("");
}

function queryLog()
{	
	factoryId = $("#cronFactory").val();
    initRegulatorTable();	
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
	//debugger;
	$("#addAttr").attr("style","display:none;");
	clearInterval(timer);
	showTime();
	timer = setInterval("showTime()",10000);
	initFactory();
	initRegulatorTable();
		$("#regulatorForm").html5Validate(function() {
	    var FactoryName = $("#name_m").val();
		var FactoryPassword = $("#password_m").val();
		var data = {
				"username": FactoryName,
				"password" : FactoryPassword,
				"factoryId" : factoryId				
			};
		var dataObj = {
				"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
		}
		$.ajax({
			url : "state/checkFactoryLogin",
			type : "post",
			data :dataObj,
			dataType : "text",
			success : function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				if (data.status == "success") {
					$("#addAttr").attr("style","display:;");
					showSuccessOrErrorModal(data.msg, "success");
					$("#regulatorForm")[0].reset();
					$("#editWavePwd").modal("hide");
					regulatorTable.draw();  
					//initRegulatorTable();
				} else {
					showSuccessOrErrorModal(data.msg, "error");
					//regulatorTable.destroy();  
				}
			},
			error : function(e) {
				showSuccessOrErrorModal("登录检查请求出错了", "error");
			}
		})
	}, {
		validate : function() {
			return true;
		}
	});
	
	$("#paramAttrForm").html5Validate(function() {
	    var rd=document.getElementsByName('paratype');
		var rd1=document.getElementsByName('isPrivate');
		var type = 0;
		if (rd[1].checked)
		{
			type = 1;
		}
/* 		else if(rd[2].checked)
		{
			type = 1;
		} */
		var isPrivate = 0;
		if (rd1[1].checked)
		{
			isPrivate = 1;
		}
		var data = {
			    "recordId": $("#recordId").val(),
				"indexno": $("#indexno_m").val(),
				"name" : $("#parmattrname_m").val(),
				"type" : type,
                "isPrivate" : isPrivate,
				"factoryId" : factoryId	
			};
		var dataObj = {
				"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
		}
		$.ajax({
			url : "state/addParamAttr",
			type : "post",
			data :dataObj,
			dataType : "text",
			success : function(data) {
				data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
				if (data.status == "success") {					
					showSuccessOrErrorModal(data.msg, "success");
					$("#paramAttrForm")[0].reset();
					$("#regulatorModal_add").modal("hide");
					regulatorTable.draw();  

				} else {
					showSuccessOrErrorModal(data.msg, "error");
					//regulatorTable.destroy();  
				}
			},
			error : function(e) {
				showSuccessOrErrorModal("保存参数属性出错了", "error");
			}
		})
	}, {
		validate : function() {
			return true;
		}
	});
});


