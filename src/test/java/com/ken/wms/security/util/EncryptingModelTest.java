package com.ken.wms.security.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class EncryptingModelTest {

    EncryptingModel encryptingModel = new EncryptingModel();

    @Test
    public void MD5() {
        try {
            System.out.println(encryptingModel.MD5("123456"));//e10adc3949ba59abbe56e057f20f883e
            System.out.println(encryptingModel.MD5("12345678"));//25d55ad283aa400af464c76d713c07ad

            System.out.println(encryptingModel.MD5("123456" + 1001));//905cdf7b9e5b8db762504e1b9aea495e
            System.out.println(encryptingModel.MD5("12345678" + 1001));//19692048ae10dc9eb25ad8d0e3299ae2

            System.out.println(encryptingModel.MD5("e10adc3949ba59abbe56e057f20f883e1001"));//6f5379e73c1a9eac6163ab8eaec7e41c
            System.out.println(encryptingModel.MD5("25d55ad283aa400af464c76d713c07ad1001"));//0ad942712ef7e271d233d2a88a4891ee
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}