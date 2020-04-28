//初始化表格
var schoolUserTable = null;
function initSchoolUserTable() {
	schoolUserTable = $('#studentTable').DataTable({
		// url
		"sAjaxSource" : "manage/course/queryStuList.do", 
		// 服务器回调函数 
		"fnServerData": function retrieveData(sSource, aoData, fnCallback) 
		{
			aoData.push({"name":"stuCode_s","value":$("#stuCode_s").val()});
			aoData.push({"name":"stuName_s","value":$("#stuName_s").val()});
			aoData.push({"name":"stuStates_s","value":$("#stuStates_s").val()});
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
					showSuccessOrErrorModal("获取学生信息失败","error");
				}
			});
		},
		// 列属性
		"columns" : [ 
		 {	
			 "title" : "姓名",  
			 "defaultContent" : "", 
			 "data" :"stuName",
			 "width": "20%",
			 "class" : "text-center"  
		 }            
		,  {	
			 "title" : "学号",  
			 "defaultContent" : "", 
			 "data" :"stuCode",
			 "width": "20%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "选课周次",  
			 "defaultContent" : "", 
			 "data" :"weekLabel",
			 "width": "20%",
			 "class" : "text-center"  
		 } ,  {	
			 "title" : "课程",  
			 "defaultContent" : "", 
			 "data" :"courseId",
			 "width": "20%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {// 在这里 时间格式化、内容太多优化显示、一列显示多列值 等问题进行优化
				 var val;
				 if(row.courseId==""){
				 val='<span style="color:red">未选课</span>';
				}
				else {val=row.courseName;}
				 return val;
			 }
		 		
		 },{	
			 "title" : "操作",  
			 "defaultContent" : "", 
			 "data" :null,
			 "width": "20%",
			 "class" : "text-center",
			 "render": function(data, type, row, meta) {
				 var content = "";
					content = '<button class="btn btn-xs blue" onclick="showStudentModal(\''+row.id+'\') " data-toggle="modal" data-target="#">修改 </button>' +
		            '<button class="btn btn-xs red" onclick="deleteStudent(\''+row.id+'\')"> 删除 </button>';
			         return content;
		      } 
		 }]
	});
}
/**
 * 功能描述：查询按钮
 */
function queryStuInfo() {	
	schoolUserTable.draw(); //刷新表格
};
//保存学生信息
function savaStudent(){
	var preCourseId=$('#pre_courseId').val();
	var curCourseId=$('#cur_courseId').val();
	var data=$('#studentForm').serialize();//name,code,weeklabel
	data+="&courseId="+$("#courseId").val()+"&pre_courseId="+preCourseId+"&cur_courseId="+curCourseId;
	
	$.ajax({
		url:"manage/course/saveStudent.do",
		type:'post',
		data:data,
		dataType:'json',
		async:false,
		success:function(data){
			if(data.status=="success"){
				 showSuccessOrErrorModal(data.msg,"success");
			}else{
				 showSuccessOrErrorModal(data.msg,"error");
			}
		},
		error:function(e){
			showSuccessOrErrorModal("请求出错了", "error");
		}
	});
}

//从后台获取课程列表填充下拉框
function getDropCourseList(){

	var html = "";
	$.ajax({
		url:"manage/course/getDropCourseList.do",
		type:'post',
		dataType:'json',
		async : false,
		success:function(data){
			if(data.status=="success")
			{
				var courseList=data.courseList;
				for(var i=0;i<courseList.length;i++)
				{	
					
					html +='<option value="' + courseList[i].courseId + '">'
					+ courseList[i].courseName + '</option>';
					//
					
					
				}
				$("#courseId").append(html);
				$("#stuStates_s").append(html);
			}else{
				showSuccessOrErrorModal("获取课程列表失败", "error");
			}
		},
		error:function(e){
			showSuccessOrErrorModal("请求出错了", "error");
		}
		
		
	});
}


//导入按钮触发模态层
function showExcelImportModal(){ 
	
	$('#excel_improt').modal('show');
	
	
}

