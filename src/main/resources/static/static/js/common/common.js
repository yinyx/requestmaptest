var userMap = {};
var timer;
function cancelModifyWavePassword()
{
	$("#password_edit").val("");
	$("#password_new").val("");
	$("#password_new_c").val("");
}

function closeModal(){
	$("#password_edit").val("");
	$("#password_new").val("");
	$("#password_new_c").val("");
}
$(document).ready(function(){
	//debugger;
	//判断是否登录
	userMap = isLogined();
	if(userMap){//成功登录
		$("#avatarNameId").html(userMap.user_name);
	}else{
		parent.location.href = jQuery.getBasePath() + "/login.html";
	}
	
	//配置DataTables默认参数
    $.extend(true, $.fn.dataTable.defaults, {
        "language": {
			"sProcessing": "正在加载数据...",
			"sLengthMenu": "_MENU_ 条记录",
			"sZeroRecords": "没有查到记录",
			"sInfo": "第  _START_ 条到第  _END_ 条记录，一共  _TOTAL_ 条记录",
			"sInfoEmpty": "0条记录",
			"emptyTable": "表中没有可用数据",
			"oPaginate": {
				"sPrevious": "<",
				"sNext": ">",
				"sFirst":"<<",
				"sLast":">>",
				"sJump":"确定"
			}
		},
        "dom": '<"top"l<"clear">>rt<"bottom"ip<"clear">>',
		"sScrollY":"350px",
		"scrollCollapse":true,
        "scrollX": true,
    	"serverSide":true,
		"bStateSave" : false,
		"ordering": false,
		"autoWidth":true,
		"lengthMenu" : [ [ 5, 15, 20],[ 5, 15, 20] ],
		"pageLength" : 15,
		/*"initComplete": function(settings, json) {
			
		 },*/
		 "drawCallback": function( settings ) {
			 stopPageLoading()
		 }
    });
    $.fn.dataTable.ext.errMode = function (s, h, m) {
        if (h == 1) {
            alert("连接服务器失败！");
        } else if (h == 7) {
            alert("返回数据错误！");
        }
    };
	loadMenu();
	onLoadTopMenu();
	$("#editUserPwd").html5Validate(function() {
		//保存密码
		saveNewPwd();
	}, {
		validate : function() {
			if (!verifyUserPwd($("#password_edit").val())) {
				$("#password_edit").testRemind("您输入的密码不正确!");
				return false;
			} else if ($("#password_new").val() != $("#password_new_c").val()) {
				$("#password_new_c").testRemind("您2次输入的密码不相同!");
				return false;
			}
			return true;
		}
	});
	
});

//加载菜单
function loadMenu(){
	if ((userMap.role == 1)||(userMap.role == 2))
	{
	var topMenuHtml = "<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"200\" onclick=\"onLoadLeftMenu('200')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/configs.png\"/>&nbsp;&nbsp;基本配置</a></li>"+
	"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"300\" onclick=\"onLoadLeftMenu('300')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/point.png\"/>&nbsp;&nbsp;波形诊断</a></li>"+
	"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"400\" onclick=\"onLoadLeftMenu('400')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/device.png\"/>&nbsp;&nbsp;设备管理</a></li>"+
	"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"500\" onclick=\"onLoadLeftMenu('500')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/brick.png\"/>&nbsp;&nbsp;故障分布</a></li>";
	}
    else
    {
	var topMenuHtml ="<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"300\" onclick=\"onLoadLeftMenu('300')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/point.png\"/>&nbsp;&nbsp;波形诊断</a></li>"+
	"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"400\" onclick=\"onLoadLeftMenu('400')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/device.png\"/>&nbsp;&nbsp;设备管理</a></li>"+
	"<li class=\"classic-menu-dropdown\" data-url=\"#\" data-id=\"500\" onclick=\"onLoadLeftMenu('500')\"><a href=\"javascript:;\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/brick.png\"/>&nbsp;&nbsp;故障分布</a></li>";
	}
	
	var leftMenuHtml200 = 
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/RegulatorInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/jianguan.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;监管单位</span><span class=\"selected\"></span></a>"+
		"</li>"+	
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/FactoryInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/changjia.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;终端厂家</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/StationInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/station.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;变电站信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/LineInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/line.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;线路信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/TowerInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/ganta.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;杆塔信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/DeviceInfo.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/zhongduan.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;终端信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"info/UserInfo.html\" onclick=\"onLoadContent(this)\"> <img alt=\"\" class=\"img-circle\" src=\"static/images/common/user.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;用户信息</span><span class=\"selected\"></span></a>"+
		"</li>";	
	var leftMenuHtml300 = 
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"report/WaveReport.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/bxsj.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;波形数据</span><span class=\"selected\"></span></a>"+
		"</li>"+	
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"report/FaultReport.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/bxzd.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;故障诊断</span><span class=\"selected\"></span></a>"+
		"</li>";	
	var leftMenuHtml400 = 
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/CommunicateState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/comm_state.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;通信状态</span><span class=\"selected\"></span></a>"+
		"</li>"+		
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/HeartBeatState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/xt.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;心跳数据</span><span class=\"selected\"></span></a>"+
		"</li>"+	
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/WorkConditionState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/gk.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;工况数据</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/ParameterAttribute.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/sx.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;参数属性</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/ParameterState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/wh.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;参数维护</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/SelfCheck.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/selfcheck.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;自检信息</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/OrderState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/ml.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;命令状态</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"state/AlarmState.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/gj.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;设备告警</span><span class=\"selected\"></span></a>"+
		"</li>";
	var leftMenuHtml500 = 
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"gis/FaultGis.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/gz.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;故障分布</span><span class=\"selected\"></span></a>"+
		"</li>"+	
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"gis/DeviceGis.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/sb.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;设备分布</span><span class=\"selected\"></span></a>"+
		"</li>"+
		"<li class=\"nav-item\"><a href=\"javascript:;\" class=\"nav-link nav-toggle\" data-url=\"gis/LineGis.html\" onclick=\"onLoadContent(this)\"><img alt=\"\" class=\"img-circle\" src=\"static/images/common/line.png\"/><span class=\"title\">&nbsp;&nbsp;&nbsp;线路分布</span><span class=\"selected\"></span></a>"+
		"</li>";
	//locache.set("100",leftMenuHtml100);
	locache.set("200",leftMenuHtml200);
	locache.set("300",leftMenuHtml300);
	locache.set("400",leftMenuHtml400);
	locache.set("500",leftMenuHtml500);
	locache.set("topMenuHtml",topMenuHtml);
}
//填充顶部导航
function onLoadTopMenu(id){
	var $topMenu = $(locache.get("topMenuHtml"));
	
	if(id != null && typeof(id) != "undefined"){
	    $topMenu.each(function(){
			if(id == $(this).attr("data-id")){
				$(this).addClass("active");
				$(this).append(' <span class="selected"></span>');
				return false;
			}
	    });	
	}else{
		//控制默认选中第一个，当前首页的url地址为空，会报错，因此默认加载系统管理
		$($topMenu[0]).addClass("active");
		$($topMenu[0]).append(' <span class="selected"></span>');
		id = $($topMenu[0]).attr("data-id");
	}
	$("#topMenu").html($topMenu);
	onLoadLeftMenu(id);
}

