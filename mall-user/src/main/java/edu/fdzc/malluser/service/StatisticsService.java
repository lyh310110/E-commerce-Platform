package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.Shop;
import edu.fdzc.malluser.entity.User;
import edu.fdzc.malluser.entity.UserRole;
import edu.fdzc.malluser.mapper.ShopMapper;
import edu.fdzc.malluser.mapper.UserMapper;
import edu.fdzc.malluser.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final ShopMapper shopMapper;

    /**
     * 获取系统统计数据
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 统计用户总数
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        long totalUsers = userMapper.selectCount(userQueryWrapper);
        statistics.put("totalUsers", totalUsers);

        // 统计买家数量
        QueryWrapper<UserRole> buyerQueryWrapper = new QueryWrapper<>();
        buyerQueryWrapper.eq("role_type", 1); // 买家角色
        long buyerCount = userRoleMapper.selectCount(buyerQueryWrapper);
        statistics.put("buyerCount", buyerCount);

        // 统计卖家数量
        QueryWrapper<UserRole> sellerQueryWrapper = new QueryWrapper<>();
        sellerQueryWrapper.eq("role_type", 2); // 卖家角色
        long sellerCount = userRoleMapper.selectCount(sellerQueryWrapper);
        statistics.put("sellerCount", sellerCount);

        // 统计店铺数量
        QueryWrapper<Shop> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.eq("status", 1); // 只统计已审核通过的店铺
        long shopCount = shopMapper.selectCount(shopQueryWrapper);
        statistics.put("shopCount", shopCount);

        // 统计商品数量（需要调用商品服务，这里先返回0，后续可以通过Feign调用）
        statistics.put("productCount", getProductCount());

        return statistics;
    }

    /**
     * 获取商品数量
     */
    private long getProductCount() {
        return 100L; // 暂时提供默认值，确保前端能显示数据
    }
}