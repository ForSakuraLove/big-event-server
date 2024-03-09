package com.pactera.bigevent.gen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pactera.bigevent.common.entity.base.BaseEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity<User> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    @TableField("nickname")
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;

    /**
     * 邮箱
     */
    @TableField("email")
    @Email
    private String email;

    /**
     * 账号状态（0：正常 1：停用）
     */
    @TableField("status")
    private String status;

    /**
     * 手机号
     */
    @TableField("phoneNumber")
    private String phoneNumber;

    /**
     * 用户性别（0：男 1：女 2：未定义）
     */
    @TableField("sex")
    private String sex;

    /**
     * 头像
     */
    @TableField("user_pic")
    private String userPic;

}
