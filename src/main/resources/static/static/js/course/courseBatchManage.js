var curBaitch = {};
var isAdd = false;
function queryCurBatch(){
	$.ajax({
		url:"manage/course/queryCurBatch.do",
		type:"post",
		dataType:"json",
		async : false,
		success:function(data) {
		   if(data.status=="success") {
               var batchData = data.batchData;
               console.log(batchData);
               curBaitch = batchData;
               var batch_status = batchData.batch_status;
               //初始化状态
               drawBatchStatus()
               //初始化 按钮
               cancelButn();
               //初始化表格数据
               drawTabText(batchData);
               stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取选课批次失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
function drawBatchStatus(){
	$('#batchSta div').hide();
	if(curBaitch.batch_status==0){//未发布
		$('#batchSta h1').html("当前选课状态:未发布");
		$('.unpush').show();
	}else if(curBaitch.batch_status==1){//进行中
		$('#batchSta h1').html("当前选课状态:进行中");
		$('.ongoing').show();
	}else if(curBaitch.batch_status==2){//已结束
		$('#batchSta h1').html("当前选课状态:已结束");
		$('.complete').show();
		
	}
}
function drawTabText(batchData){
	$('#batch_id .tabText').html(batchData.batch_id);
   	$('#batch_name .tabText').html(batchData.batch_name);
	$('#class_size .tabText').html(batchData.class_size+" 人/课");
	$('#start_time .tabText').html(batchData.start_time);
	$('#end_time .tabText').html(batchData.end_time);
}
function drawInput(batchData){
	$('#batch_id input').val(batchData.batch_id);
   	$('#batch_name input').val(batchData.batch_name);
	$('#class_size input').val(batchData.class_size);
	$('#start_time input').val(batchData.start_time);
	$('#end_time input').val(batchData.end_time);
}
//编辑
function editBatchInfo(){
	isAdd = false;
	$('#batch_id input').attr("readonly",true);
	$('.tabText').hide();
	$('.tabInput').show();
	$("button").hide();
    $("#saveBtn").show();
    $("#cancelBtn").show();
	drawInput(curBaitch)
}
//取消
function cancelButn(){
	$('.tabText').show();
	$('.tabInput').hide();
	$("button").hide();
	if(curBaitch.batch_status==0){//未发布
		 $("#pushBtn").show();
		 $("#editBtn").show();
	}else if(curBaitch.batch_status==1){//进行中
		 $("#editBtn").show();
	}else if(curBaitch.batch_status==2){//已结束
		 $("#addBtn").show();
	}
}
//比较时间大小
function checkEndTime(){
	var startTime=$("#sTime").val();
	var endTime=$("#eTime").val();
	var start=new Date(startTime.replace("-", "/").replace("-", "/"));
	var end=new Date(endTime.replace("-", "/").replace("-", "/"));
	if(end<=start){
		return false ;
	}
	return true;
}
//新增
function addNewBatch(){
	isAdd = true;
	$('#batch_id input').attr("readonly",false);
	$('.tabText').hide();
	$('.tabInput').show();
	$("button").hide();
    $("#saveBtn").show();
    $("#cancelBtn").show();
    $('#batch_id input').val();
   	$('#batch_name input').val();
	$('#class_size input').val();
	$('#start_time input').val();
	$('#end_time input').val();
}
//发布
function publishNewBatch(){
	showConfirmModal("是否确定发布该选课批次",function(){
		$.ajax({
			url:"manage/course/publishNewBatch.do",
			type:"post",
			data:{"id":curBaitch.id},
			dataType:"json",
			async : false,
			success:function(data) {
				$('.sweet-overlay').hide();
				$('.sweet-alert').hide()
			    if(data.status=="success") {
			        queryCurBatch()
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	});
	
}

$(document).ready(function(){
	queryCurBatch();
	$('.date-picker').datepicker({
		language : 'zh-CN',
		autoclose : true,
		todayHighlight : true
	});
	// 表单验证(点击submit触发该方法)
	$("#batchForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#batchForm").serialize();
		if(isAdd){
			data += "&id=" + "";
		}else{
			data += "&id=" + curBaitch.id;
			/*data += "&batch_id=" + curBaitch.batch_id;*/
		}
		console.log(data);
		$.ajax({
			url:"manage/course/saveCourseBatch.do",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	console.log(data)
			    	queryCurBatch();
			    	showSuccessOrErrorModal(data.msg,"success");	
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	}, {
		 validate: function() {
	        if (!checkEndTime()) {
	        	$("#eTime").testRemind("截止时间应该大于开始时间！");
	            return false;  
	        }
	       return true;  
		}
	});
});


