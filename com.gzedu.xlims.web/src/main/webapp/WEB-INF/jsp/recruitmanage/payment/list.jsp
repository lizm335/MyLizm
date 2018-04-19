<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>缴费管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
    <li class="active">缴费管理</li>
  </ol>
</section>
<section class="content">
  <div class="nav-tabs-custom no-margin">
    <ul class="nav nav-tabs nav-tabs-lg">
      <li class="active"><a href="#tab_top_1" data-toggle="tab">缴费管理</a></li>
      <li><a href="#tab_top_2" data-toggle="tab">缴费统计</a></li>
    </ul>
    <div class="tab-content">
      <div class="tab-pane active" id="tab_top_1">
      <form class="form-horizontal" id="listForm" action="list.html">
        <div class="box box-border">
          <div class="box-body">
            <form class="form-horizontal">
              <div class="row reset-form-horizontal pad-t15">
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">学号</label>
                    <div class="col-sm-9">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">姓名</label>
                    <div class="col-sm-9">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">专业</label>
                    <div class="col-sm-9">
                      <select class="form-control" >
                        <option value="">全部</option>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">年级</label>
                    <div class="col-sm-9">
                      <select class="form-control" >
                        <option value="">全部</option>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">层次</label>
                    <div class="col-sm-9">
                      <select class="form-control" >
                        <option value="">全部</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div> 
          <div class="box-footer">
            <div class="pull-right"><button type="button" class="btn btn-default">重置</button></div>
            <div class="pull-right margin_r15"><button type="button" class="btn min-width-90px btn-primary">搜索</button></div>
          </div>
        </div>
        <div class="box box-border margin-bottom-none">
          <div class="box-header with-border">
            <div class="fr">
              <button class="btn btn-default btn-outport btn-sm margin_r10"><i class="fa fa-fw fa-sign-out"></i> 导出待确认名单</button>
              <button class="btn btn-default btn-sm btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出欠费名单</button>
            </div>
            <h3 class="box-title pad-t5">缴费列表</h3>
          </div>
          <div class="box-body">
            <div class="filter-tabs clearfix">
              <ul class="list-unstyled">
                <li class="actived">已缴清(131)</li>
                <li>已欠费(40)</li>
                <li>待确认(40)</li>
              </ul>
            </div>
            <div class="table-responsive">
              <table class="table table-bordered table-striped vertical-mid table-font">
                <thead>
                  <tr>
                    <th class="text-center">个人信息</th>
                    <th class="text-center">报读信息</th>
                    <th class="text-center" width="100">报名时间</th>
                    <th class="text-center text-nowrap">全额学费</th>
                    <th class="text-center text-nowrap">优惠金额</th>
                    <th class="text-center text-nowrap">应交学费</th>
                    <th class="text-center text-nowrap">已缴学费</th>
                    <th class="text-center text-nowrap">已交期数</th>
                    <th class="no-padding" data-role="menu-th">
                      <div class="dropdown custom-dropdown">
                        <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
                          缴费方式
                          <span class="caret"></span>
                        </a>
                        <div class="dropdown-menu" style="width:160px;">
                          <ul class="list-unstyled">
                            <li>
                              <i class="fa fa-fw"></i> 分学年缴费（1300）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 分期缴费（760）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 全额缴费（520）
                              <input type="checkbox">
                            </li>
                          </ul>
                          <div class="text-center margin_t5">
                            <button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
                            <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                          </div>
                        </div>
                        
                      </div>
                    </th>
                    <th class="no-padding" data-role="menu-th">
                      <div class="dropdown custom-dropdown">
                        <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
                        	  缴费状态
                          <span class="caret"></span>
                        </a>
                        <div class="dropdown-menu">
                          <ul class="list-unstyled">
                            <li>
                              <i class="fa fa-fw"></i> 已缴清（1208）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 已欠费（21）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 待确认（19）
                              <input type="checkbox">
                            </li>
                          </ul>
                          <div class="text-center margin_t5">
                            <button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
                            <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                          </div>
                        </div>
                        
                      </div>
                    </th>
                    <th class="text-center" width="80">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>
                      姓名：朱洋波（男）<br>
                      学号：443089290<br>
                      手机：18928851128<br>
                      身份证：430626198823123211
                    </td>
                    <td>
                      层次：高起专<br>
                      年级：2015年春<br>
                      专业：计算机网络技术（网络管理）
                    </td>
                    <td>
                      2016-08-03
                    </td>
                    <td class="text-center">
                      ￥6400
                    </td>
                    <td class="text-center">
                      ￥700
                    </td>
                    <td class="text-center">
                      ￥5300
                    </td>
                    <td class="text-center">￥2600</td>
                    <td class="text-center text-light-blue">1/2</td>
                    <td class="text-center">分学年缴费</td>
                    <td class="text-center text-orange">待确认</td>
                    <td class="text-center">
                      <a href="缴费详情.html" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                    </td>
                  </tr>                                 
                </tbody>
              </table>
              <div class="page-container clearfix">
                <div class="pageing-info">
                  <div class="dataTables_info">共 2 页，到第 <input type="text" class="form-control jump-page-input" value="1"> 页 <button class="btn btn-block btn-default sure-btn">确定</button>
                  </div>
                </div>
                <div class="pageing-list">
                  <div class="dataTables_paginate paging_simple_numbers">
                    <ul class="pagination">
                      <li class="paginate_button previous disabled"><a href="#">上一页</a></li>
                      <li class="paginate_button active"><a href="#">1</a></li>
                      <li class="paginate_button"><a href="#">2</a></li>
                      <li class="paginate_button"><a href="#">3</a></li>
                      <li class="paginate_button"><a href="#">4</a></li>
                      <li class="paginate_button disabled" ><a href="#">…</a></li>
                      <li class="paginate_button"><a href="#">5</a></li>
                      <li class="paginate_button next"><a href="#">下一页</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
       </form>
      </div>
      <div class="tab-pane" id="tab_top_2">
        <div class="row">
          <div class="col-md-6">
            <!-- 缴费状态统计 -->
            <div class="panel panel-default">
              <div class="panel-heading no-pad-top">
                <div class="row">
                  <div class="col-sm-4">
                    <h3 class="panel-title text-bold text-nowrap pad-t15">缴费状态统计</h3>
                  </div>
                  <div class="col-sm-8 row-offset-3px">
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部年级</option>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部层次</option>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部专业</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-body">
                <div id="pieChart2" data-role="chart" style="height:270px"></div>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <!-- 缴费方式统计 -->
            <div class="panel panel-default">
              <div class="panel-heading no-pad-top">
                <div class="row">
                  <div class="col-sm-4">
                    <h3 class="panel-title text-bold text-nowrap pad-t15">缴费方式统计</h3>
                  </div>
                  <div class="col-sm-8 row-offset-3px">
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部年级</option>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部层次</option>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="form-control input-xs">
                          <option>全部专业</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-body">
                <div id="pieChart3" data-role="chart" style="height:270px"></div>
              </div>
            </div>
          </div>
        </div>

        <div class="panel panel-default">
          <div class="panel-heading no-pad-top">
            <div class="row">
              <div class="col-sm-4">
                <h3 class="panel-title text-bold text-nowrap pad-t15">缴费金额统计</h3>
              </div>
              <div class="col-sm-6 col-sm-offset-2">
                <div class="row-offset-3px">
                  <div class="col-sm-3">
                    <div class="form-group margin_t10 margin-bottom-none">
                      <select class="form-control input-xs">
                        <option>全部缴费年度</option>
                      </select>
                    </div>
                  </div>
                  <div class="col-sm-3">
                    <div class="form-group margin_t10 margin-bottom-none">
                      <select class="form-control input-xs">
                        <option>全部年级</option>
                      </select>
                    </div>
                  </div>
                  <div class="col-sm-3">
                    <div class="form-group margin_t10 margin-bottom-none">
                      <select class="form-control input-xs">
                        <option>全部层次</option>
                      </select>
                    </div>
                  </div>
                  <div class="col-sm-3">
                    <div class="form-group margin_t10 margin-bottom-none">
                      <select class="form-control input-xs">
                        <option>全部专业</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="panel-body">
            <div class="table-responsive">
              <table class="table table-bordered table-striped vertical-mid table-font">
                <thead>
                  <tr>
                    <th class="text-center">缴费年度</th>
                    <th class="text-center">应缴人数</th>
                    <th class="text-center">应缴金额</th>
                    <th class="text-center">优惠人数</th>
                    <th class="text-center">优惠金额</th>
                    <th class="text-center">已缴人数</th>
                    <th class="text-center">已缴金额</th>
                    <th class="text-center">缴费率</th>
                    <th class="text-center">欠费人数</th>
                    <th class="text-center">欠费金额</th>
                    <th class="text-center">欠费率</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="text-center">
                      2016
                    </td>
                    <td class="text-center">
                      910
                    </td>
                    <td class="text-center">
                      ￥2730000
                    </td>
                    <td class="text-center">
                      200
                    </td>
                    <td class="text-center">
                      ￥140000
                    </td>
                    <td class="text-center">
                      500
                    </td>
                    <td class="text-center">
                      ￥1360000
                    </td>
                    <td class="text-center">
                      55%
                    </td>
                    <td class="text-center">
                      410
                    </td>
                    <td class="text-center">
                      ￥1230000
                    </td>
                    <td class="text-center">
                      45%
                    </td>
                  </tr>
                  <tr>
                    <td class="text-center">
                      2016
                    </td>
                    <td class="text-center">
                      910
                    </td>
                    <td class="text-center">
                      ￥2730000
                    </td>
                    <td class="text-center">
                      200
                    </td>
                    <td class="text-center">
                      ￥140000
                    </td>
                    <td class="text-center">
                      500
                    </td>
                    <td class="text-center">
                      ￥1360000
                    </td>
                    <td class="text-center">
                      55%
                    </td>
                    <td class="text-center">
                      410
                    </td>
                    <td class="text-center">
                      ￥1230000
                    </td>
                    <td class="text-center">
                      45%
                    </td>
                  </tr>
                </tbody>
              </table>
              <div class="page-container clearfix">
                <div class="pageing-info">
                  <div class="dataTables_info">共 2 页，到第 <input type="text" class="form-control jump-page-input" value="1"> 页 <button class="btn btn-block btn-default sure-btn">确定</button>
                  </div>
                </div>
                <div class="pageing-list">
                  <div class="dataTables_paginate paging_simple_numbers">
                    <ul class="pagination">
                      <li class="paginate_button previous disabled"><a href="#">上一页</a></li>
                      <li class="paginate_button active"><a href="#">1</a></li>
                      <li class="paginate_button"><a href="#">2</a></li>
                      <li class="paginate_button"><a href="#">3</a></li>
                      <li class="paginate_button"><a href="#">4</a></li>
                      <li class="paginate_button disabled" ><a href="#">…</a></li>
                      <li class="paginate_button"><a href="#">5</a></li>
                      <li class="paginate_button next"><a href="#">下一页</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
