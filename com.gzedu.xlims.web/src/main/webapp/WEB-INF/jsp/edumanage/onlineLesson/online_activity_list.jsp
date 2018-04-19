<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统</title>

    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp" %>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="homepage.html"><i class="fa fa-home"></i>首页</a></li>
        <li class="active">授课管理</li>
    </ol>
</section>
<section class="content">
    <form id="listForm" class="form-horizontal">
        <input type="hidden" name="search_lessonType" value="${param.search_lessonType}" id="search_lessonType"/>
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">直播名称</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_onlinetutorName"
                                       value="${param.search_onlinetutorName}" placeholder="请输入直播名称">
                             </div>
                         </div>
                     </div>
<div class="col-sm-4">
                         <div class="form-group">
		<label class="control-label col-sm-3 text-nowrap">
			直播时间
		</label>
		<div class="col-sm-9">
			<input type="text" name="search_onlinetutorStart" value="${param.search_onlinetutorStart}" class="form-control"  data-role="datetime" />
				</div>
			</div>
                       </div>
                   </div>
                   
               </div>
				<div class="box-footer">
                   <div class="btn-wrap">
                       <button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
                   </div>
                   <div class="btn-wrap">
                       <button type="submit" class="btn min-width-90px btn-primary" id="btn_search">搜索</button>
                   </div>
               </div>
           </div>
           <div class="box">
               <div class="box-header with-border">
                   <h3 class="box-title pad-t5">直播列表</h3>
                   <div class="fr">
	                   	<shiro:hasPermission name="/edumanage/onlinelesson/queryOnlineActivityList$create">
	                    <div class="btn-wrap fl">
	                        <a href="${ctx}/edumanage/onlinelesson/toActivityForm" class="btn btn-block btn-default btn-add">
	                            <i class="fa fa-fw fa-plus"></i> 新建直播</a>
	                    </div>
	                    </shiro:hasPermission>
                	</div>
            	</div>
				<div class="box-body">
				    <div class="filter-tabs filter-tabs2 clearfix">
				        <ul class="list-unstyled">
				            <li  value="3"
				                 class="<c:if test="${empty param['search_lessonType'] || param['search_lessonType'] eq '3'}">actived</c:if> lesson_type">
				                全部(${lessonNum})
				            </li>
				            <li value="1"
				                class=" <c:if test="${param['search_lessonType'] == '1' }">actived </c:if> lesson_type">
				                直播中(${lessoningNum})
				            </li>
				            <li value="0"
				                class="<c:if test="${param['search_lessonType'] == '0' }">actived</c:if> lesson_type">
				                待开始(${needLessonNum})
				            </li>
				            <li value="2"
				                class="<c:if test="${param['search_lessonType'] == '2' }">actived</c:if> lesson_type">
				                已结束(${lessonedNum})
				            </li>
				        </ul>
				    </div>
				    <div class="table-responsive">
				        <table class="table table-bordered table-striped vertical-mid text-center table-font" id="table">
				            <thead>
				            <tr>
				                <th width="20%">直播名称</th>
				                <th>直播时间</th>
				                <th width="20%">直播范围</th>
				                <th>计划直播人数</th>
				                <th>参与直播人数</th>
				                <th>主播</th>
				                <th>状态</th>
				                <th>助教</th>
				                <th>操作</th>
				            </tr>
				            </thead>
				            <tbody>
				            <c:choose>
				                <c:when test="${not empty pageInfo.content}">
				                    <c:forEach items="${pageInfo.content}" var="entity">
				                        <tr>
				                            <td>
				                                    ${entity.onlinetutorName}
				                            </td>
				                            <td>
				                                <fmt:formatDate value="${entity.onlinetutorStart}" type="date"
				                                                pattern="yyyy-MM-dd"/>
				                                <br/>
				                                <fmt:formatDate value="${entity.onlinetutorStart}" type="date"
				                                                pattern="HH:mm"/>
				                                ~
				                                <fmt:formatDate value="${entity.onlinetutorFinish}" type="date"
				                                                pattern="HH:mm"/>
				                            </td>
				                            <td style="text-align: left">
				                                               层次：${empty(entity.getRangeIds(8))?'全部':'' }
				<c:forEach items="${entity.getRangeIds('8') }" var="item">
				${pyccMap[item]};
				</c:forEach>
				<br/>
				 年级：${empty(entity.getRangeIds(4))?'全部':'' }
				<c:forEach items="${entity.getRangeIds(4) }" var="item">
				${gradeMap[item]};
				</c:forEach> <br/>
				专业：${empty(entity.getRangeIds(5))?'全部':'' }
				<c:forEach items="${entity.getRangeIds(5) }" var="item">
				<span style="display: inline-block;">${specialtyMap[item]}</span>
				</c:forEach> <br/>
				课程：${empty(entity.getRangeIds(9))?'全部':'' }
				<c:forEach items="${entity.getRangeIds(9) }" var="item">
				${courseMap[item]};
				</c:forEach>
				                            </td>
				                            <td>
				
				                                    ${entity.allNum}
				                            </td>
				                            <td>
				
				                                    ${empty(entity.joinNum)?0:entity.joinNum}
				                            </td>
				                            <td>
				
				                                    ${userName}
				                            </td>
				                            <td>
				                                    <c:choose>
				                                        <c:when test="${entity.status eq 0}">
				                                            <div class="text-green">待开始</div>
				                                            <small class="gray9">（${entity.days}天后开始）</small>
				                                        </c:when>
				                                        <c:when test="${entity.status eq 2  }"><div class="text-gray">已结束</div></c:when>
				                                        <c:otherwise><div class="text-orange">直播中</div></c:otherwise>
				                                    </c:choose>
				                            </td>
				                              <td data-oid="${entity.onlinetutorId}">
				<c:if test="${entity.status eq 0 or entity.status eq 1}">
				   									<a href="${entity.teacherjoinurl}?nickname=${userName}&token=111111" target="_blank" >助教入口</a>
				   								</c:if>
				</td>
				              								
				                                       <td data-oid="${entity.onlinetutorId}">
				                                <div class="data-operion">
				                                		<c:if test="${entity.status eq 0}">
				                                			<shiro:hasPermission name="/edumanage/onlinelesson/queryOnlineActivityList$update">
				                                        	 	<a href="${ctx}/edumanage/onlinelesson/toActivityForm?id=${entity.id}" class="operion-item" data-toggle="tooltip" title="修改"><i class="fa fa-edit"></i></a>
				                                        	</shiro:hasPermission>
				                                        	<shiro:hasPermission name="/edumanage/onlinelesson/queryOnlineActivityList$delete">
				                                         	<a href="#"   data-id="${entity.id}"  class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class=" fa fa-trash-o text-red"></i></a>
				                                       	</shiro:hasPermission>
				                                     </c:if>
				                                    
				                                    <c:if test="${entity.status eq 1}">
				                                        <a href="${entity.teacherjoinurl}?nickname=${userName}&token=${entity.teachertoken}" target="_blank" class="operion-item" data-toggle="tooltip" title="" data-original-title="进入直播"><i class="fa fa-fw fa-sign-in"></i>进入直播</a>
				                                    </c:if>
				                                        <a href="${ctx}/edumanage/onlinelesson/queryOnlineActivityDetail?id=${entity.id}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-view-more"></i></a>
				                                </div>
				                            </td>
				                        </tr>
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
    </form>
