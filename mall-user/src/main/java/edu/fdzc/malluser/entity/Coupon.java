package edu.fdzc.malluser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Integer type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private Date startTime;
    private Date endTime;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private Integer status;
    private Long shopId;
}