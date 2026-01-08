package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.Coupon;
import edu.fdzc.malluser.entity.UserCoupon;
import edu.fdzc.malluser.mapper.CouponMapper;
import edu.fdzc.malluser.mapper.UserCouponMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    public List<Coupon> getAvailableCoupons() {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("end_time", new Date())
                   .gt("remaining_quantity", 0);
        logger.info("获取可用优惠券列表");
        return couponMapper.selectList(queryWrapper);
    }

    public List<UserCoupon> getUserCoupons(Long userId) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("obtain_time");
        // 显式指定查询字段，排除数据库中不存在的use_time和order_id
        queryWrapper.select("id", "user_id", "coupon_id", "obtain_time", "status");
        logger.info("获取用户 {} 的优惠券列表", userId);
        
        // 获取用户优惠券列表
        List<UserCoupon> userCoupons = userCouponMapper.selectList(queryWrapper);
        
        // 为每个用户优惠券加载对应的优惠券详情
        for (UserCoupon userCoupon : userCoupons) {
            Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
            userCoupon.setCoupon(coupon);
        }
        
        return userCoupons;
    }

    public boolean obtainCoupon(Long userId, Long couponId) {
        logger.info("用户 {} 领取优惠券 {}", userId, couponId);
        try {
            Coupon coupon = couponMapper.selectById(couponId);
            LocalDateTime endDateTime = coupon.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (coupon == null || coupon.getRemainingQuantity() <= 0 || endDateTime.isBefore(LocalDateTime.now())) {
                return false;
            }

            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserId(userId);
            userCoupon.setCouponId(couponId);
            userCoupon.setObtainTime(new Date());
            userCoupon.setStatus(1); // 未使用（与前端一致）

            return userCouponMapper.insert(userCoupon) > 0;
        } catch (Exception e) {
            logger.error("领取优惠券失败", e);
            return false;
        }
    }

    // 卖家端优惠券管理方法
    public List<Coupon> getSellerCoupons(Long shopId) {
        logger.info("获取店铺 {} 的优惠券列表", shopId);
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        return couponMapper.selectList(queryWrapper);
    }

    public boolean createCoupon(Coupon coupon) {
        logger.info("创建优惠券: {}", coupon.getName());
        try {
            coupon.setRemainingQuantity(coupon.getTotalQuantity());
            return couponMapper.insert(coupon) > 0;
        } catch (Exception e) {
            logger.error("创建优惠券失败", e);
            return false;
        }
    }

    public boolean updateCoupon(Coupon coupon) {
        logger.info("更新优惠券: {}", coupon.getId());
        try {
            return couponMapper.updateById(coupon) > 0;
        } catch (Exception e) {
            logger.error("更新优惠券失败", e);
            return false;
        }
    }

    public boolean deleteCoupon(Long id) {
        logger.info("删除优惠券: {}", id);
        try {
            return couponMapper.deleteById(id) > 0;
        } catch (Exception e) {
            logger.error("删除优惠券失败", e);
            return false;
        }
    }

    public boolean toggleCouponStatus(Long id, Integer status) {
        logger.info("切换优惠券 {} 状态为: {}", id, status);
        try {
            Coupon coupon = new Coupon();
            coupon.setId(id);
            coupon.setStatus(status);
            return couponMapper.updateById(coupon) > 0;
        } catch (Exception e) {
            logger.error("切换优惠券状态失败", e);
            return false;
        }
    }
}
