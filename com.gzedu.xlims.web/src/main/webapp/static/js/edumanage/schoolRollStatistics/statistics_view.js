/**
 * Created by Administrator on 2017/5/12.
 */

var geoCoordMap = {
    "广州麓湖体验中心（广州总部）":[113.282728,23.151831],
    "广州市开发区学习中心":[113.532061,23.065869],
    "佛山电大":[113.117012,23.037045],
    "深圳市宝安区东方培训中心":[113.828171,22.677558],
    "广州市番禺区培贤教育培训中心":[113.389465,22.949517],
    "中山职业技术学院":[113.387713,22.534285],
    "深圳科讯人才培训中心":[114.091082,22.547011],
    "广州麓湖学习中心":[113.282728,23.151831]
};

//select2
//$(".select2").select2();
// filter tabs
$(".filter-tabs li").click(function(event) {
    if($(this).hasClass('actived')){
        $(this).removeClass('actived');
    }
    else{
        $(this).addClass('actived');
    }
});




// 标签页
$('.nav-tabs-lg a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
    if($(this).attr("href")=='#tab_top_2'){
        var $tabItem=$(".tab-pane:last");
        if($tabItem.find('[data-role="chart"]').children('div').width()==0){
            $(window).resize();
        }
    }
});
/* echarts 图表 */

