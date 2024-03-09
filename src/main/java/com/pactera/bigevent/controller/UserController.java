package com.pactera.bigevent.controller;

import com.pactera.bigevent.gen.entity.Result;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.Md5Util;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.pactera.bigevent.gen.entity.RedisDefinition.LOGIN_USER_KEY_PREFIX;
import static com.pactera.bigevent.gen.entity.RedisDefinition.LOGIN_USER_KEY_TIME;

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

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public Result register(@Pattern(regexp = "^[a-zA-Z]{5,16}$") String username, @Pattern(regexp = "^\\w{6,16}$") String password) {
        log.debug("register start ---");
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.error("用户名已存在");
        }
        Integer isInsert = userService.register(username, password);
        if (isInsert != 1) {
            return Result.error("系统错误");
        }
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z]{5,16}$") String username, @Pattern(regexp = "^\\w{6,16}$") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
            return Result.error("密码不正确");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("roleId", 0L);
        String token = jwtUtil.genToken(map);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + user.getId(), token, LOGIN_USER_KEY_TIME, TimeUnit.HOURS);
        return Result.success(token);
    }

    @RequestMapping("/userInfo")
    public Result userInfo() {
        String username = ThreadLocalUserUtil.getUsername();
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        Long id = ThreadLocalUserUtil.getUserId();
        if (!id.equals(user.getId())) {
            return Result.error("更新失败");
        }
        Integer update = userService.updateByUser(user);
        if (update != 1) {
            return Result.error("更新失败");
        }
        return Result.success(user);
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        Integer update = userService.updateAvatar(avatarUrl);
        if (update != 1) {
            return Result.error("更新失败");
        }
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (oldPwd == null || newPwd == null || rePwd == null || oldPwd.isEmpty() || newPwd.isEmpty() || rePwd.isEmpty()) {
            return Result.error("参数不能为空");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码不一致");
        }
        if (oldPwd.equals(newPwd)) {
            return Result.error("新密码和旧密码不能一致");
        }
        String username = ThreadLocalUserUtil.getUsername();
        User user = userService.findByUsername(username);
        if (!Md5Util.getMD5String(oldPwd).equals(user.getPassword())) {
            return Result.error("原密码不正确");
        }
        user.setPassword(Md5Util.getMD5String(newPwd));
        boolean update = userService.updateById(user);
        if (!update) {
            return Result.error("系统错误");
        }
        stringRedisTemplate.opsForValue().getOperations().delete(LOGIN_USER_KEY_PREFIX + user.getId());
        return Result.success();
    }
}
