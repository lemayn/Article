package com.example.leon.article;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        String s = convert("注册失败，请重试");
        System.out.println(s);
        System.out.println("\u6ce8\u518c\u5931\u8d25\uff0c\u8bf7\u91cd\u8bd5");
    }

    private static String convert(String str) {
        String result = "";
        for(int i = 0; i < str.length(); i++) {
            String temp = "";
            int strInt = str.charAt(i);
            if(strInt > 127) {
                temp += "\\u" + Integer.toHexString(strInt);
            } else {
                temp = String.valueOf(str.charAt(i));
            }
            result += temp;
        }
        return result;
    }
}