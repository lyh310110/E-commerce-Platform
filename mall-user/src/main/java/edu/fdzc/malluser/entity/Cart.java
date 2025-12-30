package edu.fdzc.malluser.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cart")
public class Cart {
    private Long id; //购物车id
    private Long userId; //用户id
    private Long productId; //商品id
    private String productName; //商品名称
    private BigDecimal price; //商品价格
    private Integer num; //商品数量
}
