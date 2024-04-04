
package com.mycompany.imagej;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Add {
    // 定义注解的成员（可以是基本数据类型、字符串、枚举、Class类型，或其他注解类型）
    String overview() default "just add";
    String ToBeMention()default "just add";
    int count() default 0;
}

