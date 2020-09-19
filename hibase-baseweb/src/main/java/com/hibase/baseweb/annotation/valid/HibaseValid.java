package com.hibase.baseweb.annotation.valid;

import java.lang.annotation.*;

/**
 * hibase自动检验
 *
 * @author chenfeng
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HibaseValid {
}
