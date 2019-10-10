package com.ken.wms.util.aop;

import java.lang.annotation.*;

/**
 * 用户操作注解
 * 用于标注用户操作的方法名称
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserOperation {
    String value();
}
