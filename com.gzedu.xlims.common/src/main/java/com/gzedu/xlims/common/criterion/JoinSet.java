/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import org.hibernate.ejb.criteria.path.AbstractFromImpl;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;

/**
 * 表关联设置<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月23日
 * @version 2.5
 * @since JDK 1.7
 */
public class JoinSet implements Serializable {

    /**
     * 连接方式，默认是INNER连接
     */
    private JoinType joinType = AbstractFromImpl.DEFAULT_JOIN_TYPE;

    /**
     * 关联结果对象
     */
    private Join join;

    public JoinSet() {
    }

    public JoinSet(JoinType joinType) {
        this.joinType = joinType;
    }

    public JoinSet(Join join) {
        this.join = join;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public Join getJoin() {
        return join;
    }

    public void setJoin(Join join) {
        this.join = join;
    }

}
