package com.pactera.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pactera.bigevent.gen.entity.MonthlyVisitors;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 每月访问量表 Mapper 接口
 * </p>
 *
 * @author zwk
 * @since 2024年03月15日
 */
@Mapper
public interface MonthlyVisitorsMapper extends BaseMapper<MonthlyVisitors> {

}
