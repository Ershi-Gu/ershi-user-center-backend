package com.ershi.userhub.service;

import com.ershi.userhub.model.domain.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * 用户服务测试
 *
 * @author Eershi
 * @date 2024/04/13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Resource
    UserService userService;


    @Test
    public void testAddUser() {

        User user = new User();
        user.setUserName("Eershi");
        user.setUserAccount("123");
        user.setUserPassword("666");
        user.setAvatarUrl("https://images.zsxq.com/FmrZfeGS00r9iIo_EtR2KXHsOBF2?" +
                "e=1714492799&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:9ZUvfGFiMivFgUTnIpOb3dH2iqE=");
        user.setGender(0);
        user.setPhoneNum("123");
        user.setEmail("xxx");

        boolean result = userService.save(user);
        System.out.println("userid = " + user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void userRegister() {
        String userAccount = "2402554312";
        String userPassword = "";
        String checkPassword = "200303078";
        String invitationCode = "Ershi111";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        Assertions.assertEquals(-1, result);

        userAccount = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        Assertions.assertEquals(-1, result);

        userAccount = "21 #31";
        result = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        Assertions.assertEquals(-1, result);

        userPassword = "123";
        checkPassword = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        Assertions.assertEquals(-1, result);

        userAccount = "2222 444";
        userPassword = "200303078";
        checkPassword = "200303078";
        result = userService.userRegister(userAccount, userPassword, checkPassword, invitationCode);
        Assertions.assertEquals(-1, result);
    }
}