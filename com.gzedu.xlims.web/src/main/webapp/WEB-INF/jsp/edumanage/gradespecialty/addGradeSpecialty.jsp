<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>添加新设专业</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
			<h3 class="box-title">添加专业</h3>
		</div>
		<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/gradespecialty/addGradeSpecialty" >
		<div class="box-body box-border">
				<input type="hidden" name="gradeId" value="${param.gradeId }" />
				<div class="pad-t15 clearfix">
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">专业规则号 </label>
							<div class="col-xs-9">
								<input class="form-control" type="text" name="search_LIKE_ruleCode" value="${param.search_LIKE_ruleCode}">
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">专业名称</label>
							<div class="col-xs-9">
								<input class="form-control" type="text" name="search_LIKE_gjtSpecialtyBase.specialtyName" value="${param['search_LIKE_gjtSpecialtyBase.specialtyName']}" placeholder="输入专业名称">
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">专业类型</label>
							<div class="col-xs-9">
								<select name="search_EQ_specialtyCategory" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${zyxzMap}" var="map">
										<option value="${map.key}" 
											<c:if test="${map.key==param['search_EQ_specialtyCategory']}">selected='selected'</c:if>>${map.value}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">培养层次</label>
							<div class="col-xs-9">
								<select name="search_EQ_pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">所属学科</label>
							<div class="col-xs-9">
								<select class="selectpicker show-tick form-control" data-size="5" data-live-search="true" name="search_EQ_zylb">
									<option value="">请选择</option>
									<c:forEach items="${xkMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param.search_EQ_zylb}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">所属行业</label>
							<div class="col-xs-9">
								<select class="selectpicker show-tick form-control" data-size="5" data-live-search="true" name="search_EQ_syhy">
									<option value="">请选择</option>
									<c:forEach items="${syhyMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param.search_EQ_syhy}">selected='selected'</c:if>>${map.value}</option>
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
								<th width="40"><input type="checkbox" class="no-margin" data-role="select-all"></th>
								<th width="12%">专业规则号</th>
								<th width="20%">专业名称</th>
								<th>专业属性</th>
								<th width="12%">最低毕业学分</th>
								<th width="9%">课程总数</th>
								<th width="9%">专业性质</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.getContent() }" var="item">
								<tr sid="${item.specialtyId}">
									<td><input type="checkbox" data-role="ck-item" value="${item.specialtyId }" name="ids"></td>
									<td data-id="s1">${item.ruleCode}</td>
									<td data-id="s2">
										${item.gjtSpecialtyBase.specialtyName}
									</td>
									<td data-id="s3" style="text-align: left">
										专业层次：${pyccMap[item.pycc]}<br>
										学科：${subjectMap[item.subject]}<br>
										学科门类：${subjectCategoryMap[item.subject][item.category]}<br>
										适用行业：${syhyMap[item.syhy]}
									</td>
									
									<td data-id="s4">${item.zdbyxf}</td>
									<td data-id="s5">${fn:length(item.gjtSpecialtyPlans)}</td>
									<td data-id="s6">
										<c:choose>
											<c:when test="${item.type == 1}">正式专业</c:when>
											<c:when test="${item.type == 2}">体验专业</c:when>
											<c:otherwise>--</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="5" />			
				</div>
			</div>
		</div>
		</form>
	</div>
	<div class="text-right pop-btn-box pad">
		<span class="pull-left pad-t5"> 已选择：<b class="text-light-blue inline-block" data-role="count">0</b>个专业
		</span>
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
				if (self !== parent) {
					var $container = parent.$('[data-role="class-box"]');
					var r = [], tmp = "";
					var html = [
							'<tr sid="{6}">',
							'<td>{0}</td>',
							'<td>{1} </td>',
							'<td style="text-align: left">{2}</td>',
							'<td>{3}</td>',
							'<td>{4}</td>',
							'<td>{5}</td>',
							'<td>',
							$(parent.$('#rowdata').html()).find('.range-temp').html(),
							'</td>',
							'<td>',
							'<a href="${ctx}/edumanage/specialty/view/{7}" class="operion-item" title="查看详情" data-tempTitle="查看详情"><i class="fa fa-fw fa-view-more"></i></a>',
							'<a href="#" class="operion-item" data-toggle="tooltip" title="删除" data-role="remove-item"><i class="fa fa-trash-o text-red"></i></a>',
							'</td>', '</tr>' ].join('');
					$(".slim-Scroll tbody tr.selected").each(
							function(index, el) {
							    tmp = html.format(
							    		$(this).find('[data-id="s1"]').text(), 
							    		$(this).find('[data-id="s2"]').html(), 
							    		$(this).find('[data-id="s3"]').html(), 
							    		$(this).find('[data-id="s4"]').text(), 
							    		$(this).find('[data-id="s5"]').text(), 
							    		$(this).find('[data-id="s6"]').text(), 
							    		$(this).attr('sid'),
							    		$(this).attr('sid')
							    		);
							    
							    r.push(tmp);
							});
					$container.append(r.join(''));
					$container.find('[name="applyRange"]').select2({
					    language : "zh-CN"
					})
				}
				parent.$.closeDialog(frameElement.api);
			});

			// 选中
			$("body").on("click", '[data-role="select-all"]', function() {
				var $box = $('.slim-Scroll');
				var $tr = $box.find('tbody tr');
				var $ck = $box.find('tbody :checkbox');

				if ($(this).is(":checked")) {
					$tr.addClass("selected");
					$ck.prop("checked", true);
					$('[data-role="count"]').html($ck.length);
				} else {
					$tr.removeClass("selected");
					$ck.prop("checked", false);
					$('[data-role="count"]').html(0);
				}
			}).on('click', '[data-role="ck-item"]', function(event) {
				var $count = $('[data-role="count"]');
				var $tr = $(this).closest('tr');
				var n = parseInt($count.text());
				if ($(this).is(":checked")) {
				//	$tr.addClass("selected");
					$count.text(n + 1);
				} else {
				//	$tr.removeClass("selected");
					$count.text(n - 1);
				}
			});

			//关闭 弹窗
			$("button[data-role='close-pop']").click(function(event) {
				parent.$.closeDialog(frameElement.api);
			});

		})
	</script>
</body>
</html>



