package com.pactera.bigevent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.bigevent.gen.entity.MonthlyVisitors;

/**
 * <p>
 * 每月访问量表 服务类
 * </p>
 *
 * @author zwk
 * @since 2024年03月15日
 */
public interface MonthlyVisitorsService extends IService<MonthlyVisitors> {

    void add();

    MonthlyVisitors get();
}
