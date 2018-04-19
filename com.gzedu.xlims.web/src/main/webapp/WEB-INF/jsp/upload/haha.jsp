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
})
</script> 

</head>
<body class="inner-page-body">
<section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="box box-primary">
                <div class="box-header with-border"> 
                  <h3 class="box-title">操作管理</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/export/testImportExcel" method="post"
                enctype="multipart/form-data"  >
                <input  type="file" name="file" />
                  <div class="box-footer">
                  	<div class="col-sm-offset-1 col-sm-11">
	                    <button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
                  	</div>
                  </div>
                  </form>
              </div>
            </div>
          </div>  
</section>
</body>
</html>