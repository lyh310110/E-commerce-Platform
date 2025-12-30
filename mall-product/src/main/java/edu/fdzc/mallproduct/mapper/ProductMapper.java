package edu.fdzc.mallproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fdzc.mallproduct.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends BaseMapper<Product> {
    //basemapper已提供crud方法
}
