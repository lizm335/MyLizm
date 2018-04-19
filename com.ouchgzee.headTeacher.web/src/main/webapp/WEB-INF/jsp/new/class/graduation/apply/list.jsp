<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
<section class="content-header">
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="#">毕业管理</a></li>
    <li class="active">毕业管理</li>
  </ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	  <div class="box">
	      <div class="box-body">
	          <div class="row pad-t15">
	            <div class="col-sm-4 col-xs-6">
	              <div class="form-group">
	                <label class="control-label col-sm-3 text-nowrap">姓名</label>
	                <div class="col-sm-9">
	                  <input class="form-control" type="text" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
	                </div>
	              </div>
	            </div>
	            <div class="col-sm-4 col-xs-6">
	              <div class="form-group">
	                <label class="control-label col-sm-3 text-nowrap">学号</label>
	                <div class="col-sm-9">
	                  <input class="form-control" type="text" name="search_LIKE_xh" value="${param.search_LIKE_xh}">
	                </div>
	              </div>
	            </div>
	
	            <div class="col-sm-4 col-xs-6">
	              <div class="form-group">
	                <label class="control-label col-sm-3 text-nowrap">专业</label>
	                <div class="col-sm-9">
	                  <select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected='selected'>请选择</option>
							<c:forEach items="${specialtyMap}" var="specialty">
								<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_specialtyId']}">selected='selected'</c:if>>${specialty.value}</option>
							</c:forEach>
						</select>
	                </div>
	              </div>
	            </div>
	            <div class="col-sm-4 col-xs-6">
	              <div class="form-group">
	                <label class="control-label col-sm-3 text-nowrap">年级</label>
	                <div class="col-sm-9">
	                  <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected='selected'>请选择</option>
							<c:forEach items="${gradeMap}" var="grade">
								<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${grade.value}</option>
							</c:forEach>
						</select>
	                </div>
	              </div>
	            </div>
	            <div class="col-sm-4 col-xs-6">
	              <div class="form-group">
	                <label class="control-label col-sm-3 text-nowrap">层次</label>
	                <div class="col-sm-9">
	                  <select name="search_EQ_pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected='selected'>请选择</option>
							<c:forEach items="${trainingLevelMap}" var="trainingLevel">
								<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
							</c:forEach>
						</select>
	                </div>
	              </div>
	            </div>
				<div class="col-md-4 col-xs-6">
					<div class="form-group">
						<label class="control-label col-sm-3 text-nowrap">申请学位</label>
						<div class="col-sm-9">
							<select id="applyDegree" name="search_EQ_applyDegree" class="selectpicker form-control">
								<option value="" selected="selected">请选择</option>
								<option value="0" >否</option>
								<option value="1" >是</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-xs-6">
					<div class="form-group">
						<label class="control-label col-sm-3 text-nowrap">毕业论文状态</label>
						<div class="col-sm-9">
							<select id="status1" name="search_EQ_status1" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${thesisStatusMap}" var="thesisStatus">
									<option value="${thesisStatus.key}">${thesisStatus.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-xs-6">
					<div class="form-group">
						<label class="control-label col-sm-3 text-nowrap">社会实践状态</label>
						<div class="col-sm-9">
							<select id="status2" name="search_EQ_status2" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${practiceStatusMap}" var="practiceStatus">
									<option value="${practiceStatus.key}">${practiceStatus.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-4 col-xs-6">
					<div class="form-group">
						<label class="control-label col-sm-3 text-nowrap">论文终稿确认</label>
						<div class="col-sm-9">
							<select id="isConfirm" name="search_EQ_isConfirm" class="selectpicker form-control">
								<option value="" selected="selected">请选择</option>
								<option value="0" >待确认</option>
								<option value="1" >已确认</option>
							</select>
						</div>
					</div>
				</div>
	          </div>
	      </div><!-- /.box-body -->
	      <div class="box-footer">
	        <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
	        <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	      </div><!-- /.box-footer-->
	  </div>
	  
	  <div class="box margin-bottom-none">
	    <div class="box-header with-border">
	      <h3 class="box-title pad-t5">毕业列表</h3>
	      <div class="pull-right no-margin">
	        <a href="javascript:exportInfo()" class="btn btn-default btn-sm"><i class="fa fa-fw fa-sign-out"></i> 导出毕业详情</a>
	      </div>
	    </div>
	    <div class="box-body">
	      <div class="filter-tabs clearfix">
	        <ul class="list-unstyled">
	          <li <c:if test="${empty param['search_EQ_isConfirm']}">class="actived"</c:if>>全部(${isConfirm + notConfirm})</li>
			  <li <c:if test="${param['search_EQ_isConfirm'] == 0}">class="actived"</c:if>>待确认论文终稿(${notConfirm})</li>
			  <li <c:if test="${param['search_EQ_isConfirm'] == 1}">class="actived"</c:if>>已确认论文终稿(${isConfirm})</li>
	        </ul>
	      </div>
	      <div class="table-responsive">
	        <table class="table table-bordered table-striped table-cell-ver-mid text-center">
	          <thead>
	            <tr>
	              <th>个人信息</th>
	              <th>报读信息</th>
	              <th>学位意向</th>
	              <th>论文状态</th>
	              <th width="6%">论文终稿确认</th>
	              <th width="6%">论文初评成绩</th>
	              <th width="6%">论文答辩成绩</th>
	              <th width="6%">社会实践状态</th>
	              <th width="6%">社会实践成绩</th>
	              <th width="7%">操作</th>
	            </tr>
	          </thead>
	          <tbody>
				<c:choose>
					<c:when test="${not empty pageInfo.content}">
						<c:forEach items="${pageInfo.content}" var="entity">
							<c:if test="${not empty entity}">
					            <tr>
					              <td>
					                <div class="text-left">
								                  姓名：${entity.studentName}<br>
								                  学号：${entity.studentCode}<br>
								                  手机：${entity.phone}
					                </div>
					              </td>
					              <td>
					                <div class="text-left">
								                  层次：${entity.trainingLevel}<br>
								                  年级：${entity.grade}<br>
								                  专业：${entity.specialtyName}
					                </div>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.applyId1}">
							              		<c:choose>
							              			<c:when test="${entity.applyDegree1 == 1}">申请学位</c:when>
							              			<c:otherwise>不申请学位</c:otherwise>
							              		</c:choose>
											</c:when>
											<c:otherwise>
												--
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.applyId1}">
												<c:forEach items="${thesisStatusMap}" var="thesisStatus">
													<c:if test="${thesisStatus.key == entity.status1 }">${thesisStatus.value}</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												未申请
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.applyId1}">
							              		<c:choose>
							              			<c:when test="${entity.status1 > 6}">已确认</c:when>
							              			<c:otherwise>待确认</c:otherwise>
							              		</c:choose>
											</c:when>
											<c:otherwise>
												--
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.reviewScore1}">
							              		${entity.reviewScore1}
											</c:when>
											<c:otherwise>
												--
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.defenceScore1}">
							              		${entity.defenceScore1}
											</c:when>
											<c:otherwise>
												--
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.applyId2}">
												<c:forEach items="${practiceStatusMap}" var="practiceStatus">
													<c:if test="${practiceStatus.key == entity.status2 }">${practiceStatus.value}</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												未申请
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                	<c:choose>
											<c:when test="${not empty entity.reviewScore2}">
							              		${entity.reviewScore2}
											</c:when>
											<c:otherwise>
												--
											</c:otherwise>
										</c:choose>
					              </td>
					              <td>
					                <c:if test="${entity.status1 == 6}">
					                	<a href="#" class="operion-item" data-toggle="tooltip" title="确认论文终稿" data-role="sure-btn-1"><i class="fa fa-fw fa-submit-sth"></i></a>
					                	<input type="hidden" id="applyId1" value="${entity.applyId1}">
									</c:if>
									<c:choose>
										<c:when test="${not empty entity.batchId1}">
											<a href="view?studentId=${entity.studentId}&batchId=${entity.batchId1}"
												class="operion-item" data-toggle="tooltip" title="查看详情">
												<i class="fa fa-fw fa-view-more"></i></a> 
										</c:when>
										<c:otherwise>
											<a href="view?studentId=${entity.studentId}&batchId=${entity.batchId2}"
												class="operion-item" data-toggle="tooltip" title="查看详情">
												<i class="fa fa-fw fa-view-more"></i></a> 
										</c:otherwise>
									</c:choose>
					              </td>
					            </tr>
	            			</c:if>
	            		</c:forEach>
	            	</c:when>
					<c:otherwise>
						<tr>
							<td align="center" colspan="10">暂无数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
	          </tbody>
	        </table>
	        <tags:pagination page="${pageInfo}" paginationSize="5" />
	      </div>
	    </div>
	  </div>
  </form>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

