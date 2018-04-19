<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
    		excluded : [ ':disabled' ],//验证下拉必需加
            fields: {
            	batchName: {
                    validators: {
                        notEmpty: {
                        	message: "请输入批次名称"
                        }
                    }
                },
                gradeId: {
                    validators: {
                        notEmpty: {
                        	message: "请选择学期"
                        }
                    }
                },
                guideLimitNum: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文指导老师指导人数上限"
                        },
                        regexp: {
                        	regexp: /^[1-9][0-9]*$/,
                            message: "请输入有效数字"
                        }
                    }
                },
                defenceLimitNum: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文答辩老师指导人数上限"
                        },
                        regexp: {
                        	regexp: /^[1-9][0-9]*$/,
                            message: "请输入有效数字"
                        }
                    }
                },
                practiceLimitNum: {
                    validators: {
                        notEmpty: {
                        	message: "请输入社会实践老师指导人数上限"
                        },
                        regexp: {
                            regexp: /^[1-9][0-9]*$/,
                            message: "请输入有效数字"
                        }
                    }
                },
                applyThesisBeginDt: {
                    validators: {
                    	notEmpty: {
                            message: '请输入论文申请开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var applyThesisEndDt = $('#inputForm').find("input[name='applyThesisEndDt']").val();
        						$('#inputForm').find("input[name='applyThesisEndDt']").keypress();
        						validator.updateStatus('applyThesisEndDt', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(applyThesisEndDt.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                applyThesisEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文申请结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var applyThesisBeginDt = $('#inputForm').find("input[name='applyThesisBeginDt']").val();
        						$('#inputForm').find("input[name='applyThesisBeginDt']").keypress();
        						validator.updateStatus('applyThesisBeginDt', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(applyThesisBeginDt.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                submitProposeEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入提交开题截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                confirmProposeEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入开题定稿截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                submitThesisEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入初稿提交截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                confirmThesisEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文定稿截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                reviewThesisDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文初评时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                defenceThesisDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入论文答辩时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                applyPracticeBeginDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入社会实践申请开始时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var applyPracticeEndDt = $('#inputForm').find("input[name='applyPracticeEndDt']").val();
        						$('#inputForm').find("input[name='applyPracticeEndDt']").keypress();
        						validator.updateStatus('applyPracticeEndDt', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(applyPracticeEndDt.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                applyPracticeEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入社会实践申请结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var applyPracticeBeginDt = $('#inputForm').find("input[name='applyPracticeBeginDt']").val();
        						$('#inputForm').find("input[name='applyPracticeBeginDt']").keypress();
        						validator.updateStatus('applyPracticeBeginDt', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(applyPracticeBeginDt.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                submitPracticeEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入初稿提交截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                confirmPracticeEndDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入实践定稿截止时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                reviewPracticeDt: {
                    validators: {
                        notEmpty: {
                        	message: "请输入实践写作评分时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
            }
        });
	
	//时间控件改变文本框的值时重新触发校验
    $('.reservation').on('change', function(e) {
    	var fieldName = $(this).attr('name');
        $('#inputForm')
        	.data('bootstrapValidator')
            .updateStatus(fieldName, 'NOT_VALIDATED', null)
            .validateField(fieldName);
    });
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">毕业批次</a></li>
		<li class="active">新增批次</li>
	</ol>
</section>

<section class="content">
	<div class="box no-margin">
              <div class="box-body pad20">
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/graduation/graduationBatch/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action}">
                <input type="hidden" name="batchId" value="${entity.batchId}">
                  <div class="cnt-box-header with-border"> 
                    <h3 class="cnt-box-title">批次设置</h3>
                  </div>
                  <div class="form-horizontal reset-form-horizontal">
                 		<c:if test="${not empty entity.batchCode}">
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>批次号:</label>
							<div class="col-sm-6">
								<p class="form-control-static">${entity.batchCode}</p>
							</div>
						</div>
						</c:if>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>批次名称:</label>
							<div class="col-sm-6">
								<input type="text" name="batchName" class="form-control" value="${entity.batchName}" placeholder="批次名称"/>
							</div>
						</div>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>学期:</label>
							<div class="col-sm-6">
								<select name="gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${termMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==entity.gradeId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
							</div>
						</div>
					</div>
			
					<div class="cnt-box-header with-border margin_b15">
					  <h3 class="cnt-box-title">指导老师分配规则设置</h3>
					</div>
					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文指导老师指导人数上限:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<input type="text" name="guideLimitNum" class="form-control" value="${entity.guideLimitNum}" placeholder="人数"/>
									<span class="input-group-addon">人</span>
								</div>
							</div>
						</div>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文答辩老师指导人数上限:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<input type="text" name="defenceLimitNum" class="form-control" value="${entity.defenceLimitNum}" placeholder="人数"/>
									<span class="input-group-addon">人</span>
								</div>
							</div>
						</div>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>社会实践老师指导人数上限:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<input type="text" name="practiceLimitNum" class="form-control" value="${entity.practiceLimitNum}" placeholder="人数"/>
									<span class="input-group-addon">人</span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="cnt-box-header with-border margin_b15">
					  <h3 class="cnt-box-title">毕业论文时间安排</h3>
					</div>
					<div class="form-horizontal reset-form-horizontal">		
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文申请时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="applyThesisBeginDt" value="${entity.applyThesisBeginDt}"/>
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	至
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="applyThesisEndDt" value="${entity.applyThesisEndDt}" />
				                    </div>
				                </div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>提交开题截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="submitProposeEndDt" value="${entity.submitProposeEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>开题定稿截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="confirmProposeEndDt" value="${entity.confirmProposeEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>初稿提交截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="submitThesisEndDt" value="${entity.submitThesisEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文定稿截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="confirmThesisEndDt" value="${entity.confirmThesisEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文初评时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="reviewThesisDt" value="${entity.reviewThesisDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>论文答辩时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="defenceThesisDt" value="${entity.defenceThesisDt}"/>
								</div>
							</div>
						</div>
					</div>
					
					<div class="cnt-box-header with-border margin_b15">
					  <h3 class="cnt-box-title">社会实践时间安排</h3>
					</div>
					<div class="form-horizontal reset-form-horizontal">		
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>社会实践申请时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
				                        <div class="input-group-addon">
				                          <i class="fa fa-calendar"></i>
				                        </div>
									    <input type="text" class="reservation form-control" name="applyPracticeBeginDt" value="${entity.applyPracticeBeginDt}"/>
									</div>
				                	<div class="input-group-addon no-border">
					                   	 至
					                </div>
					                <div class="input-group full-width">
				                        <div class="input-group-addon">
				                          <i class="fa fa-calendar"></i>
				                        </div>
								    	<input type="text" class="reservation form-control" name="applyPracticeEndDt" value="${entity.applyPracticeEndDt}" />
								    </div>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>初稿提交截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="submitPracticeEndDt" value="${entity.submitPracticeEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>实践定稿截止时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="confirmPracticeEndDt" value="${entity.confirmPracticeEndDt}"/>
								</div>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>实践写作评分时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
			                        <div class="input-group-addon">
			                          <i class="fa fa-calendar"></i>
			                        </div>
									<input type="text" class="reservation form-control" name="reviewPracticeDt" value="${entity.reviewPracticeDt}"/>
								</div>
							</div>
						</div>
                  </div>

                  <div class="row">
                  	<div class="col-sm-6 col-sm-offset-3">
	                    <button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
    	                <button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
                  	</div>
                  </div>
                  </form>
                  
              </div>
     </div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>