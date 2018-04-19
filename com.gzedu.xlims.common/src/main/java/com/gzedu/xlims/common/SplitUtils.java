package com.gzedu.xlims.common;


import java.util.StringTokenizer;

/**
 * 字符串分割
 */
public class SplitUtils {

    /**
     * 字符串分割
     * @param split 需分割的字符串
     * @param splitFlag 分割标识
     * @return 返回分割后的字符串数组
     */
    public static String[] split(String split,String splitFlag){
        if (split == null || splitFlag == null){
            String[] tSplit = new String[1];
            tSplit[0] = split;
            return  tSplit;
        }

        StringTokenizer st = new StringTokenizer(split,splitFlag);
        int size = st.countTokens();
        String[] tSplit = new String[size];
        for (int i = 0; i < size; i++) {
            tSplit[i] = st.nextToken();
        }
        return tSplit;
    }


    public static void main(String[] args) {
        String str = "a,b,c";
        String[] s = split(str,",");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }

    }

}
