package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.Shop;
import edu.fdzc.malluser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final UserService userService;

    /**
     * 申请开店
     */
    @PostMapping("/apply")
    public Result<Boolean> applyShop(@RequestBody Shop shop) {
        boolean success = userService.applyShop(shop);
        return success ? Result.success() : Result.error("开店申请失败");
    }

    /**
     * 获取店铺信息
     */
    @GetMapping("/info")
    public Result<Shop> getShopInfo(@RequestParam Long userId) {
        Shop shop = userService.getShopByUserId(userId);
        return shop != null ? Result.success(shop) : Result.error("店铺不存在");
    }

    /**
     * 更新店铺信息
     */
    @PutMapping("/update")
    public Result<Boolean> updateShop(@RequestBody Shop shop) {
        boolean success = userService.updateShop(shop);
        return success ? Result.success() : Result.error("更新失败");
    }
}