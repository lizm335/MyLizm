package com.gzedu.xlims.third.weixin;

/**
     * 消息模板<br/>
     * %1$s-使用String.format(String format, Object... args)格式化
     */
public enum MessageTemplate {
    TEST("0", "test"),
    // 回复答疑消息模板
    FEEDBACK_REPLY("1", "老师已经回复你的问题，点击(<a href=\\\"%1$s\\\">查看回复内容</a>)可以查看回复内容。");

    private String code;

    private String name;

    MessageTemplate(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static MessageTemplate getItem(String value) {
        for (MessageTemplate item : values()) {
            if (item.getCode().equals(value))
                return item;
        }
        return null;
    }
}