//根据系统菜单id，加载对应的左侧菜单
function onLoadLeftMenu(id){
	//顶部导航选中处理
	$("#topMenu li").each(function(){
		$(this).siblings().removeClass("active");
		$(this).find("span.selected").remove();
		
		if(id == $(this).attr("data-id")){
			$(this).addClass("active");
			$(this).append(' <span class="selected"></span>');
			return false;
		}
	});
	
	//填充左侧菜单栏
	var $leftSidebar = $(locache.get(id));//以后KEY改为id中截取一个字段来使用
	
	$($leftSidebar[0]).addClass("active open");
	$($leftSidebar[0]).find("ul li").first().addClass("active");
	$("#leftSidebar").html($leftSidebar);
	onLoadContent($($leftSidebar[0]).find("a")[0]);
}

//装载content部分内容
function onLoadContent(object){
	if(object){
		var ajaxUrl = object.getAttribute('data-url');
	    var comIds = object.getAttribute('data-comids');
		 var $this = $(object);
		 $("#leftSidebar li").removeClass("active open");
		 $this.parent().addClass("active");
		 $this.parent().parent().parent().addClass("active open");
		 ajaxGetContent(ajaxUrl);
		 // ajaxGetContent("views/info/articleEdit.html");
	}
  
}


//异步加载content部分内容
function ajaxGetContent(url){
	startPageLoading();
	$("#loadDiv").load("views/"+url,function(){
		stopPageLoading();
		handleSidebarAndContentHeight();
	});
}

//对page-concent的高度重计算
var handleSidebarAndContentHeight = function () {
	var resBreakpointMd = App.getResponsiveBreakpoint('md');
	var content = $('.page-content');
	var sidebar = $('.page-sidebar');
	var height;
	var headerHeight = $('.page-header').outerHeight();
	var footerHeight = $('.page-footer').outerHeight();
	var sidebarHeight = sidebar.outerHeight();
	if (App.getViewPort().width < resBreakpointMd) {
	    height = App.getViewPort().height - headerHeight - footerHeight;
	} else {
	    height = sidebar.height() + 20;
	}
	if ((height + headerHeight + footerHeight) <= App.getViewPort().height) {
	    height = App.getViewPort().height - headerHeight - footerHeight;
	}
	if(sidebarHeight>height){
		height = sidebarHeight;
	}
	content.css('min-height', height);
};
//保存密码
function saveNewPwd() {
	var password_new = $("#password_new").val();
	var newpsd = $("#password_edit").val();
	var data = {
			"id":userMap.id,
			"newPassword" : hex_md5(password_new),
			"oldPassword":hex_md5(newpsd)
		};
	var dataObj = {
			"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url : "user/saveNewPassWord",
		type : "post",
		data : dataObj,
		dataType : "text",
		success : function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			console.log(data)
			if (data.result == "success") {
				showSuccessOrErrorModal(data.reason, "success");
				userMap.password = password_new;
				$("#editUserPwd")[0].reset();
				$("#userPwdModal").modal("hide");
			} else {
				showSuccessOrErrorModal(data.reason, "error");
			}
		},
		error : function(e) {
			showSuccessOrErrorModal("保存密码请求出错了", "error");
		}
	})
}
//验证密码
function verifyUserPwd(pwd) {
	var oldPwd = userMap.password;
	if (hex_md5(pwd) == oldPwd) {
		return true;
	} else {
		return false;
	}
}