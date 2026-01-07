package edu.fdzc.mallorder.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String userNickname;
    private Long shopId;
    private String shopName;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private Integer paymentType;
    private LocalDateTime paymentTime;
    private LocalDateTime consignTime;
    private LocalDateTime endTime;
    private LocalDateTime closeTime;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}