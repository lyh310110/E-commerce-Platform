package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.Cart;
import edu.fdzc.malluser.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public List<Cart> getCartByUserId(Long userId){
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        logger.info("获取用户 {} 的购物车列表", userId);
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        logger.info("获取到购物车列表：{}", cartList);
        return cartList;
    }

    public boolean addCart(Cart cart){
        logger.info("添加购物车：{}", cart);
        boolean success = cartMapper.insert(cart) > 0;
        logger.info("添加购物车结果：{}", success);
        return success;
    }

    public boolean deleteCart(String id){
        logger.info("开始删除购物车项，id: {}", id);
        logger.info("id的类型: {}", id != null ? id.getClass().getName() : "null");
        
        if (id == null || id.isEmpty()) {
            logger.error("删除购物车失败，id为null或空");
            return false;
        }
        
        try {
            int result = cartMapper.deleteById(id);
            logger.info("删除购物车操作结果，影响行数: {}", result);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除购物车项失败，id: {}", id, e);
            return false;
        }
    }
}
