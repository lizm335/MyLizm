package com.gzedu.xlims.pojo.status;

public class FeedbackEnum {

    public enum Type {

        SUBJECT("反馈", "feedback"), REPLY("回复", "reply");

        private String name;
        private String value;

        private Type(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static enum Source {

        PC("PC", "PC"), PHONE("手机", "PHONE"), PAD("平板", "PAD");

        private String name;
        private String value;

        private Source(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static enum Role {

        STUDENT("学员", "student"), HEADTEACHER("班主任", "headteacher");

        private String name;
        private String value;

        private Role(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static enum DealResult {

        FINISHED("已完成", "finished"), UNFINISH("未完成", "unfinish");

        private String name;
        private String value;

        private DealResult(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
