//初始化表格
var schoolUserTable = null;
function initCourseTable() {
	schoolUserTable = $('#courseTable').DataTable({
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
					content = '<button class="btn btn-xs blue" onclick="showCourseEditModal(\''+row.id+'\') " data-toggle="modal" data-target="#"> 修改 </button>' +
		            '<button class="btn btn-xs red" onclick="deleteCourse(\''+row.id+'\')"> 删除 </button>';
			         return content;
		      } 
		 }]
	});
}	
// 点击编辑按钮
function showCourseEditModal(id){
	startPageLoading();
	$.ajax({
		url:"manage/coursem/getCourseById.do",
		type:"post",
		data:{"courseId":id},
		dataType:"json",
		success:function(data) {
		   if(data.status=="success") {
               console.log(data)
               var courseData = data.usersData;
               	$('#courseId').val(courseData.id)
               	$('#courseName').val(courseData.course_name);
               	$('#weekLabel').val(courseData.week_label);
               	$('#courseAddr').val(courseData.course_addr);
               	$('#courseTeacher').val(courseData.course_teacher);
               	$('#courseSize').val(courseData.course_size);
               	$('#stuNum').val(courseData.stu_num);
                $('#courseModal').modal('show');
                stopPageLoading()
		   } else {
			   stopPageLoading()
			   showSuccessOrErrorModal("获取课程信息失败","error");
		   }
		   
		},
		error:function(e) {
			stopPageLoading()
		   showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
//新增用户按钮
function addCourse(){
	$("#courseForm")[0].reset();
	$('#courseModal').modal('show');
}
//删除用户
function deleteCourse(courseId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"manage/coursem/deleteCourse.do",
			type:"post",
			data:{"courseId":courseId},
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			        showSuccessOrErrorModal(data.msg,"success"); 
			        schoolUserTable.draw(); //刷新表格
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
	initCourseTable();
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


function saveWeekLabel(){
	$.ajax({
		url:"manage/coursem/saveWeekLabel.do",
		type:"post",
		data:{weekLabel: $("#weekLabelAll").val()},
		dataType:"json",
		success:function(data) {
		    if(data.status=="success") {
		        showSuccessOrErrorModal(data.msg,"success"); 
		        schoolUserTable.draw();
		    	$("#weekLabelModal").modal("hide");
		    } else {
		        showSuccessOrErrorModal(data.msg,"error");	
		    }         
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了","error"); 
		}
	});
}
function log(formstr){
	var res = {};
	formstr.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
	console.log(res)
}


