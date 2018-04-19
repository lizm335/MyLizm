<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="listForm" class="form-horizontal">
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">选择考试科目</h3>
	</div>
	<div class="box-body">
		<div class="box-border">
			<form class="form-horizontal">
		        <div class="pad-t15 clearfix">
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">科目编号</label>
		              <div class="col-xs-9">
		                <input name="search_LIKE_subjectCode" value="${param.search_LIKE_subjectCode}" class="form-control" type="text" placeholder="科目编号">
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">科目名称</label>
		              <div class="col-xs-9">
		                <input name="search_LIKE_name" value="${param.search_LIKE_name}" type="text" class="form-control" placeholder="科目名称">
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">对应课程</label>
		              <div class="col-xs-9">
		                <input name="search_EQ_gjtCourse.kcmc" value="${param.search_EQ_gjtCourse.kcmc}" type="text" class="form-control" placeholder="对应课程">
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">试卷状态</label>
		              <div class="col-xs-9">
		                <select name="search_EQ_status" class="selectpicker show-tick form-control" 
							data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<option value="0"<c:if test="${param['search_EQ_status']==0}">selected='selected'</c:if> >未同步</option>
							<option value="1"<c:if test="${param['search_EQ_status']==1}">selected='selected'</c:if> >已同步</option>
							<option value="2"<c:if test="${param['search_EQ_status']==2}">selected='selected'</c:if> >制作中</option>
							<option value="3"<c:if test="${param['search_EQ_status']==3}">selected='selected'</c:if> >已发布</option>
						</select>
		              </div>
		            </div>
		          </div>
		        </div>
		    </form>
	    </div>
	</div>
	<div class="box-footer">
      <div class="btn-wrap">
			<button type="button" class="btn btn-default btn-reset">重置</button>
		</div>
		<div class="btn-wrap">
			<button type="submit" class="btn btn-primary">搜索</button>
		</div>
    </div>
	<div class="box-body">
		<div class="margin_b10 clearfix">
			<h3 class="cnt-box-title f16">考试科目列表</h3>			
		</div>
		
		<div class="table-responsive">
			<div class="slim-Scroll" style="height:190px;overflow:hidden;">
				<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th>选择</th>
							<th>科目编号</th>
							<th>科目名称</th>
							<th>对应课程</th>
							<th>试卷状态</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty pageInfo.content}">
					<c:forEach items="${pageInfo.content}" var="entity">
						<c:if test="${not empty entity}">
						<tr>
							<td>
								<i class="fa fa-circle-o"></i>
							</td>
							<td class="order-num">
								${entity.subjectCode}
								<input type="hidden" class="subjectId" value="${entity.subjectId}"/>
							</td>
							<td class="name">${entity.name}</td>
							<td>${entity.gjtCourse.kcmc}</td>
							<td class="text-green">
		            			<c:if test="${entity.status==0}">未同步</c:if>
															<c:if test="${entity.status==1}">已同步</c:if>
															<c:if test="${entity.status==2}">制作中</c:if>
															<c:if test="${entity.status==3}">已发布</c:if>
		            		</td>
						</tr>
						</c:if>
					</c:forEach>
					</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="10">
									  注：选不到考试科目？先去创建考试科目吧！
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="3" />					
			</div>					
		</div>
	</div>
</div>
</form>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<script type="text/javascript">

$(function(){
	$('.slim-Scroll').slimScroll({
	    height: 190,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
		  var $container=parent.$(".select-container-ul.on");
		  //var html="";
		  var html=[
		    '<li class="select2-selection__choice">',
		      '<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>',
		      '<span class="select2-name" title="#name#" data-toggle="tooltip" data-container="body"  data-order="#order#">#name#</span>',
		      '<input type="hidden" name = "subjectId" value="#subjectId#"/>',	
		      '</li>'
		  ];
		  $container.empty();
		  $(".batch-teacher tr.on").each(function(index, el) {
		    var tmp=html.join("");
		    var name=$.trim($(this).find(".name").text());
		    var order=$.trim($(this).find(".order-num").text());
		    var subjectId=$.trim($(this).find(".subjectId").val());
		    tmp=tmp.replace(/#name#/g,name);
		    tmp=tmp.replace(/#order#/g,order);
		    tmp=tmp.replace(/#subjectId#/g,subjectId);		    
		    $container.append(tmp);
		  });
		}
	 	parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}
		else{
			$(this).addClass('on');
		}
		$(this).siblings('tr').removeClass('on');
	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>
