package edu.fdzc.mallorder.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallorder.entity.Address;
import edu.fdzc.mallorder.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 获取用户收货地址列表
     */
    @GetMapping("/list")
    public Result<List<Address>> getUserAddresses(@RequestParam Long userId) {
        List<Address> addresses = addressService.getUserAddresses(userId);
        return Result.success(addresses);
    }

    /**
     * 获取默认收货地址
     */
    @GetMapping("/default")
    public Result<Address> getDefaultAddress(@RequestParam Long userId) {
        Address address = addressService.getDefaultAddress(userId);
        return address != null ? Result.success(address) : Result.error("未设置默认地址");
    }

    /**
     * 添加收货地址
     */
    @PostMapping("/add")
    public Result<Boolean> addAddress(@RequestBody Address address) {
        boolean success = addressService.addAddress(address);
        return success ? Result.success() : Result.error("添加地址失败");
    }

    /**
     * 更新收货地址
     */
    @PutMapping("/update")
    public Result<Boolean> updateAddress(@RequestBody Address address) {
        boolean success = addressService.updateAddress(address);
        return success ? Result.success() : Result.error("更新地址失败");
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteAddress(@PathVariable Long id,
                                         @RequestParam Long userId) {
        boolean success = addressService.deleteAddress(id, userId);
        return success ? Result.success() : Result.error("删除地址失败");
    }
}