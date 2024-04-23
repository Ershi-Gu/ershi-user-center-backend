package com.ershi.userhub.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ershi.userhub.common.ErrorCode;
import com.ershi.userhub.constant.UserConstant;
import com.ershi.userhub.exception.BusinessException;
import com.ershi.userhub.model.domain.User;
import com.ershi.userhub.model.domain.dto.UserDTO;
import com.ershi.userhub.service.UserService;
import com.ershi.userhub.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 用户服务实现类
 *
 * @author Eershi
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 加密盐值，用于混淆加密
     */
    public static final String SALT = "Eershi";

    @Resource
    private UserMapper userMapper;


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String invitationCode) {

        // 校验数据是否为空
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            //todo 用户校验优化
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 校验账户长度
        if (userAccount.length() < 6 || userAccount.length() > 15) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度不符合要求");
        }

        // 校验账号是否包含特殊字符
        Matcher matcher = Pattern.compile(UserConstant.SPECIAL_CHARACTERS).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号包含特殊字符");
        }

        // 校验密码是否不少于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 校验密码与重复校验密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请重复输入正确密码");
        }

        // 检验邀请码
        if (!StringUtils.equalsAny(invitationCode, UserConstant.INVITATION_CODE)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邀请码错误");
        }

        // 校验账号是否有已经存在
        // 查询数据库的操作可以放在最后，防止性能浪费，如果前面的验证都未通过则不用多一次查询。
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        int count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "该账号已存在");
        }

        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 向数据库加入插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        int saveResult = userMapper.insert(user);
        if (saveResult == 0) { // 插入失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "服务器错误请重试");
        }

        // 返回新注册账户 id
        return user.getId();
    }

    @Override
    public UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验数据是否为空
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            //todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 2. 校验账户长度
        if (userAccount.length() < 6 || userAccount.length() > 15) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不符合要求");
        }

        // 3. 校验账号是否包含特殊字符
        Matcher matcher = Pattern.compile(UserConstant.SPECIAL_CHARACTERS).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不符合要求");
        }

        // 4. 密码不少于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 5. 校验加密密码与数据库中的密文密码是否匹配
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        userQueryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);

        if (user == null) { // 用户不存在
            // 记录日志
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }

        //todo 单个账户短时间内多次登录，需要限流

        // 6. 用户信息脱敏返回
        UserDTO userDTO = desensitizeUserInfo(user);

        // 7. 记录用户登录态，存储登录信息到 session
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, userDTO);


        return userDTO;
    }


    @Override
    public List<UserDTO> searchUsers(String userName) {

        // 从数据库中查询并返回数据
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        // 检验数据, 若数据为空则查询所有用户信息
        if (StringUtils.isNotBlank(userName)) {
            userQueryWrapper.like("user_name", userName);
        }

        List<User> users = userMapper.selectList(userQueryWrapper);

        if (users.size() == 0) { // 用户不存在
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }

        // 用户信息脱敏
        ArrayList<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = desensitizeUserInfo(user);
            userDTOs.add(userDTO);
        }


        return userDTOs;
    }


    @Override
    public boolean userDelete(long id) {
        // 验证 id
        if (id <= 0) {
            //todo 用户校验优化
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }

        // 根据 id 删除用户
        int deleteId = userMapper.deleteById(id); // Mybatis 框架会执行逻辑删除
        if (deleteId <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "删除失败，请重试");
        }

        return true;
    }


    @Override
    public int userOutLogin(HttpServletRequest request) {
        // 获取 session
        HttpSession session = request.getSession();
        if (session == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }

        Object userLoginState = session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userLoginState == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "当前未登录");
        }

        // 删除登录态
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }


    /**
     * 用户信息脱敏
     *
     * @param originUser 未脱敏的用户实体类
     * @return {@link User} safetyUser
     */
    @Override
    public UserDTO desensitizeUserInfo(User originUser) {
        if (originUser == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(originUser.getId());
        userDTO.setUserName(originUser.getUserName());
        userDTO.setUserAccount(originUser.getUserAccount());
        userDTO.setAvatarUrl(originUser.getAvatarUrl());
        userDTO.setGender(originUser.getGender());
        userDTO.setPhoneNum(originUser.getPhoneNum());
        userDTO.setEmail(originUser.getEmail());
        userDTO.setUserStatus(originUser.getUserStatus());
        userDTO.setCreateTime(originUser.getCreateTime());
        userDTO.setUpdateTime(originUser.getUpdateTime());
        userDTO.setUserRole(originUser.getUserRole());
        return userDTO;
    }

}





