package edu.fdzc.mallorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.fdzc.mallorder.entity.Address;
import edu.fdzc.mallorder.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;

    /**
     * 获取用户收货地址列表
     */
    public List<Address> getUserAddresses(Long userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("is_default")
                .orderByAsc("create_time");
        return addressMapper.selectList(queryWrapper);
    }

    /**
     * 获取默认收货地址
     */
    public Address getDefaultAddress(Long userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("is_default", 1);
        return addressMapper.selectOne(queryWrapper);
    }

    /**
     * 添加收货地址
     */
    public boolean addAddress(Address address) {
        // 如果是默认地址，先将其他地址设置为非默认
        if (address.getIsDefault() == 1) {
            clearDefaultAddress(address.getUserId());
        }

        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insert(address) > 0;
    }

    /**
     * 更新收货地址
     */
    public boolean updateAddress(Address address) {
        if (address.getId() == null) {
            return false;
        }

        // 如果是默认地址，先将其他地址设置为非默认
        if (address.getIsDefault() == 1) {
            clearDefaultAddress(address.getUserId());
        }

        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateById(address) > 0;
    }

    /**
     * 删除收货地址
     */
    public boolean deleteAddress(Long id, Long userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("user_id", userId);
        return addressMapper.delete(queryWrapper) > 0;
    }

    /**
     * 清除用户的默认地址
     */
    private void clearDefaultAddress(Long userId) {
        UpdateWrapper<Address> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                .eq("is_default", 1)
                .set("is_default", 0);
        addressMapper.update(null, updateWrapper);
    }
}