// 年龄
var ecBar=(function(){
    var obj={};
    var user_grade_id = $('#user_grade_id').val();
    var user_pycc_id = $('#user_pycc_id').val();
    var user_specialty_id = $('#user_specialty_id').val();
    $.ajax({
        type: "GET",
        url:ctx + "/recruitmanage/signup/searchSignUserAge",
        data:{GRADE_ID:user_grade_id,PYCC_ID:user_pycc_id,SPECIALTY_ID:user_specialty_id},
        dataType:"json",
        async: true,
        success:function(datas){
            if(datas!=null){
                $("#male").html(datas.MALE_COUNT);
                $("#female").html(datas.FEMALE_COUNT);
                $("#femalePercent").html(datas.FEMALE_PERCENT);
                $("#malePercent").html(datas.MALE_PERCENT);
                var male = parseInt(datas.MALE_PERCENT)/10;
                var female = parseInt(datas.FEMALE_PERCENT)/10;
                $("#malePic").empty();
                $("#femalePic").empty();
                for(var i=1;i<=male;i++){

                    $("#malePic").append("<i class='fa fa-fw fa-male text-blue'></i>");
                }
                for(var i=1;i<=female;i++){
                    $("#femalePic").append("<i class='fa fa-fw fa-female text-red'></i>");
                }

                var data = [
                    {
                        name:'00后',
                        value:datas.AGE_FLAG_00s,
                        itemStyle:{
                            normal:{
                                color:'#f56954'
                            }
                        }
                    },
                    {
                        name:'90后',
                        value:datas.AGE_FLAG_90s,
                        itemStyle:{
                            normal:{
                                color:'#00a65a'
                            }
                        }
                    },
                    {
                        name:'80后',
                        value:datas.AGE_FLAG_80s,
                        itemStyle:{
                            normal:{
                                color:'#00c0ef'
                            }
                        }
                    },
                    {
                        name:'70后',
                        value:datas.AGE_FLAG_70s,
                        itemStyle:{
                            normal:{
                                color:'#ffa412'
                            }
                        }
                    },
                    {
                        name:'60后',
                        value:datas.AGE_FLAG_60s,
                        itemStyle:{
                            normal:{
                                color:'#3c8dbc'
                            }
                        }
                    },
                    {
                        name:'60前',
                        value:datas.AGE_FLAG_60s_ago,
                        itemStyle:{
                            normal:{
                                color:'#8b52cc'
                            }
                        }
                    }
                ];
                obj.chart.hideLoading();
                obj.chart.setOption({
                    xAxis:[
                        {
                            data:$.map(data,function(n){ return n.name })
                        }
                    ],
                    series:[
                        {
                            data : data
                        }
                    ]
                });
            }
        }
    });
    /*
     obj.data=[
     {
     name:'00后',
     value:10,
     itemStyle:{
     normal:{
     color:'#f56954'
     }
     }
     },
     {
     name:'90后',
     value:30,
     itemStyle:{
     normal:{
     color:'#00a65a'
     }
     }
     },
     {
     name:'80后',
     value:20,
     itemStyle:{
     normal:{
     color:'#00c0ef'
     }
     }
     },
     {
     name:'70后',
     value:20,
     itemStyle:{
     normal:{
     color:'#ffa412'
     }
     }
     },
     {
     name:'60后',
     value:10,
     itemStyle:{
     normal:{
     color:'#3c8dbc'
     }
     }
     },
     {
     name:'60前',
     value:10,
     itemStyle:{
     normal:{
     color:'#8b52cc'
     }
     }
     }
     ];
     */
    /*function sum(arr){
     var r=0;
     for (var i = 0; i < arr.length; i++) {
     r+=arr[i].value;
     };
     return r;
     };*/

    obj.chart=echarts.init(document.getElementById('barChart'));
    obj.chart.setOption({
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter:'{b}：{c}%'
            /*formatter:function (params, ticket, callback) {
             return params[0].name+'：'+params[0].value
             +' '+Math.ceil(params[0].value/sum(obj.data)*100)+'%';
             }*/
        },
        grid: {
            left:0,
            right:20,
            bottom: 0,
            top:20,
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                //   data : ['00后', '90后', '80后', '70后', '60后', '60前'],
                data : [],
                axisTick: {
                    alignWithLabel: true
                },
                axisLine:{
                    lineStyle:{
                        color:'#d2d6de',
                        width:1
                    }
                },
                axisLabel:{
                    textStyle:{
                        color:'#666666'
                    }
                },
                splitArea:{
                    show:true
                }

            }
        ],
        yAxis : [
            {
                type : 'value',
                min:0,
                max:100,
                axisLine:{
                    //show:false
                    lineStyle:{
                        color:'#d2d6de',
                        width:1
                    }
                },
                axisLabel:{
                    textStyle:{
                        color:'#666666'
                    },
                    formatter:'{value}%'
                },
                axisTick:{
                    //show:false
                },
                splitLine:{
                    //show:false
                }
            }
        ],
        series : [
            {
                name:'直接访问',
                type:'bar',
                barWidth: '50%',
                label:{
                    normal:{
                        position:'top',
                        show:true,
                        formatter: '{c}%'
                    }
                },
                data:obj.data
                // data:[]
            }
        ]
    });

    $('#signUpUser').delegate(':input','change',function(){
        var user_grade_id = $('#user_grade_id').val();
        var user_pycc_id = $('#user_pycc_id').val();
        var user_specialty_id = $('#user_specialty_id').val();
        $.ajax({
            type: "GET",
            url:ctx + "/recruitmanage/signup/searchSignUserAge",
            data:{GRADE_ID:user_grade_id,PYCC_ID:user_pycc_id,SPECIALTY_ID:user_specialty_id},
            dataType:"json",
            async: true,
            success:function(datas){
                if(datas!=null){
                    $("#male").html(datas.MALE_COUNT);
                    $("#female").html(datas.FEMALE_COUNT);
                    $("#femalePercent").html(datas.FEMALE_PERCENT);
                    $("#malePercent").html(datas.MALE_PERCENT);
                    var male = parseInt(datas.MALE_PERCENT)/10;
                    var female = parseInt(datas.FEMALE_PERCENT)/10;
                    $("#malePic").empty();
                    $("#femalePic").empty();
                    for(var i=1;i<=male;i++){
                        $("#malePic").append("<i class='fa fa-fw fa-male text-blue'></i>");
                    }
                    for(var i=1;i<=female;i++){
                        $("#femalePic").append("<i class='fa fa-fw fa-female text-red'></i>");
                    }
                    var data = [
                        {
                            name:'00后',
                            value:datas.AGE_FLAG_00s,
                            itemStyle:{
                                normal:{
                                    color:'#f56954'
                                }
                            }
                        },
                        {
                            name:'90后',
                            value:datas.AGE_FLAG_90s,
                            itemStyle:{
                                normal:{
                                    color:'#00a65a'
                                }
                            }
                        },
                        {
                            name:'80后',
                            value:datas.AGE_FLAG_80s,
                            itemStyle:{
                                normal:{
                                    color:'#00c0ef'
                                }
                            }
                        },
                        {
                            name:'70后',
                            value:datas.AGE_FLAG_70s,
                            itemStyle:{
                                normal:{
                                    color:'#ffa412'
                                }
                            }
                        },
                        {
                            name:'60后',
                            value:datas.AGE_FLAG_60s,
                            itemStyle:{
                                normal:{
                                    color:'#3c8dbc'
                                }
                            }
                        },
                        {
                            name:'60前',
                            value:datas.AGE_FLAG_60s_ago,
                            itemStyle:{
                                normal:{
                                    color:'#8b52cc'
                                }
                            }
                        }
                    ];
                    obj.chart.hideLoading();
                    obj.chart.setOption({
                        xAxis:[
                            {
                                data:$.map(data,function(n){ return n.name })
                            }
                        ],
                        series:[
                            {
                                data : data
                            }
                        ]
                    });
                }
            }
        });
    });
    // $('#signUpUser :input:eq(0)').trigger('change');
    return obj;
})();
// 学习中心统计
var ecMap2=(function(){
    var obj={};

    obj.chart=echarts.init(document.getElementById('mapChart1'));
    obj.chart.setOption({
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type : 'shadow'
            },
            formatter:'{b}：{c}'
        },
        grid: {
            left:10,
            right:10,
            bottom: 10,
            top:20,
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data:[],
                axisTick: {
                    alignWithLabel: true
                },
                axisLine:{
                    lineStyle:{
                        color:'#d2d6de',
                        width:1
                    }
                },
                axisLabel:{
                    textStyle:{
                        color:'#666666'
                    }
                }

            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLine:{
                    lineStyle:{
                        color:'#d2d6de',
                        width:1
                    }
                },
                axisLabel:{
                    textStyle:{
                        color:'#666666'
                    },
                    formatter:'{value}'
                }
            }
        ],
        series : [
            {
                name:'报读资料统计',
                type:'bar',
                barWidth: '50%',
                label:{
                    normal:{
                        position:'top',
                        show:true,
                        formatter: '{c}'
                    }
                },
                data:[]
            }
        ],
    });
    $('#signUpCenter').delegate(':input','change',function(){
        var center_grade_id = $("#center_grade_id").val();
        var center_pycc_id = $("#center_pycc_id").val();
        var center_specialty_id = $("#center_specialty_id").val();
        $.ajax({
            type: "GET",
            url:ctx + "/recruitmanage/signup/queryStudyCenter",
            data:{CENTER_GRADE_ID:center_grade_id,CENTER_PYCC_ID:center_pycc_id,CENTER_SPECIALTY_ID:center_specialty_id},
            dataType:"json",
            async: true,
            success:function(datas){
                if(datas!=null){
                    var data = [
                        {
                            name:datas.SC_NAME_1,
                            value:datas.STUDY_COUNT_1,
                            itemStyle:{
                                normal:{
                                    color:'#f56954'
                                }
                            }
                        },
                        {
                            name:datas.SC_NAME_2,
                            value:datas.STUDY_COUNT_2,
                            itemStyle:{
                                normal:{
                                    color:'#00a65a'
                                }
                            }
                        },
                        {
                            name:datas.SC_NAME_3,
                            value:datas.STUDY_COUNT_3,
                            itemStyle:{
                                normal:{
                                    color:'#00c0ef'
                                }
                            }
                        },
                        {
                            name:datas.SC_NAME_4,
                            value:datas.STUDY_COUNT_4,
                            itemStyle:{
                                normal:{
                                    color:'#ffa412'
                                }
                            }
                        },
                        {
                            name:datas.SC_NAME_5,
                            value:datas.STUDY_COUNT_5,
                            itemStyle:{
                                normal:{
                                    color:'#3c8dbc'
                                }
                            }
                        }
                    ];
                    obj.chart.hideLoading();
                    obj.chart.setOption({
                        xAxis:[
                            {
                                data:$.map(data,function(n){ return n.name })
                            }
                        ],
                        series:[
                            {
                                data : data
                            }
                        ]
                    });
                }
            }
        });
    });
    $('#signUpCenter :input:eq(0)').trigger('change');
    return obj;
})();



