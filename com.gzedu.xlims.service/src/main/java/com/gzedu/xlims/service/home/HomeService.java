package com.gzedu.xlims.service.home;

import java.util.List;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;
import com.gzedu.xlims.service.vo.Todo;

public interface HomeService {

	public List<Todo> getTodoList(GjtUserAccount user);

	public List<Object[]> getMessageList(GjtUserAccount user);

	public List<GjtWorkOrderAssignPerson> getWorkOrderList(GjtUserAccount user);
}
