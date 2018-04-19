package com.ouchgzee.study.web.controller;

import com.gzedu.xlims.common.HttpClientUtils;

public class Test01 {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1; i++) {
                        HttpClientUtils.doHttpGet("http://localhost:8080/api/home/message/view?studentId=43096d336520467f8e9edaa4d050417b", null, 60000, "UTF-8");
                    }
                }
            }).start();
        }
    }

}
