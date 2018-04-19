<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报读信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
function choice(flag){
	$("#changetype").val(flag);
	$("#listForm").submit();
}
function check(){
	$("#changetype").val($('#es').val());
}
</script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">招生管理</a></li>
			<li class="active">招生计划</li>
		</ol>
	</section>

<section class="content"  data-id="0">
<form class="form-horizontal" id="listForm" action="list.html">
<input id="changetype" type="hidden" name="search_EQ_status" value="${param.search_EQ_status}">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">招生批次</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_enrollBatchName" value="${param.search_LIKE_enrollBatchName}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">招生年度</label>
	              <div class="col-sm-9">
	               <select class="selectpicker show-tick form-control" name="search_EQ_studyYearId" value="${param.search_EQ_studyYearId}" data-size="5" data-live-search="true">
	                  <option value="">请选择</option>
	                  <c:forEach items="${studyYearMap}" var="map">
						<option value="${map.key}"<c:if test="${map.key==param['search_EQ_studyYearId']}">selected='selected'</c:if> >${map.value}</option>
					  </c:forEach>
	                </select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">招生专业</label>
	              <div class="col-sm-9">
	                <select class="selectpicker show-tick form-control" name="search_EQ_gjtSpecialty.specialtyId" value="${param.search_EQ_gjtSpecialty.specialtyId}" data-size="5" data-live-search="true">
	                  <option value="">请选择</option>
	                  <c:forEach items="${specialtyMap}" var="map">
						<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtSpecialty.specialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  </c:forEach>
	                </select>
	              </div>
	            </div>
	          </div>

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">培养层次</label>
	              <div class="col-sm-9">
	                <select class="selectpicker show-tick form-control" name="search_EQ_pycc" value="${param.search_EQ_pycc}" data-size="5" data-live-search="true">
	                  <option value=" ">请选择</option>
	                  <c:forEach items="${pyccMap}" var="map">
	                  	<option value="${map.key}"<c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if> >${map.value}</option>
	                  </c:forEach>
	                </select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">状态</label>
	              <div class="col-sm-9">
	                <select class="form-control" onclick="check();" id="es">
	                  <option value=" ">请选择</option>
	                  <option value="2"  <c:if test="${'2'==param.search_EQ_status}">selected='selected'</c:if>>已发布</option>
	                  <option value="3"  <c:if test="${'3'==param.search_EQ_status}">selected='selected'</c:if>>编辑中</option>
	                  <option value="4"  <c:if test="${'4'==param.search_EQ_status}">selected='selected'</c:if>>已结束</option>
	                </select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">招生完成度</label>
	              <div class="col-sm-9 ">
	              	<div class="input-group">
                      <div class="input-group-btn">
                        <select class="form-control input-group-select" name="year">
                          <option value=" ">请选择</option>
                          <option value="1" >=</option>
                          <option value="2" >&gt;</option>
                          <option value="3" >&lt;</option>
                          <option value="4" >&gt;=</option>
                          <option value="5" >&lt;=</option>
                        </select>
                      </div><!-- /btn-group -->
                      <input type="text" class="form-control" name="yearDegree" placeholder="请输入具体比例数字">
                    </div>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    
	    <div class="box-footer">
	      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="box">	
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">计划列表</h3>
			<div class="btn-wrap fl">
				<a href="add" class="btn btn-default btn-sm">
				 <i class="fa fa-fw fa-plus"></i>创建招生计划</a>
			</div>
		</div>
		<!--  <div class="box">-->
			
			
			<div class="box-body">
			<div id="dtable_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>
				<div class="filter-tabs clearfix">
				<ul class="list-unstyled">
					<li <c:if test="${param.search_EQ_status == '1'}">class="actived"</c:if> onclick="choice('1')">已上架(131)<c:if test="${param.search_EQ_status == '1'}"></c:if></li>
					<li <c:if test="${param.search_EQ_status == '2'}">class="actived"</c:if> onclick="choice('2')">已发布(${hasRelease})<c:if test="${param.search_EQ_status == '2'}"></c:if></li>
					<li <c:if test="${param.search_EQ_status == '3'}">class="actived"</c:if> onclick="choice('3')">编辑中(${edit})<c:if test="${param.search_EQ_status == '3'}"></c:if></li>
					<li <c:if test="${param.search_EQ_status == '4'}">class="actived"</c:if> onclick="choice('4')">已结束(${endTotalNum})<c:if test="${param.search_EQ_status == '4'}"></c:if></li>
					<li <c:if test="${param.search_EQ_status == '0'}">class="actived"</c:if> onclick="choice('0')">完成招生总指标(40)<c:if test="${param.search_EQ_status == '0'}"></c:if></li>
					<li <c:if test="${param.search_EQ_status == '0'}">class="actived"</c:if> onclick="choice('0')">未完成招生总指标(40)<c:if test="${param.search_EQ_status == '0'}"></c:if></li>
				</ul>
			</div>
				<div class="row">
					<div class="col-sm-12">
						<table id="dtable"
							class="table table-bordered table-striped table-container">
							<thead>
								<tr>
									<th>招生批次</th>
		                			<th>招生年度</th>
					                <th>招生专业</th>
					                <th>培养层次</th>
					                <th>招生周期</th>
					                <th>招生总指标</th>
					                <th>执行学习中心</th>
					                <th>实招人数</th>
					                <th>状态</th>
					                <th>操作</th>
								</tr>
							</thead>
		            <tbody>
		              <c:choose>
		               <c:when test="${not empty pageInfo.content}">
		            	<c:forEach items="${pageInfo.getContent()}" var="item">	            	
		            	<tr>
		            		<td>${item.enrollBatchName}</td>
		            		<td>${item.gjtstudyYearInfo.studyYearName}</td>
		            		<td>${item.gjtSpecialty.zymc}</td>		            		
		            		<td>
		            		<c:forEach items="${pyccMap}" var="map">
		            		<c:if test="${item.pycc==map.key}">${map.value}</c:if>		            		
		            		</c:forEach>
		            		</td>		            		
		            		<td>
		            		<fmt:formatDate value="${item.enrollSdate}" pattern="yyyy-MM-dd"/>~
		            		<fmt:formatDate value="${item.enrollEdate}" pattern="yyyy-MM-dd"/>
		            		</td>
		            		<td>${item.enrollTotalNum}</td>
		            		<td>${fn:length(item.gjtStudyEnrollNums)}</td>
		            		<td>${fn:length(item.gjtSignup)}<br><span class="gray9">(${fn:length(item.gjtSignup)/item.enrollTotalNum}%)</span></td>
		            		<td>
		            		<c:if test="${item.status==2}"><span class="text-orange">已发布</span></c:if>	
		            		<c:if test="${item.status==3}"><span class="text-orange">编辑中</span></c:if>
		            		<c:if test="${item.status==4}"><span class="text-orange">已结束</span></c:if>	
		            		</td>
		            		<td>
		            		  <c:if test="${item.status==2}">
		            			<a href="view/${item.enrollBatchId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
		            		</c:if>
		            		<c:if test="${item.status==3}">
		            			<a href="javascript:void(0);"  val="${item.enrollBatchId}" class="operion-item update-one" data-toggle="tooltip" title="发布"><i class="fa fa-edit"></i></a>
		            			<a href="update/${item.enrollBatchId}" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
		            			<a href="view/${item.enrollBatchId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
		            			<a href="javascript:void(0);" class="operion-item operion-view del-one" val="${item.enrollBatchId}" data-toggle="tooltip" title="删除" data-role="sure-btn-1"><i class="fa fa-trash-o text-red"></i></a>
		            		</c:if>
		            		<c:if test="${item.status==4}">
		            			<a href="view/${item.enrollBatchId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
		            		</c:if>
		            		</td>
		            	</tr>		            	
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
			 </div>
		 </div>
		 <tags:pagination page="${pageInfo}" paginationSize="5" />
	</div>	 
   </div>
  </div>
 </form>
</section>
<script type="text/javascript">
//发布
 $('.update-one').click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
           $.confirm({
               title: '提示',
               content: '确认发布招生计划？',
               confirmButton: '确认',
               icon: 'fa fa-warning',
               cancelButton: '取消',  
               confirmButtonClass: 'btn-primary',
               closeIcon: true,
               closeIconClass: 'fa fa-close',
               confirm: function () { 
               	 $.post("updateStatus",{ids:id},function(data){
               		if(data.successful){
               			showMessage(data);
               			window.location.reload();
               			//showSueccess(data);
               			//$this.parent().parent().parent().remove();
               		}else{
               			showMessage(data);
               		}
               },"json"); 
               } 
           });
       });

</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>