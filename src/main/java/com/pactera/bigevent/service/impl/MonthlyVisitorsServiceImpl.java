package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.gen.entity.MonthlyVisitors;
import com.pactera.bigevent.mapper.MonthlyVisitorsMapper;
import com.pactera.bigevent.service.MonthlyVisitorsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 每月访问量表 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年03月15日
 */
@Service
public class MonthlyVisitorsServiceImpl extends ServiceImpl<MonthlyVisitorsMapper, MonthlyVisitors> implements MonthlyVisitorsService {

    @Resource
    private MonthlyVisitorsMapper monthlyVisitorsMapper;

    @Override
    public void add() {
        MonthlyVisitors monthlyVisitors = getMonthlyVisitors();
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        if (monthlyVisitors == null) {
            monthlyVisitors = new MonthlyVisitors();
            monthlyVisitors.setMonthDate(firstDay);
            monthlyVisitorsMapper.insert(monthlyVisitors);
            return;
        }
        monthlyVisitors.setVisitorsCount(monthlyVisitors.getVisitorsCount() + 1);
        monthlyVisitorsMapper.updateById(monthlyVisitors);
    }

    @Override
    public MonthlyVisitors get() {
        return getMonthlyVisitors();
    }

    private MonthlyVisitors getMonthlyVisitors() {
        QueryWrapper<MonthlyVisitors> queryWrapper = new QueryWrapper<>();
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        queryWrapper.lambda()
                .eq(MonthlyVisitors::getMonthDate, firstDay);
        return monthlyVisitorsMapper.selectOne(queryWrapper);
    }

}
