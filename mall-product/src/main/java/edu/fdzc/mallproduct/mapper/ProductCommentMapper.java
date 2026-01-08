package edu.fdzc.mallproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fdzc.mallproduct.entity.ProductComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductCommentMapper extends BaseMapper<ProductComment> {
    @Select("SELECT * FROM product_comment WHERE product_id = #{productId} ORDER BY create_time DESC")
    List<ProductComment> getCommentsByProductId(@Param("productId") Long productId);
}