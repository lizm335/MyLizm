<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Main content -->
	<div class="box">
		<div class="box-header with-border">
			<h3 class="box-title">高级搜索</h3>
		</div>
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal clearbox">
				<div class="col-md-4">
					<div class="form-group">
						<label class="control-label col-sm-3">姓名</label>
						<div class="col-sm-9">
							<input type="text" class="form-control">
						</div>
					</div>
				</div>

				<div class="col-md-4">
					<div class="form-group">
						<label class="control-label col-sm-3">日期</label>
						<div class="col-sm-9">
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right reservation">
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="col-md-4 btn-wrap">
						<button class="btn btn-block btn-primary">搜索</button>
					</div>
					<div class="col-md-4 btn-wrap">
						<button class="btn btn-block btn-default">清除</button>
					</div>
					<div class="col-md-4 btn-wrap">
						<div class="search-more-in">
							高级搜索<i class="fa fa-fw fa-angle-up"></i>
						</div>
					</div>
				</div>
			</div>

			<div class="reset-table top-border">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th><input type="checkbox"></th>
							<th>表格列1</th>
							<th>表格列2</th>
							<th>表格列3</th>
							<th>表格列4</th>
							<th>表格列5</th>
							<th>表格列6</th>
							<th>表格列7</th>
							<th>表格列8</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>表格列1</td>
							<td>表格列2</td>
							<td>表格列3</td>
							<td>表格列4</td>
							<td>表格列5</td>
							<td>表格列6</td>
							<td>表格列7</td>
							<td><ul class="data-operion clearbox">
									<li class="operion-item operion-edit"><a href="#"> <i
											class="fa fa-fw fa-edit"></i> 修改
									</a></li>
									<li class="operion-item operion-view"><a href="#"> <i
											class="fa fa-fw fa-files-o"></i> 查看
									</a></li>
									<li class="operion-item operion-del"><a href="#"> <i
											class="fa fa-fw fa-trash-o"></i> 删除
									</a></li>
									<li class="operion-item operion-more">
										<div class="dropdown yahei">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												更多 <span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项一</a></li>
												<li role="presentation"><a role="menuitem"
													tabindex="-1" href="#">选项二</a></li>
											</ul>
										</div>
									</li>
								</ul></td>
						</tr>
					</tbody>
				</table>
			</div>
			<!--分页-->
			<div class="paging fr">
				<a href="javascript:;" class="disabled"> &lt;上一页 </a><a
					href="javascript:;" class="current">1</a><a href="javascript:;">2</a><a
					href="javascript:;">3</a><a href="javascript:;">4</a><a href="#">5</a>...<a
					href="javascript:;"> 下一页&gt; </a><span class="left10">共 100
					页，到第 <input type="text"> 页<a href="javascript:;"
					class="left5">确定</a>
				</span>
			</div>
			<!--/.分页-->
		</div>
	</div>
