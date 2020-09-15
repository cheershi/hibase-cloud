package com.hibase.baseweb.valid;


import cn.hutool.core.util.StrUtil;
import com.hibase.baseweb.utils.MyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检验是否可以转为日期实现类
 *
 * @author admin
 * @create 2018-08-20 14:34
 */
public class CheckFormatDateValidate implements ConstraintValidator<CheckFormatDate, String> {

    private CheckFormatDate checkFormatDate;

    @Override
    public void initialize(CheckFormatDate constraintAnnotation) {

        this.checkFormatDate = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StrUtil.isEmpty(value)) {

            return true;
        }

        if (MyUtils.isValidDate(value, checkFormatDate.value()) != null) {

            return true;
        }

        return false;
    }
}
