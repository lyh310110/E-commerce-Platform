package edu.fdzc.mallorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fdzc.mallorder.dto.OrderDTO;
import edu.fdzc.mallorder.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 获取商家订单列表
     */
    @Select("SELECT o.*, u.nickname as userNickname, s.shop_name as shopName FROM `order` o JOIN shop s ON o.shop_id = s.id JOIN user u ON o.user_id = u.id WHERE o.shop_id = #{shopId} ORDER BY o.create_time DESC")
    List<OrderDTO> selectShopOrders(@Param("shopId") Long shopId);
}