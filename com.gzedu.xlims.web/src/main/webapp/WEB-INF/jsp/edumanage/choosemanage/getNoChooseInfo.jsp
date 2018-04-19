<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-bordered table-striped vertical-mid text-center table-font">
	<thead>
		<tr>
			<th width="80px"><input type="checkbox" class="select-all">选课</th>
			<th>学号</th>
			<th>姓名</th>
			<th>报读信息</th>
			<th>选课信息</th>
			<th>选课类型</th>
			<th>选课状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${chooseList }" var="entity">
		<tr>
			<td>
				<input type="checkbox" name="ids" data-id="" data-name="check-id" value="${entity.STUDENT_ID },${entity.TEACH_PLAN_ID },${entity.TERMCOURSE_ID },${entity.TERM_ID },${entity.COURSE_ID }">
			</td>
			<td>${entity.XH }</td>
			<td>${entity.XM }</td>
			<td>
				<div class="text-left">
					专业层次：${entity.PYCC_NAME }<br>
					入学学期：${entity.GRADE_NAME }<br>
					报读专业：${entity.ZYMC }
				</div>
			</td>
			<td>
				<div class="text-left">
					开课学期：${entity.OPEN_GRADE_NAME }<br>
					课程代码：${entity.KCH }<br>
					课程名称：${entity.KCMC }<br>
				</div>
			</td>
			<td>
				新增
			</td>
			<td>
				<span class="text-orange">未选课</span><br>
				<span class="gray9">(未同步)</span>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<script type="text/javascript">
	
</script>