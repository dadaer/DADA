package com.example.demo.test;

import com.example.demo.utils.Base64Util;

import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) throws Exception {
        String str = "LY@15295315097";
        System.out.println(Base64Util.encode(str));
        String str1 = Base64Util.encode(str);
        System.out.println(Base64Util.decode("aGoxMzE0MTQ3"));

    }
}
