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
  <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="#">教务管理</a></li>
    <li><a href="#">缴费管理</a></li>
    <li class="active">缴费详情</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">个人信息</span>
          </h3>
        </div>
        <div id="info-box-1" class="collapse in">
          <div class="panel-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th">
                  <tr>
                    <th>
                      姓名
                    </th>
                    <td>
                      李浩强
                    </td>
                    <th>
                      学号
                    </th>
                    <td>
                      1444101207307
                    </td>
                  </tr>
                  
                  <tr>
                    <th>
                      身份证
                    </th>
                    <td>
                      430626198805011456
                    </td>
                    <th>
                      手机
                    </th>
                    <td>
                      18956417789
                    </td>
                  </tr>
                  <tr>
                    <th>
                      邮箱
                    </th>
                    <td>
                      18956417789@189.com
                    </td>
                    <th>
                      单位名称
                    </th>
                    <td>
                      广州远程教育中心有限公司
                    </td>
                  </tr>
                  <tr>
                    <th>
                      联系地址
                    </th>
                    <td colspan="3">
                      广州市越秀区麓景西路41号广州广播电视大学6号楼5楼
                    </td>
                  </tr>
                </table>
              </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">报读明细</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th">
                  <tr>
                    <th>
                      院校
                    </th>
                    <td>
                      国家开放大学（广州）实验学院
                    </td>
                    <th>
                      年级
                    </th>
                    <td>
                      2015春
                    </td>
                  </tr>
                  <tr>
                    <th>
                      层次
                    </th>
                    <td>
                      高起专
                    </td>
                    <th>
                      专业
                    </th>
                    <td>
                      计算机网络技术（网络管理）
                    </td>
                  </tr>
                  <tr>
                    <th>
                      学习中心
                    </th>
                    <td>
                      东软学院学习中心
                    </td>
                    <th>
                      入学时间
                    </th>
                    <td>
                      2015年1月10日
                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">缴费明细</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">
          <div class="panel-body">
            <div class="cnt-box-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th">
                  <tr>
                    <th>
                      全额学费
                    </th>
                    <td>
                      ￥6400
                    </td>
                    <th>
                      优惠金额
                    </th>
                    <td>
                      ￥700
                    </td>
                  </tr>
                  <tr>
                    <th>
                      优惠政策
                    </th>
                    <td>
                      报读国开学历（高起专）立减700元
                    </td>
                    <th>
                      已缴学费
                    </th>
                    <td>
                      ￥5300
                    </td>
                  </tr>
                  <tr>
                    <th>
                      已缴期数
                    </th>
                    <td>
                      2/30
                    </td>
                    <th>
                      缴费方式
                    </th>
                    <td>
                      分期缴费
                    </td>
                  </tr>
                  <tr>
                    <th>
                      缴费状态
                    </th>
                    <td colspan="3" class="text-red">
                      已欠费
                    </td>
                  </tr>
                </table>
              </div>
            </div>
            <div class="pad margin_t10">
              <div class="table-responsive">
                <table class="table table-bordered table-striped table-gray-th text-center table-font">
                  <thead>
                    <tr>
                      <th>缴费月度</th>
                      <th>应缴金额</th>
                      <th>已缴金额</th>
                      <th>缴费状态</th>
                      <th>缴费方式</th>
                      <th>缴费日期</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        2016-04
                      </td>
                      <td>
                        ￥300
                      </td>
                      <td>
                        0
                      </td>
                      <td class="text-red">
                        已欠费
                      </td>
                      <td>
                        
                      </td>
                      <td>
                        
                      </td>
                    </tr>
                    <tr>
                      <td>
                        2016-03
                      </td>
                      <td>
                        ￥300
                      </td>
                      <td>
                        ￥300
                      </td>
                      <td>
                        已缴清
                      </td>
                      <td>
                        微信支付
                      </td>
                      <td>
                        2016-03-01
                      </td>
                    </tr>
                    <tr>
                      <td>
                        2016-02
                      </td>
                      <td>
                        ￥300
                      </td>
                      <td>
                        ￥300
                      </td>
                      <td>
                        已缴清
                      </td>
                      <td>
                        微信支付
                      </td>
                      <td>
                        2016-02-01
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
  </div>
</section>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
