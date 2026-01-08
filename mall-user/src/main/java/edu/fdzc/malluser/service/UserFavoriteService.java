package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.UserFavorite;
import edu.fdzc.malluser.mapper.UserFavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {
    private final UserFavoriteMapper userFavoriteMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserFavoriteService.class);

    public List<UserFavorite> getFavoritesByUserId(Long userId) {
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        logger.info("获取用户 {} 的收藏列表", userId);
        return userFavoriteMapper.selectList(queryWrapper);
    }

    public boolean addFavorite(UserFavorite userFavorite) {
        logger.info("添加收藏：userFavorite={}, userId={}, productId={}", userFavorite, userFavorite.getUserId(), userFavorite.getProductId());
        try {
            if (userFavorite.getUserId() == null || userFavorite.getProductId() == null) {
                logger.error("添加收藏失败：userId或productId为null, userId={}, productId={}", userFavorite.getUserId(), userFavorite.getProductId());
                return false;
            }
            
            // 检查记录是否已存在
            UserFavorite existingFavorite = getFavoriteByUserIdAndProductId(userFavorite.getUserId(), userFavorite.getProductId());
            if (existingFavorite != null) {
                logger.info("商品已收藏，无需重复添加：userId={}, productId={}", userFavorite.getUserId(), userFavorite.getProductId());
                return true;
            }
            
            userFavorite.setCreateTime(new Date());
            logger.info("设置createTime后：{}", userFavorite);
            int result = userFavoriteMapper.insert(userFavorite);
            logger.info("插入结果：{}", result);
            return result > 0;
        } catch (Exception e) {
            logger.error("添加收藏失败：", e);
            logger.error("异常详情：", e.getCause());
            logger.error("异常堆栈：", e);
            return false;
        }
    }

    public boolean removeFavorite(Long id) {
        logger.info("删除收藏，id: {}", id);
        if (id == null) {
            logger.error("删除收藏失败，id为null");
            return false;
        }
        try {
            return userFavoriteMapper.deleteById(id) > 0;
        } catch (Exception e) {
            logger.error("删除收藏失败，id: {}", id, e);
            return false;
        }
    }

    public boolean checkFavoriteExists(Long userId, Long productId) {
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("product_id", productId);
        return userFavoriteMapper.selectOne(queryWrapper) != null;
    }

    public UserFavorite getFavoriteByUserIdAndProductId(Long userId, Long productId) {
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("product_id", productId);
        return userFavoriteMapper.selectOne(queryWrapper);
    }
}
