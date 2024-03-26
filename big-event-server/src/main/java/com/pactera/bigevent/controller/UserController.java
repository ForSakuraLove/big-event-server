package com.pactera.bigevent.controller;

import com.pactera.bigevent.common.entity.base.Result;
import com.pactera.bigevent.common.entity.constants.ErrorMessageConst;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.Md5Util;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static com.pactera.bigevent.common.entity.constants.RedisDefinition.LOGIN_USER_KEY_PREFIX;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Validated
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("register")
    public Result register(@Pattern(regexp = "^[a-zA-Z]{5,16}$") String username, @Pattern(regexp = "^\\w{6,16}$") String password) {
        log.debug("register start ---");
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.error(ErrorMessageConst.USERNAME_ALREADY_EXISTS);
        }
        Integer isInsert = userService.register(username, password);
        if (isInsert != 1) {
            return Result.error(ErrorMessageConst.INTERNAL_SERVER_ERROR);
        }
        return Result.success("注册成功");
    }

    @RequestMapping("/userInfo")
    public Result userInfo() {
        String username = ThreadLocalUserUtil.getUsername();
        User user = userService.findByUsername(username);
        return Result.success("获取用户信息成功", user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        Long id = ThreadLocalUserUtil.getUserId();
        if (!Objects.equals(id, user.getId())) {
            return Result.error(ErrorMessageConst.UPDATE_FAILED);
        }
        Integer update = userService.updateByUser(user);
        if (update != 1) {
            return Result.error(ErrorMessageConst.UPDATE_FAILED);
        }
        return Result.success("更新用户成功", user);
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        Integer update = userService.updateAvatar(avatarUrl);
        if (update != 1) {
            return Result.error(ErrorMessageConst.UPDATE_FAILED);
        }
        return Result.success("更新头像成功");
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (oldPwd == null || newPwd == null || rePwd == null || oldPwd.isEmpty() || newPwd.isEmpty() || rePwd.isEmpty()) {
            return Result.error(ErrorMessageConst.PARAMETER_CANNOT_BE_EMPTY);
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error(ErrorMessageConst.TWO_PASSWORDS_ARE_INCONSISTENT);
        }
        if (oldPwd.equals(newPwd)) {
            return Result.error(ErrorMessageConst.THE_OLD_AND_NEW_PASSWORDS_CANNOT_BE_CONSISTENT);
        }
        String username = ThreadLocalUserUtil.getUsername();
        User user = userService.findByUsername(username);
        if (!Md5Util.getMD5String(oldPwd).equals(user.getPassword())) {
            return Result.error(ErrorMessageConst.THE_ORIGINAL_PASSWORD_IS_INCORRECT);
        }
        user.setPassword(Md5Util.getMD5String(newPwd));
        boolean update = userService.updateById(user);
        if (!update) {
            return Result.error(ErrorMessageConst.INTERNAL_SERVER_ERROR);
        }
        stringRedisTemplate.opsForValue().getOperations().delete(LOGIN_USER_KEY_PREFIX + user.getId());
        return Result.success("更新密码成功");
    }

    @GetMapping("/systemManage/getAllUsers")
    public Result getAllUsers() {
        return Result.success("获取用户列表成功", userService.list());
    }
}
