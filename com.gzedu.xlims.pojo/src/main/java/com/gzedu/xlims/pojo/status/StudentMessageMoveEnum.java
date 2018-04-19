package com.gzedu.xlims.pojo.status;

/**
 * 学员信息更正类别
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月7日
 * @version 2.5
 */
public enum StudentMessageMoveEnum {

    性别民族变更(0),入学文化程度更变(1),姓名变更(2),身份证变更(3);

    private int value;

    StudentMessageMoveEnum(int value) {
        this.value = value;
    }

    public static String getName(int number) {
        for (StudentMessageMoveEnum e : StudentMessageMoveEnum.values()) {
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
