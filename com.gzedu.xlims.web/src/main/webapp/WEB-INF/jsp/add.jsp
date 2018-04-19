<%@ page contentType="text/html; charset=UTF-8"%>
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
                <form role="form">
                  <div class="box-body">
                    <div class="form-group">
                      <label for="exampleInputPassword1">菜单名称:</label>
	                      <div>
					        <select id="basic" class="selectpicker show-tick form-control">
					          <option>cow</option>
					          <option data-subtext="option subtext">bull</option>
					          <option class="get-class" disabled>ox</option>
					          <optgroup label="test" data-subtext="optgroup subtext">
					            <option>ASD</option>
					            <option selected>Bla</option>
					            <option>Ble</option>
					          </optgroup>
					        </select>
					      </div>
                    </div>
                    <div class="form-group">
                      <label for="exampleInputPassword1">链接地址:</label>
                      <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                    </div>
                    <div class="form-group">
                      <label for="exampleInputPassword1">排序数值:</label>
                      <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                    </div>
                    <div class="form-group">
                      <label for="exampleInputPassword1">是否显示:</label>
                      <label class="checkbox-inline">
					      <input type="radio" name="optionsRadiosinline" id="optionsRadios3" 
					         value="option1" checked> 选项 1
					   </label>
					   <label class="checkbox-inline">
					      <input type="radio" name="optionsRadiosinline" id="optionsRadios4" 
					         value="option2"> 选项 2
					   </label>
                    </div>
                  </div><!-- /.box-body -->

                  <div class="box-footer">
                    <button type="submit" class="btn btn-primary">确定</button>
                    <button type="submit" class="btn btn-primary">取消</button>
                  </div>
                </form>
              </div><!-- /.box -->

            </div><!--/.col (left) -->
            <!-- right column -->
          </div>   <!-- /.row -->
        </section>