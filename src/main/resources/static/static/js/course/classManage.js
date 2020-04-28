//初始化表格
var classTable = null;
var courseTable=null;

function initClassTable() {
	classTable= $('#classTable').DataTable({
		// url
		"sAjaxSource" : "manage/course/getClassByCourseId.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{	
			aoData.push( { "name": "courseId", "value":$("#courseId").val()});
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(aoData),
				success: function(data) 
				{
					if(data.status == "success")
					{
						fnCallback(data.classData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取班级信息失败","error");
				}
			});
		},
		"scrollX": false,
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [0,1,2],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "姓名",  
			 "defaultContent" : "", 
			 "data" :"stuName",
			 "width": "33.3%",
			 "class" : "text-center"  
		 } , {	
			 "title" : "学号",  
			 "defaultContent" : "", 
			 "data" :"stuCode",
			 "width": "10%",
			 "class" : "text-center"  
		 },{	
			 "title" : "上课周次",  
			 "defaultContent" : "", 
			 "data" :"weekLabel",
			 "width": "10%",
			 "class" : "text-center"  
		 }   
		]
		
	});
}

function initCourseTable() {
	courseTable= $('#courseTable').DataTable({
		// url
		"sAjaxSource" : "manage/coursem/queryCouseList.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			$.ajax({
				type: "POST",
				url: sSource,
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(aoData),
				success: function(data) 
				{
					if(data.status == "success")
					{
						fnCallback(data.stuData);
					}else{
						showSuccessOrErrorModal(data.msg,"error");
					}
				},
				error:function(e){
					showSuccessOrErrorModal("获取课程信息失败","error");
				}
			});
		},
		// 列属性-类处理
		"columnDefs" : [ 
            {
	            targets: [0,1,2,3,4,5],
	        	render: function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
	        		return isNullThen(data);
	        	}
		 }],
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "课程名称",  
			 "defaultContent" : "", 
			 "data" :"course_name",
			 "width": "20%",
			 "class" : "text-center"  
		 } , {	
			 "title" : "上课周次",  
			 "defaultContent" : "", 
			 "data" :"week_label",
			 "width": "10%",
			 "class" : "text-center"  
		 },{	
			 "title" : "上课地点",  
			 "defaultContent" : "", 
			 "data" :"course_addr",
			 "width": "20%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "上课老师",  
			 "defaultContent" : "", 
			 "data" :"course_teacher",
			 "width": "10%",
			 "class" : "text-center"  
		 }, {	
			 "title" : "课程规模",  
			 "defaultContent" : "", 
			 "data" :"course_size",
			 "width": "10%",
			 "class" : "text-center"  
		 }, {	
			 "title" : "已选学生人数",  
			 "defaultContent" : "", 
			 "data" :"stu_num",
			 "width": "10%",
			 "class" : "text-center"  
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "20%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
					content = '<button class="btn btn-xs blue" onclick="showClassModal(\''+row.id+'\') "> 查看 </button>'+
		            '<button class="btn btn-xs blue" onclick="exportClass(\''+row.id+'\',\''+row.course_name+'\')"> 导出班级 </button>';
			         return content;
		      } 
		 }]
		
	});
}	
// 点击查看按钮
function showClassModal(courseId,courseName){
	 startPageLoading()
	 $("#courseId").val(courseId);
	 classTable.draw();
	 $('#classModal').modal('show');
}

//导出所有班级
function exportAllClass(){
	showConfirmModal("是否确定导出所有班级?",function(){
		var ACCESSTOKENID =$.cookie("ACCESSTOKEN");
		var url = "manage/course/exportAllClass.do?ACCESSTOKEN="+ACCESSTOKENID;
		location.href=url;
		showSuccessOrErrorModal("导出完成！","success");	
	});
}
//导出班级
function exportClass(id,course_name){
	console.log(id);
	showConfirmModal("是否确定导出该班级?",function(){
		var courseId=id;
		var	courseName=course_name;
		var ACCESSTOKENID =$.cookie("ACCESSTOKEN");
		var url = "manage/course/exportClass.do?ACCESSTOKEN="+ACCESSTOKENID+"&courseId="+courseId+"&courseName="+courseName;
		location.href=url;
		showSuccessOrErrorModal("导出完成！","success");	
	});
}

//新增用户按钮
$(document).ready(function(){
	initCourseTable();
	initClassTable();
	// 表单验证(点击submit触发该方法)
	$("#courseForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var data = $("#courseForm").serialize();
		log(data)
		$.ajax({
			url:"manage/coursem/saveCourse.do",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	console.log(data)
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#courseModal").modal("hide");
			    } else {
			        showSuccessOrErrorModal(data.msg,"error");	
			    }         
			},
			error:function(e) {
			    showSuccessOrErrorModal("请求出错了","error"); 
			}
		});
	});
});
function log(formstr){
	var res = {};
	formstr.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
	console.log(res)
}


