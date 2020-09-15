package com.hibase.baseweb.valid;


import com.hibase.baseweb.utils.MyUtils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 检查是否可以转为日期
 *
 * @author admin
 * @create 2018-08-20 14:27
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckFormatDateValidate.class)
public @interface CheckFormatDate {

    String message() default "日期格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default MyUtils.FORMAT_YYYY_MM_DD;
}
