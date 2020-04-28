$(document).ready(function(){
	queryWave()
	
    
    
});
// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));
function queryWave(){
	
	var path = $("#wave_path").val();
	var data = {"fileName":path,"si":0,"ei":12000};
	var dataObj = {
		"paramObj":encrypt(JSON.stringify(data),"abcd1234abcd1234")
	}
	$.ajax({
		url:"sys/queryWaveData",
		type:"post",
		data:dataObj,
		dataType:"text",
		success:function(data) {
			data = $.parseJSON(decrypt(data,"abcd1234abcd1234"));
			console.log(data)
			if (data.status == "success") {
				var dataList = data.dataList;
				var dataMap = data.data;
				var df2 = (dataMap.df2)?(dataMap.df2):("");
				option = {
		                title: {
		                    text: dataMap.time,
		                    x: 'center'
		                },
		                tooltip: {
		                    trigger: 'axis',
		                    axisPointer: {
		                        animation: false
		                    }
		                },
		                legend: {
		                    data:['通道1','通道2'],
		                    x: 'left'
		                },
		                axisPointer: {
		                    link: {xAxisIndex: 'all'}
		                },
		                dataZoom: [
		                    {
		                        show: true,
		                        realtime: true,
		                        start: 30,
		                        end: 70,
		                        xAxisIndex: [0, 1]
		                    },
		                    {
		                        type: 'inside',
		                        realtime: true,
		                        start: 30,
		                        end: 70,
		                        xAxisIndex: [0, 1]
		                    }
		                ],
		                grid: [{
		                    left: 50,
		                    right: 50,
		                    height: '35%'
		                }, {
		                    left: 50,
		                    right: 50,
		                    top: '55%',
		                    height: '35%'
		                }],
		                xAxis : [
		                    {
		                        type : 'category',
		                        boundaryGap : false,
		                        axisLine: {onZero: true},
		                        data: dataList[2],
		                        show:false
		                    },
		                    {
		                        gridIndex: 1,
		                        type : 'category',
		                        boundaryGap : false,
		                        axisLine: {onZero: true},
		                        data: dataList[2],
		                        position: 'top'
		                    }
		                ],
		                yAxis : [
		                    {
		                        name : '通道1',
		                        type : 'value'
		                    },
		                    {
		                        gridIndex: 1,
		                        name : '通道2 ' + df2,
		                        type : 'value'
		                    }
		                ],
		                series : [
		                    {
		                        name:'通道1',
		                        type:'line',
		                        symbolSize: 8,
		                        hoverAnimation: false,
		                        data:dataList[0]
		                    },
		                    {
		                        name:'通道2',
		                        type:'line',
		                        xAxisIndex: 1,
		                        yAxisIndex: 1,
		                        symbolSize: 8,
		                        hoverAnimation: false,
		                        data:dataList[1]
		                    }
		                ]
		            };
					myChart.setOption(option);
			} else {
				showSuccessOrErrorModal(data.msg, "error");
			}
			
		},
		error:function(e) {
		    showSuccessOrErrorModal("请求出错了2","error"); 
		}
		});
}
    


