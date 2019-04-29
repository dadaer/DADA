package com.example.demo.test;

import com.example.demo.utils.Base64Util;

import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(Base64Util.encode(""));
        System.out.println(Base64Util.decode("Y3NlLjI0NQ=="));

    }
}
