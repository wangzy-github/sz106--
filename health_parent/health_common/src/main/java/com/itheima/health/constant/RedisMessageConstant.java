package com.itheima.health.constant;

public interface RedisMessageConstant {
    /**
     * 用于缓存体检预约时发送的验证码
     */
    static final String SENDTYPE_ORDER = "001_";
    /**
     * 用于缓存手机号快速登录时发送的验证码
     */
    static final String SENDTYPE_LOGIN = "002_";
    /**
     * 用于缓存找回密码时发送的验证码
     */
    static final String SENDTYPE_GETPWD = "003_";
}