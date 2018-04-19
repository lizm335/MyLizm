package com.gzedu.xlims.common.criterion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Test {

    public static void main(String[] args) {
        /**
        Criteria<Event> c = new Criteria<Event>();
        c.setJoinType("priRoleInfo.priModelInfos.priOperateInfos", JoinType.LEFT);

        c.add(Restrictions.like("code", searchParam.getCode(), true));
        c.add(Restrictions.eq("flowStatus", searchParam.getFlowStatus(), true));
        c.add(Restrictions.lte("submitTime", searchParam.getStartSubmitTime(), true));
        c.add(Restrictions.gte("submitTime", searchParam.getEndSubmitTime(), true));
        c.add(Restrictions.ne("flowStatus", CaseConstants.CASE_STATUS_DRAFT, true));

        List<String> teamCodes = new ArrayList<String>();
        teamCodes.add("34818da4ac10a5730194b85fe66f1922");
        teamCodes.add("34803b6aac10a5730194b85f743cc1b3");
        c.add(Restrictions.in("solveTeam.code",teamCodes, true));
        eventDao.findAll(c);
         */
    	
    	
    	
    	
    	try {
			FileInputStream input = new FileInputStream(
					"C:\\Users\\admin\\Desktop\\NewFile2");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while(reader.ready()) {
				String line = reader.readLine();
				if(line == null) {
					break;
				}
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
