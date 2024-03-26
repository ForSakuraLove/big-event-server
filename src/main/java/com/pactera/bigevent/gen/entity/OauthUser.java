package com.pactera.bigevent.gen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pactera.bigevent.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 第三方授权用户表
 * </p>
 *
 * @author zwk
 * @since 2024年03月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("oauth_user")
public class OauthUser extends BaseEntity<OauthUser> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 授权方提供的id
     */
    @TableField("third_account_id")
    private String thirdAccountId;

    /**
     * 第三方登录名称
     */
    @TableField("platform_name")
    private String platformName;

}
