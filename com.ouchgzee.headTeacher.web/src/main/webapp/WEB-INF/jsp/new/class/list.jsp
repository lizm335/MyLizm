<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 切换班级</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
<div class="nav-tabs-custom no-shadow margin-bottom-none">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/home/class/list" data-toggle="tab">当前班级(${opCount})</a></li>
		<li><a href="${ctx}/home/class/closeList">已关闭班级(${clCount})</a></li>
	</ul>

	<div class="slim-Scroll">
		<div class="tab-content pad no-pad-bottom">

			<div class="tab-pane active" id="tab_1">
				<form class="form-horizontal" id="listForm" action="list.html">
					<div class="clearfix form-horizontal reset-form-horizontal margin_t10">
						<div class="col-sm-3">
							<div class="form-group">
								<label class="col-sm-4 control-label text-left2">学期</label>
								<div class="col-sm-8">
									<select name="search_EQ_gjtGrade.gradeId" class="form-control selectpicker show-tick" <%--multiple--%> data-size="10" data-live-search="true" id="grade_id">
										<option value="">- 请选择 -</option>
										<c:forEach var="item" items="${gradeMap}">
											<option value="${item.key}" <c:if test="${param['search_EQ_gjtGrade.gradeId']==item.key}">selected</c:if>>${item.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
								<label class="col-sm-4 control-label text-left2">班级名称</label>
								<div class="col-sm-8">
									<input type="text" name="search_LIKE_bjmc" class="form-control" value="${param.search_LIKE_bjmc}" placeholder="请输入班级" id="bjmc"/>
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<button type="submit" class="btn btn-primary margin_r10">搜索</button>
								<a role="button" href="${ctx}/home/class/classesConditionExportPage" data-value="" id="export_stud_condition" class="btn btn-default pull-right margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员学情</a>
								<a role="button" href="${ctx}/home/class/classesStudExportPage" data-value="" id="export_stud_info" class="btn btn-default pull-right" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员信息</a>
							</div>
						</div>
					</div>
				</form>
				<div class="table-responsive margin_t10">
					<table class="table table-bordered table-striped table-container table-cnt-center" data-role="dtable">
						<thead>
						<tr>
							<th width="50">
								<input type="checkbox" data-role="sel-all">
							</th>
							<th width="10%">学期</th>
							<th width="">班级名称</th>
							<th width="10%">班级人数</th>
							<th width="">所属机构</th>
							<th width="10%">代办事项</th>
							<%--<th width="15%">创建时间</th>--%>
							<th width="15%">操作</th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${not empty infos && infos.numberOfElements > 0}">
								<c:forEach items="${infos.content}" var="info">
									<c:if test="${not empty info}">
										<tr>
											<td><input type="checkbox" data-role="sel-item" name="class_id" value="${info.classId}"></td>
											<td>${info.gjtGrade.gradeName}</td>
											<td>${info.bjmc}</td>
											<td>${info.colStudentNum}/${info.bjrn}</td>
											<td>${info.gjtOrg.orgName}</td>
											<td>0</td>
											<%--<td><fmt:formatDate value="${info.createdDt}" type="date" /></td>--%>
											<td>
												<a href="go/${info.classId}" class="f12 text-nowrap margin_r10">进入班级</a>
												<a href="#" class="f12 text-nowrap" data-role="close-class" val="${info.classId}">关闭班级</a>
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="15">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>

					<tags:pagination page="${infos}" paginationSize="10" />
				</div>
			</div><!-- /.tab-pane -->

		</div><!-- /.tab-content -->
	</div><!-- /.slim-Scroll -->
</div>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">

    //页面高度自适应
    $(window).on('resize',function(){
        $(".scroll-box").height($(window).height()-$('.main-header').height())
    }).trigger('resize');

	$(document).ready(function () {

		$('.nav-tabs-custom').slimScroll({
			height: '525px',
			size: '5px',
			alwaysVisible:true
		});

		var objId;
		// 关闭班级
		$('#tab_1').confirmation({
			selector: "[data-role='close-class']",
			html:true,
			placement:'top',
			content:'<div class="f12 gray9 margin_b10">关闭后的班级不纳入代办事项统计</div>',
			title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">请确认是否关闭班级？</span>',
			btnOkClass    : 'btn btn-xs btn-primary',
			btnOkLabel    : '确认',
			btnOkIcon     : '',
			btnCancelClass  : 'btn btn-xs btn-default margin_l10',
			btnCancelLabel  : '取消',
			btnCancelIcon   : '',
			popContentWidth : 180,
			onShow:function(event,element){
				if($(element).attr('val') != null) {
					objId = $(element).attr('val');
				}
			},
			onConfirm:function(event,element){
				if(objId != null) {
					var id = objId;
					objId = null;

					$.post("close",{ids:id},function(data){
						if(data.successful){
							showSueccess(data);

							$(element).parents('tr').remove();
						}else{
							$.alert({
								title: '失败',
								icon: 'fa fa-exclamation-circle',
								confirmButtonClass: 'btn-primary',
								content: '班级关闭失败！',
								confirmButton: '确认',
								confirm:function(){
									showFail(data);
								}
							});
						}
					},"json");
				}
			},
			onCancel:function(event, element){

			}
		});

		// 重开班级

		$('#tab_2').confirmation({
			selector: "[data-role='re-open-class']",
			html:true,
			placement:'top',
			content:'<div class="f12 gray9 margin_b10">重开后的班级将纳入代办事项统计</div>',
			title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">请确认是否重开班级？</span>',
			btnOkClass    : 'btn btn-xs btn-primary',
			btnOkLabel    : '确认',
			btnOkIcon     : '',
			btnCancelClass  : 'btn btn-xs btn-default margin_l10',
			btnCancelLabel  : '取消',
			btnCancelIcon   : '',
			popContentWidth : 180,
			onShow:function(event,element){
				if($(element).attr('val') != null) {
					objId = $(element).attr('val');
				}
			},
			onConfirm:function(event,element){
				if(objId != null) {
					var id = objId;
					objId = null;

					$.post("reopen",{ids:id},function(data){
						if(data.successful){
							showSueccess(data);

							$(element).parents('tr').remove();
						}else{
							$.alert({
								title: '失败',
								icon: 'fa fa-exclamation-circle',
								confirmButtonClass: 'btn-primary',
								content: '班级重开失败！',
								confirmButton: '确认',
								confirm:function(){
									showFail(data);
								}
							});
						}
					},"json");
				}
			},
			onCancel:function(event, element){

			}
		});

	});


    //全选
    $('body').on('click','[data-role="sel-all"]',function(event) {
        var $cks=$(this).closest('table').find('[data-role="sel-item"]');
        if($(this).prop('checked')){
            $cks.prop('checked',true);
            $("#export_stud_condition").attr("data-value","all");
            $("#export_stud_info").attr("data-value","all");
        }
        else{
            $cks.prop('checked',false);
            $("#export_stud_condition").attr("data-value","");
            $("#export_stud_info").attr("data-value","");
        }
    });

    //导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var _this=this;
        var $cks=$('.table tbody').find('[data-role="sel-item"]:checked');
		var grade_id = $("#grade_id").val();
        var bjmc = $.trim($("#bjmc").val());

        var class_ids = "";
        if($("#export_stud_condition").attr("data-value")==="all"){
            class_ids = "all";
		}else {
        	class_ids = $cks.map(function(){return $(this).val()}).get().join(",");
		}

        if($cks.length<=0){
            $.alertDialog({
                id:'confirm',
                width:320,
                height:240,
                zIndex:11000,
                cancel:function(){//“确定”按钮的回调方法
                    $.closeDialog(this);
                },
                cancelLabel:'关闭',
                ok:false,
                content:[
                    '<div class="text-center">',
                    '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
                    '<span class="f16 inline-block vertical-mid text-left">',
                    '请选择导出班级',
                    '</span>',

                    '</div>'].join('')
            });
        }
        else{
            $.mydialog({
                id:'export',
                width:600,
                height:415,
                zIndex:11000,
                content: 'url:'+$(_this).attr('href')+"?class_ids="+class_ids+"&grade_id="+grade_id+"&bjmc="+bjmc
            });
        }
    });

</script>
</body>
</html>