// 区域统计
/**
var ecMap=(function(){
    var obj={};

    obj.mapChartData=[
        {name: '北京',value: 7 },
        {name: '天津',value: 2 },
        {name: '上海',value: 12 },
        {name: '重庆',value: 14 },
        {name: '河北',value: 8 },
        {name: '河南',value: 1 },
        {name: '云南',value: 15 },
        {name: '辽宁',value: 5 },
        {name: '黑龙江',value: 9 },
        {name: '湖南',value: 7 },
        {name: '安徽',value: 17 },
        {name: '山东',value: 2 },
        {name: '新疆',value: 4 },
        {name: '江苏',value: 14 },
        {name: '浙江',value: 2 },
        {name: '江西',value: 7},
        {name: '湖北',value: 9 },
        {name: '广西',value: 19 },
        {name: '甘肃',value: 7 },
        {name: '山西',value: 3 },
        {name: '内蒙古',value: 5 },
        {name: '陕西',value: 7 },
        {name: '吉林',value: 8 },
        {name: '福建',value: 12 },
        {name: '贵州',value: 10 },
        {name: '广东',value: 24 },
        {name: '青海',value: 4 },
        {name: '西藏',value: 7 },
        {name: '四川',value: 2 },
        {name: '宁夏',value: 18 },
        {name: '海南',value: 14 },
        {name: '台湾',value: 16 },
        {name: '香港',value: 4 },
        {name: '澳门',value: 7 }
    ];
    obj.chart=echarts.init(document.getElementById('mapChart2'));
    obj.chart.setOption({
        tooltip: {
            trigger: 'item',
            formatter:'{b}：{c}k'
        },
        visualMap: {
            type:'piecewise',
            inverse:true,
            itemGap:1,
            textStyle:{color:'#a96158'},
            pieces:[
                {min:0,max:5,label:'0-5k',color:'#ffe9e7'},
                {min:5,max:10,label:'5k-10k',color:'#ffc9c2'},
                {min:10,max:15,label:'10k-15k',color:'#ffada3'},
                {min:15,max:20,label:'15k-20k',color:'#ff9184'},
                {min:20,label:'20k+',color:'#ff5844'}
            ],
            left: 'left',
            top: 'bottom',
        },
        series: [
            {
                name: '区域统计',
                type: 'map',
                mapType: 'china',
                roam: true,
                layoutCenter: ['50%', '50%'],
                layoutSize:'120%',
                label: {
                    normal: {
                        show: false
                    },
                    emphasis: {
                        show: true

                    }
                },
                data:obj.mapChartData
            }
        ]
    });
    return obj;
})();
 */
