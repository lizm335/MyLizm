<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body" style="padding-bottom: 45px">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">教材管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<p><h3 class="box-title pad-t5">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;级</h3>：${gjtGrade.gradeName }</p>
			<h3 class="box-title pad-t5">教材列表</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		              	<th><input type="checkbox" id="sel_all"/></th>
		              	<td>序号</td>
		                <th>书号</th>
		                <th>教材名称</th>
		                <th>使用课程</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty gjtTextbooks}">
								<c:forEach items="${gjtTextbooks}" var="entity" varStatus="i">
						            	<tr>
						            		<td><input type="checkbox" class="tb_ck" data-id="${entity.textbookId}" /></td>
						            		<td>${i.count }</td>
						            		<td>
						            			${entity.textbookCode}
						            		</td>
						            		<td>
						            			${entity.textbookName}
						            		</td>
						            		<td>
						            			<c:if test="${not empty entity.gjtCourseList}">
													<c:forEach  items="${entity.gjtCourseList}" var="item" varStatus="status">
														${item.kcmc}
														<span class="course gray9">(${item.kch })</span><br />
													</c:forEach>
												</c:if>
						            		</td>
						            	</tr>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="11">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			</div>
		</div>
	</div>
	</form>
</section>
<section>
	<div class="text-right pop-btn-box pad">
		<span class="pull-left pad-t5">
				已选 <b class="text-light-blue inline-block selected-course" width="">0</b>本教材 
				共  <b class="text-light-blue inline-block selected-textbook">0</b> 门课程
		</span>
		<button type="button" class="btn btn-default min-width-90px" data-role="back-off">取消</button>
		<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
	</div>
</section>




<script type="text/javascript">

$(function(){
    $('body').off('click','tbody tr');
   
    $('#sel_all').change(function(){
    	$('.tb_ck').prop('checked',$(this).is(':checked'));
    	countNum();
    	
    });

    $('.tb_ck').change(function(){
        countNum();
    });

    $('[data-role="save-data"]').click(function(){
		var textbookIds=[];
		$('.tb_ck:checked').each(function(){
		    textbookIds.push($(this).data('id'));
		});
		if(textbookIds.length==0){
			alert('请选择教材');
			return false;
		}
		var postIngIframe = $.formOperTipsDialog({
			text : '数据提交中...',
			iconClass : 'fa-refresh fa-spin'
	    });
		$.post('${ctx}/textbookArrange/saveArrangeTextbooks',{
			gradeId:'${param.gradeId}',
			textbookIds:textbookIds
		},function(data){
			if(data.successful){
			    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
				postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
				location.href='${ctx}/textbookArrange/list';
			}else{
				alert(data.message);
				 $.closeDialog(postIngIframe);
			}
		},'json');
	});

	
});
    
function countNum(){
    $('.selected-course').text($('.tb_ck:checked').length);
    var i=0;
    $('.tb_ck:checked').each(function(){
		i+=$(this).closest('tr').find('.course').length;
	 });
    $('.selected-textbook').text(i);
}
</script>
</body>
</html>