$(function() {
    var value1='${param.search_EQ_status1}';
    $('#status1').selectpicker();
    $('#status1').selectpicker('val', value1);
    
    var value2='${param.search_EQ_status2}';
    $('#status2').selectpicker();
    $('#status2').selectpicker('val', value2);
    
    var value3='${param.search_EQ_isConfirm}';
    $('#isConfirm').selectpicker();
    $('#isConfirm').selectpicker('val', value3);
    
    var value4='${param.search_EQ_applyDegree}';
    $('#applyDegree').selectpicker();
    $('#applyDegree').selectpicker('val', value4);
})

// filter tabs
$(".filter-tabs li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "list";
		} else if (el == $li.context && index == 1) {
			window.location.href = "list?search_EQ_isConfirm=0";
		} else if (el == $li.context && index == 2) {
			window.location.href = "list?search_EQ_isConfirm=1";
		}
	});
});

function exportInfo() {
	window.location.href='${ctx}/home/class/graduation/apply/export?' + $('#listForm').serialize();
}

// 确认论文
$("body").confirmation({
  selector: "[data-role='sure-btn-1']",
  html:true,
  placement:'top',
  content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>是否确认收到论文终稿？</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:240,
  onShow:function(event,element){
    
  },
  onConfirm:function(event,element){
	var applyId = $(element).closest("td").find("#applyId1").val();
	$.post("confirm",{applyId:applyId},function(data){
   		if(data.successful){
   			showMessage(data);
   			window.location.reload();
   		}else{
   			showMessage(data);
   		}
   },"json");
  },
  onCancel:function(event, element){
    
  }
});
</script>
</body>
</html>
