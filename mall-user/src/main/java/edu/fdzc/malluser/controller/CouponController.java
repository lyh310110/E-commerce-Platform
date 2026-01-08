package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.Coupon;
import edu.fdzc.malluser.entity.UserCoupon;
import edu.fdzc.malluser.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/available")
    public Result<List<Coupon>> getAvailableCoupons() {
        List<Coupon> coupons = couponService.getAvailableCoupons();
        return Result.success(coupons);
    }

    @GetMapping("/user/list")
    public Result<List<UserCoupon>> getUserCoupons(@RequestParam Long userId) {
        List<UserCoupon> coupons = couponService.getUserCoupons(userId);
        return Result.success(coupons);
    }

    @PostMapping("/obtain")
    public Result<Boolean> obtainCoupon(@RequestParam Long userId, @RequestParam Long couponId) {
        boolean success = couponService.obtainCoupon(userId, couponId);
        return success ? Result.success() : Result.error("领取优惠券失败");
    }

    // 卖家端优惠券管理接口
    @GetMapping("/seller/list")
    public Result<List<Coupon>> getSellerCoupons(@RequestParam Long shopId) {
        List<Coupon> coupons = couponService.getSellerCoupons(shopId);
        return Result.success(coupons);
    }

    @PostMapping("/seller/create")
    public Result<Boolean> createCoupon(@RequestBody Coupon coupon) {
        boolean success = couponService.createCoupon(coupon);
        return success ? Result.success() : Result.error("创建优惠券失败");
    }

    @PostMapping("/seller/update")
    public Result<Boolean> updateCoupon(@RequestBody Coupon coupon) {
        boolean success = couponService.updateCoupon(coupon);
        return success ? Result.success() : Result.error("更新优惠券失败");
    }

    @PostMapping("/seller/delete")
    public Result<Boolean> deleteCoupon(@RequestParam Long id) {
        boolean success = couponService.deleteCoupon(id);
        return success ? Result.success() : Result.error("删除优惠券失败");
    }

    @PostMapping("/seller/toggle-status")
    public Result<Boolean> toggleCouponStatus(@RequestParam Long id, @RequestParam Integer status) {
        boolean success = couponService.toggleCouponStatus(id, status);
        return success ? Result.success() : Result.error("切换优惠券状态失败");
    }
}
