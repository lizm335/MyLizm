<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>我发布的活动</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
	
</script>

</head>
<body class="inner-page-body">
	<!-- Main content -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">主页</a>
			</li>
			<li class="active">组织活动管理</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal" action="putList">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li>
						<a href="${ctx}/admin/messageInfo/list" target="_self">我接收的活动</a>
					</li>
					<li class="active">
						<a href="${ctx}/admin/messageInfo/putList" target="_self">我发布的活动</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="box box-border">
						<div class="box-body">
							<div class="row pad-t15">
								<div class="col-md-4">
									<label class="control-label col-sm-3">标题</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_infoTheme" value="${param.search_LIKE_infoTheme}">
										<input type="hidden" name="search_EQ_degree" value="${param.search_EQ_degree}">
										<input type="hidden" name="isEffective" value="${isEffective}">
									</div>
								</div>
								<div class="col-md-4">
									<label class="control-label col-sm-3">活动类型</label>
									<div class="col-sm-9">
										<select id="basic" name="search_EQ_infoType" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${infoTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${param.search_EQ_infoType==map.key}">selected</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="col-sm-4">
						            <div class="form-group">
						              <label class="control-label col-sm-3 text-nowrap">日期</label>
						              <div class="col-sm-9">
						                <div class="input-group input-daterange" data-role="date-group">
						                  <input type="text" name="search_GTE_createdDt" class="form-control" data-role="date-start">
						                  <span class="input-group-addon nobg">－</span>
						                  <input type="text" name="search_LT_createdDt" class="form-control" data-role="date-end">
						              	</div>
						              </div>
						            </div>
						        </div>
								
							</div>
						</div>
						<div class="box-footer">
							<div class="btn-wrap">
								<button type="reset" class="btn btn-default">重置</button>
							</div>
							<div class="btn-wrap">
								<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
							</div>
						</div>
					</div>

					<div class="box box-border">
						<div class="box-header with-border">
							<div class="fr">
								<div class="btn-wrap fl">
									<shiro:hasPermission name="/admin/messageInfo/list$create">
										<a href="create" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增
										</a>
									</shiro:hasPermission>
								</div>
							</div>
						</div>

						<div class="box-body">
							<div class="filter-tabs filter-tabs2 clearfix">
								<ul class="list-unstyled">
									<li lang=":input[name='search_EQ_degree'], :input[name='isEffective']" <c:if test="${empty param['search_EQ_degree'] and empty isEffective }">class="actived"</c:if>>全部(${totalNum})</li>
									<li value="1" role=":input[name='search_EQ_degree']" lang=":input[name='isEffective']" <c:if test="${param['search_EQ_degree'] == '1' }">class="actived"</c:if>>重要(${importantNum})</li>
									<li value="0" role=":input[name='search_EQ_degree']" lang=":input[name='isEffective']" <c:if test="${param['search_EQ_degree'] == '0' }">class="actived"</c:if>>一般(${genericNum})</li>
									<li value="1" role=":input[name='isEffective']" lang=":input[name='search_EQ_degree']" <c:if test="${param['isEffective'] == '1' }">class="actived"</c:if>>置顶(${efectiveNum})</li>
								</ul>
							</div>
							<div class="table-responsive">
								<table class="table table-bordered table-striped  text-center  table-cell-ver-mid ">
									<thead>
										<tr>
											<th>封面</th>
											<th>标题</th>
											<th>活动相关</th>
											<th>发布人</th>
											<th>发布时间</th>
											<th>已读/未读人数</th>
											<th>已读查看比例</th>
											<th>互动</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty pageInfo.content}">
												<c:forEach items="${pageInfo.content}" var="info">
													<c:if test="${not empty info}">
														<td>
															<c:if test="${not empty info.attachment }">
																<img src="${info.attachment }" alt="Image" width="120" height="80">
															</c:if>
															<c:if test="${empty info.attachment }">
																<img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png" alt="Image" width="120" height="80">
															</c:if>
														</td>
														<td>${info.isStick==true?'【置顶】':'' }${info.infoTheme }</td>
														<td style="text-align: left;">类型：${infoTypeMap[info.infoType]} <br /> 
														分类：${info.typeClassify}<br /> 
														重要程度：${info.degree}<br />
														</td>
														<td>${info.putUserName}
															<c:if test="${not empty info.createRoleName}">
																<div class="gray9">（${info.createRoleName}）</div>
															</c:if>
														</td>
														<td>${info.createdDt}</td>
														<td>${info.readTotal}/${info.putTotal}
															<div class="gray9">（${info.noReadRatio}未读）</div>
														</td>
														<td class="text-left">PC：${info.pcCount} <br /> APP：${info.appCount}<br /> 公众号：${info.comCount}<br />
														</td>
														<td>
															<c:if test="${info.isComment eq '1' }">
																	评论：${info.commentCount} <br />
															</c:if>
															 <c:if test="${info.isLike eq '1' }">
																	点赞：${info.likeCount}<br />
															</c:if> 
															<c:if test="${info.isFeedback eq '1' }">
																	反馈：${info.feedbackCount}<br />
															</c:if>
														</td>
														<td>
															
															<div class="data-operion">
																<c:if test="${info.isStick }">
																	<a href="javaScript:;" id="cleanStick" data-id="${info.messageId}" class="operion-item operion-view" data-toggle="tooltip" title="" data-original-title="取消置顶"><i class="fa fa-minus-circle"></i></a>
																</c:if>
																<a href="view/${info.messageId}" class="operion-item operion-view" data-toggle="tooltip" title="" data-original-title="查看详情"><i
																	class="fa fa-fw fa-view-more"></i> </a>
																 <shiro:hasPermission name="/admin/messageInfo/list$update">
																	<a href="update/${info.messageId}" class="operion-item operion-edit" title="编辑"> <i class="fa fa-fw fa-edit"></i>
																	</a>
																</shiro:hasPermission> 
																<%-- 	<a href="toDetail?id=${info.messageId}"
																		   class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a> --%>
																<%-- 
																		<a href="view/${info.messageId}" 
																			class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a>
																		 --%>
																<shiro:hasPermission name="/admin/messageInfo/list$delete">
																	<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${info.messageId}" title="删除" data-tempTitle="删除"><i
																		class="fa fa-fw fa-trash-o"></i></a>
																</shiro:hasPermission>
															</div>
														</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td align="center" colspan="9">暂无数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<tags:pagination page="${pageInfo}" paginationSize="5" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</section>
	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script>
		$('#cleanStick').click(function(){
			$.get('cleanStick',{id:$(this).data('id')},function(data){
				if(data.successful){
					window.location.reload();
				}else{
					alert(data.message);
				}
			},'json');
		});
	</script>
</body>
</html>
