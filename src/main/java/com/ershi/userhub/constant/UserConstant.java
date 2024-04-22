package com.ershi.userhub.constant;

/**
 * 用户常量 <br>
 * 接口中的属性都是 public static final
 * @author Eershi
 * @date 2024/04/14
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    // ------ 权限 ------
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;


    //todo 正则优化
    /**
     *验证不含特殊字符的账号正则
     */
    String SPECIAL_CHARACTERS = "^(?![A-Za-z0-9]{4,40}$).*$";


    /**
     *默认用户名
     */
    String DEFAULT_USERNAME = "Hello";

    /**
     *默认头像地址
     */
    String DEFAULT_AVATAR_URL = "https://note-qingtian.oss-cn-hangzhou.aliyuncs.com/default-user.jpg";


    /**
     *邀请码匹配列表
     */
    String[] INVITATION_CODE = {"Ershi111"};
}
