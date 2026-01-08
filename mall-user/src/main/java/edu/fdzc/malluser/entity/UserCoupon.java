package edu.fdzc.malluser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_coupon")
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long couponId;
    private Date obtainTime;
    
    @TableField(exist = false)
    private Date useTime;
    
    @TableField(exist = false)
    private Long orderId;
    
    private Integer status;
    
    @TableField(exist = false)
    private Coupon coupon;
}