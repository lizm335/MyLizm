<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">新增菜单</h3>
                </div><!-- /.box-header -->
                <!-- form start -->
                <form role="form" id="inputForm" action="save" method="post">
                  <div class="box-body">
                    <div class="form-group">
                      <label for="exampleInputPassword1">父菜单名称:</label>
	                      <div>
					        <select id="parentId" name="parentId" class="form-control">
					        	<option selected="selected" value="">顶级分类</option>
					        	<c:forEach items="${list}" var="menu">
									<option value="${menu.id}">
										<c:choose>
											<c:when test="${menu.menu_level eq 0}">
												&nbsp;&nbsp;
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${menu.menu_level eq 1}">
														&nbsp;&nbsp;&nbsp;&nbsp;
													</c:when>
													<c:otherwise>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</c:otherwise>
												</c:choose>
												
											</c:otherwise>
										</c:choose>
										${menu.name}
									</option>
								</c:forEach>
					        </select>
					      </div>
                    </div>
                    <div class="form-group">
                      <label for="name">菜单名称:</label>
                      <input type="text" name= "name" class="form-control" id="name"/>
                    </div>
                    <div class="form-group">
                      <label for="url">链接地址:</label>
                      <input type="text" name="url"  class="form-control" id="url"/>
                    </div>
                    <div class="form-group">
                      <label for="orderNo">排序数值:</label>
                      <input type="text" class="form-control" id="orderNo" name="orderNo"/>
                    </div>
                    <div class="form-group">
                      <label for="visible">是否显示:</label>
                      <label class="checkbox-inline">
					      <input type="radio" name="visible" id="optionsRadios3" 
					         value="1" checked/> 是
					   </label>
					   <label class="checkbox-inline">
					      <input type="radio" name="visible" id="optionsRadios4" 
					         value="0"/> 否
					   </label>
                    </div>
                  </div><!-- /.box-body -->

                  <div class="box-footer">
                    <button type="submit" class="btn btn-primary">确定</button>
                    <button type="reset" class="btn btn-primary">取消</button>
                  </div>
                </form>
              </div><!-- /.box -->

            </div><!--/.col (left) -->
            <!-- right column -->
          </div>   <!-- /.row -->
        </section>
        <script type="text/javascript">
	        	$(document).ready(function() {
	        	    $('#inputForm')
	        	        .bootstrapValidator({
	        	            fields: {
	        	            	name: {
	        	                    validators: {
	        	                        notEmpty: "required",
	        	                    }
	        	                },
	        	                url: {
	        	                    validators: {
	        	                        notEmpty: "required",
	        	                    }
	        	                },
	        	                orderNo: {
	        	                    validators: {
	        	                        notEmpty: "required",
	        	                		digits:"required",
	        	                    }
	        	                }
	        	            }
	        	        }).on('success.form.bv', function(e) {
	        	            // Prevent form submission
	        	            e.preventDefault();

	        	            // Get the form instance
	        	            var $form = $(e.target);

	        	            // Get the BootstrapValidator instance
	        	            var bv = $form.data('bootstrapValidator');

	        	            // Use Ajax to submit form data
	        	            $.post($form.attr('action'), $form.serialize(), function(feedback) {
	        	            	alert(feedback.message);
	        					if (feedback.successful) {
	        						window.location.href = ctx + "/module/menu/list";
	        					}
	        	            }, 'json');
	        	        });
	        	});
		</script>
</body>
</html>