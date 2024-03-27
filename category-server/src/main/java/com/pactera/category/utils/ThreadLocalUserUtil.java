package com.pactera.category.utils;

import com.pactera.category.common.entity.CurrentUserContext;

import java.util.List;

/**
 * ThreadLocal 工具类
 */
@SuppressWarnings("all")
public class ThreadLocalUserUtil {
    //提供ThreadLocal对象,
    private static final ThreadLocal<CurrentUserContext> currentUserThreadLocal = new ThreadLocal();

    public static void setCurrentUserContext(CurrentUserContext currentUser) {
        currentUserThreadLocal.set(currentUser);
    }

    public static CurrentUserContext getCurrentUserContext() {
        return currentUserThreadLocal.get();
    }


    public static Long getUserId() {
        CurrentUserContext currentUser = getCurrentUserContext();
        return currentUser != null ? currentUser.getUserId() : null;
    }

    public static List<String> getRoleNames() {
        CurrentUserContext currentUser = getCurrentUserContext();
        return currentUser != null ? currentUser.getRoleNames() : null;
    }

    public static String getUsername() {
        CurrentUserContext currentUser = getCurrentUserContext();
        return currentUser != null ? currentUser.getUsername() : null;
    }

    //清除ThreadLocal 防止内存泄漏
    public static void remove() {
        currentUserThreadLocal.remove();
    }
}
