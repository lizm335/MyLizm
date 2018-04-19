<%@page import="java.util.Date"%>
<%@page import="javax.xml.crypto.Data"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统</title>

    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">


<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">授课管理</a></li>
        <li><a href="#">直播辅导</a></li>
        <li class="active">详情</li>
    </ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/openclass/toLessonDetail">
    <div class="box">
    	<div class="box-header with-border">
			<div class="media pad-t10 pad-b10 question-item">
				<div class="media-left">
					<c:if test="${not empty(onlineLesson.IMG_URL)}">
						<img src="${onlineLesson.IMG_URL}" width="210" height="140">
					</c:if>
					<c:if test="${empty(onlineLesson.IMG_URL)}">
						<img src="${css}/common/v2/temp/demo_01.jpg" width="210" height="140">
					</c:if>
				</div>
				<div class="media-body">
					<h4 class="media-heading">${onlineLesson.ONLINE_NAME}</h4>
					<div class="gray9">
						<div class="margin_t5">直播时间：
							 <fmt:formatDate value="${onlineLesson.START_DT}" type="date" pattern="yyyy-MM-dd HH:mm"/>~<fmt:formatDate value="${onlineLesson.END_DT}" type="date" pattern="HH:mm"/>
						</div>
						<div class="margin_t10 txt gray9" data-id="broadcast-summary">${onlineLesson.ONLINETUTOR_DESC}</div>
						<ul class="list-unstyled margin_t10">
							<li>
								学期：
		                     	<c:forEach items="${gjtGrades}" var="g">
		                     		${g.gradeName}、
		                     	</c:forEach></li>
							<li>
								课程：
		                     	<c:forEach items="${gjtCourses}" var="c">
		                     		${c.kcmc}(${c.kch})、
		                     	</c:forEach>
		                     </li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="box-body pad-t15 pad-b15">
			<div class="clearfix">
				<div class="pull-right margin_t10 text-nowrap">
					<c:choose>
						<c:when test="${onlineLesson.LESSON_STATE == 'lessoning' || onlineLesson.LESSON_STATE == 'needlesson'}">
							<span class="inline-block vertical-middle margin_r10">${onlineLesson.LESSON_STATE=='needlesson'?'距离直播开始还有':'直播已开始' } ${diff }</span>
							<a type="button" target="_blank" href="${onlineLesson.TCH_URL}?nickname=${onlineLesson.CLASS_TEACHER}&token=${onlineLesson.TCH_TOKEN}" class="btn btn-warning btn-md" style="width: 120px;">进入直播</a>
							<div class="text-right margin_t5">
								<a target="_blank" href="${onlineLesson.TCH_URL}?nickname=${userName}&token=111111" class="text-blue inline-block text-center" style="width: 120px;">
									<u>助教入口</u>
								</a>
							</div>
						</c:when>
						<c:when test="${onlineLesson.LESSON_STATE eq 'lessoned'}">
							<c:if test="${not empty(onlineLesson.videojoinurl) }">
								<a target="_blank" href="${onlineLesson.VIDEO_URL}?nickname=${userName}&token=${onlineLesson.VIDEO_TOKEN}" class="btn btn-warning btn-md" style="width: 120px;">查看录播</a>
							</c:if>
							<c:if test="${empty(onlineLesson.videojoinurl) }">
								<a target="_blank" href="" id="videoBtn" class="btn btn-warning btn-md" style="width: 120px;">查看录播</a>
							</c:if>
						</c:when>
					</c:choose>
				</div>
				<div class="oh">
					<div class="row stu-info-status text-center">
						<div class="col-sm-2 col-xs-6" style="border-left: 1px solid #eee;">
							<div class="f20">${allStu}</div>
							<div class="gray9">需辅导人数</div>
						</div>
						<div class="col-sm-2 col-xs-6">
							<div class="f20">${onlineLesson.LESSON_STATE=='needlesson'?'--':joinStu}</div>
							<div class="gray9">参与直播人数</div>
						</div>
						<div class="col-sm-2 col-xs-6">
							<div class="f20">${userName}</div>
							<div class="gray9">主播</div>
						</div>
						<div class="col-sm-2 col-xs-6">
							<c:choose>
                                   <c:when test="${onlineLesson.LESSON_STATE eq 'lessoning'}"><div class="f20 text-orange">直播中</div></c:when>
                                   <c:when test="${onlineLesson.LESSON_STATE eq 'needlesson'}"><div class="f20 text-orange">待开始</div></c:when>
                                   <c:otherwise> <div class="f20 gray9">已结束</div></c:otherwise>
                               </c:choose>
							<div class="gray9">状态</div>
						</div>
					</div>
				</div>
			</div>
		</div>
    </div>
    <div class="box">
        <div class="box-header with-border">
             <input type="hidden" name="id" value="${param.id }" /> 
            <div class="row ">
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label col-sm-3 text-nowrap">姓名</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="search_studentName" value="${param['search_studentName']}">
                        </div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="form-group">
                        <label class="control-label col-sm-3 text-nowrap">学号</label>
                        <div class="col-sm-9">
                            <input class="form-control" type="text" name="search_studentCode" value="${param.search_studentCode}" >
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
						<label class="control-label col-sm-3">所属专业</label>
						<div class="col-sm-9">
							<select name="search_specialtyId" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${specialtyBaseMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param.search_specialtyId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
				</div>
                <div class="col-sm-2">
               		 <button type="submit" class="btn min-width-90px btn-primary" id="btn_search">搜索</button>
                </div>
            </div>
                      
        </div>
        <div class="box-body">
        	<input type="hidden" id="exportJoinType" name="search_exportJoinType" />
        	<input type="hidden" name="isLesson" value="true">
            <div class="pull-right">
				<div class="dropdown">
					<button type="button" class="btn btn-sm btn-default" data-toggle="dropdown" style="width: 120px;">
						<i class="fa fa-fw fa-sign-in"></i> 导出学员
					</button>
					<input type="hidden" id="exportJoinType" name="search_exportJoinType" />
					<ul class="dropdown-menu">
						<li>
							<a href="#" data-type="" data-role="output">导出全部学员</a>
						</li>
						<li>
							<a href="#" data-type="1" data-role="output">导出已参与学员</a>
						</li>
						<li>
							<a href="#" data-type="0" data-role="output">导出未参与学员</a>
						</li>
						<li>
							<a href="#" data-type="2" data-role="output">导出已看录播学员</a>
						</li>
					</ul>
				</div>
			</div>
            <div class="filter-tabs clearfix">
            	<input type="hidden" id="joinType" name="search_joinType" value="${param.search_joinType }" />
                <ul class="list-unstyled">
                    <li class="<c:if test="${empty param['search_joinType'] }">actived</c:if> search_list" data-type="" >全部（${allNum}）</li>
                    <li class="<c:if test="${param['search_joinType'] eq '1'}">actived</c:if> search_list" data-type="1" >已参与（${joinNum}）</li>
                    <li class="<c:if test="${param['search_joinType'] eq '0'}">actived</c:if> search_list" data-type="0" >未参与（${unjoinNum}）</li>
                    <li class="<c:if test="${param['search_joinType'] eq '2'}">actived</c:if> search_list" data-type="2" >已看录播（${viewNum}）</li>
                </ul>
            </div>
             
            <div class="table-responsive">
                <table class="table table-bordered table-striped vertical-mid text-center table-font">
                    <thead>
                    <tr>
                        <th width="70">头像</th>
                        <th>个人信息</th>
                        <th width="21%">报读信息</th>
                        <th width="10%">学期</th>
                        <th width="15%">课程</th>
                        <th width="14%">进入时间</th>
                        <th width="8%">状态</th>
                        <th width="10%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty pageInfo.content}">
                                <c:forEach items="${pageInfo.content}" var="entity">
                                    <tr>
                                        <td>
                                            <img src="${not empty entity.PIC ? entity.PIC : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
		                                    <c:if test="${empty entity.PIC}">
		                                        <p class="gray9">未上传</p>
		                                    </c:if>
                                        </td>
                                        <td>
                                            <ul class="list-unstyled text-left">
                                                <li>姓名：${entity.XM}</li>
                                                <li>学号：${entity.XH}</li>
                                                <li>手机：
	                                                <shiro:hasPermission name="/personal/index$privacyJurisdiction">
														${entity.SJH}
													</shiro:hasPermission>
													<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
														<c:if test="${not empty entity.SJH }">
														${fn:substring(entity.SJH,0, 3)}******${fn:substring(entity.SJH,8, (entity.SJH).length())}
														</c:if>
													</shiro:lacksPermission>
                                                </li>
                                            </ul>
                                        </td>
                                        <td>
                                            <ul class="list-unstyled text-left">
                                                <li>层次：${entity.PYCC}</li>
                                                <li>年级：${entity.GRADE_NAME}</li>
                                                <li>学期：${entity.TERM_NAME}</li>
                                                <li>专业：${entity.ZYMC}</li>
                                            </ul>
                                        </td>
                                        <td>${entity.TERM_NAME}</td>
                                        <td>
                                            <div>${entity.KCMC}</div>
                                            <small class="gray9">（${entity.KCH}）</small>
                                        </td>
                                        <td>
                                                ${empty(entity.LOGIN_TIME)?'--':entity.LOGIN_TIME}
                                        </td>
                                        <td>	
                                        	<c:choose>
                                        		<c:when test="${entity.IS_INTO=='Y'}">
                                        			<span class="text-green">已参与</span>
                                        		</c:when>
                                        		<c:otherwise>
                                        			<span class="gray9">未参与</span>
                                        		</c:otherwise>
                                        	</c:choose>
                                        </td>
                                        <td><a href="${ctx}/edumanage/roll/analogLogin?id=${entity.STUDENT_ID}" target="_blank">模拟登陆</a></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td align="center" colspan="8">暂无数据</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <tags:pagination page="${pageInfo}" paginationSize="10"/>
            </div>
        </div>
    </div>
    </form>
</section>


<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    $(function() {

        //TODO  查看录播
        $("#btn_resee").click(function (event) {
            var tch_url = $("#tch_url").val();
            var tch_token = $("#tch_token").val();
            console.log(tch_url,tch_token);

        });

        $(".search_list").click(function (event) {
            $('#joinType').val($.trim($(this).data('type')));
            $('#listForm').submit();
        })

        $('.list-export li').click(function(){
			$('#exportJoinType').val($.trim($(this).data('type')));
			var url=$('#listForm').attr('action');
			$('#listForm').attr('action','${ctx}/edumanage/openclass/exportLessonStudent');
			$('#listForm').submit();
			$('#listForm').attr('action',url);
        });
      //导出学员
        $(document).on('click', '[data-role="output"]', function(event) {
          event.preventDefault();
          var self=this;
          $('#exportJoinType').val($(this).data('type'));
          $.mydialog({
            id:'export',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/onlinelesson/toExportStudent?'+$('#listForm').serialize()
          });
        });

        $('#videoBtn').click(function(){
		var href=$(this).attr('href');
		if(href=='#'){
			alert('该直播无录播视频');
			return false;
		}
		if(href==''){
			var rs=false;
			$.ajax({
			    url:'${ctx}/edumanage/onlinelesson/queryVideoUrl?id=${param.id}',
			    async:false,
			    dataType:'json',
			    success:function(data){
					if(data.successful){
						var url=data.obj.videojoinurl+'?nickname=${userName}&token='+data.obj.videotoken;
					    $('#videoBtn').attr('href',url);
					    rs=true;
					}else{
					    alert('该直播无录播视频');
					    $('#videoBtn').attr('href','#');
					}
				}
			});
			return rs;
		}
	});

    });
</script>
</body>
</html>