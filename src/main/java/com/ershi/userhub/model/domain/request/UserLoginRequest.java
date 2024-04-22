package com.ershi.userhub.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 * @author Eershi
 * @date 2024/04/13
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -2703568123350635806L;

    private String userAccount;

    private String userPassword;
}

