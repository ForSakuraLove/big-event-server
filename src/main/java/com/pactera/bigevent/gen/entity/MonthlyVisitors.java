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
import java.time.LocalDate;

/**
 * <p>
 * 每月访问量表
 * </p>
 *
 * @author zwk
 * @since 2024年03月15日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("monthly_visitors")
public class MonthlyVisitors extends BaseEntity<MonthlyVisitors> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 月份
     */
    @TableField("month_date")
    private LocalDate monthDate;

    /**
     * 访问量
     */
    @TableField("visitors_count")
    private Integer visitorsCount;

}
