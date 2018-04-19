<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>教材配置</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/course/choiceTextbookList">
<input type="hidden" name="courseId" value="${param.courseId }" />
<div class="box no-border no-shadow">
	<div class="box-header with-border">
      <h3 class="box-title">教材配置</h3>
    </div>
    <div class="box-body">
    	<div class="slim-Scroll">
	    	<div class="panel panel-default">
		        <div class="panel-heading">
		          <h3 class="panel-title" data-toggle="collapse" data-target="#info-box-1" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle vertical-middle"></i>
		              <span class="margin-r-5 inline-block vertical-middle">课程信息</span>
		          </h3>
		        </div>
		        <div id="info-box-1" class="collapse in">
		          <div class="panel-body">
		              	<table class="table-gray-th text-center">
			                <colgroup>
			                  <col width="13%">
			                  <col width="20%">

			                  <col width="13%">
			                  <col width="20%">

			                  <col width="13%">
			                  <col width="20%">
			                </colgroup>
			                <tbody>
			                  <tr>
			                    <th>课程代码</th>
			                    <td>${entity.kch}</td>

			                    <th>课程名称</th>
			                    <td>${entity.kcmc}</td>

			                    <th>教学方式</th>
			                    <td>${wsjxzkMap[entity.wsjxzk]}</td>
			                  </tr>
			                  <tr>
			                    <th>课程类型</th>
			                    <td>${courseCategoryMap[entity.courseCategory]} </td>

			                    <th>学分</th>
			                    <td>${entity.credit} 学分</td>

			                    <th>学时</th>
			                    <td>${entity.hour} 学时</td>
			                  </tr>
			                </tbody>
		              	</table>
		          </div>
		        </div>
		    </div>
		    <div class="panel panel-default">
		        <div class="panel-heading">
		          <h3 class="panel-title" data-toggle="collapse" data-target="#info-box-2" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle vertical-middle"></i>
		              <span class="margin-r-5 inline-block vertical-middle">教材配置信息</span>
		          </h3>
		        </div>
		        <div id="info-box-2" class="collapse in">
		          <div class="panel-body">
		              	<table class="table table-bordered text-center vertical-middle margin-bottom-none">
			                <colgroup>
			                	<col width="150" />
			                	<col width="38%" />
			                	<col width="14%" />
			                	<col width="23%" />
			                </colgroup>
			                <thead class="with-bg-gray">
			                	<tr>
			                		<th class="text-no-bold">教材图片</th>
			                		<th class="text-no-bold">教材名称</th>
			                		<th class="text-no-bold">教材所属</th>
			                		<th class="text-no-bold">教材基本信息</th>
			                		<th class="text-no-bold">操作</th>
			                	</tr>
			                </thead>
			                <tbody>
			                	<c:forEach items="${textbooks}" var="item">
			                		<tr>
				                  		<td>
				                  			<c:if test="${not empty(item.cover)}">
				                  				<img src="${item.cover }" width="120" height="70">
				                  			</c:if>
				                  			
				                  			<c:if test="${empty(item.cover)}">
				                  				<img src="${css}/common/v2/temp/demo_01.jpg" width="120" height="70">
				                  			</c:if>
				                  		</td>
				                  		<td>
				                  			${item.textbookName }
				                  		</td>
				                  		<td>学院教材</td>
				                  		<td>
				                  			<ul class="list-unstyled text-left">
				                  				<li>书号：${item.textbookCode }</li>
				                  				<li>版次：${item.revision }</li>
				                  				<li>作者：${item.author }</li>
				                  				<li>定价：￥ ${item.price }</li>
				                  			</ul>
				                  		</td>
				                  		<td>
				                  			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-id="${item.textbookId}" data-role="sure-btn-1"><i class="fa fa-fw fa-trash-o text-red"></i></a>
				                  		</td>
				                  	</tr>
			                	</c:forEach>
			                </tbody>
		              	</table>
		          </div>
		        </div>
		    </div>
		    <div class="panel panel-default">
		        <div class="panel-heading">
		          <h3 class="panel-title" data-toggle="collapse" data-target="#info-box-3" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle vertical-middle"></i>
		              <span class="margin-r-5 inline-block vertical-middle">教材列表</span>
		          </h3>
		        </div>
		        <div id="info-box-3" class="collapse in">
		          	<div class="panel-body no-padding">
		              	<div class="row border-bottom form-horizontal pad-t15 pad-r15 pad-l15">
		              		<div class="col-sm-4">
		              			<div class="form-group">
		              				<label class="control-label col-sm-3 text-nowrap text-no-bold">书号</label>
		              				<div class="col-sm-9">
						              <input type="text" name="search_LIKE_textbookCode" value="${param.search_LIKE_textbookCode }" class="form-control">
						            </div>
		              			</div>
		              		</div>
		              		<div class="col-sm-4">
		              			<div class="form-group">
		              				<label class="control-label col-sm-3 text-nowrap text-no-bold">教材名称</label>
		              				<div class="col-sm-9">
						              <input type="text" name="search_LIKE_textbookName" value="${param.search_LIKE_textbookName }" class="form-control">
						            </div>
		              			</div>
		              		</div>
		              		<div class="col-sm-4">
		              			<div class="form-group">
		              				<label class="control-label col-sm-3 text-nowrap text-no-bold">作者</label>
		              				<div class="col-sm-9">
						              <input type="text" name="search_LIKE_author" value="${param.search_LIKE_author }"  class="form-control">
						            </div>
		              			</div>
		              		</div>
		              		<div class="col-sm-4">
		              			<div class="form-group">
		              				<label class="control-label col-sm-3 text-nowrap text-no-bold">版次</label>
		              				<div class="col-sm-9">
						            	 <input type="text" name="search_LIKE_revision" value="${param.search_LIKE_revision }" class="form-control">
						            </div>
		              			</div>
		              		</div>
		              	</div>
		              	<div class="text-right pad15 border-bottom">
						    <button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						    <button type="submit" class="btn min-width-90px btn-primary margin_l10">搜索</button>
						</div>
						
		          	</div>
		          	<c:if test="${not empty(pageInfo.content)}">
		          	<div style="margin: -1px;">
						<table class="table table-bordered text-center vertical-middle margin-bottom-none" data-id="tbl">
							<colgroup>
								<col width="40" />
								<col width="140" />
								<col width="10%" />
								<col />
								<col width="10%" />
								<col width="12%" />
								<col width="10%" />
								<col width="10%" />
							</colgroup>
			                <thead class="with-bg-gray">
			                    <tr>
			                        <th class="text-no-bold">
			                            <input type="checkbox" data-id="sel-all">
			                        </th>
			                        <th class="text-no-bold">封面</th>
			                        <th class="text-no-bold">书号</th>
			                        <th class="text-no-bold">商品名称</th>
			                        <th class="text-no-bold">教材所属</th>
			                        <th class="text-no-bold">版次</th>
			                        <th class="text-no-bold">作者</th>
			                        <th class="text-no-bold">定价</th>
			                    </tr>
			                </thead>
			                <tbody>
			                	<c:forEach items="${pageInfo.content}" var="item">
			                		<tr>
				                    	<td>
				                            <input type="checkbox" name="r1" data-tid="${item.textbookId }" data-id="sel-item">
				                        </td>
				                        <td>
				                        	<c:if test="${not empty(item.cover)}">
				                  				<img src="${item.cover }" width="120" height="70">
				                  			</c:if>
				                  			
				                  			<c:if test="${empty(item.cover)}">
				                  				<img src="${css}/common/v2/temp/demo_01.jpg" width="120" height="70">
				                  			</c:if>
				                        </td>
				                        <td>${item.textbookCode }</td>
				                        <td>
				                        	${item.textbookName}
				                        </td>
				                        <td>
				                        	学院教材
				                        </td>
				                        <td>${empty(item.revision)?'--':item.revision}</td>
				                        <td>${empty(item.author)?'--':item.author }</td>
				                        <td>${empty(item.price)?'--':'￥ '.concat(item.price) }</td>
				                    </tr>
			                	</c:forEach>
			                </tbody>
			            </table>
			            <tags:pagination page="${pageInfo}" paginationSize="10" />
					</div>
					</c:if>
					<c:if test="${empty(pageInfo.content)}">
					<div class="text-center gray9 pad20">
	              		<i class="fa fa-fw fa-warning gray9" style="font-size: 80px;"></i>
	              		<div class="margin_t5 f20">
	              			没有搜索到相关教材！
	              		</div>
	              	</div>
	              	</c:if>
		        </div>
		    </div>
		</div>
    </div>
