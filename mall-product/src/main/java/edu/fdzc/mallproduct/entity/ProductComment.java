package edu.fdzc.mallproduct.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_comment")
public class ProductComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Long userId;
    private String userName;
    private String content;
    private Integer rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}