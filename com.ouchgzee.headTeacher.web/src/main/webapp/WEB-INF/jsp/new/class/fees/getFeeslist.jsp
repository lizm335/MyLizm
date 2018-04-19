<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 缴费管理</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
    <li class="active">缴费管理</li>
  </ol>
</section>
<section class="content">
  <div class="nav-tabs-custom no-margin">
    <ul class="nav nav-tabs nav-tabs-lg">
      <li class="active"><a href="/teacher/home/class/fees/getFeeslist" >缴费管理</a></li>
      <li><a href="/teacher/home/class/fees/getFeesStatistics">缴费统计</a></li>
    </ul>
    <div class="tab-content">
      <form class="form-horizontal" id="formList">
      <div class="tab-pane active" id="tab_top_1">
        <div class="box box-border">
          <div class="box-body">
              <div class="row reset-form-horizontal pad-t15">
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">姓名</label>
                    <div class="col-sm-9">
                      <input type="text" class="form-control" name="XM" value="${param.XM }">
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">学号</label>
                    <div class="col-sm-9">
                      <input type="text" class="form-control" name="XH" value="${param.XH }">
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">身份证</label>
                    <div class="col-sm-9">
                      <input type="text" class="form-control" name="SFZH" value="${param.SFZH }">
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">专业</label>
                    <div class="col-sm-9">
                      <select name="SPECIALTY_ID" id="specialty_id" class="form-control selectpicker show-tick" data-size="5" data-live-search="true">
                          <option value="">- 请选择 -</option>
                          <c:forEach var="item" items="${specialtyMap}">
                              <option value="${item.key}" <c:if test="${param.SPECIALTY_ID==item.key}">selected</c:if>>${item.value}</option>
                          </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">年级</label>
                    <div class="col-sm-9">
                      <select name="GRADE_ID" class="form-control selectpicker show-tick" data-size="5" data-live-search="true">
                          <option value="">- 请选择 -</option>
                          <c:forEach var="item" items="${gradeMap}">
                              <option value="${item.key}" <c:if test="${param.GRADE_ID==item.key}">selected</c:if>>${item.value}</option>
                          </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="col-sm-4 col-xs-6">
                  <div class="form-group">
                    <label class="control-label col-sm-3">层次</label>
                    <div class="col-sm-9">
                      <dic:selectBox name="PYCC" typeCode="TrainingLevel" code="${param.PYCC}" otherAttrs='class="form-control selectpicker show-tick" data-size="5" data-live-search="false"'/>
                    </div>
                  </div>
                </div>
              </div>
          </div> 
          <div class="box-footer">
            <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
            <div class="pull-right margin_r15"><button type="submit" class="btn btn-primary">搜索</button></div>
          </div>
        </div>
        <div class="box box-border margin-bottom-none">
          <div class="box-header with-border">
            <div class="fr">
              <button type="button" class="btn btn-default btn-outport btn-sm margin_r10"><i class="fa fa-fw fa-sign-out"></i> 导出待确认名单</button>
              <button type="button" class="btn btn-default btn-sm btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出欠费名单</button>
            </div>
            <h3 class="box-title pad-t5">缴费列表</h3>
          </div>
          <div class="box-body">
            <div class="filter-tabs clearfix">
              <ul class="list-unstyled">
              	<li <c:if test="${empty param.PAY_FEES_STATE  }">class="actived"</c:if> onclick="changState('')">全部(0)</li>
                <li <c:if test="${param.PAY_FEES_STATE eq '1' }">class="actived"</c:if> onclick="changState('1')">已缴清(0)</li>
                <li <c:if test="${param.PAY_FEES_STATE eq '2' }">class="actived"</c:if> onclick="changState('2')">已欠费(0)</li>
                <li <c:if test="${param.PAY_FEES_STATE eq '3' }">class="actived"</c:if> onclick="changState('3')">待确认(0)</li>
              </ul>
              <input type="hidden" name="PAY_FEES_STATE" id="pay_fees_state" value="${param.PAY_FEES_STATE }" />
            </div>
            <div class="table-responsive">
              <table class="table table-bordered table-striped table-cell-ver-mid">
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
                        <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">缴费方式
                          <span class="caret"></span>
                        </a>
                        <div class="dropdown-menu" style="width:160px;">
                          <ul class="list-unstyled">
                            <li>
                              <i class="fa fa-fw"></i> 分学年缴费（0）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 分期缴费（0）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 全额缴费（0）
                              <input type="checkbox">
                            </li>
                          </ul>
                          <div class="text-center margin_t5">
                            <button type="button" class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
                            <button type="button" class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                          </div>
                        </div>
                        
                      </div>
                    </th>
                    <th class="no-padding" data-role="menu-th">
                      <div class="dropdown custom-dropdown">
                        <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">缴费状态
                          <span class="caret"></span>
                        </a>
                        <div class="dropdown-menu">
                          <ul class="list-unstyled">
                            <li>
                              <i class="fa fa-fw"></i> 已缴清（0）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 已欠费（0）
                              <input type="checkbox">
                            </li>
                            <li>
                              <i class="fa fa-fw"></i> 待确认（0）
                              <input type="checkbox">
                            </li>
                          </ul>
                          <div class="text-center margin_t5">
                            <button type="button" class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
                            <button type="button" class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                          </div>
                        </div>
                        
                      </div>
                    </th>
                    <th class="text-center" width="80">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${infos.content}" var="info">
                  <tr>
                    <td>
				                      姓名：${info.XM }<br>
				                      学号：${info.XH }<br>
				                      手机：${info.SJH }<br>
				                      身份证：${info.SFZH }
                    </td>
                    <td>
				                      层次：${info.PYCC_NAME }<br>
				                      年级：${info.GRADE_NAME }<br>
				                      专业：${info.ZYMC }
                    </td>
                    <td>
                      	--
                    </td>
                    <td class="text-center">
                    	--
                    </td>
                    <td class="text-center">
                    	--
                    </td>
                    <td class="text-center">
                    	--  	
                    </td>
                    <td class="text-center">--</td>
                    <td class="text-center">--</td>
                    <td class="text-center">--</td>
                    <td class="text-center">--</td>
                    <td class="text-center">
                      <c:if test="${empty info.LEARNING_STATE || info.LEARNING_STATE=='1'}">
                      	<a href="javascript:;" class="operion-item" data-toggle="tooltip" title="" data-original-title="关闭学习"><i class="fa fa-fw fa-close"></i></a>
                      </c:if>
                      <c:if test="${info.LEARNING_STATE=='0'}">
                      	<a href="javascript:;" class="operion-item" data-toggle="tooltip" title="恢复学习"><i class="fa fa-fw fa-reply"></i></a>
                      </c:if>
                      <a href="/teacher/home/class/fees/getFeesView?STUDENT_ID=${info.STUDENT_ID }&ATID=${info.ATID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                    </td>
                  </tr>
                  </c:forEach>
                </tbody>
              </table>
              <tags:pagination page="${infos}" paginationSize="10" />
              
            </div>
          </div>
        </div>
      </div>
      </form>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	$(function() {
		
	})
	
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

	function changState(type) {
		$("#pay_fees_state").val(type);
		$("#formList").submit();
	}
</script>
</body>
</html>