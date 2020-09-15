package com.hibase.baseweb.user;

import com.hibase.baseweb.model.User;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求上下文
 *
 * @author hufeng
 * @create 2018-10-08 14:57
 */
@Slf4j
public class UserInfoContext {

    private static ThreadLocal<User> userInfo = new ThreadLocal<User>();

    public UserInfoContext() {
    }

    public static User getUser() {
        log.info("获取用户信息{}", userInfo.get());
        return (User) userInfo.get();
    }

    public static void setUser(User user) {
        userInfo.set(user);
    }

    public static void clear() {

        log.info("清除用户信息{}", userInfo.get());
        if (UserInfoContext.getUser() != null) {

            userInfo.remove();
        }
    }
}
