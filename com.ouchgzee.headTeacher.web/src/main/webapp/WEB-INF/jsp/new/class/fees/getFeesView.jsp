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
  <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" style="margin-top: 2px;" data-role="back-off">返回</button>
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
    <li>缴费管理</li>
    <li class="active">缴费详情</li>
  </ol>
</section>
<section class="content">
  <div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="${studentMap.ZP }" class="img-circle" style="width:112px;height:112px;" alt="User Image">
        </div>
        <div class="media-body">
          <h3 class="margin_t10"> ${studentMap.XM }
            <small class="f14">(<c:if test="${studentMap.XBM eq '2' }">女</c:if><c:if test="${studentMap.XBM ne '2' }">男</c:if>)</small>
          </h3>
          <div class="row">
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>学号:</b> <span>${studentMap.XH }</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>手机:</b>
              <span>${studentMap.SJH }</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>邮箱:</b> <span>${studentMap.DZXX }</span>
            </div>
          </div>
        </div>
      </div>
      
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">--</div>
          <div class="text-center gray6">缴费方式</div>
        </div>
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">--</div>
          <div class="text-center gray6">缴费状态</div>
        </div>
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">
          	<c:if test="${empty studentMap.LEARNING_STATE || studentMap.LEARNING_STATE=='1'}">
            	正常学习
            </c:if>
            <c:if test="${studentMap.LEARNING_STATE=='0'}">
            	正常学习
            </c:if>
          </div>
          <div class="text-center gray6">学习状态</div>
        </div>
        <div class="col-sm-3 col-xs-6 text-center">
          <button class="btn btn-default btn-sm margin_t10">
            <i class="fa fa-close f14 vertical-middle"></i> 关闭学习
          </button>
        </div>
      </div>
    </div>
  </div>

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
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-block table-col-4">
                  <tr class="table-row">
                    <td class="table-cell-block">姓名</td>
                    <td class="table-cell-block even"> ${studentMap.XM }</td>
                    <td class="table-cell-block">学号</td>
                    <td class="table-cell-block even">${studentMap.XH }</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">身份证</td>
                    <td class="table-cell-block even">${studentMap.SFZH }</td>
                    <td class="table-cell-block">手机</td>
                    <td class="table-cell-block even">${studentMap.SJH }</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">邮箱</td>
                    <td class="table-cell-block even">${studentMap.DZXX }</td>
                    <td class="table-cell-block">单位名称</td>
                    <td class="table-cell-block even">${studentMap.SC_CO }</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">联系地址</td>
                    <td class="table-cell-block even" colspan="3">${studentMap.TXDZ }</td>
                  </tr>
                </table>
              </div>
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
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="table-responsive margin-bottom-none">
                  <table class="table-block table-col-4">
                    <tr class="table-row">
                      <td class="table-cell-block">院校</td>
                      <td class="table-cell-block even">${studentMap.XXMC }</td>
                      <td class="table-cell-block">年级</td>
                      <td class="table-cell-block even">${studentMap.GRADE_NAME }</td>
                    </tr>
                    <tr class="table-row">
                      <td class="table-cell-block"> 层次</td>
                      <td class="table-cell-block even">${studentMap.PYCC_NAME }</td>
                      <td class="table-cell-block"> 专业</td>
                      <td class="table-cell-block even">${studentMap.ZYMC }</td>
                    </tr>
                    <tr class="table-row">
                      <td class="table-cell-block">学习中心</td>
                      <td class="table-cell-block even">${studentMap.SC_NAME }</td>
                      <td class="table-cell-block">入学时间</td>
                      <td class="table-cell-block even">--</td>
                    </tr>
                  </table>
                </div>
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
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-block table-col-4">
                  <tr class="table-row">
                    <td class="table-cell-block">全额学费</td>
                    <td class="table-cell-block even">--</td>
                    <td class="table-cell-block">优惠金额</td>
                    <td class="table-cell-block even">--</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">优惠政策</td>
                    <td class="table-cell-block even">--</td>
                    <td class="table-cell-block"> 已缴学费</td>
                    <td class="table-cell-block even">--</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">已缴期数</td>
                    <td class="table-cell-block even">--</td>
                    <td class="table-cell-block">缴费方式</td>
                    <td class="table-cell-block even">--</td>
                  </tr>
                  <tr class="table-row">
                    <td class="table-cell-block">缴费状态</td>
                    <td class="table-cell-block even text-red" colspan="3">--</td>
                  </tr>
                </table>
              </div>
            </div>
            <div class="pad margin_t10">
              <div class="table-responsive">
                <table class="table table-bordered table-striped table-cell-ver-mid">
                  <thead>
                    <tr>
                      <th class="text-center">缴费月度</th>
                      <th class="text-center">应缴金额</th>
                      <th class="text-center">已缴金额</th>
                      <th class="text-center">缴费状态</th>
                      <th class="text-center">缴费方式</th>
                      <th class="text-center">缴费日期</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td class="text-center">--</td>
                      <td class="text-center">--</td>
                      <td class="text-center">--</td>
                      <td class="text-center">--</td>
                      <td class="text-center">--</td>
                      <td class="text-center">--</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

</script>
</body>
</html>