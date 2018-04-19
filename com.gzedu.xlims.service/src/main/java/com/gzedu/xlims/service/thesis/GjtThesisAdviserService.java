package com.gzedu.xlims.service.thesis;

import java.util.Set;

public interface GjtThesisAdviserService {
	
	public void deleteByArrangeIdAndAdviserTypes(String arrangeId, Set<Integer> dviserTypes);

}
