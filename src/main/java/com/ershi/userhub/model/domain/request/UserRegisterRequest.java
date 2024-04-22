package com.ershi.userhub.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 * @author Eershi
 * @date 2024/04/13
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7261270495863893183L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String invitationCode;
}
