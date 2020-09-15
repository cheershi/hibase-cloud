package com.hibase.baseweb.utils;


import cn.hutool.core.util.StrUtil;
import com.hibase.baseweb.valid.ValidationResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类备注：
 *
 * @author hufeng
 * @version 1.0
 * @date 2018-09-30 21:58
 * @desc
 * @since 1.8
 */

public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (MyUtils.setIsNotNull(set)) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<>();
            for (ConstraintViolation<T> cv : set) {

                if (StrUtil.isEmpty(result.getDefaultMessage())) {

                    result.setDefaultMessage(cv.getMessage());
                }
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (MyUtils.setIsNotNull(set)) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }
}