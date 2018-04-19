<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>教务班级系统-列表查询</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
			<h3 class="box-title">教务班级</h3>
		</div>
		<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/classstudent/queryTeachClass/${gjtGrade.gradeId}/${classId}" >
		<div class="box-body box-border">
		<div class="row reset-form-horizontal clearbox">				
				<div class="col-sm-6">
					<div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">学期</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" value="${gjtGrade.gradeName}" readonly="true">
						</div>	
					</div>
				</div>
				<div class="col-sm-5">
					<div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">班级名称</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="search_LIKE_bjmc" value="${param['search_LIKE_bjmc']}">
						</div>	
					</div>
				</div>
				</div>
				<div class="row reset-form-horizontal clearbox">
				<div class="col-sm-6">
					<div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">所属机构</label>
					<div class="col-sm-9">
						<select name="search_EQ_gjtOrg.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<c:forEach items="${orgMap}" var="map">
								<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtOrg.id']}">selected='selected'</c:if>>${map.value}</option>
							</c:forEach>
						</select>
					</div>
					</div>
				</div>
			</div>				
		</div>
		<div class="box-body">
			<div class="margin_b10 text-right">
				<button type="button" class="btn btn-default min-width-90px  btn-reset">重置</button>
				<button type="submit" class="btn btn-success min-width-90px margin_l15 search">搜索</button>
			</div>

			<div class="margin_t20">
				<div class="slim-Scroll" style="height: 190px; overflow: hidden;">
					<table class="table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none" style="word-break:break-all;">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" class="no-margin" data-role="select-all"></th>
								<th width="12%">学期</th>
								<th width="20%">班级</th>
								<th width="12%">班主任</th>
								<th width="30%">所属机构</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.getContent() }" var="item">
								<tr>
									<td><input type="checkbox" data-role="ck-item" value="${item.classId}" name="ids"></td>
									<td data-id="s1">${item.gjtGrade.gradeName}</td>
									<td data-id="s2" id="s2">
										${item.bjmc}
									</td>
									<c:if test="${item.gjtBzr != null}">
										<td>${item.gjtBzr.xm}</td>
									</c:if>
									<c:if test="${item.gjtBzr == null}">
										<td class="text-orange">未设置</td>
									</c:if>
									<td>
									<c:if test="${item.gjtOrg != null}">
										${item.gjtOrg.orgName}
									</c:if>	
									</td>							
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="10" />			
				</div>
			</div>
		</div>
		</form>
	</div>
	<div class="text-right pop-btn-box pad">
		<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
		<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
	</div>
	<script type="text/javascript">
		$(function() {
			$('.slim-Scroll').slimScroll({
				height : 220,
				size : '5px'
			});

			//确定选择
			$("[data-role='save-data']").click(function(event) {				
				event.preventDefault();
				var ids =[];//班级ID
				var className=[];//班级名称
				$('[data-role="ck-item"]:checked').each(function(){
					ids.push($(this).val());
					className.push($(this).parent().next().next().text().trim());					
				});
				if(ids.length>1){
					alert('只能选择一个教务班级');
					return;
				}
				$(window.parent.document).find("[data-role='select-class']").val(className);
				$(window.parent.document).find('#newClassId').val(ids);
				parent.$.closeDialog(frameElement.api);
			});
			//关闭 弹窗
			$("button[data-role='close-pop']").click(function(event) {
				parent.$.closeDialog(frameElement.api);
			});

		})
	</script>
</body>
</html>



