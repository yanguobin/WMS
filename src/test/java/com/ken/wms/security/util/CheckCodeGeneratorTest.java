package com.ken.wms.security.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CheckCodeGeneratorTest {

    CheckCodeGenerator checkCodeGenerator = new CheckCodeGenerator();

    @Test
    public void generlateCheckCode() {
        Map<String, Object> checkCode = checkCodeGenerator.generlateCheckCode();
        System.out.println(checkCode.get("checkCodeString"));
        System.out.println(checkCode.get("checkCodeImage"));
    }
}