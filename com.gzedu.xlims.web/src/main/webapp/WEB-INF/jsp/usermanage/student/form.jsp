<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任管理系统-操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/plugins/select2/select2.min.css">
<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script>
<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script> 
<script src="${ctx}/static/plugins/morris/raphael-min.js"></script>
<script src="${ctx}/static/plugins/morris/morris.min.js"></script>
<script src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script> 
<script src="${ctx}/static/dist/js/app.js"></script> 
<script src="${ctx}/static/dist/js/common.js"></script> 


<script type="text/javascript">
$(function() {
	
	$(".btn-edit-info").click(function(){
		$(this).hide();
		$(".content-group").addClass("editing");
		$(".btn-wraper").show();
	});
	
	//取消编辑
	$(".btn-cancel-edit").click(function(){
		$(".btn-edit-info").hide();
		$(".content-group").removeClass("editing");
		$(".btn-wraper").hide();
		$("#edit").show();
	});
	
	//保存修改
	$(".btn-save-edit").click(function(){
	});
	
	 $('#datepicker').datepicker({
	      autoclose: true,
	      format : 'yyyymm', //控件中from和to 显示的日期格式
	      language: 'zh-CN',
	      todayHighlight: true
   });
})
</script>

</head>
<body class="inner-page-body">
	<section class="content">
	<div class="box no-margin stu-info-v-e">
		<div class="box-body">
			<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/usermanage/student/update.html" method="post">
			<div class="pad">
				<div class="content-group">
					<div class="cnt-box-header with-border clearfix">
					  <h3 class="cnt-box-title">基本信息</h3>
					  <div class="cnt-box-tools pull-right">
						<button class="btn btn-default btn-xs btn-edit-info" id="edit" type="button"><span class="pad">编辑资料</span></button>
						<button class="btn btn-default btn-xs " id="return" type="reset" 	onclick="history.back()"><span class="pad">返回</span></button>
					  </div>
					</div>
					<div class="cnt-box-body">
						<div class="table-block">
							<div class="table-row">
								<div class="table-cell-block">
									头像
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<c:choose>
				                        <c:when test="${not empty item.avatar}">
				                            <img src="${item.avatar}" class="img-thumbnail margin_r15" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'"
				                            style="width: 100px; height: 100px;" />
				                        </c:when>
				                        <c:otherwise>
				                            <img src="${ctx }/static/images/headImg04.png" class="img-thumbnail margin_r15" alt="User Image" 
				                            style="width: 50px; height: 50px;" />
				                        </c:otherwise>
				                    	</c:choose>
										
									</div>
									<div class="info-edit" data-type="user-image-box">
										<input id="headImgUrl" type="hidden" name ="avatar" value=""/>  <!-- update对象的时候拿到改变值，如果为空则不修改头像 -->
										 <c:choose>
				                        <c:when test="${not empty item.avatar}">
				                            <img id ="headImgId" src="${item.avatar}" class="img-thumbnail margin_r15" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'"
				                            style="width: 100px; height: 100px;" />
				                        </c:when>
				                        <c:otherwise>
				                            <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-thumbnail margin_r15" alt="User Image" 
				                            style="width: 100px; height: 100px;" />
				                        </c:otherwise>
				                    </c:choose>
										<button class="btn btn-default" name="headPortrait" type="button" onclick="javascript:uploadImage('headImgId','headImgUrl');">上传照片</button>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									学号
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.xh}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<span>${item.xh}</span>
											<input type="hidden" name="studentId" value="${item.studentId }">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									姓名
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.xm}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" class="form-control" name="xm"placeholder="姓名" value="${item.xm}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									性别
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.xbm=='1'?'男':'女'}</span>
									</div>
									<div class="info-edit" data-type="radio-group">
										<label>
										  <input type="radio" name="xbm"  value="1" <c:if test="${item.xbm=='1'}">checked</c:if>/>
										  男
										</label>
										<label class="left10">
										  <input type="radio" name="xbm"  value="2" <c:if test="${item.xbm=='2'}">checked</c:if>/>
										  女
										</label>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									民族
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.nation}</span>
									</div>
									<div class="info-edit row" data-type="select2-plug">
										<div class="col-sm-12 col-md-8">
											<input type="text" name="nation" class="form-control" value="${item.nation}" placeholder="民族">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									政治面貌
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.politicsstatus}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" class="form-control" placeholder="政治面貌" name="politicsstatus" value="${item.politicsstatus}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									学员类型
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span><dic:getLabel typeCode="USER_TYPE" code="${item.userType}" /></span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<dic:selectBox typeCode="USER_TYPE" name="search_EQ_userType" code="${item.userType}" otherAttrs='class="selectpicker show-tick form-control" data-size="5" data-live-search="true"' />
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									证件类型
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>身份证</span>：<span>${item.sfzh}</span>
									</div>
									<div class="info-edit row" data-type="form-group" >
										<div class="col-sm-12 col-md-8">
											<div class="input-group">
												<div class="input-group-btn">
												<span>身份证</span>：<input type="text" name="sfzh" class="form-control" placeholder="身份证号码" value="${item.sfzh}">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="content-group">
					<div class="cnt-box-header with-border clearfix">
					  <h3 class="cnt-box-title">学籍信息</h3>
					</div>
					<div class="cnt-box-body">
						<div class="table-block">
							<div class="table-row">
								<div class="table-cell-block">
									学期
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.gjtGrade.gradeName}</span>&nbsp;&nbsp;
										<span>${pyccMap[item.pycc]}</span>&nbsp;&nbsp;
										<span>${item.gjtSpecialty.zymc}</span>
									</div>
									<div class="info-edit row" data-type="form-group" >
										<span>${item.gjtGrade.gradeName}</span>&nbsp;&nbsp;
										<span>${pyccMap[item.pycc]}</span>&nbsp;&nbsp;
										<span>${item.gjtSpecialty.zymc}</span>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									学籍状态
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>
											${rollTypeMap[item.xjzt]}
										</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<select name="xjzt" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											  <option value="" selected="selected">请选择</option>
												<c:forEach items="${rollTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==item.xjzt}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									所属机构
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.gjtSchoolInfo.xxmc}</span>
									</div>
									<div class="info-edit row">
										<span>${item.gjtSchoolInfo.xxmc}</span>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									学习中心
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.gjtStudyCenter.scName}</span>
									</div>
									<div class="info-edit row">
										<span>${item.gjtStudyCenter.scName}</span>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									入学时间
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.rxny}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<div class="input-group">
											  <div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											  </div>
											  <input type="text" class="form-control pull-right" id="datepicker" name="rxny" placeholder="入学时间" 
						                  		value="${item.rxny}" />
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									第一学历
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.exedulevel}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" name="exedulevel" class="form-control" placeholder="第一学历" value="${item.exedulevel}">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="content-group">
					<div class="cnt-box-header with-border clearfix">
					  <h3 class="cnt-box-title">通讯方式</h3>
					</div>
					<div class="cnt-box-body">
						<div class="table-block">
							<div class="table-row">
								<div class="table-cell-block">
									邮寄地址
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.txdz}</span>
									</div>
									<div class="info-edit  row" >
										<div class="col-sm-12 col-md-8">
											<div class="row row-offset-3px">
											<input type="text" class="form-control" name="txdz" placeholder="具体地址" value="${item.txdz}">
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									邮政编码
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.yzbm}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" class="form-control" name="yzbm"placeholder="邮政编码" value="${item.yzbm}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									电子邮箱
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.dzxx}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="email" class="form-control" name="dzxx" placeholder="电子邮箱" value="${item.dzxx}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									手机号码
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.sjh}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="tel" class="form-control" name="sjh" placeholder="手机号码" value="${item.sjh}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									单位名称
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.scCo}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" class="form-control" name="scCo" placeholder="单位名称" value="${item.scCo}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									单位地址
								</div>
								<div class="table-cell-block">
									<div class="info-text">
									<span>${item.scCoAddr}</span>
									</div>
									<div class="info-edit row" data-type="address-group" >
										<div class="col-sm-12 col-md-8">
											<div class="row row-offset-3px">
													<input type="text" class="form-control" name="scCoAddr" placeholder="具体地址" value="${item.scCoAddr}">
											</div>
										</div>
									</div>
								</div>
							</div>
							<%--<div class="table-row">
								<div class="table-cell-block">
									第二联系人
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.scName}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="text" class="form-control" name="scName" placeholder="第二联系人" value="${item.scName}">
										</div>
									</div>
								</div>
							</div>
							<div class="table-row">
								<div class="table-cell-block">
									第二联系人电话
								</div>
								<div class="table-cell-block">
									<div class="info-text">
										<span>${item.scPhone}</span>
									</div>
									<div class="info-edit row">
										<div class="col-sm-12 col-md-8">
											<input type="tel" class="form-control" name="scPhone" placeholder="第二联系人电话" value="${item.scPhone}">
										</div>
									</div>
								</div>
							</div>--%>
							
						</div>
					</div>
				</div>
				<div class="row btn-wraper" style="display:none;">
					<div class="col-sm-3"></div>
					<div class="col-sm-9">
						<button type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
						<button class="btn btn-default min-width-90px btn-cancel-edit" type="button">取消</button>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>
</section>

	<jsp:include page="/eefileupload/upload.jsp"/>
</body>
</html>
