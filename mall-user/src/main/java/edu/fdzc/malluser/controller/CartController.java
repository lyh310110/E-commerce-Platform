package edu.fdzc.malluser.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.malluser.entity.Cart;
import edu.fdzc.malluser.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    // 访问路径 /cart/list?userId=XXXX
    @GetMapping("/list")
    public Result<List<Cart>> getCartByUserId(@RequestParam Long userId){
        List<Cart> cartList = cartService.getCartByUserId(userId);
        return Result.success(cartList);
    }

    // 添加购物车 post /cart/add
    @PostMapping("/add")
    public Result<Boolean> addCart(@RequestBody Cart cart){
        boolean success = cartService.addCart(cart);
        return success?Result.success():Result.error("添加失败");
    }

    // 删除购物车 DELETE /cart/{id}
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCart(@PathVariable(required = true) String id){
        logger.info("接收到删除购物车请求，id: {}", id);
        boolean success = cartService.deleteCart(id);
        return success?Result.success():Result.error("删除失败");
    }
}
