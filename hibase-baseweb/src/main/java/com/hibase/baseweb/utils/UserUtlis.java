package com.hibase.baseweb.utils;

import com.hibase.baseweb.constant.ExceptionCode;
import com.hibase.baseweb.exception.AcquireUserException;
import com.hibase.baseweb.model.User;
import com.hibase.baseweb.user.UserInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户工具类
 *
 * @author hufeng
 * @create 2018-08-31 12:21
 */
@Component
@Slf4j
public class UserUtlis {

    private static String active;

    @Value("${spring.profiles.active}")
    public void setActive(String active) {

        this.active = active;
    }

    /**
     * 获取已登录的用户信息
     *
     * @return
     */
    public static User getLoginUserInfo(boolean throwException) {

        User user = UserInfoContext.getUser();

        if (user == null && (active != null && active.equals("dev"))) {

            user = new User();
            user.setId("1");
            user.setLoginName("admin");
            user.setSysName("admin");
        }

        if (user == null && throwException) {

            throw new AcquireUserException(ExceptionCode.ACQUIRE_USER_EXCEPTION.getCode(), ExceptionCode.ACQUIRE_USER_EXCEPTION.getMessage());
        }

        return user == null ? null : user;
    }

    /**
     * 获取已登录的用户信息
     *
     * @return
     */
    public static User getLoginUserInfo() {

        return getLoginUserInfo(false);
    }
}
