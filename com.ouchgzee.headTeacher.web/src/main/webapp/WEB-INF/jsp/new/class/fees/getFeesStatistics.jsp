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
      <li><a href="/teacher/home/class/fees/getFeeslist" >缴费管理</a></li>
      <li class="active"><a href="/teacher/home/class/fees/getFeesStatistics">缴费统计</a></li>
    </ul>
    <div class="tab-content">
      <div class="tab-pane active" id="tab_top_2">
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
                        <select class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                          <option>全部年级</option>
                          <c:forEach var="item" items="${gradeMap}">
                              <option value="${item.key}">${item.value}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <dic:selectBox name="" typeCode="TrainingLevel" code="" otherAttrs='class="form-control selectpicker show-tick" data-size="5" data-live-search="false"'/>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                          <option>全部专业</option>
                          <c:forEach var="item" items="${specialtyMap}">
                              <option value="${item.key}">${item.value}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-body" id="payfeestype_div">
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
                        <select class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                          <option>全部年级</option>
                          <c:forEach var="item" items="${gradeMap}">
                              <option value="${item.key}">${item.value}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                      	<dic:selectBox name="" typeCode="TrainingLevel" code="" otherAttrs='class="form-control selectpicker show-tick" data-size="5" data-live-search="false"'/>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="form-group margin_t10 margin-bottom-none">
                        <select class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                          <option>全部专业</option>
                          <c:forEach var="item" items="${specialtyMap}">
                              <option value="${item.key}">${item.value}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-body" id="payfeesstate_div">
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
            
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">
	function getPayFeesType() {
		$.get("/teacher/home/class/fees/getPayFeesType",function(content){
			$("#payfeestype_div").html(content);
		})
		
	}
	getPayFeesType();
	
	function getPayFeesState() {
		$.get("/teacher/home/class/fees/getPayFeesState",function(content){
			$("#payfeesstate_div").html(content);
		})
	}
	getPayFeesState();
</script>
</body>
</html>