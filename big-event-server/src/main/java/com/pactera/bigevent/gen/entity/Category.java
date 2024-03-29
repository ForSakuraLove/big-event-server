package com.pactera.bigevent.gen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pactera.bigevent.common.entity.base.BaseEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
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
@TableName("category")
public class Category extends BaseEntity<Category> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 分类名称
     */
    @TableField("category_name")
    @NotEmpty
    private String categoryName;

    /**
     * 分类别名
     */
    @TableField("category_alias")
    @NotEmpty
    private String categoryAlias;

    public interface Add extends Default {

    }

    public interface Update extends Default {

    }
}
