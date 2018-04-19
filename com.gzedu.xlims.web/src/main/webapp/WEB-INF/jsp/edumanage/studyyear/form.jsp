<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学年度操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if('${action}' == 'view'){
		$(':input').attr("readonly","readonly");
		$('select').attr("disabled","disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
	
	
    $('#inputForm').bootstrapValidator({
    		excluded: [':disabled'],//验证下拉必需加
            fields: {
        		oldStudentEnterDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入老生开学日期'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
                newStudentEnterDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入新生开学日期'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        }
                    }
                },
        		payStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入老生缴费开始日期'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var endDate = $('#inputForm').find("input[name='payEndDate']").val();
        						$('#inputForm').find("input[name='payEndDate']").keypress();
        						validator.updateStatus('payEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(endDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                payEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入老生缴费结束日期"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var startDate = $('#inputForm').find("input[name='payStartDate']").val();
        						$('#inputForm').find("input[name='payStartDate']").keypress();
        						validator.updateStatus('payStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(startDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                enrollStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入招生计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var enrollEndDate = $('#inputForm').find("input[name='enrollEndDate']").val();
        						$('#inputForm').find("input[name='enrollEndDate']").keypress();
        						validator.updateStatus('enrollEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(enrollEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                enrollEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入招生计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var enrollStartDate = $('#inputForm').find("input[name='enrollStartDate']").val();
        						$('#inputForm').find("input[name='enrollStartDate']").keypress();
        						validator.updateStatus('enrollStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(enrollStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                enrollResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择招生计划责任岗位"
                        }
                    }
                },
                enrollResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择招生计划责任人"
                        }
                    }
                },
                schoolrollStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入学籍计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var schoolrollEndDate = $('#inputForm').find("input[name='schoolrollEndDate']").val();
        						$('#inputForm').find("input[name='schoolrollEndDate']").keypress();
        						validator.updateStatus('schoolrollEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(schoolrollEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                schoolrollEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入学籍计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var schoolrollStartDate = $('#inputForm').find("input[name='schoolrollStartDate']").val();
        						$('#inputForm').find("input[name='schoolrollStartDate']").keypress();
        						validator.updateStatus('schoolrollStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(schoolrollStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                schoolrollResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择学籍计划责任岗位"
                        }
                    }
                },
                schoolrollResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择学籍计划责任人"
                        }
                    }
                },
                educationalStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入教务计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var educationalEndDate = $('#inputForm').find("input[name='educationalEndDate']").val();
        						$('#inputForm').find("input[name='educationalEndDate']").keypress();
        						validator.updateStatus('educationalEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(educationalEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                educationalEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入教务计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var educationalStartDate = $('#inputForm').find("input[name='educationalStartDate']").val();
        						$('#inputForm').find("input[name='educationalStartDate']").keypress();
        						validator.updateStatus('educationalStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(educationalStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                educationalResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择教务计划责任岗位"
                        }
                    }
                },
                educationalResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择教务计划责任人"
                        }
                    }
                },
                teachingStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入教学计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var teachingEndDate = $('#inputForm').find("input[name='teachingEndDate']").val();
        						$('#inputForm').find("input[name='teachingEndDate']").keypress();
        						validator.updateStatus('teachingEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(teachingEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                teachingEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入教学计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var teachingStartDate = $('#inputForm').find("input[name='teachingStartDate']").val();
        						$('#inputForm').find("input[name='teachingStartDate']").keypress();
        						validator.updateStatus('teachingStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(teachingStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                teachingResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择教学计划责任岗位"
                        }
                    }
                },
                teachingResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择教学计划责任人"
                        }
                    }
                },
                examStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入考试计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var examEndDate = $('#inputForm').find("input[name='examEndDate']").val();
        						$('#inputForm').find("input[name='examEndDate']").keypress();
        						validator.updateStatus('examEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(examEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                examEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入考试计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var examStartDate = $('#inputForm').find("input[name='examStartDate']").val();
        						$('#inputForm').find("input[name='examStartDate']").keypress();
        						validator.updateStatus('examStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(examStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                examResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择考试计划责任岗位"
                        }
                    }
                },
                examResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择考试计划责任人"
                        }
                    }
                },
                graduationStartDate: {
                    validators: {
                    	notEmpty: {
                            message: '请输入毕业计划开始时间'
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '开始日期不能晚于结束日期',
        					callback: function(value, validator, $field, options){
        						var graduationEndDate = $('#inputForm').find("input[name='graduationEndDate']").val();
        						$('#inputForm').find("input[name='graduationEndDate']").keypress();
        						validator.updateStatus('graduationEndDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) <= Date.parse(graduationEndDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                graduationEndDate: {
                    validators: {
                        notEmpty: {
                        	message: "请输入毕业计划结束时间"
                        },
                		date: {
                            format: 'YYYY-MM-DD',
                            message: '请输入正确的时间格式（YYYY-MM-DD）'
                        },
                        callback: {
        					message: '结束日期不能早于开始日期',
        					callback: function(value, validator, $field, options){
        						var graduationStartDate = $('#inputForm').find("input[name='graduationStartDate']").val();
        						$('#inputForm').find("input[name='graduationStartDate']").keypress();
        						validator.updateStatus('graduationStartDate', 'VALID');
        						return Date.parse(value.replace(/-/g,"/")) >= Date.parse(graduationStartDate.replace(/-/g,"/"));
        					}
        				}
                    }
                },
                graduationResponsible: {
                    validators: {
                        notEmpty: {
                        	message: "请选择毕业计划责任岗位"
                        }
                    }
                },
                graduationResponsiblePer: {
                    validators: {
                        notEmpty: {
                        	message: "请选择毕业计划责任人"
                        }
                    }
                }
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
  	
  	$("#studyYearCode").on('change', function(e) {
        $("#studyYearCode1").val($(this).val());
    });
	
})
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学期计划</a></li>
		<li class="active">设定计划</li>
	</ol>
</section>

<section class="content">
	<div class="box margin-bottom-none">
	
	<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/studyyear/${action }" method="post">
		<input id="action" type="hidden" name="action" value="${action }">
		<input type="hidden" name="gradeId" value="${item.gradeId }">
		<div class="box-body">
			<h3 class="cnt-box-title f16 margin_b10 margin_t20">设定计划</h3>
			<div class="row pad-l20" style="line-height: 30px">
				 <div class="col-sm-4 col-xs-6">
				 	所属年级：${item.gjtYear.name}
				 </div>
				 <div class="col-sm-4 col-xs-6">
				 	学期编号：${item.gradeCode}
				 </div>
				 <div class="col-sm-4 col-xs-6">
				 	学期名称：${item.gradeName}
				 </div>
				 <div class="col-sm-4 col-xs-6">
				 	学期时间：${item.startDate}~${item.endDate}
				 </div>
			</div>
			
			<h3 class="cnt-box-title f16 margin_b10 margin_t20">学期基础计划</h3>
			<div class="form-horizontal reset-form-horizontal">	
				<div class="form-group">
					<label class="col-sm-3 control-label"><small class="text-red">*</small>老生缴费日期</label>
					<div class="col-sm-6 position-relative">
						<div class="row">
							<div class="col-sm-8">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="payStartDate" value="${item.payStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="payEndDate" value="${item.payEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label"><small class="text-red">*</small>老生开学日期</label>
					<div class="col-sm-4">
						<div class="input-group full-width">
	                      <div class="input-group-addon">
	                        <i class="fa fa-calendar"></i>
	                      </div>
	                      <input type="text" name="oldStudentEnterDate" value="${item.oldStudentEnterDate}" class="form-control reservation">
	                    </div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label"><small class="text-red">*</small>新生开学日期</label>
				<div class="col-sm-4">
					<div class="input-group full-width">
                      <div class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                      </div>
                      <input type="text" name="newStudentEnterDate" value="${item.newStudentEnterDate}" class="form-control reservation">
                    </div>
				</div>
			</div>
			
			<h3 class="cnt-box-title f16 margin_b10 margin_t20">学期详细计划制定安排</h3>
			<div class="form-horizontal reset-form-horizontal">	
				<div class="form-group with-bg-gray" style="font-weight: 700;text-align: center">
					<div class="col-sm-2" style="text-align: right">计划类型</div>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			要求定制时间
		            		</div>
		            		<div class="col-sm-3">
								责任岗位
		            		</div>
		            		<div class="col-sm-3">
								责任人
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>招生计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="enrollStartDate" value="${item.enrollStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="enrollEndDate" value="${item.enrollEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select sub_sel="enrollResponsiblePer" name="enrollResponsible" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.enrollResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="enrollResponsiblePer" temp_val="${item.enrollResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学籍计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="schoolrollStartDate" value="${item.schoolrollStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="schoolrollEndDate" value="${item.schoolrollEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select name="schoolrollResponsible" sub_sel="schoolrollResponsiblePer" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.schoolrollResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="schoolrollResponsiblePer" temp_val="${item.schoolrollResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>教务计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="educationalStartDate" value="${item.educationalStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="educationalEndDate" value="${item.educationalEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select sub_sel="educationalResponsiblePer" name="educationalResponsible" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.educationalResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="educationalResponsiblePer" temp_val="${item.educationalResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>教学计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="teachingStartDate" value="${item.teachingStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="teachingEndDate" value="${item.teachingEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select sub_sel="teachingResponsiblePer" name="teachingResponsible" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.teachingResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="teachingResponsiblePer" temp_val="${item.teachingResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>考试计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="examStartDate" value="${item.examStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="examEndDate" value="${item.examEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select sub_sel="examResponsiblePer" name="examResponsible" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.examResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="examResponsiblePer" temp_val="${item.examResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>毕业计划</label>
					<div class="col-sm-8 position-relative">
						<div class="row">
							<div class="col-sm-6">
		            			<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="graduationStartDate" value="${item.graduationStartDate}" class="form-control reservation">
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	~
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" name="graduationEndDate" value="${item.graduationEndDate}" class="form-control reservation">
				                    </div>
				                </div>
		            		</div>
		            		<div class="col-sm-3">
								<select sub_sel="graduationResponsiblePer" name="graduationResponsible" class="selectpicker show-tick form-control role-sel" data-size="5" data-live-search="true">
									<option value="">选择责任岗位</option>
									<c:forEach items="${responsibleMap}" var="map">								
										<option value="${map.key}" <c:if test="${map.key==item.graduationResponsible}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>	
		            		</div>
		            		<div class="col-sm-3">
								<select name="graduationResponsiblePer" temp_val="${item.graduationResponsiblePer }" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">选择责任人</option>
								</select>	
		            		</div>
		            	</div>
					</div>
				</div>
			</div>
			<div class="row-offset-10px clearfix text-center">
				<div class="box-footer">
					<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel" onclick="history.back()">取消</button>
					<button type="submit" id="btn-submit" class="btn btn-success min-width-90px " data-role="save1">确认发布</button>
				</div>
			</div>
		</div>
	</form>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
/*日期控件*/
$('[data-role="date-group"]').each(function(i,e){
	var startDate=$('[data-role="date-start"]',e);
	var endDate=$('[data-role="date-end"]',e);
	//开始时间			
	startDate.datepicker({
	  language:'zh-CN',
	  format:'yyyy-mm-dd'
	}).on('changeDate', function(e) {
		var add=increaseOnedate(e.target.value);
		endDate.datepicker('setStartDate',add);
	});
	//结束时间
	endDate.datepicker({
	  language:'zh-CN',
	  format:'yyyy-mm-dd'
	}).on('changeDate', function(e) {
		var d=decreaseOnedate(e.target.value);
		startDate.datepicker('setEndDate',d);
	}).on('focus',function(){
		if(this.value==""&&startDate.val()==""){
			startDate.focus();
			endDate.datepicker('hide');
		}
	});
});

$('.role-sel').change(function(){
    var sub_sel=$(this).attr('sub_sel');
    var $sub=$('[name="'+sub_sel+'"]');
    $sub.find('option').not(':first').remove();
    $sub.val('');
    if($(this).val()==''){
		$sub.selectpicker('render');
		$sub.selectpicker('refresh');
    }else{
		$.get('${ctx}/system/user/getUserByRoleId',{roleId:$(this).val()},function(data){
		   if(data){
		       var html = [];
		       for(var key in data){
			  	 html.push('<option value="'+key+'">'+data[key]+'</option>');
		       };
		       $sub.append(html.join(''));
		   }
		   console.log($(this).attr('temp_val'));
		   if($sub.attr('temp_val')){
		       $sub.val($sub.attr('temp_val'));
		       $sub.removeAttr('temp_val');
		   }
		   $sub.selectpicker('render');
		   $sub.selectpicker('refresh');
		},'JSON');
    }
    
});
$(".role-sel").each(function(){
    if($(this).val()){
		$(this).trigger("change",$(this).attr('temp_val'));
    }
});  

//保存
/* $('[data-role="save"]').click(function(event) {
	
	var postIngIframe=$.mydialog({
	  id:'dialog-1',
	  width:150,
	  height:50,
	  backdrop:false,
	  fade:true,
	  showCloseIco:false,
	  zIndex:11000,
	  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	});

	//if("成功提交") 就执行下面语句
    setTimeout(function(){//此句模拟交互，程序时请去掉
      postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>')

      setTimeout(function(){
        $.closeDialog(postIngIframe);
      },2000)
    },2000);//此句模拟交互，程序时请去掉
}); */
</script>
</body>
</html>