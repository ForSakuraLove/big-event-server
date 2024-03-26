package com.pactera.bigevent.controller;

import com.pactera.bigevent.common.entity.base.Result;
import com.pactera.bigevent.gen.entity.MonthlyVisitors;
import com.pactera.bigevent.service.MonthlyVisitorsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 每月访问量表 前端控制器
 * </p>
 *
 * @author zwk
 * @since 2024年03月15日
 */
@RestController
@RequestMapping("/monthlyVisitors")
public class MonthlyVisitorsController {

    @Resource
    private MonthlyVisitorsService monthlyVisitorsService;

    @PostMapping
    public Result add() {
        monthlyVisitorsService.add();
        return Result.success("访问量加一");
    }

    @GetMapping
    public Result get() {
        MonthlyVisitors monthlyVisitors = monthlyVisitorsService.get();
        return Result.success("获取访问量成功", monthlyVisitors);
    }

}