</section>


<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        //删除直播
        $("body").confirmation({
            selector: '[data-role="remove"]',
            html:true,
            placement:'top',
            content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除该直播？</div>',
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

                postIngIframe=$.formOperTipsDialog({
                    text:'数据删除中...',
                    iconClass:'fa-refresh fa-spin'
                });
                var url = "/edumanage/onlinelesson/deleteOnlineActivity?id="+element.attr("data-id");
                $.post(url,{},function (data) {
                    if(data.successful){
                        postIngIframe.find('[data-role="tips-text"]').html('数据删除成功...');
                        postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
                        location.reload();
                    }else{
						alert("系统异常");
						$.closeDialog(postIngIframe);
                    }
                },"json");
            },
            onCancel:function(event, element){

            }
        });


        $(".lesson_type").click(function(event){// 状态选择
            event.preventDefault();
            $("#search_lessonType").val($(this).val());
            $('#btn_search').click();
        });


        $('[data-toggle="tab"]').click(function(event) {
            event.preventDefault();
            window.location.href=$(this).attr("href");
        });

        /*日期控件*/
		$('[data-role="datetime"]').datepicker({
			language:'zh-CN',
			autoclose: true,//选中之后自动隐藏日期选择框
			format:'yyyy-mm-dd'
		});

    })

  
</script>
</body>
</html>
