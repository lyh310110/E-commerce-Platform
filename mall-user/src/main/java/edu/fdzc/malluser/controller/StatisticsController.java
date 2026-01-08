package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取系统统计数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> statistics = statisticsService.getDashboardStatistics();
        return Result.success(statistics);
    }
}