/*
 setTimeout(function(){
 mapChartData=[
 {name: '北京',value: 17 },
 {name: '天津',value: 12 },
 {name: '上海',value: 12 },
 {name: '重庆',value: 14 },
 {name: '河北',value: 8 },
 {name: '河南',value: 1 },
 {name: '云南',value: 15 },
 {name: '辽宁',value: 5 },
 {name: '黑龙江',value: 9 },
 {name: '湖南',value: 7 },
 {name: '安徽',value: 17 },
 {name: '山东',value: 2 },
 {name: '新疆',value: 14 },
 {name: '江苏',value: 14 },
 {name: '浙江',value: 2 },
 {name: '江西',value: 7},
 {name: '湖北',value: 9 },
 {name: '广西',value: 19 },
 {name: '甘肃',value: 7 },
 {name: '山西',value: 3 },
 {name: '内蒙古',value: 5 },
 {name: '陕西',value: 7 },
 {name: '吉林',value: 8 },
 {name: '福建',value: 12 },
 {name: '贵州',value: 10 },
 {name: '广东',value: 24 },
 {name: '青海',value: 4 },
 {name: '西藏',value: 7 },
 {name: '四川',value: 2 },
 {name: '宁夏',value: 18 },
 {name: '海南',value: 14 },
 {name: '台湾',value: 16 },
 {name: '香港',value: 4 },
 {name: '澳门',value: 7 }
 ];
 ecMap.mapChart.setOption({
 series:[{
 data:mapChartData
 }]
 });
 },2000);
 */
