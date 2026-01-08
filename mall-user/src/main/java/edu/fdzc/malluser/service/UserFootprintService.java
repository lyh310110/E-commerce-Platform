package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.UserFootprint;
import edu.fdzc.malluser.mapper.UserFootprintMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFootprintService {
    private final UserFootprintMapper userFootprintMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserFootprintService.class);

    public List<UserFootprint> getFootprintsByUserId(Long userId) {
        QueryWrapper<UserFootprint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("visit_time");
        logger.info("获取用户 {} 的足迹列表", userId);
        return userFootprintMapper.selectList(queryWrapper);
    }

    public boolean addFootprint(UserFootprint userFootprint) {
        logger.info("添加足迹：{}", userFootprint);
        try {
            if (userFootprint == null || userFootprint.getUserId() == null || userFootprint.getProductId() == null) {
                logger.error("添加足迹失败，参数不完整");
                return false;
            }

            // 检查是否已存在该用户对该商品的足迹记录
            UserFootprint existingFootprint = getFootprintByUserIdAndProductId(userFootprint.getUserId(), userFootprint.getProductId());
            if (existingFootprint != null) {
                // 如果存在，更新访问时间
                existingFootprint.setVisitTime(new Date());
                logger.info("更新足迹访问时间：{}", existingFootprint);
                return userFootprintMapper.updateById(existingFootprint) > 0;
            } else {
                // 如果不存在，设置访问时间并插入新记录
                userFootprint.setVisitTime(new Date());
                logger.info("插入新足迹记录：{}", userFootprint);
                return userFootprintMapper.insert(userFootprint) > 0;
            }
        } catch (Exception e) {
            logger.error("添加足迹失败", e);
            return false;
        }
    }

    public boolean clearFootprintsByUserId(Long userId) {
        logger.info("清空用户 {} 的足迹列表", userId);
        if (userId == null) {
            logger.error("清空足迹失败，用户id为null");
            return false;
        }
        try {
            QueryWrapper<UserFootprint> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            return userFootprintMapper.delete(queryWrapper) > 0;
        } catch (Exception e) {
            logger.error("清空足迹失败，用户id: {}", userId, e);
            return false;
        }
    }

    /**
     * 根据用户ID和商品ID查询足迹
     */
    private UserFootprint getFootprintByUserIdAndProductId(Long userId, Long productId) {
        QueryWrapper<UserFootprint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("product_id", productId);
        return userFootprintMapper.selectOne(queryWrapper);
    }
}