function showUpload(){
	
	$('#upload').click();
	$('#upload').change(function(){
		var file=$('#upload').val();
		var arr=file.split('\\');
		var fileName=arr[arr.length-1];
		$('#file').text(fileName);	
		
		
	
	});
	
		
}
function uploadExcelFile(){	
//创建表单  
var formData = new FormData($( "#uploadForm" )[0]);  
var file = $("#upload").val();  


var arr=file.split('\\');//注split可以用字符或字符串分割  
var fileName=arr[arr.length-1];//这就是要取得的文件名称  

//var name = $("input").val();  
formData.append("file",$("#upload")[0].files[0]);  
//formData.append("name",name);  

if ($('#upload').val() == "") {  
       // alert("请选择所要上传的文件");
		showSuccessOrErrorModal("请选择所要上传的文件","error");
	
                              
    }else{  
        var index = file.lastIndexOf("."); 
        
        if(index < 0 ){  
            //alert("上传的文件格式不正确，请选择Excel文件(*.xls)！");  
        	showSuccessOrErrorModal("上传的文件格式不正确，请选择Excel文件(*.xls)！","error");                      
        }else{  
            var ext = file.substring(index + 1, file.length);  
            if(ext == "xls"){  
                
            	doUpload(formData,fileName);  
            }else{  
//              
//           	alert("上传的文件格式不正确，请选择Excel文件(*.xls)！");  
                showSuccessOrErrorModal("上传的文件格式不正确，请选择Excel文件(*.xls)！","error");  
                                      
            }  
        }  


    }  
} 
//上传
function doUpload(formData,fileName) {  
	
    $.ajax({  
         url:"manage/course/import.do" ,  
         type: 'POST',  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false, 
         dataType: "json",
         success: function (data) {  
        	if(data.status=="success"){
             
             $('#excel_improt').modal('hide');
             showSuccessOrErrorModal(data.msg,"success");
             schoolUserTable.draw(); //刷新表格
        	}else{
        		showSuccessOrErrorModal(data.msg,"error");
        	}
            
        	     
             
         },
         error: function (data) { 
        	stopPageLoading()
        	showSuccessOrErrorModal("请求出错了","error"); 
            
            
         }  
    });  
}

//模板下载
function excelfileDown(){
	var ACCESSTOKENID =$.cookie("ACCESSTOKEN");;
	/*var ACCESSTOKENID = "sys_b3f5270890064a11a897b60deb352c94";
	
	var url="views/course/student.xls?ACCESSTOKEN="+ACCESSTOKENID;*/
	var url="manage/course/downFile.do?ACCESSTOKEN="+ACCESSTOKENID;
	
	location.href=url;
}
//点击编辑按钮
function showStudentModal(stuId){
	startPageLoading();
	$.ajax({
		url:"manage/course/getStudentById.do",
		type:"post",
		data:{"stuId":stuId},
		dataType:"json",
		success:function(data) {
		   if(data.status=="success") {
               var stuData = data.usersData;
               
               if(stuData.course_id==""){
            	  // 
            	  //下拉框选中未选课
            	   $('#courseId').val("");
               }else{
            	   
            	   $('#courseId').val(stuData.course_id);
               }
               	$('#stuId').val(stuData.id)
               	$('#stuName').val(stuData.stu_name);
               	$('#stuCode').val(stuData.stu_code);
               	$('#weekLabel').val(stuData.week_label);
               	$('#pre_courseId').val(stuData.course_id);
               	$('#cur_courseId').val(stuData.course_id);
                $('#studentModal').modal('show');
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
function addStudent(){
	$("#studentForm")[0].reset();
	$("#stuId").val("");
	$('#studentModal').modal('show');
}
//删除用户
function deleteStudent(stuId){
	showConfirmModal("是否确定删除！",function(){
		$.ajax({
			url:"manage/course/deleteStudent.do",
			type:"post",
			data:{"stuId":stuId},
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
	initSchoolUserTable();
	getDropCourseList();
	// 表单验证(点击submit触发该方法)
	$("#studentForm").html5Validate(function() {
		// TODO 验证成功之后的操作
		var preCourseId=$('#pre_courseId').val();
		var curCourseId=$('#courseId').val();
		var data = $("#studentForm").serialize();
		data += "&batch_id=" +"201720180202";
		data+="&courseId="+$("#courseId").val()+"&pre_courseId="+preCourseId+"&cur_courseId="+curCourseId;
		log(data)
		$.ajax({
			url:"manage/course/saveStudent.do",
			type:"post",
			data:data,
			dataType:"json",
			success:function(data) {
			    if(data.status=="success") {
			    	
			    	showSuccessOrErrorModal(data.msg,"success"); 
			    	schoolUserTable.draw();
			    	$("#studentModal").modal("hide");
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




