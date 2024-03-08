package com.pactera.bigevent.gen;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pactera.bigevent.anno.ArticleState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("article")
public class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章标题
     */
    @TableField("title")
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;

    /**
     * 文章内容
     */
    @TableField("content")
    @NotBlank
    private String content;

    /**
     * 文章封面
     */
    @TableField("cover_img")
    @URL
    private String coverImg;

    /**
     * 文章状态: 只能是[已发布] 或者 [草稿]
     */
    @TableField("state")
    @ArticleState
    private String state;

    /**
     * 文章分类ID
     */
    @TableField("category_id")
    @NotNull
    private Integer categoryId;

    /**
     * 创建人ID
     */
    @TableField("create_user")
    private Integer createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
