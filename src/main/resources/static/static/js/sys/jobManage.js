var jobInfos = null;

function initJobTable() {
	document.getElementById("cronID")[1].selected=true;
	$.ajax({
		url : "quartz/queryJobList",
		type : 'post',
		data : {},
		dataType : 'text',
		async : false,
		success : function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			//console.log(data)
			if(data.status == "success"&&data.jobInfos.length!=0){
				jobInfos = data.jobInfos[0];
				$("#cronID").val(jobInfos.cronExpr);
				console.log(jobInfos.previousFireTime)
				if(jobInfos.previousFireTime==null){
					$("#time1").html("");
				}else{
					$("#time1").html(new Date(jobInfos.previousFireTime).toLocaleString().replace(/\//g, "-"));
				}
				$("#time2").html(new Date(jobInfos.nextFireTime).toLocaleString().replace(/\//g, "-"));
				
				if(jobInfos.jobStatus=="NORMAL"){
					$("#jobStatus").html(jobInfos.jobStatus);
				}else{
					$("#jobStatus").html("<span style='color:red'>"+jobInfos.jobStatus+"<span>");
				}
				
				var cron = jobInfos.cronExpr.replace(/\ \?/g, ""); 
			}else{
				showSuccessOrErrorModal("查找定时任务失败","error");
			}
			
		}
	})
}

function resumeJob() {
	showConfirmModal("是否确定重启定时器！",function(){
		$.ajax({
			url : "quartz/resumeJob",
			type : 'post',
			data : {
				"jobName":"firstJobName",
				"jobGroupName":"firstJobGroupName"
			},
			dataType : 'json',
			success : function(data) {
				console.log(data);
				if(data.status == "success"){
					 showSuccessOrErrorModal("重启定时任务成功","success"); 
					initJobTable();
				}else{
					showSuccessOrErrorModal("重启定时任务失败","error");
				}
				
			}
		})
	})
}
//
function editJob(){
	var cronStr = $("#cronID").val();
	console.log(cronStr)
	showConfirmModal("是否确定修改时间表达式！",function(){
		$.ajax({
			url:"quartz/editJob.do",
			type:"post",
			data:{"cron":cronStr},
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        initJobTable();
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了2","error"); 
			}
		});
	});
	
}
$(document).ready(function(){
	
	initJobTable();
	
});