</div>
</form>
<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
  		已选择了 <b class="text-light-blue select-total">0</b> 个课程
  	</span>
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">关闭</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">添加</button>
</div>

<script type="text/javascript">
$(function(){

    $('.slim-Scroll').slimScroll({
	    height: $(window).height()-126,
	    size: '5px'
	});

	//关闭 弹窗
	$('[data-role="close-pop"]').click(function(event) {
		parent.$.closeDialog(frameElement.api);
	});

	//确定 弹窗
	$('[data-role="sure"]').click(function(event) {
		var textbookIds=[];
		$('[data-id="tbl"] tbody tr.on [data-id="sel-item"]').each(function(){
		    textbookIds.push($(this).data('tid'));
		});
		if(textbookIds.length==0){
			alert('请选择需要添加的教材');
			return false;
		}
		var postIngIframe = $.formOperTipsDialog({
			text : '数据提交中...',
			iconClass : 'fa-refresh fa-spin'
		});
		var param={
			courseId:'${param.courseId}',
			textbookIds:textbookIds
		};
		$.post('${ctx}/edumanage/course/saveCourseTextBook',param,function(data){
			if(data.successful){
			    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
				postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
				parent.location.reload();
				parent.$.closeDialog(frameElement.api);
			}else{
			    $.closeDialog(postIngIframe);
		        alert(data.message);
			}
		},'json');
	});

	//删除 教材配置信息
	$("body").confirmation({
	  selector: "[data-role='sure-btn-1']",
	  html:true,
	  placement:'top',
	  content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定移除该教材吗？</div>',
	  title:false,
	  btnOkClass    : 'btn btn-xs btn-primary',
	  btnOkLabel    : '确认',
	  btnOkIcon     : '',
	  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	  btnCancelLabel  : '取消',
	  btnCancelIcon   : '',
	  popContentWidth:190,
	  onShow:function(event,element){
	    
	  },
	  onConfirm:function(event,element){
	     $.get('${ctx}/edumanage/course/deleteCourseTextBook',{
		 		courseId:'${param.courseId}',
				textbookId:$(element).data('id')
		     },function(data){
			if(data.successful){
			    $(element).closest('tr').remove();
			}else{
		        alert(data.message);
		        location.reload();
			}
		},'json');
	  	
	  },
	  onCancel:function(event, element){
	    
	  }
	});

	 $('body').off('click','tbody tr');
	
	//单选
	$('body').on('click', '[data-id="sel-item"]', function(event) {
		var $tr=$('[data-id="tbl"] tbody tr');
		if( $(this).prop('checked') ){
			$(this).closest('tr').addClass('on');
		}
		else{
			$(this).closest('tr').removeClass('on');
		}
		$('.select-total').text($tr.filter('.on').length);
	})
	//全选
	.on('click','[data-id="sel-all"]',function(event) {
		var $tr=$('[data-id="tbl"] tbody tr');
	    if($(this).prop('checked')){
	        $('[data-id="sel-item"]').prop('checked',true);
	        $tr.addClass('on');
	    }
	    else{
	        $('[data-id="sel-item"]').prop('checked',false);
	        $tr.removeClass('on');
	    }
	    $('.select-total').text($tr.filter('.on').length);
	});
});
</script>
</script>
</body>
</html>
