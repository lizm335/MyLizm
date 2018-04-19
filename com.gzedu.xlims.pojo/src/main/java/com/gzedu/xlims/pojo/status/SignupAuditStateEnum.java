package com.gzedu.xlims.pojo.status;

/**
 * Created by Administrator on 2016/9/21.
 */
public enum SignupAuditStateEnum {

    不通过(0),通过(1),待审核(3),重提交(2),未提交(4);

    private int value;

    SignupAuditStateEnum(int value) {
        this.value = value;
    }

    public static String getName(int number) {
        for (SignupAuditStateEnum e : SignupAuditStateEnum.values()) {
            if (e.value == number) {
                return e.name();
            }
        }
        return "";
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
