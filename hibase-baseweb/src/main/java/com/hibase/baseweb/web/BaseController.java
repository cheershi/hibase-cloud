package com.hibase.baseweb.web;

import com.hibase.baseweb.constant.ResponseCode;
import com.hibase.baseweb.service.BaseService;
import com.hibase.baseweb.utils.SpringContextUtil;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController<T> {

    public static final String GENERATE_CURD_ENTITY = "curd_entity";

    public String getEntityClass(HttpServletRequest request) {

        return request.getHeader(GENERATE_CURD_ENTITY);
    }

    public static BaseService getEntityMapper(String entityClass) {

        String iserviceName = entityClass.replaceAll("entity", "service.impl").replaceAll("dataobject\\.", "") + "Mapper";

        try {

            Class serviceClass = Class.forName(iserviceName);
            BaseService iService = (BaseService) SpringContextUtil.getBean(serviceClass);

            return iService;
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 返回成功
     * @return
     */
    public ResponseModel<T> successMsg() {

        return new ResponseModel<T>();
    }

    /**
     * 返回成功，自定义状态码
     * @return
     */
    public ResponseModel<T> successMsg(String msg, T data) {

        return new ResponseModel<T>(data, ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 返回成功，自定义状态码
     * @return
     */
    public ResponseModel<T> successMsg(T data) {

        return new ResponseModel<T>(data);
    }

    /**
     * 返回成功，自定义状态码
     * @return
     */
    public ResponseModel<T> failMsg(int code, String msg) {

        return new ResponseModel<T>(code, msg);
    }

    /**
     * 返回成功，自定义状态码
     * @return
     */
    public ResponseModel<T> failMsg(int code, String msg, T data) {

        return new ResponseModel<T>(data, code, msg);
    }
}
