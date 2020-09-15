package com.hibase.baseweb.utils.excel;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsGenColumnExclude {
}
