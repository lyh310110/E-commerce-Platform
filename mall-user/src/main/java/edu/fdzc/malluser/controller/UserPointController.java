package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.PointRecord;
import edu.fdzc.malluser.entity.UserPoint;
import edu.fdzc.malluser.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class UserPointController {
    private final UserPointService userPointService;

    @GetMapping("/user")
    public Result<UserPoint> getUserPoint(@RequestParam Long userId) {
        UserPoint userPoint = userPointService.getUserPoint(userId);
        return Result.success(userPoint);
    }

    @PostMapping("/add")
    public Result<Boolean> addPoints(@RequestParam Long userId, @RequestParam int points, @RequestParam String reason) {
        boolean success = userPointService.addPoints(userId, points, reason);
        return success ? Result.success() : Result.error("增加积分失败");
    }

    @PostMapping("/deduct")
    public Result<Boolean> deductPoints(@RequestParam Long userId, @RequestParam int points, @RequestParam String reason) {
        boolean success = userPointService.deductPoints(userId, points, reason);
        return success ? Result.success() : Result.error("扣除积分失败");
    }

    @GetMapping("/records")
    public Result<List<PointRecord>> getPointRecords(@RequestParam Long userId) {
        List<PointRecord> records = userPointService.getPointRecords(userId);
        return Result.success(records);
    }
}
