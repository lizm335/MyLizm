package org.com.gzedu.xlims.third.moor;

import com.gzedu.xlims.third.moor.MoorApiUtil;
import org.junit.Test;

public class MoorTest {

    @Test
    public void checkTokenLegal() {
        System.out.println(MoorApiUtil.checkTokenLegal("539aeed336dd6482465aaffb79ad0ee2", "26c40120-d4b8-11e7-a0ce-714b02a1d9d3"));
    }
}
