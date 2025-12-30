package edu.fdzc.malluser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.malluser.entity.Cart;
import edu.fdzc.malluser.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public List<Cart> getCartByUserId(Long userId){
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return cartMapper.selectList(queryWrapper);
    }

    public boolean addCart(Cart cart){
        return cartMapper.insert(cart) > 0;
    }

    public boolean deleteCart(Long id){
        return cartMapper.deleteById(id) > 0;
    }
}
