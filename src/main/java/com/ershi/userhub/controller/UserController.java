package com.ershi.userhub.controller;


import com.ershi.userhub.common.BaseResponse;
import com.ershi.userhub.common.ErrorCode;
import com.ershi.userhub.constant.UserConstant;
import com.ershi.userhub.exception.BusinessException;
import com.ershi.userhub.model.domain.User;
import com.ershi.userhub.model.domain.dto.UserDTO;
import com.ershi.userhub.model.domain.request.UserDeleteRequest;
import com.ershi.userhub.model.domain.request.UserLoginRequest;
import com.ershi.userhub.model.domain.request.UserRegisterRequest;
import com.ershi.userhub.model.domain.request.UserSearchRequest;
import com.ershi.userhub.service.UserService;
import com.ershi.userhub.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author Eershi
 * @date 2024/04/13
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) { // 接收到空请求
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 基础校验
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String invitationCode = userRegisterRequest.getInvitationCode();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, invitationCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long registerResult = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        return ResponseUtils.success(registerResult, "注册成功");

    }

    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) { // 接收到空请求
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 基础校验
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            return ResponseUtils.error(ErrorCode.PARAMS_ERROR);
        }

        UserDTO loginUserDTO = userService.userLogin(userAccount, userPassword, request);

        return ResponseUtils.success(loginUserDTO, "登录成功");

    }


    /**
     * 获取用户登录态接口
     *
     * @param request http 请求
     * @return {@link UserDTO} 当前用户信息实体类
     */
    @GetMapping("/current")
    public BaseResponse<UserDTO> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        UserDTO currentUser = (UserDTO) userObj;
        if (currentUser == null) {
            return ResponseUtils.error(ErrorCode.NO_AUTH, "请先登录");
        }
        Long id = currentUser.getId();

        // 从数据库查询最新的用户信息
        User newCurrentUser = userService.getById(id);
        if (newCurrentUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "无法获取当前在线用户信息");
        }

        currentUser = userService.desensitizeUserInfo(newCurrentUser);

        //todo 校验用户是否合法

        return ResponseUtils.success(currentUser, "获取当前用户登录态成功");
    }


    @GetMapping("/search")
    public BaseResponse<List<UserDTO>> searchUsers(UserSearchRequest userSearchRequest, HttpServletRequest request) {
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理权限");
        }
        List<UserDTO> userDTOS = userService.searchUsers(userSearchRequest.getUserName());
        return ResponseUtils.success(userDTOS, "查询成功");
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理权限");
        }

        if (userDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");

        }

        // 基础校验
        Long id = userDeleteRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "无指定对象");
        }

        boolean deleteResult = userService.userDelete(id);

        return ResponseUtils.success(deleteResult,"删除指定对象成功");
    }


    @PostMapping(value = "/outLogin")
    public BaseResponse<Integer> userOutLogin(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求为空");
        }

        int outLoginResult = userService.userOutLogin(request);

        return ResponseUtils.success(outLoginResult,"注销成功");
    }


    /**
     * 判断权限是否为管理员
     *
     * @return boolean 管理员-true，其余返回 fasle
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            return false;
        }
        UserDTO loginUser = (UserDTO) userObj;
        return loginUser.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
