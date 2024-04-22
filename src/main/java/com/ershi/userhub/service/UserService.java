package com.ershi.userhub.service;

import com.ershi.userhub.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ershi.userhub.model.domain.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户服务接口
 * @author Eershi
 * @date 2024/04/13
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 重复检验密码
     * @param invitationCode 邀请码
     * @return long 新用户账号 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String invitationCode);


    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request 请求 request
     * @return {@link UserDTO} 用户登录后信息（脱敏）
     */
    UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 根据用户名查询所有满足条件的用户
     * @param userName 用户名
     * @return {@link List}<{@link UserDTO}> 用户列表
     */
    List<UserDTO> searchUsers(String userName);


    /**
     * 根据 id 删除用户
     * @param id 用户 id
     * @return boolean 删除成功返回 true
     */
    boolean userDelete(long id);


    /**
     * 注销登录
     * @return int 注销成功返回 1, 失败返回 -1
     */
    int userOutLogin(HttpServletRequest request);


    /**
     * 用户信息脱敏
     *
     * @param originUser 未脱敏的用户实体类
     * @return {@link UserDTO} safetyUser
     */
    UserDTO desensitizeUserInfo(User originUser);
}