<script type="text/javascript">
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

// filter tabs
$(".filter-tabs li").click(function(event) {
  if($(this).hasClass('actived')){
    $(this).removeClass('actived');
  }
  else{
    $(this).addClass('actived');
  }
});

// filter menu
$(".custom-dropdown")
.on('click', "[data-role='sure-btn']", function(event) {
  var $box=$(this).closest('.custom-dropdown');
  $(this).closest('th').removeClass('on');
  $box.find("li").removeClass('actived');
  $box.find(":checkbox").attr("checked",false);
})
.on('click', "[data-role='close-btn']", function(event) {
  $(this).closest('th').removeClass('on');
})
.on('click', 'li', function(event) {
  if($(this).hasClass('actived')){
    $(this).removeClass('actived')
    .find(":checkbox").attr("checked",false);
  }
  else{
    $(this).addClass('actived')
    .find(":checkbox").attr("checked",true);
  }
});

$('th[data-role="menu-th"]')
.on('mouseover', function(event) {
  $(this).addClass('on');
})
.on('mouseout', function(event) {
  if(!$(this).children('.custom-dropdown').hasClass('open')){
    $(this).removeClass('on');
  }
});

// 缴费状态统计
var ecPie2=(function(){
  var obj={};
  obj.chart=echarts.init(document.getElementById('pieChart2'));
  obj.chart.setOption({
      legend: {
          top:10,
          right:10,
          orient:'vertical',
          data:['已交清','待确认','已欠费']
      },
      tooltip : {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
      },
      series : [
          {

              name: '缴费状态统计',
              type: 'pie',
              center: ['50%', '50%'],
              label:{
                normal:{
                  formatter: "{b}:{c}({d}%)"
                }
              },
              data:[
                  {value:335, name:'已交清',itemStyle:{
                      normal:{
                        color:'#00a65a'
                      }
                    }
                  },
                  {value:234, name:'待确认',itemStyle:{
                      normal:{
                        color:'#f39c12'
                      }
                    }
                  },
                  {value:134, name:'已欠费',itemStyle:{
                      normal:{
                        color:'#f56954'
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
  return obj;
})();

// 缴费方式统计
var ecPie3=(function(){
  var obj={};
  obj.chart=echarts.init(document.getElementById('pieChart3'));
  obj.chart.setOption({
      legend: {
          top:10,
          right:10,
          orient:'vertical',
          data:['全额缴费','分期缴费','分学年缴费']
      },
      tooltip : {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
      },
      series : [
          {

              name: '缴费方式统计',
              type: 'pie',
              center: ['50%', '50%'],
              label:{
                normal:{
                  formatter: "{b}:{c}({d}%)"
                }
              },
              data:[
                  {value:335, name:'全额缴费',itemStyle:{
                      normal:{
                        color:'#3c8dbc'
                      }
                    }
                  },
                  {value:234, name:'分期缴费',itemStyle:{
                      normal:{
                        color:'#00a65a'
                      }
                    }
                  },
                  {value:134, name:'分学年缴费',itemStyle:{
                      normal:{
                        color:'#8b52cc'
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
  return obj;
})();

$(window).bind("resize",function(){
  ecPie2.chart.resize();
  ecPie3.chart.resize();
});
</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