// 学历层次统计
var ecPie2=(function(){
    var obj={};
    var pycc_grade_id = $('#pycc_grade_id').val();
    var pycc_specialty_id = $('#pycc_specialty_id').val();
    $.ajax({
        type: "GET",
        url:ctx + "/recruitmanage/signup/userPyccCount",
        data:{PYCC_GRADE_ID:pycc_grade_id,PYCC_SPECIALTY_ID:pycc_specialty_id},
        dataType:"json",
        async: true,
        success:function(datas){
            if(datas!=null){
                obj.chart.setOption({
                    legend: {
                        top:10,
                        right:10,
                        orient:'vertical',
                        data:['专科','本科']
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{b} : {c} ({d}%)"
                    },
                    series : [
                        {

                            name: '学历层次统计',
                            type: 'pie',
                            center: ['50%', '50%'],
                            label:{
                                normal:{
                                    formatter: "{b}:{c}({d}%)"
                                }
                            },
                            data:[
                                {value:datas.HIGHSPIN, name:'专科',itemStyle:{
                                    normal:{
                                        color:'#3c8dbc'
                                    }
                                }
                                },
                                {value:datas.SPECIALE_DITION, name:'本科',itemStyle:{
                                    normal:{
                                        color:'#00a65a'
                                    }
                                }
                                }
                            ],
                            itemStyle: {
                                normal:{
                                    borderWidth:1,
                                    borderColor:'#fff'
                                },
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            }
        }

    });
    obj.chart=echarts.init(document.getElementById('pieChart2'));

    $('#signUpPycc').delegate(':input','change',function(){
        var pycc_grade_id = $('#pycc_grade_id').val();
        var pycc_specialty_id = $('#pycc_specialty_id').val();
        $.ajax({
            type: "GET",
            url:ctx + "/recruitmanage/signup/userPyccCount",
            data:{PYCC_GRADE_ID:pycc_grade_id,PYCC_SPECIALTY_ID:pycc_specialty_id},
            dataType:"json",
            async: true,
            success:function(datas){
                if(datas!=null){
                    obj.chart.setOption({
                        legend: {
                            top:10,
                            right:10,
                            orient:'vertical',
                            data:['专科','本科']
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{b} : {c} ({d}%)"
                        },
                        series : [
                            {

                                name: '学历层次统计',
                                type: 'pie',
                                center: ['50%', '50%'],
                                label:{
                                    normal:{
                                        formatter: "{b}:{c}({d}%)"
                                    }
                                },
                                data:[
                                    {value:datas.HIGHSPIN, name:'专科',itemStyle:{
                                        normal:{
                                            color:'#3c8dbc'
                                        }
                                    }
                                    },
                                    {value:datas.SPECIALE_DITION, name:'本科',itemStyle:{
                                        normal:{
                                            color:'#00a65a'
                                        }
                                    }
                                    }

                                ],
                                itemStyle: {
                                    normal:{
                                        borderWidth:1,
                                        borderColor:'#fff'
                                    },
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    });
                }
            }

        });
    });

    return obj;
})();

var specialInfoChart = (function(){
    var obj = {};
    var special_grade_id = $("#special_grade_id").val();
    var special_pycc_id = $("#special_pycc_id").val();
    $.ajax({
        type: "GET",
        url:ctx + "/recruitmanage/signup/userCountBySpecial",
        data:{SPECIAL_GRADE_ID:special_grade_id,SPECIAL_PYCC_ID:special_pycc_id},
        dataType:"json",
        async: true,
        success:function(datas){
            if(datas!=null){
                //var info = $("#specialInfo");
                $("#specialInfoChart").empty();
                var html = "";
                html +="<dl class='spe-list'>";
                for(var i=0;i<datas.SPECIALLIST.length;i++){
                    html +="<dt class='text-no-bold f12'>"+datas.SPECIALLIST[i].ZYMC+"</dt>";
                    html +="<dd class='margin_t5'>";
                    html +="<div class='progress'>";
                    var s = "width: "+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%";
                    html +="<div class='progress-bar progress-bar-green' style='"+s+"'>";
                    html +="<span>"+datas.SPECIALLIST[i].ZYMC_COUNT+"("+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%)"+"</span>";
                    html +="</div>";
                    html +="</div>";
                    html +="</dd>";
                }
                html += "</dl>";
                $("#specialInfoChart").append(html);
            }else{
                $("#specialInfoChart").append("暂无数据！");
            }
        }
    });
    obj.chart=echarts.init(document.getElementById('specialInfoChart'));

    $("#specialInfo").delegate(':input','change',function(){
        var special_grade_id = $("#special_grade_id").val();
        var special_pycc_id = $("#special_pycc_id").val();
        $.ajax({
            type: "GET",
            url:ctx + "/recruitmanage/signup/userCountBySpecial",
            data:{SPECIAL_GRADE_ID:special_grade_id,SPECIAL_PYCC_ID:special_pycc_id},
            dataType:"json",
            async: true,
            success:function(datas){
                if(datas!=null){
                    //var info = $("#specialInfo");
                    $("#specialInfoChart").empty();
                    var html = "";
                    html +="<dl class='spe-list'>";
                    for(var i=0;i<datas.SPECIALLIST.length;i++){
                        html +="<dt class='text-no-bold f12'>"+datas.SPECIALLIST[i].ZYMC+"</dt>";
                        html +="<dd class='margin_t5'>";
                        html +="<div class='progress'>";
                        var s = "width: "+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%";
                        html +="<div class='progress-bar progress-bar-green' style='"+s+"'>";
                        html +="<span>"+datas.SPECIALLIST[i].ZYMC_COUNT+"("+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%)"+"</span>";
                        html +="</div>";
                        html +="</div>";
                        html +="</dd>";
                    }
                    html += "</dl>";
                    $("#specialInfoChart").append(html);
                }else{
                    $("#specialInfoChart").append("暂无数据！");
                }
            }
        });
    });


    return obj;
})();

//报读资料统计
var ecBar4=(function(){
    var obj={};
    obj.chart=echarts.init(document.getElementById('pieChart'));
    obj.chart.setOption({
        tooltip: {
            trigger: 'item',
            formatter: "{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            right: '20%',
            top:'center',
            data:['退学','休学']
        },
        series: [
            {
                name:'学员考勤',
                type:'pie',
                center:['30%', '50%'],
                radius: ['55%', '80%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center',
                        formatter: "{c}"

                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            //color: '#666666',
                            fontSize:14,
                            fontWeight:'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                itemStyle: {
                    normal:{
                        borderWidth:1,
                        borderColor:'#fff'
                    }
                },
                data:[]
            }
        ]
    });

    $('#signupInfo').delegate(':input', 'change', function () {
        // $(".sigunpinfo").empty();
        var gradeId=$('#gradeId').val();
        var pycc=$('#pycc').val();
        var signupSpecialtyId=$('#signupSpecialtyId').val();
        $.ajax({
            type: "GET",
            url:ctx + "/recruitmanage/signup/studentRollSituation",
            data:{gradeId:gradeId,pycc:pycc,specialtyId:signupSpecialtyId},
            dataType: "json",
            async: true,	// 是否异步
            success:function(datas){
                var studentRollSituation;
                if(datas.successful) {
                    studentRollSituation = datas.obj.studentRollSituation;
                }
                var data = [
                    {
                        value: studentRollSituation.LEAVESCHOOLNUM,
                        name: '退学',
                        label: {
                            normal: {
                                textStyle: {
                                    color: '#666'
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#d2d6de'
                            }
                        }
                    },
                    {
                        value: studentRollSituation.TEMPORARYSCHOOLNUM,
                        name: '休学',
                        itemStyle: {
                            normal: {
                                color: '#f56954'
                            }
                        }
                    }
                ];
                obj.chart.hideLoading();
                obj.chart.setOption({
                    series: [
                        {
                            data: data
                        }
                    ]
                });
                $('#alreadyRegNum').text(studentRollSituation.ALREADYREGNUM);
                $('#notRegNum').text(studentRollSituation.NOTREGNUM);
                $('#perfectSignupNum').text(studentRollSituation.PERFECTSIGNUPNUM);
                $('#notPerfectSignupNum').text(studentRollSituation.NOTPERFECTSIGNUPNUM);
            }
        });
    });
    $('#signupInfo :input:eq(0)').trigger('change');
    return obj;
})();

$(window).bind("resize",function(){
    ecBar.chart.resize();
    ecPie2.chart.resize();
    // ecMap.chart.resize();
    ecMap2.chart.resize();
    ecBar4.chart.resize();
    specialInfoChart.chart.resize();
});

function showStudyCenter(str){
    var obj = {};
    obj.chart=echarts.init(document.getElementById('mapChart1'));

    obj.chart.convertData = function (data) {
        var res = [];
        for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
                res.push({
                    name: data[i].name,
                    value: geoCoord.concat(data[i].value)
                });
            }
        }
        return res;
    };
    obj.chart.setOption({
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                return params.name + ' : ' + params.value[2];
            }
        },

        geo: {
            map: 'china',
            zoom:1.2,
            roam: true,
            label: {
                emphasis: {
                    show: true
                }
            },
            itemStyle:{
                normal:{
                    color:'#ffebe9'
                },
                emphasis:{
                    color:'#ff5945'
                }
            }
        },
        series: [
            {
                name: '学习中心统计',
                type: 'scatter',
                coordinateSystem: 'geo',
                data: [],
                symbol:'pin',
                symbolSize: 20,
                label: {
                    normal: {
                        show: false
                    },
                    emphasis: {
                        show: false
                    }
                },
                itemStyle: {
                    emphasis: {
                        borderColor: '#fff',
                        borderWidth: 1
                    }
                }
            }
        ]
    });
    obj.chart.hideLoading();
    setTimeout(function () {
        obj.chart.setOption({
            series : [
                {
                    data:obj.chart.convertData(str)
                }
            ]
        });
    },1000);
}

function changType(type) {
    if (type=="1") {
        window.location.href = "list";
    } else {
        window.location.href = "statistics";
    }
}