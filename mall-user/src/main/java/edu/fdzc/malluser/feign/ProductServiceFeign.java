package edu.fdzc.malluser.feign;

import edu.fdzc.mallcommon.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 商品服务Feign客户端
 */
@FeignClient(name = "mall-product-service")
public interface ProductServiceFeign {

    /**
     * 获取商品总数
     */
    @GetMapping("/product/count")
    Result<Long> getProductCount();
}
