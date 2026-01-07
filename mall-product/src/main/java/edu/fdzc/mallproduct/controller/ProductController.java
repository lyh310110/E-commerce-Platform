package edu.fdzc.mallproduct.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallproduct.entity.Product;
import edu.fdzc.mallproduct.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> getAllProducts(@RequestParam(required = false) String sort){
        List<Product> products = productService.getAllProducts(sort);
        return Result.success(products);
    }

    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        return product == null ? Result.error("商品不存在") : Result.success(product);
    }
}
