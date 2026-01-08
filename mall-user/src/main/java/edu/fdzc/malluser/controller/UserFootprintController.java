package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.UserFootprint;
import edu.fdzc.malluser.service.UserFootprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/footprint")
@RequiredArgsConstructor
public class UserFootprintController {
    private final UserFootprintService userFootprintService;

    @GetMapping("/list")
    public Result<List<UserFootprint>> getFootprintsByUserId(@RequestParam Long userId) {
        List<UserFootprint> footprints = userFootprintService.getFootprintsByUserId(userId);
        return Result.success(footprints);
    }

    @PostMapping("/add")
    public Result<Boolean> addFootprint(@RequestBody UserFootprint userFootprint) {
        boolean success = userFootprintService.addFootprint(userFootprint);
        return success ? Result.success() : Result.error("添加足迹失败");
    }

    @DeleteMapping("/clear")
    public Result<Boolean> clearFootprintsByUserId(@RequestParam Long userId) {
        boolean success = userFootprintService.clearFootprintsByUserId(userId);
        return success ? Result.success() : Result.error("清空足迹失败");
    }
}
