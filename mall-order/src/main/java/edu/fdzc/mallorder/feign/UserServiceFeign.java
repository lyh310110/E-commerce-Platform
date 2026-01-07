package edu.fdzc.mallorder.feign;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallorder.entity.Shop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "mall-user", path = "/shop")
public interface UserServiceFeign {

    /**
     * 根据用户ID获取店铺信息
     */
    @GetMapping("/info")
    Result<Shop> getShopInfo(@RequestParam("userId") Long userId);
}
