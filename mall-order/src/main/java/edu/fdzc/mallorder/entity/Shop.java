package edu.fdzc.mallorder.entity;

import lombok.Data;

/**
 * 店铺实体类（用于Feign调用）
 */
@Data
public class Shop {
    private Long id;
    private Long userId;
    private String shopName;
    private String shopLogo;
    private String shopDesc;
    private String contactPhone;
    private Integer status;
    private String auditRemark;
}
