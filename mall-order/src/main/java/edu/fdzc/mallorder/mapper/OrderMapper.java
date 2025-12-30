package edu.fdzc.mallorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fdzc.mallorder.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}