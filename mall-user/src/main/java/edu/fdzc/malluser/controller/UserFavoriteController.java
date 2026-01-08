package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.UserFavorite;
import edu.fdzc.malluser.service.UserFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class UserFavoriteController {
    private final UserFavoriteService userFavoriteService;

    @GetMapping("/list")
    public Result<List<UserFavorite>> getFavoritesByUserId(@RequestParam Long userId) {
        List<UserFavorite> favorites = userFavoriteService.getFavoritesByUserId(userId);
        return Result.success(favorites);
    }

    @PostMapping("/add")
    public Result<Boolean> addFavorite(@RequestBody UserFavorite userFavorite) {
        boolean success = userFavoriteService.addFavorite(userFavorite);
        return success ? Result.success() : Result.error("添加收藏失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> removeFavorite(@PathVariable Long id) {
        boolean success = userFavoriteService.removeFavorite(id);
        return success ? Result.success() : Result.error("删除收藏失败");
    }

    @GetMapping("/check")
    public Result<UserFavorite> checkFavoriteExists(@RequestParam Long userId, @RequestParam Long productId) {
        UserFavorite favorite = userFavoriteService.getFavoriteByUserIdAndProductId(userId, productId);
        return Result.success(favorite);
    }
}
