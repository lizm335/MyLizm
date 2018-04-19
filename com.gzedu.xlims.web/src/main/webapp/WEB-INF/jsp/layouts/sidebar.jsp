<%@ page contentType="text/html; charset=UTF-8"%>
    <aside class="main-sidebar">
        <section class="sidebar">
			<div class="user-panel">
				<div class="pull-left image">
					<img src="${ctx }/static/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
				</div>
				<div class="pull-left info">
					<p>${user.loginAccount }</p>
					<a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
				</div>
			</div>
			<!-- search form (Optional) -->
			<form action="#" method="get" class="sidebar-form">
				<div class="input-group">
					<input type="text" name="q" class="form-control" placeholder="搜索...">
					<span class="input-group-btn">
					<button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
					</span> 
				</div>
			</form>
			<!-- /.search form --> 
          <ul class="sidebar-menu">
			<!-- <li class="header">基础信息</li>
            <li class="treeview">
              <a href="#">
                <i class="fa fa-dashboard"></i> <span>账号管理</span> <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li><a href="p1-1.html"><i class="fa fa-circle-o"></i>学员管理</a></li>
              </ul>
            </li>
            <li class="treeview">
              <a href="#">
                <i class="fa fa-dashboard"></i> <span>院校设定</span> <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li><a href="p1-2.html"><i class="fa fa-circle-o"></i>学期管理</a></li>
              </ul>
            </li>
            <li class="treeview">
              <a href="#">
                <i class="fa fa-dashboard"></i> <span>日志管理</span> <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li><a href="p2-1.html"><i class="fa fa-circle-o"></i>登录日志管理</a></li>
              </ul>
            </li> -->
          </ul>
        </section>
        <!-- /.sidebar -->
      </aside>