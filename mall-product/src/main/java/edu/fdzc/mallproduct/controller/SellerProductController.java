package edu.fdzc.mallproduct.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallproduct.entity.Product;
import edu.fdzc.mallproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;

    /**
     * 发布商品
     */
    @PostMapping("/publish")
    public Result<Boolean> publishProduct(@RequestBody Product product) {
        boolean success = productService.publishProduct(product);
        return success ? Result.success() : Result.error("发布失败");
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public Result<Boolean> updateProduct(@RequestBody Product product) {
        boolean success = productService.updateProduct(product);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 下架商品
     */
    @PostMapping("/off-shelf/{id}")
    public Result<Boolean> offShelfProduct(@PathVariable Long id,
                                           @RequestParam Long shopId) {
        boolean success = productService.offShelfProduct(id, shopId);
        return success ? Result.success() : Result.error("下架失败");
    }

    /**
     * 获取店铺商品列表
     */
    @GetMapping("/list")
    public Result<List<Product>> getShopProducts(@RequestParam Long shopId,
                                                 @RequestParam(required = false) Integer status) {
        List<Product> products = productService.getShopProducts(shopId, status);
        return Result.success(products);
    }
}